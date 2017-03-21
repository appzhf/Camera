package com.example.zhf.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.zhf.camera.ui.ShowMediaActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener {
    private Context context;
    private Button camera, video;
    private ImageView img;
    private SimpleDateFormat sdf;
    public static final int RESULT = 0;
    private int index = 0;
    private String name;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    //初始化View
    private void initView() {
        context = MainActivity.this;
        camera = (Button) findViewById(R.id.camera);
        video = (Button) findViewById(R.id.video);
        img = (ImageView) findViewById(R.id.img);

        //添加OnClick监听时间
        camera.setOnClickListener(this);
        video.setOnClickListener(this);
        img.setOnClickListener(this);
        sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    }

    @Override
    public void onClick(View v) {
        name = sdf.format(new Date());
        switch (v.getId()) {
            case R.id.camera:
                //照相
                index = 0;
                setMedia(MediaStore.ACTION_IMAGE_CAPTURE, index);
                break;
            case R.id.video:
                //录像
                index = 1;
                setMedia(MediaStore.ACTION_VIDEO_CAPTURE, index);
                break;
            case R.id.img:
                if (null != name) {
                   startActivity(new Intent(context, ShowMediaActivity.class).putExtra("type",type));
                } else {
                    show("请拍照或录像");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT) {
            Bitmap bm = null;
            if (index == 0) {
                bm = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + "/" + type);
            } else if (index == 1) {
                String path = Environment.getExternalStorageDirectory() + "/" + type;
                //获取视频的第一帧图片并显示在一个ImageView中显示
                MediaMetadataRetriever media = new MediaMetadataRetriever();
                media.setDataSource(path);
                bm = media.getFrameAtTime();
            }
            bm = ThumbnailUtils.extractThumbnail(bm, 50, 50, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            img.setImageBitmap(bm);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setMedia(String mediaStore, int index) {
        String SDState = Environment.getExternalStorageState();
        Uri uri = null;
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            if (index == 0) {
                type = name + ".jpg";
                //存储文件路径
                uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + type));
            } else if (index == 1) {
                type = name + ".mp4";
                //存储文件路径
                uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/" + type));
            }
            Intent intent = new Intent(mediaStore);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, RESULT);
        } else {
            show("内存卡不存在!");
        }
    }

    //显示Toast
    private void show(String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
