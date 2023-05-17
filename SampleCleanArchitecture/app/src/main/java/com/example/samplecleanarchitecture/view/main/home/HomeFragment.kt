package com.example.samplecleanarchitecture.view.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.samplecleanarchitecture.R
import com.example.samplecleanarchitecture.core.exception.Failure
import com.example.samplecleanarchitecture.core.extension.*
import com.example.samplecleanarchitecture.core.platform.BaseActivity
import com.example.samplecleanarchitecture.core.platform.BaseFragment
import com.example.samplecleanarchitecture.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with (viewModel) {
            loading(loading, requireActivity())
            failure(failure, ::handleFailure)
            observe(uiState, ::renderUiState)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeView()
        viewModel.loadMovies()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeView() = with (binding) {
        movieList.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        movieList.adapter = moviesAdapter
        moviesAdapter.clickListener = { uiMovie ->
            Log.e("HJH", "click - $uiMovie")
            viewModel.loadMovieDetails(uiMovie.id)
        }
    }

    private fun renderUiState(uiState: HomeUiState?) {
        Log.e("HJH", "uiState - ${uiState?.movieDetail}")

        moviesAdapter.collection = uiState?.movies.orEmpty()
    }

    private fun handleFailure(failure: Failure?) {
        when (failure ?: return) {
            is Failure.NetworkConnection -> renderFailure(R.string.failure_network_connection)
            is Failure.ServerError -> renderFailure(R.string.failure_server_error)
            is HomeFailure.ListNotAvailable -> renderFailure(R.string.failure_movies_list_unavailable)
            else -> renderFailure(R.string.failure_server_error)
        }
    }

    private fun renderFailure(@StringRes message: Int) {
        notifyWithAction(message, R.string.action_refresh) {
            viewModel.loadMovies()
        }
    }
}