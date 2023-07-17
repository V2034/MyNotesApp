package com.example.mynotesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.widget.Toast
import com.example.mynotesapp.databinding.ActivityAddNoteBinding
import com.example.mynotesapp.databinding.ActivityMainBinding
import java.lang.Exception
import java.util.Date
import java.util.logging.SimpleFormatter
import java.util.zip.DataFormatException

class AddNote : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding

    private lateinit var note:Note
    private lateinit var old_note:Note
    var isUpdate=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try{
            old_note=intent.getSerializableExtra("current_note")as note
            binding.editTitle.setText(old_note.title)
            binding.editNote.setText(old_note.note)
            isUpdate=true
        }catch (e:Exception){
            e.printStackTrace()
        }
        binding.imgCheck.setOnClickListener{
            val title=binding.editTitle.text.toString()
            val note=binding.editNote.text.toString()

            if(title.isEmpty()||note.isNotEmpty()){
                val formatter=SimpleFormatter("dd MMM YYYY-HH:mm")
                if(isUpdate){
                    note= Note(
                        old_note,title,note,formatter(Date())
                    )

                }else{
                    note= Note(
                        null, title, note_desc, formatter.format(Date())
                    )
                }
                val intent=Intent()
                intent.putExtra("note",note)
                setResult(Activity.RESULT_OK,intent)
                finish()
            }else{
                Toast.makeText(this@AddNote,"Please enter some data",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }
        binding.idBackArrow.setOnClickListener{
            onBackPressed()
        }
    }
}