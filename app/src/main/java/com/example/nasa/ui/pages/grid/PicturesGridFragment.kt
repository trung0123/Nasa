package com.example.nasa.ui.pages.grid


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.nasa.R
import com.example.nasa.databinding.FragmentPicturesGridBinding
import com.example.nasa.di.misc.injector
import com.example.nasa.di.misc.savedStateActivityViewModels
import com.example.nasa.ui.MainViewModel
import com.example.nasa.ui.adapters.APodItemListener
import com.example.nasa.ui.adapters.APodsGridAdapter

/**
 * A simple [Fragment] subclass.
 */
class PicturesGridFragment : Fragment() {

    val viewModel: MainViewModel by savedStateActivityViewModels { savedStateHandle ->
        injector.mainViewModel.create(savedStateHandle)
    }

    private val gridScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val firstVisiblePosition = gridLayoutManager.findFirstVisibleItemPosition()
            viewModel.currentPicturePosition = firstVisiblePosition
        }
    }

    private lateinit var binding: FragmentPicturesGridBinding
    private lateinit var gridLayoutManager: GridLayoutManager

    override fun onAttach(context: Context) {
        requireActivity().injector.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPicturesGridBinding.inflate(inflater)

        val adapter = APodsGridAdapter(APodItemListener { position ->

        })

        binding.apodsGrid.apply {
            setHasFixedSize(true)
            gridLayoutManager = layoutManager as GridLayoutManager
            this.adapter = adapter

            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (adapter.getItemViewType(position) == APodsGridAdapter.LOADING_ITEM) {
                        2
                    } else {
                        1
                    }
                }
            }
        }

        return binding.root
    }
}
