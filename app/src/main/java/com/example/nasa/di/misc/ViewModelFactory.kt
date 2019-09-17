package com.example.nasa.di.misc

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

// We will be using Saved state ViewModel factories by default for this app

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> FragmentActivity.savedStateViewModels(
    crossinline provider: (SavedStateHandle) -> T
) = viewModels<T> {
    object : AbstractSavedStateViewModelFactory(this, Bundle.EMPTY) {
        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T = provider(handle) as T
    }
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : ViewModel> Fragment.savedStateActivityViewModels(
    crossinline provider: (SavedStateHandle) -> T
) = activityViewModels<T> {
    object : AbstractSavedStateViewModelFactory(requireActivity(), Bundle.EMPTY) {
        override fun <T : ViewModel?> create(
            key: String,
            modelClass: Class<T>,
            handle: SavedStateHandle
        ): T = provider(handle) as T
    }
}
