package com.secondtech.sensortest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

class AccelerationSensorListener implements SensorEventListener {
    AccelerationRepository accelerationRepository;

    AccelerationSensorListener(AccelerationRepository accelerationRepository) {
        this.accelerationRepository = accelerationRepository;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        accelerationRepository.setAcceleration(sensorEvent.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
