package com.arlibs.yagna.roomDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface LoginDao {
    @Insert
    suspend fun insertDetails(data: LoginTable) : Long

    @get:Query("select * from LoginDetails")
    val details: LiveData<List<LoginTable?>?>?

    @Query("SELECT * FROM LoginDetails WHERE id = :id LIMIT 1")
    fun getUserById(id: Int): LoginTable?

    @Query("SELECT * from LoginDetails where email =:username")
    suspend fun getUserByUsername(username: String): List<LoginTable?>?

    @Query("SELECT * from LoginDetails where email =:username And Password =:password LIMIT 1")
    suspend fun loginWithUsernamePassword(username: String, password : String ): LoginTable?

    @Query("delete from LoginDetails")
    suspend fun deleteAllData()
}