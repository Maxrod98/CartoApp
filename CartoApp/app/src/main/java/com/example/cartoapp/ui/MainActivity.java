package com.example.cartoapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.example.cartoapp.R;
import com.example.cartoapp.database.Entities.FileEntity;
import com.example.cartoapp.database.Repositories.FileRepository;
import com.example.cartoapp.database.Repositories.InvoiceRepository;
import com.example.cartoapp.databinding.ActivityMainBinding;
import com.example.cartoapp.ui.FileList.AddFileFragment;
import com.example.cartoapp.ui.InvoiceDetail.InvoiceDetailFragment;
import com.example.cartoapp.ui.Invoice.InvoiceFragment;
import com.example.cartoapp.utils.FileUtils;
import com.example.cartoapp.utils.NAVIGATION;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements
        InvoiceFragment.Listener, InvoiceDetailFragment.Listener, AddFileFragment.Listener {
    //Tod: think about a web copy where everything is saved daily so that we dont have to worry about migrations
    //TODO: add file view fragment
    //TODO: add header for detail of the invoice
    //TODO: use bigdecimals instead of doubles
    //TODO: receive image and pdf files from intent

    ActivityMainBinding binding;
    public static Integer navigation = NAVIGATION.INVOICE_LISTING;
    public static String PDFS_FOLDER_NAME = "carto_files";
    private String PDFS_FOLDER_PATH;
    SharedPreferences sharedPreferences;
    TextView toolText;
    InvoiceRepository invoiceRepository;
    FileRepository fileRepository;

    //ACTIVITY DEFAULT METHODS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);

        invoiceRepository = new InvoiceRepository(getApplicationContext());
        fileRepository = new FileRepository(getApplicationContext());

        //receiving data from share button
        handleShareIntent();


        setSupportActionBar(binding.toolbar);

        setNavigation(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setNavigation(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.unattached_files){
            popUpFileFragment();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //receives data from SHARE button
    private void handleShareIntent() {
        PDFS_FOLDER_PATH = getApplicationContext().getFilesDir().getPath() + File.separator + PDFS_FOLDER_NAME;
        FileUtils.createFolderIfNecessary(PDFS_FOLDER_PATH);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {

            //pf.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            popUpFileFragment();

            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/png") ) { //handles images
                handleSendImage(intent); // Handle single image being sent
            } else if (type.startsWith("image/jpeg")) {
                handleSendImage(intent);
            } else if (type.startsWith("application/pdf")) {
                handleSendImage(intent);
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {

            popUpFileFragment();
            if (type.startsWith("image/png")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            } else if (type.startsWith("image/jpeg")){
                handleSendMultipleImages(intent);
            } else if (type.startsWith("application/pdf")){
                handleSendMultipleImages(intent);
            }
        }
        else {
            // Handle other intents, such as being started from the home screen
        }
    }

    private void popUpFileFragment() {
        navigateToLowerFragment(AddFileFragment.newInstance(this), false, "Test");
    }

    public void setNavigation(boolean addToBackstack) {
        switch (navigation) {
            case (NAVIGATION.INVOICE_LISTING):
                getSupportActionBar().setTitle("Listado de Facturas");
                navigateTo(InvoiceFragment.newInstance(this), addToBackstack, "INVOICE_LISTING");
                break;

            case (NAVIGATION.INVOICE_DETAIL_LISTING):
                getSupportActionBar().setTitle("Detalle");
                navigateTo(InvoiceDetailFragment.newInstance(this), addToBackstack, "INVOICE_DETAIL_LISTING");
                break;
        }
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        insertFileEntity(imageUri);
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            for (Uri imageUri : imageUris){
                insertFileEntity(imageUri);
            }
        }
    }

    void insertFileEntity(Uri imageUri){
        if (imageUri != null) {

            Integer newFileID;
            try {
                FileEntity fileEntity = fileRepository.findLastFileEntity().subscribeOn(Schedulers.io()).blockingGet();
                newFileID = fileEntity.getFileID() + 1;
            } catch (Exception e){
                newFileID = 1;
            }

            String originalName = FileUtils.getFileDetailFromUri(this, imageUri);
            String fileType = originalName.substring(originalName.lastIndexOf(".") + 1, originalName.length());
            String nameOfNewFile = String.valueOf(newFileID) + "_file";
            String newFileLocation = PDFS_FOLDER_PATH + File.separator + nameOfNewFile + "." + fileType;

            FileEntity fileEntity = new FileEntity();
            fileEntity.setTypeOfFile(fileType);
            fileEntity.setPathToFile(newFileLocation);
            fileEntity.setOriginalName(originalName);
            fileRepository.insertFileEntity(fileEntity).subscribeOn(Schedulers.io()).blockingGet();

            FileUtils.copyFileFromUri(this, imageUri, newFileLocation);
        }
    }

    //COMMUNICATION METHODS
    @Override
    public void goToInvoiceDetails() {
        navigation = NAVIGATION.INVOICE_DETAIL_LISTING;
        setNavigation(true);
    }


    public void navigateToLowerFragment(Fragment fragment, boolean addToBackStack, String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack((addToBackStack) ? fragmentTag : null)
                .replace(R.id.insertFileContainer, fragment, fragmentTag)
                .commit();
    }

    @Override
    public void addFilesToInvoiceDetail(List<FileEntity> fileEntities) {
        Integer asda = 2;
    }
}