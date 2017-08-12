package com.technifiser.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.technifiser.R;
import com.technifiser.TecnifiserActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by guerra on 10/08/17.
 * Home of the application check the permissions to access the location.
 */

public class HomeActivity extends TecnifiserActivity {

    @BindView(R.id.image_home)
    ImageView imageHome;
    @BindView(R.id.text_welcome)
    TextView textWelcome;
    @BindView(R.id.text_permission)
    TextView textPermission;
    @BindView(R.id.button_permission)
    Button buttonPermission;
    @BindView(R.id.button_start)
    Button buttonStart;

    private AnimationDrawable animation;
    private int PERMISSION_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initToolbar();
        initAnimation();
        checkPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        animation.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        animation.stop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                textPermission.setVisibility(View.GONE);
                buttonPermission.setVisibility(View.GONE);
                buttonStart.setVisibility(View.VISIBLE);
            } else {
                textPermission.setVisibility(View.VISIBLE);
                buttonPermission.setVisibility(View.VISIBLE);
                buttonStart.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initAnimation(){
        animation = (AnimationDrawable) imageHome.getBackground();
    }

    private void checkPermission() {
        if (isPermissionGranted(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            textPermission.setVisibility(View.GONE);
            buttonPermission.setVisibility(View.GONE);
            buttonStart.setVisibility(View.VISIBLE);
        } else {
            textPermission.setVisibility(View.VISIBLE);
            buttonPermission.setVisibility(View.VISIBLE);
            buttonStart.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.image_home, R.id.text_about})
    void startAbout() {
        startActivity(new Intent(HomeActivity.this, AboutActivity.class));
    }

    @OnClick(R.id.button_permission)
    void askPermission() {
        askForPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_ACCESS_FINE_LOCATION);
    }

    @OnClick(R.id.button_start)
    void startMap() {
        startActivity(new Intent(HomeActivity.this, MapActivity.class));
    }
}