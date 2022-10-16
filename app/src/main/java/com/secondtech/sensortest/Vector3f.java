package com.secondtech.sensortest;

import java.util.Objects;

public final class Vector3f {
    public float x, y, z;

    Vector3f(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Vector3f(float[] values){
        if(values.length != 3)
            throw new RuntimeException("Vector3f requires 3 arguments, but " + values.length + " arguments given.");
        x = values[0];
        y = values[1];
        z = values[2];
    }
}
