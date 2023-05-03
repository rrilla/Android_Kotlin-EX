package com.example.test_bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.test_bottomsheet.databinding.ItemRecyclerviewBinding
import com.example.test_bottomsheet.databinding.ModalBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.math.roundToInt
import kotlin.properties.Delegates

abstract class ModalBottomSheet <VB: ViewBinding> : BottomSheetDialogFragment() {

    sealed class BottomSheetButton {
        data class OneButton(
            val title: String,
            val listener: View.OnClickListener
        ) : BottomSheetButton()
        data class TwoButton(
            val titleRight: String,
            val titleLeft: String,
            val listenerRight: View.OnClickListener,
            val listenerLeft: View.OnClickListener,
        ) : BottomSheetButton()
    }

    companion object {
        const val TAG = "ModalBottomSheet"
        const val SMALL = 0.3f
        const val MEDIUM = 0.5f
    }

    private lateinit var baseBinding: ModalBottomSheetBinding

    private lateinit var _binding: VB
    val binding get() = _binding

    val parentHeight :Int by lazy {
        getWindowHeight()
    }

    var bottomMargin: Int = 50

    /** scale : 바텀시트가 접혔을 때, 켜질 때 높이 비율 */
    abstract val scale: Float
    /** title : 바텀시트 상단의 타이틀 */
    abstract val title: String
    /** baseButton : 버튼 수, 각 버튼 타이틀, 클릭 리스너 */
    abstract val baseButton: BottomSheetButton

    /** 바텀시트 내의 content 영역에 표시할 View Binding 객체 전달 */
    abstract fun inflateViewBinding(inflater: LayoutInflater) : VB

    abstract fun initView()

    /** 테마 설정 - 상단 둥근 테두리 */
    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("Lifecycle", "onCreateView")
        baseBinding = ModalBottomSheetBinding.inflate(layoutInflater)
        _binding = inflateViewBinding(inflater)

        baseBinding.contentArea.addView(binding.root)

        initView()
        initButton()

        return baseBinding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Log.e("Lifecycle", "onCreateDialog")
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
            setBehavior()
        }
        return dialog
    }

    private fun initButton() {
        with (baseBinding) {
            title.text = this@ModalBottomSheet.title
            baseButton.also {
                when (it) {
                    is BottomSheetButton.OneButton -> {
                        buttonLeft.apply {
                            text = it.title
                            setOnClickListener(it.listener)
                        }
                        buttonRight.visibility = View.GONE
                    }
                    is BottomSheetButton.TwoButton -> {
                        buttonLeft.apply {
                            text = it.titleLeft
                            setOnClickListener(it.listenerLeft)
                        }
                        buttonRight.apply {
                            text = it.titleRight
                            setOnClickListener(it.listenerRight)
                        }
                    }
                }
            }
        }
    }

    private fun setBehavior() {
        val bottomSheetDialog = (requireDialog() as BottomSheetDialog).apply {
            dismissWithAnimation = true //  외부터치하여 창 종료시 애니메이션 여부
        }
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = bottomSheetDialog.behavior.apply {
            val currentPeekHeight = parentHeight - bottomSheet.top
            val setPeekHeight = (parentHeight * scale).toInt()
            peekHeight = if (currentPeekHeight < setPeekHeight) currentPeekHeight else setPeekHeight
        }
        val buttonLayout = bottomSheet.findViewById<View>(R.id.buttonLayout)
        val buttonOffset = behavior.peekHeight - buttonLayout.top - convertDPtoPX(bottomMargin)
        Log.e("button position : ", "${buttonLayout.top} \n ${buttonLayout.bottom} \n parent : $parentHeight \n  bottomTop : ${bottomSheet.top} peekHeight : ${behavior.peekHeight} \n convert : ${convertDPtoPX(50)} \n result : $buttonOffset")
        //  바텀시트 켜질 때 바텀시트 비율에 맞게 버튼 위치 조정
        buttonLayout.offsetTopAndBottom(buttonOffset)
//        behavior.maxHeight = (getWindowHeight() * scale).toInt()
//        behavior.saveFlags = BottomSheetBehavior.SAVE_ALL
//        behavior.isFitToContents = true   // 시트 높이가 contents의 높이에 맞추는지
//        behavior.isHideable = true    // 아래로 드래그해서 닫힘 여부
//        behavior.isDraggable = true //  드래그 가능 여부
//        behavior.halfExpandedRatio = 0.2f
        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                bottomSheet.findViewById<TextView>(R.id.textView).text = when (newState) {
//                    BottomSheetBehavior.STATE_EXPANDED -> "STATE_EXPANDED"
//                    BottomSheetBehavior.STATE_COLLAPSED -> "STATE_COLLAPSED"
//                    BottomSheetBehavior.STATE_DRAGGING -> "STATE_DRAGGING"
//                    BottomSheetBehavior.STATE_HALF_EXPANDED -> "STATE_HALF_EXPANDED"
//                    BottomSheetBehavior.STATE_HIDDEN -> "STATE_HIDDEN"
//                    BottomSheetBehavior.STATE_SETTLING -> "STATE_SETTLING"
//                    else -> null
//                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val buttonTop = parentHeight - bottomSheet.top // 바텀시트 픽셀
                val buttonLayout = bottomSheet.findViewById<View>(R.id.buttonLayout)
                buttonLayout.offsetTopAndBottom((buttonTop - convertDPtoPX(bottomMargin)) - buttonLayout.top)
//                val viewHeight2: Float = ((getWindowHeight() * scale).toInt()) * (slideOffset+1) // 바텀시트 픽셀
//                val result1 = 1900 * viewHeight1 / parentHeight
//                Log.e("savedTop", "top : ${button.top} \n bottom : ${button.bottom}")
//                Log.e("viewHeight1", "${viewHeight1}\n ${savedTop}\n ${parentHeight}")
//                Log.e("bottomSheet", "x : ${bottomSheet.x}, y : ${bottomSheet.y}" )
//                Log.e("result", "\n1 : ${result1}")
            }
        }
        behavior.addBottomSheetCallback(bottomSheetCallback)
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        Log.e("hjh", "heightPixels : ${displayMetrics.heightPixels}")
        return displayMetrics.heightPixels
    }

    private fun convertDPtoPX(dp: Int): Int {
        val density: Float = requireContext().resources.displayMetrics.density
        return (dp.toFloat() * density).roundToInt()
    }


    /**
     * 바텀시트의 뷰크기 강제 설정
     */
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.setOnShowListener { dialogInterface ->
//            val bottomSheetDialog = dialogInterface as BottomSheetDialog
//            setupRatio(bottomSheetDialog)
//        }
//        return dialog
//    }
//
//    private fun setupRatio(bottomSheetDialog: BottomSheetDialog){
//        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
//        val layoutParams = bottomSheet!!.layoutParams
//        layoutParams.height = getBottomSheetDialogDefaultHeight()
//        bottomSheet.layoutParams = layoutParams
////        val behavior = BottomSheetBehavior.from(bottomSheet)
////        behavior.state = BottomSheetBehavior.STATE_EXPANDED
//    }
//
//    private fun getBottomSheetDialogDefaultHeight(): Int {
//        return (getWindowHeight() * scale).toInt()
//    }
}


//open class RoundedBottomSheetDialogFragment: BottomSheetDialogFragment(){
//    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)
//    fun expendDialog(activity: FragmentActivity?, logTag: String, performOnError: () -> Unit){
//        try {
//            val bottomSheet = dialog!!.findViewById(R.id.design_bottom_sheet) as View
//            val behavior = BottomSheetBehavior.from(bottomSheet)
//            val displayMetrics = DisplayMetrics()
//            requireActivity().windowManager!!.defaultDisplay!!.getMetrics(displayMetrics)
//            behavior.peekHeight = displayMetrics.heightPixels
//        } catch (e: NullPointerException) {
//            Log.d(logTag, e.message ?: "NPE in onResume")
//            performOnError()
//        }
//    }
//}