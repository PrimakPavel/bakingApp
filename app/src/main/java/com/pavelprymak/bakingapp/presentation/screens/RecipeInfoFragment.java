package com.pavelprymak.bakingapp.presentation.screens;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.bakingapp.App;
import com.pavelprymak.bakingapp.MainActivity;
import com.pavelprymak.bakingapp.R;
import com.pavelprymak.bakingapp.data.pojo.RecipeItem;
import com.pavelprymak.bakingapp.data.pojo.StepsItem;
import com.pavelprymak.bakingapp.databinding.FragmentRecipeInfoBinding;
import com.pavelprymak.bakingapp.presentation.adapters.RecipeInfoAdapter;
import com.pavelprymak.bakingapp.presentation.adapters.RecipeStepItemClickListener;
import com.pavelprymak.bakingapp.presentation.viewModels.RecipeInfoViewModel;
import com.pavelprymak.bakingapp.utils.otto.EventOnStepItemClick;
import com.pavelprymak.bakingapp.widget.RecipeUpdateWidgetService;

import java.util.List;

import static com.pavelprymak.bakingapp.presentation.adapters.RecipeInfoAdapter.SHIFT_FOR_STEPS_LIST;
import static com.pavelprymak.bakingapp.presentation.common.Constants.INVALID_RECIPE_ID;
import static com.pavelprymak.bakingapp.presentation.common.Constants.INVALID_STEP_ID;
import static com.pavelprymak.bakingapp.presentation.common.Constants.INVALID_STEP_POSITION;


public class RecipeInfoFragment extends Fragment implements RecipeStepItemClickListener {
    private static final String SAVE_INSTANCE_SELECTED_STEP_ID = "saveInstanceSelectedStepId";
    public static final String ARG_RECIPE_ID = "argRecipeId";
    public static final String ARG_RECIPE_TITLE = "argRecipeTitle";
    private FragmentRecipeInfoBinding mBinding;
    private RecipeInfoViewModel mInfoViewModel;
    private RecipeInfoAdapter mAdapter;
    private NavController mNavController;
    private RecipeItem mRecipeItem;
    private boolean mIsFavorite;
    private Menu mMenu;
    private int mRecipeId = INVALID_RECIPE_ID;
    private String mRecipeTitle;
    private int mSelectedStepId = INVALID_STEP_ID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(ARG_RECIPE_ID);
            mRecipeTitle = getArguments().getString(ARG_RECIPE_TITLE);
        }
        if (savedInstanceState != null) {
            mSelectedStepId = savedInstanceState.getInt(SAVE_INSTANCE_SELECTED_STEP_ID, INVALID_STEP_ID);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mMenu = menu;
        inflater.inflate(R.menu.recipe_info_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.favorite_action) {
            if (mIsFavorite) {
                mInfoViewModel.removeFromFavorite();
            } else {
                mInfoViewModel.addToFavorite();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepareFavoriteIcon(boolean isFavorite) {
        if (mMenu != null) {
            MenuItem menuItem = mMenu.findItem(R.id.favorite_action);
            if (menuItem != null) {
                if (isFavorite) {
                    menuItem.setIcon(R.drawable.ic_favorite_remove);
                } else {
                    menuItem.setIcon(R.drawable.ic_favorite_add);
                }
            }
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        mMenu = menu;
        prepareFavoriteIcon(mIsFavorite);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setHomeBtnEnable(true);
        }
        mInfoViewModel = ViewModelProviders.of(this).get(RecipeInfoViewModel.class);

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_info, container, false);
        // Inflate the layout for this fragment
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNavController = Navigation.findNavController(view);
        initRecipeInfoRecyclerView();

        if (mRecipeTitle != null) {
            setAppBarTitle(mRecipeTitle);
        }
        if (mRecipeId != INVALID_RECIPE_ID) {
            mInfoViewModel.getFavoritesIdList().observe(this, integers -> {
                // update AppWidget
                if (getActivity() != null) {
                    RecipeUpdateWidgetService.startActionUpdateRecipesWidgets(getActivity().getApplicationContext());
                }

                if (integers != null) {
                    mIsFavorite = integers.contains(mRecipeId);
                    prepareFavoriteIcon(mIsFavorite);
                } else {
                    mIsFavorite = false;
                }
            });
            mInfoViewModel.getRecipeItemById(mRecipeId).observe(this, recipeItem -> {
                if (recipeItem != null) {
                    mRecipeItem = recipeItem;

                    if (recipeItem.getIngredients() != null && recipeItem.getSteps() != null) {
                        mAdapter.updateList(recipeItem.getSteps(), recipeItem.getIngredients());
                    }
                    //start first step if tablet and first start
                    if (savedInstanceState == null && getResources().getBoolean(R.bool.isTablet)) {
                        startFirstStep(recipeItem.getSteps());
                    } else {
                        recyclerScrollToSelectedPosition();
                    }
                }
            });

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mInfoViewModel.removeObservers(this);
    }

    private void startFirstStep(List<StepsItem> steps) {
        if (steps != null && steps.size() > 0 && mSelectedStepId == INVALID_STEP_ID) {
            mSelectedStepId = steps.get(0).getId();
            mAdapter.setSelectedStepId(mSelectedStepId);
            App.eventBus.post(new EventOnStepItemClick(mRecipeId, mSelectedStepId));
        }
    }

    private void initRecipeInfoRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mBinding.recipeInfoRecycler.setLayoutManager(layoutManager);
        mBinding.recipeInfoRecycler.setHasFixedSize(true);
        mAdapter = new RecipeInfoAdapter(this);
        mAdapter.setSelectedStepId(mSelectedStepId);
        mBinding.recipeInfoRecycler.setAdapter(mAdapter);
    }


    private void recyclerScrollToSelectedPosition() {
        if (mSelectedStepId != INVALID_STEP_ID) {
            int selectedPosition = mInfoViewModel.getPositionByStepId(mSelectedStepId);
            if (selectedPosition != INVALID_STEP_POSITION) {
                mBinding.recipeInfoRecycler.smoothScrollToPosition(selectedPosition + SHIFT_FOR_STEPS_LIST);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_INSTANCE_SELECTED_STEP_ID, mSelectedStepId);
    }

    @Override
    public void onRecipeStepItemClick(int stepId) {
        mSelectedStepId = stepId;
        if (getResources().getBoolean(R.bool.isTablet)) {
            App.eventBus.post(new EventOnStepItemClick(mRecipeId, stepId));
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(StepsFragment.ARG_RECIPE_ID, mRecipeId);
            bundle.putString(StepsFragment.ARG_RECIPE_TITLE, mRecipeTitle);
            bundle.putInt(StepsFragment.ARG_STEP_ID, stepId);
            mNavController.navigate(R.id.stepsFragment, bundle);
        }
    }

    private void setAppBarTitle(String title) {
        if (getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
        }
    }
}
