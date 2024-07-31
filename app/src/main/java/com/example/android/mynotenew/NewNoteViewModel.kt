package com.example.android.mynotenew

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */

class NewNoteViewModel(private val repository: NoteRepository) : ViewModel() {

    // Использование оперативных данных и кэширование того, что возвращает all Words, имеет ряд преимуществ:
    //    // - Мы можем поместить наблюдателя в данные (вместо опроса изменений) и обновлять пользовательский интерфейс только
    //тогда, когда данные действительно изменяются.
    //    // - Репозиторий полностью отделен от пользовательского интерфейса с помощью ViewModel.
    val allNotes: LiveData<List<NoteEntity>> = repository.allNotes.asLiveData()


    fun update(note: NoteEntity) = viewModelScope.launch {
        repository.update(note)
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

