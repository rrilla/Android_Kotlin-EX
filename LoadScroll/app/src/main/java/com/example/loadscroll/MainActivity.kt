package com.example.loadscroll

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.loadscroll.databinding.ActivityMainBinding
import com.example.loadscroll.home.trending.TrendingFragment
import com.example.loadscroll.mypage.favorites.FavoritesFragment

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        handleBottomNavigation()
        setTransaction()
    }

    private fun handleBottomNavigation() = with(binding) {
        bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.menu_home -> {
                    if(bottomNavigation.selectedItemId == R.id.menu_home){
                        false
                    }else{
                        supportFragmentManager.commit {
                            val fragment1: Fragment? = supportFragmentManager.findFragmentByTag("home")
                            if(fragment1 != null) {
                                replace(binding.bodyContainerView.id, fragment1)
                            }
                        }
                        true
                    }
                }
                R.id.menu_myPage -> {
                    if(bottomNavigation.selectedItemId == R.id.menu_myPage){
                        false
                    }else{
                        supportFragmentManager.commit {
                            var fragment2: Fragment? = supportFragmentManager.findFragmentByTag("myPage")
                            if(fragment2 == null) {
                                fragment2 = FavoritesFragment()
                                // BackStack에 이전Fragment 저장
                                addToBackStack(null)
                                replace(binding.bodyContainerView.id, fragment2, "myPage")
                            }else{
                                replace(binding.bodyContainerView.id, fragment2)
                            }
                        }
                        true
                    }
                }
                else -> false
            }
        }
    }

    private fun setTransaction() {
        supportFragmentManager.commit {
            add(binding.bodyContainerView.id, TrendingFragment(), "home")
//            replace(binding.fragmentContainerView.id, firstFragment)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val tag1: Fragment? = supportFragmentManager.findFragmentByTag("home")
        if(tag1 != null && tag1.isVisible)
            binding.bottomNavigation.menu.findItem(R.id.menu_home).isChecked = true
    }
}