package com.ecarto.cartoapp.ui.Files;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ecarto.cartoapp.R;
import com.ecarto.cartoapp.database.Entities.FileEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddFileA extends RecyclerView.Adapter<AddFileA.AddFileViewHolder> {
    List<FileEntity> elements;
    AddFileA.Listener listener;
    Set<FileEntity> fileEntitySet;


    public AddFileA(List<FileEntity> elements, Object context) {
        this.elements = elements;
        if (context instanceof AddFileA.Listener){
            listener = (AddFileA.Listener) context;
        }
        fileEntitySet = new HashSet<>();
    }

    @NonNull
    @Override
    public AddFileA.AddFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_file_item, parent, false);
        return new AddFileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddFileA.AddFileViewHolder holder, int position) {
        holder.fileName.setText(elements.get(position).getOriginalName());

        holder.checkBox.setOnCheckedChangeListener((v, isChecked) -> {
            if (isChecked){
                fileEntitySet.add(elements.get(position));
            } else {
                fileEntitySet.remove(elements.get(position));
            }
            listener.onCheckedBoxSelected(new ArrayList<>(fileEntitySet));
        });
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    public class AddFileViewHolder extends RecyclerView.ViewHolder{
        TextView fileName;
        CheckBox checkBox;

        public AddFileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.txtFileName);
            checkBox = itemView.findViewById(R.id.ckFileAttached);
        }
    }

    public interface Listener {
        void onCheckedBoxSelected(List<FileEntity> fileEntities);
    }
}
