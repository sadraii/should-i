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

import android.os.Parcel
import android.os.Parcelable
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
    @PrimaryKey val id: String = UUID.randomUUID().toString(), /* TODO Remove */
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "picture_url") var pictureUrl: String,
    @ColumnInfo(name = "created") val created: Long = Instant.now().toEpochMilli(),
    @ColumnInfo(name = "yes_votes") val yesVotes: Int = 0,
    @ColumnInfo(name = "no_votes") val noVotes: Int = 0,
    @ColumnInfo(name = "featured_count") val featuredCount: Int = 0,
    @ColumnInfo(name = "featured_time") val featuredTime: Long? = null,
    @ColumnInfo(name = "position_time") val positionTime: Long? = null,
    @ColumnInfo(name = "expo_fallback_scale") val expoFallbackScale: Int = 0,
    @ColumnInfo(name = "caption") val caption: String = ""
) : Parcelable {

    constructor() : this("", "", "") // TODO Needed for FirebaseUI?

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readLong(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readInt(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(userId)
        parcel.writeString(pictureUrl)
        parcel.writeLong(created)
        parcel.writeInt(yesVotes)
        parcel.writeInt(noVotes)
        parcel.writeInt(featuredCount)
        parcel.writeValue(featuredTime)
        parcel.writeValue(positionTime)
        parcel.writeInt(expoFallbackScale)
        parcel.writeString(caption)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PictureEntity> {
        override fun createFromParcel(parcel: Parcel): PictureEntity {
            return PictureEntity(parcel)
        }

        override fun newArray(size: Int): Array<PictureEntity?> {
            return arrayOfNulls(size)
        }
    }
}


