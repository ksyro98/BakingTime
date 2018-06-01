package com.example.dell.bakingtime.recipe;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Recipe implements Parcelable, Serializable{

    private int id;
    private String name;
    private int servings;
    private String image;   //always null
    private ArrayList<Ingredient> ingredients = new ArrayList<>();
    private ArrayList<Step> steps = new ArrayList<>();

    public Recipe(int id, String name,  int servings, String image, ArrayList<Ingredient> ingredients, ArrayList<Step> steps){
        this.id = id;
        this.name = name;
        this.servings = servings;
        this.image = image;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public Recipe(Parcel in) {
        id = in.readInt();
        name = in.readString();
        servings = in.readInt();
        image = in.readString();
        if (in.readByte() == 0x01) {
            ingredients = new ArrayList<Ingredient>();
            in.readList(ingredients, Ingredient.class.getClassLoader());
        } else {
            ingredients = null;
        }
        if (in.readByte() == 0x01) {
            steps = new ArrayList<Step>();
            in.readList(steps, Step.class.getClassLoader());
        } else {
            steps = null;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getServings(){
        return servings;
    }

    public String getImage(){
        return image;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServings(int servings){
        this.servings = servings;
    }

    public void setImage(String image){
        this.image = image;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeInt(servings);
        dest.writeString(image);
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

    @Override
    public String toString() {
        return String.valueOf(id) + "&"
                + name + "&"
                + String.valueOf(servings) + "&"
                + image + "&"
                + ingredients.toString() + "&"
                + steps.toString();
    }

    public boolean compare(Recipe recipeToCompare){
        if(this.getId() != recipeToCompare.getId())
            return false;
        if(!this.getName().equals(recipeToCompare.getName()))
            return false;
        if(this.servings != recipeToCompare.servings)
            return false;
        if(!this.getImage().equals(recipeToCompare.getImage()))
            return false;
        for(int i=0; i<ingredients.size(); i++)
            if(!ingredients.get(i).compare(recipeToCompare.ingredients.get(i)))
                return false;
        for(int i=0; i<steps.size(); i++)
            if(!steps.get(i).compare(recipeToCompare.steps.get(i)))
                return false;
        return true;
    }
}
