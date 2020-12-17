package com.ecarto.cartoapp.ui.Invoice;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.ExtendedInvoiceEntity;
import com.ecarto.cartoapp.utils.Selector;
import com.ecarto.cartoapp.utils.StringUtils;

import java.util.List;

public class InvoiceA extends RecyclerView.Adapter<InvoiceA.InvoiceViewHolder> implements Selector.Listener {
    public static final Integer MAX_SIZE_SELLER = 20;
    public static final Integer MAX_SIZE_DESCRIPTION = 20;

    List<ExtendedInvoiceEntity> elements;
    InvoiceA.Listener listener;
    Selector selector;

    public InvoiceA(List<ExtendedInvoiceEntity> invoiceEntityList, Object context) {
        elements = invoiceEntityList;
        if (context instanceof InvoiceA.Listener) {
            listener = (InvoiceA.Listener) context;
            selector = new Selector(this);
        } else {
            throw new RuntimeException("Necesitas implementar el listener del Selector");
        }
    }

    @NonNull
    @Override
    public InvoiceA.InvoiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invoice_item, parent, false);

        return new InvoiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvoiceA.InvoiceViewHolder holder, int position) {
        //setting data
        holder.vSelectionBar.setVisibility(selector.isSelected(position) ? View.VISIBLE : View.INVISIBLE);
        holder.txtDate.setText(StringUtils.formatDateFromLong(elements.get(position).getDate()));
        holder.txtDescription.setText(StringUtils.validateLength(elements.get(position).getDescription(), MAX_SIZE_DESCRIPTION));
        holder.txtSeller.setText(StringUtils.validateLength(elements.get(position).getSeller(), MAX_SIZE_SELLER));

        holder.txtTotalCost.setText(StringUtils.formatMoney(elements.get(position).getTotalCost()));

        holder.txtSeller.getRootView().setOnClickListener((v) -> {
            selector.onItemClickSelection(position);
            notifyDataSetChanged();

            NavHostFragment.findNavController((Fragment) listener)
                    .navigate(InvoiceFDirections.actionInvoiceFragmentToInvoiceDetailFragment(elements.get(position).getInvoiceID()));
        });

        holder.txtSeller.getRootView().setOnLongClickListener((v -> {
            selector.onItemClickSelection(position);
            notifyDataSetChanged();

            NavHostFragment.findNavController((Fragment) listener)
                    .navigate(InvoiceFDirections.actionInvoiceFragmentToInvoiceOptionsDialog(elements.get(position).getInvoiceID()));

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
        Integer getCurrentSelection();
        void setCurrentSelection(Integer position);
    }
}


