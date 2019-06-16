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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.pavelprymak.bakingapp.R;
import com.pavelprymak.bakingapp.data.pojo.StepsItem;
import com.pavelprymak.bakingapp.databinding.FragmentStepsBinding;
import com.pavelprymak.bakingapp.presentation.viewModels.StepsViewModel;
import com.pavelprymak.bakingapp.utils.activity.ActivityHelper;
import com.pavelprymak.bakingapp.utils.player.ExoPlayerHelper;

import static com.pavelprymak.bakingapp.presentation.common.Constants.INVALID_RECIPE_ID;
import static com.pavelprymak.bakingapp.presentation.common.Constants.INVALID_STEP_ID;

public class StepsFragment extends Fragment {
    private static final String ARG_RECIPE_ID = "argRecipeId";
    private static final String ARG_STEP_ID = "argStepId";

    private int mRecipeId = INVALID_RECIPE_ID;
    private int mStepId = INVALID_STEP_ID;


    private FragmentStepsBinding mBinding;
    private ExoPlayerHelper mPlayerHelper;
    private StepsViewModel mStepsViewModel;
    private static final int FIRST_RECIPE_ID = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(ARG_RECIPE_ID, INVALID_RECIPE_ID);
            mStepId = getArguments().getInt(ARG_STEP_ID, INVALID_STEP_ID);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // WAKE_LOCK ON
        ActivityHelper.setWakeLock(getActivity(), true);
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ActivityHelper.setAppBarVisibility(getActivity(), false);
            ActivityHelper.setFullScreen(getActivity(), true);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_steps, container, false);
        mStepsViewModel = ViewModelProviders.of(this).get(StepsViewModel.class);
        mStepsViewModel.prepareStepsByRecipeId(FIRST_RECIPE_ID);
        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.nextStepBtn.setOnClickListener(v -> showNextStep());
        mBinding.prevStepBtn.setOnClickListener(v -> showPrevStep());

    }

    private void showStepInfo(StepsItem stepItem, boolean resetVideoPosition) {
        if (mPlayerHelper == null) {
            mPlayerHelper = new ExoPlayerHelper(getContext(), mBinding.playerView, mBinding.progressBar);
        }
        String stepVideoUrl = stepItem.getVideoURL();
        mPlayerHelper.stopCurrentVideo();
        if (resetVideoPosition) {
            mPlayerHelper.clearResumePosition();
        }
        if (stepVideoUrl != null && !stepVideoUrl.isEmpty()) {

            Uri uri = Uri.parse(stepVideoUrl);
            mPlayerHelper.initializePlayer(uri);
            if (stepItem.getShortDescription() != null) {
                mPlayerHelper.setMediaDescriptionText(stepItem.getShortDescription());
            }
        } else {
            Toast.makeText(getContext(), R.string.error_video_url, Toast.LENGTH_LONG).show();
        }
        mBinding.descriptionShortTv.setText(stepItem.getShortDescription());
        mBinding.descriptionTv.setText(stepItem.getDescription());
    }

    @Override
    public void onResume() {
        super.onResume();
        //Load current step
        StepsItem currentStepItem = mStepsViewModel.getCurrentStep();
        if (currentStepItem != null) {
            showStepInfo(currentStepItem, false);
        } else {
            Toast.makeText(getContext(), R.string.error_step_load, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPlayerHelper != null) {
            mPlayerHelper.releasePlayer();
            mPlayerHelper = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ActivityHelper.setWakeLock(getActivity(), false);
        ActivityHelper.setAppBarVisibility(getActivity(), true);
        ActivityHelper.setFullScreen(getActivity(), false);
    }


    private void showNextStep() {
        StepsItem nextStep = mStepsViewModel.getNextStep();
        if (nextStep != null) {
            showStepInfo(nextStep, true);
        } else {
            Toast.makeText(getContext(), R.string.error_step_next, Toast.LENGTH_LONG).show();
        }
    }

    private void showPrevStep() {
        StepsItem prevStep = mStepsViewModel.getPrevStep();
        if (prevStep != null) {
            showStepInfo(prevStep, true);
        } else {
            Toast.makeText(getContext(), R.string.error_step_prev, Toast.LENGTH_LONG).show();
        }
    }
}
