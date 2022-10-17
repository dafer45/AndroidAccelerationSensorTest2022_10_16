package com.secondtech.sensortest;

public class Timestamped<Type> {
    Type data;
    long timestamp;

    Timestamped(Type data, long timestamp){
        this.data = data;
        this.timestamp = timestamp;
    }
}
