package com.secondtech.sensortest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

class AccelerationSensorListener implements SensorEventListener {
    MutableLiveData<float[]> acceleration;

    AccelerationSensorListener(MutableLiveData<float[]> acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        acceleration.setValue(sensorEvent.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
