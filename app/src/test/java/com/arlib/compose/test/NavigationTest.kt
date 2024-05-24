package com.arlib.compose.test

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arlib.compose.test.di.RepoModule
import com.arlib.compose.test.repo.MedicineRepository
import com.arlib.compose.test.ui.medicinelist.viewmodel.MainActivityViewModel
import com.arlib.compose.test.ui.medicinelist.viewmodel.MainActivityViewModelFactory
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var navController: TestNavHostController

    private var viewModel: MainActivityViewModel? = null

    @Before
    fun setupAppNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            ArlibNavHost(navController = navController)

            val retrofitClient = RepoModule().provideRetrofit()
            val repo = MedicineRepository(retrofitClient)
            viewModel =
                viewModel(factory = MainActivityViewModelFactory(repo))

        }
    }
    @Test
    fun appNavHost_verifyStartDestination() {
        composeTestRule
            .onNodeWithContentDescription("Sign in page Image")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Arlib Test")
            .assertIsDisplayed()

    }
    @Test
    fun appNavHost_verifyNavigationFromSignInToMedicineList() {

        // Verify that we are on Screen 1 initially
        composeTestRule.onNodeWithText("Sign in as guest").assertExists()

        // Perform click to navigate to Screen 2
        composeTestRule.onNodeWithText("Sign in as guest")
            .performScrollTo()
            .performClick()

        //Check the destination page is displayed or not
        composeTestRule.onNodeWithTag("Greeting User")
            .assertExists()

        //Check weather greeting text is displayed correctly
        composeTestRule.onNodeWithText(viewModel?.getTimeWiseGreeting()+ ", "+ "Guest")
            .assertIsDisplayed()

    }
}
