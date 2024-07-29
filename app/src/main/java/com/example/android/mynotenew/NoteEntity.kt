package com.example.android.mynotenew

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Базовый класс, представляющий сущность, которая является строкой в таблице базы данных, состоящей из одного столбца.
 *  *
 *  * @ Entity - Вы должны аннотировать класс как сущность и указать имя таблицы, если не имя класса.
 *  * @ * @ Primary Key - Вы должны указать первичный ключ.
 *  * @ ColumnInfo - Вы должны указать имя столбца, если оно отличается от имени переменной.
 *  *
 *  * Полный набор аннотаций приведен в документации.
 * https://developer.android.com/topic/libraries/architecture/room.html
 */

@Entity(tableName = "my_table")
data class NoteEntity(
    @PrimaryKey
//    @ColumnInfo(name = "id")
//    val id: Int,
    @ColumnInfo(name = "title")
val title: String)



