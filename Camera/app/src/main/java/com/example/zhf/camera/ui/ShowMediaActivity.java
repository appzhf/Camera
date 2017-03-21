package com.example.zhf.camera.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.example.zhf.camera.R;

public class ShowMediaActivity extends Activity {
    private RelativeLayout show_media;
    private ImageView image;
    private VideoView video;
    private MediaController mc;//视频控制器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_media);
        int w = getWindowManager().getDefaultDisplay().getWidth();
        int h = getWindowManager().getDefaultDisplay().getHeight();
        show_media = (RelativeLayout) findViewById(R.id.show_media);
        //Intent传过来的数据
        String type = getIntent().getStringExtra("type");
        String path = Environment.getExternalStorageDirectory() + "/" + type;
        if (type.substring(type.indexOf(".")).equals(".jpg")) {
            image = new ImageView(this);
            image.setLayoutParams(new LayoutParams(w, h));
            image.setScaleType(ImageView.ScaleType.FIT_XY);
            Bitmap bm = BitmapFactory.decodeFile(path);
            bm = ThumbnailUtils.extractThumbnail(bm, w, h, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            image.setImageBitmap(bm);
            show_media.addView(image);
        } else if (type.substring(type.indexOf(".")).equals(".mp4")) {
            video = new VideoView(this);
            mc = new MediaController(this);
            video.setVideoPath(path);
            mc.setMediaPlayer(video);
            video.setMediaController(mc);
            show_media.addView(video);
        }
    }
}
