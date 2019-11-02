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
import java.util.Date

@Entity(tableName = "pictures")
data class PictureEntity(@PrimaryKey(autoGenerate = true) @ColumnInfo(name = "picture_id") val pictureId: Int,
    @ColumnInfo(name = "uri") val uri: String?,
    @ColumnInfo(name = "created") val created: Date,
    @ColumnInfo(name = "yes_count") var yesCount: Int,
    @ColumnInfo(name = "no_count") var noCount: Int,
    @ColumnInfo(name = "featured_count") var featuredCount: Int,
    @ColumnInfo(name = "featured_time") var featuredTime: Date,
    @ColumnInfo(name = "position_time") var positionTime: Date,
    @ColumnInfo(name = "expo_fallback_scale") var expoFallbackScale: Int)
//owner details
//no caption details - caption is superimposed on pic when taken

