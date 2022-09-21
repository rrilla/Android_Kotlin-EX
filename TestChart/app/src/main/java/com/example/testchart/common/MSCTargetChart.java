package com.example.testchart.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.testchart.MainActivity;
import com.example.testchart.R;
import com.example.testchart.data.ShotData;
import com.example.testchart.util.SC300LIB;
import com.example.testchart.util.Utils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;


interface OnAnimationListener {
    void onAnimationStart();
    void onAnimationEnd();
}
class ReverseRelativeLayout extends LinearLayout {
    private boolean isReverse = false;
    public void setReverse(boolean reverse) {
        isReverse = reverse;
    }
    public ReverseRelativeLayout(Context context) {
        super(context);
    }

    public ReverseRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReverseRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (isReverse) {
            canvas.scale(-1, 1, canvas.getWidth()/2, canvas.getHeight()/2);
        }
        super.dispatchDraw(canvas);
    }
}

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


    LineChart mLineChart;

    private View mLayoutNoShot;

    private LineDataSet mDataTarget;
    private ArrayList<ILineDataSet> mDataSets = new ArrayList<>();
    private double mdMaxHeight = 0;
    private double mChartMax = 0;

    private double[] mLastTrajectory_t;    //시간
    private double[] mLastTrajectory_y;    //수평거리
    private double[] mLastTrajectory_z;    //높이

    private int[] mListGrayBall;
    private int[] mListRedBall;

    private ShotData mLastShotData;
    private ArrayList<ShotData> mShotDataList;

    public MSCTargetChart(@NonNull Context context) {
        super(context);
    }

    public MSCTargetChart(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MSCTargetChart(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    protected void init(Context context){
//        View view=View.inflate(context, R.layout.msc_target_chart,this);
//
//        mLayoutNoShot = view.findViewById(R.id.layoutNohaveShot);
//
//        ReverseRelativeLayout reverseRelativeLayout = view.findViewById(R.id.layoutTargetReverse);
//        reverseRelativeLayout.setReverse(true);
//
//        mLineChart = view.findViewById(R.id.targetLineChart);
//        initChart();
//
//        mLastTrajectory_t = new double[200];//시간
//        mLastTrajectory_y = new double[200];//수평거리
//        mLastTrajectory_z = new double[200];//높이
//
//        setTargetDistance((int) CaddiePreference.getInstance().getSettingTargetDistance());
//    }

//    protected void initChart(){
//        mLineChart.setMinOffset(20f);
//        mLineChart.setNoDataText("");
//        //mLineChart.setOnDrawListener(this);
//        mLineChart.setOnAnimationListener(this);
//        mLineChart.getDescription().setEnabled(false);
//
//        mLineChart.getAxisLeft().setEnabled(true);
//        mLineChart.getRendererLeftYAxis().setDrawReverse(true);
//        mLineChart.getRendererLeftYAxis().setUnit(App.UNIT_APEX[CaddiePreference.getInstance().getSettingUnit()]);
//        mLineChart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
//        mLineChart.getAxisLeft().setValueFormatter(new MyYAxisValueFormatter());
//        mLineChart.getRendererXAxis().setUnit(App.UNIT_DISTANCE[CaddiePreference.getInstance().getSettingUnit()]);
//        mLineChart.getAxisRight().setEnabled(false);
//        mLineChart.getLegend().setEnabled(false);
//        mLineChart.setTouchEnabled(false);
//
//        List<Entry> entries = new ArrayList<>();
//        entries.add(new Entry(0,  0));
//        LineDataSet lineDataSet = new LineDataSet(entries, "");
//        lineDataSet.setDrawCircles(false);
//        mLineChart.setData(new LineData(lineDataSet));
//        mLineChart.getAxisLeft().setAxisMinimum(0);
//        int clubCode = GlobalViewModel.getClubCode(CaddiePreference.getInstance().getSettingTargetClubId(), CaddiePreference.DEFAULT_TARGET_CLUB_CODE);
//        int nMaxApex = ClubData.getClubApex(clubCode, CaddiePreference.getInstance().getSettingUnit());
//        mLineChart.getAxisLeft().setAxisMaximum(nMaxApex);
//        float maxDistance = (float)CaddiePreference.getInstance().getSettingTargetDistance()*1.3f;
//        mLineChart.getXAxis().setAxisMaximum(maxDistance);
//        mLineChart.getXAxis().setDrawGridLines(false);
//        mLineChart.invalidate();
//
//        mListGrayBall = new int[10];
//        mListGrayBall[0] = R.drawable.gray_01;
//        mListGrayBall[1] = R.drawable.gray_02;
//        mListGrayBall[2] = R.drawable.gray_03;
//        mListGrayBall[3] = R.drawable.gray_04;
//        mListGrayBall[4] = R.drawable.gray_05;
//        mListGrayBall[5] = R.drawable.gray_06;
//        mListGrayBall[6] = R.drawable.gray_07;
//        mListGrayBall[7] = R.drawable.gray_08;
//        mListGrayBall[8] = R.drawable.gray_09;
//        mListGrayBall[9] = R.drawable.gray_10;
//
//        mListRedBall = new int[10];
//        mListRedBall[0] = R.drawable.red_01;
//        mListRedBall[1] = R.drawable.red_02;
//        mListRedBall[2] = R.drawable.red_03;
//        mListRedBall[3] = R.drawable.red_04;
//        mListRedBall[4] = R.drawable.red_05;
//        mListRedBall[5] = R.drawable.red_06;
//        mListRedBall[6] = R.drawable.red_07;
//        mListRedBall[7] = R.drawable.red_08;
//        mListRedBall[8] = R.drawable.red_09;
//        mListRedBall[9] = R.drawable.red_10;
//    }

    public void drawChart(ShotData lastShotData, ArrayList<ShotData> dataSets, boolean animation){
        mLastShotData = lastShotData;
        mShotDataList = dataSets;
        drawChart(animation);
    }

    public void drawChart(boolean animation) {
        setShotList(animation);

        if(mDataSets.size() == 0)mLayoutNoShot.setVisibility(VISIBLE);
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
            entries.add(new Entry((float) 1,  0));
            LineDataSet lineDataSet = new LineDataSet(entries, "");
            lineDataSet.setDrawCircles(false);
            lineDataSet.setDrawValues(false);
            lineData.addDataSet(lineDataSet);
        }
        mLineChart.setData(lineData);

        ///////////////////////////////////
        if (mLineChart.getXChartMax() > mChartMax) mChartMax = mLineChart.getXChartMax();
        int clubcode = DEFAULT_TARGET_CLUB_CODE;
        if (mChartMax < CLUB_CARRY[clubcode]) {
            mChartMax = CLUB_CARRY[clubcode];
        }


        if (mdMaxHeight > mLineChart.getAxisLeft().getAxisMaximum()) {
            mLineChart.getAxisLeft().setAxisMaximum((float) (mdMaxHeight));
        }

        if (mLastShotData != null && mLastShotData.getTotal() > mLineChart.getXAxis().getAxisMaximum()){
            mLineChart.getXAxis().setAxisMaximum(mLastShotData.getTotal()*1.3f);
        }

        mLineChart.getAxisLeft().setLabelCount(4, true);
        mLineChart.getAxisLeft().setTextColor(getResources().getColor(R.color.main_graph_axis_y));

        mLineChart.setDoubleTapToZoomEnabled(false);
        mLineChart.setDragEnabled(false);
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.getXAxis().setLabelRotationAngle(-180);
        mLineChart.getXAxis().setTextColor(getResources().getColor(R.color.main_graph_axis_y));

        if (animation) mLineChart.animateX(2000);

        //if (!mbEnable) return;
        ///////////////////////////////////

        mLineChart.postInvalidate();
    }

    public void setUnit(Short unit){
        //  단위 텍스트 설정하는 곳 같음. lib버전 때문인가 해당함수 못써서 일단 주석
//        mLineChart.getRendererLeftYAxis().setUnit("dd");
//        mLineChart.getRendererXAxis().setUnit(App.UNIT_DISTANCE[unit]);

        int clubCode = DEFAULT_TARGET_CLUB_CODE;
        int nMaxApex = Utils.Companion.getClubApex(clubCode, Utils.Companion.getSettingUnit());
        mLineChart.getAxisLeft().setAxisMaximum(nMaxApex);
        float maxDistance = (float)DEFAULT_TARGET_DISTANCE*1.3f;
        if(maxDistance > mLineChart.getXAxis().getAxisMaximum()) {
            mLineChart.getXAxis().setAxisMaximum(maxDistance);
        }
        mLineChart.invalidate();

        drawChart(false);
    }

    // 타겟 거리 설정
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
        mLineChart.getAxisLeft().setAxisMaximum(nMaxApex);
        float maxDistance = (float)integer*1.3f;
        mLineChart.getXAxis().setAxisMaximum(maxDistance);
        mLineChart.setVisibleXRangeMaximum(maxDistance);
        mLineChart.invalidate();

        drawChart(false);
    }


    public float px2dp(float px){
        float density = getContext().getResources().getDisplayMetrics().density;
        if(density==1.0) density *=4.0;
        else if(density==1.5) density *=(8/3);
        else if(density==2.0) density *=2.0;

        return px/density;
    }

    protected void setShotList(boolean animation){
        mDataSets.clear();

        int color = R.color.chart_carry_max;
        if(mLastShotData != null) {
            LineDataSet lds = createLineDataSet(getTrajectoryList(mLastShotData), "LAST", color);
            mDataSets.add(lds);
        }

        if(mShotDataList == null) mShotDataList = new ArrayList<>();

        //  샷 데이터리스트가 10보다 작을 때
        for (int i = 0; i < mShotDataList.size() && i < 10; i++) {
            if(animation && i == mShotDataList.size()-1) break;
            ShotData data = mShotDataList.get(i);

            ArrayList<Entry> entries = new ArrayList<>();

            String unit = UNIT_DISTANCE[Utils.Companion.getSettingUnit()];
            float distance = Utils.Companion.valueForUnit(data.getCarry(), unit);

            //  마지막 샷이면
            if(i == mShotDataList.size()-1){
                entries.add(new Entry(distance, 0, getResources().getDrawable(mListRedBall[i])));
            } else {
                entries.add(new Entry(distance, 0, getResources().getDrawable(mListGrayBall[i])));
            }

            LineDataSet lds = new LineDataSet(entries, String.format("C%d", i));
            lds.setLineWidth(0);
            lds.setDrawIcons(true);
            lds.setDrawCircles(true);
            lds.setDrawValues(false);
            mDataSets.add(lds);
        }
    }

    private LineDataSet createLineDataSet(List<Entry> list, String label, int id) {
        LineDataSet dataSet = new LineDataSet(list, label);
        dataSet.setLineWidth(3.5f);
        dataSet.setDrawCircles(false);
        dataSet.setDrawValues(false);
        dataSet.setDrawIcons(false);
        dataSet.setColor(getResources().getColor(id));
        //dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        return dataSet;
    }

    private List<Entry> getTrajectoryList(ShotData shotData) {

        List<Entry> entryList = new ArrayList<>();
        SC300LIB sc300lib = new SC300LIB();

        int zmax_index = sc300lib.sc300_get_trajectory(shotData.getBall_speed(),
                (shotData.getLaunch_angle() > 100 ? shotData.getLaunch_angle() - 100 : shotData.getLaunch_angle()),
                (int)shotData.getSpin_rate(), (int)shotData.getTemperature(), (int)shotData.getAir_pressure(),
                shotData.getCarry(), (shotData.getApex() > 100 ? shotData.getApex() - 100 : shotData.getApex()),
                mLastTrajectory_t, mLastTrajectory_y, mLastTrajectory_z);

        float yFactor = getMeterYardFactor();
        float zFactor = getMeterFeetFactor();

        if (!(zmax_index <=0 || zmax_index >= 200)) {
            for (int i = 0; i < 200; i++) {
                mLastTrajectory_y[i] *= yFactor;
                mLastTrajectory_z[i] *= zFactor;
                //Log.d(TAG, "i : " + i + ", y : " + (float) mLastTrajectory_y[i] + ", z : " + (float) mLastTrajectory_z[i]);
                entryList.add(new Entry((float) mLastTrajectory_y[i], (float) mLastTrajectory_z[i]));
                if (mdMaxHeight < mLastTrajectory_z[i]) mdMaxHeight = mLastTrajectory_z[i];
            }
        }
        return entryList;
    }
    public Float getMeterYardFactor() {
        if (UNIT_DISTANCE[Utils.Companion.getSettingUnit()] == "yd")
            return 1.09361f;
        else
            return 1.0f;
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
//        GlobalViewModel.getInstance().getPlaySoundPoolSfxAction().postValue(MainActivity.SoundType.RECEIVE_READY.ordinal());
        drawChart(false);
    }

}
