# NBC Replicate App

This project is a simplified replica of the NBC app, built using Jetpack Compose, Hilt for dependency injection, and Kotlin Coroutines with StateFlow for state management.

## Usage

1. Run the application on an Android device or emulator.
2. The Home Screen will display a list of shelves with items, categorized under different titles.
3. Each shelf can be scrolled horizontally to view the items.

## Project Structure

### 1. Application Class

The `BaseApplication` class is annotated with `@HiltAndroidApp` to initiate Hilt's code generation, including a base class for the application that serves as the application-level dependency container.

### 2. Dependency Injection (DI) Setup

The `AppModule` class provides the necessary dependencies for the application.

### 3. Models

The data models represent the structure of the Home Page, Shelves, and Items.

### 4. Repository

The `AppRepositoryImpl` interface defines a method to fetch home page data.

### 5. ViewModel

The `AppViewModel` class manages UI-related data and communicates with the repository.

### 6. Composables

The `HomePageScreen` composable function renders the home page content.

Other supporting composables like `Shelf` and `ShelfItem` handle the display of shelves and items respectively.

## Dependencies

- Jetpack Compose
- Hilt
- Kotlin Coroutines
- StateFlow
- Glide (for image loading)

## Details

### How the Code Works

1. **Application Class**:
   - `BaseApplication` class is annotated with `@HiltAndroidApp`, which triggers Hilt's code generation. It acts as the application-level dependency container.

2. **Dependency Injection Setup**:
   - `AppModule` is a Hilt module annotated with `@Module` and `@InstallIn(SingletonComponent::class)`. It provides the necessary dependencies, such as `AppRepositoryImpl`.

3. **Models**:
   - The data classes `HomePage`, `Shelf`, and `Item` define the structure of the data used in the app. Each `HomePage` contains multiple `Shelf` objects, and each `Shelf` contains multiple `Item` objects.

4. **Repository**:
   - `AppRepositoryImpl` is an interface that defines a method `getHomePageData()` to fetch home page data asynchronously using Kotlin Coroutines and returns a `Flow<HomePage>`.

5. **ViewModel**:
   - `AppViewModel` is annotated with `@HiltViewModel` and injected with `AppRepository`. It manages the UI state using a `MutableStateFlow` which holds `UiStates` (INITIAL, LOADING, SUCCESS, ERROR).
   - The `getData()` function fetches the home page data, updates the state to LOADING, and then collects the data from the repository. If successful, it updates the state to SUCCESS; if an error occurs, it updates the state to ERROR.

6. **Composables**:
   - `HomePageScreen` observes the `dataState` from the `AppViewModel` and updates the UI based on the state (SUCCESS, LOADING, ERROR, INITIAL).
   - `Shelf` composable displays a shelf's title and a horizontally scrollable row of items.
   - `ShelfItem` composable displays individual items with an image, title, subtitle, and other details. It uses Glide for image loading.

### How to Use

1. **Initialize Hilt**:
   - Ensure that the `BaseApplication` class is set up and annotated with `@HiltAndroidApp`.

2. **Inject Dependencies**:
   - Use Hilt to inject dependencies into your ViewModel and other classes as needed. For example, `AppViewModel` is injected with `AppRepository`.

3. **Fetch Data**:
   - Call `getData()` from the `AppViewModel` to initiate data fetching and observe the `dataState` to update the UI accordingly.

4. **Compose UI**:
   - Use Jetpack Compose to build the UI components, such as `HomePageScreen`, `Shelf`, and `ShelfItem`. Observe the ViewModel's state and update the UI reactively.

## App Testing

This project includes unit tests for the `AppRepository` and `AppViewModel` classes using JUnit4 and Mockito for mocking dependencies. The goal of these tests is to ensure that the data retrieved from the repository matches the expected data and that the ViewModel correctly processes the data and handles different states (loading, success, error).

### Project Structure

- **AppRepositoryTest**: Contains unit tests for the `AppRepository` class.
- **AppViewModelTest**: Contains unit tests for the `AppViewModel` class.

### AppRepositoryTest

The `AppRepositoryTest` class is responsible for testing the `AppRepository` class, which fetches data from a JSON file.

#### Setup

- Initialize the `AppRepository` and `Context` instances.
- Use Mockito to mock the `Context` and `AssetManager`.

#### Test Cases

- **testGetHomePageDataSuccess**
  - This test ensures that the data fetched from the repository matches the expected JSON structure.
  - It reads a JSON file from the assets, parses it, and compares it with the expected data.

- **testGetHomePageDataFail**
  - This test is designed to fail by providing different data to ensure the test detects discrepancies.
  - It reads a modified JSON file with different values and compares it with the expected data, causing the test to fail due to a mismatch.

### AppViewModelTest

The `AppViewModelTest` class tests the `AppViewModel` class to ensure it correctly processes data and handles different states.

#### Setup

- Initialize the `AppViewModel` and mock the `AppRepository`.
- Use a `TestCoroutineDispatcher` and `TestCoroutineScope` for coroutine testing.
- Observe the `dataState` LiveData in the ViewModel to verify state changes.

#### Test Cases

- **testGetDataSuccess**
  - This test verifies that the ViewModel correctly processes the data and enters the success state.
  - It mocks the repository to return the expected data and checks if the ViewModel updates the UI state to success.

- **testGetDataError**
  - This test ensures that the ViewModel correctly handles an error state.
  - It mocks the repository to throw an exception and checks if the ViewModel updates the UI state to error with the appropriate message.
