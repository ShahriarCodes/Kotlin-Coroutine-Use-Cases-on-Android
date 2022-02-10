package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase6

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import timber.log.Timber

class RetryNetworkRequestViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    fun performNetworkRequest() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            val numberOfRetries = 2
            try {
                retry(numberOfRetries) { loadRecentAndroidVersions() }
            } catch (e: Exception) {
                Timber.e(e)
                uiState.value = UiState.Error("Network request failed")
            }

        }
    }

    private suspend fun <T> retry(numberOfRetries: Int, block: suspend () -> T): T {
        repeat(numberOfRetries) {
            try {
                return block()

            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        return block()
    }

    fun performNetworkRequest1() {
        uiState.value = UiState.Loading

        viewModelScope.launch {
            val numberOfRetries = 2
            try {
                repeat(numberOfRetries)
                {
                    try {
                        loadRecentAndroidVersions()
                        return@launch // leave the control flow after successful network request
                    } catch (e: Exception) {
                        Timber.e(e)
                    }
                }

                // 3rd try
                loadRecentAndroidVersions()
            } catch (e: Exception) {
                Timber.e(e)
                uiState.value = UiState.Error("Network request failed")
            }

        }
    }

    private suspend fun loadRecentAndroidVersions() {
        val recentAndroidVersions = api.getRecentAndroidVersions()
        uiState.value = UiState.Success(recentAndroidVersions)
    }

}