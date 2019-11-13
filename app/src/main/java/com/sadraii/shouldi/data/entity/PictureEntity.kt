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
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.UUID

@Entity(
    tableName = "pictures",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("user_id"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["user_id"])]
)
data class PictureEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "picture_url") val pictureUrl: String = "",
    @ColumnInfo(name = "created") val created: Instant = Instant.now(),
    @ColumnInfo(name = "yes_count") var yesCount: Int = 0,
    @ColumnInfo(name = "no_count") var noCount: Int = 0,
    @ColumnInfo(name = "featured_count") var featuredCount: Int = 0,
    @ColumnInfo(name = "featured_time") var featuredTime: Instant? = null,
    @ColumnInfo(name = "position_time") var positionTime: Instant? = null,
    @ColumnInfo(name = "expo_fallback_scale") var expoFallbackScale: Int = 0,
    @ColumnInfo(name = "caption") val caption: String = ""

)

// data class PictureEntityFirebase(
//     val id: String,
//     val userId: String,
//     val pictureUrl: String,
//     val created: Long,
//     var yesCount: Int,
//     var noCount: Int,
//     var featuredCount: Int,
//     var featuredTime: Long?,
//     var positionTime: Long?,
//     var expoFallbackScale: Int,
//     val caption: String
// )
//
// fun PictureEntity.toFB() = mapOf<String, Any?>(
//     "id" to id,
//     "userId" to userId,
//     "pictureUrl" to pictureUrl,
//     "created" to created.epochSecond,
//     "yesCount" to yesCount,
//     "noCount" to noCount,
//     "featuredCount" to featuredCount,
//     "featuredTime" to featuredTime?.epochSecond,
//     "positionTime" to positionTime?.epochSecond,
//     "expoFallbackScale" to expoFallbackScale,
//     "caption" to caption
// )
//
// fun PictureEntity.toFirebase() = PictureEntityFirebase(
//     id,
//     userId,
//     pictureUrl,
//     created.epochSecond,
//     yesCount,
//     noCount,
//     featuredCount,
//     featuredTime?.epochSecond,
//     positionTime?.epochSecond,
//     expoFallbackScale,
//     caption
// )
//
// fun PictureEntityFirebase.fromFirebase() = PictureEntity(
//     id,
//     userId,
//     pictureUrl,
//     Instant.ofEpochMilli(created),
//     yesCount,
//     noCount,
//     featuredCount,
//     featuredTime?.let { Instant.ofEpochMilli(it) },
//     positionTime?.let { Instant.ofEpochMilli(it) },
//     expoFallbackScale,
//     caption
// )