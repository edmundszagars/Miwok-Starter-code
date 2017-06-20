package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FamilyActivity extends AppCompatActivity {

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
    public static final List<Word> FAMILY = new ArrayList<Word>();

    static {
        FAMILY.add(new Word("father", "әpә",R.drawable.family_father, R.raw.family_father, R.drawable.ic_play_circle_outline_black_24dp));
        FAMILY.add(new Word("mother", "әṭa",R.drawable.family_mother, R.raw.family_mother, R.drawable.ic_play_circle_outline_black_24dp));
        FAMILY.add(new Word("son", "angsi",R.drawable.family_son, R.raw.family_son, R.drawable.ic_play_circle_outline_black_24dp));
        FAMILY.add(new Word("daughter", "tune",R.drawable.family_daughter, R.raw.family_daughter, R.drawable.ic_play_circle_outline_black_24dp));
        FAMILY.add(new Word("older brother", "taachi",R.drawable.family_older_brother, R.raw.family_older_brother, R.drawable.ic_play_circle_outline_black_24dp));
        FAMILY.add(new Word("younger brother", "younger brother",R.drawable.family_younger_brother, R.raw.family_younger_brother, R.drawable.ic_play_circle_outline_black_24dp));
        FAMILY.add(new Word("older sister", "teṭe",R.drawable.family_older_sister, R.raw.family_older_sister, R.drawable.ic_play_circle_outline_black_24dp));
        FAMILY.add(new Word("younger sister", "kolliti",R.drawable.family_younger_sister, R.raw.family_younger_sister, R.drawable.ic_play_circle_outline_black_24dp));
        FAMILY.add(new Word("grandmother", "ama",R.drawable.family_grandmother, R.raw.family_grandmother, R.drawable.ic_play_circle_outline_black_24dp));
        FAMILY.add(new Word("grandfather", "paapa",R.drawable.family_grandfather, R.raw.family_grandfather, R.drawable.ic_play_circle_outline_black_24dp));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);



        WordAdapter familyAdapter = new WordAdapter(this, FAMILY, R.color.category_family);
        ListView familyView = (ListView) findViewById(R.id.list);
        familyView.setAdapter(familyAdapter);

        familyView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Word word = FAMILY.get(position);
                releaseMediaPlayer();
                mPlayer = MediaPlayer.create(FamilyActivity.this,word.getAudioResourceID());

                boolean gotFocus = requestAudioFocusForMyApp(FamilyActivity.this);
                if(gotFocus) {
                    mPlayer.start();
                }
                mPlayer.setOnCompletionListener(mCompletionListner);
            }
        });

        //Up button
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

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
