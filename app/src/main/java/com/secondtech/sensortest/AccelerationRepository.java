package com.secondtech.sensortest;

import androidx.lifecycle.MutableLiveData;

public class AccelerationRepository {
    private MutableLiveData<float[]> acceleration = new MutableLiveData<float[]>();

    public MutableLiveData<float[]> getData(){
        return acceleration;
    }

    public void setAcceleration(float[] acceleration) {
        this.acceleration.setValue(acceleration);
    }
}
