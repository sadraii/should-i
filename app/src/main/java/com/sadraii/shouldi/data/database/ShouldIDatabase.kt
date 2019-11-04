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
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sadraii.shouldi.TAG
import com.sadraii.shouldi.data.dao.PictureDao
import com.sadraii.shouldi.data.dao.UserDao
import com.sadraii.shouldi.data.entity.PictureEntity
import com.sadraii.shouldi.data.entity.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

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
            Log.d(TAG, "get database()")
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
    }

    private class ShouldIDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            Log.d(TAG, "onOpen() database")
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.userDao(), database.pictureDao())
                }
            }
        }

        suspend fun populateDatabase(userDao: UserDao, pictureDao: PictureDao) {
            userDao.deleteAllUsers()

            var newUser =
                UserEntity(1, "user1", "Elise", "Elser", "elise@example.com", Date(), null)
            userDao.insert(newUser)
            newUser =
                UserEntity(2, "user2", "Kendra", "Kahn", "kendra@example.com", Date(), Date())
            userDao.insert(newUser)
            newUser = UserEntity(3, "user3", "Corey", "Coursey", "corey@example.com", Date(), null)
            userDao.insert(newUser)

            pictureDao.deleteAllPictures()

            var newPicture = PictureEntity(
                11,
                "https://www.example.com/pic1",
                Date(),
                null,
                null,
                null,
                null,
                null,
                null,
                "Should I picture 1",
                1
            )
            pictureDao.insert(newPicture)
            newPicture = PictureEntity(
                22,
                "https://www.example.com/pic2",
                Date(),
                4,
                2,
                1,
                Date(),
                Date(),
                1,
                "Should I picture 2",
                2
            )
            pictureDao.insert(newPicture)
            newPicture = PictureEntity(
                33,
                "https://www.example.com/pic3",
                Date(),
                null,
                null,
                null,
                null,
                null,
                null,
                "Should I picture 3",
                3
            )
            pictureDao.insert(newPicture)
            newPicture = PictureEntity(
                111,
                "https://www.example.com/pic111",
                Date(),
                100,
                90,
                50,
                Date(),
                Date(),
                3,
                "Should I picture 1 again",
                1
            )
            pictureDao.insert(newPicture)
            Log.d(TAG, "populated database")
        }
    }
}

