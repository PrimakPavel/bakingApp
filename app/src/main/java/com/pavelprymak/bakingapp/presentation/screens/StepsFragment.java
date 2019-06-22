package com.pavelprymak.bakingapp.presentation.screens;


import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;

import com.pavelprymak.bakingapp.App;
import com.pavelprymak.bakingapp.MainActivity;
import com.pavelprymak.bakingapp.R;
import com.pavelprymak.bakingapp.data.pojo.StepsItem;
import com.pavelprymak.bakingapp.databinding.FragmentStepsBinding;
import com.pavelprymak.bakingapp.presentation.viewModels.StepsViewModel;
import com.pavelprymak.bakingapp.utils.activity.ActivityHelper;
import com.pavelprymak.bakingapp.utils.otto.EventOnStepItemClick;
import com.pavelprymak.bakingapp.utils.player.PlayerHelper;
import com.squareup.otto.Subscribe;

import static com.pavelprymak.bakingapp.presentation.common.Constants.INVALID_RECIPE_ID;
import static com.pavelprymak.bakingapp.presentation.common.Constants.INVALID_STEP_ID;
import static com.pavelprymak.bakingapp.utils.player.PlayerHelper.DEFAULT_RESUME_POSITION;
import static com.pavelprymak.bakingapp.utils.player.PlayerHelper.DEFAULT_RESUME_WINDOW;

public class StepsFragment extends Fragment {
    static final String ARG_RECIPE_ID = "argRecipeId";
    static final String ARG_STEP_ID = "argStepId";
    static final String ARG_RECIPE_TITLE = "argRecipeTitle";
    private static final String SAVE_INSTANCE_RESUME_POSITION = "saveInstanceResumePosition";
    private static final String SAVE_INSTANCE_RESUME_WINDOW = "saveInstanceResumeWindow";
    private int mResumeWindow = DEFAULT_RESUME_WINDOW;
    private long mResumePosition = DEFAULT_RESUME_POSITION;

    private int mRecipeId = INVALID_RECIPE_ID;
    private int mStepId = INVALID_STEP_ID;
    private String mRecipeTitle;
    private Bundle mSaveInstanceSate;


    private FragmentStepsBinding mBinding;
    private PlayerHelper mPlayerHelper;
    private StepsViewModel mStepsViewModel;
    private Toast mToast;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSaveInstanceSate = savedInstanceState;
        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(ARG_RECIPE_ID, INVALID_RECIPE_ID);
            mRecipeTitle = getArguments().getString(ARG_RECIPE_TITLE);
            mStepId = getArguments().getInt(ARG_STEP_ID, INVALID_STEP_ID);
        }
        if (savedInstanceState != null) {
            mResumePosition = savedInstanceState.getLong(SAVE_INSTANCE_RESUME_POSITION, DEFAULT_RESUME_POSITION);
            mResumeWindow = savedInstanceState.getInt(SAVE_INSTANCE_RESUME_WINDOW, DEFAULT_RESUME_WINDOW);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // WAKE_LOCK ON
        ActivityHelper.setWakeLock(getActivity(), true);

        if (getResources().getBoolean(R.bool.isTablet)) return;

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ActivityHelper.setAppBarVisibility(getActivity(), false);
            ActivityHelper.setFullScreen(getActivity(), true);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setHomeBtnEnable(true);
        }
        App.eventBus.register(this);
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps, container, false);
        mStepsViewModel = ViewModelProviders.of(this).get(StepsViewModel.class);
        if (savedInstanceState == null) {
            if (mRecipeId != INVALID_RECIPE_ID) {
                mStepsViewModel.prepareRecipeItemById(mRecipeId);
            }
        }
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mRecipeTitle != null && !mRecipeTitle.isEmpty()) {
            setAppBarTitle(mRecipeTitle);
        }
        mBinding.nextStepBtn.setOnClickListener(v -> showNextStep());
        mBinding.prevStepBtn.setOnClickListener(v -> showPrevStep());
    }

    private void showStepInfo(StepsItem stepItem, int resumeWindow, long resumePosition) {
        if (mPlayerHelper == null) {
            mPlayerHelper = new PlayerHelper(getContext(), mBinding.playerView, mBinding.progressBar);
        }
        String stepVideoUrl = stepItem.getVideoURL();
        mPlayerHelper.stopCurrentVideo();
        mPlayerHelper.setResumePosition(resumeWindow, resumePosition);
        if (stepVideoUrl != null && !stepVideoUrl.isEmpty()) {
            Uri uri = Uri.parse(stepVideoUrl);
            mPlayerHelper.initializePlayer(uri);
            if (stepItem.getShortDescription() != null) {
                mPlayerHelper.setMediaDescriptionText(stepItem.getShortDescription());
            }
        } else {
            showToast(R.string.error_video_url);

        }
        mBinding.descriptionShortTv.setText(stepItem.getShortDescription());
        mBinding.descriptionTv.setText(stepItem.getDescription());
    }

    @Override
    public void onResume() {
        super.onResume();
        //Load current step
        LiveData<StepsItem> currentStepData = mStepsViewModel.getCurrentStepData();
        if (mSaveInstanceSate == null) {
            currentStepData = mStepsViewModel.getCurrentStepDataById(mStepId);
        }
        currentStepData.observe(this, currentStepItem -> {
            mStepsViewModel.removeObserversCurrentStepData(this);
            if (currentStepItem != null) {
                showStepInfo(currentStepItem, mResumeWindow, mResumePosition);
            } else {
                showToast(R.string.error_step_load);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayerHelper != null) {
            mPlayerHelper.releasePlayer();
            mResumeWindow = mPlayerHelper.getCurrentResumeWindow();
            mResumePosition = mPlayerHelper.getCurrentResumePosition();
            mPlayerHelper = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_INSTANCE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(SAVE_INSTANCE_RESUME_POSITION, mResumePosition);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        App.eventBus.unregister(this);
        mStepsViewModel.removeObserversAll(this);
        ActivityHelper.setWakeLock(getActivity(), false);
        ActivityHelper.setAppBarVisibility(getActivity(), true);
        ActivityHelper.setFullScreen(getActivity(), false);
    }

    @Subscribe
    public void onStepItemClick(EventOnStepItemClick event) {
        if (event.getRecipeId() != mRecipeId || event.getStepId() != mStepId) {
            mStepsViewModel.prepareRecipeItemById(event.getRecipeId());
            mStepsViewModel.getCurrentStepDataById(event.getStepId()).observe(this, currentStep -> {
                mStepsViewModel.removeObserversCurrentStepData(this);
                if (currentStep != null) {
                    showStepInfo(currentStep, DEFAULT_RESUME_WINDOW, DEFAULT_RESUME_POSITION);
                }
            });
        }
    }

    private void showNextStep() {
        mStepsViewModel.getNextStepItem().observe(this, nextStep -> {
            mStepsViewModel.removeObserversNextStepData(this);
            if (nextStep != null) {
                showStepInfo(nextStep, DEFAULT_RESUME_WINDOW, DEFAULT_RESUME_POSITION);
            } else {
                showToast(R.string.error_step_next);
            }
        });
    }

    private void showPrevStep() {
        mStepsViewModel.getPrevStepItem().observe(this, prevStep -> {
            mStepsViewModel.removeObserversPrevStepData(this);
            if (prevStep != null) {
                showStepInfo(prevStep, DEFAULT_RESUME_WINDOW, DEFAULT_RESUME_POSITION);
            } else {
                showToast(R.string.error_step_prev);
            }
        });

    }

    private void setAppBarTitle(String title) {
        if (getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
        }
    }

    private void showToast(int messageRes) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(getContext(), messageRes, Toast.LENGTH_LONG);
        mToast.show();
    }
}
