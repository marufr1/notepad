package io.github.marufr1.notepad.ui.add

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.marufr1.notepad.data.local.NoteDatabase
import io.github.marufr1.notepad.data.model.Note
import io.github.marufr1.notepad.databinding.ActivityAddNoteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private var database: NoteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = NoteDatabase.getInstance(this)

        binding.btnSave.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val note = binding.editNote.text.toString()
            val currentDate =
                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
            val thisDataNote = Note(
                null,
                title,
                note,
                currentDate
            )
            saveDataNote(thisDataNote)
        }
    }

    private fun saveDataNote(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            val resultInsert = database!!.noteDao().insertNote(note)
            if (resultInsert != 0L) {
                showMessage("Successfully inserting data ${note.title}")
                closeScreen()
            } else {
                showMessage("Failed to insert data ${note.title}")
            }
        }
    }

    private fun showMessage(message: String) {
        runOnUiThread {
            Toast.makeText(
                this,
                message,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun closeScreen() {
        runOnUiThread {
            finish()
        }
    }
}