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

package com.arlib.compose.test.ui.signin.state

import java.util.regex.Pattern

// Consider an email valid if there's some text before and after a "@"
private const val EMAIL_VALIDATION_REGEX = "^(.+)@(.+)\$"

class EmailState(val email: String? = null) :
    TextFieldState(validator = ::isEmailOrUsernameValid, errorFor = ::emailValidationError) {

    init {
        email?.let {
            text = it
        }
    }
}

/**
 * Returns an error to be displayed or null if no error was found
 */
private fun emailValidationError(email: String): String {
    if(email.contains("@")) {
        return "Invalid email: $email"
    }
    else {
        return "Invalid email or username: $email"

    }
}

 fun isEmailOrUsernameValid(email: String): Boolean {
     if(email.contains("@")) {
         return Pattern.matches(EMAIL_VALIDATION_REGEX, email)
     }
     else {
         if(email.length>2){
             return true
         }
         else{
             return false
         }
     }
}

val EmailStateSaver = textFieldStateSaver(EmailState())
