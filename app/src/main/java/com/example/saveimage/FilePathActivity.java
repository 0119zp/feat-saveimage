package com.example.saveimage;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author Panpan Zhang
 * @date 2019/6/21 14:01
 *
 * description: 展示存储路径
 */
public class FilePathActivity extends Activity {

    private TextView filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_path);
        initView();
    }

    private void initView() {
        filePath = findViewById(R.id.tv_file_path);
    }
}
