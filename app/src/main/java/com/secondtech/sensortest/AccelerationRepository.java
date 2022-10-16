package com.secondtech.sensortest;

import androidx.lifecycle.MutableLiveData;

public class AccelerationRepository {
    private MutableLiveData<Vector3f> acceleration = new MutableLiveData<Vector3f>();

    public MutableLiveData<Vector3f> getData(){
        return acceleration;
    }

    public void setAcceleration(Vector3f acceleration) {
        this.acceleration.setValue(acceleration);
    }
}
