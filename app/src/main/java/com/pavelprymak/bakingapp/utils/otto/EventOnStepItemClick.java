package com.pavelprymak.bakingapp.utils.otto;

public class EventOnStepItemClick {
    private int recipeId;
    private int stepId;

    public EventOnStepItemClick(int recipeId, int stepId) {
        this.recipeId = recipeId;
        this.stepId = stepId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }
}
