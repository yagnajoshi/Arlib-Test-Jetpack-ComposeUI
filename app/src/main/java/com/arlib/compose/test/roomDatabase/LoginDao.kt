package com.arlibs.yagna.roomDatabase

import androidx.room.Dao
import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LoginDao {
    @Insert
    suspend fun insertDetails(data: LoginTable)

    @get:Query("select * from LoginDetails")
    val details: LiveData<List<LoginTable?>?>?

    @Query("SELECT * from LoginDetails where email =:username")
    suspend fun login(username: String): List<LoginTable?>?

    @Query("delete from LoginDetails")
    suspend fun deleteAllData()
}