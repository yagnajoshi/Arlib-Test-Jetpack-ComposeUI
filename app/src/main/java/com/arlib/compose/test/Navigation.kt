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

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arlib.compose.test.Destinations.MEDICINE_DETAIL_ROUTE
import com.arlib.compose.test.Destinations.MEDICINE_LIST_ROUTE
import com.arlib.compose.test.Destinations.SIGN_IN_ROUTE
import com.arlib.compose.test.ui.medicinelist.route.MedicineListRoute
import com.arlib.compose.test.ui.signin.route.SignInRoute


object Destinations {
    const val SIGN_IN_ROUTE = "signin"
    const val MEDICINE_LIST_ROUTE = "medicine_list/{email}"
    const val MEDICINE_DETAIL_ROUTE = "medicine_detail"
}

@Composable
fun ArlibNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = SIGN_IN_ROUTE,
    ) {

        composable(SIGN_IN_ROUTE) {
            SignInRoute(
                onSignInSubmitted = {
                    navController.navigate("medicine_list/$it")
                },
                onSignInAsGuest = {
                    navController.navigate("medicine_list/Guest")
                },
                onNavUp = {}//navController::popBackStack,
            )
        }


        composable(MEDICINE_LIST_ROUTE) {
            val startingEmail = it.arguments?.getString("email")
            MedicineListRoute(
                email = startingEmail?:"Guest" ,
                itemClick = {
                    //navController.navigate(MEDICINE_DETAIL_ROUTE)
                },
                onNavUp = {}//navController::popBackStack,
            )
        }

    }
}
