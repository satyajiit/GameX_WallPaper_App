package com.satyajit.gamex.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.satyajit.gamex.GetterSetter.Items;
import com.satyajit.gamex.R;
import com.satyajit.gamex.adapters.SlidingImage_Adapter;
import com.satyajit.gamex.utils.GameX;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.satyajit.gamex.DatabaseHelper.SQLiteDBHelper;

public class SlideImageActivity extends AppCompatActivity {

    private static ViewPager mPager;
    public List<Items> namesList = new ArrayList<>();
    SlidingImage_Adapter mAdapter;
    TextView count;
    int temp;

    LikeButton fav;

    int isShare = 0;

    private static final int WRITE_REQUEST_CODE = 300;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_image);

        initUI();



        //new LoadURL().execute("");

        namesList = ((GameX) this.getApplication()).getList();

        mAdapter = new SlidingImage_Adapter(namesList, this);


        mPager.setAdapter(mAdapter);

         temp = getIntent().getIntExtra("position",0);

        mPager.setCurrentItem(temp);

        updateCount(temp);


        listener();

        checkIfFav();

    }

    void checkIfFav(){

        SQLiteDBHelper database = new SQLiteDBHelper(this);
        if (database.CheckIfExist(namesList.get(temp).getImage())){
                    fav.setLiked(true);
        }
        else fav.setLiked(false);


    }

    void updateCount(int position){

        if (namesList.get(position).getCount()!=null) {

            float counter = Float.parseFloat(namesList.get(position).getCount());

            float temp;

            if (counter > 1000) {

                temp = counter / 1000;

                if (temp > 99)
                    count.setText(String.format(java.util.Locale.US, "%.0f", counter / 1000) + "K");

                else
                    count.setText(String.format(java.util.Locale.US, "%.1f", counter / 1000) + "K");

            } else
                count.setText(String.valueOf(counter));


        }


        else
            count.setVisibility(View.GONE);


    }

    void initUI(){

        mPager = findViewById(R.id.image_slider);
        count = findViewById(R.id.count_top);
        fav = findViewById(R.id.fab_fav);

    }



    public void back(View view) {

        onBackPressed();
        finish();
    }

    void listener(){

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                updateCount(position);
                temp = position;
                checkIfFav();
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        fav.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                    storeAsFav();
            }

            @Override
            public void unLiked(LikeButton likeButton) {

               deleteFav();
                Toast.makeText(SlideImageActivity.this, "Removed from Favourite!", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void download(View view) {

       isShare = 0;


            Dexter.withActivity(this)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override public void onPermissionGranted(PermissionGrantedResponse response) {

                            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "GameX/"+namesList.get(temp).getImage());
                            if(file.exists())
                                Toast.makeText(SlideImageActivity.this, "Already Downlaoded in GameX Folder!!", Toast.LENGTH_LONG).show();
                        else
                            new DownloadFile().execute("http://adminpanelririo.com/Rgaming//upload/"+namesList.get(temp).getImage());


                        }
                        @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                            if (response.isPermanentlyDenied()) {
                                showSettingsDialog();
                            }
                        }
                        @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        }
                    }).check();







    }


    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SlideImageActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public void share(View view) {

    isShare = 1;
        share();



    }


    private class DownloadFile extends AsyncTask<String, String, String> {

        private ProgressDialog progressDialog;
        private String fileName;
        private String folder;

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog = new ProgressDialog(SlideImageActivity.this);
            this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            this.progressDialog.setCancelable(false);
            this.progressDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // getting file length
                int lengthOfFile = connection.getContentLength();


                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);



                //Extract file name from URL
                fileName = namesList.get(temp).getImage();



                //External directory path to save file
                folder = Environment.getExternalStorageDirectory() + File.separator + "GameX/";

                //Create androiddeft folder if it does not exist
                File directory = new File(folder);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file
                OutputStream output = new FileOutputStream(folder + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));
                    Log.d(TAG, "Progress: " + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();
                return "Downloaded at: " + folder + fileName;

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return "Something went wrong!!";
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String message) {
            // dismiss the dialog after the file was downloaded
            this.progressDialog.dismiss();

            if (isShare == 0)
            // Display File path after downloading
            Toast.makeText(getApplicationContext(),
                    message, Toast.LENGTH_LONG).show();

            else if (isShare == 3 )
                                setWallpaperIntent();
            else
                share();
        }
    }

void share(){

    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "GameX/"+ namesList.get(temp).getImage());

    Uri uri = FileProvider.getUriForFile(this, "com.satyajit.gamex.provider", file);

    Intent intent = ShareCompat.IntentBuilder.from(this)
            .setType("image/jpg")
            .setSubject("Wallpaper Share")
            .setStream(uri)
            .setChooserTitle("Share Wallpaper")
            .createChooserIntent()
            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);


    if(file.exists()){
        // share.putExtra(Intent.EXTRA_STREAM, data);
        Log.d("TEST", "SATYA");
        startActivity(intent);

    }

    else {
        new DownloadFile().execute("http://adminpanelririo.com/Rgaming//upload/"+namesList.get(temp).getImage());

    }



}

public void setWallpaper(View v){


        setWallpaperIntent();

}

void setWallpaperIntent(){
    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "GameX/"+namesList.get(temp).getImage());

    Uri uri = FileProvider.getUriForFile(this, "com.satyajit.gamex.provider", file);

    isShare = 3;

    Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
    intent.addCategory(Intent.CATEGORY_DEFAULT);
    intent.setDataAndType(uri, "image/jpeg");
    intent.putExtra("mimeType", "image/jpeg");

    if (file.exists())
        this.startActivity(Intent.createChooser(intent, "Set as:"));
    else
        new DownloadFile().execute("http://adminpanelririo.com/Rgaming//upload/"+namesList.get(temp).getImage());
}


public void storeAsFav(){

        SQLiteDatabase database = new SQLiteDBHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteDBHelper.COLUMN_NAME, namesList.get(temp).getName());
        values.put(SQLiteDBHelper.COLUMN_IMAGE, namesList.get(temp).getImage());
        database.insert(SQLiteDBHelper.TABLE_NAME, null, values);

        Toast.makeText(this, "Added to Favourite", Toast.LENGTH_LONG).show();



}

public void deleteFav(){

    new SQLiteDBHelper(this).deleteRow(namesList.get(temp).getImage());

    }


}
