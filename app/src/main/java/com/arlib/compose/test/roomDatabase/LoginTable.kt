package com.arlibs.yagna.roomDatabase

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "LoginDetails")
class LoginTable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id = 0

    @ColumnInfo(name = "Email")
    var email: String? = null

    @ColumnInfo(name = "Password")
    var password: String? = null


}