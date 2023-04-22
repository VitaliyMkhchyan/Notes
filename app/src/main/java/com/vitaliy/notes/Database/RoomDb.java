package com.vitaliy.notes.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.vitaliy.notes.Models.Note;

@Database(entities = Note.class, version = 1)
public abstract class RoomDb extends RoomDatabase {
    public NoteDAO noteDAO;
}
