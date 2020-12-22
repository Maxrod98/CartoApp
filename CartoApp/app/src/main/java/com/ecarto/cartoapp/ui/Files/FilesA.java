package com.ecarto.cartoapp.ui.Files;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.FileEntity;

import java.util.List;

public class FilesA extends RecyclerView.Adapter<FilesA.FileViewHolder> {
    List<FileEntity> elements;
    Listener listener;

    public FilesA(List<FileEntity> elements, Object context) {
        this.elements = elements;
        if (context instanceof Listener){
            listener = (Listener) context;
        }
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_file, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.txtOriginalName.setText(elements.get(position).getOriginalName() == null ? "" : elements.get(position).getOriginalName());
        holder.txtTypeOfFile.setText(elements.get(position).getTypeOfFile());

        holder.imgOpenFile.getRootView().setOnClickListener((v) -> {
            listener.onItemSelected(elements.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        TextView txtOriginalName;
        TextView txtTypeOfFile;
        ImageView imgOpenFile;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            txtOriginalName = itemView.findViewById(R.id.txtOriginalName);
            txtTypeOfFile = itemView.findViewById(R.id.txtTypeOfFile);
            imgOpenFile = itemView.findViewById(R.id.imgOpenFile);
        }
    }

    public interface Listener{
        void onItemSelected(FileEntity fileEntity);
    }
}
