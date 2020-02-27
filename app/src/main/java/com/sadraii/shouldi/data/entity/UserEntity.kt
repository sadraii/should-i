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

package com.sadraii.shouldi.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "user_name") val userName: String? = null,
    @ColumnInfo(name = "email") val email: String? = null,
    @ColumnInfo(name = "created") val created: Long = Instant.now().toEpochMilli(),
    @ColumnInfo(name = "last_online") val lastOnline: Long? = null,
    @ColumnInfo(name = "photo_url") val photoUrl: String? = null,
    @ColumnInfo(name = "last_vote") val lastVote: Long? = null
) {

    // No arg constructor required for FirebaseUI
    constructor() : this("")
}


