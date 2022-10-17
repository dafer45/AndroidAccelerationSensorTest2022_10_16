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
    private VelocityTimeSeriesRepository velocityTimeSeriesRepository = new VelocityTimeSeriesRepository();
    Long time = null;

    int counter = 0;
    public void startListen(MainActivity mainActivity){
        localAccelerationRepository.getData().observe(mainActivity, new Observer<Vector3f>(){
            @Override
            public void onChanged(Vector3f floats) {
                Vector3f acceleration = toGlobalCoordinateSystem(localAccelerationRepository.getData().getValue());
                globalAccelerationRepository.setAcceleration(acceleration);
            }
        });
        globalAccelerationRepository.getData().observe(
                mainActivity,
                acceleration -> {
                    accelerationTimeSeriesRepository.addAcceleration(acceleration);
                }
        );
        accelerationTimeSeriesRepository.getAccelerationTimeSeries().observe(
                mainActivity,
                accelerationTimeSeries -> {
                    Vector3f v = new Vector3f(0, 0, 0);
                    LinkedList<Vector3f> velocityTimeSeries = velocityTimeSeriesRepository.getVelocityTimeSeries().getValue();
                    if(velocityTimeSeries.size() != 0){
                        v = velocityTimeSeries.getLast();
                    }
                    Float dt = getDt();
                    if(dt == null)
                        return;
                    Vector3f a = accelerationTimeSeries.getLast();
                    Vector3f newV = new Vector3f(
                            v.x + a.x*dt,
                            v.y + a.y*dt,
                            v.z + a.z*dt
                    );
                    velocityTimeSeriesRepository.addVelocity(newV);
                    if(counter++%10 == 0)
                        Log.d("Velocity", "" + newV.x + " " + newV.y + " " + newV.z);
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

    public LiveData<LinkedList<Vector3f>> getVelocityTimeSeries(){
        return velocityTimeSeriesRepository.getVelocityTimeSeries();
    }

    public MutableLiveData<float[]> getRotation(){
        return rotationRepository.getData();
    }
}
