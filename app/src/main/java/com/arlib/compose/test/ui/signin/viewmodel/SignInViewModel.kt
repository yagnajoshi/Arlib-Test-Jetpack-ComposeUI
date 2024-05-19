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

package com.arlib.compose.test.ui.signin.viewmodel

import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.arlib.compose.test.repo.UserRepository
import com.arlibs.yagna.roomDatabase.LoginDatabase
import com.arlibs.yagna.roomDatabase.LoginTable
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class SignInViewModel(private val userRepository: UserRepository) : ViewModel() {

    var user = userRepository.user
    var preferences : SharedPreferences? = null
    val oldOrNewUser: LiveData<String?> = userRepository._oldOrNewUser

    /**
     * Consider all sign ins successful
     */
    fun signIn(
        email: String,
        password: String,
        onSignInComplete: (email: String) -> Unit,
    ) {
        viewModelScope.launch {
            userRepository.signIn(email, password)
            Handler(Looper.getMainLooper()).postDelayed({
                onSignInComplete(email)
            },1500)
        }

    }

    fun signInAsGuest(
        onSignInComplete: () -> Unit,
    ) {
        userRepository.signInAsGuest()
        onSignInComplete()
    }

    fun initDB(db: LoginDatabase?) {

        val loginDao = db?.loginDoa()

        userRepository.setUpDao(loginDao)

        user = userRepository.user
        if(!(preferences?.getBoolean("isDataAdded",false))!!) {
            Executors.newSingleThreadScheduledExecutor()
                .execute {
                    viewModelScope.launch {
                        loginDao?.deleteAllData()
                        val data = LoginTable()
                        data.id = 0
                        data.email = "y@g.com"
                        data.password = "123456"
                        loginDao?.insertDetails(data)

                        val data2 = LoginTable()
                        data2.id = 0
                        data2.email = "test@g.com"
                        data2.password = "123456"
                        loginDao?.insertDetails(data2)

                        Log.e("LoginDatabase", "DB created with one entry")
                        preferences?.edit()?.putBoolean("isDataAdded", true)?.apply()
                    }



                }
        }
    }

    fun resetOldNewUserText() {
        userRepository._oldOrNewUser.postValue("")
    }
}

class SignInViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SignInViewModel::class.java)) {
            return SignInViewModel(UserRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
