package com.secondtech.sensortest;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class PlotViewModel extends ViewModel {
    private AccelerationRepository localAccelerationRepository = new AccelerationRepository();
    private AccelerationRepository globalAccelerationRepository = new AccelerationRepository();
    private RotationRepository rotationRepository = new RotationRepository();
    float cumulativeAcceleration[] = {0, 0, 0};
    Long time = null;

    public void startListen(MainActivity mainActivity){
        localAccelerationRepository.getData().observe(mainActivity, new Observer<float[]>(){
            @Override
            public void onChanged(float[] floats) {
                Float dt = getDt();
                if(dt == null)
                    return;
                float[] acceleration = toGlobalCoordinateSystem(localAccelerationRepository.getData().getValue());
                globalAccelerationRepository.setAcceleration(acceleration);
                for(int n = 0; n < acceleration.length; n++)
                    cumulativeAcceleration[n] += acceleration[n]*dt;
                String values = "";
                for(int n = 0; n < acceleration.length; n++)
                    values += " " + cumulativeAcceleration[n];
//                Log.d("onSensorChanged", values);
            }
        });
    }

    private float[] toGlobalCoordinateSystem(float[] v){
        Quaternion vQ = new Quaternion(v);
        Quaternion rQ = new Quaternion(rotationRepository.getData().getValue());
        Quaternion r = rQ.multiply(vQ).multiply(rQ.inverse());
        float result[] = {r.x, r.y, r.z};
        return result;
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

    public MutableLiveData<float[]> getLocalAcceleration(){
        return localAccelerationRepository.getData();
    }

    public LiveData<float[]> getGlobalAcceleration(){
        return globalAccelerationRepository.getData();
    }

    public MutableLiveData<float[]> getRotation(){
        return rotationRepository.getData();
    }
}
