package com.nbc.nbcreplicate.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nbc.nbcreplicate.models.HomePage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import javax.inject.Inject

class AppRepository @Inject constructor(@ApplicationContext val context: Context) : AppRepositoryImpl {
    override suspend fun getHomePageData()  = flow {
        val inputStream = context.assets.open("homepage.json")
        val reader = InputStreamReader(inputStream)
        val homePage: HomePage = Gson().fromJson(reader, object : TypeToken<HomePage>() {}.type)
        emit(homePage)
    }.flowOn(Dispatchers.IO)

}