package com.example.mynotesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.provider.ContactsContract.CommonDataKinds.Note
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
//import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mynotesapp.Adapter.NotesAdapter
import com.example.mynotesapp.Database.NoteDatabase
import com.example.mynotesapp.Models.NotesViewModel
import com.example.mynotesapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),NotesAdapter.NotesClickListener,PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: NoteDatabase
    lateinit var viewModel:NotesViewModel
    lateinit var adapter: NotesAdapter
    lateinit var seletedNote:com.example.mynotesapp.Models.Note

    private val updateNote=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if(result.resultCode==Activity.RESULT_OK){
            val note=result.data?.getSerializableExtra("note") as? com.example.mynotesapp.Models.Note
            if(note!=null){
                viewModel.updateNote(note)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //for initialising ui
        initUI()

        viewModel=ViewModelProvider(this,
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NotesViewModel::class.java)

        viewModel.allNotes.observe(this) { list ->

            list?.let {
               adapter.updateList(list)
            }
        }
        database=NoteDatabase.getDatabase(this)
    }

    private fun initUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager=StaggeredGridLayoutManager(2,LinearLayout.VERTICAL)
        adapter= NotesAdapter(this,this)
        binding.recyclerView.adapter=adapter

        val getContent=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if(result.resultCode== Activity.RESULT_OK){
                val note=result.data?.getSerializableExtra("note") as? com.example.mynotesapp.Models.Note
                if(note!=null){
                    viewModel.addNote(note)
                }
            }
        }
        binding.floatingActionButton.setOnClickListener{
            val intent=Intent(this,AddNote::class.java)
            getContent.launch(intent)
        }
        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    adapter.filterList(newText)
                }
                return true
            }

        })

    }

    override fun OnItemClicked(note:com.example.mynotesapp.Models.Note) {
        val intent=Intent(this@MainActivity,AddNote::class.java)
        intent.putExtra("current_note",note)
        updateNote.launch(intent)
    }

    override fun OnItemLongClicked(note : com.example.mynotesapp.Models.Note, cardView: CardView) {
        seletedNote=note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
      val popup=PopupMenu(this,cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
        popup.inflate(R.menu.pop_up_menu)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId==R.id.delete_note){
            viewModel.deleteNote(seletedNote)
            return true
        }
        return false
    }


}