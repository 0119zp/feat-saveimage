package com.example.saveimage;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import java.io.File;

/**
 * @author Panpan Zhang
 * @date 2019/6/21 14:01
 *
 * description: 展示存储路径
 */
public class FilePathActivity extends Activity {

    private TextView intornalFilePath;
    private TextView extornalFilePath;
    private TextView systemFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_path);
        initView();
    }

    private void initView() {
        intornalFilePath = findViewById(R.id.tv_intornal_file_path);
        extornalFilePath = findViewById(R.id.tv_extornal_file_path);
        systemFilePath = findViewById(R.id.tv_system_file_path);

        setInternalStorage();
        setExternalStorage();
        setSystemStorage();
    }

    private void setInternalStorage() {
        StringBuffer sb = new StringBuffer();
        sb.append("1.Environment.getDataDirectory()\n=").append(Environment.getDataDirectory()).append("\n");
        sb.append("2.getFilesDir().getAbsolutePath()\n=").append(getFilesDir().getAbsolutePath()).append("\n");
        sb.append("3.getCacheDir().getAbsolutePath()\n=").append(getCacheDir().getAbsolutePath()).append("\n");
        sb.append("4.getDir(“saveFile”, MODE_PRIVATE).getAbsolutePath()\n=")
            .append(getDir("saveFile", MODE_PRIVATE).getAbsolutePath())
            .append("\n");
        intornalFilePath.setText(sb.toString());
    }

    private void setExternalStorage() {
        // 遍历外部存储路径
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            File[] files = getExternalFilesDirs(Environment.MEDIA_MOUNTED);
            for (File file : files) {
                Log.e("zpan", file.getAbsolutePath());
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append("1.Environment.getExternalStorageDirectory().getAbsolutePath()\n=")
            .append(Environment.getExternalStorageDirectory().getAbsolutePath())
            .append("\n");
        sb.append("2.Environment.getExternalStoragePublicDirectory(“”).getAbsolutePath()\n=")
            .append(Environment.getExternalStoragePublicDirectory("").getAbsolutePath())
            .append("\n");
        sb.append("3.getExternalFilesDir(“”).getAbsolutePath()\n=").append(getExternalFilesDir("").getAbsolutePath()).append("\n");
        sb.append("4.getExternalCacheDir().getAbsolutePath()\n=").append(getExternalCacheDir().getAbsolutePath()).append("\n");
        extornalFilePath.setText(sb.toString());

        Log.e("zpan", "Environment.getExternalStorageDirectory().getAbsolutePath()");
        Log.e("zpan", Environment.getExternalStorageDirectory().getAbsolutePath());
        Log.e("zpan", "Environment.getExternalStoragePublicDirectory(\"\").getAbsolutePath()");
        Log.e("zpan", Environment.getExternalStoragePublicDirectory("").getAbsolutePath());
        Log.e("zpan", "getExternalFilesDir(\"\").getAbsolutePath()");
        Log.e("zpan", getExternalFilesDir("").getAbsolutePath());
        Log.e("zpan", "getExternalCacheDir().getAbsolutePath()");
        Log.e("zpan", getExternalCacheDir().getAbsolutePath());
    }

    private void setSystemStorage() {
        StringBuffer sb = new StringBuffer();
        sb.append("1.Environment.getDownloadCacheDirectory()\n=").append(Environment.getDownloadCacheDirectory()).append("\n");
        sb.append("2.Environment.getRootDirectory()\n=").append(Environment.getRootDirectory()).append("\n");
        systemFilePath.setText(sb.toString());
    }
}
