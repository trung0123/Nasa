package com.example.nasa.data.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.example.nasa.BuildConfig
import com.example.nasa.data.APod
import com.example.nasa.data.NetworkState
import com.example.nasa.data.source.local.APodDao
import com.example.nasa.data.source.remote.APodApiService
import com.example.nasa.utils.DateUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date

class APodBoundaryCallback(
    private val localSource: APodDao,
    private val remoteSource: APodApiService
) : PagedList.BoundaryCallback<APod>() {

    val networkState = MutableLiveData<NetworkState>()

    private val uiScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private var isLoading = false

    // No items are present, load data from API
    override fun onZeroItemsLoaded() {
        super.onZeroItemsLoaded()
        getAndSaveAPodRange(null)
    }

    // Once we reach end of database, request new data from API
    override fun onItemAtEndLoaded(itemAtEnd: APod) {
        super.onItemAtEndLoaded(itemAtEnd)
        getAndSaveAPodRange(itemAtEnd.date)
    }

    private fun getAndSaveAPodRange(date: Date?) {
        if (isLoading) return
        uiScope.launch {
            isLoading = true
            // Getting calendar instance for current date
            val calendar = Calendar.getInstance()

            // Limiting data to 16 Jun 1995, since API only has data after that
            val limitCal = Calendar.getInstance().apply {
                set(Calendar.DAY_OF_MONTH, 16)
                set(Calendar.MONTH, Calendar.JUNE)
                set(Calendar.YEAR, 1995)
            }

            // If last date is passed, we are setting it as current date
            // and getting day before date.
            if (date != null) {
                calendar.time = date
                calendar.add(Calendar.DAY_OF_MONTH, -1)
            }

            // Getting 20 days worth of images at a time
            val endDate = DateUtils.formatDate(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, -20)
            if (calendar >= limitCal) {
                networkState.postValue(NetworkState.Loading)
                val startDate = DateUtils.formatDate(calendar.time)
                try {
                    // Getting images and filtering them so that only image type are saved into db
                    val picturesResponse = withContext(Dispatchers.IO) {
                        // Just to be safe making the suspended retrofit call into separate dispatcher
                        // room will automatically use io executor from android architecture components
                        // so no need for moving it manually into separate dispatcher;
                        remoteSource.getAPods(
                            BuildConfig.API_KEY,
                            startDate,
                            endDate
                        )
                    }
                    if (picturesResponse.isSuccessful) {
                        localSource.insertApod(
                            *picturesResponse.body().orEmpty().toTypedArray()
                        )
                        networkState.postValue(NetworkState.Success)
                    } else {
                        when (picturesResponse.code()) {
                            400 -> {
                                networkState.postValue(NetworkState.BadRequestError)
                            }
                            404 -> {
                                networkState.postValue(NetworkState.NotFoundError)
                            }
                            500 -> {
                                networkState.postValue(NetworkState.ServerError)
                            }
                            else -> {
                                networkState.postValue(
                                    NetworkState.UnknownError(picturesResponse.code())
                                )
                            }
                        }
                    }
                } catch (e: Exception) {
                    networkState.postValue(NetworkState.Exception(e.localizedMessage))
                }
            } else {
                networkState.postValue(NetworkState.Success)
            }
            isLoading = false
        }
    }
}
