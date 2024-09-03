package com.example.android.mynotenew

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class NewNoteViewModel(private val repository: NoteRepository) : ViewModel() {
    fun update(note: NoteEntity, onFinishCallback: () -> Unit) = viewModelScope.launch {
        repository.update(note)
        onFinishCallback()
    }

    fun insert(note: NoteEntity, onFinishCallback: () -> Unit) = viewModelScope.launch {
        repository.insert(note)
        onFinishCallback()
    }
}

class NewNoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewNoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewNoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

