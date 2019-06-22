package com.pavelprymak.bakingapp.presentation.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pavelprymak.bakingapp.App;
import com.pavelprymak.bakingapp.data.Constants;
import com.pavelprymak.bakingapp.utils.RecipeItemToRecipeEntityConverter;
import com.pavelprymak.bakingapp.data.db.RecipeEntity;
import com.pavelprymak.bakingapp.data.pojo.RecipeItem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import timber.log.Timber;

public class MainViewModel extends ViewModel {
    private static final int CONNECTION_TIMEOUT_MS = 60000;// timing out in a minute
    private Executor ioExecutor = App.appExecutors.networkIO();
    private MutableLiveData<Boolean> connectionErrorLiveData = new MutableLiveData<>();

    public LiveData<Boolean> getConnectionErrorData() {
        return connectionErrorLiveData;
    }

    public void initDbFromNetworkFile() {
        connectionErrorLiveData.setValue(false);
        ioExecutor.execute(() -> {
            if (App.dbRepo.loadRecipesCount() == 0)
                saveAllToDb(loadFileFromNetwork());
        });
    }

    public LiveData<List<RecipeEntity>> getRecipes() {
        return App.dbRepo.loadAllRecipes();
    }

    private List<RecipeItem> loadFileFromNetwork() {
        List<RecipeItem> resultRecipes = new ArrayList<>();
        StringBuilder resultStr = new StringBuilder();
        try {
            URL url = new URL(Constants.NETWORK_RES);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(CONNECTION_TIMEOUT_MS);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String str;
            while ((str = in.readLine()) != null) {
                resultStr.append(str);
            }
            in.close();
        } catch (Exception e) {
            connectionErrorLiveData.postValue(true);
            Timber.d(e.toString());
        }
        Timber.d(resultStr.toString());
        try {
            RecipeItem[] recipeItems = new Gson().fromJson(resultStr.toString(), RecipeItem[].class);
            if (recipeItems != null) {
                resultRecipes = Arrays.asList(recipeItems);
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return resultRecipes;
    }

    private void saveAllToDb(List<RecipeItem> recipes) {
        if (recipes != null && recipes.size() > 0) {
            App.dbRepo.insertAllRecipes(RecipeItemToRecipeEntityConverter.convertToRecipeEntityList(recipes));
        }
    }
}
