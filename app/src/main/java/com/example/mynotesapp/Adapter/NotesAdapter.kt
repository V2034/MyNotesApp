package com.example.mynotesapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotesapp.Models.Note
import com.example.mynotesapp.R

class NotesAdapter(private val context:Context, val listener: NotesClickListener): RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private val NotesList=ArrayList<Note>()
    private val fullList=ArrayList<Note>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdapter.NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        )
    }

    override fun onBindViewHolder(holder: NotesAdapter.NoteViewHolder, position: Int) {
       val currentNote= NotesList[position]
        holder.title.text=currentNote.title
        holder.title.isSelected=true
        holder.note_tv.text=currentNote.note
        holder.date.text=currentNote.date
        holder.date.isSelected=true

        holder.notes_layout.setOnClickListener{
            listener.OnItemClicked(NotesList[holder.adapterPosition])
        }
        holder.notes_layout.setOnClickListener{
            listener.OnItemLongClicked(NotesList[holder.adapterPosition],holder.notes_layout)
            true
        }
    }

    override fun getItemCount(): Int {
        return NotesList.size
    }

    fun updateList(newList: List<Note>){
        fullList.clear()
        fullList.addAll(newList)

        NotesList.clear()
        NotesList.addAll(fullList)
        notifyDataSetChanged()
    }

    fun filterList(search: String){
        NotesList.clear()

        for(item in fullList){
            if(item.title?.lowercase()?.contains(search.lowercase())==true||
                item.note?.lowercase()?.contains(search.lowercase())==true){
                NotesList.add(item)
            }
        }
        notifyDataSetChanged()
    }
    class NoteViewHolder(view: View):RecyclerView.ViewHolder(view){
        val notes_layout=view.findViewById<CardView>(R.id.card_view)
        val title=view.findViewById<TextView>(R.id.id_title)
        val note_tv=view.findViewById<TextView>(R.id.id_note)
        val date=view.findViewById<TextView>(R.id.id_date)

    }
    interface NotesClickListener{
        fun OnItemClicked(note: Note)
        fun OnItemLongClicked(note: Note,cardView: CardView)
    }
}