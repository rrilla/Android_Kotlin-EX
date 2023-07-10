package com.example.theme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.theme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val myAdapter = MyAdapter()
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = myAdapter
        }

        myAdapter.setData(
            listOf("zzz","dfasdf","zzz","dfasdf","zzz","dfasdf","zzz","dfasdf","zzz","dfasdf")
        )

    }
}