package com.example.android.mynotenew

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ivan.mitroshenko.roomnotessample.R
import kotlinx.coroutines.flow.Flow



class MainActivity : AppCompatActivity(){
//    private lateinit var noteList: ArrayList<NoteEntity>

    //    private lateinit var noteAdapter: NoteListAdapter
    private val newNoteActivityRequestCode = 1
    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NotesApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = NoteListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewNotesActivity::class.java)
            startActivityForResult(intent, newNoteActivityRequestCode)



        }



        // Добавьте наблюдателя к текущим данным, возвращаемым с помощью get Alphabetized Words.
        //        // Метод OnChanged() срабатывает, когда наблюдаемые данные изменяются, а действие
        //// находится на переднем плане.
        noteViewModel.allNotes.observe(owner = this) { notes ->
            // Uобновите кэшированную копию слов в адаптере.
            notes.let { adapter.submitList(it) }
        }
        val itemTouchHelper=ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.getStringExtra(NewNotesActivity.EXTRA_REPLY)?.let { reply ->
                val title = NoteEntity(reply)
                noteViewModel.insert(title)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }
    val simpleCallback= object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            lateinit var noteAdapter: NoteListAdapter
            lateinit var noteList: ArrayList<NoteEntity>
            val position=viewHolder.adapterPosition
            val note=noteList[position]
            when(direction)
            {
                ItemTouchHelper.RIGHT->{
                    noteViewModel.delete(note)
                    noteAdapter.notifyDataSetChanged()
                }
                ItemTouchHelper.LEFT->{
                    noteViewModel.delete(note)
                    noteAdapter.notifyDataSetChanged()
                }
            }
        }

    }


}

