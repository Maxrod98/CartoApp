package com.ecarto.cartoapp.ui.Invoice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceEntity;
import com.ecarto.cartoapp.utils.Selector;
import com.ecarto.cartoapp.utils.StringUtils;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.InvoiceViewHolder> implements Selector.Listener {
    public static final Integer MAX_SIZE_SELLER = 20;
    public static final Integer MAX_SIZE_DESCRIPTION = 20;

    List<ExtendedInvoiceEntity> elements;
    InvoiceAdapter.Listener listener;
    Selector selector;

    public InvoiceAdapter(List<ExtendedInvoiceEntity> invoiceEntityList, Object context) {
        elements = invoiceEntityList;
        if (context instanceof InvoiceAdapter.Listener) {
            listener = (InvoiceAdapter.Listener) context;
            selector = new Selector(this);
        } else {
            throw new RuntimeException("Necesitas implementar el listener del Selector");
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
        //setting data
        holder.vSelectionBar.setVisibility(selector.isSelected(position) ? View.VISIBLE : View.INVISIBLE);
        holder.txtDate.setText((new SimpleDateFormat("dd/MM")).format(new Date(elements.get(position).getDate())));
        holder.txtDescription.setText(StringUtils.validateLength(elements.get(position).getDescription(), MAX_SIZE_DESCRIPTION));
        holder.txtSeller.setText(StringUtils.validateLength(elements.get(position).getSeller(), MAX_SIZE_SELLER));

        holder.txtTotalCost.setText("$" + (elements.get(position).getTotalCost() == null ? 0 : StringUtils.formatMoney(elements.get(position).getTotalCost())));

        holder.txtSeller.getRootView().setOnClickListener((v) -> {
            selector.onItemClickSelection(position);
            notifyDataSetChanged();
            listener.goToInvoiceDetail(elements.get(position));
        });

        holder.txtSeller.getRootView().setOnLongClickListener((v -> {
            selector.onItemClickSelection(position);
            notifyDataSetChanged();
            listener.goToInvoiceOptions(elements.get(position));
            return false;
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
            vSelectionBar = itemView.findViewById(R.id.vSelectorBar);
        }
    }

    public interface Listener {
        void goToInvoiceDetail(InvoiceEntity invoiceEntity);
        Integer getCurrentSelection();
        void setCurrentSelection(Integer position);
        void goToInvoiceOptions(InvoiceEntity invoiceEntity);
    }
}


