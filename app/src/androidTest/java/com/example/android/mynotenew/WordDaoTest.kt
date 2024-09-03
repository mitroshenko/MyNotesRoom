package com.example.android.mynotenew

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

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

    private lateinit var noteDao: NoteDao
    private lateinit var db: NoteRoomDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, NoteRoomDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        noteDao = db.noteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetWord() = runBlocking {
        val note = NoteEntity("word")
        noteDao.insert(note)
        val allWords = noteDao.getAlphabetizedWords().first()
        assertEquals(allWords[0].title, note.title)
    }

    @Test
    @Throws(Exception::class)
    fun getAllWords() = runBlocking {
        val note = NoteEntity("aaa")
        noteDao.insert(note)
        val note2 = NoteEntity("bbb")
        noteDao.insert(note2)
        val allWords = noteDao.getAlphabetizedWords().first()
        assertEquals(allWords[0].title, note.title)
        assertEquals(allWords[1].title, note2.title)
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() = runBlocking {
        val note = NoteEntity("word")
        noteDao.insert(note)
        val note2 = NoteEntity("word2")
        noteDao.insert(note2)
        noteDao.deleteAll()
        val allWords = noteDao.getAlphabetizedWords().first()
        assertTrue(allWords.isEmpty())
    }
}
