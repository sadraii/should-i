/*
 * Copyright 2019 Mostafa Sadraii
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sadraii.shouldi.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sadraii.shouldi.data.dao.PictureDao
import com.sadraii.shouldi.data.dao.UserDao
import com.sadraii.shouldi.data.entity.PictureEntity
import com.sadraii.shouldi.data.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [PictureEntity::class, UserEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class ShouldIDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun pictureDao(): PictureDao

    companion object {
        @Volatile
        private var INSTANCE: ShouldIDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): ShouldIDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShouldIDatabase::class.java,
                    "should_i_database"
                )
                    .addCallback(ShouldIDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class ShouldIDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.userDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(userDao: UserDao) {
            userDao.deleteAllUsers()

//            var word = Word("Hello")
//            wordDao.insert(word)
//            word = Word("World!")
//            wordDao.insert(word)
        }
    }
}

