package com.pavelprymak.bakingapp.presentation.adapters;

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
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.pavelprymak.bakingapp.presentation.common.Constants.INVALID_STEP_ID;

public class RecipeInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int SHIFT_FOR_STEPS_LIST = 1;

    private List<StepsItem> mSteps;
    private List<IngredientsItem> mIngredients;
    private RecipeStepItemClickListener clickListener;
    private Context mContext;
    private static final int STEP_VH_ID = 1;
    private static final int INGREDIENTS_VH_ID = 2;


    private int mSelectedStepId = INVALID_STEP_ID;


    public RecipeInfoAdapter(RecipeStepItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setSelectedStepId(int stepId) {
        mSelectedStepId = stepId;
        notifyDataSetChanged();
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
        if (mSteps == null) return SHIFT_FOR_STEPS_LIST;
        else return mSteps.size() + SHIFT_FOR_STEPS_LIST;
    }

    class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemViewRecipeStepBinding binding;
        private static final String EMPTY = "";
        private View itemView;

        RecipeStepViewHolder(@NonNull ItemViewRecipeStepBinding stepBinding) {
            super(stepBinding.getRoot());
            this.binding = stepBinding;
            itemView = stepBinding.getRoot();
            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            StepsItem stepsItem = mSteps.get(position - SHIFT_FOR_STEPS_LIST);
            if (stepsItem != null && mContext != null) {
                itemView.setSelected(stepsItem.getId() == mSelectedStepId);
                //Content
                if (stepsItem.getShortDescription() != null) {
                    binding.stepDescription.setText(stepsItem.getShortDescription());
                } else {
                    binding.stepDescription.setText(EMPTY);
                }
                //LOGO
                if (stepsItem.getThumbnailURL() != null && !stepsItem.getThumbnailURL().isEmpty()) {
                    Picasso.get()
                            .load(stepsItem.getThumbnailURL())
                            .error(R.drawable.ic_menu_slideshow)
                            .into(binding.stepLogo);

                } else {
                    binding.stepLogo.setImageResource(R.drawable.ic_menu_slideshow);
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (mSteps != null) {
                StepsItem stepsItem = mSteps.get(getAdapterPosition() - SHIFT_FOR_STEPS_LIST);
                if (stepsItem != null) {
                    mSelectedStepId = stepsItem.getId();
                    notifyDataSetChanged();
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
                binding.ingredientsTv.setText(createIngredientsStr());
            } else {
                binding.ingredientsTv.setText(EMPTY);
            }
        }

        private String createIngredientsStr() {
            StringBuilder ingredientsStringBuilder = new StringBuilder(mContext.getString(R.string.ingredients_label));
            ingredientsStringBuilder.append("\n\n\n");
            for (int i = 0; i < mIngredients.size(); i++) {
                IngredientsItem ingredient = mIngredients.get(i);
                if (ingredient != null) {
                    ingredientsStringBuilder.append(i + 1).append(". ");
                    ingredientsStringBuilder.append(ingredient.getIngredient());
                    ingredientsStringBuilder.append(" ");
                    ingredientsStringBuilder.append(ingredient.getQuantity());
                    ingredientsStringBuilder.append(ingredient.getMeasure());
                    ingredientsStringBuilder.append(";\n");
                }
            }
            return ingredientsStringBuilder.toString();
        }
    }
}

