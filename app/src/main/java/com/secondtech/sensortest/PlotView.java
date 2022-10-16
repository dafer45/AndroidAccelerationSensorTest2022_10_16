package com.secondtech.sensortest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import java.util.LinkedList;
import java.util.Vector;

public class PlotView extends View {
    private int coordinateId;
    private LiveData<LinkedList<Vector3f>> timeSeries;

    public PlotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas){
        Paint background = new Paint();
        background.setColor(0xFFFFFFFF);
        canvas.drawRect(0, 0, getWidth(), getHeight(), background);
        LinkedList<Vector3f> data = timeSeries.getValue();
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
            float dx = getWidth()/data.size();
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
                        new Paint()
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

    public void setData(MainActivity mainActivity, LiveData<LinkedList<Vector3f>> timeSeries, int coordinateId){
        this.timeSeries = timeSeries;
        this.coordinateId = coordinateId;
        this.timeSeries.observe(mainActivity, dummy -> {
            postInvalidate();
        });
    }
}
