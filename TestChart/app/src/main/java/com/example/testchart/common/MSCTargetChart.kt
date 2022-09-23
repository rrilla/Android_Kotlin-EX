package com.example.testchart.common

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.testchart.R
import com.example.testchart.data.MyYAxisValueFormatter
import com.example.testchart.data.ShotData
import com.example.testchart.util.ReverseRelativeLayout
import com.example.testchart.util.SC300LIB
import com.example.testchart.util.Utils.Companion.getClubApex
import com.example.testchart.util.Utils.Companion.getSettingUnit
import com.example.testchart.util.Utils.Companion.valueForUnit
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnAnimationListener

class MSCTargetChart : ConstraintLayout, OnAnimationListener {
    enum class Mode (
        val animation: Boolean,
        val keepLine: Boolean
            ) {
        STATS(animation = false, keepLine = true),
        TARGET(animation = true, keepLine = false)
    }

    val targetLabel = "TARGET"

    private val TAG: String = this::class.java.simpleName

    private val UNIT_DISTANCE = arrayOf("yd", "yd", "m", "m", "m", "m", "yd", "yd")
    private val UNIT_APEX = arrayOf("ft", "ft", "ft", "ft", "m", "m", "m", "m")
    private val DEFAULT_TARGET_CLUB_CODE = 11
    private val DEFAULT_TARGET_DISTANCE = 15
    private val DEFAULT_CHART_X = 100

    private val mLastTrajectory_t = DoubleArray(200) //시간
    private val mLastTrajectory_y = DoubleArray(200) //수평거리
    private val mLastTrajectory_z = DoubleArray(200) //높이

    private lateinit var lineChart: LineChart
    private lateinit var mLayoutNoShot: View
    private lateinit var mDataTarget: LineDataSet
    private val mDataSets = ArrayList<ILineDataSet>()
    private var mdMaxHeight = 0.0

    //  이 놈을 Res가 아닌 View로 그린 공으로 교체 해야됨.
    private lateinit var mListGrayBall: IntArray
    private lateinit var mListRedBall: IntArray
    private var mLastShotData: ShotData? = null
    private var mShotDataList: ArrayList<ShotData>? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    protected fun init(context: Context?) {
        val view = inflate(context, R.layout.msc_target_chart, this)
        mLayoutNoShot = view.findViewById(R.id.layoutNohaveShot)
        val reverseRelativeLayout =
            view.findViewById<ReverseRelativeLayout>(R.id.layoutTargetReverse)
        reverseRelativeLayout.setReverse(true)
        lineChart = view.findViewById(R.id.targetLineChart)
        initChart()
        setTargetDistance(DEFAULT_TARGET_DISTANCE)
    }

    protected fun initChart() {
        lineChart.minOffset = 20f
        lineChart.setNoDataText("")
        //mLineChart.setOnDrawListener(this);
        lineChart.onAnimationListener = this
        lineChart.description.isEnabled = false
        lineChart.axisLeft.isEnabled = true
        lineChart.rendererLeftYAxis.setDrawReverse(true)
        lineChart.rendererLeftYAxis.setUnit(UNIT_APEX[getSettingUnit().toInt()])
        lineChart.axisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        lineChart.axisLeft.valueFormatter = MyYAxisValueFormatter()
        lineChart.rendererXAxis.setUnit(UNIT_DISTANCE[getSettingUnit().toInt()])
        lineChart.axisRight.isEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.setTouchEnabled(false)

        //  이걸 왜 그리지 초기화할 때??
//        List<Entry> entries = new ArrayList<>();
//        entries.add(new Entry(0, 0));
//        LineDataSet lineDataSet = new LineDataSet(entries, "");
//        lineDataSet.setDrawCircles(false);
//        lineChart.setData(new LineData(lineDataSet));
        lineChart.axisLeft.axisMinimum = 0f
        val clubCode = DEFAULT_TARGET_CLUB_CODE
        val nMaxApex = getClubApex(clubCode, getSettingUnit().toInt())
        lineChart.axisLeft.axisMaximum = nMaxApex.toFloat()
        val maxDistance = DEFAULT_TARGET_DISTANCE.toFloat() * 1.3f
        lineChart.xAxis.axisMaximum = maxDistance
        lineChart.xAxis.setDrawGridLines(false)
        lineChart.invalidate()
        mListGrayBall = IntArray(10)
        mListGrayBall[0] = R.drawable.gray_01
        mListRedBall = IntArray(10)
        mListRedBall[0] = R.drawable.red_01

        lineChart.axisLeft.setLabelCount(4, true)
        lineChart.axisLeft.textColor = resources.getColor(R.color.main_graph_axis_y)
        lineChart.isDoubleTapToZoomEnabled = false
        lineChart.isDragEnabled = false
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.labelRotationAngle = -180f
        lineChart.xAxis.textColor = resources.getColor(R.color.main_graph_axis_y)
    }

    fun drawChart(lastShotData: ShotData?, dataSets: ArrayList<ShotData>?, mode: Mode) {
        mLastShotData = lastShotData
        mShotDataList = dataSets
        drawChart(mode)
    }

    fun drawStatsChart(lastShotData: ShotData?, dataSets: ArrayList<ShotData>?, mode: Mode, unit: String, target: Int) {
        Log.e(TAG, lastShotData.toString() + dataSets.toString())
        mLastShotData = lastShotData
        mShotDataList = dataSets
        setTargetDistance(target)
        setUnit(unit)
        drawChart(mode)
    }

    private fun drawChart(mode: Mode) {
        setShotList(mode.animation) // return mDataSets(차트에 그릴 공, 라인)
        // TODO: 단위 설정, 타겟 설정 추가하고 사용처에서 단위,타겟 설정 실행

        // 데이터 없을 때 보여줄 view
        mLayoutNoShot.visibility = if (mDataSets.size == 0) VISIBLE else GONE

        val lineData = LineData()
        lineData.addDataSet(mDataTarget)
        lineData.isHighlightEnabled = false
        // TODO: setShotList()함수 내부에서 아래 처리, mDataSets 전역변수 -> 지역변수
        if (mDataSets.size > 0) {
            for (dataset in mDataSets) {
                lineData.addDataSet(dataset)
            }
        } else {
            val entries: MutableList<Entry> = ArrayList()
            entries.add(Entry(1f, 0f))
            val lineDataSet = LineDataSet(entries, "")
            lineDataSet.setDrawCircles(false)
            lineDataSet.setDrawValues(false)
            lineData.addDataSet(lineDataSet)
        }
        // TODO: 타겟 교체하기 테스트 해봐야됨
//        lineChart.data = lineData
//        lineChart.data.getDataSetByLabel(targetLabel, false)
//        val int = lineChart.data.getIndexOfDataSet(lineChart.data.getDataSetByLabel(targetLabel, false))
//        lineChart.data.dataSets.removeAt(int)
//        lineChart.data.dataSets.add() //타겟 넣기


//        if (lineChart.xChartMax > mChartMax) mChartMax = lineChart.xChartMax.toDouble()
//        val clubCode = DEFAULT_TARGET_CLUB_CODE
//        if (mChartMax < CLUB_CARRY[clubCode]) {
//            mChartMax = CLUB_CARRY[clubCode].toDouble()
//        }
        if (mdMaxHeight > lineChart.axisLeft.axisMaximum) {
            lineChart.axisLeft.axisMaximum = mdMaxHeight.toFloat()
        }

        //  차트에 그릴 값에 따라 차트 범위 설정 (가로)
        mShotDataList?.let { data ->
            val maxDataTotal = data.maxOfOrNull {
                it.total
            } ?: 0f
            if (maxDataTotal > lineChart.xAxis.axisMaximum) {
                lineChart.xAxis.axisMaximum = maxDataTotal * 1.3f
            }
        }

        mLastShotData?.let {
            val maxShotTotal = mLastShotData?.total ?: 0f
            if (maxShotTotal > lineChart.xAxis.axisMaximum) {
                lineChart.xAxis.axisMaximum = maxShotTotal * 1.3f
            }
        }

//        lineChart.axisLeft.setLabelCount(4, true)
//        lineChart.axisLeft.textColor = resources.getColor(R.color.main_graph_axis_y)
//        lineChart.isDoubleTapToZoomEnabled = false
//        lineChart.isDragEnabled = false
//        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
//        lineChart.xAxis.labelRotationAngle = -180f
//        lineChart.xAxis.textColor = resources.getColor(R.color.main_graph_axis_y)
        if (mode.animation) lineChart.animateX(2000)

        lineChart.postInvalidate()
    }

    // 단위 설정
    fun setUnit(unit: String?) {
        var bUnit = getSettingUnit()
        if (unit != null && unit == "meter") {
            if (bUnit.toInt() == 0) {
                bUnit = 3
            } else if (bUnit.toInt() == 1) {
                bUnit = 2
            } else if (bUnit.toInt() == 6) {
                bUnit = 4
            } else if (bUnit.toInt() == 7) {
                bUnit = 5
            }
        } else {
            if (bUnit.toInt() == 2) {
                bUnit = 1
            } else if (bUnit.toInt() == 3) {
                bUnit = 0
            } else if (bUnit.toInt() == 4) {
                bUnit = 6
            } else if (bUnit.toInt() == 5) {
                bUnit = 7
            }
        }
        lineChart.rendererLeftYAxis.setUnit(UNIT_APEX[bUnit.toInt()])
        lineChart.rendererXAxis.setUnit(UNIT_DISTANCE[bUnit.toInt()])
        val clubCode = DEFAULT_TARGET_CLUB_CODE
        val nMaxApex = getClubApex(clubCode, getSettingUnit().toInt())
        lineChart.axisLeft.axisMaximum = nMaxApex.toFloat()
        val maxDistance = DEFAULT_TARGET_DISTANCE.toFloat() * 1.3f
        if (maxDistance > lineChart.xAxis.axisMaximum) {
            lineChart.xAxis.axisMaximum = maxDistance
        }
        lineChart.invalidate()
        drawChart(Mode.STATS)
    }

    // 타겟 거리 설정 - 타겟 이미지 표시, 그래프 범위 변경
    fun setTargetDistance(integer: Int) {
        val entries = ArrayList<Entry>()
        val drawableflag = resources.getDrawable(R.drawable.target_img_flag_left)
        //Drawable drawableflag = getResources().getDrawable(R.drawable.graph_icon_target);
        val ypos = 0f
        val entryFlag = Entry(
            integer.toFloat(), ypos, drawableflag
        )
        entries.add(entryFlag)
        mDataTarget = LineDataSet(entries, targetLabel)
        mDataTarget.lineWidth = 0f
        mDataTarget.setDrawIcons(true)
        mDataTarget.setDrawCircles(false)
        mDataTarget.setDrawValues(false)

        // TODO: 가로 세로 max 시점 정리해야함. unit, target, draw 다함
        val clubCode = DEFAULT_TARGET_CLUB_CODE
        val nMaxApex = getClubApex(clubCode, getSettingUnit().toInt())
        lineChart.axisLeft.axisMaximum = nMaxApex.toFloat()
        lineChart.xAxis.axisMaximum = DEFAULT_CHART_X * 1.3f
        lineChart.setVisibleXRangeMaximum(DEFAULT_CHART_X * 1.3f)
        lineChart.invalidate()
        drawChart(Mode.STATS)
    }

    fun px2dp(px: Float): Float {
        var density = context.resources.displayMetrics.density
        if (density.toDouble() == 1.0) density *= 4.0.toFloat() else if (density.toDouble() == 1.5) density *= (8 / 3).toFloat() else if (density.toDouble() == 2.0) density *= 2.0.toFloat()
        return px / density
    }

    //  차트의 라인, 공
    protected fun setShotList(animation: Boolean) {
        mDataSets.clear()
        val color = R.color.chart_carry_max
        //        if (mLastShotData != null) {
//            LineDataSet lds = createLineDataSet(getTrajectoryList(mLastShotData), "LAST", color);
//            mDataSets.add(lds);
//        }
        if (mShotDataList != null) {
            for (i in mShotDataList!!.indices) {
                val lds1 = createLineDataSet(getTrajectoryList(mShotDataList!![i]), "LAST", color)
                mDataSets.add(lds1)
            }
        }
        if (mShotDataList == null) mShotDataList = ArrayList()

        //  샷 데이터리스트가 10보다 작을 때
        var i = 0
        while (i < mShotDataList!!.size && i < 10) {
            if (animation && i == mShotDataList!!.size - 1) break
            val data = mShotDataList!![i]
            val entries = ArrayList<Entry>()
            val unit = UNIT_DISTANCE[getSettingUnit().toInt()]
            val distance = valueForUnit(data.carry, unit)

            //  마지막 샷이면
//            if(i == mShotDataList.size()-1){
//                entries.add(new Entry(distance, 0, getResources().getDrawable(mListRedBall[i])));
//            } else {
//                entries.add(new Entry(distance, 0, getResources().getDrawable(mListGrayBall[i])));
//            }

            //  볼 이미지 그리기
            entries.add(
                Entry(
                    distance, 0F, resources.getDrawable(
                        mListGrayBall[0]
                    )
                )
            )
            val lds = LineDataSet(entries, String.format("C%d", i))
            lds.lineWidth = 0f
            lds.setDrawIcons(true)
            lds.setDrawCircles(true)
            lds.setDrawValues(false)
            mDataSets.add(lds)
            i++
        }
    }

    // 라인 그리기
    private fun createLineDataSet(list: List<Entry>, label: String, id: Int): LineDataSet {
        val dataSet = LineDataSet(list, label)
        dataSet.lineWidth = 1.5f // 선 두께
        dataSet.setDrawCircles(false)
        dataSet.setDrawValues(false)
        dataSet.color = resources.getColor(id)
        //        dataSet.setDrawIcons(true);
//        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        return dataSet
    }

    private fun getTrajectoryList(shotData: ShotData): List<Entry> {
        val entryList: MutableList<Entry> = ArrayList()
        val sc300lib = SC300LIB()
        val zmax_index = sc300lib.sc300_get_trajectory(
            shotData.ball_speed.toDouble(),
            (if (shotData.launch_angle > 100) shotData.launch_angle - 100 else shotData.launch_angle).toDouble(),
            shotData.spin_rate.toInt(),
            shotData.temperature.toInt(),
            shotData.air_pressure.toInt(),
            shotData.carry.toDouble(),
            (if (shotData.apex > 100) shotData.apex - 100 else shotData.apex).toDouble(),
            mLastTrajectory_t,
            mLastTrajectory_y,
            mLastTrajectory_z
        )
        val yFactor = meterYardFactor
        val zFactor = meterFeetFactor
        if (!(zmax_index <= 0 || zmax_index >= 200)) {
            for (i in 0..199) {
                mLastTrajectory_y[i] = mLastTrajectory_y[i] * yFactor
                mLastTrajectory_z[i] = mLastTrajectory_z[i] * zFactor
                //                Log.d("hjh", "i : " + i + ", y : " + (float) mLastTrajectory_y[i] + ", z : " + (float) mLastTrajectory_z[i]);
                entryList.add(
                    Entry(
                        mLastTrajectory_y[i].toFloat(), mLastTrajectory_z[i].toFloat()
                    )
                )
                if (mdMaxHeight < mLastTrajectory_z[i]) mdMaxHeight = mLastTrajectory_z[i]
            }
        }
        return entryList
    }

    val meterYardFactor: Float
        get() = if (UNIT_DISTANCE[getSettingUnit().toInt()] == "yd") 1.09361f else 1.0f
    val meterFeetFactor: Float
        get() = if (UNIT_APEX[getSettingUnit().toInt()] == "ft") 3.28084f else 1.0f

    override fun onAnimationStart() {
        super.onAnimationStart()
    }

    override fun onAnimationEnd() {
        super.onAnimationEnd()
        //  TTS
//        GlobalViewModel.getInstance().getPlaySoundPoolSfxAction().postValue(MainActivity.SoundType.RECEIVE_READY.ordinal());
//        drawChart(Mode.STATS)
    }

    companion object {
        val CLUB_CARRY = intArrayOf(
            240, 240, 210, 210, 210,
            210, 210, 210, 180, 180,
            180, 180, 180, 180, 170,
            170, 150, 150, 150, 150,
            140, 140
        )
    }
}