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
    val allWords: Flow<List<NoteEntity>> = noteDao.getAlphabetizedNotes()

    // По умолчанию Room выполняет запросы suspend вне основного потока, поэтому нам не нужно
    //// реализовывать что-либо еще, чтобы избежать длительной работы с базой данных
    //    // вне основного потока.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(note: NoteEntity) {
        noteDao.insert(note)
    }
    fun delete(context: Context, note:NoteEntity){
        var noteDatabase:NoteRoomDatabase?=null

        noteDatabase= initialiseDB(context)
        CoroutineScope(Dispatchers.IO).launch {
            noteDatabase?.noteDao()?.delete(note)
        }
    }

    private fun initialiseDB(context: Context):NoteRoomDatabase?
    {
        return  NoteRoomDatabase.getInstance(context)
    }

}
