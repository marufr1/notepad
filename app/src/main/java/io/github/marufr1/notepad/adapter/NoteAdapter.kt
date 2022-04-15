package io.github.marufr1.notepad.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.github.marufr1.notepad.data.model.Note
import io.github.marufr1.notepad.databinding.ItemNoteBinding

class NoteAdapter(
    private val notes: List<Note>,
    private val onItemClick: (Note) -> Unit,
    private val onDeleteClick: (Note) -> Unit,
): RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.binding.txtTitle.text = note.title
        holder.binding.txtNote.text = note.note
        holder.binding.txtDateTime.text = note.date
        holder.binding.imgBtnDelete.setOnClickListener {
            onDeleteClick(note)
        }
        holder.binding.itemNote.setOnClickListener {
            onItemClick(note)
        }
    }

    override fun getItemCount() = notes.size

}