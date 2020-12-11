package com.example.cartoapp.ui.InvoiceDetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartoapp.R;
import com.example.cartoapp.database.Entities.InvoiceDetailEntity;

import java.util.List;

public class InvoiceDetailAdapter extends RecyclerView.Adapter<InvoiceDetailAdapter.InvoiceDetailViewHolder> {

    private List<InvoiceDetailEntity> elements;
    private Listener listener;

    public InvoiceDetailAdapter(List<InvoiceDetailEntity> elements, Fragment context) {
        this.elements = elements;
        if (context instanceof InvoiceDetailAdapter.Listener){
            this.listener = (InvoiceDetailAdapter.Listener) context;
        }
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
        holder.txtProduct.setText(elements.get(position).getProduct());
        holder.txtUnit.setText(elements.get(position).getUnit());
        holder.txtCost.setText(String.valueOf(elements.get(position).getCost()));
        holder.txtQuantity.setText(String.valueOf(elements.get(position).getQuantity()));
        holder.txtTotalCostOfItem.setText(String.valueOf(elements.get(position).getTotalCostOfItem()));
        holder.imgFileAttached.setVisibility(elements.get(position).getPathToFile() == null ? View.INVISIBLE : View.VISIBLE);

        holder.imgDeleteInvoiceDetail.setOnClickListener((v) -> {
                listener.deleteInvoiceDetail(elements.get(position));
            });
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public static class InvoiceDetailViewHolder extends RecyclerView.ViewHolder {
        public TextView txtProduct;
        public TextView txtUnit;
        public TextView txtCost;
        public TextView txtQuantity;
        public TextView txtTotalCostOfItem;
        public ImageView imgFileAttached;
        public ImageView imgDeleteInvoiceDetail;

        public InvoiceDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProduct = itemView.findViewById(R.id.txtProduct);
            txtUnit = itemView.findViewById(R.id.txtUnit);
            txtCost = itemView.findViewById(R.id.txtCost);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtTotalCostOfItem = itemView.findViewById(R.id.txtTotalCostOfItem);
            imgFileAttached = itemView.findViewById(R.id.imgFileAttached);
            imgDeleteInvoiceDetail = itemView.findViewById(R.id.imgDeleteInvoiceDetail);
        }
    }

    public interface Listener{
        void deleteInvoiceDetail(InvoiceDetailEntity invoiceDetailEntity);
    }
}
