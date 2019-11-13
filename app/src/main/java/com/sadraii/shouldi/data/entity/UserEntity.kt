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

package com.sadraii.shouldi.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "user_name") val userName: String? = null,
    @ColumnInfo(name = "first_name") val firstName: String? = null,
    @ColumnInfo(name = "last_name") val lastName: String? = null,
    @ColumnInfo(name = "email") val email: String? = null,
    @ColumnInfo(name = "created") val created: Instant = Instant.now(),
    @ColumnInfo(name = "last_online") var lastOnline: Instant? = null
)

// data class UserEntityFirebase(
//     val id: String,
//     val userName: String?,
//     val firstName: String?,
//     val lastName: String?,
//     val email: String?,
//     val created: Long,
//     var lastOnline: Long?
// )
//
// fun UserEntity.toFirebase() = UserEntityFirebase(
//     id,
//     userName,
//     firstName,
//     lastName,
//     email,
//     created.epochSecond,
//     lastOnline?.epochSecond
// )
//
// fun UserEntityFirebase.fromFirebase() = UserEntity(
//     id,
//     userName,
//     firstName,
//     lastName,
//     email,
//     Instant.ofEpochMilli(created),
//     lastOnline?.let { Instant.ofEpochMilli(it) }
// )