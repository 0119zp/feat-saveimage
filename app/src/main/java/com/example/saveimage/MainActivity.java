package com.example.saveimage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author zpan
 */
public class MainActivity extends AppCompatActivity {

    private Bitmap saveBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        AppCompatImageView imageView = findViewById(R.id.iv_save_imageview);
        AppCompatButton saveImage = findViewById(R.id.btn_save_image);
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveBitmap == null) {
                    Toast.makeText(MainActivity.this, "未获得图片", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveImageToAlbum(saveBitmap);
            }
        });

        String imageUrl =
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1561045677571&di=98cc6faa5254d25c8cabcb3b6d2edc72"
                + "&imgtype=0"
                + "&src=http%3A%2F%2Fpic27.nipic.com%2F20130320%2F11295670_210523384101_2.jpg";

        Glide.with(this).load(imageUrl).into(imageView);

        Glide.with(this).asBitmap().load(imageUrl).into(new BitmapImageViewTarget(imageView) {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                super.onResourceReady(resource, transition);
                saveBitmap = resource;
            }
        });
    }

    public void saveImageToAlbum(Bitmap bmp) {
        // 首先保存图片
        File appDir = getExternalCacheDir(this);

        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "FileNotFoundException", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "IOException", Toast.LENGTH_SHORT).show();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), fileName, null);
            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(appDir)));
    }

    /**
     * 图片保存路径
     */
    public File getExternalCacheDir(final Context context) {
        if (hasExternalCacheDir()) {
            return context.getExternalFilesDir("");
        }

        final String cacheDir = "/Android/data/" + context.getPackageName() + "/files/";
        return createFile(Environment.getExternalStorageDirectory().getPath() + cacheDir, "");
    }

    public boolean hasExternalCacheDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public File createFile(String folderPath, String fileName) {
        File destDir = new File(folderPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return new File(folderPath, fileName);
    }
}
