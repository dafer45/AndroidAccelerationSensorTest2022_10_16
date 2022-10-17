package com.secondtech.sensortest;

import androidx.lifecycle.MutableLiveData;

import java.util.LinkedList;

public class PositionTimeSeriesRepository {
    MutableLiveData<LinkedList<Timestamped<Vector3f>>> timeSeries = new MutableLiveData<LinkedList<Timestamped<Vector3f>>>();

    PositionTimeSeriesRepository(){
        timeSeries.setValue(new LinkedList<Timestamped<Vector3f>>());
    }

    public MutableLiveData<LinkedList<Timestamped<Vector3f>>> getPositionTimeSeries(){
        return timeSeries;
    }

    public void addPosition(Timestamped<Vector3f> position){
        LinkedList<Timestamped<Vector3f>> list = timeSeries.getValue();
        list.add(position);
        if(list.size() > 1000)
            list.pop();
        timeSeries.setValue(list);
    }
}
