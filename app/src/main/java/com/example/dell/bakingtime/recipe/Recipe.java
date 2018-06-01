package com.example.dell.bakingtime.recipe;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Parcelable, Serializable{

    private int id;
    private String name;
    private int servings;
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private ArrayList<Step> steps = new ArrayList<>();


    /**
     * Constructor
     */
    public Recipe(int id, String name,  int servings, ArrayList<Ingredient> ingredients, ArrayList<Step> steps){
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.ingredients = ingredients;
        this.steps = steps;
    }


    /**
     * Constructor
     */
    public Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
        if (in.readByte() == 0x01) {
            ingredients = new ArrayList<>();
            in.readList(ingredients, Ingredient.class.getClassLoader());
        } else {
            ingredients = null;
        }
        if (in.readByte() == 0x01) {
            steps = new ArrayList<>();
            in.readList(steps, Step.class.getClassLoader());
        } else {
            steps = null;
        }
    }


    /**
     * getters
     */
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }


    /**
     * setters
     */
    public void setId(int id) {
        this.id = id;
    }

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
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(servings);
        if (ingredients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(ingredients);
        }
        if (steps == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(steps);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };


    /**
     * Used instead of equals, because equals doesn't work as intended.
     * I didn't override equals because the I should override hashCode() too.
     * @param recipeToCheck the recipe to check if it is the same
     * @return true if the recipes are the same, false otherwise
     */
    public boolean checkIfEqual(Recipe recipeToCheck){
        if(this.getId() != recipeToCheck.getId()) {
            return false;
        }
        if(!this.getName().equals(recipeToCheck.getName())) {
            return false;
        }
        if(this.servings != recipeToCheck.servings) {
            return false;
        }
        for(int i=0; i<ingredients.size(); i++) {
            if (!ingredients.get(i).checkIfEqual(recipeToCheck.ingredients.get(i))) {
                return false;
            }
        }
        for(int i=0; i<steps.size(); i++) {
            if (!steps.get(i).checkIfEquals(recipeToCheck.steps.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(id) + "&"
                + name + "&"
                + String.valueOf(servings) + "&"
                + ingredients.toString() + "&"
                + steps.toString();
    }
}
