package com.smitcoderx.task.aerologixtask.Db

import androidx.room.*
import com.smitcoderx.task.aerologixtask.Model.Data
import kotlinx.coroutines.flow.Flow

@Dao
interface AerologixDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addData(data: List<Data>)

    @Query("SELECT * FROM Data")
    fun getAllDataFromDB(): Flow<List<Data>>

    @Query("DELETE FROM Data")
    suspend fun deleteDataFromDB()


}