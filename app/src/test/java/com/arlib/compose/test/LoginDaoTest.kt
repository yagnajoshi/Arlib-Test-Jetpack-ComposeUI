package com.arlib.compose.test
import android.content.Context
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arlibs.yagna.roomDatabase.LoginDao
import com.arlib.compose.test.roomDatabase.LoginDatabase
import com.arlibs.yagna.roomDatabase.LoginTable
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginDaoTest {
    private var database: LoginDatabase? = null
    private var loginDao: LoginDao? = null

    @Before
    fun setUp() {
        // Create an in-memory version of the database
        database = inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(),
            LoginDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        loginDao = database?.loginDoa()
    }

    @After
    fun tearDown() {
        database?.close()
    }

    /**
     *
     * Check data is getting stored in room db or not
     */
    @Test
    fun insertUser_andRetrieveByIdTest() = runBlocking {
        // Create a new user
        val user = LoginTable()

        user.email = "Yagna@arlib.com"
        user.password = "Yagna@123"

        // Insert user into database
        val id: Long? = loginDao?.insertDetails(user)

        // Retrieve user from database
        val retrievedUser: LoginTable? = id?.let { loginDao?.getUserById(it.toInt()) }

        // Check if the retrieved user matches the inserted user
        Assert.assertNotNull(retrievedUser)
        assertEquals(user.password, retrievedUser?.password)
        assertEquals(user.email, retrievedUser?.email)
    }

    /**
     * Positive Login Test with correct credentials
     */
    @Test
    fun loginWith_CorrectUserNameAndPasswordTest() = runBlocking {
        // Create a new user
        val user = LoginTable()

        user.email = "Yagna@arlib.com"
        user.password = "Yagna@123"

        // Insert user into database
        val id: Long? = loginDao?.insertDetails(user)


        // Retrieve user from database using username and password
        val loginCorrectUser: LoginTable? = id?.let { loginDao?.loginWithUsernamePassword("Yagna@arlib.com", "Yagna@123") }

        // Check if the logged in  user matches the inserted user
        Assert.assertNotNull(loginCorrectUser)
        assertEquals(user.password, loginCorrectUser?.password)
        assertEquals(user.email, loginCorrectUser?.email)
    }

    /**
     * Negative Login Test with wrong credentials
     *
     */
    @Test
    fun loginWith_IncorrectUserNameAndPasswordTest() = runBlocking {
        // Create a new user
        val user = LoginTable()

        user.email = "Yagna@arlib.com"
        user.password = "Yagna@123"

        // Insert user into database
        val id: Long? = loginDao?.insertDetails(user)


        // Retrieve user from database using username and password
        val retrivedUser: LoginTable? = id?.let { loginDao?.loginWithUsernamePassword("Yagna@arlib.com", "Yagna@456") }

        // Check if the logged in  user is Null or not
        Assert.assertNull(retrivedUser)

    }




}
