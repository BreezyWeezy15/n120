package com.nbc.nbcreplicate.screens

import com.nbc.nbcreplicate.R
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.ImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.nbc.nbcreplicate.models.Item
import com.nbc.nbcreplicate.viewmodels.AppViewModel


@Composable
fun HomePageScreen(appViewModel: AppViewModel) {
    val uiState by appViewModel.dataState.collectAsState()
    when (val state = uiState) {
        is AppViewModel.UiStates.SUCCESS -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(state.homePage.shelves) { shelf ->
                    Text(
                        text = shelf.title,
                        style = MaterialTheme.typography.headlineSmall.copy(color = Color.White),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .horizontalScroll(rememberScrollState())
                    ) {
                        val  isContinueWatching = shelf.title == "Trending Now" || shelf.title == "Latest Episodes"
                        shelf.items.forEach { item ->
                            ShelfItem(item = item, isContinueWatching = isContinueWatching)
                        }
                    }
                }
            }
        }
        is AppViewModel.UiStates.LOADING -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Loading...", color = Color.White)
            }
        }
        is AppViewModel.UiStates.ERROR -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${state.error}", color = Color.White)
            }
        }
        AppViewModel.UiStates.INITIAL -> Unit
    }
}


@Composable
fun ShelfItem(item: Item, isContinueWatching: Boolean) {

    val imageHeight = if (isContinueWatching) 180.dp else 280.dp
    val imageWidth = if (isContinueWatching) 250.dp else 180.dp

    Column(
        modifier = Modifier
            .padding(6.dp)

    ) {
        val painter = rememberImagePainter(
            data = item.image,
            builder = {
                crossfade(true)
                scale(Scale.FILL)
                error(R.drawable.no_image)
            }
        )

        Box(
            modifier = Modifier
                .height(imageHeight)
                .width(imageWidth)
        ) {
            Image(
                painter = painter,
                contentDescription = item.title,
                contentScale =  ContentScale.Crop,
            )
        }

        Spacer(modifier = Modifier.height(4.dp))
        Text(text = item.title, maxLines = 1, style = MaterialTheme.typography.bodyMedium.copy(color = Color.White))
        item.subtitle?.let {
            Text(text = it, maxLines = 1, style = MaterialTheme.typography.bodyMedium.copy(color = Color.White))
        }
        item.labelBadge?.let {
            Text(text = it, maxLines = 1, style = MaterialTheme.typography.bodyMedium.copy(color = Color.White))
        }
    }
}