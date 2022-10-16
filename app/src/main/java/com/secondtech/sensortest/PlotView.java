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

public class PlotView extends View {
    private LiveData<float[]> acceleration;
    private int coordinateId;
    private LinkedList<Float> timeseries = new LinkedList<Float>();

    public PlotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas){
        Paint background = new Paint();
        background.setColor(0xFFFFFFFF);
        canvas.drawRect(0, 0, getWidth(), getHeight(), background);
        if(timeseries.size() != 0){
            float min = timeseries.get(0);
            float max = timeseries.get(0);
            for(float value : timeseries){
                if(min > value)
                    min = value;
                if(max < value)
                    max = value;
            }
            float dx = getWidth()/timeseries.size();
            for(int n = 1; n < timeseries.size(); n++){
                float previous = timeseries.get(n-1);
                float current = timeseries.get(n);
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

    public void setData(MainActivity mainActivity, LiveData<float[]> acceleration, int coordinateId){
        this.acceleration = acceleration;
        this.coordinateId = coordinateId;
        this.acceleration.observe(mainActivity, a -> {
            timeseries.add(acceleration.getValue()[coordinateId]);
            if(timeseries.size() > 1000)
                timeseries.pop();
            postInvalidate();
        });
    }
}
