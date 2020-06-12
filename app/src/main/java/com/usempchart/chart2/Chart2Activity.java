package com.usempchart.chart2;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.usempchart.R;
import com.usempchart.ValueBean;
import com.usempchart.chart.XAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by JFZ
 * date: 2020-05-13 9:48
 **/
public class Chart2Activity extends AppCompatActivity {
    private LineChart lineChart;
    private XAxis xAxis;
    private YAxis leftYAxis, rightYaxis;
    private List<Entry> entries;
    private LineDataSet lineDataSet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        lineChart = findViewById(R.id.chart);
        initChartConfig();
    }

    public void initChartData(View v) {
        this.entries = new ArrayList();
        final List<ValueBean> data = getValues();
        for (int i = 0; i < data.size(); i++) {
            if (i == 3 || i == 6 || i == 7 || i == 10 || i == data.size() - 1) {
                this.entries.add(new Entry(i, data.get(i).yVal, false));
            } else
                this.entries.add(new Entry(i, data.get(i).yVal, true));
        }
        lineDataSet = new LineDataSet(this.entries, "");
        lineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getPointLabel(Entry entry) {
                if (entry.getData() != null && entry.getData() instanceof Boolean) {
                    boolean bool = (boolean) entry.getData();
                    if (!bool) return "";
                }
                return super.getPointLabel(entry);
            }
        });
        lineDataSet.setColor(ContextCompat.getColor(Chart2Activity.this, R.color.colorPrimary));
        lineDataSet.setLineWidth(2.0F);
        lineDataSet.setFormLineWidth(2.0F);
        lineDataSet.setFormSize(15.0F);
        lineDataSet.setValueTextSize(10F);
        lineDataSet.setValueTextColor(ContextCompat.getColor(Chart2Activity.this, R.color.colorPrimary));
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleRadius(4f);
        lineDataSet.setCircleColor(Color.parseColor("#008577"));
        lineDataSet.setCircleHoleRadius(3f);
        lineDataSet.setCircleHoleColor(Color.WHITE);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setHighlightLineWidth(1f);
        lineDataSet.setHighLightColor(Color.TRANSPARENT);
        lineDataSet.setDrawFilled(true);

        lineDataSet.enableDashedLine(10f, 5f, 0f);
        lineDataSet.setLineWidth(1f);

        lineDataSet.setFillDrawable(ContextCompat.getDrawable(this, R.drawable.bg_linechart));

        LineData lineData = new LineData(lineDataSet);
        xAxis.setValueFormatter(new XAxisValueFormatter(data));
//        leftYAxis.setValueFormatter(new YAxisValueFormatter(data));
        //设置一页最大显示个数为6，超出部分就滑动
        float ratio = (float) data.size() / (float) 6;
        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
        lineChart.zoom(ratio, 1f, 0, 0);
        lineChart.setDragDecelerationEnabled(false);
        lineChart.setData(lineData);
    }

    //配置x坐标 y坐标
    private void initChartConfig() {
        lineChart.setRenderer(new CutLineChartRenderer(lineChart, lineChart.getAnimator(), lineChart.getViewPortHandler()));
        lineChart.setExtraBottomOffset(20f);
        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawGridBackground(false);
        lineChart.animateY(500);
        lineChart.animateX(600);
        lineChart.setDrawBorders(false);
        //是否缩放
        lineChart.setScaleEnabled(false);
        //是否拖动
        lineChart.setDragEnabled(true);
        //是否触摸
        lineChart.setTouchEnabled(true);
        lineChart.setPinchZoom(true);
        //设置背景色
        lineChart.setBackgroundColor(Color.WHITE);
        //没有数据显示的文本
        lineChart.setNoDataText("暂无数据");
        //无数据的文本字体颜色
        lineChart.setNoDataTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        //横坐标
        xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//绘制位置
        xAxis.setAxisMinimum(0.0F);//最小值
        xAxis.setTextColor(Color.parseColor("#333333"));//x坐标文字颜色
        xAxis.setGridColor(Color.TRANSPARENT);//x网格颜色
        xAxis.setGranularity(1f);
        xAxis.enableGridDashedLine(10f, 10f, 0f);

        //纵坐标
        //右边纵坐标隐藏
        rightYaxis = lineChart.getAxisRight();
        rightYaxis.setAxisMinimum(0.0F);
        rightYaxis.setEnabled(false);
        //左边纵坐标
        leftYAxis = lineChart.getAxisLeft();
        leftYAxis.setTextColor(Color.parseColor("#333333"));//y坐标文字颜色
        leftYAxis.setGridColor(Color.parseColor("#8DCDC9"));//y网格颜色
        leftYAxis.setAxisMinimum(0.0F);
        leftYAxis.setLabelCount(6, false);//纵轴上标签显示的数量
        leftYAxis.setDrawZeroLine(true);//绘制0线
        leftYAxis.setZeroLineColor(Color.parseColor("#8DCDC9"));//0线颜色
        leftYAxis.setAxisLineColor(Color.parseColor("#8DCDC9"));//纵轴线颜色
        leftYAxis.enableGridDashedLine(10f, 10f, 0f);
        //隐藏图例
        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(12.0F);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setEnabled(false);
        legend.setDrawInside(false);
    }


    private List<ValueBean> getValues() {
        List<ValueBean> data = new ArrayList<>();
        data.add(new ValueBean("1", 0f));
        data.add(new ValueBean("2", 6f));
        data.add(new ValueBean("3", 14.6f));
        data.add(new ValueBean("4", 15f));
        data.add(new ValueBean("5", 15.5f));
        data.add(new ValueBean("6", 13f));
        data.add(new ValueBean("7", 19f));
        data.add(new ValueBean("8", 16f));
        data.add(new ValueBean("9", 17.5f));
        data.add(new ValueBean("10", 15.8f));
        data.add(new ValueBean("11", 14f));
        data.add(new ValueBean("12", 19f));
        data.add(new ValueBean("13", 15f));
        data.add(new ValueBean("14", 11f));
        data.add(new ValueBean("15", 0f));
        data.add(new ValueBean("16", 3f));
        data.add(new ValueBean("17", 13.4f));
        data.add(new ValueBean("18", 15f));
        data.add(new ValueBean("19", 14f));
        data.add(new ValueBean("20", 30f));
        return data;
    }
}
