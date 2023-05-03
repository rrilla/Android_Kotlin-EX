package com.example.testremotecontrol

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.testremotecontrol.databinding.FragmentSampleDialogBinding
import kotlin.math.roundToInt

class SampleFragmentDialog : DialogFragment() {

    private val margin = 16

    private lateinit var binding: FragmentSampleDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSampleDialogBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        binding.root.measure(displayMetrics.widthPixels - convertDPtoPX(margin*2), displayMetrics.heightPixels - convertDPtoPX(margin*2))
        binding.root.layoutParams.apply {
            width = displayMetrics.widthPixels - convertDPtoPX(margin*2)
            height = displayMetrics.heightPixels - convertDPtoPX(margin*2)
        }
        Log.e("hjh", "heightPixels : ${displayMetrics.heightPixels}")

        //  화면밖 혹은 뒤로가기 버튼시 다이얼로그가 dismiss
//        isCancelable = false


//        binding.btnSample.setOnClickListener {
//            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
//        }
    }

    private fun convertDPtoPX(dp: Int): Int {
        val density: Float = requireContext().resources.displayMetrics.density
        return (dp.toFloat() * density).roundToInt()
    }

}