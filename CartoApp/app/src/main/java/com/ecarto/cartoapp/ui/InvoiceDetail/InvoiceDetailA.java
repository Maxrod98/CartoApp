package com.ecarto.cartoapp.ui.InvoiceDetail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceDetailEntity;
import com.ecarto.cartoapp.database.Entities.InvoiceDetailEntity;
import com.ecarto.cartoapp.utils.Selector;
import com.ecarto.cartoapp.utils.StringUtils;

import java.util.List;

public class InvoiceDetailA extends RecyclerView.Adapter<InvoiceDetailA.InvoiceDetailViewHolder> {
    private List<ExtendedInvoiceDetailEntity> elements;
    private Listener listener;
    Selector selector;

    public InvoiceDetailA(List<ExtendedInvoiceDetailEntity> elements, Object context) {
        this.elements = elements;
        if (context instanceof InvoiceDetailA.Listener) {
            this.listener = (InvoiceDetailA.Listener) context;
        }
        selector = new Selector((Fragment) listener);
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
        holder.txtQuantity.setText(StringUtils.formatMoney(elements.get(position).getCostOfItem()));
        boolean hasFiles = elements.get(position).getNumFiles() > 0;
        holder.imgFileAttached.setAlpha(!hasFiles ? 0.3f : 1.0f);

        holder.invoiceDetailItem.setOnClickListener((v) -> {
            selector.onItemClickSelection(position);
            listener.onInvoiceDetailClick(elements.get(position));
            notifyDataSetChanged();
        });

        holder.invoiceDetailItemSelector.setVisibility(selector.isSelected(position) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
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

    public interface Listener {
        void onInvoiceDetailClick(InvoiceDetailEntity invoiceDetailEntity);
    }
}
