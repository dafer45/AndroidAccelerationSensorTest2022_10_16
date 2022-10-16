package com.secondtech.sensortest;

public class Quaternion {
    public float x, y, z, w;

    Quaternion(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    Quaternion(float[] values) {
        if(values.length == 3){
            w = 0;
        }
        else if(values.length == 4){
            w = values[3];
        }
        else{
            throw new RuntimeException("Quaternion takes 3 or 4 arguments, not " + values.length);
        }
        x = values[0];
        y = values[1];
        z = values[2];
    }

    public Quaternion multiply(Quaternion rhs) {
        return new Quaternion(
                x * rhs.w + y * rhs.z - z * rhs.y + w * rhs.x,
                -x * rhs.z + y * rhs.w + z * rhs.x + w * rhs.y,
                x * rhs.y - y * rhs.x + z * rhs.w + w * rhs.z,
                -x * rhs.x - y * rhs.y - z * rhs.z + w * rhs.w
        );
    }

    public Quaternion inverse() {
        float normSquared = x * x + y * y + z * z + w * w;
        return new Quaternion(
                -x / normSquared,
                -y / normSquared,
                -z / normSquared,
                w / normSquared
        );
    }
}
