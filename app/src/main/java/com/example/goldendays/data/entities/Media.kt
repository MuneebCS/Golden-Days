package com.example.goldendays.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
 tableName = "media",
 foreignKeys = [
  ForeignKey(
   entity = Event::class,
   parentColumns = ["id"],
   childColumns = ["eventId"],
   onDelete = ForeignKey.CASCADE
  )
 ]
)
data class Media(
 @PrimaryKey(autoGenerate = true)
 val mediaId: Int = 0,
 val eventId: Int,
 val uri: ByteArray,
 val type: String
)
