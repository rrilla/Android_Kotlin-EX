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
        //  ㅄ
//        val serverRes = "{\"data\":[{\"averageScore\":6,\"stats\":[{\"averageApex\":3.65,\"averageBallSpeed\":31.29,\"averageCarry\":59.379,\"averageDrivingDistance\":83.63,\"averageLaunchAngle\":12.47,\"averageScore\":7,\"averageSwingSpeed\":24.47,\"clubId\":1635301211096,\"clubCode\":13,\"clubType\":\"I5\"},{\"averageApex\":6.49,\"averageBallSpeed\":30.7,\"averageCarry\":65.839,\"averageDrivingDistance\":83.84,\"averageLaunchAngle\":17.22,\"averageScore\":5.71,\"averageSwingSpeed\":24.66,\"clubId\":1635301211098,\"clubCode\":15,\"clubType\":\"I7\"},{\"averageApex\":4.3,\"averageBallSpeed\":17.77,\"averageCarry\":25.43,\"averageDrivingDistance\":30.81,\"averageLaunchAngle\":26.71,\"averageScore\":4,\"averageSwingSpeed\":14.67,\"clubId\":1635817381538,\"clubCode\":20,\"clubType\":\"SW\"},{\"averageApex\":57.84,\"averageBallSpeed\":56.4,\"averageCarry\":141.162,\"averageDrivingDistance\":153.36,\"averageLaunchAngle\":31.66,\"averageScore\":4,\"averageSwingSpeed\":38.11,\"clubId\":1635301211094,\"clubCode\":11,\"clubType\":\"I3\"}],\"targetDistance\":50,\"targetUnit\":\"meter\"},{\"averageScore\":4,\"stats\":[{\"averageApex\":36.55,\"averageBallSpeed\":55.28,\"averageCarry\":157.846,\"averageDrivingDistance\":160.46,\"averageLaunchAngle\":17.98,\"averageScore\":4,\"averageSwingSpeed\":55.23,\"clubId\":1635817381538,\"clubCode\":20,\"clubType\":\"SW\"},{\"averageApex\":48.87,\"averageBallSpeed\":61.12,\"averageCarry\":172.031,\"averageDrivingDistance\":178.96,\"averageLaunchAngle\":21.16,\"averageScore\":4,\"averageSwingSpeed\":46.71,\"clubId\":1635301211098,\"clubCode\":15,\"clubType\":\"I7\"},{\"averageApex\":23.57,\"averageBallSpeed\":66.41,\"averageCarry\":219.049,\"averageDrivingDistance\":231.19,\"averageLaunchAngle\":9.1,\"averageScore\":4,\"averageSwingSpeed\":55.66,\"clubId\":1635301211094,\"clubCode\":11,\"clubType\":\"I3\"}],\"targetDistance\":65,\"targetUnit\":\"meter\"},{\"averageScore\":4,\"stats\":[{\"averageApex\":6.24,\"averageBallSpeed\":22.77,\"averageCarry\":43.022,\"averageDrivingDistance\":55.72,\"averageLaunchAngle\":25.56,\"averageScore\":4,\"averageSwingSpeed\":19.53,\"clubId\":1635301211101,\"clubCode\":18,\"clubType\":\"PW\"},{\"averageApex\":13.13,\"averageBallSpeed\":38.82,\"averageCarry\":100.528,\"averageDrivingDistance\":131.47,\"averageLaunchAngle\":18.41,\"averageScore\":4,\"averageSwingSpeed\":26.92,\"clubId\":1635301211094,\"clubCode\":11,\"clubType\":\"I3\"}],\"targetDistance\":1,\"targetUnit\":\"yard\"},{\"averageScore\":4,\"stats\":[{\"averageApex\":5.48,\"averageBallSpeed\":18.32,\"averageCarry\":30.672,\"averageDrivingDistance\":41.28,\"averageLaunchAngle\":31.06,\"averageScore\":4,\"averageSwingSpeed\":15.41,\"clubId\":1635301211101,\"clubCode\":18,\"clubType\":\"PW\"},{\"averageApex\":14.89,\"averageBallSpeed\":25.66,\"averageCarry\":53.699,\"averageDrivingDistance\":61.28,\"averageLaunchAngle\":39.16,\"averageScore\":4,\"averageSwingSpeed\":20.89,\"clubId\":1635817381538,\"clubCode\":20,\"clubType\":\"SW\"},{\"averageApex\":15,\"averageBallSpeed\":36.09,\"averageCarry\":120.701,\"averageDrivingDistance\":124.23,\"averageLaunchAngle\":14.48,\"averageScore\":4,\"averageSwingSpeed\":26.82,\"clubId\":1635301211094,\"clubCode\":11,\"clubType\":\"I3\"},{\"averageApex\":15,\"averageBallSpeed\":38.94,\"averageCarry\":146.304,\"averageDrivingDistance\":152.04,\"averageLaunchAngle\":17.56,\"averageScore\":4,\"averageSwingSpeed\":32.51,\"clubId\":1635757240298,\"clubCode\":14,\"clubType\":\"I6\"}],\"targetDistance\":15,\"targetUnit\":\"yard\"},{\"averageScore\":4,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":52.26,\"averageCarry\":181.051,\"averageDrivingDistance\":194.33,\"averageLaunchAngle\":21.73,\"averageScore\":4,\"averageSwingSpeed\":40.23,\"clubId\":1635757240298,\"clubCode\":14,\"clubType\":\"I6\"},{\"averageApex\":15,\"averageBallSpeed\":56.41,\"averageCarry\":201.168,\"averageDrivingDistance\":209.55,\"averageLaunchAngle\":24.14,\"averageScore\":4,\"averageSwingSpeed\":44.7,\"clubId\":1635301211094,\"clubCode\":11,\"clubType\":\"I3\"}],\"targetDistance\":25,\"targetUnit\":\"yard\"},{\"averageScore\":4,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":31.74,\"averageCarry\":118.872,\"averageDrivingDistance\":134.96,\"averageLaunchAngle\":14.26,\"averageScore\":4,\"averageSwingSpeed\":26.42,\"clubId\":1635301211094,\"clubCode\":11,\"clubType\":\"I3\"}],\"targetDistance\":40,\"targetUnit\":\"yard\"},{\"averageScore\":4,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":38.85,\"averageCarry\":134.874,\"averageDrivingDistance\":141.81,\"averageLaunchAngle\":16.18,\"averageScore\":4,\"averageSwingSpeed\":29.97,\"clubId\":1635301211094,\"clubCode\":11,\"clubType\":\"I3\"},{\"averageApex\":15,\"averageBallSpeed\":50.37,\"averageCarry\":150.876,\"averageDrivingDistance\":160.18,\"averageLaunchAngle\":18.11,\"averageScore\":4,\"averageSwingSpeed\":33.53,\"clubId\":1635301211103,\"clubCode\":20,\"clubType\":\"SW\"}],\"targetDistance\":75,\"targetUnit\":\"yard\"},{\"averageScore\":5.5,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":32.57,\"averageCarry\":100.584,\"averageDrivingDistance\":114.39,\"averageLaunchAngle\":12.07,\"averageScore\":7,\"averageSwingSpeed\":22.35,\"clubId\":1635301211094,\"clubCode\":11,\"clubType\":\"I3\"},{\"averageApex\":15,\"averageBallSpeed\":29.88,\"averageCarry\":109.728,\"averageDrivingDistance\":118.94,\"averageLaunchAngle\":13.17,\"averageScore\":4,\"averageSwingSpeed\":24.38,\"clubId\":1635301211104,\"clubCode\":21,\"clubType\":\"LW\"}],\"targetDistance\":85,\"targetUnit\":\"yard\"},{\"averageScore\":4.1,\"stats\":[{\"averageApex\":8.43,\"averageBallSpeed\":32.65,\"averageCarry\":75.596,\"averageDrivingDistance\":92.77,\"averageLaunchAngle\":17.32,\"averageScore\":4.33,\"averageSwingSpeed\":25.22,\"clubId\":1635301211098,\"clubCode\":15,\"clubType\":\"I7\"},{\"averageApex\":15,\"averageBallSpeed\":41.68,\"averageCarry\":140,\"averageDrivingDistance\":156.88,\"averageLaunchAngle\":16.8,\"averageScore\":4,\"averageSwingSpeed\":31.11,\"clubId\":1635301211100,\"clubCode\":17,\"clubType\":\"I9\"},{\"averageApex\":15,\"averageBallSpeed\":53.52,\"averageCarry\":172.5,\"averageDrivingDistance\":184.46,\"averageLaunchAngle\":20.7,\"averageScore\":4,\"averageSwingSpeed\":38.33,\"clubId\":1635301211099,\"clubCode\":16,\"clubType\":\"I8\"},{\"averageApex\":15,\"averageBallSpeed\":48.27,\"averageCarry\":173.333,\"averageDrivingDistance\":184.47,\"averageLaunchAngle\":20.8,\"averageScore\":4,\"averageSwingSpeed\":38.52,\"clubId\":1635301211101,\"clubCode\":18,\"clubType\":\"PW\"},{\"averageApex\":15,\"averageBallSpeed\":60.29,\"averageCarry\":200,\"averageDrivingDistance\":212,\"averageLaunchAngle\":24,\"averageScore\":4,\"averageSwingSpeed\":44.44,\"clubId\":1635301211103,\"clubCode\":20,\"clubType\":\"SW\"},{\"averageApex\":49.95,\"averageBallSpeed\":79.37,\"averageCarry\":256.364,\"averageDrivingDistance\":292.33,\"averageLaunchAngle\":12.35,\"averageScore\":4,\"averageSwingSpeed\":64.07,\"clubId\":1660187824732,\"clubCode\":13,\"clubType\":\"I5\"}],\"targetDistance\":60,\"targetUnit\":\"meter\"},{\"averageScore\":5,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":37.24,\"averageCarry\":118.872,\"averageDrivingDistance\":123.35,\"averageLaunchAngle\":14.26,\"averageScore\":4,\"averageSwingSpeed\":26.42,\"clubId\":1635301211096,\"clubCode\":13,\"clubType\":\"I5\"},{\"averageApex\":15,\"averageBallSpeed\":34.99,\"averageCarry\":118.872,\"averageDrivingDistance\":119.9,\"averageLaunchAngle\":14.26,\"averageScore\":4,\"averageSwingSpeed\":26.42,\"clubId\":1635757240298,\"clubCode\":14,\"clubType\":\"I6\"},{\"averageApex\":15,\"averageBallSpeed\":34.21,\"averageCarry\":118.872,\"averageDrivingDistance\":132.92,\"averageLaunchAngle\":14.26,\"averageScore\":4,\"averageSwingSpeed\":26.42,\"clubId\":1635301211098,\"clubCode\":15,\"clubType\":\"I7\"},{\"averageApex\":15,\"averageBallSpeed\":41.72,\"averageCarry\":142.875,\"averageDrivingDistance\":152.84,\"averageLaunchAngle\":17.14,\"averageScore\":5.31,\"averageSwingSpeed\":31.75,\"clubId\":1635301211103,\"clubCode\":20,\"clubType\":\"SW\"},{\"averageApex\":15,\"averageBallSpeed\":51.13,\"averageCarry\":182.88,\"averageDrivingDistance\":198.77,\"averageLaunchAngle\":21.95,\"averageScore\":4,\"averageSwingSpeed\":40.64,\"clubId\":1635301211102,\"clubCode\":19,\"clubType\":\"GW\"}],\"targetDistance\":60,\"targetUnit\":\"yard\"},{\"averageScore\":4,\"stats\":[{\"averageApex\":37.45,\"averageBallSpeed\":56.28,\"averageCarry\":161.058,\"averageDrivingDistance\":163.71,\"averageLaunchAngle\":17.64,\"averageScore\":4,\"averageSwingSpeed\":55.3,\"clubId\":1635817381538,\"clubCode\":20,\"clubType\":\"SW\"},{\"averageApex\":40.66,\"averageBallSpeed\":65.92,\"averageCarry\":208.975,\"averageDrivingDistance\":217.12,\"averageLaunchAngle\":15.2,\"averageScore\":4,\"averageSwingSpeed\":56.22,\"clubId\":1635757240298,\"clubCode\":14,\"clubType\":\"I6\"}],\"targetDistance\":95,\"targetUnit\":\"yard\"},{\"averageScore\":4,\"stats\":[{\"averageApex\":13.26,\"averageBallSpeed\":30.91,\"averageCarry\":72.307,\"averageDrivingDistance\":85.73,\"averageLaunchAngle\":25.93,\"averageScore\":4,\"averageSwingSpeed\":33.44,\"clubId\":1635301211100,\"clubCode\":17,\"clubType\":\"I9\"},{\"averageApex\":13.41,\"averageBallSpeed\":34.03,\"averageCarry\":85.09,\"averageDrivingDistance\":102.7,\"averageLaunchAngle\":23.12,\"averageScore\":4,\"averageSwingSpeed\":24.12,\"clubId\":1635301211098,\"clubCode\":15,\"clubType\":\"I7\"},{\"averageApex\":30.05,\"averageBallSpeed\":39.72,\"averageCarry\":101.884,\"averageDrivingDistance\":111.68,\"averageLaunchAngle\":32.39,\"averageScore\":4,\"averageSwingSpeed\":34.07,\"clubId\":1635301211101,\"clubCode\":18,\"clubType\":\"PW\"}],\"targetDistance\":1,\"targetUnit\":\"meter\"},{\"averageScore\":8.2,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":53.78,\"averageCarry\":190,\"averageDrivingDistance\":197.18,\"averageLaunchAngle\":22.8,\"averageScore\":10,\"averageSwingSpeed\":42.22,\"clubId\":1635301211100,\"clubCode\":17,\"clubType\":\"I9\"},{\"averageApex\":15,\"averageBallSpeed\":44.37,\"averageCarry\":150,\"averageDrivingDistance\":162.43,\"averageLaunchAngle\":18,\"averageScore\":6.33,\"averageSwingSpeed\":33.33,\"clubId\":1635301211102,\"clubCode\":19,\"clubType\":\"GW\"}],\"targetDistance\":190,\"targetUnit\":\"meter\"},{\"averageScore\":4,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":48.18,\"averageCarry\":180,\"averageDrivingDistance\":188.54,\"averageLaunchAngle\":21.6,\"averageScore\":4,\"averageSwingSpeed\":40,\"clubId\":1635301211102,\"clubCode\":19,\"clubType\":\"GW\"}],\"targetDistance\":80,\"targetUnit\":\"meter\"},{\"averageScore\":4,\"stats\":[{\"averageApex\":11.3,\"averageBallSpeed\":33.45,\"averageCarry\":86.508,\"averageDrivingDistance\":95.38,\"averageLaunchAngle\":29.7,\"averageScore\":4,\"averageSwingSpeed\":26.26,\"clubId\":1635301211103,\"clubCode\":20,\"clubType\":\"SW\"}],\"targetDistance\":15,\"targetUnit\":\"meter\"},{\"averageScore\":4,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":40.15,\"averageCarry\":150,\"averageDrivingDistance\":166.06,\"averageLaunchAngle\":18,\"averageScore\":4,\"averageSwingSpeed\":33.33,\"clubId\":1635301211103,\"clubCode\":20,\"clubType\":\"SW\"}],\"targetDistance\":85,\"targetUnit\":\"meter\"},{\"averageScore\":5.3,\"stats\":[{\"averageApex\":52.59,\"averageBallSpeed\":59.07,\"averageCarry\":151.367,\"averageDrivingDistance\":154.65,\"averageLaunchAngle\":26.59,\"averageScore\":5.33,\"averageSwingSpeed\":50.29,\"clubId\":1635817381538,\"clubCode\":20,\"clubType\":\"SW\"}],\"targetDistance\":95,\"targetUnit\":\"meter\"},{\"averageScore\":8,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":62.77,\"averageCarry\":220,\"averageDrivingDistance\":232.24,\"averageLaunchAngle\":26.4,\"averageScore\":8,\"averageSwingSpeed\":48.89,\"clubId\":1635301211103,\"clubCode\":20,\"clubType\":\"SW\"}],\"targetDistance\":195,\"targetUnit\":\"meter\"},{\"averageScore\":4,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":57.37,\"averageCarry\":192.024,\"averageDrivingDistance\":211.76,\"averageLaunchAngle\":23.04,\"averageScore\":4,\"averageSwingSpeed\":42.67,\"clubId\":1635301211103,\"clubCode\":20,\"clubType\":\"SW\"}],\"targetDistance\":65,\"targetUnit\":\"yard\"},{\"averageScore\":5,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":31.2,\"averageCarry\":109.728,\"averageDrivingDistance\":118.14,\"averageLaunchAngle\":13.17,\"averageScore\":5,\"averageSwingSpeed\":24.38,\"clubId\":1635301211103,\"clubCode\":20,\"clubType\":\"SW\"}],\"targetDistance\":150,\"targetUnit\":\"yard\"},{\"averageScore\":10,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":43.83,\"averageCarry\":164.592,\"averageDrivingDistance\":164.98,\"averageLaunchAngle\":19.75,\"averageScore\":10,\"averageSwingSpeed\":36.58,\"clubId\":1635301211103,\"clubCode\":20,\"clubType\":\"SW\"}],\"targetDistance\":170,\"targetUnit\":\"yard\"}],\"totalTargetedDistance\":21}"
        val serverRes = "{\"data\":[{\"averageScore\":4,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":39.5,\"averageCarry\":140.208,\"averageDrivingDistance\":149.93,\"averageLaunchAngle\":16.82,\"averageScore\":4,\"averageSwingSpeed\":31.16,\"clubId\":1664343747102,\"clubCode\":16,\"clubType\":\"I8\"},{\"averageApex\":15,\"averageBallSpeed\":41.05,\"averageCarry\":143.256,\"averageDrivingDistance\":156.29,\"averageLaunchAngle\":17.19,\"averageScore\":4,\"averageSwingSpeed\":31.83,\"clubId\":1664343747100,\"clubCode\":14,\"clubType\":\"I6\"},{\"averageApex\":15,\"averageBallSpeed\":42.09,\"averageCarry\":143.256,\"averageDrivingDistance\":159.67,\"averageLaunchAngle\":17.19,\"averageScore\":4,\"averageSwingSpeed\":31.83,\"clubId\":1664343747101,\"clubCode\":15,\"clubType\":\"I7\"},{\"averageApex\":15,\"averageBallSpeed\":44.42,\"averageCarry\":146.304,\"averageDrivingDistance\":152.78,\"averageLaunchAngle\":17.56,\"averageScore\":4,\"averageSwingSpeed\":32.51,\"clubId\":1664343747106,\"clubCode\":20,\"clubType\":\"SW\"},{\"averageApex\":15,\"averageBallSpeed\":40.31,\"averageCarry\":149.352,\"averageDrivingDistance\":165.53,\"averageLaunchAngle\":17.92,\"averageScore\":4,\"averageSwingSpeed\":33.19,\"clubId\":1664343747098,\"clubCode\":12,\"clubType\":\"I4\"},{\"averageApex\":15,\"averageBallSpeed\":41.58,\"averageCarry\":149.352,\"averageDrivingDistance\":160.08,\"averageLaunchAngle\":17.92,\"averageScore\":4,\"averageSwingSpeed\":33.19,\"clubId\":1664343747107,\"clubCode\":21,\"clubType\":\"LW\"},{\"averageApex\":15,\"averageBallSpeed\":40.56,\"averageCarry\":152.4,\"averageDrivingDistance\":167.22,\"averageLaunchAngle\":18.29,\"averageScore\":4,\"averageSwingSpeed\":33.87,\"clubId\":1664343747103,\"clubCode\":17,\"clubType\":\"I9\"},{\"averageApex\":15,\"averageBallSpeed\":43.36,\"averageCarry\":155.448,\"averageDrivingDistance\":164.38,\"averageLaunchAngle\":18.65,\"averageScore\":4,\"averageSwingSpeed\":34.54,\"clubId\":1664343747099,\"clubCode\":13,\"clubType\":\"I5\"},{\"averageApex\":15,\"averageBallSpeed\":49.35,\"averageCarry\":155.448,\"averageDrivingDistance\":172.8,\"averageLaunchAngle\":18.65,\"averageScore\":4,\"averageSwingSpeed\":34.54,\"clubId\":1664343747105,\"clubCode\":19,\"clubType\":\"GW\"},{\"averageApex\":15,\"averageBallSpeed\":48.29,\"averageCarry\":164.592,\"averageDrivingDistance\":180.61,\"averageLaunchAngle\":19.75,\"averageScore\":4,\"averageSwingSpeed\":36.58,\"clubId\":1664343747104,\"clubCode\":18,\"clubType\":\"PW\"},{\"averageApex\":15,\"averageBallSpeed\":46.52,\"averageCarry\":165.354,\"averageDrivingDistance\":174.81,\"averageLaunchAngle\":19.84,\"averageScore\":4,\"averageSwingSpeed\":36.75,\"clubId\":1664343747097,\"clubCode\":11,\"clubType\":\"I3\"}],\"targetDistance\":15,\"targetUnit\":\"yard\"}],\"totalTargetedDistance\":1}"
//        val serverRes = "{\"data\":[{\"averageScore\":4,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":44.76,\"averageCarry\":157.436,\"averageDrivingDistance\":165.85,\"averageLaunchAngle\":18.89,\"averageScore\":4,\"averageSwingSpeed\":34.99,\"clubId\":1655425882067,\"clubCode\":11,\"clubType\":\"I3\"}],\"targetDistance\":15,\"targetUnit\":\"yard\"},{\"averageScore\":4,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":49.18,\"averageCarry\":164.018,\"averageDrivingDistance\":174.12,\"averageLaunchAngle\":19.68,\"averageScore\":4,\"averageSwingSpeed\":36.45,\"clubId\":1656655681982,\"clubCode\":11,\"clubType\":\"I3\"}],\"targetDistance\":45,\"targetUnit\":\"yard\"},{\"averageScore\":4,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":31.88,\"averageCarry\":114.3,\"averageDrivingDistance\":126.5,\"averageLaunchAngle\":13.72,\"averageScore\":4,\"averageSwingSpeed\":25.4,\"clubId\":1657068982933,\"clubCode\":13,\"clubType\":\"I5\"},{\"averageApex\":15,\"averageBallSpeed\":50.79,\"averageCarry\":171.312,\"averageDrivingDistance\":180.98,\"averageLaunchAngle\":20.56,\"averageScore\":4,\"averageSwingSpeed\":38.07,\"clubId\":1655425882067,\"clubCode\":11,\"clubType\":\"I3\"}],\"targetDistance\":75,\"targetUnit\":\"yard\"},{\"averageScore\":7.3,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":39.16,\"averageCarry\":138.176,\"averageDrivingDistance\":147.56,\"averageLaunchAngle\":16.58,\"averageScore\":7.56,\"averageSwingSpeed\":30.71,\"clubId\":1655425882067,\"clubCode\":11,\"clubType\":\"I3\"},{\"averageApex\":15,\"averageBallSpeed\":45.48,\"averageCarry\":153.785,\"averageDrivingDistance\":165.55,\"averageLaunchAngle\":18.45,\"averageScore\":7.18,\"averageSwingSpeed\":34.17,\"clubId\":1657068982933,\"clubCode\":13,\"clubType\":\"I5\"}],\"targetDistance\":150,\"targetUnit\":\"yard\"},{\"averageScore\":6,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":51.55,\"averageCarry\":182.88,\"averageDrivingDistance\":195.8,\"averageLaunchAngle\":21.95,\"averageScore\":6,\"averageSwingSpeed\":40.64,\"clubId\":1655425882067,\"clubCode\":11,\"clubType\":\"I3\"}],\"targetDistance\":165,\"targetUnit\":\"yard\"},{\"averageScore\":6.7,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":41.33,\"averageCarry\":142.646,\"averageDrivingDistance\":156.56,\"averageLaunchAngle\":17.12,\"averageScore\":7.6,\"averageSwingSpeed\":31.7,\"clubId\":1655425882077,\"clubCode\":21,\"clubType\":\"LW\"},{\"averageApex\":36.25,\"averageBallSpeed\":47.29,\"averageCarry\":172.63,\"averageDrivingDistance\":181.7,\"averageLaunchAngle\":40.54,\"averageScore\":5.5,\"averageSwingSpeed\":38.36,\"clubId\":1655425882076,\"clubCode\":20,\"clubType\":\"SW\"}],\"targetDistance\":100,\"targetUnit\":\"yard\"},{\"averageScore\":4,\"stats\":[{\"averageApex\":15,\"averageBallSpeed\":52.16,\"averageCarry\":196.596,\"averageDrivingDistance\":208.17,\"averageLaunchAngle\":23.59,\"averageScore\":4,\"averageSwingSpeed\":43.69,\"clubId\":1655425882076,\"clubCode\":20,\"clubType\":\"SW\"}],\"targetDistance\":135,\"targetUnit\":\"yard\"}],\"totalTargetedDistance\":7}"
        val statsTargetAll = Gson().fromJson(serverRes, StatsTargetAll::class.java)
        viewModel.setData(statsTargetAll)

    }
}