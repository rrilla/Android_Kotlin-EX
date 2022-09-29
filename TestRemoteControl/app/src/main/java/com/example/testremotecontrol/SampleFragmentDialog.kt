package com.example.testremotecontrol

import android.os.Bundle
import androidx.fragment.app.DialogFragment

class SampleFragmentDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그라 dismiss 되지 않는다.
        isCancelable = true
    }

    private lateinit var binding: DialogFragmentSampleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogFragmentSampleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val text = "Hello, Welcome to blackjin Tisotry"

        binding.tvSample.text = text

        binding.btnSample.setOnClickListener {
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
        }
    }
}