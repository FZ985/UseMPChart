package com.usempchart.kchart;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.usempchart.R;
import com.usempchart.kchart.ceshi.Model;
import com.usempchart.kchart.ceshi.StockListBean;
import com.usempchart.kchart.listener.KChartFingerTouchListener;
import com.usempchart.kchart.listener.KCoupleChartValueSelectedListener;
import com.usempchart.kchart.render.KCoupleChartGestureListener;
import com.usempchart.kchart.widget.KBarChart;
import com.usempchart.kchart.widget.KCombinedChart;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Create by JFZ
 * date: 2020-05-28 10:11
 **/
public class KChartActivity extends AppCompatActivity implements KCoupleChartGestureListener.OnEdgeListener, KChartFingerTouchListener.HighlightListener, KCoupleChartValueSelectedListener.ValueSelectedListener {
    private TextView tv;

    private KCombinedChart chart;
    private YAxis yAxis;
    private XAxis xAxis;
    private CandleDataSet candleDataSet;
    private LineDataSet ma5LineSet, ma10LineSet, ma20LineSet;

    private KBarChart barchart;
    private BarDataSet barDataSet;

    private KCoupleChartGestureListener chartGes, barchartGes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kchart);
        setTitle("KChart");
        tv = findViewById(R.id.info);
        chart = findViewById(R.id.chart);
        barchart = findViewById(R.id.barchart);
        initChart();
    }

    private LineData lineData;
    private CandleData candleData;
    private int itemcount;

    public void onBtn(View view) {
        CombinedData data = new CombinedData();
        List<CandleEntry> candleEntriesOld = Model.getCandleEntries();
        List<CandleEntry> candleEntries = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            candleEntries.add(candleEntriesOld.get(i));
        }

        itemcount = candleEntries.size();

        List<String> xVals = new ArrayList<>();
        List<StockListBean.StockBean> stockBeans = Model.getData();

        for (int i = 0; i < itemcount; i++) {
            xVals.add(stockBeans.get(i).getDate());
        }
        candleDataSet = new CandleDataSet(candleEntries, "");
        candleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        candleDataSet.setShadowWidth(0.7f);
        candleDataSet.setDecreasingColor(Color.RED);
        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setIncreasingColor(Color.GREEN);
        candleDataSet.setIncreasingPaintStyle(Paint.Style.STROKE);
        candleDataSet.setNeutralColor(Color.RED);
        candleDataSet.setShadowColorSameAsCandle(true);
        candleDataSet.setHighlightLineWidth(0.5f);
        candleDataSet.setHighLightColor(Color.parseColor("#ff9900"));
        candleDataSet.setHighlightEnabled(false);
        candleDataSet.setDrawValues(true);
        candleDataSet.setValueTextSize(10);
        candleDataSet.setValueTextColor(Color.parseColor("#fc9501"));
//        candleDataSet.setBarSpace(0.2f);
        candleData = new CandleData(candleDataSet);
        data.setData(candleData);

        xAxis.setAxisMaximum(data.getXMax() + 1f);//
        xAxis.setAxisMinimum(data.getXMin() - 1f);//
        //设置一页最大显示个数为6，超出部分就滑动
        float ratio = (float) candleEntries.size() / (float) 20;
        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
        chart.zoom(ratio, 1f, 0, 0);

        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index >= xVals.size() || index < 0) {
                    return "";
                }
                return xVals.get(index);
            }
        });

        /*ma5*/
        ArrayList<Entry> ma5Entries = new ArrayList<Entry>();
        for (int index = 0; index < itemcount; index++) {
            ma5Entries.add(new Entry(index, stockBeans.get(index).getMa5(), xVals.get(index)));
        }

        /*ma10*/
        ArrayList<Entry> ma10Entries = new ArrayList<Entry>();
        for (int index = 0; index < itemcount; index++) {
            ma10Entries.add(new Entry(index, stockBeans.get(index).getMa10(), xVals.get(index)));
        }

        /*ma20*/
        ArrayList<Entry> ma20Entries = new ArrayList<Entry>();
        for (int index = 0; index < itemcount; index++) {
            ma20Entries.add(new Entry(index, stockBeans.get(index).getMa20(), xVals.get(index)));
        }

        ma5LineSet = generateLineDataSet(ma5Entries, Color.parseColor("#00ffff"), "ma5");
        ma10LineSet = generateLineDataSet(ma10Entries, Color.YELLOW, "ma10");
        ma20LineSet = generateLineDataSet(ma20Entries, Color.parseColor("#ff99ff"), "ma20");

        lineData = generateMultiLineData(ma5LineSet, ma10LineSet, ma20LineSet);

        data.setData(lineData);
        chart.setData(data);
        chart.moveViewToAnimated(candleEntries.get(candleEntries.size() - 1).getX(), candleEntries.get(candleEntries.size() - 1).getY(), YAxis.AxisDependency.LEFT, 200);
        chart.notifyDataSetChanged();
        chart.invalidate();


        //柱状图
        List<BarEntry> barEntries = new ArrayList<>();

        for (int i = 0; i < itemcount; i++) {
            barEntries.add(new BarEntry(i, Float.parseFloat(stockBeans.get(i).getVolume()), candleEntries.get(i).getClose() >= candleEntries.get(i).getOpen() ? 1 : 0));
        }

        barDataSet = new BarDataSet(barEntries, "");
        barDataSet.setDrawIcons(false);
        barDataSet.setDrawValues(false);
        barDataSet.setHighlightEnabled(false);
        barDataSet.setHighLightColor(Color.parseColor("#ff9900"));
        barDataSet.setColors(Color.RED, Color.GREEN);
        BarData barData = new BarData(barDataSet);
        barData.setValueTextSize(10f);
//        barData.setBarWidth(1 - candleDataSet.getBarSpace() * 2);

        //显示的时候是按照多大的比率缩放显示,1f表示不放大缩小
//        barchart.zoom(ratio, 1f, 0, 0);
        barchart.moveViewToAnimated(barEntries.get(barEntries.size() - 1).getX(), barEntries.get(barEntries.size() - 1).getY(), YAxis.AxisDependency.LEFT, 200);
        barchart.getXAxis().setAxisMaximum(barData.getXMax() + 1f);//
        barchart.getXAxis().setAxisMinimum(barData.getXMin() - 1f);//
        barchart.setData(barData);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initChart() {
        KChartInit.initKCombinedChart(chart);
        KChartInit.initKBarChart(barchart);
        //x轴
        xAxis = chart.getXAxis();
        //y轴
        yAxis = chart.getAxisLeft();


//        barchart.getAxisLeft().setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                return "";
//            }
//        });

        chartGes = new KCoupleChartGestureListener(this, chart, barchart);
        chart.setOnChartGestureListener(chartGes);//设置手势联动监听
        barchartGes = new KCoupleChartGestureListener(this, barchart, chart);
        barchart.setOnChartGestureListener(barchartGes);

        //设置高亮联动
        chart.setOnChartValueSelectedListener(new KCoupleChartValueSelectedListener(this, chart, barchart));
        barchart.setOnChartValueSelectedListener(new KCoupleChartValueSelectedListener(this, barchart, chart));
        //手指长按滑动高亮
        chart.setOnTouchListener(new KChartFingerTouchListener(chart, this));
        barchart.setOnTouchListener(new KChartFingerTouchListener(barchart, this));
    }

    private LineDataSet generateLineDataSet(List<Entry> entries, int color, String label) {
        LineDataSet set = new LineDataSet(entries, label);
        set.setColor(color);
        set.setLineWidth(1f);
        set.setDrawCircles(false);
        set.setDrawCircleHole(false);
        set.setDrawValues(false);
        set.setHighlightEnabled(false);
        set.setHighLightColor(Color.parseColor("#ff9900"));
//        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        return set;
    }

    private LineData generateMultiLineData(LineDataSet... lineDataSets) {
        List<ILineDataSet> dataSets = new ArrayList<>();
        for (int i = 0; i < lineDataSets.length; i++) {
            dataSets.add(lineDataSets[i]);
        }
        LineData data = new LineData(dataSets);
        return data;
    }

    @Override
    public void enableHighlight() {
        if (!barDataSet.isHighlightEnabled()) {
            candleDataSet.setHighlightEnabled(true);
            ma5LineSet.setHighlightEnabled(true);
            ma10LineSet.setHighlightEnabled(true);
            ma20LineSet.setHighlightEnabled(true);
            barDataSet.setHighlightEnabled(true);
        }
    }

    @Override
    public void disableHighlight() {
        if (barDataSet.isHighlightEnabled()) {
            candleDataSet.setHighlightEnabled(false);
            ma5LineSet.setHighlightEnabled(false);
            ma10LineSet.setHighlightEnabled(false);
            ma20LineSet.setHighlightEnabled(false);
            barDataSet.setHighlightEnabled(false);
            if (chartGes != null) {
                chartGes.setHighlight(true);
            }
            if (barchartGes != null) {
                barchartGes.setHighlight(true);
            }
        }
    }

    @Override
    public void valueSelected(Entry e, Highlight h) {
        StringBuilder builder = new StringBuilder();
        maData(builder, e);
        tv.setText(builder.toString());
    }

    private void maData(StringBuilder builder, Entry e) {
        if (e.getData() != null) {
            int x = (int) e.getX();
            if (x > 0 && x < chart.getData().getEntryCount()) {
                CandleEntry candleEntry = candleDataSet.getEntries().get(x);
                float change = (candleEntry.getClose() - candleEntry.getOpen()) / candleEntry.getOpen();
                NumberFormat nf = NumberFormat.getPercentInstance();
                nf.setMaximumFractionDigits(2);
                String changePercentage = nf.format(Double.valueOf(String.valueOf(change)));
                builder.append("onValueSelected_high:" + candleEntry.getHigh() + "\nlow:" + candleEntry.getLow() + "\nopen:" + candleEntry.getOpen() + "\nclose:" + candleEntry.getClose() + "\nchangePercentage:" + changePercentage).toString();

                if (ma5LineSet.getEntries().get(x).getData() != null && ma5LineSet.getEntries().get(x).getData() instanceof String) {
                    String ma5 = (String) ma5LineSet.getEntries().get(x).getData();
                    builder.append("\nma5:" + x + ",y" + ma5LineSet.getEntries().get(x).getY());
                }
                if (ma10LineSet.getEntries().get(x).getData() != null && ma10LineSet.getEntries().get(x).getData() instanceof String) {
                    String ma10 = (String) ma10LineSet.getEntries().get(x).getData();
                    builder.append("\nma10:" + x + ",y" + ma10LineSet.getEntries().get(x).getY());
                }
                if (ma20LineSet.getEntries().get(x).getData() != null && ma20LineSet.getEntries().get(x).getData() instanceof String) {
                    String ma20 = (String) ma20LineSet.getEntries().get(x).getData();
                    builder.append("\nma20:" + x + ",y" + ma20LineSet.getEntries().get(x).getY());
                }
                builder.append("\nVolume:" + x + ",y:" + barDataSet.getEntries().get(x).getY());
            }
        }
    }

    @Override
    public void nothingSelected() {
        tv.setText("");
    }

    @Override
    public void edgeLoad(float x, boolean left) {

    }
}
