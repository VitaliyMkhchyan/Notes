package com.vitaliy.notes.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.vitaliy.notes.Models.Note;

@Database(entities = Note.class, version = 2, exportSchema = false)
public abstract class RoomDb extends RoomDatabase {
    public abstract NoteDAO noteDAO();
}
