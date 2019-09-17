package com.example.nasa.di.app

import android.content.Context
import com.example.nasa.ui.MainActivity
import com.example.nasa.ui.MainViewModel
import com.example.nasa.ui.pages.grid.PicturesGridFragment
import com.example.nasa.ui.pages.viewer.ViewerFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class, ViewModelModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    val mainViewModel: MainViewModel.Factory

    fun inject(mainActivity: MainActivity)
    fun inject(picturesGridFragment: PicturesGridFragment)
    fun inject(viewerFragment: ViewerFragment)
}