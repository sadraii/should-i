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
import java.util.Date

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
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "picture_url") val pictureUrl: String? = null,
    @ColumnInfo(name = "created") val created: Date = Date(0),
    @ColumnInfo(name = "yes_count") var yesCount: Int = 0,
    @ColumnInfo(name = "no_count") var noCount: Int = 0,
    @ColumnInfo(name = "featured_count") var featuredCount: Int = 0,
    @ColumnInfo(name = "featured_time") var featuredTime: Date? = null,
    @ColumnInfo(name = "position_time") var positionTime: Date? = null,
    @ColumnInfo(name = "expo_fallback_scale") var expoFallbackScale: Byte = 0,
    @ColumnInfo(name = "caption") val caption: String = "",
    @ColumnInfo(name = "user_id") val userId: Int = 0
)
//no caption details - caption is superimposed on pic when taken



