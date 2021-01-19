package com.ecarto.cartoapp.ui.MailInvoices;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.web.DTOs.MailInvoiceDTO;

import org.w3c.dom.Text;

import java.util.List;

public class MailnvoicesAdapter extends  RecyclerView.Adapter<MailnvoicesAdapter.MyViewHolder>{

    List<MailInvoiceDTO> elements = null;
    Listener listener = null;

    public MailnvoicesAdapter(List<MailInvoiceDTO> elements, Object listener){
        this.elements = elements;
        this.listener = (Listener) listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mail_invoice, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView Receptor;
        public TextView Fecha;
        public TextView Total;
        public TextView Folio;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


        }
    }

    public interface Listener {

    }
}
