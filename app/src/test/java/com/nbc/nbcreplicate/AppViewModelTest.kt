package com.nbc.nbcreplicate

import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nbc.nbcreplicate.models.HomePage
import com.nbc.nbcreplicate.repository.AppRepository
import com.nbc.nbcreplicate.viewmodels.AppViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class AppViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: AppRepository

    private lateinit var viewModel: AppViewModel

    @Mock
    private lateinit var observer: Observer<AppViewModel.UiStates>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = AppViewModel(repository)
        viewModel.dataState.observeForever(observer)
    }

    @Test
    fun testGetDataSuccess() = runTest {
        val json = """{
            "title": "Home Page Title",
            "description": "This is a description."
        }"""
        val homePage = Gson().fromJson(json, object : TypeToken<HomePage>() {}.type)

        Mockito.`when`(repository.getHomePageData()).thenReturn(flow {
            emit(homePage)
        })

        viewModel.getData()

        assertEquals(UiStates.LOADING, viewModel.dataState.value)
        assertEquals(UiStates.SUCCESS(homePage), viewModel.dataState.value)
    }

    @Test
    fun testGetDataError() = runTest {
        val errorMessage = "Error occurred"
        Mockito.`when`(repository.getHomePageData()).thenThrow(RuntimeException(errorMessage))

        viewModel.getData()

        assertEquals(UiStates.LOADING, viewModel.dataState.value)
        assertEquals(UiStates.ERROR(errorMessage), viewModel.dataState.value)
    }
}