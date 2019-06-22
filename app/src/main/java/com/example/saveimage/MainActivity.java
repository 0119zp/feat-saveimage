package com.example.saveimage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.TextView;
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
    private TextView savePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        savePath = findViewById(R.id.tv_image_save_path);
        AppCompatImageView imageView = findViewById(R.id.iv_save_imageview);
        AppCompatButton saveImage = findViewById(R.id.btn_save_image);
        AppCompatButton startFilePath = findViewById(R.id.btn_start_file_path);
        startFilePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FilePathActivity.class));
            }
        });
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveBitmap == null) {
                    Toast.makeText(MainActivity.this, "未获得图片", Toast.LENGTH_SHORT).show();
                    return;
                }

                String[] mPermissionList = new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                };
                ActivityCompat.requestPermissions(MainActivity.this, mPermissionList, 100);
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

        savePath.setText(getImageCacheDir().getAbsolutePath());
    }

    public void saveImageToAlbum(Bitmap bmp) {
        // 首先保存图片
        File cacheDir = getImageCacheDir();

        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(cacheDir, fileName);
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
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(cacheDir)));
    }

    /**
     * 图片保存路径
     */
    public File getImageCacheDir() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return getExternalFilesDir("");
        }
        return createFile(Environment.getExternalStorageDirectory().getAbsolutePath(), "zpan");
    }

    public File createFile(String folderPath, String fileName) {
        File destDir = new File(folderPath, fileName);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        return destDir;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                boolean writeExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean readExternalStorage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (grantResults.length > 0 && writeExternalStorage && readExternalStorage) {
                    saveImageToAlbum(saveBitmap);
                } else {
                    Toast.makeText(this, "请设置存储权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
