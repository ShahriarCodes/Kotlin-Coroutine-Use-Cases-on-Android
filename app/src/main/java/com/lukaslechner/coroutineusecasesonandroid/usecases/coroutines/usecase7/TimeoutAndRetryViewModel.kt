package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase7

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.VersionFeatures
import kotlinx.coroutines.*
import timber.log.Timber

class TimeoutAndRetryViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {
    val numberOfRetries = 2
    val timeOut = 1000L

    fun performNetworkRequest() {
        uiState.value = UiState.Loading
        val oreoFeatures = viewModelScope.async {
            retryWithTimeOut(numberOfRetries, timeOut) {
                api.getAndroidVersionFeatures(27)
            }
        }

        val pieFeatures = viewModelScope.async {
            retryWithTimeOut(numberOfRetries, timeOut) {
                api.getAndroidVersionFeatures(28)
            }
        }

        viewModelScope.launch {
            try {
                val versionFeatures = awaitAll(oreoFeatures, pieFeatures)

                uiState.value = UiState.Success(versionFeatures)
            } catch (exception: Exception) {
                uiState.value = UiState.Error("Network request failed!")
            }
        }
    }

    private suspend fun <T> retryWithTimeOut(
        numberOfRetries: Int,
        timeOut: Long,
        block: suspend () -> T
    ) = retry(numberOfRetries) {
        withTimeout(timeOut) {
            block()
        }
    }

    private suspend fun <T> retry(
        numberOfRetries: Int,
        delayBetweenRetries: Long = 100,
        block: suspend () -> T
    ): T {
        repeat(numberOfRetries) {
            try {
                return block()

            } catch (e: Exception) {
                Timber.e(e)
            }
            delay(delayBetweenRetries)
        }

        return block() // last attempt
    }

}