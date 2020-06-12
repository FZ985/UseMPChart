package com.usempchart.kchart.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.YAxis;
import com.usempchart.kchart.render.KInBoundXAxisRenderer;

/**
 * Create by JFZ
 * date: 2020-05-28 15:07
 **/
public class KCombinedChart extends CombinedChart {
    public KCombinedChart(Context context) {
        super(context);
    }

    public KCombinedChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KCombinedChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        mViewPortHandler = new KViewPortHandler() {
            @Override
            public void restrainViewPort(float offsetLeft, float offsetTop, float offsetRight, float offsetBottom) {
                super.restrainViewPort(0, 0, 1, offsetBottom);
            }
        };
        super.init();
        mXAxisRenderer = new KInBoundXAxisRenderer(this.getViewPortHandler(), this.getXAxis(), this.getTransformer(YAxis.AxisDependency.LEFT), 10);
    }
}
