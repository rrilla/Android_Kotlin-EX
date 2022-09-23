package com.example.testchart.common;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.testchart.R;
import com.example.testchart.data.MyYAxisValueFormatter;
import com.example.testchart.data.ShotData;
import com.example.testchart.util.ReverseRelativeLayout;
import com.example.testchart.util.SC300LIB;
import com.example.testchart.util.Utils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnAnimationListener;

import java.util.ArrayList;
import java.util.List;


public class MSCTargetChart extends ConstraintLayout implements OnAnimationListener {

    String[] UNIT_DISTANCE = new String[]{"yd", "yd", "m", "m", "m", "m", "yd", "yd"};
    String[] UNIT_APEX = new String[]{"ft", "ft", "ft", "ft", "m", "m", "m", "m"};
    Integer DEFAULT_TARGET_CLUB_CODE = 11;
    Integer DEFAULT_TARGET_DISTANCE = 15;
    public static final int[] CLUB_CARRY = {
            240, 240, 210, 210, 210,
            210, 210, 210, 180, 180,
            180, 180, 180, 180, 170,
            170, 150, 150, 150, 150,
            140, 140};

    LineChart lineChart;

    private View mLayoutNoShot;

    private LineDataSet mDataTarget;
    private ArrayList<ILineDataSet> mDataSets = new ArrayList<>();
    private double mdMaxHeight = 0;
    private double mChartMax = 0;

    private double[] mLastTrajectory_t = new double[200];//시간
    private double[] mLastTrajectory_y = new double[200];//수평거리
    private double[] mLastTrajectory_z = new double[200];//높이

    //  이 놈을 Res가 아닌 View로 그린 공으로 교체 해야됨.
    private int[] mListGrayBall;
    private int[] mListRedBall;

    private ShotData mLastShotData;
    private ArrayList<ShotData> mShotDataList;

    public MSCTargetChart(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MSCTargetChart(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MSCTargetChart(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        View view = View.inflate(context, R.layout.msc_target_chart, this);
        mLayoutNoShot = view.findViewById(R.id.layoutNohaveShot);
        ReverseRelativeLayout reverseRelativeLayout = view.findViewById(R.id.layoutTargetReverse);
        reverseRelativeLayout.setReverse(true);
        lineChart = view.findViewById(R.id.targetLineChart);

        initChart();

        setTargetDistance(DEFAULT_TARGET_DISTANCE);
    }

    protected void initChart() {
        lineChart.setMinOffset(20f);
        lineChart.setNoDataText("");
        //mLineChart.setOnDrawListener(this);
        lineChart.setOnAnimationListener(this);
        lineChart.getDescription().setEnabled(false);

        lineChart.getAxisLeft().setEnabled(true);
        lineChart.getRendererLeftYAxis().setDrawReverse(true);
        lineChart.getRendererLeftYAxis().setUnit(UNIT_APEX[Utils.Companion.getSettingUnit()]);
        lineChart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        lineChart.getAxisLeft().setValueFormatter(new MyYAxisValueFormatter());
        lineChart.getRendererXAxis().setUnit(UNIT_DISTANCE[Utils.Companion.getSettingUnit()]);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.setTouchEnabled(false);

        //  이걸 왜 그리지 초기화할 때??
//        List<Entry> entries = new ArrayList<>();
//        entries.add(new Entry(0, 0));
//        LineDataSet lineDataSet = new LineDataSet(entries, "");
//        lineDataSet.setDrawCircles(false);
//        lineChart.setData(new LineData(lineDataSet));
        lineChart.getAxisLeft().setAxisMinimum(0);
        int clubCode = DEFAULT_TARGET_CLUB_CODE;
        int nMaxApex = Utils.Companion.getClubApex(clubCode, Utils.Companion.getSettingUnit());
        lineChart.getAxisLeft().setAxisMaximum(nMaxApex);
        float maxDistance = (float) DEFAULT_TARGET_DISTANCE * 1.3f;
        lineChart.getXAxis().setAxisMaximum(maxDistance);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.invalidate();

        mListGrayBall = new int[10];
        mListGrayBall[0] = R.drawable.gray_01;

        mListRedBall = new int[10];
        mListRedBall[0] = R.drawable.red_01;
    }

    public void drawChart(ShotData lastShotData, ArrayList<ShotData> dataSets, boolean animation) {
        mLastShotData = lastShotData;
        mShotDataList = dataSets;
        drawChart(animation);
    }

    public void drawChart(boolean animation) {
        setShotList(animation);

        // 데이터 없을 때 보여줄 view
        if (mDataSets.size() == 0) mLayoutNoShot.setVisibility(VISIBLE);
        else mLayoutNoShot.setVisibility(GONE);

        LineData lineData = new LineData();

        lineData.addDataSet(mDataTarget);
        lineData.setHighlightEnabled(false);
        if (mDataSets.size() > 0) {
            for (ILineDataSet dataset : mDataSets) {
                lineData.addDataSet(dataset);
            }
        } else {
            List<Entry> entries = new ArrayList<>();
            entries.add(new Entry((float) 1, 0));
            LineDataSet lineDataSet = new LineDataSet(entries, "");
            lineDataSet.setDrawCircles(false);
            lineDataSet.setDrawValues(false);
            lineData.addDataSet(lineDataSet);
        }
        lineChart.setData(lineData);

        ///////////////////////////////////
        if (lineChart.getXChartMax() > mChartMax) mChartMax = lineChart.getXChartMax();
        int clubcode = DEFAULT_TARGET_CLUB_CODE;
        if (mChartMax < CLUB_CARRY[clubcode]) {
            mChartMax = CLUB_CARRY[clubcode];
        }


        if (mdMaxHeight > lineChart.getAxisLeft().getAxisMaximum()) {
            lineChart.getAxisLeft().setAxisMaximum((float) (mdMaxHeight));
        }

        //  차트에 그릴 값에 따라 차트 범위 설정
//        if (mShotDataList != null) {
//            ArrayList<Float> arr = new ArrayList<Float>();
////            float maxValue = mShotDataList.
//            for (int i = 0; i < mShotDataList.size(); i++) {
//                arr.add(mShotDataList.get(i).getTotal());
//            }
//        }
        if (mLastShotData != null && mLastShotData.getTotal() > lineChart.getXAxis().getAxisMaximum()) {
            lineChart.getXAxis().setAxisMaximum(mLastShotData.getTotal() * 1.3f);
        }

        lineChart.getAxisLeft().setLabelCount(4, true);
        lineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.main_graph_axis_y));

        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setLabelRotationAngle(-180);
        lineChart.getXAxis().setTextColor(getResources().getColor(R.color.main_graph_axis_y));

        if (animation) lineChart.animateX(2000);

        //if (!mbEnable) return;
        ///////////////////////////////////

        lineChart.postInvalidate();
    }

    // 단위 설정
    public void setUnit(Short unit) {
        lineChart.getRendererLeftYAxis().setUnit(UNIT_APEX[unit]);
        lineChart.getRendererXAxis().setUnit(UNIT_DISTANCE[unit]);

        int clubCode = DEFAULT_TARGET_CLUB_CODE;
        int nMaxApex = Utils.Companion.getClubApex(clubCode, Utils.Companion.getSettingUnit());
        lineChart.getAxisLeft().setAxisMaximum(nMaxApex);
        float maxDistance = (float) DEFAULT_TARGET_DISTANCE * 1.3f;
        if (maxDistance > lineChart.getXAxis().getAxisMaximum()) {
            lineChart.getXAxis().setAxisMaximum(maxDistance);
        }
        lineChart.invalidate();

        drawChart(false);
    }

    // 타겟 거리 설정 - 타겟 이미지 표시, 그래프 범위 변경
    public void setTargetDistance(Integer integer) {
        ArrayList<Entry> entries = new ArrayList<>();
        Drawable drawableflag = getResources().getDrawable(R.drawable.target_img_flag_left);
        //Drawable drawableflag = getResources().getDrawable(R.drawable.graph_icon_target);

        float ypos = 0;
        Entry entryFlag = new Entry(integer, ypos, drawableflag);
        entries.add(entryFlag);

        mDataTarget = new LineDataSet(entries, "TARGET");
        mDataTarget.setLineWidth(0);
        mDataTarget.setDrawIcons(true);
        mDataTarget.setDrawCircles(false);
        mDataTarget.setDrawValues(false);

        int clubCode = DEFAULT_TARGET_CLUB_CODE;
        int nMaxApex = Utils.Companion.getClubApex(clubCode, Utils.Companion.getSettingUnit());
        lineChart.getAxisLeft().setAxisMaximum(nMaxApex);
        float maxDistance = (float) integer * 1.3f;
        lineChart.getXAxis().setAxisMaximum(maxDistance);
        lineChart.setVisibleXRangeMaximum(maxDistance);
        lineChart.invalidate();

        drawChart(false);
    }


    public float px2dp(float px) {
        float density = getContext().getResources().getDisplayMetrics().density;
        if (density == 1.0) density *= 4.0;
        else if (density == 1.5) density *= (8 / 3);
        else if (density == 2.0) density *= 2.0;

        return px / density;
    }

    protected void setShotList(boolean animation) {
        mDataSets.clear();

        int color = R.color.chart_carry_max;
//        if (mLastShotData != null) {
//            LineDataSet lds = createLineDataSet(getTrajectoryList(mLastShotData), "LAST", color);
//            mDataSets.add(lds);
//        }
        if (mShotDataList != null) {
            for (int i = 0; i < mShotDataList.size(); i++) {
                LineDataSet lds1 = createLineDataSet(getTrajectoryList(mShotDataList.get(i)), "LAST", color);
                mDataSets.add(lds1);
            }
        }

        if (mShotDataList == null) mShotDataList = new ArrayList<>();

        //  샷 데이터리스트가 10보다 작을 때
        for (int i = 0; i < mShotDataList.size() && i < 10; i++) {
            if (animation && i == mShotDataList.size() - 1) break;
            ShotData data = mShotDataList.get(i);

            ArrayList<Entry> entries = new ArrayList<>();

            String unit = UNIT_DISTANCE[Utils.Companion.getSettingUnit()];
            float distance = Utils.Companion.valueForUnit(data.getCarry(), unit);

            //  마지막 샷이면
//            if(i == mShotDataList.size()-1){
//                entries.add(new Entry(distance, 0, getResources().getDrawable(mListRedBall[i])));
//            } else {
//                entries.add(new Entry(distance, 0, getResources().getDrawable(mListGrayBall[i])));
//            }
            //  볼 이미지 그리기
            entries.add(new Entry(distance, 0, getResources().getDrawable(mListGrayBall[0])));

            LineDataSet lds = new LineDataSet(entries, String.format("C%d", i));
            lds.setLineWidth(0);
            lds.setDrawIcons(true);
            lds.setDrawCircles(true);
            lds.setDrawValues(false);
            mDataSets.add(lds);
        }
    }

    // 라인 그리기
    private LineDataSet createLineDataSet(List<Entry> list, String label, int id) {
        LineDataSet dataSet = new LineDataSet(list, label);
        dataSet.setLineWidth(1.5f); // 선 두께
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setColor(getResources().getColor(id));
//        dataSet.setDrawIcons(true);
//        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        return dataSet;
    }

    private List<Entry> getTrajectoryList(ShotData shotData) {
        List<Entry> entryList = new ArrayList<>();
        SC300LIB sc300lib = new SC300LIB();

        int zmax_index = sc300lib.sc300_get_trajectory(shotData.getBall_speed(),
                (shotData.getLaunch_angle() > 100 ? shotData.getLaunch_angle() - 100 : shotData.getLaunch_angle()),
                (int) shotData.getSpin_rate(), (int) shotData.getTemperature(), (int) shotData.getAir_pressure(),
                shotData.getCarry(), (shotData.getApex() > 100 ? shotData.getApex() - 100 : shotData.getApex()),
                mLastTrajectory_t, mLastTrajectory_y, mLastTrajectory_z);

        float yFactor = getMeterYardFactor();
        float zFactor = getMeterFeetFactor();

        if (!(zmax_index <= 0 || zmax_index >= 200)) {
            for (int i = 0; i < 200; i++) {
                mLastTrajectory_y[i] *= yFactor;
                mLastTrajectory_z[i] *= zFactor;
//                Log.d("hjh", "i : " + i + ", y : " + (float) mLastTrajectory_y[i] + ", z : " + (float) mLastTrajectory_z[i]);
                entryList.add(new Entry((float) mLastTrajectory_y[i], (float) mLastTrajectory_z[i]));
                if (mdMaxHeight < mLastTrajectory_z[i]) mdMaxHeight = mLastTrajectory_z[i];
            }
        }
        return entryList;
    }

    public Float getMeterYardFactor() {
        return UNIT_DISTANCE[Utils.Companion.getSettingUnit()].equals("yd") ? 1.09361f : 1.0f;
    }

    public float getMeterFeetFactor() {
        return UNIT_APEX[Utils.Companion.getSettingUnit()].equals("ft") ? 3.28084f : 1.0f;
    }

    @Override
    public void onAnimationStart() {
        super.onAnimationStart();
    }

    @Override
    public void onAnimationEnd() {
        super.onAnimationEnd();
        //  TTS
//        GlobalViewModel.getInstance().getPlaySoundPoolSfxAction().postValue(MainActivity.SoundType.RECEIVE_READY.ordinal());
        drawChart(false);
    }
}
