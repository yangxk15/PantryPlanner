package edu.dartmouth.cs.pantryplanner.app.controller;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.dartmouth.cs.pantryplanner.app.R;

/**
 * Created by litianqi on 3/1/17.
 */

public class CreateRecipeFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_create_recipe, container, false);
    }

}
