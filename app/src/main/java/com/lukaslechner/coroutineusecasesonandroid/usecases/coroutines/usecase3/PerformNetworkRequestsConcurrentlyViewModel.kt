package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class PerformNetworkRequestsConcurrentlyViewModel(
    private val mockApi: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequestsSequentially() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val oreoFeatures = mockApi.getAndroidVersionFeatures(27)
                val pieFeatures = mockApi.getAndroidVersionFeatures(28)
                val android10Features = mockApi.getAndroidVersionFeatures(29)

                val versionFeatures = listOf(oreoFeatures, pieFeatures, android10Features)
                uiState.value = UiState.Success(versionFeatures)
            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network request failed!")
            }
        }
    }

    fun performNetworkRequestsConcurrently() {
        uiState.value = UiState.Loading

        val oreoFeatures = viewModelScope.async { mockApi.getAndroidVersionFeatures(27) }
        val pieFeatures = viewModelScope.async { mockApi.getAndroidVersionFeatures(28) }
        val android10Features = viewModelScope.async { mockApi.getAndroidVersionFeatures(29) }

        viewModelScope.launch {
            try {
//                val versionFeatures = listOf(
//                    oreoFeatures.await(), // coroutine already running.. await doesn't initialize them
//                    pieFeatures.await(),
//                    android10Features.await()
//                )

                val versionFeatures = awaitAll(
                    oreoFeatures,
                    pieFeatures,
                    android10Features
                )

                uiState.value = UiState.Success(versionFeatures)
            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network request failed!")
            }
        }
    }


    fun performNetworkRequestsConcurrently2() {
        uiState.value = UiState.Loading
        viewModelScope.launch {
            try {
                val oreoFeatures = async { mockApi.getAndroidVersionFeatures(27) }
                val pieFeatures = async { mockApi.getAndroidVersionFeatures(28) }
                val android10Features = async { mockApi.getAndroidVersionFeatures(29) }
                val versionFeatures = listOf(
                    oreoFeatures.await(),
                    pieFeatures.await(),
                    android10Features.await()
                )
                uiState.value = UiState.Success(versionFeatures)
            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network request failed!")
            }
        }
    }
}