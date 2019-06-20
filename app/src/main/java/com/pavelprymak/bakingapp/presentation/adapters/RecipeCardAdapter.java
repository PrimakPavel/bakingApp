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
import com.pavelprymak.bakingapp.data.pojo.RecipeItem;
import com.pavelprymak.bakingapp.databinding.ItemViewRecipeCardBinding;

import java.util.List;

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.RecipeCardViewHolder> {
    private List<RecipeItem> mRecipes;
    private RecipeCardItemClickListener clickListener;
    private Context mContext;


    public RecipeCardAdapter(RecipeCardItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void updateList(List<RecipeItem> reviews) {
        mRecipes = reviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemViewRecipeCardBinding itemViewReviewBinding = DataBindingUtil.inflate(inflater, R.layout.item_view_recipe_card, parent, false);
        return new RecipeCardViewHolder(itemViewReviewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeCardViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mRecipes == null) return 0;
        else return mRecipes.size();
    }

    class RecipeCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ItemViewRecipeCardBinding binding;
        private static final String EMPTY = "";

        RecipeCardViewHolder(@NonNull ItemViewRecipeCardBinding recipeBinding) {
            super(recipeBinding.getRoot());
            this.binding = recipeBinding;
            recipeBinding.getRoot().setOnClickListener(this);
        }

        void bind(int position) {
            RecipeItem recipeItem = mRecipes.get(position);
            if (recipeItem != null && mContext != null) {
                //Content
                if (recipeItem.getName() != null) {
                    binding.recipeTitle.setText(recipeItem.getName());
                } else {
                    binding.recipeTitle.setText(EMPTY);
                }
                if (mContext != null && recipeItem.getIngredients() != null && recipeItem.getIngredients().size() > 0) {
                    binding.recipeIngredients.setText(createIngredientsStr(mContext, recipeItem.getIngredients()));
                } else {
                    binding.recipeIngredients.setText(EMPTY);
                }
            }
        }

        @Override
        public void onClick(View v) {
            if (mRecipes != null) {
                RecipeItem recipeItem = mRecipes.get(getAdapterPosition());
                if (recipeItem != null) {
                    clickListener.onRecipeCardItemClick(recipeItem.getId(), recipeItem.getName());
                }
            }
        }
    }

    public static String createIngredientsStr(@NonNull Context context, @NonNull List<IngredientsItem> ingredients) {
        StringBuilder ingredientsStringBuilder = new StringBuilder(context.getString(R.string.ingredients_label));
        for (int i = 0; i < ingredients.size(); i++) {
            IngredientsItem ingredient = ingredients.get(i);
            if (ingredient != null) {
                ingredientsStringBuilder.append(ingredient.getIngredient());
                if (i < ingredients.size() - 1) {
                    ingredientsStringBuilder.append(", ");
                } else {
                    ingredientsStringBuilder.append(";");
                }
            }
        }
        return ingredientsStringBuilder.toString();
    }
}

