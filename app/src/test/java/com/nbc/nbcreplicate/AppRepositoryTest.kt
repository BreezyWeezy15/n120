package com.nbc.nbcreplicate

import android.content.Context
import android.content.res.AssetManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nbc.nbcreplicate.models.HomePage
import com.nbc.nbcreplicate.repository.AppRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.io.FileNotFoundException

class AppRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: AppRepository
    private lateinit var context: Context

    @Before
    fun setUp() {
        context  =  mock(Context::class.java)
        repository = AppRepository(context)
    }


    /// Test and make sure data match
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
        val assetManager = mock(android.content.res.AssetManager::class.java)
        `when`(context.assets).thenReturn(assetManager)
        `when`(context.assets.open("homepage.json")).thenReturn(inputStream)

        val type = object : TypeToken<HomePage>() {}.type
        val expected: HomePage = Gson().fromJson(json, type)

        val homePage = repository.getHomePageData().first()

        assertEquals(expected, homePage)
    }

    // Test to fail providing json with different data
    @Test
    fun testGetHomePageData2() = runBlocking {

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
        val assetManager = mock(android.content.res.AssetManager::class.java)
        `when`(context.assets).thenReturn(assetManager)
        `when`(context.assets.open("homepage.json")).thenReturn(inputStream)

        // Create a new json with different values to cause test to fail


        val modifiedJson = """{
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
                        "title": "Different Show Title", 
                        "image": "https://img.nbc.com/sites/nbcunbc/files/images/2022/2/16/ThisIsUs-S6-KeyArt-Logo-Vertical-852x1136-1.jpg"
                    }
                ]
            }
        ]
    }"""
        val type = object : TypeToken<HomePage>() {}.type
        val expected: HomePage = Gson().fromJson(modifiedJson, type)

        val homePage = repository.getHomePageData().first()

        assertEquals(expected, homePage) // This will fail due to mismatch
    }


}
