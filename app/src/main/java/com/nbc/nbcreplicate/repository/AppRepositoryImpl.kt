package com.nbc.nbcreplicate.repository

import android.content.Context
import com.google.gson.Gson
import com.nbc.nbcreplicate.models.HomePage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

interface AppRepositoryImpl {

    suspend fun getHomePageData(): Flow<HomePage>

}