package com.example.cartoapp.ui.Invoice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartoapp.R;
import com.example.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.example.cartoapp.database.Entities.InvoiceEntity;
import com.example.cartoapp.utils.Selector;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder> implements Selector.Listener {
    List<ExtendedInvoiceEntity> elements;
    InvoiceAdapter.Listener listener;
    //TODO : add option to edit invoice

    Selector selector;


    public InvoiceAdapter(List<ExtendedInvoiceEntity> invoiceEntityList, Object context) {
        elements = invoiceEntityList;
        if (context instanceof InvoiceAdapter.Listener) {
            listener = (InvoiceAdapter.Listener) context;
            selector = new Selector(this);
        } else {

        }

    }

    @NonNull
    @Override
    public InvoiceAdapter.InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invoice_item, parent, false);

        return new InvoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceAdapter.InvoiceViewHolder holder, int position) {
        holder.txtDate.setText((new SimpleDateFormat("dd/MM")).format(new Date(elements.get(position).getDate())));
        holder.txtDescription.setText(elements.get(position).getDescription());
        holder.txtSeller.setText(elements.get(position).getSeller());
        holder.txtTotalCost.setText("$" + String.valueOf(elements.get(position).getTotalCost() == null ? 0 : elements.get(position).getTotalCost()));

        holder.txtSeller.getRootView().setOnClickListener((v) -> {
            selector.onItemClickSelection(position);
            notifyDataSetChanged();
            listener.goToInvoiceDetail(elements.get(position));
        });
        holder.vSelectionBar.setVisibility(selector.isSelected(position) ? View.VISIBLE : View.INVISIBLE);
        holder.imgDeleteInvoice.setVisibility(View.VISIBLE);

        holder.imgDeleteInvoice.setOnClickListener((v -> {
            listener.deleteInvoice(elements.get(position));
        }));
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    @Override
    public void setSelectedPosition(Integer position) {
        listener.setCurrentSelection(position);
    }

    @Override
    public Integer getSelectedPosition() {
        return listener.getCurrentSelection();
    }

    @Override
    public Integer getNumElems() {
        return elements.size();
    }


    public static class InvoiceViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDate;
        public TextView txtDescription;
        public TextView txtSeller;
        public TextView txtTotalCost;
        public ImageView imgDeleteInvoice;
        public View vSelectionBar;

        public InvoiceViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtDescription = itemView.findViewById(R.id.txtDescription);
            txtSeller = itemView.findViewById(R.id.txtSeller);
            txtTotalCost = itemView.findViewById(R.id.txtTotalCost);
            imgDeleteInvoice = itemView.findViewById(R.id.imgDeleteInvoice);
            vSelectionBar = itemView.findViewById(R.id.vSelectorBar);
        }
    }

    public interface Listener {
        void goToInvoiceDetail(InvoiceEntity invoiceEntity);

        void deleteInvoice(InvoiceEntity invoiceEntity);

        Integer getCurrentSelection();

        void setCurrentSelection(Integer position);
    }
}


