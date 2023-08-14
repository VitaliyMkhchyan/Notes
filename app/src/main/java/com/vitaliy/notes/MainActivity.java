package com.vitaliy.notes;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.vitaliy.notes.Adapters.NoteAdapter;
import com.vitaliy.notes.Database.RoomDb;
import com.vitaliy.notes.Interfaces.SetOnClickItem;
import com.vitaliy.notes.Models.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Note> noteList;
    private RoomDb roomDatabase;
    private NoteAdapter noteAdapter;
    private RecyclerView recyclerView;
    private ImageButton btnCreateNote, btnSearchNote, btnLayoutManager;
    private EditText search;
    private boolean flag;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Темный режим
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitialObjects();

        noteAdapter = new NoteAdapter(noteList, setOnClickItem, MainActivity.this);
        recyclerView.setAdapter(noteAdapter);

        flag = sp.getBoolean("layout", true);
        changeLayoutManager(!flag);

        // Создание заметки
        btnCreateNote.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            intent.putExtra("status", 1); // Status = 1; если это новая заметка
            activityResultLauncher.launch(intent);
        });

        // При нажатии на кнопку search скрывать или показывать searchView
        btnSearchNote.setOnClickListener(view -> {
            if (search.getVisibility() == View.GONE) {search.setVisibility(View.VISIBLE);}
            else {search.setVisibility(View.GONE);}
        });

        // Фильтр
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence newText, int start, int before, int count) {
                FilterText(newText.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Изменение LayoutManager для RecyclerView
        btnLayoutManager.setOnClickListener(view -> {
            if (flag) {
                changeLayoutManager(true);
                flag = false;
            } else {
                changeLayoutManager(false);
                flag = true;
            }
            sp.edit().putBoolean("layout", flag).apply();
        });
    }

    /** Поиск заметки */
    private void FilterText(String newText) {
        List<Note> filteredList = new ArrayList<>();
        for (Note note : noteList) {
            if (note.getTitle().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(note);
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No note found", Toast.LENGTH_SHORT).show();
        } else {noteAdapter.setFilteredList(filteredList);}
    }

    /** Инициализация используемых объектов */
    private void InitialObjects() {
        recyclerView = findViewById(R.id.recycler_view);
        btnCreateNote = findViewById(R.id.btn_create_note);
        btnSearchNote = findViewById(R.id.btn_search_note);
        btnLayoutManager = findViewById(R.id.btn_layout_manager);
        search = findViewById(R.id.search);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        roomDatabase = Room.databaseBuilder(MainActivity.this, RoomDb.class, "notes").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        noteList = roomDatabase.noteDAO().getAll();
    }

    /** Нажатие на элемент RecyclerView */
    final SetOnClickItem setOnClickItem = new SetOnClickItem() {
        @Override
        public void onClick(Note note) {
            Intent intent = new Intent(MainActivity.this, NoteActivity.class);
            intent.putExtra("old_note", note);
            intent.putExtra("status", 0); // Status = 0; если это старая заметка
            activityResultLauncher.launch(intent);
        }

        @Override
        public void onLongClick(View view, Note note) {
            showPopupMenu(view, note);
        }
    };

    /** Получение результата из NoteActivity (Note) */
    @SuppressLint("NotifyDataSetChanged")
    final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Note new_note = (Note) bundle.get("notes");
                    int status = bundle.getInt("status");

                    // Обновляем или создаем ячейку в базе данных
                    switch (status) {
                        case 0: roomDatabase.noteDAO().update(new_note.getId(), new_note.getTitle(), new_note.getDescription(), new_note.getDate_of_creation(), new_note.getColor()); break;
                        case 1: roomDatabase.noteDAO().insert(new_note); break;
                        case 2: roomDatabase.noteDAO().delete(new_note); break;
                    }

                    noteList.clear();
                    noteList.addAll(roomDatabase.noteDAO().getAll());
                    noteAdapter.notifyDataSetChanged();
                }
            }
    );

    /** Popup меню для удаления заметки */
    @SuppressLint("NotifyDataSetChanged")
    private void showPopupMenu (View view, Note note) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.menu);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {
            noteList.remove(note);
            roomDatabase.noteDAO().delete(note);
            noteList.clear();
            noteList.addAll(roomDatabase.noteDAO().getAll());
            noteAdapter.notifyDataSetChanged();
            return true;
        });
    }

    /** Изменение LayoutManager по нажатию на кнопку */
    private void changeLayoutManager(boolean flag) {
        if (flag) {
            btnLayoutManager.setImageResource(R.drawable.ic_grid_layout);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        } else {
            btnLayoutManager.setImageResource(R.drawable.ic_linear_layout);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }
}