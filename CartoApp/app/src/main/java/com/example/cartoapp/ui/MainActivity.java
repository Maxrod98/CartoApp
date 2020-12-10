package com.example.cartoapp.ui;

import android.os.Bundle;

import com.example.cartoapp.R;
import com.example.cartoapp.database.Entities.InvoiceEntity;
import com.example.cartoapp.database.Repositories.InvoiceRepository;
import com.example.cartoapp.databinding.ActivityMainBinding;
import com.example.cartoapp.ui.InvoiceMain.InsertInvoiceEntityDialog;
import com.example.cartoapp.ui.InvoiceMain.InvoiceListingFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    InvoiceRepository invoiceRepository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        invoiceRepository = new InvoiceRepository(getApplication());
        navigateTo(new InvoiceListingFragment());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsertInvoiceEntityDialog insertInvoiceEntityDialog = new InsertInvoiceEntityDialog();
                insertInvoiceEntityDialog.show(getSupportFragmentManager(), "InsertInvoiceEntityDialog");
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void navigateTo(Fragment fragment) {
        navigateTo(fragment, true, fragment.getClass().getSimpleName());
    }

    public void navigateTo(Fragment fragment, boolean addToBackStack, String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack((addToBackStack) ? fragmentTag : null)
                .replace(R.id.mainActivityFragmentContainer, fragment, fragmentTag)
                .commit();
    }
}