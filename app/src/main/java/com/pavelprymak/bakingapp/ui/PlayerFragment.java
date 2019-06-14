package com.pavelprymak.bakingapp.ui;


import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.pavelprymak.bakingapp.R;
import com.pavelprymak.bakingapp.databinding.FragmentPlayerBinding;
import com.pavelprymak.bakingapp.utils.player.ExoPlayerHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayerFragment extends Fragment {
    private FragmentPlayerBinding mBinding;
    private ExoPlayerHelper mPlayerHelper;

    public static final String MEDIA_DESCRIPTION = " Recipe introduction";


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_player, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Uri uri = Uri.parse("https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffdc33_-intro-brownies/-intro-brownies.mp4");

        mPlayerHelper = new ExoPlayerHelper(getContext(), mBinding.playerView, mBinding.progressBar);
        mPlayerHelper.initializePlayer(uri);
        mPlayerHelper.setMediaDescriptionText(MEDIA_DESCRIPTION);
    }

    private void setNextVideo(String url, String mediaDescriptionText) {
        if (mPlayerHelper != null) {
            mPlayerHelper.clearResumePosition();
            Uri uri = Uri.parse(url);
            mPlayerHelper.initializePlayer(uri);
            mPlayerHelper.setMediaDescriptionText(mediaDescriptionText);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPlayerHelper.releasePlayer();
        mPlayerHelper = null;
    }
}
