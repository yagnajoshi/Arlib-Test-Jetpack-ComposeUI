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

package com.arlib.compose.test

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arlib.compose.test.theme.ArlibTestTheme
import com.arlib.compose.test.ui.signin.viewmodel.SignInViewModel
import com.arlib.compose.test.ui.signin.viewmodel.SignInViewModelFactory
import com.arlibs.yagna.roomDatabase.LoginDatabase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val signInViewModel: SignInViewModel = viewModel(factory = SignInViewModelFactory())
            val preferences: SharedPreferences = getSharedPreferences("appPref", MODE_PRIVATE)
            signInViewModel.preferences = preferences

            signInViewModel.initDB(LoginDatabase.getDatabase(application))

            ArlibTestTheme {
                ArlibNavHost()
            }
        }

    }
}
