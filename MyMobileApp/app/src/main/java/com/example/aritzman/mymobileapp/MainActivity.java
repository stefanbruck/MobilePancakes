package com.example.aritzman.mymobileapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int QR_REQUEST = 0;
    int PHOTO_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    public void buttonQRscanOnClick(View v)
    {
        try
        {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

            //startActivityForResult(Intent.createChooser(intent, "Choose QR Scanner"), 0);
            startActivityForResult(intent, 0);

        } catch (Exception e) {

            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == 0)
        {
            if (resultCode == RESULT_OK)
            {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Toast.makeText(this, contents, Toast.LENGTH_LONG).show();
                // Handle successful scan
                TextView tv2 = (TextView) findViewById(R.id.textViewScanResult);
                tv2.setText(contents);

            } else if (resultCode == RESULT_CANCELED)
            {
                //Handle cancel
            }
        }
        else
        if (requestCode == PHOTO_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                //super.onActivityResult(requestCode, resultCode, data);
                Bitmap thumbnail = (Bitmap) intent.getExtras().get("data");
            }
        }
    }

    public void buttonOCRscanOnClick(View v)
    {
        try
        {
            Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(i, PHOTO_REQUEST);


            String path = "empty";
            ExifInterface e = new ExifInterface(path);
            int ex = e.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

/*
            TessBaseAPI tessapi = new TessBaseAPI();
            //Here PATH is the path of the language files present in the sdcard for example
            tessapi.init(PATH, lang);

            tessapi.setImage(bitmap);
            String txt = tessapi.getUTF8Text();
            tessapi.end();
*/

        } catch (Exception e) {

        }
    }

}
