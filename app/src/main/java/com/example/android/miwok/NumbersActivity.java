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
import java.util.List;

import static android.R.attr.data;

public class NumbersActivity extends AppCompatActivity {

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

        final List<Word> words = new ArrayList<Word>();


        words.add(new Word("one","lutti", R.drawable.number_one, R.raw.number_one, R.drawable.ic_play_circle_outline_black_24dp));
        words.add(new Word("two","otiiko",R.drawable.number_two, R.raw.number_two, R.drawable.ic_play_circle_outline_black_24dp));
        words.add(new Word("three","tolookosu",R.drawable.number_three, R.raw.number_three, R.drawable.ic_play_circle_outline_black_24dp));
        words.add(new Word("four","oyyisa", R.drawable.number_four, R.raw.number_four, R.drawable.ic_play_circle_outline_black_24dp));
        words.add(new Word("five","massokka",R.drawable.number_five, R.raw.number_five, R.drawable.ic_play_circle_outline_black_24dp));
        words.add(new Word("six","temmokka",R.drawable.number_six, R.raw.number_six, R.drawable.ic_play_circle_outline_black_24dp));
        words.add(new Word("seven","kenekaku",R.drawable.number_seven, R.raw.number_seven, R.drawable.ic_play_circle_outline_black_24dp));
        words.add(new Word("eight","kawinta",R.drawable.number_eight, R.raw.number_eight, R.drawable.ic_play_circle_outline_black_24dp));
        words.add(new Word("nine","wo'e",R.drawable.number_nine, R.raw.number_nine, R.drawable.ic_play_circle_outline_black_24dp));
        words.add(new Word("ten","na'aacha",R.drawable.number_ten, R.raw.number_ten, R.drawable.ic_play_circle_outline_black_24dp));



        WordAdapter wordAdapter = new WordAdapter(this,words, R.color.category_numbers);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(wordAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Word word = words.get(position);
                releaseMediaPlayer();
                mPlayer = MediaPlayer.create(NumbersActivity.this,word.getAudioResourceID());
                boolean gotFocus = requestAudioFocusForMyApp(NumbersActivity.this);
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