package com.secondtech.sensortest;

import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;

public class VelocityTimeSeriesRepository {
    MutableLiveData<LinkedList<Timestamped<Vector3f>>> timeSeries = new MutableLiveData<LinkedList<Timestamped<Vector3f>>>();

    VelocityTimeSeriesRepository(){
        timeSeries.setValue(new LinkedList<Timestamped<Vector3f>>());
    }

    public MutableLiveData<LinkedList<Timestamped<Vector3f>>> getVelocityTimeSeries(){
        return timeSeries;
    }

    public void addVelocity(Timestamped<Vector3f> velocity){
        LinkedList<Timestamped<Vector3f>> list = timeSeries.getValue();
        list.add(velocity);
        if(list.size() > 1000)
            list.pop();
        timeSeries.setValue(list);
    }
}
