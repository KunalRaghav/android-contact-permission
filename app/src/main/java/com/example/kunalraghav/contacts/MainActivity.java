package com.example.kunalraghav.contacts;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final int REQUEST_CODE_READ_CONTACTS = 1;
    public static boolean READ_CONTACTS_GRANTED = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        SQLiteDatabase sqLiteDatabase = getBaseContext().openOrCreateDatabase("contacts.db",0,null);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Contacts;");
//        String command="CREATE TABLE IF NOT EXISTS Contacts (name text, phone integer, email text);";
//        sqLiteDatabase.execSQL(command);
//        command ="INSERT INTO Contacts values (\"Kunal\",9810390605,\"kraghav123@gmail.com\");";
//        sqLiteDatabase.execSQL(command);
//        command ="INSERT INTO Contacts values (\"Papa\",9810354250,\"kumarbaranwal@gmail.com\");";
//        sqLiteDatabase.execSQL(command);
//
//        Cursor query = sqLiteDatabase.rawQuery("SELECT * FROM Contacts",null);
//
//        if(query.moveToFirst()) {
//            do {
//                String name = query.getString(0);
//                Integer number = query.getInt(1);
//                Log.d(TAG, "onCreate: number: "+number);
//                String email = query.getString(2);
//                Toast.makeText(this, "Name: " + name + "  Number: " + number + "  Email: " + email, Toast.LENGTH_LONG).show();
//            }while (query.moveToNext());
//        }
//        query.close();
//        sqLiteDatabase.close();

        final ListView contactName = (ListView) findViewById(R.id.ContactName);

        final int HasReadContactPermission = ContextCompat.checkSelfPermission(this, READ_CONTACTS);
        Log.d(TAG, "onCreate: HasReadPermission: "+HasReadContactPermission);
        if(HasReadContactPermission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{READ_CONTACTS},REQUEST_CODE_READ_CONTACTS);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                Log.d(TAG, "onClick: FAB clicked \n FAB method start");
//                if(READ_CONTACTS_GRANTED) {
                if(ContextCompat.checkSelfPermission(MainActivity.this,READ_CONTACTS)==PackageManager.PERMISSION_GRANTED){
                    String[] projection = {ContactsContract.Contacts.DISPLAY_NAME};
                    ContentResolver resolver = getContentResolver();
                    Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI,
                            projection,
                            null,
                            null,
                            ContactsContract.Contacts.DISPLAY_NAME);
                    if (cursor != null) {
                        List<String> names = new ArrayList<String>();
                        while (cursor.moveToNext()) {
                            names.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                        }
                        cursor.close();
                        ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.contact_detail, R.id.contact_name, names);
                        contactName.setAdapter(adapter);
                    }
                    Log.d(TAG, "onClick: FAB method ends");
                }
                else{
                    Snackbar.make(view,"Contacts Permission not Granted",Snackbar.LENGTH_INDEFINITE)
                      .setAction("Grant", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "onClick: snackbar clicked");
                            if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,READ_CONTACTS)){
                                Log.d(TAG, "Snackbar onClick: Calling requestPermissions");
                                ActivityCompat.requestPermissions(MainActivity.this,new String[]{READ_CONTACTS},REQUEST_CODE_READ_CONTACTS);
                            }else {
                                //The user has permanently denied the permission, so open settings
                                Log.d(TAG, "Snackbar onClick: permission permanently denied, calling settings");
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",MainActivity.this.getPackageName(),null);
                                Log.d(TAG, "Snackbar onClick: uri: "+ uri.toString());
                                intent.setData(uri);
                                MainActivity.this.startActivity(intent);
                                
                            }
                            Log.d(TAG, "onClick: Snackbar ends");
                        }
                    }).show();
                }
            }
        });
        Log.d(TAG, "onCreate: ends");
    }
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode){
//            case REQUEST_CODE_READ_CONTACTS:
//                // if request is cancelled, the rest arrays are empty
//                if( grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
//                    //  permission was granted
//                    // Contacts related task needed to be done goes here.
//                    Log.d(TAG, "onRequestPermissionsResult: Permission Granted");
//                    READ_CONTACTS_GRANTED=true;
//                }else {
//                    //  permission denied
//                    // disable the functionality that uses this permission
//                    Log.d(TAG, "onRequestPermissionsResult: Permission Refused");
//                }
//        }
//
//    }

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
}
