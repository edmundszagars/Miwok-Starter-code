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

import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {

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

        final ArrayList<Word> phrases = new ArrayList<Word>();

        phrases.add(new Word("Where are you going?","minto wuksus", R.raw.phrase_where_are_you_going, R.drawable.ic_play_circle_outline_black_24dp));
        phrases.add(new Word("What is your name?","tinnә oyaase'nә", R.raw.phrase_what_is_your_name, R.drawable.ic_play_circle_outline_black_24dp));
        phrases.add(new Word("My name is...","oyaaset...", R.raw.phrase_my_name_is, R.drawable.ic_play_circle_outline_black_24dp));
        phrases.add(new Word("How are you feeling?","michәksәs?", R.raw.phrase_how_are_you_feeling, R.drawable.ic_play_circle_outline_black_24dp));
        phrases.add(new Word("I’m feeling good.","kuchi achit", R.raw.phrase_im_feeling_good, R.drawable.ic_play_circle_outline_black_24dp));
        phrases.add(new Word("Are you coming?","әәnәs'aa?", R.raw.phrase_are_you_coming, R.drawable.ic_play_circle_outline_black_24dp));
        phrases.add(new Word("Yes, I’m coming.","hәә’ әәnәm", R.raw.phrase_yes_im_coming, R.drawable.ic_play_circle_outline_black_24dp));
        phrases.add(new Word("I’m coming.","әәnәm", R.raw.phrase_im_coming, R.drawable.ic_play_circle_outline_black_24dp));
        phrases.add(new Word("Let’s go.","yoowutis", R.raw.phrase_lets_go, R.drawable.ic_play_circle_outline_black_24dp));
        phrases.add(new Word("Come here.","әnni'nem", R.raw.phrase_come_here, R.drawable.ic_play_circle_outline_black_24dp));


        WordAdapter phrasesAdapter = new WordAdapter(this,phrases, R.color.category_phrases);
        ListView phrasesView = (ListView) findViewById(R.id.list);
      phrasesView.setAdapter(phrasesAdapter);

        phrasesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Word word = phrases.get(position);
                releaseMediaPlayer();
                mPlayer = MediaPlayer.create(PhrasesActivity.this,word.getAudioResourceID());

                boolean gotFocus = requestAudioFocusForMyApp(PhrasesActivity.this);
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
