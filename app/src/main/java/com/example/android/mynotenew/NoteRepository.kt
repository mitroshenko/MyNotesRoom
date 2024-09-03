package com.example.android.mynotenew

import android.content.Context
import androidx.annotation.WorkerThread
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow


class NoteRepository(private val noteDao: NoteDao) {
    val allNotes: Flow<List<NoteEntity>> = noteDao.getAlphabetizedNotes()

    @WorkerThread
    suspend fun insert(note: NoteEntity) {
        noteDao.insert(note)
    }

    suspend fun delete(note: NoteEntity) {
        noteDao.delete(note)
    }

    suspend fun update(note: NoteEntity) {
        noteDao.update(note)
    }

    private var noteDatabase: NoteRoomDatabase? = null

    private fun initialiseDB(context: Context): NoteRoomDatabase? {
        val applicationScope = CoroutineScope(SupervisorJob())
        return NoteRoomDatabase.getDatabase(context, applicationScope)
    }
}



