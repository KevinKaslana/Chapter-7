package com.bytedance.videoplayer;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private static VideoView video;
    private Button button;
    private SeekBar seekBar;
    private TextView tv;
    private static int current = 0;
    private static Bundle outState;

    static Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            refresh();
            handler.postDelayed(runnable,1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (outState != null) {
            current = outState.getInt("key");
        }
        Toast.makeText(this,""+current,Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_main);
        video = findViewById(R.id.video);
        button = findViewById(R.id.button);
        seekBar = findViewById(R.id.bar);
        tv = findViewById(R.id.current);
        video.setVideoPath(getVideoPath(R.raw.bytedance));
        video.seekTo(current);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (button.getText().equals("Start")){
                    video.start();
                    button.setText("Pause");
                }
                else{
                    video.pause();
                    button.setText("Start");
                }
            }
        });
        handler.postDelayed(runnable,0);
        seekBar.setOnSeekBarChangeListener(listener);
    }

    private String getVideoPath(int resId) {
        return "android.resource://" + this.getPackageName() + "/" + resId;
    }

    private void refresh(){
        int current = video.getCurrentPosition();
        int total = video.getDuration();
        seekBar.setProgress((int)((float)current*100.0f/(float)total));
        int total_minute = total / 60000;
        int total_second = (total % 60000) / 1000;
        int current_minute = current / 60000;
        int current_second = (current % 60000) / 1000;
        tv.setText(current_minute+"min"+current_second+"s/"+total_minute+"min"+total_second+"s");
    }

    private SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int current = seekBar.getProgress();
            int total = video.getDuration();
            int video_current = (int)((float)current/100.0f*(float)total);
            video.seekTo(video_current);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        current = video.getCurrentPosition();
        if (outState != null){
            outState.clear();
            outState = null;
        }
        outState = new Bundle();
        outState.putInt("key",current);
    }
}
