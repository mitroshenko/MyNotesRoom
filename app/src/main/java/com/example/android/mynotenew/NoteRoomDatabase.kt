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


        private const val DATABASE_NAME="NoteDatabase"

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
                    "note_database"
                )
                    // Стирает и перестраивает вместо переноса, если объект переноса отсутствует.
                    // Перенос не является частью этой кодовой таблицы.
                    .fallbackToDestructiveMigration()
                    .addCallback(NoteDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class NoteDatabaseCallback(
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
            noteDao.deleteAll()}
    }
}

//            var title = NoteEntity("Hello")
//            noteDao.insert(title)
//            title = NoteEntity("World!")
//            noteDao.insert(title)
//        }
//    }
//}
