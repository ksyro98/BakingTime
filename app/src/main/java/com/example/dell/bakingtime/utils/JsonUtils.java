package com.example.dell.bakingtime.utils;


import com.example.dell.bakingtime.recipe.Ingredient;
import com.example.dell.bakingtime.recipe.Recipe;
import com.example.dell.bakingtime.recipe.Step;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class JsonUtils {
    /**
     * Gets a recipe from a JSONArray.
     * @param jsonArray the JSONArray to get the recipe
     * @param id the index of the recipe in the JSONArray
     * @return the recipe that we got from the JSONArray
     */
    public static Recipe getRecipeFromJson(JSONArray jsonArray, int id){
        try {
            String recipeName = jsonArray.getJSONObject(id).getString("name");
            int recipeId = id + 1;
            int servings = jsonArray.getJSONObject(id).getInt("servings");

            JSONArray ingredientJSONArray = jsonArray.getJSONObject(id).getJSONArray("ingredients");

            ArrayList<Ingredient> ingredientArrayList = new ArrayList<>();

            for(int i=0; i<ingredientJSONArray.length(); i++){
                double quantity = ingredientJSONArray.getJSONObject(i).getDouble("quantity");

                String measure = ingredientJSONArray.getJSONObject(i).getString("measure");

                String name = ingredientJSONArray.getJSONObject(i).getString("ingredient");

                ingredientArrayList.add(new Ingredient(quantity, measure, name));
            }


            JSONArray stepJSONArray = jsonArray.getJSONObject(id).getJSONArray("steps");
            ArrayList<Step> stepArrayList = new ArrayList<>();
            for(int i=0; i<stepJSONArray.length(); i++){
                int stepId = stepJSONArray.getJSONObject(i).getInt("id");
                String shortDescription = stepJSONArray.getJSONObject(i).getString("shortDescription");
                String description = stepJSONArray.getJSONObject(i).getString("description");
                String videoURL = stepJSONArray.getJSONObject(i).getString("videoURL");
                String thumbnailURL = stepJSONArray.getJSONObject(i).getString("thumbnailURL");
                stepArrayList.add(new Step(stepId, shortDescription, description, videoURL, thumbnailURL));
            }

            return new Recipe(recipeId, recipeName, servings, ingredientArrayList, stepArrayList);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
