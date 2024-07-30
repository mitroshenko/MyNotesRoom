package com.example.android.mynotenew

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View

import android.widget.TextView
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import androidx.appcompat.widget.SearchView
import javax.security.auth.Subject


class MainActivity : AppCompatActivity() {


    private val newNoteActivityRequestCode = 1
    private val updateNoteActivityRequestCode = 2
    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NotesApplication).repository)
    }
    private val adapter: NoteListAdapter by lazy {
        NoteListAdapter { title ->

            val intent = Intent(this@MainActivity, UpdateActivity::class.java)
            intent.putExtra("mynote", title)
            startActivity(intent)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)



        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewNotesActivity::class.java)
            startActivityForResult(intent, newNoteActivityRequestCode)


        }
        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)


        // Добавьте наблюдателя к текущим данным, возвращаемым с помощью get Alphabetized Words.
        //        // Метод OnChanged() срабатывает, когда наблюдаемые данные изменяются, а действие
        //// находится на переднем плане.
        noteViewModel.allNotes.observe(owner = this) { notes ->
            // Uобновите кэшированную копию слов в адаптере.
            notes.let { adapter.submitList(it) }
        }



    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)
        when (requestCode) {

            1 -> if (requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
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
            2 -> if (requestCode == updateNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
                intentData?.getStringExtra(NewNotesActivity.EXTRA_REPLY)?.let { reply ->
                    val title = NoteEntity(reply)
                    noteViewModel.update(title)
                }
            } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
        }
    }

    val simpleCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.RIGHT -> {
                        noteViewModel.delete(position)
                    }

                    ItemTouchHelper.LEFT -> {
                        noteViewModel.delete(position)
                    }
                }
            }

        }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // below line is to get our inflater
        val inflater = menuInflater

        // inside inflater we are inflating our menu file.
        inflater.inflate(R.menu.search_menu, menu)

        // below line is to get our menu item.
        val searchItem: MenuItem = menu.findItem(R.id.actionSearch)

        // getting search view of our item.
        val searchView: SearchView = searchItem.getActionView() as SearchView

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                // inside on query text change method we are
                // calling a method to filter our recycler view.
                filter(msg)
                return false
            }
        })
        return true
    }

    private fun filter(text: String) {
        // creating a new array list to filter our data.
        val filteredlist: ArrayList<NoteEntity> = ArrayList()

        // running a for loop to compare elements.
        for (item in noteViewModel.allNotes.value!!) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.title.toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.

            adapter.submitList(emptyList())
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.submitList(filteredlist)
        }
    }


}


