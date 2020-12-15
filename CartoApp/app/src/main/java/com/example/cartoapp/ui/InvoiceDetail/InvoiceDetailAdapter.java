package com.example.cartoapp.ui.InvoiceDetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartoapp.R;
import com.example.cartoapp.database.Entities.ExtendedInvoiceDetailEntity;
import com.example.cartoapp.database.Entities.InvoiceDetailEntity;
import com.example.cartoapp.utils.Selector;

import java.math.BigDecimal;
import java.util.List;

public class InvoiceDetailAdapter extends RecyclerView.Adapter<InvoiceDetailAdapter.InvoiceDetailViewHolder> implements Selector.Listener{

    private List<ExtendedInvoiceDetailEntity> elements;
    private Listener listener;
    Selector selector;

    public InvoiceDetailAdapter(List<ExtendedInvoiceDetailEntity> elements, Object context) {
        this.elements = elements;
        if (context instanceof InvoiceDetailAdapter.Listener){
            this.listener = (InvoiceDetailAdapter.Listener) context;
        }
        selector = new Selector(this);
    }

    @NonNull
    @Override
    public InvoiceDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invoice_detail_item, parent, false);

        return new InvoiceDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceDetailViewHolder holder, int position) {
        holder.txtProduct.setText(elements.get(position).getConceptDescription());
        holder.txtQuantity.setText( "$" + String.valueOf(elements.get(position).getCostOfItem()));

        boolean hasFiles = elements.get(position).getNumFiles() > 0;
        if (!hasFiles){
            holder.imgFileAttached.setAlpha(0.3f);
        }
        holder.invoiceDetailItem.setOnClickListener((v) -> {
            selector.onItemClickSelection(position);
            listener.goToInvoiceDetailOptionsDialog(elements.get(position));
            notifyDataSetChanged();
        });

        holder.invoiceDetailItemSelector.setVisibility(selector.isSelected(position)? View.VISIBLE: View.INVISIBLE);
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

    public static class InvoiceDetailViewHolder extends RecyclerView.ViewHolder {
        public TextView txtProduct;
        public TextView txtQuantity;
        public ImageView imgFileAttached;
        public ConstraintLayout invoiceDetailItem;
        public View invoiceDetailItemSelector;

        public InvoiceDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProduct = itemView.findViewById(R.id.txtProduct);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            imgFileAttached = itemView.findViewById(R.id.imgFileAttached);
            invoiceDetailItem = itemView.findViewById(R.id.invoice_detail_item);
            invoiceDetailItemSelector = itemView.findViewById(R.id.invoice_detail_item_selector);
        }
    }

    public interface Listener{
        Integer getCurrentSelection();
        void setCurrentSelection(Integer integer);
        void goToInvoiceDetailOptionsDialog(InvoiceDetailEntity invoiceDetailEntity);
    }
}
