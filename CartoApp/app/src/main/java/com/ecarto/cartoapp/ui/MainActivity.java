package com.ecarto.cartoapp.ui;

import static androidx.core.content.FileProvider.getUriForFile;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import com.ecarto.cartoapp.BuildConfig;
import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.ViewModels.MainActivityViewModel;
import com.ecarto.cartoapp.database.DatabaseClass;
import com.ecarto.cartoapp.database.Entities.FileEntity;
import com.ecarto.cartoapp.database.Entities.ProjectEntity;
import com.ecarto.cartoapp.database.Repositories.FileRepository;
import com.ecarto.cartoapp.database.Repositories.InvoiceRepository;
import com.ecarto.cartoapp.database.Repositories.ProjectRepository;
import com.ecarto.cartoapp.database.Repositories.UserRepository;
import com.ecarto.cartoapp.databinding.ActivityMainBinding;
import com.ecarto.cartoapp.ui.Files.AddFileF;
import com.ecarto.cartoapp.ui.Invoice.DownloadDataF;
import com.ecarto.cartoapp.utils.FileManipUtils;
import com.ecarto.cartoapp.utils.StringUtils;
import com.ecarto.cartoapp.web.DTOs.ProjectDTO;

import android.os.FileUtils;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.room.Database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    public static String TAG = "MAIN_ACTIVITY";
    public static String PDFS_FOLDER_PATH;

    ActivityMainBinding binding;
    SharedPreferences sharedPreferences;
    InvoiceRepository invoiceRepository;
    ProjectRepository projectRepository;
    UserRepository userRepository;
    FileRepository fileRepository;
    MainActivityViewModel mainActivityViewModel;


    //TODO: get invoices from mails
    //TODO: upload files too to webservices
    //TODO: make the background of each 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);

        invoiceRepository = new InvoiceRepository(getApplicationContext());
        fileRepository = new FileRepository(getApplicationContext());
        projectRepository = new ProjectRepository(getApplicationContext());
        userRepository = new UserRepository(getApplicationContext());
        PDFS_FOLDER_PATH = getApplicationContext().getFilesDir().getPath() + File.separator + getString(R.string.pdfs_folder_name);

        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mainActivityViewModel.getOnFilesBeingInserted().observe(this, isInserted -> {
            binding.imgInstructionsFiles.setVisibility(isInserted ? View.VISIBLE : View.INVISIBLE);
        });

        //receiving data from share button
        handleShareIntent();

        binding.imgBackup.setOnClickListener((v) -> {
            DatabaseClass.getDatabase(this).close();
            DatabaseClass.setnull();
            String originalURI = DatabaseClass.getDbPath(getApplicationContext());
            Toast.makeText(this, originalURI, Toast.LENGTH_LONG).show();

            File imagePath = new File(getApplicationContext().getFilesDir(), "carto_files");
            File newFile = new File(imagePath, "database.carto.sqlite");

            FileManipUtils.copyFile(originalURI, newFile.getPath());
            Uri contentUri = getUriForFile(getApplicationContext(),
                    BuildConfig.APPLICATION_ID + ".fileprovider", newFile);

            //PDFS_FOLDER_PATH
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.setType("text/plain");

            PackageManager pm = getPackageManager();
            List<ResolveInfo> resInfo = pm.queryIntentActivities(shareIntent, 0);
            List<ComponentName> toRemove = new ArrayList<ComponentName>();

            for (ResolveInfo info : resInfo) {
                String packageName = info.activityInfo.packageName;
                if (packageName.equals(BuildConfig.APPLICATION_ID)) {
                    toRemove.add(new ComponentName(packageName, info.activityInfo.name));
                }
            }
            resInfo.removeAll(toRemove);

            Intent chooserIntent = Intent.createChooser(shareIntent, "Share images to...");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                    resInfo.toArray(new Parcelable[resInfo.size()]));

            startActivity(Intent.createChooser(shareIntent, "Share images to..."));
        });

        /*
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(null);
        
         */


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
        FileManipUtils.createFolderIfNecessary(PDFS_FOLDER_PATH);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            Uri uri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true)
                    .setTitle("Migrar informacion")
                    .setMessage("¿Seguro que desea migrar toda la informacion de aqui? la base de datos previa sera removida")
                    .setPositiveButton("Migrar datos", (dialog, which) -> {
                        try {
                            String pathDb = DatabaseClass.getDbPath(this);

                            DatabaseClass.delete(this);
                            Toast.makeText(this, "This is a database file.", Toast.LENGTH_LONG).show();
                            FileManipUtils.copyFileFromUriToPath(this, uri, pathDb);
                        } catch (Exception e) {
                            Toast.makeText(this, "Didn't work" + uri.getPath(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("Cancelar", ((dialog, which) -> {
                    })).create().show();
            /*
            // I removed this, I want to simplify migration. We don't really need this.
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
             */
        }
    }

    private void popUpFileFragment() {
        navigateToLowerFragment(AddFileF.newInstance(null), false, "Test");
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
            Long newFileID = StringUtils.getUniqueID();
            String originalName = FileManipUtils.getFileDetailFromUri(this, imageUri);
            String fileType = originalName.substring(originalName.lastIndexOf(".") + 1);
            String nameOfNewFile = newFileID + "_file";
            String newFileLocation = PDFS_FOLDER_PATH + File.separator + nameOfNewFile + "." + fileType;

            FileEntity fileEntity = new FileEntity();
            fileEntity.setFileID(newFileID);
            fileEntity.setTypeOfFile(fileType);
            fileEntity.setPathToFile(newFileLocation);
            fileEntity.setOriginalName(originalName);
            fileRepository.insertFileEntity(fileEntity).subscribeOn(Schedulers.io()).blockingGet();

            FileManipUtils.copyFileFromUri(this, imageUri, newFileLocation);
        }
    }
}