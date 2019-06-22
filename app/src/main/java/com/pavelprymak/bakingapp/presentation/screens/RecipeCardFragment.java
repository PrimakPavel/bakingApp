package com.pavelprymak.bakingapp.presentation.screens;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.bakingapp.MainActivity;
import com.pavelprymak.bakingapp.R;
import com.pavelprymak.bakingapp.data.pojo.RecipeItem;
import com.pavelprymak.bakingapp.databinding.FragmentRecipeCardBinding;
import com.pavelprymak.bakingapp.presentation.adapters.RecipeCardAdapter;
import com.pavelprymak.bakingapp.presentation.adapters.RecipeCardItemClickListener;
import com.pavelprymak.bakingapp.presentation.viewModels.RecipeCardViewModel;

import java.util.List;

import static com.pavelprymak.bakingapp.presentation.screens.RecipeInfoFragment.ARG_RECIPE_ID;
import static com.pavelprymak.bakingapp.presentation.screens.RecipeInfoFragment.ARG_RECIPE_TITLE;


public class RecipeCardFragment extends Fragment implements RecipeCardItemClickListener {
    private RecipeCardAdapter mAdapter;
    private FragmentRecipeCardBinding mBinding;
    private RecipeCardViewModel mRecipeCardViewModel;
    private NavController mNavController;


    public RecipeCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setHomeBtnEnable(false);
        }
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_card, container, false);
        mRecipeCardViewModel = ViewModelProviders.of(this).get(RecipeCardViewModel.class);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mNavController = Navigation.findNavController(view);
        super.onViewCreated(view, savedInstanceState);
        initRecipeCardRecyclerView();
        mRecipeCardViewModel.getRecipes().observe(this, recipes -> {
            if (recipes != null) {
                mAdapter.updateList(recipes);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecipeCardViewModel.getRecipes().removeObservers(this);
    }

    private void initRecipeCardRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.list_colons_num), RecyclerView.VERTICAL, false);
        mBinding.recipeRecycler.setLayoutManager(layoutManager);
        mBinding.recipeRecycler.setHasFixedSize(true);
        mAdapter = new RecipeCardAdapter(this);
        mBinding.recipeRecycler.setAdapter(mAdapter);
    }

    @Override
    public void onRecipeCardItemClick(int recipeId, String recipeTitle) {
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_RECIPE_ID, recipeId);
        bundle.putString(ARG_RECIPE_TITLE, recipeTitle);
        mNavController.navigate(R.id.recipeInfoFragment, bundle);
    }
}
