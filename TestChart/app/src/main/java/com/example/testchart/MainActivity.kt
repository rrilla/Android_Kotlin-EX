package com.example.testchart

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.testchart.chart.ChartViewModel
import com.example.testchart.data.StatsTargetAll
import com.example.testchart.databinding.ActivityMainBinding
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: ChartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[ChartViewModel::class.java]

        binding.recyclerView.adapter = viewModel.getAdapter()
        val serverRes = "{\n" +
                "    \"data\": [\n" +
                "        {\n" +
                "            \"averageScore\": 4,\n" +
                "            \"stats\": [\n" +
                "                {\n" +
                "                    \"averageApex\": 15,\n" +
                "                    \"averageBallSpeed\": 49.18,\n" +
                "                    \"averageCarry\": 164.018,\n" +
                "                    \"averageDrivingDistance\": 174.12,\n" +
                "                    \"averageLaunchAngle\": 19.68,\n" +
                "                    \"averageScore\": 4,\n" +
                "                    \"averageSwingSpeed\": 36.45,\n" +
                "                    \"clubId\": 1656655681982,\n" +
                "                    \"clubCode\": 11,\n" +
                "                    \"clubType\": \"I3\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"targetDistance\": 45,\n" +
                "            \"targetUnit\": \"yard\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"averageScore\": 4,\n" +
                "            \"stats\": [\n" +
                "                {\n" +
                "                    \"averageApex\": 15,\n" +
                "                    \"averageBallSpeed\": 31.88,\n" +
                "                    \"averageCarry\": 114.3,\n" +
                "                    \"averageDrivingDistance\": 126.5,\n" +
                "                    \"averageLaunchAngle\": 13.72,\n" +
                "                    \"averageScore\": 4,\n" +
                "                    \"averageSwingSpeed\": 25.4,\n" +
                "                    \"clubId\": 1657068982933,\n" +
                "                    \"clubCode\": 13,\n" +
                "                    \"clubType\": \"I5\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"averageApex\": 15,\n" +
                "                    \"averageBallSpeed\": 50.79,\n" +
                "                    \"averageCarry\": 171.312,\n" +
                "                    \"averageDrivingDistance\": 180.98,\n" +
                "                    \"averageLaunchAngle\": 20.56,\n" +
                "                    \"averageScore\": 4,\n" +
                "                    \"averageSwingSpeed\": 38.07,\n" +
                "                    \"clubId\": 1655425882067,\n" +
                "                    \"clubCode\": 11,\n" +
                "                    \"clubType\": \"I3\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"targetDistance\": 75,\n" +
                "            \"targetUnit\": \"yard\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"averageScore\": 6.7,\n" +
                "            \"stats\": [\n" +
                "                {\n" +
                "                    \"averageApex\": 15,\n" +
                "                    \"averageBallSpeed\": 41.33,\n" +
                "                    \"averageCarry\": 142.646,\n" +
                "                    \"averageDrivingDistance\": 156.56,\n" +
                "                    \"averageLaunchAngle\": 17.12,\n" +
                "                    \"averageScore\": 7.6,\n" +
                "                    \"averageSwingSpeed\": 31.7,\n" +
                "                    \"clubId\": 1655425882077,\n" +
                "                    \"clubCode\": 21,\n" +
                "                    \"clubType\": \"LW\"\n" +
                "                },\n" +
                "                {\n" +
                "                    \"averageApex\": 36.25,\n" +
                "                    \"averageBallSpeed\": 47.29,\n" +
                "                    \"averageCarry\": 172.63,\n" +
                "                    \"averageDrivingDistance\": 181.7,\n" +
                "                    \"averageLaunchAngle\": 40.54,\n" +
                "                    \"averageScore\": 5.5,\n" +
                "                    \"averageSwingSpeed\": 38.36,\n" +
                "                    \"clubId\": 1655425882076,\n" +
                "                    \"clubCode\": 20,\n" +
                "                    \"clubType\": \"SW\"\n" +
                "                }\n" +
                "            ],\n" +
                "            \"targetDistance\": 100,\n" +
                "            \"targetUnit\": \"yard\"\n" +
                "        }\n" +
                "    ],\n" +
                "    \"totalTargetedDistance\": 3\n" +
                "}"
        val statsTargetAll = Gson().fromJson(serverRes, StatsTargetAll::class.java)
        viewModel.setData(statsTargetAll)

    }
}