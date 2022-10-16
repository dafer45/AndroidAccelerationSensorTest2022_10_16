package com.secondtech.sensortest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import androidx.lifecycle.MutableLiveData;

class RotationSensorListener implements SensorEventListener {
    private MutableLiveData<float[]> rotation;

    public RotationSensorListener(MutableLiveData<float[]> rotation) {
        this.rotation = rotation;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float rotation[] = new float[4];
        for (int n = 0; n < 4; n++)
            rotation[n] = sensorEvent.values[n];
        this.rotation.setValue(rotation);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
