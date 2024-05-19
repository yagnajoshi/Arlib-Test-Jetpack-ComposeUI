/*
 * Copyright 2022 The Android Open Source Project
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

package com.arlib.compose.test.survey

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arlib.compose.test.ui.signin.state.isEmailOrUsernameValid
import com.arlib.compose.test.ui.signin.state.isPasswordValid
import com.google.common.truth.Truth
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInPageTest {

    @Before
    fun setUp() {
       // No Implementation Yet
    }

    @Test
    fun signInEmailUsernameValidationTest() {

        // Test empty emai/username
        Truth.assertThat(isEmailOrUsernameValid("")).isFalse()

        // Test arbitrary correct format email
        Truth.assertThat(isEmailOrUsernameValid("yagna@arlib.com")).isTrue()

        // Test arbitrary wrong format email
        Truth.assertThat(isEmailOrUsernameValid("yagna@")).isFalse()

        // Test arbitrary username
        Truth.assertThat(isEmailOrUsernameValid("Yagna")).isTrue()

        // Test arbitrary username
        Truth.assertThat(isEmailOrUsernameValid("Yagna")).isTrue()


    }
    @Test
    fun signInPasswordValidationTest() {

        // Test arbitrary >3 length password
        Truth.assertThat(isPasswordValid("12345")).isTrue()

        // Test arbitrary <3 length password
        Truth.assertThat(isPasswordValid("11")).isFalse()

    }
}
