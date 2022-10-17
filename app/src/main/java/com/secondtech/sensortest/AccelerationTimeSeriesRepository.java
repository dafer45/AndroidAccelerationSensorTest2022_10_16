package com.secondtech.sensortest;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;

public class AccelerationTimeSeriesRepository {
    MutableLiveData<LinkedList<Timestamped<Vector3f>>> timeSeries = new MutableLiveData<LinkedList<Timestamped<Vector3f>>>();

    AccelerationTimeSeriesRepository(){
        timeSeries.setValue(new LinkedList<Timestamped<Vector3f>>());
    }

    public MutableLiveData<LinkedList<Timestamped<Vector3f>>> getAccelerationTimeSeries(){
        return timeSeries;
    }

    public void addAcceleration(Timestamped<Vector3f> acceleration){
        LinkedList<Timestamped<Vector3f>> list = timeSeries.getValue();
        list.add(acceleration);
        if(list.size() > 1000)
            list.pop();
        timeSeries.setValue(list);
    }
}
