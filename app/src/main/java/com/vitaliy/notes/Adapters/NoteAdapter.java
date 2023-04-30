package com.vitaliy.notes.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vitaliy.notes.Interfaces.SetOnClickItem;
import com.vitaliy.notes.Models.Note;
import com.vitaliy.notes.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{

    private List<Note> noteList;
    private final SetOnClickItem setOnClickItem;
    private final LayoutInflater inflater;
    private static boolean flug = false;

    public NoteAdapter(List<Note> noteList, SetOnClickItem setOnClickItem, Context context) {
        this.noteList = noteList;
        this.setOnClickItem = setOnClickItem;
        inflater = LayoutInflater.from(context);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(List<Note> filteredList) {
        this.noteList = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_note_model, parent, false));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = noteList.get(position);

        holder.title.setText(note.getTitle());
        holder.description.setText(note.getDescription());
        holder.date_of_creation.setText(note.getDate_of_creation());

        holder.btn_star_note.setOnClickListener(view -> {
            if (flug) {
                holder.btn_star_note.setImageResource(R.drawable.ic_no_star);
                flug = false;
            } else {
                holder.btn_star_note.setImageResource(R.drawable.ic_yes_star);
                flug = true;
            }
        });

        // Обработка нажатия на элемент RecyclerView
        holder.itemView.setOnClickListener(view -> setOnClickItem.onClick(note));
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        // Объекты модели
        final TextView title, description, date_of_creation;
        final ImageButton btn_star_note;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_);
            description = itemView.findViewById(R.id.description);
            date_of_creation = itemView.findViewById(R.id.date);
            btn_star_note = itemView.findViewById(R.id.btn_star_note);
        }
    }
}
