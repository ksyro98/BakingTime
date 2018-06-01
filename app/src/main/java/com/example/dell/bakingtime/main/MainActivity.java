package com.example.dell.bakingtime.main;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.dell.bakingtime.ingredients_and_steps.IngredientsAndStepsActivity;
import com.example.dell.bakingtime.utils.JsonUtils;
import com.example.dell.bakingtime.R;
import com.example.dell.bakingtime.recipe.Recipe;
import com.example.dell.bakingtime.testing.SimpleIdlingResource;

import org.json.JSONArray;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements BakingAdapter.ClickListener {

    private static final String RECIPE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    @BindView(R.id.baking_recycler_view) RecyclerView bakingRecyclerView;
    @BindView(R.id.progress_bar) ProgressBar progressBar;
    @BindView(R.id.error_text_view) TextView errorTextView;
    @BindView(R.id.error_button) Button errorButton;


    private ArrayList<Recipe> recipeArrayList = new ArrayList<>();
    private BakingAdapter bakingAdapter;
    public static final String INTENT_RECIPE = "recipe_main";

    @Nullable private Recipe recipeClicked;
    @Nullable private SimpleIdlingResource idlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource(){
        if(idlingResource == null){
            idlingResource = new SimpleIdlingResource();
        }

        return idlingResource;
    }


    @VisibleForTesting
    @Nullable
    public Recipe getRecipeClicked(){
        return recipeClicked;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        bakingRecyclerView.setLayoutManager(linearLayoutManager);
        bakingRecyclerView.setHasFixedSize(true);
        showProgressBar();

        loadUI();

        bakingAdapter = new BakingAdapter(recipeArrayList, this);
        bakingRecyclerView.setAdapter(bakingAdapter);

        errorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadUI();
            }
        });

    }


    @Override
    public void onItemClick(int recipeIndex) {
        Intent intent = new Intent(this, IngredientsAndStepsActivity.class);
        intent.putExtra(INTENT_RECIPE, (Parcelable) recipeArrayList.get(recipeIndex));
        recipeClicked = recipeArrayList.get(recipeIndex);
        this.startActivity(intent);
    }

    private void loadUI(){
        showProgressBar();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        getIdlingResource();
        if(idlingResource != null){
            idlingResource.setIdleState(false);
        }

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, RECIPE_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int numberOfRecipes = response.length();
                        for(int i=0; i<numberOfRecipes; i++){
                            recipeArrayList.add(JsonUtils.getRecipeFromJson(response, i));
                        }
                        showRecyclerView();
                        bakingAdapter.notifyDataSetChanged();

                        if(idlingResource != null) {
                            idlingResource.setIdleState(true);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showError();

                if(idlingResource != null) {
                    idlingResource.setIdleState(true);
                }
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    private void showRecyclerView(){
        bakingRecyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.INVISIBLE);
        errorButton.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar(){
        bakingRecyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.INVISIBLE);
        errorButton.setVisibility(View.INVISIBLE);
    }

    private void showError(){
        bakingRecyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
        errorButton.setVisibility(View.VISIBLE);
    }
}
