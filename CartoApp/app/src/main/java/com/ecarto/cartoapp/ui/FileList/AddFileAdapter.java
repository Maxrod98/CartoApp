package com.ecarto.cartoapp.ui.FileList;

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

public class AddFileAdapter extends RecyclerView.Adapter<AddFileAdapter.AddFileViewHolder> {
    List<FileEntity> elements;
    AddFileAdapter.Listener listener;
    Set<FileEntity> fileEntitySet;


    public AddFileAdapter(List<FileEntity> elements, Object context) {
        this.elements = elements;
        if (context instanceof AddFileAdapter.Listener){
            listener = (AddFileAdapter.Listener) context;
        }
        fileEntitySet = new HashSet<>();
    }

    @NonNull
    @Override
    public AddFileAdapter.AddFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_file_item, parent, false);
        return new AddFileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddFileAdapter.AddFileViewHolder holder, int position) {
        holder.fileName.setText(elements.get(position).getOriginalName());

        holder.checkBox.setOnCheckedChangeListener((v, v2) -> {
            fileEntitySet.add(elements.get(position)); // to avoid repeats
            listener.updateSelectedFiles(new ArrayList<>(fileEntitySet));
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
        void updateSelectedFiles(List<FileEntity> fileEntities);
    }
}
