package com.notes.gabriel.advancednote;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.notes.gabriel.advancednote.R;

/**
 * Created by ysuarez on 3/3/16.
 */
public class ViewNoteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_note_layout);

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            String title = extras.getString("title");
            String body = extras.getString("body");

            TextView tv=(TextView)findViewById(R.id.tvNoteView);
            tv.setText(body);

            ActionBar actionBar= this.getSupportActionBar();
            actionBar.setTitle(title);
        }
    }
}