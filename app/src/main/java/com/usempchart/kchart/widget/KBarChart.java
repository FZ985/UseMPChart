package com.usempchart.kchart.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.BarChart;
import com.usempchart.BaseApp;
import com.usempchart.Tools;
import com.usempchart.kchart.render.KOffsetBarRenderer;

/**
 * Create by JFZ
 * date: 2020-05-29 14:06
 **/
public class KBarChart extends BarChart {
    public KBarChart(Context context) {
        super(context);
    }

    public KBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        mViewPortHandler = new KViewPortHandler() {
            @Override
            public void restrainViewPort(float offsetLeft, float offsetTop, float offsetRight, float offsetBottom) {
                super.restrainViewPort(0, 0, 1f, offsetBottom);
            }
        };
        super.init();
//        设置渲染器控制颜色、偏移，以及高亮
        mRenderer = new KOffsetBarRenderer(this, mAnimator, mViewPortHandler, 0)
                .setHighlightWidthSize(0.5f, Tools.dip2px(BaseApp.getInstance(), 8));
    }

}
