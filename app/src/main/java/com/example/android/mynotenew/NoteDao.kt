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

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Волшебство этого файла заключается в том, что вы сопоставляете вызов метода с SQL-запросом.
 *  *
 *  * Когда вы используете сложные типы данных, такие как Дата, вам также необходимо указать преобразователи типов.
 *  * Чтобы этот пример был простым, не используются типы, для которых требуются преобразователи типов.
 *  * Смотрите документацию по адресу
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */

@Dao
interface NoteDao {

    // Поток всегда содержит/кэширует последнюю версию данных. Уведомляет своих наблюдателей
    //об изменении // данных.
    @Query("SELECT * FROM my_table")
    fun getAlphabetizedNotes(): Flow<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: NoteEntity)

    @Delete
    suspend fun delete(note:NoteEntity)

    @Query("DELETE FROM my_table")
    suspend fun deleteAll()
}
