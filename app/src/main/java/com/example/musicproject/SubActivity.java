package com.example.musicproject;

import android.animation.ObjectAnimator;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

public class SubActivity extends AppCompatActivity {
    DatabaseHelper helper;
    Button button, btnNextSong, btnPreSong, btnSkipNext , btnSkipPre , btnVolume , btnBack;
    ToggleButton btnAdd;
    MediaPlayer player;
    SeekBar sbTime;
    SeekBar sbVolume;
    TextView lbStart , lbEnd;
    ImageView ivSongSub;
    TextView lbSongNameSub , lbSingerNameSub;
    ArrayList<Song> songs;



    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sub);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        helper = new DatabaseHelper(SubActivity.this);

        Intent intent = getIntent();
        Song song= (Song)intent.getSerializableExtra("song");
        songs = (ArrayList<Song>) intent.getSerializableExtra("songs");
        btnSkipPre = findViewById(R.id.btnSkipPre);
        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    helper.addFauSong(song);
                    Toast.makeText(SubActivity.this,"Đã thêm vào playlist",Toast.LENGTH_LONG).show();
                }
            }
        });
        lbStart = findViewById(R.id.lbStart);
        lbEnd = findViewById(R.id.lbEnd);
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation.setAnimation(view);
                finish();
            }
        });
        btnNextSong = findViewById(R.id.btnNextSong);
        btnPreSong = findViewById(R.id.btnPreSong);
        btnSkipNext = findViewById(R.id.btnSkipNext);
        btnSkipNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation.setAnimation(view);
                int curTime = player.getCurrentPosition();
                player.seekTo(curTime + 15000);
                sbTime.setProgress(curTime);
                String cur = getTimeToString(curTime);
                lbStart.setText(cur);
            }
        });
        btnSkipPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation.setAnimation(view);
                int curTime = player.getCurrentPosition();
                player.seekTo(curTime - 15000);
                sbTime.setProgress(curTime);
                String cur = getTimeToString(curTime);
                lbStart.setText(cur);
            }
        });
        btnNextSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation.setAnimation(view);
                for(int i = 0 ; i < songs.size() ; i++){
                    if(songs.get(i).getFileId() == song.getFileId() && i < songs.size()-1){

                        player.stop();
                        setSong(song,songs.get(i+1));
                        player = MediaPlayer.create(SubActivity.this,song.getFileId());
                        player.start();
                        break;
                    }else if(i == songs.size()-1){
                        setSong(song,songs.get(0));
                        player.stop();
                        player = MediaPlayer.create(SubActivity.this,song.getFileId());
                        player.start();
                    }
                }
            }
        });
        btnPreSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation.setAnimation(view);
                for(int i = 0 ; i < songs.size() ; i++){
                    if(songs.get(i).getFileId() == song.getFileId() && i > 0){
                        player.stop();
                        setSong(song,songs.get(i-1));
                        player = MediaPlayer.create(SubActivity.this,song.getFileId());
                        player.start();
                        break;
                    }
                }
            }
        });
        btnVolume = findViewById(R.id.btnVolume);
        sbVolume = findViewById(R.id.sbVolume);
        sbVolume.setProgress(50);
        lbSongNameSub = findViewById(R.id.lbSongNameSub);
        lbSingerNameSub = findViewById(R.id.lbSingerNameSub);
        lbSongNameSub.setText(song.getSongName());
        lbSingerNameSub.setText(song.getSingerName());
        ivSongSub = findViewById(R.id.ivSongSub);
        ivSongSub.setImageResource(song.getImageId());
        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    float volume = i / 100f;
                    player.setVolume(volume,volume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        sbTime = findViewById(R.id.sbTime);
        player = MediaPlayer.create(SubActivity.this, song.getFileId());
        player.start();
        player.setLooping(true);
        player.seekTo(0);
        player.setVolume(0.5f,0.5f);
        String duration = getTimeToString(player.getDuration());
        lbEnd.setText(duration);
        button = findViewById(R.id.btnMusic);
        sbTime.setMax(player.getDuration());
        sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    player.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Animation.setAnimation(view);
                if (!player.isPlaying()) {
                    player.start();
                    button.setBackgroundResource(R.drawable.baseline_pause_circle_24);

                } else {
                    player.pause();
                    button.setBackgroundResource(R.drawable.baseline_play_circle_24);
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(player!=null){
                    if(player.isPlaying()){
                        double position = player.getCurrentPosition();
                        String curTime = getTimeToString((int)position);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lbStart.setText(curTime);
                                sbTime.setProgress((int)position);
                            }
                        });
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }).start();
    }



    private void setSong(Song song, Song song1) {
        song.setImageId(song1.getImageId());
        song.setSongName(song1.getSongName());
        song.setSingerName(song1.getSingerName());
        song.setFileId(song1.getFileId());
        ivSongSub.setImageResource(song.getImageId());
        lbSongNameSub.setText(song.getSongName());
        lbSingerNameSub.setText(song.getSingerName());
    }

    private String getTimeToString(int duration) {
        String time = "";
        int second = (duration / 1000) % 60;
        int minute = (duration / (1000 * 60)) % 60;
        time += minute +":";
        if(second < 10){
            time += "0";
        }
        time += second;
        return time;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
        player = null;
    }
}