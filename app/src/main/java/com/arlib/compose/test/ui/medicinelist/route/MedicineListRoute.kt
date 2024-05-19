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

package com.arlib.compose.test.ui.medicinelist.route

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arlib.compose.test.di.RepoModule
import com.arlib.compose.test.repo.MedicineRepository
import com.arlib.compose.test.ui.medicinelist.MedicineListScreen
import com.arlib.compose.test.ui.medicinelist.viewmodel.MainActivityViewModel
import com.arlib.compose.test.ui.medicinelist.viewmodel.MainActivityViewModelFactory

@Composable
fun MedicineListRoute(
    email :String,
    itemClick: () -> Unit,
    onNavUp: () -> Unit,
) {

    val retrofitClient = RepoModule().provideRetrofit()
    val repo = MedicineRepository(retrofitClient)
    val mainActivityViewModel: MainActivityViewModel = viewModel(factory = MainActivityViewModelFactory(repo))

    MedicineListScreen(
        viewModel = mainActivityViewModel,
        email = email,
        itemClick = itemClick,
        onNavUp = onNavUp)

}
