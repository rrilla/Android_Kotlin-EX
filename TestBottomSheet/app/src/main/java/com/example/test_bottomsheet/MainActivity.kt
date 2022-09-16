package com.example.test_bottomsheet

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.test_bottomsheet.databinding.ActivityMain2Binding
import com.example.test_bottomsheet.databinding.ActivityMainBinding
import com.example.test_bottomsheet.databinding.ItemRecyclerviewBinding
import com.google.android.material.animation.ArgbEvaluatorCompat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var standardBottomSheetBehavior: BottomSheetBehavior<View>

    private val startColor = Color.parseColor("#00FFFFFF")
    private val endColor = Color.parseColor("#FFFFFFFF")
    private val textColor = Color.parseColor("#FF000000")

    private var modalDismissWithAnimation = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupButtons()
//        setupStandardBottomSheet()
//        animateStandardBottomSheetStates()
    }

    private fun setupButtons() {
//        binding.standardBottomSheetButton.setOnClickListener {
//            standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//        }
        binding.modalBottomSheetButton.setOnClickListener {
            showModalBottomSheet()
        }
//        binding.toggleModalDismissAnimationButton.setOnClickListener {
//            modalDismissWithAnimation = !modalDismissWithAnimation
//        }
    }

//    private fun setupStandardBottomSheet() {
//        standardBottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet)
//        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
//
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                binding.textView.text = when (newState) {
//                    BottomSheetBehavior.STATE_EXPANDED -> "STATE_EXPANDED"
//                    BottomSheetBehavior.STATE_COLLAPSED -> "STATE_COLLAPSED"
//                    BottomSheetBehavior.STATE_DRAGGING -> "STATE_DRAGGING"
//                    BottomSheetBehavior.STATE_HALF_EXPANDED -> "STATE_HALF_EXPANDED"
//                    BottomSheetBehavior.STATE_HIDDEN -> "STATE_HIDDEN"
//                    BottomSheetBehavior.STATE_SETTLING -> "STATE_SETTLING"
//                    else -> null
//                }
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                val fraction = (slideOffset + 1f) / 2f
//                val color = ArgbEvaluatorCompat.getInstance().evaluate(fraction, startColor, endColor)
//                binding.slideView.setBackgroundColor(color)
//            }
//        }
//        standardBottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)
//        standardBottomSheetBehavior.saveFlags = BottomSheetBehavior.SAVE_ALL
//        binding.textView.setTextColor(textColor)
//    }

    private fun showModalBottomSheet() {
        val modalBottomSheet = ModalBottomSheet()
        modalBottomSheet.show(supportFragmentManager, ModalBottomSheet.TAG)
    }

//    private fun animateStandardBottomSheetStates() {
//        binding.standardBottomSheet.postDelayed({
//            standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//        }, 1000L)
//        binding.standardBottomSheet.postDelayed({
//            standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
//        }, 2000L)
//        binding.standardBottomSheet.postDelayed({
//            standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
//        }, 3000L)
//        binding.standardBottomSheet.postDelayed({
//            standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//        }, 4000L)
//        binding.standardBottomSheet.postDelayed({
//            standardBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//        }, 5000L)
//    }
}


