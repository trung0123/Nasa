package com.example.nasa.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.example.nasa.data.APod
import com.example.nasa.data.source.APodRepository
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.launch

class MainViewModel @AssistedInject constructor(
    private val aPodRepository: APodRepository,
    @Assisted private val handle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_CURRENT_PICTURE_POSITION = "dev.sasikanth.nasa.apod.current_position"
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(handle: SavedStateHandle): MainViewModel
    }

    val networkState = aPodRepository.networkState
    val aPods: LiveData<PagedList<APod>> = aPodRepository.getAPods()

    var currentPicturePosition: Int = 0
        get() = handle.get(KEY_CURRENT_PICTURE_POSITION) ?: 0
        set(value) {
            if (value >= 0 && value != field) {
                field = value
                handle.set(KEY_CURRENT_PICTURE_POSITION, value)
            }
        }

    init {
        viewModelScope.launch {
            aPodRepository.getLatestAPod()
        }
    }
}
