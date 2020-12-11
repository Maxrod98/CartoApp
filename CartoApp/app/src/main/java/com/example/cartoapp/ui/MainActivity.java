package com.example.cartoapp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.cartoapp.R;
import com.example.cartoapp.database.Entities.InvoiceDetailEntity;
import com.example.cartoapp.database.Entities.InvoiceEntity;
import com.example.cartoapp.database.Repositories.InvoiceRepository;
import com.example.cartoapp.databinding.ActivityMainBinding;
import com.example.cartoapp.ui.InsertFragments.InsertInvoiceDetailDialog;
import com.example.cartoapp.ui.InsertFragments.InsertInvoiceEntityDialog;
import com.example.cartoapp.ui.InvoiceDetail.InvoiceDetailFragment;
import com.example.cartoapp.ui.InvoiceMain.InvoiceFragment;
import com.example.cartoapp.utils.NAVIGATION;

import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;

import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements InsertInvoiceEntityDialog.Listener,
        InvoiceFragment.Listener, InsertInvoiceDetailDialog.Listener, InvoiceDetailFragment.Listener {


    //TODO: think about a web copy

    ActivityMainBinding binding;
    InvoiceFragment invoiceFragment;
    InvoiceRepository invoiceRepository;
    public static Integer navigation = NAVIGATION.INVOICE_LISTING;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);

        invoiceRepository = new InvoiceRepository(getApplication());
        setNavigation();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setNavigation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void setNavigation() {
        switch (navigation) {
            case (NAVIGATION.INVOICE_LISTING):
                insertInvoiceDetailClickListener();
                navigateTo(InvoiceFragment.newInstance(this));
                break;


            case (NAVIGATION.INVOICE_DETAIL_LISTING):
                insertInvoiceClickListener();
                navigateTo(InvoiceDetailFragment.newInstance(this));

                break;

            case (NAVIGATION.REFRESH_DETAIL_LISTING_NO_BACKSTACK):
                insertInvoiceClickListener();
                getSupportFragmentManager().popBackStack();
                navigateTo(InvoiceDetailFragment.newInstance(this), false, "Detail");
                break;

            case (NAVIGATION.INVOICE_WAS_JUST_INSERTED):
                insertInvoiceClickListener();
                navigateTo(InvoiceDetailFragment.newInstance(this));
                InvoiceEntity invoiceEntity = invoiceRepository.findLastInvoiceEntity().subscribeOn(Schedulers.io()).blockingGet();
                sharedPreferences.edit().putInt(getString(R.string.selectedInvoiceEntityID), invoiceEntity.getInvoiceID()).commit();

        }
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

    public void insertInvoiceClickListener(){
        binding.fab.setOnClickListener((v) -> {
            InsertInvoiceDetailDialog insertInvoiceDetailDialog = InsertInvoiceDetailDialog.newInstance(MainActivity.this);
            insertInvoiceDetailDialog.show(getSupportFragmentManager(), "InsertInvoiceDetailDialog");
        });
    }

    public void insertInvoiceDetailClickListener(){
        binding.fab.setOnClickListener((v) -> {
            InsertInvoiceEntityDialog insertInvoiceEntityDialog = InsertInvoiceEntityDialog.newInstance(MainActivity.this);
            insertInvoiceEntityDialog.show(getSupportFragmentManager(), "InsertInvoiceEntityDialog");
        });
    }

    @Override
    public void invoiceEntityWasInserted() {
        navigation = NAVIGATION.INVOICE_WAS_JUST_INSERTED;
        setNavigation();
    }

    @Override
    public void goToInvoiceDetailFragmentToActivity(InvoiceEntity invoiceEntity) {
        navigation = NAVIGATION.INVOICE_DETAIL_LISTING;
        setNavigation();
    }

    @Override
    public void insertInvoiceDetail(InvoiceDetailEntity invoiceDetailEntity) {
        invoiceRepository.insert(invoiceDetailEntity).subscribeOn(Schedulers.io()).blockingGet();
        navigation = NAVIGATION.REFRESH_DETAIL_LISTING_NO_BACKSTACK;
        setNavigation();
    }
}