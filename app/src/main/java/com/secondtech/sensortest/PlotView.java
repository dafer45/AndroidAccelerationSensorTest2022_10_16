package com.secondtech.sensortest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class PlotView extends View {
    public PlotView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas){
        Paint background = new Paint();
        background.setColor(0xFFFF0000);
        canvas.drawRect(0, 0, getWidth(), getHeight(), background);
    }

    public void setPlotViewModel(){

    }
}
