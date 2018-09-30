package com.hua.navfloatwindow;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button exit;
    private Button start_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        Context baseContext = getBaseContext();
        Context baseContext1 = ((ContextWrapper) getApplicationContext()).getBaseContext();

        Log.e("@@@hua", "mw1 = " + baseContext);
        Log.e("@@@hua", "mw2 = " + baseContext1);


    }

    private void initView() {
        exit = (Button) findViewById(R.id.exit);
        start_exit = (Button) findViewById(R.id.start_exit);

        exit.setOnClickListener(this);
        start_exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit:
                finish();
                break;
            case R.id.start_exit:
//                useInstrumentation();
//                requestDrawOverLays();
//                startFloatWindow();
                //finish();
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_GRANTED) {
                    startService(new Intent(this, NavWindowFloatService.class));
                } else {
                    //requestPermissions(new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW}, 1);
                }


                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //doStart();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12) {
            if (Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "can draw", Toast.LENGTH_SHORT).show();
                //doStart();
            } else {
                Toast.makeText(this, "can not draw", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("@@@hua", "on keyDown");
        return super.onKeyDown(keyCode, event);
    }

}
