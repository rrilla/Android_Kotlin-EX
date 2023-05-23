package com.example.samplecleanarchitecture.view.main.permission

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
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
import com.example.samplecleanarchitecture.databinding.FragmentPermissionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PermissionFragment : BaseFragment() {

    private var _binding: FragmentPermissionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PermissionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPermissionBinding.inflate(inflater, container, false)
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


        getLauncher({
            Log.e("HJH", "수락")
        },{
            Log.e("HJH", "거절")
        }).launch(Manifest.permission.BLUETOOTH_CONNECT)

    }

    fun getLauncher(action1: () -> Unit, action2: () -> Unit): ActivityResultLauncher<String> {

    }

    fun dd(permission: String, action1: () -> Unit, action2: () -> Unit) {
        val launcher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                action1.invoke()
            } else {
                action2.invoke()
                Log.e("HJH", "거절했음.")
            }
        }
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED -> {
                action1.invoke()
            }
            shouldShowRequestPermissionRationale(permission) -> {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI, include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.

//            showInContextUI(...)
        }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                launcher.launch(
                    permission)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initializeView() = with (binding) {

    }

    private fun renderUiState(uiState: PermissionUiState?) {
        Log.e("HJH", "uiState - ${uiState?.movieDetail}")

    }

    private fun handleFailure(failure: Failure?) {
        when (failure ?: return) {
            is Failure.NetworkConnection -> renderFailure(R.string.failure_network_connection)
            is Failure.ServerError -> renderFailure(R.string.failure_server_error)
            else -> renderFailure(R.string.failure_server_error)
        }
    }

    private fun renderFailure(@StringRes message: Int) {
        notifyWithAction(message, R.string.action_refresh) {
            viewModel.loadMovies()
        }
    }
}