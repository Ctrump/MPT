package com.example.cenoo.ms_detect_text;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.io.File;
import android.view.View.OnClickListener;

public class DetectActivity extends Activity implements OnClickListener{

    String textString = null;

    ProgressDialog dialog = null;

    private Button ctn;

    private Button pause;

    private Button stop;

    private MediaPlayer mediaPlayer = new MediaPlayer();

    private int xxj = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect);

        ctn = (Button)findViewById(R.id.ctn);
        pause = (Button)findViewById(R.id.pause);
        stop = (Button)findViewById(R.id.stop);

        ctn.setOnClickListener(this);
        pause.setOnClickListener(this);
        stop.setOnClickListener(this);

        Intent intent = getIntent();

        if ("text/plain".equals(intent.getType())) {
            textString = intent.getStringExtra(Intent.EXTRA_TEXT);
        }

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setMessage("正在为您推荐音乐......");

        new DownloadTask().execute(textString);



    }

    private void initMediaPlayer() {
        try {
            File file = null;
            switch(xxj)
            {
                case 0:file = new File("/storage/emulated/0/music/positive","music1.mp3");break;
                case 1:file = new File("/storage/emulated/0/music/positive","music.mp3");break;
                case 2:file = new File("/storage/emulated/0/music/positive","music2.mp3");break;
                case 3:file = new File("/storage/emulated/0/music/negtive","music.mp3");break;
                case 4:file = new File("/storage/emulated/0/music/negtive","music1.mp3");break;
                case 5:file = new File("/storage/emulated/0/music/negtive","music2.mp3");break;
                default:break;
            }
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
            //mediaPlayer.start();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ctn:
                if(!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
                break;
            case R.id.pause:
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
                break;
            case R.id.stop:
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.reset();
                    initMediaPlayer();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            dialog.dismiss();
            double d = JsonParser.getDouble(s);
            Log.e("ddddddddddddddd",""+d);
            if(d>0.95) xxj = 0;
            else if(d>0.9&&d<=0.95) xxj = 1;
            else if(d>0.5&&d<=0.9) xxj = 2;
            else if(d>0.3&&d<=0.5) xxj = 3;
            else if(d>0.1&&d<=0.3) xxj = 4;
            else if(d<=0.1) xxj = 5;
            initMediaPlayer();
            mediaPlayer.start();
        }

        @Override
        protected String doInBackground(String... strings) {
            return JsonFetch.getJsonString(strings[0]);
        }
    }
}


