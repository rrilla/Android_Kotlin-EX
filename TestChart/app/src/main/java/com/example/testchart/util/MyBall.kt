package com.example.testchart.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.View

class MyBall : View {
    // 생성자
    constructor(context: Context?) : super(context)

    var ballColor: Int = Color.BLACK
    var ballText: String = "H1"
    var size: Float = 50f

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//
//        // 뷰 크기 모드 체크
//        val viewWidthMode = MeasureSpec.getMode(widthMeasureSpec)
//        val viewHeightMode = MeasureSpec.getMode(heightMeasureSpec)
//
//        // 뷰 크기 값 체크
//        val viewWidthSize = MeasureSpec.getSize(widthMeasureSpec)
//        val viewHeightSize = MeasureSpec.getSize(heightMeasureSpec)
//
//
////        // 크기 모드에 따라 setMeasuredDimension() 메서드로 뷰의 영역 크기를 설정한다.
////        if(viewWidthMode == MeasureSpec.EXACTLY && viewHeightMode == MeasureSpec.EXACTLY){
////            // XML에서 뷰의 크기가 특정 값으로 설정된 경우, 그대로 사용한다.
////            setMeasuredDimension(viewWidthSize, viewHeightSize)
////        }else{
////            // wrap_content이거나, 지정되지 않은 경우, 뷰의 크기를 내부에서 지정해주어야 한다
////            setMeasuredDimension(10, 10)
////        }
//        setMeasuredDimension(100,100)
//    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 배경
//        canvas?.drawColor(Color.GREEN)

        // 공 배경(원)
        val paint = Paint()
        paint.color = ballColor

        //  공 텍스트(글)
        val textPaint = Paint().apply {
            strokeWidth = 1f
            color = Color.BLACK
            style = Paint.Style.FILL_AND_STROKE
            textAlign = Paint.Align.CENTER
            textSize = size / 2
        }
        val fontMetrics: Paint.FontMetrics = textPaint.fontMetrics
        val fontHeight = fontMetrics.bottom - fontMetrics.top

        canvas.apply {
            scale(-1f, 1f, (canvas.width / 4).toFloat(), (canvas.height / 4).toFloat())
            drawCircle(size, size, size / 2, paint)
            drawText(ballText, size, size + fontHeight / 4, textPaint)
        }
    }
}