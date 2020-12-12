package com.example.cartoapp.ui.InvoiceDetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cartoapp.R;
import com.example.cartoapp.database.Entities.ExtendedInvoiceDetailEntity;
import com.example.cartoapp.database.Entities.InvoiceDetailEntity;

import java.util.List;

public class InvoiceDetailAdapter extends RecyclerView.Adapter<InvoiceDetailAdapter.InvoiceDetailViewHolder> {

    private List<ExtendedInvoiceDetailEntity> elements;
    private Listener listener;

    public InvoiceDetailAdapter(List<ExtendedInvoiceDetailEntity> elements, Object context) {
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
        holder.txtQuantity.setText( "$" + String.valueOf(elements.get(position).getQuantity()));
        holder.imgDeleteInvoiceDetail.setOnClickListener((v) -> {
                listener.deleteInvoiceDetail(elements.get(position));
            });

        //TODO: en vez de no mostrar mejor setEnabled
        boolean hasFiles = elements.get(position).getNumFiles() > 0;
        if (!hasFiles){
            holder.imgFileAttached.setAlpha(0.3f);
        }


        holder.imgEditInvoiceDetail.setOnClickListener((v) -> {
            listener.editInvoiceDetail(elements.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public static class InvoiceDetailViewHolder extends RecyclerView.ViewHolder {
        public TextView txtProduct;
        public TextView txtQuantity;
        public ImageView imgFileAttached;
        public ImageView imgDeleteInvoiceDetail;
        public ImageView imgEditInvoiceDetail;

        public InvoiceDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            txtProduct = itemView.findViewById(R.id.txtProduct);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            imgFileAttached = itemView.findViewById(R.id.imgFileAttached);
            imgDeleteInvoiceDetail = itemView.findViewById(R.id.imgDeleteInvoiceDetail);
            imgEditInvoiceDetail = itemView.findViewById(R.id.imgEditInvoiceDetail);
        }
    }

    public interface Listener{
        void deleteInvoiceDetail(InvoiceDetailEntity invoiceDetailEntity);
        void checkFileList(InvoiceDetailEntity invoiceDetailEntity);
        void editInvoiceDetail(InvoiceDetailEntity invoiceDetailEntity);
    }
}
