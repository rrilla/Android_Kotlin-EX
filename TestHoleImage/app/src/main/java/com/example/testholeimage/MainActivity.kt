package com.example.testholeimage

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.testholeimage.databinding.ActivityMainBinding
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.hypot


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val CUSTOM_MARKER_POINT1 = MapPoint.mapPointWithGeoCoord(35.26563460576242, 129.0980170875011)
    private val CUSTOM_MARKER_POINT2 = MapPoint.mapPointWithGeoCoord(35.26105563333692, 129.10240746980742)
    private val CUSTOM_MARKER_POINT3 = MapPoint.mapPointWithGeoCoord(35.258901538628926, 129.09906542626933)
    private val CUSTOM_MARKER_POINT4 = MapPoint.mapPointWithGeoCoord(35.26348151893561, 129.09467504396298)

    private val url = "https://d2vb0229yoijdi.cloudfront.net/qa/base/20210901000000002192.png"

    private var layout:LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapView = MapView(this).apply {
            setMapCenterPoint(MapPoint.mapPointWithGeoCoord(35.2612103190717, 129.1006609825230), true)
            setZoomLevel(0, true)

            setCurrentLocationEventListener(listenerLocation)   //현위치 트래킹 이벤트
            setMapViewEventListener(listenerEvent)  //이동,확대,축소,터치 등 이벤트


        }

        val marker1 = MapPOIItem().apply {
            itemName = "1"
            tag = 0
            mapPoint = CUSTOM_MARKER_POINT1
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
        }
        val marker2 = MapPOIItem().apply {
            itemName = "2"
            tag = 1
            mapPoint = CUSTOM_MARKER_POINT2
            markerType = MapPOIItem.MarkerType.RedPin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
        }
        val marker3 = MapPOIItem().apply {
            itemName = "3"
            tag = 2
            mapPoint = CUSTOM_MARKER_POINT3
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
        }
        val marker4 = MapPOIItem().apply {
            itemName = "4"
            tag = 3
            mapPoint = CUSTOM_MARKER_POINT4
            markerType = MapPOIItem.MarkerType.YellowPin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
        }

        mapView.addPOIItems(arrayOf(marker1,marker2,marker3,marker4))

        binding.mapView.addView(mapView)
        binding.button.setOnClickListener {
            createView()
//            mapView.findPOIItemByTag(0).also {
//                Log.e("HJH", "${it.mapPoint.mapPointScreenLocation.x}")
//                Log.e("HJH", "${it.mapPoint.mapPointScreenLocation.y}")
//            }
        }
    }

    //Function to convert dp to pixels.
    private fun dpTopx(dp: Int): Int {
        return (dp * applicationContext.resources.displayMetrics.density).toInt()
    }

    fun createView() {
        val x1 = CUSTOM_MARKER_POINT1.mapPointScreenLocation.x
        val y1 = CUSTOM_MARKER_POINT1.mapPointScreenLocation.y
        val x2 = CUSTOM_MARKER_POINT2.mapPointScreenLocation.x
        val y2 = CUSTOM_MARKER_POINT2.mapPointScreenLocation.y
        val x4 = CUSTOM_MARKER_POINT4.mapPointScreenLocation.x
        val y4 = CUSTOM_MARKER_POINT4.mapPointScreenLocation.y

        val mH = y1-y2
        val mW = x2-x1
        val width = hypot(abs(x1 - x2), abs(y1 - y2))
        val height = hypot(abs(x1 - x4), abs(y1 - y4))
        Log.e("HJH", "width : $width, height : $height")
        Log.e("HJH", "mH : $mH, mW : $mW")

        val imageView = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT).apply {
                gravity = Gravity.CENTER
                this.width = (width * 0.6).toInt()
                this.height = (height * 0.6).toInt()
            }
        }.also {
            Glide.with(this).load(url).into(it)
        }

        layout?.let { binding.mapView.removeView(it) }
        layout = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.CENTER
                this.width = width.toInt()
                this.height = height.toInt()
//                val dd = 40
//                setPadding(dpTopx(dd), dpTopx(dd), dpTopx(dd),dpTopx(dd));
            }
            rotation = abs(Math.toDegrees(atan2(mH, mW))).toFloat()
            x = x4.toFloat()
            y = y1.toFloat()
        }.also {
            it.addView(imageView)
            binding.mapView.addView(it)
        }

        Log.e("HJH", "각도 : ${(Math.toDegrees(atan2(mH, mW)))}")

    }

    private val listenerLocation = object: MapView.CurrentLocationEventListener{
        override fun onCurrentLocationUpdate(p0: MapView?, p1: MapPoint?, p2: Float) {
            Log.e("HJH", "mapPoint - lat : ${p1?.mapPointGeoCoord?.latitude} lon : ${p1?.mapPointGeoCoord?.longitude}")
            Log.e("HJH", "mapPoint - x : ${p1?.mapPointScreenLocation?.x} y : ${p1?.mapPointScreenLocation?.y}")
        }
        override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {}
        override fun onCurrentLocationUpdateFailed(p0: MapView?) {}
        override fun onCurrentLocationUpdateCancelled(p0: MapView?) {}
    }

    private val listenerEvent = object: MapView.MapViewEventListener {
        override fun onMapViewInitialized(p0: MapView?) {}

        override fun onMapViewCenterPointMoved(p0: MapView?, p1: MapPoint?) {
            Log.e("HJH", "센터 위치 바뀜 $p1")
            Log.e("HJH", "mapPoint - lat : ${p1?.mapPointGeoCoord?.latitude} lon : ${p1?.mapPointGeoCoord?.longitude}")
            Log.e("HJH", "mapPoint - x : ${p1?.mapPointScreenLocation?.x} y : ${p1?.mapPointScreenLocation?.y}")
        }

        override fun onMapViewZoomLevelChanged(p0: MapView?, p1: Int) {
            Log.e("HJH", "줌 레벨 바뀜 : $p1")
        }

        override fun onMapViewSingleTapped(p0: MapView?, p1: MapPoint?) {}

        override fun onMapViewDoubleTapped(p0: MapView?, p1: MapPoint?) {}

        override fun onMapViewLongPressed(p0: MapView?, p1: MapPoint?) {}

        override fun onMapViewDragStarted(p0: MapView?, p1: MapPoint?) {}

        override fun onMapViewDragEnded(p0: MapView?, p1: MapPoint?) {}

        override fun onMapViewMoveFinished(p0: MapView?, p1: MapPoint?) {}
    }
}