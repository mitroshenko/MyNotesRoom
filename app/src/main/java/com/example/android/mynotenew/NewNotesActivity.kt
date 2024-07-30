package com.example.android.mynotenew

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import ivan.mitroshenko.roomnotessample.R

/**
 * Activity for entering a word.
 */

class NewNotesActivity : AppCompatActivity() {
    private val newNoteActivityRequestCode = 1
    private val updateNoteActivityRequestCode = 2

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_word)
        val editNoteView = findViewById<EditText>(R.id.edit_word)

        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val mynote = intent.getStringExtra("mynote")
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editNoteView.text)) {
                setResult(Activity.RESULT_CANCELED, replyIntent)
            } else {
                val note = editNoteView.text.toString()
                replyIntent.putExtra(EXTRA_REPLY, note)
                setResult(Activity.RESULT_OK, replyIntent)
            }
            finish()
        }


    }

    companion object {
        const val EXTRA_REPLY = "MyNotesRoom.REPLY"
    }
}
