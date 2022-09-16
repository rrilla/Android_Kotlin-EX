package com.example.test_bottomsheet

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_bottomsheet.databinding.ItemRecyclerviewBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlin.math.roundToInt

class ModalBottomSheet : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "ModalBottomSheet"
    }

    private val scale = 0.9f

//    private lateinit var _binding: ModalBottomSheetContentBinding
//    private val binding get() = _binding

    //  테마 설정 - 상단 둥근 테두리
    override fun getTheme(): Int = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        _binding = ModalBottomSheetContentBinding.inflate(inflater, container, false)
//        return _binding.root
        return inflater.inflate(R.layout.modal_bottom_sheet_content, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initRecycleView(view)
//        setBehavior(view)
    }

    private fun setBehavior() {
        //  외부터치하여 창 종료시 애니메이션 여부
        val bottomSheetDialog = (requireDialog() as BottomSheetDialog)
        bottomSheetDialog.dismissWithAnimation = true
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        bottomSheet.findViewById<Button>(R.id.btn_cancel).offsetTopAndBottom((getWindowHeight() - bottomSheet.top) - convertDPtoPX(30))
        val behavior = (requireDialog() as BottomSheetDialog).behavior
//        behavior.maxHeight = (getWindowHeight() * scale).toInt()
        behavior.peekHeight = (getWindowHeight() * scale).toInt()
//        behavior.saveFlags = BottomSheetBehavior.SAVE_ALL
//        behavior.isFitToContents = true   // 시트 높이가 contents의 높이에 맞추는지
//        behavior.isHideable = true    // 아래로 드래그해서 닫힘 여부
//        behavior.isDraggable = true //  드래그 가능 여부
//        behavior.halfExpandedRatio = 0.2f
        val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                Log.e("hjh", "bottomSheet : ${bottomSheet.top} , ${bottomSheet.bottom} \n button : ${button.top} , ${button.bottom}")
                bottomSheet.findViewById<TextView>(R.id.textView).text = when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        "STATE_EXPANDED"
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
//                        ViewCompat.offsetTopAndBottom(button, 800)
                        "STATE_COLLAPSED"
                    }
                    BottomSheetBehavior.STATE_DRAGGING -> {
//                        ViewCompat.offsetTopAndBottom(button, savedTop - button.top)
                        "STATE_DRAGGING"
                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {
//                        ViewCompat.offsetTopAndBottom(button, behavior.halfExpandedOffset)
                        "STATE_HALF_EXPANDED"
                    }
                    BottomSheetBehavior.STATE_HIDDEN -> "STATE_HIDDEN"
                    BottomSheetBehavior.STATE_SETTLING -> {
//                        ViewCompat.offsetTopAndBottom(button, savedTop - button.top)
                        "STATE_SETTLING"
                    }
                    else -> null
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val buttonTop = getWindowHeight() - bottomSheet.top // 바텀시트 픽셀
                val button = bottomSheet.findViewById<Button>(R.id.btn_cancel)
//                val viewHeight2: Float = ((getWindowHeight() * scale).toInt()) * (slideOffset+1) // 바텀시트 픽셀
//                val result1 = 1900 * viewHeight1 / parentHeight
//                Log.e("savedTop", "top : ${button.top} \n bottom : ${button.bottom}")
//                Log.e("viewHeight1", "${viewHeight1}\n ${savedTop}\n ${parentHeight}")
//                Log.e("bottomSheet", "x : ${bottomSheet.x}, y : ${bottomSheet.y}" )
//                Log.e("result", "\n1 : ${result1}")

                button.offsetTopAndBottom((buttonTop - convertDPtoPX(50)) - button.top)
            }
        }
        behavior.addBottomSheetCallback(bottomSheetCallback)
    }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener { dialogInterface ->
//            val bottomSheetDialog = dialogInterface as BottomSheetDialog
//            setupRatio(bottomSheetDialog)
            setBehavior()
        }
        return dialog
    }

    private fun setupRatio(bottomSheetDialog: BottomSheetDialog){
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
        val behavior = BottomSheetBehavior.from(bottomSheet)
        val button = bottomSheet.findViewById<Button>(R.id.btn_cancel)
        Log.e("hjh", bottomSheet.top.toString())
        button.offsetTopAndBottom((getWindowHeight() - bottomSheet.top) - convertDPtoPX(50))

//        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getWindowHeight(): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        Log.e("hjh", "heightPixels : ${displayMetrics.heightPixels}")
        return displayMetrics.heightPixels
    }

    private fun initRecycleView(view: View) {
        val data = mutableListOf<String>()
        for(i in 1..30){
            data.add("Item $i")
        }
        view.findViewById<RecyclerView>(R.id.recycleView).apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = MyAdapter(data)
        }
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


//  Test용 RecycleViewAdapter

//항목 View를 가지는 역활
class MyViewHolder(val binding: ItemRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)
//항목 구성자. Adapter
class MyAdapter(val datas: MutableList<String>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    //항목 갯수를 판단하기 위해서 자동 호출
    override fun getItemCount(): Int{
        return datas.size
    }
    //항목의 뷰를 가지는 Holder 를 준비하기 위해 자동 호출
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = MyViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    //각 항목을 구성하기 위해서 호출
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding=(holder as MyViewHolder).binding
        //뷰에 데이터 출력
        binding.itemData.text= datas[position]
    }
}