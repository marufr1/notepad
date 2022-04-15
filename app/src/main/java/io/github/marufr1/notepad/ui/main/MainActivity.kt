package io.github.marufr1.notepad.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.marufr1.notepad.adapter.NoteAdapter
import io.github.marufr1.notepad.data.local.NoteDatabase
import io.github.marufr1.notepad.data.model.Note
import io.github.marufr1.notepad.databinding.ActivityMainBinding
import io.github.marufr1.notepad.ui.add.AddNoteActivity
import io.github.marufr1.notepad.ui.edit.EditActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var database: NoteDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = NoteDatabase.getInstance(this)

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        loadAllNotes()
    }

    private fun loadAllNotes() {
        CoroutineScope(Dispatchers.IO).launch {
            val allNotes = database?.noteDao()?.getAllNotes()
            runOnUiThread {
                allNotes?.let {
                    binding.recyclerNote.apply {
                        layoutManager = LinearLayoutManager(
                            context, RecyclerView.VERTICAL, false
                        )

                        adapter = NoteAdapter(it, {
                            intentUpdateNote(it)
                        }, {
                            showAlertDelete(it)
                        })
                    }
                }
            }
        }
    }

    private fun intentUpdateNote(note: Note) {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra("note", note)
        startActivity(intent)
    }

    private fun showAlertDelete(note: Note) {
        val builder = AlertDialog.Builder(this)
            .setTitle("Warning")
            .setMessage("Are you sure you want to delete '${note.title}'?")
            .setCancelable(true)
            .setPositiveButton("Yes") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    database?.noteDao()?.deleteNote(note)
                    runOnUiThread {
                        this@MainActivity.loadAllNotes()
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
        builder.create().show()
    }
}