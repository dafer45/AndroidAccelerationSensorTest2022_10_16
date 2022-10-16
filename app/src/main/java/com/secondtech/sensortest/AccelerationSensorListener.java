package com.secondtech.sensortest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

class AccelerationSensorListener implements SensorEventListener {
    MutableLiveData<Vector3f> acceleration;

    AccelerationSensorListener(MutableLiveData<Vector3f> acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        acceleration.setValue(
                new Vector3f(
                        sensorEvent.values[0],
                        sensorEvent.values[1],
                        sensorEvent.values[2]
                )
        );
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
