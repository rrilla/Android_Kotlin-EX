package com.example.testchart.data;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class MyYAxisValueFormatter implements IAxisValueFormatter {
    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        float[] axisEntries = axis.mEntries;
        String result = (int)value + "";
        if (value == axisEntries[0]) {
            result = "";
        }
        return result;
    }
}
