package com.example.baked;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.baked.Model.Recipe;
import com.example.baked.Model.Step;
import com.example.baked.Utils.RecipeViewModel;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static android.view.View.GONE;

public class StepDetailsFragment extends Fragment {

    private final String KEY_PLAYER_POSITION = "player_position";
    private final String KEY_PLAYER_PLAY_WHEN_READY = "play_when_ready";

    private Context mContext;
    private Recipe mRecipe;
    private Step mStep;
    private String mVideoURL;
    private String mThumbnailURL;
    private SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private int mCountSteps;
    private RecipeViewModel mViewModel;
    private long mPlayerPosition = 0;
    private boolean mPlayerPlayWhenReady = false;

    public StepDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(requireActivity()).get(RecipeViewModel.class);
        mRecipe = mViewModel.getCurrentRecipe();
        mStep = mViewModel.getCurrentStep();
        mCountSteps = mRecipe.getSteps().size();
        mPlayerPosition = mViewModel.getPlayerPosition();
        mPlayerPlayWhenReady = mViewModel.getPlayerPlayWhenReady();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        mContext = getContext();

        if (mStep == null) {
            mStep = mRecipe.getSteps().get(0);
        }

        int stepId = mStep.getId();

        mVideoURL = mStep.getVideoURL();
        mThumbnailURL = mStep.getThumbnailURL();
        mPlayerView = rootView.findViewById(R.id.step_video);
        LinearLayout mediaLayout = rootView.findViewById(R.id.recipe_media_layout);
        ImageView thumbnailImageView = rootView.findViewById(R.id.step_thumbnail);
        setUpMediaViews(mediaLayout, thumbnailImageView);

        String shortDescription = mStep.getShortDescription();
        String description = mStep.getDescription();
        TextView shortDescriptionTextView = rootView.findViewById(R.id.step_short_description);
        shortDescriptionTextView.setText(shortDescription);
        TextView descriptionTextView = rootView.findViewById(R.id.step_description);
        descriptionTextView.setText(description);

        setUpNavButtons(rootView, stepId);

        return rootView;
    }

    private void setUpMediaViews(LinearLayout mediaLayout, ImageView thumbnailImageView) {
        if (!mVideoURL.equals("")) {
            mPlayerView.setVisibility(View.VISIBLE);
            setUpPlayer();
        } else {
            mPlayerView.setVisibility(GONE);
        }
        if (!mThumbnailURL.equals("")) {
            thumbnailImageView.setVisibility(View.VISIBLE);
        } else {
            thumbnailImageView.setVisibility(GONE);
        }
        if ((!mVideoURL.equals("") || !mThumbnailURL.equals("")) && mediaLayout != null) {
            mediaLayout.setVisibility(View.VISIBLE);
        }
    }

    private void setUpPlayer() {
        if (mExoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.setVideoScalingMode(C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
            String userAgent = Util.getUserAgent(mContext, "Baked");
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mVideoURL),
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);
            mExoPlayer.prepare(mediaSource);
            if (mPlayerPosition != 0) {
                mExoPlayer.seekTo(mPlayerPosition);
                mExoPlayer.setPlayWhenReady(mPlayerPlayWhenReady);
            }
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mExoPlayer != null) {
            releasePlayer();
        }
    }

    private void setUpNavButtons(View rootView, int stepId) {
        if (stepId < mCountSteps - 1) {
            Button nextButton = rootView.findViewById(R.id.next_step_button);
            if (nextButton != null) {
                Recipe finalRecipe = mRecipe;
                nextButton.setOnClickListener(view -> goToNextStep(stepId, finalRecipe));
                nextButton.setVisibility(View.VISIBLE);
            }
        }
        if (stepId > 0) {
            Button previousButton = rootView.findViewById(R.id.previous_step_button);
            if (previousButton != null) {
                Recipe finalRecipe = mRecipe;
                previousButton.setOnClickListener(view -> goToPreviousStep(stepId, finalRecipe));
                previousButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void goToNextStep(int stepId, Recipe recipe) {
        mViewModel.setCurrentStep(recipe.getSteps().get(stepId + 1));
        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_container, stepDetailsFragment)
                .commit();

        StepDetailsActivity activity = (StepDetailsActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.step, String.valueOf(stepId + 1)));
    }

    private void goToPreviousStep(int stepId, Recipe recipe) {
        mViewModel.setCurrentStep(recipe.getSteps().get(stepId - 1));
        StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_container, stepDetailsFragment)
                .commit();

        StepDetailsActivity activity = (StepDetailsActivity) getActivity();
        ActionBar actionBar = activity.getSupportActionBar();
        if (stepId == 1) {
            actionBar.setTitle(getResources().getString(R.string.introduction));
        } else {
            actionBar.setTitle(getResources().getString(R.string.step, String.valueOf(stepId + 1)));
        }
    }

    @Override
    public void onDestroyView() {
        if (mExoPlayer != null) {
            mViewModel.setPlayerPosition(mExoPlayer.getContentPosition());
            mViewModel.setPlayerPlayWhenReady(mExoPlayer.getPlayWhenReady());
        }
        super.onDestroyView();
    }
}