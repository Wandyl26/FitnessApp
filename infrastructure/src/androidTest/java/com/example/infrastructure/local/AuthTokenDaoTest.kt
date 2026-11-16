package com.example.infrastructure.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.infrastructure.local.dao.AuthTokenDao
import com.example.infrastructure.local.entity.AuthToken
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthTokenDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: AuthTokenDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.authTokenDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertToken_and_getToken_returnsStoredValue() = runTest {
        dao.insertToken(AuthToken(token = "abc-123", username = "wdiaz"))

        val stored = dao.getToken()

        assertEquals("abc-123", stored?.token)
        assertEquals("wdiaz", stored?.username)
    }

    @Test
    fun insertToken_twice_replacesExistingToken() = runTest {
        dao.insertToken(AuthToken(token = "old", username = "user1"))
        dao.insertToken(AuthToken(token = "new", username = "user2"))

        val stored = dao.getToken()

        assertEquals("new", stored?.token)
        assertEquals("user2", stored?.username)
    }

    @Test
    fun clearToken_removesStoredToken() = runTest {
        dao.insertToken(AuthToken(token = "abc", username = "wdiaz"))
        dao.clearToken()

        assertNull(dao.getToken())
    }
}