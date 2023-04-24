package com.vitaliy.notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.vitaliy.notes.Models.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {

    private EditText editTitle, editDescription;
    private ImageButton btnSave, btnDelete, btnBack;
    private int status;
    private Note note;
    private boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        InitialObjects();

        status = (int) getIntent().getSerializableExtra("status");

        btnDelete.setVisibility(View.GONE);

        try {
            note = (Note) getIntent().getSerializableExtra("old_note");

            editTitle.setText(note.getTitle());
            editDescription.setText(note.getDescription());
            isOldNote = true;
            btnDelete.setVisibility(View.VISIBLE);
        } catch (Exception e) {e.printStackTrace();}

        // Сохранение заметки
        btnSave.setOnClickListener(view -> {
            String title = editTitle.getText().toString();
            String description = editDescription.getText().toString();

            if (title.equals("") && description.equals("")) {
                Toast.makeText(this, "Нельзя сохранить пустую заметку!", Toast.LENGTH_SHORT).show();
            } else {
                if (!isOldNote) {
                    note = new Note();
                }

                note.setTitle(title);
                note.setDescription(description);
                note.setDate_of_creation(currentDateTime());
                goHome(note, status);
            }
        });

        // Удаление заметки
        btnDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Удаление заметки");
            builder.setMessage("Вы действительно хотите удалить заметку?");

            builder.setPositiveButton("Удалить", (dialogInterface, i) -> {
                if (isOldNote) {
                    goHome(note, 2);
                } else {
                    Toast.makeText(this, "Это новая заметка!", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, "Заметка удалена!", Toast.LENGTH_SHORT).show();
            });

            builder.setNegativeButton("Отмена", (dialogInterface, i) -> {});
            builder.create().show();
        });

        // Назад в MainActivity
        btnBack.setOnClickListener(view -> finish());
    }

    /** Инициализация используемых объектов */
    private void InitialObjects() {
        btnSave = findViewById(R.id.btn_save);
        btnDelete = findViewById(R.id.btn_delete);
        btnBack = findViewById(R.id.btn_back);

        editTitle = findViewById(R.id.edit_title);
        editDescription = findViewById(R.id.edit_description);

        note = new Note();
    }

    /** Получение даты и времени
     * @return 20.10.2023 15:33:33 */
    private String currentDateTime() {
        // Текущее время
        Date currentDate = new Date();

        // Форматирование времени как "день.месяц.год"
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String date = dateFormat.format(currentDate);

        // Форматирование времени как "часы:минуты:секунды"
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String time = timeFormat.format(currentDate);

        return date + " " + time;
    }

    /** Отправка данных и завершение работы NoteActivity */
    private void goHome(Note note, int status) {
        Intent intent = new Intent(NoteActivity.this, MainActivity.class);
        intent.putExtra("notes", note);
        intent.putExtra("status", status);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}