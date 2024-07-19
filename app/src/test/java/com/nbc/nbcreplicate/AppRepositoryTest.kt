package com.nbc.nbcreplicate

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nbc.nbcreplicate.models.HomePage
import com.nbc.nbcreplicate.repository.AppRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class AppRepositoryTest {


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: AppRepository
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        repository = AppRepository(context)
    }

    @Test
    fun testGetHomePageData() = runBlocking {
        // Updated JSON data based on the provided structure
        val json = """{
            "page": "HOMEPAGE",
            "shelves": [
                {
                    "title": "Continue Watching",
                    "type": "Shelf",
                    "items": [
                        {
                            "type": "Live",
                            "tagline": "WATCH NBC NEWS NOW LIVE",
                            "title": "Hallie Jackson NOW",
                            "subtitle": "Live News Streaming",
                            "image": "https://img.nbc.com/sites/nbcunbc/files/images/2021/2/04/NBC-News_2048_1152.jpg"
                        }
                    ]
                },
                {
                    "title": "Trending Now",
                    "type": "Shelf",
                    "items": [
                        {
                            "type": "Show",
                            "title": "This Is Us",
                            "image": "https://img.nbc.com/sites/nbcunbc/files/images/2022/2/16/ThisIsUs-S6-KeyArt-Logo-Vertical-852x1136-1.jpg"
                        }
                    ]
                }
            ]
        }"""
        val inputStream = json.byteInputStream()
        Mockito.`when`(context.assets.open("homepage.json")).thenReturn(inputStream)

        val type = object : TypeToken<HomePage>() {}.type
        val expected: HomePage = Gson().fromJson(json, type)

        val homePage = repository.getHomePageData().first()

        assertEquals(expected, homePage)
    }
}