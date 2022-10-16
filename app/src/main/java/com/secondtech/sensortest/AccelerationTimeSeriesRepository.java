package com.secondtech.sensortest;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;

public class AccelerationTimeSeriesRepository {
    MutableLiveData<LinkedList<Vector3f>> timeSeries = new MutableLiveData<LinkedList<Vector3f>>();

    AccelerationTimeSeriesRepository(){
        timeSeries.setValue(new LinkedList<Vector3f>());
    }

    public MutableLiveData<LinkedList<Vector3f>> getAccelerationTimeSeries(){
        return timeSeries;
    }

    public void addAcceleration(Vector3f acceleration){
        LinkedList<Vector3f> list = timeSeries.getValue();
        list.add(acceleration);
        if(list.size() > 1000)
            list.pop();
        timeSeries.setValue(list);
    }
}
