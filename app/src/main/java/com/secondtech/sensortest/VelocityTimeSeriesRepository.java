package com.secondtech.sensortest;

import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;

public class VelocityTimeSeriesRepository {
    MutableLiveData<LinkedList<Vector3f>> timeSeries = new MutableLiveData<LinkedList<Vector3f>>();

    VelocityTimeSeriesRepository(){
        timeSeries.setValue(new LinkedList<Vector3f>());
    }

    public MutableLiveData<LinkedList<Vector3f>> getVelocityTimeSeries(){
        return timeSeries;
    }

    public void addVelocity(Vector3f velocity){
        LinkedList<Vector3f> list = timeSeries.getValue();
        list.add(velocity);
        if(list.size() > 1000)
            list.pop();
        timeSeries.setValue(list);
    }
}
