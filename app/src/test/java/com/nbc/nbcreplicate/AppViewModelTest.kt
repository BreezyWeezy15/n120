import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nbc.nbcreplicate.models.HomePage
import com.nbc.nbcreplicate.repository.AppRepository
import com.nbc.nbcreplicate.viewmodels.AppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class AppViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: AppRepository

    private lateinit var viewModel: AppViewModel

    @Mock
    private lateinit var observer: Observer<AppViewModel.UiStates>

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = AppViewModel(repository)
        Dispatchers.setMain(testDispatcher)
        viewModel.dataState.asLiveData().observeForever(observer)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher
        testDispatcher.cleanupTestCoroutines() // Clean up the test dispatcher
    }

    @Test
    fun testGetDataSuccess() = testScope.runTest {
        // Updated JSON data
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

        val type = object : TypeToken<HomePage>() {}.type
        val expected: HomePage = Gson().fromJson(json, type)

        Mockito.`when`(repository.getHomePageData()).thenReturn(flow {
            emit(expected)
        })

        viewModel.getData()

        // Verify the loading state
        Mockito.verify(observer).onChanged(AppViewModel.UiStates.LOADING)

        // Wait for data to be processed
        kotlinx.coroutines.delay(100) // Adjust if necessary

        // Verify the success state with the expected data
        Mockito.verify(observer).onChanged(AppViewModel.UiStates.SUCCESS(expected))
        assertEquals(AppViewModel.UiStates.SUCCESS(expected), viewModel.dataState.value)
    }

    @Test
    fun testGetDataError() = testScope.runTest {
        val errorMessage = "Error occurred"
        Mockito.`when`(repository.getHomePageData()).thenThrow(RuntimeException(errorMessage))

        viewModel.getData()

        // Verify the loading state
        Mockito.verify(observer).onChanged(AppViewModel.UiStates.LOADING)

        // Wait for error to be processed
        kotlinx.coroutines.delay(100) // Adjust if necessary

        // Verify the error state with the error message
        Mockito.verify(observer).onChanged(AppViewModel.UiStates.ERROR(errorMessage))
        assertEquals(AppViewModel.UiStates.ERROR(errorMessage), viewModel.dataState.value)
    }
}
