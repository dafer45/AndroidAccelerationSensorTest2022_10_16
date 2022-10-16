package com.secondtech.sensortest;

import androidx.lifecycle.MutableLiveData;

public class RotationRepository {
    private MutableLiveData<float[]> rotation = new MutableLiveData<float[]>();

    RotationRepository(){
        rotation.setValue(new float[]{0, 0, 1, 0});
    }

    public MutableLiveData<float[]> getData(){
        return rotation;
    }

    public void setRotation(float[] rotation) {
        this.rotation.setValue(rotation);
    }
}
