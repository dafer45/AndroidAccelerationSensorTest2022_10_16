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
    private PositionTimeSeriesRepository positionTimeSeriesRepository = new PositionTimeSeriesRepository();
    Long time = null;
    Long time2 = null;

    int counter = 0;
    public void startListen(MainActivity mainActivity){
        localAccelerationRepository.getData().observe(mainActivity, new Observer<Timestamped<Vector3f>>(){
            @Override
            public void onChanged(Timestamped<Vector3f> floats) {
                Timestamped<Vector3f> acceleration = toGlobalCoordinateSystem(localAccelerationRepository.getData().getValue());
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
                    LinkedList<Timestamped<Vector3f>> velocityTimeSeries = velocityTimeSeriesRepository.getVelocityTimeSeries().getValue();
                    if(velocityTimeSeries.size() != 0){
                        v = velocityTimeSeries.getLast().data;
                    }
                    Float dt = getDt();
                    if(dt == null)
                        return;
                    Vector3f a = accelerationTimeSeries.getLast().data;
                    Vector3f newV = new Vector3f(
                            v.x + a.x*dt,
                            v.y + a.y*dt,
                            v.z + a.z*dt
                    );
                    velocityTimeSeriesRepository.addVelocity(new Timestamped<Vector3f>(newV, accelerationTimeSeries.getLast().timestamp));
                    if(counter++%10 == 0)
                        Log.d("Velocity", "" + newV.x + " " + newV.y + " " + newV.z);
                }
        );
        velocityTimeSeriesRepository.getVelocityTimeSeries().observe(
                mainActivity,
                velocityTimeSeries -> {
                    Vector3f p = new Vector3f(0, 0, 0);
                    LinkedList<Timestamped<Vector3f>> positionTimeSeries = positionTimeSeriesRepository.getPositionTimeSeries().getValue();
                    if(positionTimeSeries.size() != 0){
                        p = positionTimeSeries.getLast().data;
                    }
                    Float dt = getDt2();
                    if(dt == null)
                        return;
                    Vector3f v = velocityTimeSeries.getLast().data;
                    Vector3f newP = new Vector3f(
                            p.x + v.x*dt,
                            p.y + v.y*dt,
                            p.z + v.z*dt
                    );
                    positionTimeSeriesRepository.addPosition(new Timestamped<Vector3f>(newP, velocityTimeSeries.getLast().timestamp));
                }
        );
    }

    private Timestamped<Vector3f> toGlobalCoordinateSystem(Timestamped<Vector3f> v){
        Quaternion vQ = new Quaternion(v.data.x, v.data.y, v.data.z, 0);
        Quaternion rQ = new Quaternion(rotationRepository.getData().getValue());
        Quaternion r = rQ.multiply(vQ).multiply(rQ.inverse());
        return new Timestamped<Vector3f>(new Vector3f(r.x, r.y, r.z), v.timestamp);
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

    private Float getDt2() {
        if(time2 == null){
            time2 = System.nanoTime();
            return null;
        }
        Long previousTime = time2;
        time2 = System.nanoTime();
        float dt = (time2 - previousTime)*1e-9f;
        return dt;
    }

    public MutableLiveData<Timestamped<Vector3f>> getLocalAcceleration(){
        return localAccelerationRepository.getData();
    }

    public LiveData<Timestamped<Vector3f>> getGlobalAcceleration(){
        return globalAccelerationRepository.getData();
    }

    public LiveData<LinkedList<Timestamped<Vector3f>>> getAccelerationTimeSeries(){
        return accelerationTimeSeriesRepository.getAccelerationTimeSeries();
    }

    public LiveData<LinkedList<Timestamped<Vector3f>>> getVelocityTimeSeries(){
        return velocityTimeSeriesRepository.getVelocityTimeSeries();
    }

    public LiveData<LinkedList<Timestamped<Vector3f>>> getPositionTimeSeries(){
        return positionTimeSeriesRepository.getPositionTimeSeries();
    }

    public MutableLiveData<float[]> getRotation(){
        return rotationRepository.getData();
    }
}
