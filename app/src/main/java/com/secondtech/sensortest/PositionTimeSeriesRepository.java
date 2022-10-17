package com.secondtech.sensortest;

import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;

public class PositionTimeSeriesRepository {
    MutableLiveData<LinkedList<Vector3f>> timeSeries = new MutableLiveData<LinkedList<Vector3f>>();

    PositionTimeSeriesRepository(){
        timeSeries.setValue(new LinkedList<Vector3f>());
    }

    public MutableLiveData<LinkedList<Vector3f>> getPositionTimeSeries(){
        return timeSeries;
    }

    public void addPosition(Vector3f position){
        LinkedList<Vector3f> list = timeSeries.getValue();
        list.add(position);
        if(list.size() > 1000)
            list.pop();
        timeSeries.setValue(list);
    }
}
