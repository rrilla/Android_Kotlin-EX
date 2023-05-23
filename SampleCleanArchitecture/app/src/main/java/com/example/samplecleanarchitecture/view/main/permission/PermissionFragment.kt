package com.example.samplecleanarchitecture.view.main.permission

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.samplecleanarchitecture.R
import com.example.samplecleanarchitecture.core.exception.Failure
import com.example.samplecleanarchitecture.core.extension.*
import com.example.samplecleanarchitecture.core.platform.BaseActivity
import com.example.samplecleanarchitecture.core.platform.BaseFragment
import com.example.samplecleanarchitecture.databinding.FragmentPermissionBinding
import dagger.hilt.android.AndroidEntryPoint

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


        binding.button.setOnClickListener {
            this.requireActivity().let {
                if (it is BaseActivity<*>) {
                    it.checkPermission(
                        this.requireContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        {
                            Log.e("HJH", "수락")
                        },{
                            Log.e("HJH", "거절")
                        }
                    )
                }
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