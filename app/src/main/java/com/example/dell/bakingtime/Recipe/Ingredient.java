package com.example.dell.bakingtime.Recipe;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Ingredient implements Parcelable{

    /*public static final int CUP = 0;
    public static final int TBLSP = 1;
    public static final int TSP = 2;
    public static final int K = 3;
    public static final int G = 4;
    public static final int OZ = 5;
    public static final int UNIT = 6;*/

    public static final String CUP_STRING = "CUP";
    public static final String TBLSP_STRING = "TBLSP";
    public static final String TSP_STRING = "TSP";
    public static final String K_STRING = "K";
    public static final String G_STRING = "G";
    public static final String OZ_STRING = "OZ";
    public static final String UNIT_STRING = "UNIT";

    private double quantity;
    private String measure;
    private String name;

    public Ingredient(double quantity, String measure, String name){
        this.quantity = quantity;
        this.measure = measure;
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getName() {
        return name;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected Ingredient(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(quantity);
        dest.writeString(measure);
        dest.writeString(name);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    /*@Override
    public String toString() {
        return String.valueOf(quantity) + "_" + String.valueOf(measure) + "_" + String.valueOf(name);
    }*/
}
