package com.portfolio.udacity.android.bakingapp.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Created by JonGaming on 23/02/2018.
 * Handles doubles in json
 */

public class DoubleTypeAdapter extends TypeAdapter<Double> {
    @Override
    public void write(JsonWriter aWriter, Double value) throws IOException {
        if (value == null) {
            aWriter.nullValue();
            return;
        }
        aWriter.value(value);
    }

    @Override
    public Double read(JsonReader aReader) throws IOException {
        if(aReader.peek() == JsonToken.NULL){
            aReader.nextNull();
            return null;
        }
        String stringValue = aReader.nextString();
        try{
            return Double.valueOf(stringValue);
        } catch(NumberFormatException e){
            return null;
        }
    }
}
