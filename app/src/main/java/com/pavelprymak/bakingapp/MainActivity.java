package com.pavelprymak.bakingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.pavelprymak.bakingapp.presentation.viewModels.MainViewModel;
import com.pavelprymak.bakingapp.widget.RecipeUpdateWidgetService;

import static com.pavelprymak.bakingapp.presentation.common.Constants.INVALID_RECIPE_ID;
import static com.pavelprymak.bakingapp.presentation.screens.RecipeInfoFragment.ARG_RECIPE_ID;
import static com.pavelprymak.bakingapp.presentation.screens.RecipeInfoFragment.ARG_RECIPE_TITLE;

public class MainActivity extends AppCompatActivity implements ShowSnackBarListener {
    public NavController mNavController;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mDrawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        mToggle.setToolbarNavigationClickListener(v -> onBackPressed());
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navigationView, mNavController);
        NavigationUI.setupActionBarWithNavController(this, mNavController, mDrawer);
        if (savedInstanceState == null) {
            implementInputIntent(getIntent());
        }

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.initDbFromNetworkFile();
        mainViewModel.getRecipes().observe(this, recipeEntities -> {
            //TODO
        });
    }


    private void implementInputIntent(Intent intent) {
        if (intent.getExtras() != null) {
            int recipeId = intent.getExtras().getInt(ARG_RECIPE_ID, INVALID_RECIPE_ID);
            if (recipeId != INVALID_RECIPE_ID) {
                String recipeName = intent.getExtras().getString(ARG_RECIPE_TITLE);
                Bundle bundle = new Bundle();
                bundle.putInt(ARG_RECIPE_ID, recipeId);
                bundle.putString(ARG_RECIPE_TITLE, recipeName);
                mNavController.navigate(R.id.recipeInfoFragment, bundle);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public void setHomeBtnEnable(boolean isEnable) {
        mToggle.setDrawerIndicatorEnabled(!isEnable);
        if (!isEnable) {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public void showSnack(int messageRes) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), messageRes, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
