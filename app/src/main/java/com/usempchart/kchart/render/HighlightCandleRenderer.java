package com.usempchart.kchart.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.CandleDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.renderer.CandleStickChartRenderer;
import com.github.mikephil.charting.renderer.DataRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.usempchart.kchart.NumberUtils;

import java.text.DecimalFormat;
import java.util.List;

/**
 * 自定义CandleStickChart渲染器 绘制高亮  -- 绘制方式和自定义LineChart渲染器相同
 * 使用方法: 1.先设置渲染器 {@link CombinedChart#setRenderer(DataRenderer)}
 * 传入自定义渲染器 将其中Candle图的渲染器替换成此渲染器
 * 2.设置数据时 调用 {@link CandleEntry#CandleEntry(float, float, float, float, float, Object)}
 * 传入String类型的data 以绘制x的值  -- 如未设置 则只绘制竖线
 */
public class HighlightCandleRenderer extends CandleStickChartRenderer {

    private float highlightSize;//图表高亮文字大小 单位:px
    private DecimalFormat format = new DecimalFormat("0.0000");
    private Highlight[] indices;
    //小数位精确位数
    private int precision = 3;

    public HighlightCandleRenderer(CandleDataProvider chart, ChartAnimator animator,
                                   ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    public HighlightCandleRenderer setHighlightSize(float textSize) {
        highlightSize = textSize;
        return this;
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {
        this.indices = indices;
    }

    protected float getYPixelForValues(float x, float y) {
        MPPointD pixels = mChart.getTransformer(YAxis.AxisDependency.LEFT).getPixelForValues(x, y);
        return (float) pixels.y;
    }

    @Override
    public void drawExtras(Canvas c) {
        if (indices == null) {
            return;
        }

        CandleData candleData = mChart.getCandleData();
        for (Highlight high : indices) {
            ICandleDataSet set = candleData.getDataSetByIndex(high.getDataSetIndex());
            if (set == null || !set.isHighlightEnabled())
                continue;

            CandleEntry e = set.getEntryForXValue(high.getX(), high.getY());
            if (!isInBoundsX(e, set))
                continue;

            float lowValue = e.getLow() * mAnimator.getPhaseY();
            float highValue = e.getHigh() * mAnimator.getPhaseY();
            MPPointD pix = mChart.getTransformer(set.getAxisDependency())
                    .getPixelForValues(e.getX(), (lowValue + highValue) / 2f);
            float xp = (float) pix.x;

            mHighlightPaint.setColor(set.getHighLightColor());
            mHighlightPaint.setStrokeWidth(set.getHighlightLineWidth());
            mHighlightPaint.setTextSize(highlightSize);

            float xMin = mViewPortHandler.contentLeft();
            float xMax = mViewPortHandler.contentRight();
            float contentBottom = mViewPortHandler.contentBottom();
            //画竖线
            int halfPaddingVer = 5;//竖向半个padding
            int halfPaddingHor = 8;
            float textXHeight = 0;

            String textX;//高亮点的X显示文字
            Object data = e.getData();
            if (data != null && data instanceof String) {
                textX = (String) data;
            } else {
                textX = e.getX() + "";
            }
            if (!TextUtils.isEmpty(textX)) {//绘制x的值
                //先绘制文字框
                mHighlightPaint.setStyle(Paint.Style.STROKE);
                int width = Utils.calcTextWidth(mHighlightPaint, textX);
                int height = Utils.calcTextHeight(mHighlightPaint, textX);
                float left = Math.max(xMin, xp - width / 2F - halfPaddingVer);//考虑间隙
                float right = left + width + halfPaddingHor * 2;
                if (right > xMax) {
                    right = xMax;
                    left = right - width - halfPaddingHor * 2;
                }
                textXHeight = height + halfPaddingVer * 2;
                RectF rect = new RectF(left, 0, right, textXHeight);
                c.drawRect(rect, mHighlightPaint);
                //再绘制文字
                mHighlightPaint.setStyle(Paint.Style.FILL);
                Paint.FontMetrics metrics = mHighlightPaint.getFontMetrics();
                float baseY = (height + halfPaddingVer * 2 - metrics.top - metrics.bottom) / 2;
                c.drawText(textX, left + halfPaddingHor, baseY, mHighlightPaint);
            }
            //绘制竖线
            c.drawLine(xp, textXHeight, xp, mChart.getHeight(), mHighlightPaint);

            //判断是否画横线
            float y = high.getDrawY();
            float yMaxValue = mChart.getYChartMax();
            float yMinValue = mChart.getYChartMin();
            float yMin = getYPixelForValues(xp, yMaxValue);
            float yMax = getYPixelForValues(xp, yMinValue);
            if (y >= 0 && y <= contentBottom) {//在区域内即绘制横线
                //先绘制文字框
                mHighlightPaint.setStyle(Paint.Style.STROKE);
                float yValue = (yMax - y) / (yMax - yMin) * (yMaxValue - yMinValue) + yMinValue;
                String text = format.format(yValue);
                int width = Utils.calcTextWidth(mHighlightPaint, text);
                int height = Utils.calcTextHeight(mHighlightPaint, text);
                float top = Math.max(0, y - height / 2F - halfPaddingVer);//考虑间隙
                float bottom = top + height + halfPaddingVer * 2;
                if (bottom > contentBottom) {
                    bottom = contentBottom;
                    top = bottom - height - halfPaddingVer * 2;
                }
//                RectF rect = new RectF(xMax - width - halfPaddingHor * 2, top, xMax, bottom);
                RectF rect = new RectF(0, top, xMax - (xMax - width - halfPaddingHor * 2), bottom);
                c.drawRect(rect, mHighlightPaint);
                //再绘制文字
                mHighlightPaint.setStyle(Paint.Style.FILL);
                Paint.FontMetrics metrics = mHighlightPaint.getFontMetrics();
                float baseY = (top + bottom - metrics.top - metrics.bottom) / 2;
//                c.drawText(text, xMax - width - halfPaddingHor, baseY, mHighlightPaint);
                c.drawText(text, halfPaddingHor, baseY, mHighlightPaint);
                //绘制横线
//                c.drawLine(0, y, xMax - width - halfPaddingHor * 2, y, mHighlightPaint);
                c.drawLine(xMax - (xMax - width - halfPaddingHor * 2), y, xMax, y, mHighlightPaint);
            }
        }
        indices = null;
    }

    @Override
    public void drawValues(Canvas c) {
        List<ICandleDataSet> dataSets = mChart.getCandleData().getDataSets();
//        mValuePaint.setColor(Color.parseColor("#406ebc"));
        for (int i = 0; i < dataSets.size(); i++) {

            ICandleDataSet dataSet = dataSets.get(i);

            if (!shouldDrawValues(dataSet) || dataSet.getEntryCount() < 1) {
                continue;
            }

            // apply the text-styling defined by the DataSet
            applyValueTextStyle(dataSet);

            Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

            mXBounds.set(mChart, dataSet);

            float[] positions = trans.generateTransformedValuesCandle(
                    dataSet, mAnimator.getPhaseX(), mAnimator.getPhaseY(), mXBounds.min, mXBounds.max);

            //计算最大值和最小值
            float maxValue = 0, minValue = 0;
            int maxIndex = 0, minIndex = 0;
            CandleEntry maxEntry = null, minEntry = null;
            boolean firstInit = true;
            for (int j = 0; j < positions.length; j += 2) {

                float x = positions[j];
                float y = positions[j + 1];

                if (!mViewPortHandler.isInBoundsRight(x)) {
                    break;
                }

                if (!mViewPortHandler.isInBoundsLeft(x) || !mViewPortHandler.isInBoundsY(y)) {
                    continue;
                }

                CandleEntry entry = dataSet.getEntryForIndex(j / 2 + mXBounds.min);

                if (dataSet.isDrawValuesEnabled()) {
                    mValuePaint.setColor(dataSet.getValueTextColor(j / 2));
                }

                if (firstInit) {
                    maxValue = entry.getHigh();
                    minValue = entry.getLow();
                    firstInit = false;
                    maxEntry = entry;
                    minEntry = entry;
                } else {
                    if (entry.getHigh() > maxValue) {
                        maxValue = entry.getHigh();
                        maxIndex = j;
                        maxEntry = entry;
                    }

                    if (entry.getLow() < minValue) {
                        minValue = entry.getLow();
                        minIndex = j;
                        minEntry = entry;
                    }

                }
            }

            //绘制最大值和最小值
            if (maxIndex > minIndex) {
                //画右边
                String highString = NumberUtils.keepPrecisionR(minValue, precision);

                //计算显示位置
                //计算文本宽度
                int highStringWidth = Utils.calcTextWidth(mValuePaint, "──• " + highString);
                int highStringHeight = Utils.calcTextHeight(mValuePaint, "──• " + highString);

                float[] tPosition = new float[2];
                tPosition[0] = minEntry == null ? 0f : minEntry.getX();
                tPosition[1] = minEntry == null ? 0f : minEntry.getLow();
                trans.pointValuesToPixel(tPosition);
                if (tPosition[0] + highStringWidth / 2 > mViewPortHandler.contentRight()) {
                    c.drawText(highString + " •──", tPosition[0] - highStringWidth / 2, tPosition[1] + highStringHeight / 2, mValuePaint);
                } else {
                    c.drawText("──• " + highString, tPosition[0] + highStringWidth / 2, tPosition[1] + highStringHeight / 2, mValuePaint);
                }
            } else {
                //画左边
                String highString = NumberUtils.keepPrecisionR(minValue, precision);

                //计算显示位置
                int highStringWidth = Utils.calcTextWidth(mValuePaint, highString + " •──");
                int highStringHeight = Utils.calcTextHeight(mValuePaint, highString + " •──");

                float[] tPosition = new float[2];
                tPosition[0] = minEntry == null ? 0f : minEntry.getX();
                tPosition[1] = minEntry == null ? 0f : minEntry.getLow();
                trans.pointValuesToPixel(tPosition);
                if (tPosition[0] - highStringWidth / 2 < mViewPortHandler.contentLeft()) {
                    c.drawText("──• " + highString, tPosition[0] + highStringWidth / 2, tPosition[1] + highStringHeight / 2, mValuePaint);
                } else {
                    c.drawText(highString + " •──", tPosition[0] - highStringWidth / 2, tPosition[1] + highStringHeight / 2, mValuePaint);
                }
            }


            //这里画的是上面两个点
            if (maxIndex > minIndex) {
                //画左边
                String highString = NumberUtils.keepPrecisionR(maxValue, precision);

                int highStringWidth = Utils.calcTextWidth(mValuePaint, highString + " •──");
                int highStringHeight = Utils.calcTextHeight(mValuePaint, highString + " •──");

                float[] tPosition = new float[2];
                tPosition[0] = maxEntry == null ? 0f : maxEntry.getX();
                tPosition[1] = maxEntry == null ? 0f : maxEntry.getHigh();
                trans.pointValuesToPixel(tPosition);
                if ((tPosition[0] - highStringWidth / 2) < mViewPortHandler.contentLeft()) {
                    c.drawText("──• " + highString, tPosition[0] + highStringWidth / 2, tPosition[1] + highStringHeight / 2, mValuePaint);
                } else {
                    c.drawText(highString + " •──", tPosition[0] - highStringWidth / 2, tPosition[1] + highStringHeight / 2, mValuePaint);
                }
            } else {
                //画右边
                String highString = NumberUtils.keepPrecisionR(maxValue, precision);

                //计算显示位置
                int highStringWidth = Utils.calcTextWidth(mValuePaint, "──• " + highString);
                int highStringHeight = Utils.calcTextHeight(mValuePaint, "──• " + highString);

                float[] tPosition = new float[2];
                tPosition[0] = maxEntry == null ? 0f : maxEntry.getX();
                tPosition[1] = maxEntry == null ? 0f : maxEntry.getHigh();
                trans.pointValuesToPixel(tPosition);
                if (tPosition[0] + highStringWidth / 2 > mViewPortHandler.contentRight()) {
                    c.drawText(highString + " •──", tPosition[0] - highStringWidth / 2, tPosition[1] + highStringHeight / 2, mValuePaint);
                } else {
                    c.drawText("──• " + highString, tPosition[0] + highStringWidth / 2, tPosition[1] + highStringHeight / 2, mValuePaint);
                }
            }
        }
    }
}
