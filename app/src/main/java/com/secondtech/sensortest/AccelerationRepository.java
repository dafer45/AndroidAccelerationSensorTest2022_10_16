package com.secondtech.sensortest;

import androidx.lifecycle.MutableLiveData;

public class AccelerationRepository {
    private MutableLiveData<Timestamped<Vector3f>> acceleration = new MutableLiveData<Timestamped<Vector3f>>();

    public MutableLiveData<Timestamped<Vector3f>> getData(){
        return acceleration;
    }

    public void setAcceleration(Timestamped<Vector3f> acceleration) {
        this.acceleration.setValue(acceleration);
    }
}
