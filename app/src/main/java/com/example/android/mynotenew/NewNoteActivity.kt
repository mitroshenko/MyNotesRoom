package com.example.android.mynotenew

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ivan.mitroshenko.roomnotessample.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class NewNoteActivity : AppCompatActivity() {

    private val newnoteViewModel: NewNoteViewModel by viewModels {
        NewNoteViewModelFactory((application as NotesApplication).repository)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_note)

        val editTitle = findViewById<EditText>(R.id.edTitle)
        val editDesc = findViewById<EditText>(R.id.edDesc)
        val mynote = intent.getParcelableExtra<NoteEntity>("mynote")

        if (mynote != null) {
            editTitle.setText(mynote.title)
            editDesc.setText(mynote.description)
        }

        val button = findViewById<Button>(R.id.btn_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editTitle.text) || TextUtils.isEmpty(editDesc.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
                Toast.makeText(
                    applicationContext,
                    R.string.empty,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (mynote == null) {
                    //create
                    newnoteViewModel.insert(
                        note = NoteEntity(
                            title = editTitle.text.toString(),
                            description = editDesc.text.toString(),
                            time_date = getCurrentTime()
                        ),
                        onFinishCallback = {
                            finish()
                        }
                    )
                } else {
                    newnoteViewModel.update(
                        note = mynote.copy(
                            title = editTitle.text.toString(),
                            description = editDesc.text.toString(),
                            time_date = getCurrentTime()
                        ),
                        onFinishCallback = {
                            finish()
                        }
                    )
                }
            }
        }
    }

    private fun getCurrentTime(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd.MM.yy kk:mm", Locale.getDefault())
        return formatter.format(time)
    }
}
