package com.secondtech.sensortest;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.LinkedList;

public class PlotViewModel extends ViewModel {
    private AccelerationRepository localAccelerationRepository = new AccelerationRepository();
    private AccelerationRepository globalAccelerationRepository = new AccelerationRepository();
    private RotationRepository rotationRepository = new RotationRepository();
    private AccelerationTimeSeriesRepository accelerationTimeSeriesRepository = new AccelerationTimeSeriesRepository();
//    float cumulativeAcceleration[] = {0, 0, 0};
    Long time = null;

    public void startListen(MainActivity mainActivity){
        localAccelerationRepository.getData().observe(mainActivity, new Observer<Vector3f>(){
            @Override
            public void onChanged(Vector3f floats) {
                Float dt = getDt();
                if(dt == null)
                    return;
                Vector3f acceleration = toGlobalCoordinateSystem(localAccelerationRepository.getData().getValue());
                globalAccelerationRepository.setAcceleration(acceleration);
/*                for(int n = 0; n < acceleration.length; n++)
                    cumulativeAcceleration[n] += acceleration[n]*dt;
                String values = "";
                for(int n = 0; n < acceleration.length; n++)
                    values += " " + cumulativeAcceleration[n];*/
            }
        });
        globalAccelerationRepository.getData().observe(
                mainActivity,
                acceleration -> {
                    accelerationTimeSeriesRepository.addAcceleration(acceleration);
                }
        );
    }

    private Vector3f toGlobalCoordinateSystem(Vector3f v){
        Quaternion vQ = new Quaternion(v.x, v.y, v.z, 0);
        Quaternion rQ = new Quaternion(rotationRepository.getData().getValue());
        Quaternion r = rQ.multiply(vQ).multiply(rQ.inverse());
        return new Vector3f(r.x, r.y, r.z);
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

    public MutableLiveData<Vector3f> getLocalAcceleration(){
        return localAccelerationRepository.getData();
    }

    public LiveData<Vector3f> getGlobalAcceleration(){
        return globalAccelerationRepository.getData();
    }

    public LiveData<LinkedList<Vector3f>> getAccelerationTimeSeries(){
        return accelerationTimeSeriesRepository.getAccelerationTimeSeries();
    }

    public MutableLiveData<float[]> getRotation(){
        return rotationRepository.getData();
    }
}
