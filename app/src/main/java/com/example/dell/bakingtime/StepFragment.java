package com.example.dell.bakingtime;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.example.dell.bakingtime.Recipe.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;


public class StepFragment extends Fragment{

    @BindView(R.id.description_text_view) TextView descriptionTextView;
    @BindView(R.id.step_previous_button) Button stepPreviousButton;
    @BindView(R.id.step_next_button) Button stepNextButton;
    @BindView(R.id.player_view) PlayerView playerView;

    private int position;
    private Step step;
    private OnButtonClickListenerStep activityCallback;
    private boolean isLastStep;
    private SimpleExoPlayer player;
    private static long playbackPosition = 0;
    private static int currentWindow = 0;
    private static boolean playWhenReady = true;
    private MediaSessionCompat mediaSessionCompat;
    private ComponentListener componentListener = new ComponentListener();

    public final static String TAG = StepFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step, container, false);

        ButterKnife.bind(this, view);

        descriptionTextView.setText(step.getLongDescription());


        stepPreviousButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                activityCallback.onButtonClickStep(position-1);
            }
        });


        if(isLastStep) {
            stepNextButton.setEnabled(false);
        }
        else {
            stepNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    releasePlayer();
                    activityCallback.onButtonClickStep(position + 1);
                }
            });
        }

        initializePlayer();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            activityCallback = (OnButtonClickListenerStep) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnButtonClickListenerStep");
        }

        mediaSessionCompat = new MediaSessionCompat(getContext(), TAG);

        mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS);

        mediaSessionCompat.setMediaButtonReceiver(null);

        PlaybackStateCompat.Builder playbackSessionCompatBuilder = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_PAUSE
                        | PlaybackStateCompat.ACTION_PLAY_PAUSE
                        | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);

        mediaSessionCompat.setPlaybackState(playbackSessionCompatBuilder.build());

        mediaSessionCompat.setCallback(new MySessionCallback());

        mediaSessionCompat.setActive(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
        if(mediaSessionCompat != null) {
            mediaSessionCompat.setActive(false);
        }
    }

    public void setStep(Step step){
        this.step = step;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public void setIsLastStep(boolean isLastStep){
        this.isLastStep = isLastStep;
    }


    public void initializePlayer(){
        if(player == null){
            Uri uri;
            if(step.getVideoUrl() != null && !step.getVideoUrl().equals("")) {
                uri = Uri.parse(step.getVideoUrl());
            }
            else if(step.getThumbnailUrl() != null){
                uri = Uri.parse(step.getThumbnailUrl());
            }
            else{

                //TODO add placeholder
                return;
            }

            player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            playerView.setPlayer(player);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
            player.addListener(componentListener);


            MediaSource mediaSource = new ExtractorMediaSource.Factory(
                    new DefaultHttpDataSourceFactory("exoplayer")).createMediaSource(uri);

            player.prepare(mediaSource, true, false);
        }
    }

    public void releasePlayer(){
        if(player != null){
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            //player.stop();
            player.removeListener(componentListener);
            player.release();
            player = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(Util.SDK_INT > 23){
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Util.SDK_INT <= 23 || player == null){
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(Util.SDK_INT > 23){
            releasePlayer();
        }
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            player.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            player.setPlayWhenReady(false);
        }
    }


    private class ComponentListener extends Player.DefaultEventListener{
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            if((playbackState == Player.STATE_READY) && playWhenReady){
                PlaybackStateCompat.Builder playbackStateCompatBuilder = new PlaybackStateCompat.Builder()
                        .setState(PlaybackStateCompat.STATE_PLAYING, playbackPosition, 1f);
                mediaSessionCompat.setPlaybackState(playbackStateCompatBuilder.build());

            }
            else if(playbackState == Player.STATE_READY){
                PlaybackStateCompat.Builder playbackStateCompatBuilder = new PlaybackStateCompat.Builder()
                        .setState(PlaybackStateCompat.STATE_PAUSED, playbackPosition, 1f);
                mediaSessionCompat.setPlaybackState(playbackStateCompatBuilder.build());
            }
        }
    }

    public interface OnButtonClickListenerStep{
        void onButtonClickStep(int position);
    }
}


//TODO ExoPlayer (almost done!)
//TODO Widget
//TODO Espresso
//TODO UI