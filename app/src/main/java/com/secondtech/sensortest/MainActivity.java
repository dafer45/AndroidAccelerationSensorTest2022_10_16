package com.secondtech.sensortest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor accelerationSensor;
    private Sensor rotationSensor;

    float cumulativeAcceleration[] = {0, 0, 0};
    Long time = null;

    private class AccelerationSensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(!rotationIsInitialized || time == null) {
                time = System.nanoTime();
                return;
            }
            Long previousTime = time;
            time = System.nanoTime();
            float dt = (time - previousTime)*1e-9f;
            float[] acceleration = new float[3];
            for(int n = 0; n < 3; n++)
                acceleration[n] = sensorEvent.values[n];
            acceleration = toGlobalCoordinateSystem(acceleration);

            for(int n = 0; n < sensorEvent.values.length; n++)
                cumulativeAcceleration[n] += acceleration[n]*dt;
            String values = "";
            for(int n = 0; n < sensorEvent.values.length; n++)
                values += " " + cumulativeAcceleration[n];
            Log.d("onSensorChanged", values);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    private class Quaternion{
        public float x, y, z, w;
        Quaternion(float x, float y, float z, float w){
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

        public Quaternion multiply(Quaternion rhs){
            return new Quaternion(
                    x*rhs.w + y*rhs.z - z*rhs.y + w*rhs.x,
                    -x*rhs.z + y*rhs.w + z*rhs.x + w*rhs.y,
                    x*rhs.y - y*rhs.x + z*rhs.w + w*rhs.z,
                    - x*rhs.x - y*rhs.y - z*rhs.z + w*rhs.w
            );
        }

        public Quaternion inverse(){
            float normSquared = x*x + y*y + z*z + w*w;
            return new Quaternion(
                    -x/normSquared,
                    -y/normSquared,
                    -z/normSquared,
                    w/normSquared
            );
        }
    }

    private float[] toGlobalCoordinateSystem(float[] v){
        float result[] = new float[3];
        Quaternion vQ = new Quaternion(v[0], v[1], v[2], 0);
        Quaternion rQ = new Quaternion(
                rotation[0],
                rotation[1],
                rotation[2],
                rotation[3]
        );
        Quaternion rQI = rQ.inverse();
        Quaternion r = rQ.multiply(vQ).multiply(rQI);

        result[0] = r.x;
        result[1] = r.y;
        result[2] = r.z;

        return result;
    }

    int counter = 0;
    float rotation[] = {0, 0, 0, 0};
    boolean rotationIsInitialized = false;
    private class RotationSensorListener implements SensorEventListener {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            for(int n = 0; n < 4; n++)
                rotation[n] = sensorEvent.values[n];
            if(counter++%100 != 0)
                return;
            String values = "";
            for(int n = 0; n < sensorEvent.values.length; n++)
                values += " " + sensorEvent.values[n];
            rotationIsInitialized = true;
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    AccelerationSensorListener accelerationSensorListener = new AccelerationSensorListener();
    RotationSensorListener rotationSensorListener = new RotationSensorListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        rotationSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
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