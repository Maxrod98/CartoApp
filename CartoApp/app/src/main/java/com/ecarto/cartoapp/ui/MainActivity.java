package com.ecarto.cartoapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.FileEntity;
import com.ecarto.cartoapp.database.Repositories.FileRepository;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.databinding.ActivityMainBinding;
import com.ecarto.cartoapp.ui.ShareFiles.AddFileFragment;
import com.ecarto.cartoapp.ui.InvoiceDetail.InvoiceDetailFragment;
import com.ecarto.cartoapp.ui.Invoice.InvoiceFragment;
import com.ecarto.cartoapp.utils.FileUtils;
import com.ecarto.cartoapp.utils.NAVIGATION;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements
        InvoiceFragment.Listener, InvoiceDetailFragment.Listener, AddFileFragment.Listener {

    public static String TAG = "MAIN_ACTIVITY";
    public static Integer navigation = NAVIGATION.INVOICE_LISTING;
    public static String PDFS_FOLDER_NAME = "carto_files";
    public static String PDFS_FOLDER_PATH;

    ActivityMainBinding binding;
    SharedPreferences sharedPreferences;
    TextView toolText;
    InvoiceRepository invoiceRepository;
    FileRepository fileRepository;

    //Tod: think about a web copy where everything is saved daily so that we dont have to worry about migrations
    //TODO: add file view fragment
    //TODO: add header for detail of the invoice
    //TODO: use bigdecimals instead of doubles for latitude and longitude
    //TODO: make list of folders
    //TODO: communication between files fragment

    //TODO: SafeArgs plugin

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

        setNavigation(false);
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
        } else if (id == R.id.unattached_files) {
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
            popUpFileFragment();
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else { //handles images
                handleSendFile(intent); // Handle single file being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            popUpFileFragment();
            handleSendMultipleFiles(intent); // Handle multiple images being sent
        } else {
            // Handle other intents, such as being started from the home screen
        }
    }

    private void popUpFileFragment() {
        navigation = NAVIGATION.INVOICE_LISTING;
        navigateToLowerFragment(AddFileFragment.newInstance(null), false, "Test");
    }

    public void setNavigation(boolean addToBackstack) {
        switch (navigation) {
            case (NAVIGATION.INVOICE_LISTING):
                getSupportActionBar().setTitle("Listado de Facturas");
                navigateTo(InvoiceFragment.newInstance(null), addToBackstack, InvoiceFragment.TAG);
                break;

            case (NAVIGATION.INVOICE_DETAIL_LISTING):
                getSupportActionBar().setTitle("Detalle");
                navigateTo(InvoiceDetailFragment.newInstance(null), addToBackstack, InvoiceDetailFragment.TAG);
                break;
        }
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
        }
    }

    void handleSendFile(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        insertFileEntity(imageUri);
    }

    void handleSendMultipleFiles(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            for (Uri imageUri : imageUris) {
                insertFileEntity(imageUri);
            }
        }
    }

    void insertFileEntity(Uri imageUri) {
        if (imageUri != null) {

            Integer newFileID;
            try {
                FileEntity fileEntity = fileRepository.findLastFileEntity().subscribeOn(Schedulers.io()).blockingGet();
                newFileID = fileEntity.getFileID() + 1;
            } catch (Exception e) {
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

    @Override
    public void addFilesToInvoiceDetail(List<FileEntity> fileEntities) {
        Integer asda = 2;
    }
}