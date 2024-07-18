package com.nbc.nbcreplicate

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nbc.nbcreplicate.models.HomePage
import com.nbc.nbcreplicate.repository.AppRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class AppRepositoryTest {

    private lateinit var repository: AppRepository
    private lateinit var context: Context

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        context = ApplicationProvider.getApplicationContext()
        repository = AppRepository(context)
    }

    @Test
    fun testGetHomePageData() = runTest {
        // Mock the input stream
        val json = """{
            "title": "Home Page Title",
            "description": "This is a description."
        }"""
        val inputStream = json.byteInputStream()
        Mockito.`when`(context.assets.open("homepage.json")).thenReturn(inputStream)

        val type = object : TypeToken<HomePage>() {}.type
        val expected: HomePage = Gson().fromJson(json, type)

        val homePage = repository.getHomePageData().first()

        assertEquals(expected, homePage)
    }
}