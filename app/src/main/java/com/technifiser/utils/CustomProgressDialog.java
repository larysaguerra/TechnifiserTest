package com.technifiser.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.technifiser.R;

/**
 * Created by guerra on 11/08/17.
 * Custom progress dialog with image.
 */

public class CustomProgressDialog extends Dialog {

    private Context mContext;
    private TextView text_tittle;
    private TextView text_message;
    private ProgressBar progress;

    public CustomProgressDialog(Context context) {
        super(context);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loading);
        text_tittle = (TextView) findViewById(R.id.text_tittle);
        text_message = (TextView) findViewById(R.id.text_message);
        progress = (ProgressBar) findViewById(R.id.progress);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        text_tittle.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        text_tittle.setText(mContext.getResources().getString(titleId));
    }

    public void setMessage(CharSequence message) {
        text_message.setText(message);
        text_message.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public void setMessage(int messageId) {
        text_message.setText(mContext.getResources().getString(messageId));
        text_message.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    public void setProgress(boolean show){
        if(show){
            progress.setVisibility(View.VISIBLE);
        }else {
            progress.setVisibility(View.GONE);
        }

    }
}
