package com.vitaliy.notes.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vitaliy.notes.Interfaces.SetOnClickItem;
import com.vitaliy.notes.Models.Note;
import com.vitaliy.notes.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{

    List<Note> noteList;
    SetOnClickItem setOnClickItem;
    LayoutInflater inflater;

    public NoteAdapter(List<Note> noteList, SetOnClickItem setOnClickItem, Context context) {
        this.noteList = noteList;
        this.setOnClickItem = setOnClickItem;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_note_model, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = noteList.get(position);

        holder.title.setText(note.getTitle());
        holder.description.setText(note.getDescription());
        holder.date_of_creation.setText(note.getDate_of_creation());

        holder.itemView.setOnClickListener(view -> setOnClickItem.onClick(note));
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {

        // Объекты модели
        final TextView title, description, date_of_creation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            date_of_creation = itemView.findViewById(R.id.date);
        }
    }
}
