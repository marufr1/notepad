package io.github.marufr1.notepad.ui.edit

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.github.marufr1.notepad.data.local.NoteDatabase
import io.github.marufr1.notepad.data.model.Note
import io.github.marufr1.notepad.databinding.ActivityEditBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private var database: NoteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = NoteDatabase.getInstance(this)

        val dataNote = intent.getParcelableExtra<Note>("note")!!

        binding.editTitle.setText(dataNote.title)
        binding.editNote.setText(dataNote.note)

        binding.btnSave.setOnClickListener {
            val title = binding.editTitle.text.toString()
            val note = binding.editNote.text.toString()
            val currentDate =
                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
            val thisDataNote = Note(
                dataNote.id,
                title,
                note,
                currentDate
            )
            editDataNote(thisDataNote)
        }

    }

    private fun editDataNote(note: Note) {
        CoroutineScope(Dispatchers.IO).launch {
            database!!.noteDao().updateNote(note)
            showMessage("Successfully updating data ${note.title}")
            closeScreen()
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