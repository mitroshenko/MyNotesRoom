package com.example.android.mynotenew

import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

/**
 * Абстрактный репозиторий, рекомендуемый Руководством по архитектуре.
 */
class NoteRepository(private val noteDao: NoteDao) {

    // Room выполняет все запросы в отдельном потоке.
    //    // Наблюдаемый поток уведомляет наблюдателя об изменении данных.
    val allWords: Flow<List<NoteEntity>> = noteDao.getAlphabetizedWords()

    // По умолчанию Room выполняет запросы suspend вне основного потока, поэтому нам не нужно
    //// реализовывать что-либо еще, чтобы избежать длительной работы с базой данных
    //    // вне основного потока.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(note: NoteEntity) {
        noteDao.insert(note)
    }
}
