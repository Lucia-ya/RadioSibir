package com.luciaya.radiosibir;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.NumberPicker;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import saschpe.exoplayer2.ext.icy.IcyHttpDataSource;
import saschpe.exoplayer2.ext.icy.IcyHttpDataSourceFactory;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private ImageButton mImageButton;
    private NumberPicker mNumberPicker;
    private MainContract.Presenter mPresenter;
    private static SimpleExoPlayer player_audio;
    private static final String TAG = "MainActivity";
    private static boolean play_audio = false;
    private DefaultDataSourceFactory dataSourceFactory;
    private ExtractorMediaSource audioSource;
    private String[] data = new String[]{"Омск, 103,9 FM", "Красноярск, 95,8 FM", "Томск, 104,6 FM", "Улан Удэ, 106,5 FM", "Чита, 102,6 FM"};
    private String[] uriArr = new String[]{"http://176.120.25.59:8090/omsk2",
            "http://prepros.pifm.ru/kr", "http://stream.radiosibir.ru:8090/HQ",
            "http://92.124.196.44:8000/radiosibiruu", "http://185.108.196.182:8090/HQ"};

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageButton = (ImageButton) findViewById(R.id.imageButton);
        mNumberPicker = (NumberPicker) findViewById(R.id.number_picker);
        mPresenter = new MainPresenterImpl(this);



        mNumberPicker.setMinValue(0);
        mNumberPicker.setMaxValue(data.length-1);
        mNumberPicker.setDisplayedValues(data);
        mNumberPicker.setWrapSelectorWheel(false);

        mNumberPicker.setOnValueChangedListener(onValueChangeListener);

        IcyHttpDataSourceFactory factory = new IcyHttpDataSourceFactory.Builder(Util.getUserAgent(this, getResources().getString(R.string.app_name)))
                .setIcyHeadersListener(new IcyHttpDataSource.IcyHeadersListener() {
                    @Override
                    public void onIcyHeaders(IcyHttpDataSource.IcyHeaders icyHeaders) {
                    }
                })
                .setIcyMetadataChangeListener(new IcyHttpDataSource.IcyMetadataListener() {
                    @Override
                    public void onIcyMetaData(IcyHttpDataSource.IcyMetadata icyMetadata) {
                    }
                }).build();

        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter(); //test
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);
        player_audio = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        dataSourceFactory = new DefaultDataSourceFactory(getApplicationContext(), null, factory);

        audioSource = new ExtractorMediaSource
                (Uri.parse("http://cdn.pifm.ru/mp3"), dataSourceFactory, new DefaultExtractorsFactory(), new Handler(), null);
        player_audio.prepare(audioSource);

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!play_audio) {
                    mPresenter.onPlayBtnClicked();
                } else {
                    mPresenter.onPauseBtnClicked();
                }
            }
        });

        player_audio.addListener(new ExoPlayer.EventListener() {


            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                Log.v(TAG, "Listener-onTracksChanged... ");
            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//                            Log.v(TAG, "Listener-onPlayerStateChanged..." + playbackState+"|||isDrawingCacheEnabled():"+simpleExoPlayerView.isDrawingCacheEnabled());
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                Log.v(TAG, "Listener-onPlayerError...");
                player_audio.stop();
                player_audio.setPlayWhenReady(true);
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
        });
    }



    NumberPicker.OnValueChangeListener onValueChangeListener =
            new 	NumberPicker.OnValueChangeListener(){
                @Override
                public void onValueChange(NumberPicker numberPicker, int i, int i1) {

                    Log.d(TAG, "PickerListener value: " +  String.valueOf(i));
                    Log.d(TAG, "Pred valaue: " + String.valueOf(i1));
                    audioSource = new ExtractorMediaSource
                            (Uri.parse(uriArr[i1]), dataSourceFactory, new DefaultExtractorsFactory(),
                                    new Handler(), null);
                    player_audio.prepare(audioSource);
                    player_audio.setPlayWhenReady(true);
                    if (!play_audio) {
                        player_audio.setPlayWhenReady(false);
                    }
                }
            };


    @Override
    public void play() {
        Log.d(TAG, "play");
        play_audio = true;
        player_audio.prepare(audioSource);
        mImageButton.setImageResource(R.drawable.pause);
        player_audio.setPlayWhenReady(true);
    }

    @Override
    public void pause() {
        play_audio = false;
        Log.d(TAG, "pause");
        mImageButton.setImageResource(R.drawable.play);
        player_audio.setPlayWhenReady(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        player_audio.setPlayWhenReady(false);
    }
}
