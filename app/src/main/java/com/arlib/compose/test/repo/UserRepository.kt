/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.arlib.compose.test.repo

import androidx.compose.runtime.Immutable
import androidx.lifecycle.MutableLiveData
import com.arlibs.yagna.roomDatabase.LoginDao
import com.arlibs.yagna.roomDatabase.LoginTable

sealed class User {
    @Immutable
    data class LoggedInUser(val email: String) : User()
    object GuestUser : User()
    object NoUserLoggedIn : User()
}

/**
 * Repository that holds the logged in user.
 *
 * In a production app, this class would also handle the communication with the backend for
 * sign in and sign up.
 */
object UserRepository {
    var loginDao : LoginDao? = null

    val _oldOrNewUser = MutableLiveData<String>()


    private var _user: User = User.NoUserLoggedIn
    val user: User
        get() = _user

    @Suppress("UNUSED_PARAMETER")
    suspend fun signIn(email: String, password: String): List<LoginTable?>? {
        _user = User.LoggedInUser(email)
        val data = LoginTable()
        data.email =email
        data.password = password

        var reuslt  = loginDao?.getUserByUsername(email)
        if(reuslt.isNullOrEmpty())
        {
            loginDao?.insertDetails(data)
            reuslt  = loginDao?.getUserByUsername(email)
            _oldOrNewUser.postValue("New user saved successfully : $email")

        }
        else {
            _oldOrNewUser.postValue("Already saved user: $email")
        }
        return reuslt
    }

    fun signInAsGuest() {
        _user = User.GuestUser
    }


    fun logout() {
        _user = User.NoUserLoggedIn
    }

    fun isKnownUserEmail(email: String): Boolean {
        // if the email contains "sign up" we consider it unknown
        return !email.contains("signup")
    }

    fun setUpDao(dao: LoginDao?) {
        loginDao = dao
    }

}
