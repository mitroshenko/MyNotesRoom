package com.example.android.mynotenew

import android.content.Context
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Абстрактный репозиторий, рекомендуемый Руководством по архитектуре.
 */
class NoteRepository(private val noteDao: NoteDao) {

    // Room выполняет все запросы в отдельном потоке.
    //    // Наблюдаемый поток уведомляет наблюдателя об изменении данных.
    val allNotes: Flow<List<NoteEntity>> = noteDao.getAlphabetizedNotes()

    // По умолчанию Room выполняет запросы suspend вне основного потока, поэтому нам не нужно
    //// реализовывать что-либо еще, чтобы избежать длительной работы с базой данных
    //    // вне основного потока.
    @WorkerThread
    suspend fun insert(note: NoteEntity) {
        noteDao.insert(note)
    }

    suspend fun delete(note:NoteEntity){
        noteDao.delete(note)
        }
    }



