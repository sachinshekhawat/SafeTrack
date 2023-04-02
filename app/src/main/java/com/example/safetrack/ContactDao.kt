package com.example.safetrack

import android.content.Context
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactDao {

    @Insert
    suspend fun insert(contactModel: ContactModel)

    @Insert
    suspend fun insertAll(contactModelList: List<ContactModel>)

    @Query("SELECT * FROM contact")
     fun getAllContacts():List<ContactModel>


}