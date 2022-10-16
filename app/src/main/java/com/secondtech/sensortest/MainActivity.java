package com.secondtech.sensortest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor accelerationSensor;
    private Sensor rotationSensor;
    private AccelerationRepository accelerationRepository = new AccelerationRepository();
    private RotationRepository rotationRepository = new RotationRepository();

    private float[] toGlobalCoordinateSystem(float[] v){
        Quaternion vQ = new Quaternion(v);
        Quaternion rQ = new Quaternion(rotationRepository.getData().getValue());
        Quaternion r = rQ.multiply(vQ).multiply(rQ.inverse());
        float result[] = {r.x, r.y, r.z};
        return result;
    }

    AccelerationSensorListener accelerationSensorListener = new AccelerationSensorListener(accelerationRepository);
    RotationSensorListener rotationSensorListener = new RotationSensorListener(rotationRepository);
    float cumulativeAcceleration[] = {0, 0, 0};
    Long time = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        accelerationRepository.getData().observe(MainActivity.this, new Observer<float[]>(){

            @Override
            public void onChanged(float[] floats) {
                Float dt = getDt();
                if(dt == null)
                    return;
                float[] acceleration = toGlobalCoordinateSystem(accelerationRepository.getData().getValue());
                for(int n = 0; n < acceleration.length; n++)
                cumulativeAcceleration[n] += acceleration[n]*dt;
                String values = "";
                for(int n = 0; n < acceleration.length; n++)
                    values += " " + cumulativeAcceleration[n];
                Log.d("onSensorChanged", values);
            }
        });
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

    private Float getDt() {
        if(time == null){
            time = System.nanoTime();
            return null;
        }
        Long previousTime = time;
        time = System.nanoTime();
        float dt = (time - previousTime)*1e-9f;
        return dt;
    }
}