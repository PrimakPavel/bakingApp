package com.pavelprymak.bakingapp.presentation.screens;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.bakingapp.R;
import com.pavelprymak.bakingapp.data.pojo.IngredientsItem;
import com.pavelprymak.bakingapp.data.pojo.StepsItem;
import com.pavelprymak.bakingapp.databinding.FragmentRecipeInfoBinding;
import com.pavelprymak.bakingapp.presentation.adapters.RecipeInfoAdapter;
import com.pavelprymak.bakingapp.presentation.adapters.RecipeStepItemClickListener;
import com.pavelprymak.bakingapp.presentation.viewModels.RecipeInfoViewModel;

import java.util.List;

import static com.pavelprymak.bakingapp.presentation.common.Constants.INVALID_RECIPE_ID;


public class RecipeInfoFragment extends Fragment implements RecipeStepItemClickListener {
    public static final String ARG_RECIPE_ID = "argRecipeId";
    private FragmentRecipeInfoBinding mBinding;
    private RecipeInfoViewModel mInfoViewModel;
    private RecipeInfoAdapter mAdapter;
    private NavController mNavController;

    private int mRecipeId = INVALID_RECIPE_ID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(ARG_RECIPE_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInfoViewModel = ViewModelProviders.of(this).get(RecipeInfoViewModel.class);
        if (mRecipeId != INVALID_RECIPE_ID) {
            mInfoViewModel.prepareIngredientsAndSteps(mRecipeId);
        }
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_info, container, false);
        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);
        initRecipeInfoRecyclerView();
        List<IngredientsItem> ingredients = mInfoViewModel.getIngredients();
        List<StepsItem> steps = mInfoViewModel.getSteps();
        if (ingredients != null && steps != null) {
            mAdapter.updateList(steps, ingredients);
        }
    }

    private void initRecipeInfoRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mBinding.recipeInfoRecycler.setLayoutManager(layoutManager);
        mBinding.recipeInfoRecycler.setHasFixedSize(true);
        mAdapter = new RecipeInfoAdapter(this);
        mBinding.recipeInfoRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onRecipeStepItemClick(int recipeId) {
        mNavController.navigate(R.id.stepsFragment);
    }
}
