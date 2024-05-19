/*
 * Copyright 2023 The Android Open Source Project
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

package com.arlib.compose.test.ui.signin.route

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arlib.compose.test.ui.signin.SignInScreen
import com.arlib.compose.test.ui.signin.viewmodel.SignInViewModel
import com.arlib.compose.test.ui.signin.viewmodel.SignInViewModelFactory
import com.arlibs.yagna.roomDatabase.LoginDatabase

@Composable
fun SignInRoute(
    onSignInSubmitted: (email: String) -> Unit,
    onSignInAsGuest: () -> Unit,
    onNavUp: () -> Unit,
) {
    val signInViewModel: SignInViewModel = viewModel(factory = SignInViewModelFactory())


    SignInScreen(
        viewModel = signInViewModel,
        email = "",
        onSignInSubmitted = { email, password ->

            signInViewModel.signIn(email, password, onSignInSubmitted)
        },
        onSignInAsGuest = {
            signInViewModel.signInAsGuest(onSignInAsGuest)
        },
        onNavUp = onNavUp,
    )
}


