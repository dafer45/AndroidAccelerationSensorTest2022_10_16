package com.secondtech.sensortest;

import android.util.Log;

import androidx.lifecycle.ViewModel;

public class PlotViewModel extends ViewModel {
    private AccelerationRepository localAccelerationRepository = new AccelerationRepository();
    private AccelerationRepository globalAccelerationRepository = new AccelerationRepository();
    private RotationRepository rotationRepository = new RotationRepository();

    public AccelerationRepository getLocalAccelerationRepository(){
        return localAccelerationRepository;
    }

    public AccelerationRepository getGlobalAccelerationRepository(){
        return globalAccelerationRepository;
    }

    public RotationRepository getRotationRepository() {
        return rotationRepository;
    }
}
