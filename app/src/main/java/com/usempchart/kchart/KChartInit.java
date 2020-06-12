package com.usempchart.kchart;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.usempchart.BaseApp;
import com.usempchart.Tools;
import com.usempchart.kchart.render.HighlightCombinedRenderer;

/**
 * Create by JFZ
 * date: 2020-05-28 13:29
 **/
public class KChartInit {

    //初始化CombinedChart
    public static void initKCombinedChart(CombinedChart chart) {
        chart.getLegend().setEnabled(false);//隐藏图例
        chart.setNoDataText("No Data");//空数据描述
        chart.setDrawGridBackground(false);//绘制网格背景
        chart.setHighlightFullBarEnabled(false);//
        chart.getDescription().setEnabled(false);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.setDragEnabled(true);
        chart.setDragDecelerationEnabled(false);//不允许甩动惯性滑动  和moveView方法有冲突 设置为false
        chart.setDoubleTapToZoomEnabled(false);
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE});
        chart.getViewPortHandler().setMaximumScaleY(3f);//设置y轴最大缩放限制
        chart.getViewPortHandler().setMaximumScaleX(20f);//设置x轴最大缩放限制
        //自定义渲染器 重绘高亮
        chart.setRenderer(new HighlightCombinedRenderer(chart, chart.getAnimator(), chart.getViewPortHandler(), Tools.dip2px(BaseApp.getInstance(), 8)));

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        chart.getXAxis().setAxisMinimum(0f);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setDrawGridLines(true);
        chart.getXAxis().setTextColor(Color.WHITE);
        chart.getXAxis().setTextSize(8f);

        chart.getAxisLeft().setAxisMinimum(0f); // this replaces setStartAtZero(true)
        chart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisLeft().enableGridDashedLine(5, 4, 0);//横向网格线设置为虚线
        chart.getAxisLeft().setYOffset(-5f);
        chart.getAxisLeft().setTextSize(8f);
        chart.getAxisLeft().setTextColor(Color.WHITE);
        chart.getAxisLeft().setDrawAxisLine(true);

        chart.getAxisRight().setEnabled(true);//隐藏右边y轴
        chart.getAxisRight().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        chart.getAxisRight().setDrawAxisLine(true);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setTextColor(Color.TRANSPARENT);
    }

    //初始化k 柱状图
    public static void initKBarChart(BarChart chart) {
        chart.getLegend().setEnabled(false);//隐藏图例
//        chart.getAxisRight().setEnabled(false);//隐藏右边y轴
        chart.setNoDataText("No Data");//空数据描述
        chart.setDrawGridBackground(false);//绘制网格背景
        chart.setHighlightFullBarEnabled(false);//
        chart.getDescription().setEnabled(false);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.setDragEnabled(true);
        chart.setDragDecelerationEnabled(false);//不允许甩动惯性滑动  和moveView方法有冲突 设置为false
        chart.setDoubleTapToZoomEnabled(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.setScaleYEnabled(true);
        chart.setMinOffset(0);

        chart.getViewPortHandler().setMaximumScaleY(3f);//设置y轴最大缩放限制
        chart.getViewPortHandler().setMaximumScaleX(20f);//设置x轴最大缩放限制

        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        chart.getAxisLeft().setTextSize(8f);
        chart.getAxisLeft().setTextColor(Color.WHITE);
        chart.getAxisLeft().setAxisMinimum(3000000f);
        chart.getAxisLeft().setDrawAxisLine(true);
//        chart.getAxisLeft().enableGridDashedLine(5, 4, 0);//横向网格线设置为虚线

        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        chart.getXAxis().setTextColor(Color.TRANSPARENT);
        chart.getXAxis().setDrawAxisLine(true);
        chart.getXAxis().setEnabled(true);

        chart.getAxisRight().setEnabled(true);//隐藏右边y轴
        chart.getAxisRight().setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        chart.getAxisRight().setDrawAxisLine(true);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setTextColor(Color.TRANSPARENT);
    }
}
