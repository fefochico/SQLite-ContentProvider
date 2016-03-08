package com.notes.gabriel.advancednote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by ysuarez on 3/3/16.
 */
public class AddNoteActivity extends AppCompatActivity {
    EditText etTitle;
    EditText etNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_note_layout);

        ActionBar actionBar= this.getSupportActionBar();
        actionBar.setTitle("New note");

        etTitle= (EditText)findViewById(R.id.etTitle);
        etNote= (EditText)findViewById(R.id.etNote);

        Button btn= (Button) findViewById(R.id.btSave);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishWithResult();
            }
        });

    }

    private void finishWithResult(){
        String strTitle= String.valueOf(etTitle.getText());
        String strNote= String.valueOf(etNote.getText());

        final Intent intent = new Intent();
        intent.putExtra("title", strTitle);
        intent.putExtra("note", strNote);
        setResult(RESULT_OK, intent);
        finish();
    }
}
