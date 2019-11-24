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

package com.sadraii.shouldi.data

import android.net.Uri
import androidx.room.TypeConverter
import java.time.Instant
import java.util.Date

class Converters {

    companion object {

        @TypeConverter
        @JvmStatic
        fun fromInstant(value: Instant?): Long? {
            return value?.toEpochMilli()
        }

        @TypeConverter
        @JvmStatic
        fun toInstant(value: Long?): Instant? {
            return value?.let {
                Instant.ofEpochMilli(it)
            }
        }

        @TypeConverter
        @JvmStatic
        fun fromUri(value: Uri?): String? {
            return value?.toString()
        }

        @TypeConverter
        @JvmStatic
        fun toUri(value: String?): Uri? {
            return value?.let {
                Uri.parse(it)
            }
        }

        @TypeConverter
        @JvmStatic
        fun fromDate(value: Date?): Long? {
            return value?.time
        }

        @TypeConverter
        @JvmStatic
        fun toDate(value: Long?): Date? {
            return value?.let {
                Date(it)
            }
        }
    }
}

