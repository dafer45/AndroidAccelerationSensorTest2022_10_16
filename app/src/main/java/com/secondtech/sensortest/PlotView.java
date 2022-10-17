package com.secondtech.sensortest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.LinkedList;

public class PlotView extends View {
    private int coordinateId;
    private LiveData<LinkedList<Vector3f>> accelerationTimeSeries;
    private LiveData<LinkedList<Vector3f>> velocityTimeSeries;
    private LiveData<LinkedList<Vector3f>> positionTimeSeries;

    public PlotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas){
        Paint background = new Paint();
        background.setColor(0xFFFFFFFF);
        canvas.drawRect(0, 0, getWidth(), getHeight(), background);
        LinkedList<Vector3f> data = accelerationTimeSeries.getValue();
        drawtimeSeries(canvas, data, new Paint());
        Paint paint = new Paint();
        paint.setColor(0xFFFF0000);
        drawtimeSeries(canvas, velocityTimeSeries.getValue(), paint);
        paint.setColor(0xFF0000FF);
        drawtimeSeries(canvas, positionTimeSeries.getValue(), paint);
        if(positionTimeSeries.getValue().size() != 0) {
            Float value = getValue(positionTimeSeries.getValue().getLast());
            Paint textPaint = new Paint();
            textPaint.setTextSize(64);
            canvas.drawText("" + value, getWidth() / 2, getHeight() / 2, textPaint);
        }
    }

    private void drawtimeSeries(Canvas canvas, LinkedList<Vector3f> data, Paint paint) {
        if(data.size() != 0){
            float min = getValue(data.get(0));
            float max = getValue(data.get(0));
            for(Vector3f v : data){
                float value = getValue(v);
                if(min > value)
                    min = value;
                if(max < value)
                    max = value;
            }
            float dx = getWidth()/ data.size();
            for(int n = 1; n < data.size(); n++){
                float previous = getValue(data.get(n-1));
                float current = getValue(data.get(n));
                float previousX = dx*(n-1);
                float previousY = getHeight()*(1 - (previous - min)/(max-min));
                float currentX = dx*n;
                float currentY = getHeight()*(1 - (current - min)/(max-min));
                canvas.drawLine(
                        previousX,
                        previousY,
                        currentX,
                        currentY,
                        paint
                );
            }
        }
    }

    private float getValue(Vector3f v){
        switch(coordinateId){
        case 0:
            return v.x;
        case 1:
            return v.y;
        case 2:
            return v.z;
        default:
            throw new RuntimeException("Unknown coordinateId " + coordinateId);
        }
    }

    public void setData(
            MainActivity mainActivity,
            LiveData<LinkedList<Vector3f>> accelerationTimeSeries,
            LiveData<LinkedList<Vector3f>> velocityTimeSeries,
            LiveData<LinkedList<Vector3f>> positionTimeSeries,
            int coordinateId
    ){
        this.accelerationTimeSeries = accelerationTimeSeries;
        this.velocityTimeSeries = velocityTimeSeries;
        this.positionTimeSeries = positionTimeSeries;
        this.coordinateId = coordinateId;
        this.velocityTimeSeries.observe(mainActivity, dummy -> {
            postInvalidate();
        });
    }
}
