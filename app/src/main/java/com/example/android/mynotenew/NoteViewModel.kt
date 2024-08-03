/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    // Использование оперативных данных и кэширование того, что возвращает all Words, имеет ряд преимуществ:
    //    // - Мы можем поместить наблюдателя в данные (вместо опроса изменений) и обновлять пользовательский интерфейс только
    //тогда, когда данные действительно изменяются.
    //    // - Репозиторий полностью отделен от пользовательского интерфейса с помощью ViewModel.
    val allNotes: LiveData<List<NoteEntity>> = repository.allNotes.asLiveData()


    fun delete(position: Int) = viewModelScope.launch {
        val note = allNotes.value?.get(position)
        if (note !== null) {
            repository.delete(note)
        }
    }

}

class NoteViewModelFactory(private val repository: NoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

