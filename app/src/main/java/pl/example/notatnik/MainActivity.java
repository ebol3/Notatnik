package pl.example.notatnik;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.example.notatnik.database.DatabaseHelper;
import pl.example.notatnik.Note;

public class MainActivity extends AppCompatActivity {

    private List<Note> noteList;
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private DatabaseHelper databaseHelper;

    private EditText titleEditText, contentEditText;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        addButton = findViewById(R.id.addButton);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(noteList);
        recyclerView.setAdapter(noteAdapter);

        databaseHelper = new DatabaseHelper(this);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });

        loadNotes();
    }

    private void addNote() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();
        String date = getCurrentDate();

        if (!title.isEmpty() && !content.isEmpty()) {
            Note note = new Note(title, content, date);
            long id = databaseHelper.createNote(note);

            if (id != -1) {
//                note.setId(id);
                noteList.add(note);
                noteAdapter.notifyDataSetChanged();

                titleEditText.setText("");
                contentEditText.setText("");
            } else {
                Toast.makeText(this, "Błąd podczas dodawania notatki", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Wprowadź tytuł i treść notatki", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadNotes() {
        noteList.clear();
        noteList.addAll(databaseHelper.getNotes());
        noteAdapter.notifyDataSetChanged();
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}