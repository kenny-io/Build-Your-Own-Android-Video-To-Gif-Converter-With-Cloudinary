package com.example.ekene.mp4togifconverter;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cloudinary.Transformation;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;


import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button uploadBtn;
    private ProgressBar progressBar;
    private int SELECT_VIDEO = 2;
    private ImageView img1;
    private DownloadManager downloadManager;
    private Button download_btn;
    private String gifUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progress_bar);
        MediaManager.init(this);
        img1 = findViewById(R.id.img1);
        uploadBtn = findViewById(R.id.uploadBtn);
        download_btn = findViewById(R.id.download_btn);
        download_btn.setVisibility(View.INVISIBLE);

        download_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(gifUrl);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                Long reference = downloadManager.enqueue(request);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickVideoFromGallery();

            }
            private void pickVideoFromGallery() {
                Intent GalleryIntent = new Intent();
                GalleryIntent.setType("video/*");
                GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(GalleryIntent, "select video"), SELECT_VIDEO);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {

        if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK) {

            final Uri selectedVideo = data.getData();
            MediaManager.get()
                    .upload(selectedVideo)
                    .unsigned("kennyy")
                    .option("resource_type", "video")
                    .callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                            progressBar.setVisibility(View.VISIBLE);
                            Toast.makeText(MainActivity.this, "Upload Started...", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onProgress(String requestId, long bytes, long totalBytes) {

                        }

                        @Override
                        public void onSuccess(String requestId, Map resultData) {

                            Toast.makeText(MainActivity.this, "Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            uploadBtn.setVisibility(View.INVISIBLE);

                            String publicId = resultData.get("public_id").toString();

                           gifUrl = MediaManager.get().url().resourceType("video")
                                    .transformation(new Transformation().videoSampling("25")
                                            .delay("200").height(200).effect("loop:10").crop("scale"))
                                    .resourceType("video").generate(publicId+".gif");


                            Glide.with(getApplicationContext()).asGif().load(gifUrl).into(img1);

                            download_btn.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            
                            Toast.makeText(MainActivity.this, "Upload Error", Toast.LENGTH_SHORT).show();
                            Log.v("ERROR!!", error.getDescription());
                        }

                        @Override
                        public void onReschedule(String requestId, ErrorInfo error) {

                        }

                    }).dispatch();
        }else {

            Toast.makeText(MainActivity.this, "Can't Upload", Toast.LENGTH_SHORT).show();
        }
    }
}
