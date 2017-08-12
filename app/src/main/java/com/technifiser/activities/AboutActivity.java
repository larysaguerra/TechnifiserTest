package com.technifiser.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.technifiser.R;
import com.technifiser.TecnifiserActivity;

/**
 * Created by guerra on 11/08/17.
 * Description of the application and contact.
 */

public class AboutActivity extends TecnifiserActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initToolbar();
        TextView text = (TextView) findViewById(R.id.text_contact);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebView(R.string.profile);
            }
        });
    }

    private void initToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

}
