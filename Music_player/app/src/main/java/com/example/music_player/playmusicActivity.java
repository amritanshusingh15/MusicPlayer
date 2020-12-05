package com.example.music_player;

import androidx.annotation.ArrayRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class playmusicActivity extends AppCompatActivity {
    Button b_next,b_previous,b_pause;
    TextView songtext;
    SeekBar ssb;
    static MediaPlayer mplayer;
    int pos;
    String sname;
    ArrayList<File> mysongs;
    Thread updateseekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playmusic);

        b_next=(Button)findViewById(R.id.next);
        b_pause=(Button)findViewById(R.id.pause);
        b_previous=(Button)findViewById(R.id.previous);
        songtext=(TextView)findViewById(R.id.songname);
        ssb=(SeekBar)findViewById(R.id.sb1);

        updateseekbar=new Thread(){

            @Override
            public void run(){
                int totalduration=mplayer.getDuration();
                int curposition=0;
                while(curposition<totalduration){
                    try{
                        sleep(500);
                        curposition=mplayer.getCurrentPosition();
                        ssb.setProgress(curposition);
                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }


            }

        };
        if(mplayer!=null){
            mplayer.stop();
            mplayer.release();
        }

        Intent i=getIntent();
        Bundle bundle=i.getExtras();
//        getting songs from mainactivity using "songs" as a key from mainactivity
        mysongs=(ArrayList) bundle.getParcelableArrayList("songs");

//        sname=mysongs.get(pos).getName().toString();
        String songName=i.getStringExtra("songname");
        songtext.setText(songName);
//        songtext.setSelected(true);
        songtext.setTextColor(Color.BLACK);
        // getting position of song from mainactivity
        pos=bundle.getInt("pos",0);
//        Uri(Uniform Resource Identifier) used to identify resources

        Uri u=Uri.parse(mysongs.get(pos).toString());

        mplayer=MediaPlayer.create(getApplicationContext(),u);
        mplayer.start();
        ssb.setMax(mplayer.getDuration());
        updateseekbar.start();

        ssb.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        ssb.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);


        ssb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mplayer.seekTo(seekBar.getProgress());
            }
        });

        b_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ssb.setMax(mplayer.getDuration());

                if(mplayer.isPlaying()){
                    b_pause.setBackgroundResource(R.drawable.icon_play);
                    mplayer.pause();
                }else{
                    b_pause.setBackgroundResource(R.drawable.icon_pause);
                    mplayer.start();
                }
            }
        });

        b_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mplayer.stop();
                mplayer.release();
                pos=((pos+1)%mysongs.size());
                Uri u=Uri.parse(mysongs.get(pos).toString());
                mplayer=MediaPlayer.create(getApplicationContext(),u);
                sname=mysongs.get(pos).getName().toString();
                songtext.setText(sname);
                mplayer.start();
            }
        });
        b_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mplayer.stop();
                mplayer.release();

                pos=((pos-1)<0)?(mysongs.size()-1):(pos-1);
                Uri u=Uri.parse(mysongs.get(pos).toString());
                mplayer=MediaPlayer.create(getApplicationContext(),u);
                sname=mysongs.get(pos).getName().toString();
                songtext.setText(sname);
                mplayer.start();
            }
        });
    }
}