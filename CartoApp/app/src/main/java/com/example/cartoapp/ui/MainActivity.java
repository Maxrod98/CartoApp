package com.example.cartoapp.ui;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.cartoapp.R;
import com.example.cartoapp.database.Repositories.InvoiceRepository;
import com.example.cartoapp.databinding.ActivityMainBinding;
import com.example.cartoapp.ui.InvoiceDetail.InvoiceDetailFragment;
import com.example.cartoapp.ui.InvoiceMain.InvoiceFragment;
import com.example.cartoapp.utils.NAVIGATION;

import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends BaseActivity implements
        InvoiceFragment.Listener, InvoiceDetailFragment.Listener {
    //Tod: think about a web copy where everything is saved daily so that we dont have to worry about migrations
    //Tod: delete invoice detail when a invoiceentity is deleted
    ActivityMainBinding binding;
    public static Integer navigation = NAVIGATION.INVOICE_LISTING;
    SharedPreferences sharedPreferences;
    TextView toolText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences(getString(R.string.sharedPreferences), MODE_PRIVATE);

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

    @Override
    public void goToInvoiceDetails() {
        navigation = NAVIGATION.INVOICE_DETAIL_LISTING;
        setNavigation(true);
    }

}