package com.example.cartoapp.ui.InvoiceMain;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartoapp.R;
import com.example.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.example.cartoapp.database.Entities.InvoiceEntity;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder> {
    List<ExtendedInvoiceEntity> elements;
    InvoiceAdapter.Listener listener;


    public InvoiceAdapter(List<ExtendedInvoiceEntity> invoiceEntityList, Fragment context) {
        elements = invoiceEntityList;
        if (context instanceof InvoiceAdapter.Listener) {
            listener = (InvoiceAdapter.Listener) context;
        }
    }

    @NonNull
    @Override
    public InvoiceAdapter.InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invoice_entity_item, parent, false);

        return new InvoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceAdapter.InvoiceViewHolder holder, int position) {
        holder.txtDate.setText((new SimpleDateFormat("dd/MM")).format(new Date(elements.get(position).getDate())));
        holder.txtDescription.setText(elements.get(position).getDescription());
        holder.txtSeller.setText(elements.get(position).getSeller());
        holder.txtTotalCost.setText( "$" + String.valueOf(elements.get(position).getTotalCost()));
        holder.imgEditInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.goToInvoiceDetail(elements.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public static class InvoiceViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDate;
        public TextView txtDescription;
        public TextView txtSeller;
        public TextView txtTotalCost;
        public ImageView imgEditInvoice;

        public InvoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtSeller = itemView.findViewById(R.id.txtSeller);
            txtTotalCost = itemView.findViewById(R.id.txtTotalCost);
            imgEditInvoice = itemView.findViewById(R.id.imgEditInvoice);
        }
    }

    public interface Listener {
        void navigateToInvoiceDetail();
        void goToInvoiceDetail(InvoiceEntity invoiceEntity);
    }
}


