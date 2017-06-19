package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

   private MediaPlayer mPlayer;
    String TAG = "AudioFocus";
    AudioManager audioManager;

    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangedListner = new AudioManager.OnAudioFocusChangeListener(){
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    Log.i(TAG, "AUDIOFOCUS_GAIN");
                    // Set volume level to desired levels
                    mPlayer.start();
                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                    Log.i(TAG, "AUDIOFOCUS_GAIN_TRANSIENT");
                    // You have audio focus for a short time
                    mPlayer.start();
                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                    Log.i(TAG, "AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK");
                    // Play over existing audio
                    mPlayer.start();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    Log.e(TAG, "AUDIOFOCUS_LOSS");
                    releaseMediaPlayer();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                    // Temporary loss of audio focus - expect to get it back - you can keep your resources around
                    mPlayer.pause();
                    mPlayer.seekTo(0);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    Log.e(TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    mPlayer.pause();
                    mPlayer.seekTo(0);
                    break;
            }
        }
    };
    private MediaPlayer.OnCompletionListener mCompletionListner = new MediaPlayer.OnCompletionListener(){
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
            Log.i("Media", "Player released");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        final ArrayList<Word> colors = new ArrayList<Word>();

        colors.add(new Word("red","weṭeṭṭi",R.drawable.color_red, R.raw.color_red, R.drawable.ic_play_circle_outline_black_24dp));
        colors.add(new Word("green","chokokki",R.drawable.color_green, R.raw.color_green, R.drawable.ic_play_circle_outline_black_24dp));
        colors.add(new Word("brown","ṭakaakki",R.drawable.color_brown, R.raw.color_brown, R.drawable.ic_play_circle_outline_black_24dp));
        colors.add(new Word("gray","ṭopoppi",R.drawable.color_gray, R.raw.color_gray, R.drawable.ic_play_circle_outline_black_24dp));
        colors.add(new Word("black","kululli",R.drawable.color_black, R.raw.color_black, R.drawable.ic_play_circle_outline_black_24dp));
        colors.add(new Word("white","kelelli",R.drawable.color_white, R.raw.color_white, R.drawable.ic_play_circle_outline_black_24dp));
        colors.add(new Word("dusty yellow","ṭopiisә",R.drawable.color_dusty_yellow, R.raw.color_dusty_yellow, R.drawable.ic_play_circle_outline_black_24dp));
        colors.add(new Word("mustard yellow","chiwiiṭә",R.drawable.color_mustard_yellow, R.raw.color_mustard_yellow, R.drawable.ic_play_circle_outline_black_24dp));

        WordAdapter colorAdapter = new WordAdapter(this,colors, R.color.category_colors);
        ListView colorView = (ListView) findViewById(R.id.list);
        colorView.setAdapter(colorAdapter);

        colorView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Word word = colors.get(position);
                releaseMediaPlayer();
                mPlayer = MediaPlayer.create(ColorsActivity.this,word.getAudioResourceID());

                boolean gotFocus = requestAudioFocusForMyApp(ColorsActivity.this);
                if(gotFocus) {
                    mPlayer.start();
                }
                mPlayer.setOnCompletionListener(mCompletionListner);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mPlayer = null;
            audioManager.abandonAudioFocus(mOnAudioFocusChangedListner);
        }
    }
    private boolean requestAudioFocusForMyApp(final Context context) {

        // Request audio focus for playback
        int result = audioManager.requestAudioFocus(mOnAudioFocusChangedListner,
                // Use the music stream.
                AudioManager.STREAM_MUSIC,
                // Request permanent focus.
                AudioManager.AUDIOFOCUS_GAIN);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.d("AudioFocus", "Audio focus received");
            return true;
        } else {
            Log.d("AudioFocus", "Audio focus NOT received");
            return false;
        }
    }
}
