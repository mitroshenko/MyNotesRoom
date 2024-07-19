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
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Это серверная часть. База данных. Раньше это делал OpenHelper.
 *  * Тот факт, что к этому приложению очень мало комментариев, подчеркивает его привлекательность.
 */
@Database(entities = [NoteEntity::class], version = 1)
abstract class NoteRoomDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): NoteRoomDatabase {
            // если значение ЭКЗЕМПЛЯРА не равно null, то верните его,
            //// если это так, то создайте базу данных
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteRoomDatabase::class.java,
                    "word_database"
                )
                    // Стирает и перестраивает вместо переноса, если объект переноса отсутствует.
                    // Перенос не является частью этой кодовой таблицы.
                    .fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class WordDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Если вы хотите сохранить данные при перезапуске приложения,
                //// закомментируйте следующую строку.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.noteDao())
                    }
                }
            }
        }

        /**
         * Заполните базу данных в новой сопрограмме.
         *          * Если вы хотите начать с большего количества слов, просто добавьте их.
         */
        suspend fun populateDatabase(noteDao: NoteDao) {
            // Каждый раз запускайте приложение с чистой базой данных.
            //            // Не требуется, если вы заполняете ее только при создании.
            noteDao.deleteAll()

//            var title = NoteEntity("Hello")
//            noteDao.insert(title)
//            title = NoteEntity("World!")
//            noteDao.insert(title)
        }
    }
}
