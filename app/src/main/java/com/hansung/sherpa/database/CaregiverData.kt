package com.hansung.sherpa.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "caregiver_data")
class CaregiverData(
    @ColumnInfo(name = "relation") var relation: String,
    @ColumnInfo(name = "tel_num") var telnum: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Long? = null
}