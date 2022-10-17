package com.secondtech.sensortest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor accelerationSensor;
    private Sensor rotationSensor;
    private AccelerationSensorListener accelerationSensorListener;
    private RotationSensorListener rotationSensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        PlotViewModel plotViewModel = new ViewModelProvider(this).get(PlotViewModel.class);
        plotViewModel.startListen(this);
        accelerationSensorListener = new AccelerationSensorListener(plotViewModel.getLocalAcceleration());
        rotationSensorListener = new RotationSensorListener(plotViewModel.getRotation());
        ((PlotView)findViewById(R.id.plotViewX)).setData(this, plotViewModel.getAccelerationTimeSeries(), plotViewModel.getVelocityTimeSeries(), 0);
        ((PlotView)findViewById(R.id.plotViewY)).setData(this, plotViewModel.getAccelerationTimeSeries(), plotViewModel.getVelocityTimeSeries(), 1);
        ((PlotView)findViewById(R.id.plotViewZ)).setData(this, plotViewModel.getAccelerationTimeSeries(), plotViewModel.getVelocityTimeSeries(), 2);
    }

    @Override
    protected void onResume(){
        super.onResume();
        sensorManager.registerListener(accelerationSensorListener, accelerationSensor, SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(rotationSensorListener, rotationSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(accelerationSensorListener);
        sensorManager.unregisterListener(rotationSensorListener);
    }
}