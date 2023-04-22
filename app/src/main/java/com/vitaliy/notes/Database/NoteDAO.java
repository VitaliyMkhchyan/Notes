package com.vitaliy.notes.Database;

import static androidx.room.OnConflictStrategy.REPLACE;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.vitaliy.notes.Models.Note;

import java.util.List;

@Dao
public interface NoteDAO {
    @Insert(onConflict = REPLACE)
    void insert(Note note);

    @Query("SELECT * FROM notes order by id desc")
    List<Note> getAll();

    @Query("UPDATE notes SET title = :title, description = :description, date_of_creation = :date_of_creation")
    void update(String title, String description, String date_of_creation);

    @Delete
    void delete(Note note);
}
