package com.example.android.mynotenew

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ivan.mitroshenko.roomnotessample.R


class NewNotesActivity : AppCompatActivity() {

    private val newnoteViewModel: NewNoteViewModel by viewModels {
        NewNoteViewModelFactory((application as NotesApplication).repository)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)

        val editNoteView = findViewById<EditText>(R.id.edit_word)
        val mynote = intent.getParcelableExtra<NoteEntity>("mynote")

        if (mynote != null) {
            editNoteView.setText(mynote.title)
        }

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editNoteView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
                finish()
            } else {
                if (mynote == null) {
                    //create
                    newnoteViewModel.insert(
                        note = NoteEntity(title = editNoteView.text.toString()),
                        onFinishCallback = {
                            finish()
                        }
                    )
                } else {
                    newnoteViewModel.update(
                        note = mynote.copy(title = editNoteView.text.toString()),
                        onFinishCallback = {
                            finish()
                        }
                    )
                }
            }


        }

    }
}
