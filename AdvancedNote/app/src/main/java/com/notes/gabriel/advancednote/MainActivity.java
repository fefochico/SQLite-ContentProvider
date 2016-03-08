package com.notes.gabriel.advancednote;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import com.notes.gabriel.advancednote.provider.AccessProvider;
import com.notes.gabriel.advancednote.provider.DatabaseHelper;
import com.notes.gabriel.advancednote.provider.NotesContentProvider;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private CustomAdapter customAdapter;

    private ArrayList<ObjectData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list= new ArrayList<ObjectData>();

        final Intent intent = new Intent(this, AddNoteActivity.class);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(intent,1);
            }
        });

        listView = (ListView) findViewById(R.id.lvList);
        customAdapter = new CustomAdapter(getApplicationContext());


        fillData();

        if (!list.isEmpty()){
            for (ObjectData objectData : list) {
                customAdapter.add(objectData);
            }
        }
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentAux = new Intent(getApplicationContext(), ViewNoteActivity.class);
                intentAux.putExtra("title", list.get(position).title);
                intentAux.putExtra("body", list.get(position).body);
                startActivity(intentAux);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                customAdapter.delete(position);
                deleteData((int) list.get(position).id);
                list.remove(position);
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode==RESULT_OK){
            String title= data.getStringExtra("title");
            String note= data.getStringExtra("note");
            String date= getDatePhone();

            ObjectData objectData= new ObjectData();
            objectData.date=date;
            objectData.title=title;
            objectData.body=note;
            addNote(objectData);
        }
    }


    private void addNote(ObjectData objectData){
        addData(objectData);
        customAdapter.addNew(objectData);
        list.add(objectData);
    }

    private String getDatePhone(){
        Calendar cal= new GregorianCalendar();
        Date date= cal.getTime();
        SimpleDateFormat df= new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate= df.format(date);
        return formatteDate;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private static final String[] PROVIDER_PROJECTION = new String[] {AccessProvider.Columnas.ID,
            AccessProvider.Columnas.DATE, AccessProvider.Columnas.TITLE, AccessProvider.Columnas.BODY};


    public void fillData(){

        ContentResolver cr= getContentResolver();
        Cursor c = cr.query(AccessProvider.CONTENT_URI, PROVIDER_PROJECTION, null, null, null);
        if(c!=null){
            if (c.moveToFirst()) {
                while(!c.isAfterLast()){
                    ObjectData objectData= new ObjectData();
                    objectData.id= c.getInt(c.getColumnIndexOrThrow(AccessProvider.Columnas.ID));
                    objectData.date= c.getString(c.getColumnIndexOrThrow(AccessProvider.Columnas.DATE));
                    objectData.title=c.getString(c.getColumnIndexOrThrow(AccessProvider.Columnas.TITLE));
                    objectData.body=c.getString(c.getColumnIndexOrThrow(AccessProvider.Columnas.BODY));
                    list.add(objectData);
                    c.moveToNext();
                }
                c.close();
            }
        }
    }

    public void addData(ObjectData objectData){

        ContentValues v= new ContentValues(3);
        v.put(AccessProvider.Columnas.DATE, objectData.date);
        v.put(AccessProvider.Columnas.TITLE, objectData.title);
        v.put(AccessProvider.Columnas.BODY, objectData.body);
        ContentResolver cr= getContentResolver();

        cr.insert(AccessProvider.CONTENT_URI, v);

    }

    public void deleteData(int id){
        ContentResolver cr=getContentResolver();

        cr.delete(AccessProvider.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build(),null, null);
    }


}
