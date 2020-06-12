package com.usempchart.chart;

import com.github.mikephil.charting.formatter.ValueFormatter;
import com.usempchart.ValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by JFZ
 * date: 2019-12-27 14:16
 **/
public class XAxisValueFormatter extends ValueFormatter {
    List<ValueBean> data;

    public XAxisValueFormatter(List<ValueBean> data) {
        if (data == null) data = new ArrayList<>();
        this.data = data;
    }

    @Override
    public String getFormattedValue(float value) {
        if (data.size() > 0){
            return data.get((int) (value % data.size())).xVal;
        }
        else return "";
    }
}
