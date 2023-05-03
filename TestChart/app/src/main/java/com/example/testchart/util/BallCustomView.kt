package com.example.testchart.util

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View


class BallCustomView constructor(context: Context, attrs: AttributeSet?) : View(context, attrs) {
   private val TAG: String = this::class.java.simpleName

   val circleLine = Paint().apply { color = Color.BLUE }
   val guideLineBlue = Paint().apply { color = Color.GREEN }
   val ballColors = Paint().apply { color = Color.GREEN }


   var scale: Float = 1f
   var centerGridLine = false

   init {
   }

//   var text : ClubTypeColorData = ClubTypeColorData("I3", Color.argb(255, 187, 134, 252))
   var text = "H1"

   override fun onDraw(canvas: Canvas?) {
      super.onDraw(canvas)
      if (canvas == null) return
      var heightRatio: Float = (height.toFloat() / width.toFloat())
      Log.e(TAG, "DRAW_START > heightRatio: $heightRatio, View.width: $width, View.height: $height , measuredWidth: $measuredWidth , measuredHeight: $measuredHeight")

      if (width < 0) {
         Log.e(TAG, "testSize is not initialized yet.")
         return
      }

      val startX = 0f
      val startY = 0f

      if (centerGridLine) {
         // Draw Guide Lines
         canvas.drawLine(startX, startY, startX + width, startY, circleLine)
         canvas.drawLine(startX, startY, startX, startY + height, guideLineBlue)
         canvas.drawLine(width.toFloat(), height.toFloat(), startX, startY + height, guideLineBlue)
         canvas.drawLine(width.toFloat(), height.toFloat(), startX + width, startY, guideLineBlue)

//      canvas.drawLine(startX, startY, width.toFloat(), height.toFloat(), Const.guideLineBlue)
//      canvas.drawLine(startX, height.toFloat(), startX + width, startY, Const.guideLineBlue)
      }

      val centerX = (startX + width.toFloat()) /2
      val centerY = (startY + height.toFloat()) /2

      val textPaint = Paint().apply {
         strokeWidth = 1f
         color = Color.WHITE
         style = Paint.Style.FILL_AND_STROKE
         textAlign = Paint.Align.CENTER
         textSize = 400f * scale
      }

      Log.d(TAG," TEST: TextSize : ${400f * scale} , Scale: ${scale}")
      textPaint.apply {

      }

      val ballRadius = (centerX-startX)*0.95f

      canvas.drawCircle(centerX, centerY, ballRadius, circleLine)
//      Const.ballColors[0].color = text.colorCode
      canvas.drawCircle(centerX, centerY, ballRadius*0.995f, ballColors)

      // FontMetrics
      val fontMetrics: Paint.FontMetrics = textPaint.fontMetrics
      Log.d(TAG,"FontMetrics > top: ${fontMetrics.top}, bottom: ${fontMetrics.bottom}, ascent: ${fontMetrics.ascent}, descent: ${fontMetrics.descent}, leading: ${fontMetrics.leading}")

      val fontHeight = fontMetrics.bottom - fontMetrics.top
      val distance = fontHeight/2 - fontMetrics.bottom
      Log.d(TAG, "FontMetrics >> fontHeight: $fontHeight , distance: $distance")

      // DrawText at center point
      Log.d(TAG, " > centerX : $centerX , centerY : $centerY")

      canvas.drawText(text, centerX, centerY + distance, textPaint)



      // 가운데 격자선
      if (centerGridLine) {
         canvas.drawLine(centerX, startY, centerX, startY + height, guideLineBlue)
         canvas.drawLine(startX, centerY, startX + width, centerY, guideLineBlue)
      }
   }

   override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
      super.onSizeChanged(w, h, oldw, oldh)
      Log.i(TAG, "onSizeChanged-() w: $w, h: $h, oldW: $oldw, oldH: $oldh")
   }

   override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec)
      Log.i(TAG, "onMeasure-() * widthMeasureSpec: $widthMeasureSpec, heightMeasureSpec: $heightMeasureSpec")

      val minw = paddingLeft + paddingRight + suggestedMinimumWidth
      val w = View.resolveSizeAndState(minw, widthMeasureSpec, 1)

      // Whatever the width ends up being, ask for a height that would let the pie get as big as it can
      val minh = View.MeasureSpec.getSize(w) + paddingBottom + paddingTop
      val h: Int = View.resolveSizeAndState(minh, heightMeasureSpec, 0)

      // 계산된 w, h 값

      Log.i(TAG, "onMeasure-() * suggestedMinWidth: $suggestedMinimumWidth , suggestedMinHeight: $suggestedMinimumHeight")
      Log.i(TAG, "onMeasure-() * paddingLeft: $paddingLeft, paddingTop: $paddingTop, paddingRight: $paddingRight, paddingBottom: $paddingBottom")
      Log.i(TAG, "onMeasure-() * w: $w, minW: $minw, h: $h, minH: $minh")

      setMeasuredDimension(w, h)
   }

}