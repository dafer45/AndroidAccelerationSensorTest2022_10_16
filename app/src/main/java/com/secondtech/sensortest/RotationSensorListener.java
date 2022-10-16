package com.secondtech.sensortest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

class RotationSensorListener implements SensorEventListener {
    private RotationRepository rotationRepository;

    public RotationSensorListener(RotationRepository rotationRepository) {
        this.rotationRepository = rotationRepository;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float rotation[] = new float[4];
        for (int n = 0; n < 4; n++)
            rotation[n] = sensorEvent.values[n];
        rotationRepository.setRotation(rotation);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}
