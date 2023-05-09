package com.vitaliy.notes.Interfaces;

import android.view.View;

import com.vitaliy.notes.Models.Note;

public interface SetOnClickItem {
    void onClick(Note note);
    void onLongClick(View view, Note note);
}
