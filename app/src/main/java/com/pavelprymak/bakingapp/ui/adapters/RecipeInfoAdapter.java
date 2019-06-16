package com.pavelprymak.bakingapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.pavelprymak.bakingapp.R;
import com.pavelprymak.bakingapp.data.pojo.IngredientsItem;
import com.pavelprymak.bakingapp.data.pojo.StepsItem;
import com.pavelprymak.bakingapp.databinding.ItemViewRecipeIngredientsBinding;
import com.pavelprymak.bakingapp.databinding.ItemViewRecipeStepBinding;

import java.util.List;

public class RecipeInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<StepsItem> mSteps;
    private List<IngredientsItem> mIngredients;
    private RecipeStepItemClickListener clickListener;
    private Context mContext;
    private static final int STEP_VH_ID = 1;
    private static final int INGREDIENTS_VH_ID = 2;


    public RecipeInfoAdapter(RecipeStepItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void updateList(List<StepsItem> steps, List<IngredientsItem> ingredients) {
        mSteps = steps;
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        switch (viewType) {
            case INGREDIENTS_VH_ID:
                ItemViewRecipeIngredientsBinding itemViewRecipeIngredientsBinding = DataBindingUtil.inflate(inflater, R.layout.item_view_recipe_ingredients, parent, false);
                return new RecipeIngredientsViewHolder(itemViewRecipeIngredientsBinding);
            case STEP_VH_ID: {
                ItemViewRecipeStepBinding itemViewRecipeStepBinding = DataBindingUtil.inflate(inflater, R.layout.item_view_recipe_step, parent, false);
                return new RecipeStepViewHolder(itemViewRecipeStepBinding);
            }
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return INGREDIENTS_VH_ID;
        return STEP_VH_ID;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case INGREDIENTS_VH_ID:
                RecipeIngredientsViewHolder recipeIngredientsViewHolder = (RecipeIngredientsViewHolder) holder;
                recipeIngredientsViewHolder.bind();
                break;
            case STEP_VH_ID:
                RecipeStepViewHolder recipeStepViewHolder = (RecipeStepViewHolder) holder;
                recipeStepViewHolder.bind(position);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mSteps == null) return 1;
        else return mSteps.size() + 1;
    }

    class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemViewRecipeStepBinding binding;
        private static final String EMPTY = "";

        RecipeStepViewHolder(@NonNull ItemViewRecipeStepBinding stepBinding) {
            super(stepBinding.getRoot());
            this.binding = stepBinding;
            stepBinding.getRoot().setOnClickListener(this);
        }

        void bind(int position) {
            StepsItem stepsItem = mSteps.get(position - 1);
            if (stepsItem != null && mContext != null) {
                //Content
                if (stepsItem.getShortDescription() != null) {
                    binding.stepDescription.setText(stepsItem.getShortDescription());
                } else {
                    binding.stepDescription.setText(EMPTY);
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (mSteps != null) {
                StepsItem stepsItem = mSteps.get(getAdapterPosition());
                if (stepsItem != null) {
                    clickListener.onRecipeStepItemClick(stepsItem.getId());
                }
            }
        }
    }

    class RecipeIngredientsViewHolder extends RecyclerView.ViewHolder {
        private final ItemViewRecipeIngredientsBinding binding;
        private static final String EMPTY = "";

        RecipeIngredientsViewHolder(@NonNull ItemViewRecipeIngredientsBinding ingredientsBinding) {
            super(ingredientsBinding.getRoot());
            this.binding = ingredientsBinding;
        }

        void bind() {
            //Content
            if (mIngredients != null && mIngredients.size() > 0) {
                binding.ingredientsTv.setText(mIngredients.toString());
            } else {
                binding.ingredientsTv.setText(EMPTY);
            }
        }

    }
}

