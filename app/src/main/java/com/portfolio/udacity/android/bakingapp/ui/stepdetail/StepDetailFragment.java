package com.portfolio.udacity.android.bakingapp.ui.stepdetail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.portfolio.udacity.android.bakingapp.R;
import com.portfolio.udacity.android.bakingapp.data.model.Recipe;
import com.portfolio.udacity.android.bakingapp.data.model.Step;
import com.portfolio.udacity.android.bakingapp.utils.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * This will contain media player and step instructions. And an up step and down step navigation.
 */
public class StepDetailFragment extends Fragment implements StepDetailContract.ViewStep,
        Player.EventListener {
    public static final String TAG = "stepFragTag";
    private static final String RECIPE_ID = "recipeId";
    private static final String STEP_ID = "stepId";

    private int mRecipeId;
    private int mStepId;

    private StepDetailContract.PresenterStep mPresenterStep;

    private TextView mStepInstructionTV;

    private SimpleExoPlayer mSimpleExoPlayer;
    private SimpleExoPlayerView mSimpleExoPlayerView;
    private ImageView mErrorIV;
    private static MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    //Tracks player position
    private static final String PLAYER_POS = "playerPos";
    private long mPlayerPos;

    public StepDetailFragment() {
    }

    public static StepDetailFragment newInstance(int aRecipeId, int aStepId) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putInt(RECIPE_ID, aRecipeId);
        args.putInt(STEP_ID, aStepId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mRecipeId = savedInstanceState.getInt(RECIPE_ID);
            mStepId = savedInstanceState.getInt(STEP_ID);
            mPlayerPos = savedInstanceState.getLong(PLAYER_POS);
        } else if (getArguments() != null) {
            mRecipeId = getArguments().getInt(RECIPE_ID);
            mStepId = getArguments().getInt(STEP_ID);
            mPlayerPos = 0;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //This prevents below fragments from being touchable
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mStepInstructionTV = view.findViewById(R.id.fragment_step_detail_instruction_tv);
        ImageButton mUpButton = view.findViewById(R.id.fragment_step_detail_up_button);
        if (mUpButton!=null) {
            mUpButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View aView) {
                    mPresenterStep.onUpClick(mRecipeId, mStepId);
                }
            });
        }
        ImageButton mDownButton = view.findViewById(R.id.fragment_step_detail_down_button);
        if (mDownButton!=null) {
            mDownButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View aView) {
                    mPresenterStep.onDownClick(mRecipeId, mStepId);
                }
            });
        }

        mSimpleExoPlayerView = view.findViewById(R.id.fragment_step_detail_exoplayerview);
        mErrorIV = view.findViewById(R.id.fragment_step_detail_player_error_iv);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenterStep.getRecipe(mRecipeId);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenterStep.unSubscribe();
        if (mSimpleExoPlayerView != null && mSimpleExoPlayerView.getPlayer() != null) {
            mSimpleExoPlayerView.getPlayer().release();
        }
    }

    @Override
    public void problemFindingData() {
        Toast.makeText(getActivity(), getString(R.string.problem_finding_data), Toast.LENGTH_SHORT).show();
        showErrorIV();
    }
    private void showErrorIV() {
        mSimpleExoPlayerView.setVisibility(View.GONE);
        mErrorIV.setVisibility(View.VISIBLE);
    }

    @Override
    public void setRecipe(Recipe aRecipe) {
        try {
            Step step = aRecipe.mSteps.get(mStepId);
            if (mStepInstructionTV!=null) {
                mStepInstructionTV.setText(step.mDescription);
            }
            //Set view
            //Note not sure if this is working, defaultArtwork isnt doing anything?
            //... can remove if needs be?
            if (step.mThumbnailURL != null && !step.mThumbnailURL.equals("")) {
                //Loads bitmap into SimpleExoPlayerView.
                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        mSimpleExoPlayerView.setDefaultArtwork(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        mSimpleExoPlayerView.setDefaultArtwork(
                                BitmapFactory.decodeResource(getResources(), R.drawable.ic_image_black_48px));
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                };
                Picasso.with(getContext())
                        .load(step.mThumbnailURL)
                        .placeholder(R.drawable.ic_image_white_48px)
                        .error(R.drawable.ic_error_white_48px)
                        .resize(getContext().getResources().getDimensionPixelSize(R.dimen.thumbnail_size),
                                getContext().getResources().getDimensionPixelSize(R.dimen.thumbnail_size))
                        .into(target);
            } else {
                mSimpleExoPlayerView.setDefaultArtwork(
                        BitmapFactory.decodeResource(getResources(), R.drawable.ic_image_white_48px));
            }
            if (step.mVideoURL!=null&&!step.mVideoURL.equals("")) {
                if (mSimpleExoPlayer == null) {
                    initializeMediaSession();
                    initializePlayer(Uri.parse(step.mVideoURL));
                } else {
                    mSimpleExoPlayer.setPlayWhenReady(true);
                }
            } else {
                mSimpleExoPlayerView.setUseArtwork(true);
                mSimpleExoPlayerView.setDefaultArtwork(
                        BitmapFactory.decodeResource(getResources(), R.drawable.ic_image_white_48px));
                //Since setDefaultArtwork isnt working?
                showErrorIV();
            }
        } catch (Exception e) {
            Utils.logDebug("Error in StepDetailFragment.setRecipe: " + e.getLocalizedMessage());
            Toast.makeText(getActivity(), getString(R.string.problem_setting_data), Toast.LENGTH_SHORT).show();
        }
    }

    private void releasePlayer() {
        if (mSimpleExoPlayer != null) {
            mSimpleExoPlayer.stop();
            mSimpleExoPlayer.release();
            mSimpleExoPlayer = null;
        }
    }
    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() throws Exception {
        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getActivity(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
    }
    /**
     * Initialize ExoPlayer.
     * @param aUrl The Url of the video to play
     */
    private void initializePlayer(Uri aUrl) throws Exception {
        if (mSimpleExoPlayer == null) {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
            mSimpleExoPlayerView.setPlayer(mSimpleExoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            mSimpleExoPlayer.addListener(this);

            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                    Util.getUserAgent(getActivity(), getString(R.string.app_name)));

            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(aUrl);

            mSimpleExoPlayer.prepare(videoSource);
            mSimpleExoPlayer.seekTo(mPlayerPos);
            mSimpleExoPlayer.setPlayWhenReady(true);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == Player.STATE_READY) && playWhenReady) {
            mSimpleExoPlayerView.hideController();
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mSimpleExoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == Player.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mSimpleExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Utils.logDebug("StepDetailFragment.onPlayerError: " + error.getLocalizedMessage());
        Toast.makeText(getActivity(), getString(R.string.problem_loading_video), Toast.LENGTH_SHORT).show();
        showErrorIV();
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            super.onPlay();
            mSimpleExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            mSimpleExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            mSimpleExoPlayer.seekTo(0);
        }
    }

    @Override
    public void setPresenter(StepDetailContract.PresenterStep aPresenter) {
        mPresenterStep = aPresenter;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECIPE_ID, mRecipeId);
        outState.putInt(STEP_ID, mStepId);
        if (mSimpleExoPlayer != null) {
            outState.putLong(PLAYER_POS, mSimpleExoPlayer.getCurrentPosition());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
