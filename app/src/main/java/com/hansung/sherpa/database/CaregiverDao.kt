package com.hansung.sherpa.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface CaregiverDao {
    @Insert
    fun insertCaregiverData(caregiverData: CaregiverData)

    @Transaction
    @Query("SELECT * FROM caregiver_data")
    fun getAll() : List<CaregiverData>

    @Transaction
    @Query("UPDATE caregiver_data SET relation = :relation, tel_num = :telnum WHERE id = :id")
    fun updateCaregiver(id : Long, relation : String, telnum : String)

    @Transaction
    @Query("DELETE from caregiver_data WHERE id = :id")
    fun deleteCaregiver(id : Long)

    @Transaction
    @Query("SELECT id from caregiver_data WHERE relation = :relation and tel_num = :telnum")
    fun getCaregiverID(relation: String, telnum: String) : Long

}