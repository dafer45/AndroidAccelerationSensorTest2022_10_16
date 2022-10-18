package com.secondtech.sensortest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

class AccelerationSensorListener implements SensorEventListener {
    MutableLiveData<Timestamped<Vector3f>> acceleration;

    AccelerationSensorListener(MutableLiveData<Timestamped<Vector3f>> acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        acceleration.setValue(
                new Timestamped<Vector3f>(
                        new Vector3f(
                                sensorEvent.values[0],
                                sensorEvent.values[1],
                                sensorEvent.values[2]
                        ),
                        sensorEvent.timestamp
                )
        );
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
