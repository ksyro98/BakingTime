package com.example.dell.bakingtime.recipe;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Ingredient implements Parcelable, Serializable{

    private double quantity;
    private String measure;
    private String name;


    /**
     * Constructor
     */
    public Ingredient(double quantity, String measure, String name){
        this.quantity = quantity;
        this.measure = measure;
        this.name = name;
    }


    /**
     * Constructor
     */
    private Ingredient(Parcel in) {
        quantity = in.readDouble();
        measure = in.readString();
        name = in.readString();
    }


    /**
     * getters
     */
    public double getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getName() {
        return name;
    }


    /**
     * setter
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * parcelable methods
     */
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


    /**
     * Used instead of equals, because equals doesn't work as intended.
     * I didn't override equals because the I should override hashCode() too.
     * @param ingredientToCheck the ingredient to check if it is the same
     * @return true if the ingredients are the same, false otherwise
     */
    boolean checkIfEqual(Ingredient ingredientToCheck){
        if(!this.measure.equals(ingredientToCheck.measure)) {
            return false;
        }
        if(!this.name.equals(ingredientToCheck.name)) {
            return false;
        }
        if(this.quantity != ingredientToCheck.quantity) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.name + " [" + String.valueOf(this.quantity) + " " + this.measure+"]";
    }
}
