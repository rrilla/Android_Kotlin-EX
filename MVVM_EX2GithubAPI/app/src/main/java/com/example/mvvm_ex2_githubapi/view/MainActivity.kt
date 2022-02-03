package com.example.mvvm_ex2_githubapi.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm_ex2_githubapi.GithubRepository
import com.example.mvvm_ex2_githubapi.GithubRepositoryAdapter
import com.example.mvvm_ex2_githubapi.GithubRepositoryItemDecoration
import com.example.mvvm_ex2_githubapi.MainViewModelFactory
import com.example.mvvm_ex2_githubapi.databinding.ActivityMainBinding
import com.example.mvvm_ex2_githubapi.data.remote.model.GithubRepositoryModel
import com.example.mvvm_ex2_githubapi.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var mGithubRepositoryAdapter: GithubRepositoryAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initButton()
        initViewModel()
    }

    private fun initButton() {
        binding.searchButton.setOnClickListener {
            onSearchClick()
        }
    }

    private fun initViewModel() {
//        viewModelFactory = MainViewModelFactory(GithubRepository())
//        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        //  가져올 ViewModel의 class 이름으로 가져옴
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.githubRepositories.observe(this) {
            updateRepositories(it)
        }
    }

    private fun updateRepositories(repos: List<GithubRepositoryModel>) {
        if(::mGithubRepositoryAdapter.isInitialized) {
            mGithubRepositoryAdapter.update(repos)
        } else {
            mGithubRepositoryAdapter = GithubRepositoryAdapter(repos).apply {
                listener = object : GithubRepositoryAdapter.OnGithubRepositoryClickListener {
                    override fun onItemClick(position: Int) {
                        mGithubRepositoryAdapter.getItem(position).run {
                            openGithub(htmlUrl)
                        }
                    }
                }
            }
            binding.githubReposView.run {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = mGithubRepositoryAdapter
                addItemDecoration(GithubRepositoryItemDecoration(6, 6))
            }
        }
    }

    private fun openGithub(url: String) {
        try {
            val uri = Uri.parse(url)
            Intent(Intent.ACTION_VIEW, uri).run {
                startActivity(this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onSearchClick() {
        binding.inputView.run {
            viewModel.requestGithubRepositories(binding.inputView.text.toString())
            text.clear()
            hideKeyboard()
        }
    }

    private fun hideKeyboard() {
        currentFocus?.run {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(windowToken, 0)
        }
    }
}