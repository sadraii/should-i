/*
* Copyright 2020 Mostafa Sadraii
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

package com.sadraii.shouldi.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sadraii.shouldi.data.dao.PictureDao
import com.sadraii.shouldi.data.dao.UserDao
import com.sadraii.shouldi.data.entity.PictureEntity
import com.sadraii.shouldi.data.entity.UserEntity
import kotlinx.coroutines.CoroutineScope

@Database(entities = [PictureEntity::class, UserEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class ShouldIDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun pictureDao(): PictureDao

    companion object {

        internal const val GS_BUCKET = "gs://should-i-98830.appspot.com"

        @Volatile
        private var INSTANCE: ShouldIDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ShouldIDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context, scope).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context, scope: CoroutineScope) =
            Room.databaseBuilder(context.applicationContext, ShouldIDatabase::class.java, "shouldi.db")
                .addCallback(ShouldIDatabaseCallback(scope))
                .build()
    }

    private class ShouldIDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

        // override fun onOpen(db: SupportSQLiteDatabase) {
        //     super.onOpen(db)
        //     Log.d(TAG, "db onOpen")
        //     INSTANCE?.let { database ->
        //         scope.launch(Dispatchers.IO) {
        //             SampleData.delete(database.userDao(), database.pictureDao())
        //             SampleData.generate(database.userDao(), database.pictureDao())
        //         }
        //     }
        // }

        // override fun onCreate(db: SupportSQLiteDatabase) {
        //     super.onCreate(db)
        //     Log.d(TAG, "db onCreate")
        //     INSTANCE?.let { database ->
        //         scope.launch(Dispatchers.IO) {
        //             SampleData.generateUserAndPicture(database.userDao(), database.pictureDao())
        //         }
        //     }
        // }
    }
}





