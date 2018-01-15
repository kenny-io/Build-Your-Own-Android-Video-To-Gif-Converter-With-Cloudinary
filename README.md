# Build-Your-Own-Android-Video-To-Gif-Converter-With-Cloudinary


![](https://d2mxuefqeaa7sj.cloudfront.net/s_20FF6D0E9F9F51079FB809C46622CA2C7AD5DE4B7CCA9757B741CC52E2DBD712_1515320131913_cloudinaryImageBG.jpg)


We can safely agree that images and videos have a huge impact in today’s life, and besides the hassle of handling storage and delivery, we developers have to deal with a lot of overhead, for example displaying a video on a website or mobile device. We have to take into account the size and format of the file, whether or not we should stream or download, file optimization and many other aspects, and this list just goes on and on..

It has been said that a picture is worth a thousand words. For modern websites, a video surely takes the visual impact to a whole new level. Modern web and mobile applications have an opportunity to dramatically increase their visual impact and level of engagement by showcasing videos online. Between news reports, user shared video snippets, explainer videos and ad campaigns, we see more and more videos appearing daily in our websites and mobile apps.

As good as it sounds to use videos and increase user experience on websites and mobile apps, it is often very tasking and expensive both for the users and for the product itself. Uploading that number of videos will affect application performance, require a proportional increase in app memory and will cost users a lot of data hence, the need for another less expensive option, the GIF.

An animated GIF is an image encoded in graphics interchange format (GIF) which contains a number of images or frames in a single file and is described by its own graphic control extension. The frames are presented in a specific order in order to convey animation. An animated GIF can loop endlessly or stop after a few sequences.
It is a lot less expensive in memory and costs a lot less in data. This makes it the perfect alternative as it offers almost the same level of user engagement on web and mobile applications.

So in this tutorial we are going to build an android application that will convert a video to an animated gif and give the user an option to download the created gif. Simply select a video and load it in the app. The app automatically uploads the video to Cloudinary, converts it to a gif file and renders it on screen from where the user can download.

We will achieve these features in Android Studio using Cloudinary. Cloudinary is a cloud-based end-to-end image and video management solution that supports uploads, storage, manipulations, optimizations and delivery. Cloudinary helps you store images/videos and perform transformations, such as resizing and adding a huge number of effects to the resource. 
More so, Cloudinary allows you to add image manipulation effects to the generated gif and deliver it through it’s unique URL.

App Flow
1. User loads up the app and clicks a button to upload a video
2. On successful upload, the app processes the video and converts it to a gif.
3. Finally, it renders it on screen for download.

That said, lets go ahead and start building.

Requirements

For this tutorial, we will be using a number of third-party libraries including:


- Cloudinary Android SDK - You will need a Cloudinary Account
- Glide for asynchronous image loading
- Fair knowledge of Android development 
Demo

Here’s a short clip of how the app works.
You can find the source code here at any time as you build
If you have an android device and would like to try out the app, download here

Setting Up Android

Create an Android Studio Project:
Open Android Studio and create a new Android project. Click the file menu and select New Project

![](https://d2mxuefqeaa7sj.cloudfront.net/s_20FF6D0E9F9F51079FB809C46622CA2C7AD5DE4B7CCA9757B741CC52E2DBD712_1515491612254_one.jpg)


Enter the application name, package name and define project location


![name the app and click next](https://d2mxuefqeaa7sj.cloudfront.net/s_20FF6D0E9F9F51079FB809C46622CA2C7AD5DE4B7CCA9757B741CC52E2DBD712_1515491638952_two.jpg)


Select target devices and API levels


![set minimum sdk to API 17 and click next](https://d2mxuefqeaa7sj.cloudfront.net/s_20FF6D0E9F9F51079FB809C46622CA2C7AD5DE4B7CCA9757B741CC52E2DBD712_1515491646947_hree.jpg)


Select an Activity template, Empty Activity in our case

![select empty activity and click next](https://d2mxuefqeaa7sj.cloudfront.net/s_20FF6D0E9F9F51079FB809C46622CA2C7AD5DE4B7CCA9757B741CC52E2DBD712_1515491758590_ff.jpg)


Rename the selected Activity, in our case we’ll keep the defaults, now click finish and we are set!

![leave the defaults and click finish](https://d2mxuefqeaa7sj.cloudfront.net/s_20FF6D0E9F9F51079FB809C46622CA2C7AD5DE4B7CCA9757B741CC52E2DBD712_1515491668645_five.jpg)

Setting Up Dependencies

Once you’ve set up your application in Android Studio, or any preferred IDE, let’s start with installing Cloudinary and Glide as a dependency in the `build.gradle` file of your application module, add:


```java    
    implementation group: 'com.cloudinary', name: 'cloudinary-android', version: '1.22.0'
    implementation 'com.squareup.picasso:picasso:2.5.2'
```

Click Sync to install the dependencies.

Then open the `AndroidManifest.xml` file and insert our Cloudinary configurations under the application tag:

```xml
    <manifest xmlns:android="http://schemas.android.com/apk/res/android"
       package="com.example.ekene.imageboardcloudinary">
    
        <uses-permission android:name="android.permission.INTERNET"/>
    
        <application
            ...
            >
            <meta-data
                android:name="CLOUDINARY_URL"
                android:value="cloudinary://@myCloudName"/>
        </application>
    </manifest>
```
myCloudName is the value of your personal Cloudinary name, which you can easily find on your console. Notice we also added the internet permissions in the manifest.

App Layout

We start by defining the layout of our App. The Layout will feature a progressbar to show the progression of the upload process, a button to trigger the video upload, an ImageView object to render the created gif  from Cloudinary and a download button to download the gif. So open `activity_main.xml` file and set it up like so:

```xml
    <?xml version="1.0" encoding="utf-8"?>
    <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:context="com.example.ekene.mp4togifconverter.MainActivity">
        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Mp4 To GIF"
            android:fontFamily="serif"
            android:textSize="20sp"
            android:layout_gravity="center"/>
        <ProgressBar
            android:id="@+id/progress_bar"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:visibility="invisible"
            android:layout_gravity="center"
            android:layout_marginBottom="20dp"/>
        <Button
            android:id="@+id/uploadBtn"
            android:layout_margin="10dp"
            android:fontFamily="serif"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Upload and Convert"/>
        <ScrollView
            android:layout_width="match_parent"
            android:layout_height="wrap_content">
            <LinearLayout
                android:layout_marginTop="50dp"
                android:layout_marginBottom="50dp"
                android:orientation="vertical"
                android:layout_width="match_parent"
                android:layout_height="wrap_content">
                <ImageView
                    android:id="@+id/img1"
                    android:layout_gravity="center"
                    android:layout_width="200dp"
                    android:layout_height="200dp"
                    android:layout_margin="10dp"/>
                <Button
                    android:id="@+id/download_btn"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:text="Download"
                    android:layout_margin="16dp"/>
            </LinearLayout>
        </ScrollView>
    </LinearLayout>
```
Next we initialize these view objects in the `MainActivity.java` file so we can make references to them. Open the `MainActivity.java` file and initialize them like so:

```java
    package com.example.ekene.mp4togifconverter;
    import ...
    
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
            //...
                  
            }
    }
```
Next we set up the upload button such that upon click it launches the gallery so the user can select a video file to upload:

```java
    package com.example.ekene.mp4togifconverter;
    import ...
    public class MainActivity extends AppCompatActivity {
        private Button uploadBtn;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            uploadBtn = findViewById(R.id.uploadBtn);        
            uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickVideoFromGallery();
        
          }
        private void pickVideoFromGallery() {
            Intent GalleryIntent = new Intent();
            GalleryIntent.setType("video/*");
            GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(GalleryIntent, 
            "select video"), SELECT_VIDEO);
        }
    });
    //...
    }
    
```
When a video is selected, the `onActivityResult` method is called which will in turn trigger an upload to Cloudinary. We do this by building an `UploadRequest` and dispatching it within the `onActivityResult` method. Before we get into all of that let’s take a minute to understand how uploading to Cloudinary works.

Since we’ll be performing network request, open AndroidManifest.xml and add this code just before the Application tag for internet permissions:
```xml
    <uses-permission android:name="android.permission.INTERNET"/>
```
Uploading to Cloudinary

Cloudinary offers two types of uploads: Signed and Unsigned uploads.
Signed uploads requires an authentication signature from a back-end. With signed uploads, your images and videos are signed with the API and secret key found in the console. Since using these keys are risky on a client side that can easily be decompiled, there is need for a backend.

Unsigned uploads on the other hand are less secure than signed uploads. They do not require any signature for uploads. Unsigned upload options are controlled by an upload preset. An upload preset is used to define options to be applied to images that are uploaded with the preset. Unsigned uploads, however, have other limitations. For example, existing images cannot be overwritten. The options you set in the unsigned preset also can limit the size or type of files that users can upload to your account.

In this tutorial, we will be implementing the unsigned upload. So, we need to enable unsigned uploads on our console. Select Settings on your dashboard, select the Upload tab, scroll down to where you have upload preset, and enable Unsigned Uploading. A new preset will be generated with a random string as its name. Copy it out as we will need it soon.


![](https://d2mxuefqeaa7sj.cloudfront.net/s_311E3D151960FA8D36ADA8A4D08DF8A79ED3B177F4D49703108BFE7EC8D88F37_1515950470718_unsigned.jpg)


Then to upload the selected video file to Cloudinary, we first initialize the Cloudinary `MediaManager` inside the MainActivity `onCreate` method like so:

```java
    package com.example.ekene.mp4togifconverter;
    import ...
    public class MainActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            //initialize MediaManager
            MediaManager.init(this);
        }
```
Then we call the `onActivityResult()` method inside the MainActivity file and set it up but before that, i’ll briefly explain the methods in the `MainActivity` class so far and how it’ll relate with the `onActivityResult` method we’ll implement soon. 


- `onCreate` - This method is called when the activity is created at the start of the app. Here we assign our earlier designed XML file as the default layout of the activity. We also added a listener to the button. When the button is clicked, the app calls the second method `pickVideoFromGallery`.


- `pickVideoFromGallery` simply launches the users gallery to select a video. The process has a unique request code stored in the `SELECT_VIDEO` variable. We expect a response from this process so we used the `startActivityForResult()` method to render the response on the `onActivityForResult()` method. The response can either be successful (if a video was successfully selected) or unsuccessful (if the process was terminated). Then if the response is successful, we define a variable `selectedVideo` to hold the `URI` of the selected video for upload.  Once a response is returned, the `onActivityForResult()` method is called.


- `onActivityForResult()` This method checks if the response from the `startActivityForResult()` method was successful or not. If a video was selected, the `resultCode` will be equal to `Activity.RESULT_OK` and if it was terminated, the `resultCode` will be equal to `Activity.RESULT_CANCELLED`.  If the earlier is the case, we then start building the upload request to Cloudinary with the `MediaManager`:

```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK) {
            Uri selectedVideo = data.getData();
            //...
            }
        }
```
Inside the `onActivityResult` we then add the `MediaManager` to build our upload request.

`MediaManager` Here we start building the `UploadRequest` to Cloudinary. It takes a few other methods: 

1. `upload()` – takes in the `uri` of the selected video to upload
2. `unsigned()` – takes in the `preset name` from your console
3. `option()` – takes in the `resource type` of the upload

```java
    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == SELECT_VIDEO && resultCode == RESULT_OK) {
            Uri selectedVideo = data.getData();
            MediaManager.get()
                    .upload(selectedVideo)
                    .unsigned("preset_name")
                    .option("resource_type", "video")
                    .callback(...)
                   //...
                   }
     }
```

4. `callback()` – takes in a new `UploadCallback` which implements several other callback methods that help track the progress of the upload:
- `onStart()` - defines what happens when the upload process starts: 

```java
    .callback(new UploadCallback() {
                        @Override
                        public void onStart(String requestId) {
                            progressBar.setVisibility(View.VISIBLE);
                            Toast.makeText(MainActivity.this, 
                            "Upload Started...", Toast.LENGTH_SHORT).show();
                        }
                        //...
```
here we made the `progressBar` visible and set a `Toast` that says “upload started…”


-  `onProgress()` -  this method lets you define what happens when the upload is in progress (has started but not completed):

```java
    .callback(new UploadCallback() {
                       @Override
                        public void onProgress(...) {
                        }
                        //...
```
In our case, we do nothing.


- `onSuccess()` - defines what happens when the video is successfully uploaded, In our case we generate the required gif from the uploaded video and pass it to the ImageView object with Glide. Hence, this is where the conversion happens:
Conversion

When the video gets uploaded to Cloudinary, a unique id is assigned to it through which we can manipulate the video. In our case, since we uploaded a video, we just need to append `.gif` to the unique id of the uploaded video `(publicId+".gif");`. Yes it’s that easy i know. Once we append `.gif` to it, we then pass it into the MediaManager `generate()` method and it’ll return the url of the generated gif. Then we pass the url into the `load()` method of our Glide object and call the `asGif()` method to render it on screen as a gif:

```java
    .callback(new UploadCallback() {
                        @Override
    public void onSuccess(String requestId, Map resultData) {
    
        Toast.makeText(MainActivity.this, "Uploaded Succesfully", 
        Toast.LENGTH_SHORT).show();
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
                          //...
      }
```
We can access the `URL` of the just uploaded video by calling `resultData.get("url")`. We can also access the video unique id by calling `resultData.get("public_id")` from which we then manipulated it to generate the gif and store it’s resulting url in a string variable.
After that, we used `Glide` to load the gif into the ImageView object.


- `onError()` - Lets you define what happens when there’s an error in the upload process, 

```java
    .callback(new UploadCallback() {
                        @Override
                        public void onError(String requestId, ErrorInfo error) {
                            Toast.makeText(MainActivity.this, 
                            "Upload Error", Toast.LENGTH_SHORT).show();
                            Log.v("ERROR!!", error.getDescription());
                        }
                        //...
```
In our case, we make a Toast of the error message.


- `dispatch()` - finally the dispatch method is called to choose when each upload request should actually run and how.

At the end we’ve completely set up the `onActivityForResult()` method. Now when it receives a response from the `pickVideoFromGallery()` method, it builds up an upload request to Cloudinary with the uri of the selected video, on successful upload of the video, it converts the video to a gif and renders it on the earlier defined ImageView object.
At this point if you run the app, it should successfully upload the video to cloudinary, convert it to gif and render it on screen.

If you get stuck at any point, always feel free to check the source code, long lines of code can get a bit overwhelming sometimes.

Downloading the gif

Next we download the created gif. to do this we need to set up the `download_btn` such that on click, it’ll download the gif using the Android DownloadManager. You may have noticed we initialized it inside the MainActivity file, well, now is the time to use it:

```java
    package com.example.ekene.mp4togifconverter;
    import ...
    public class MainActivity extends AppCompatActivity {
        private Button download_btn;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            download_btn = findViewById(R.id.download_btn);
            download_btn.setVisibility(View.INVISIBLE);        
       
            download_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
            
                    downloadManager = (DownloadManager)getSystemService(
                    Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(gifUrl);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    Long reference = downloadManager.enqueue(request);
                }
            });
            //...
      }
```

Now when the download button is clicked, it parses the uri of the created gif to the DownloadManager which will in turn enqueue a download request with it and show the download notification as it progresses.

Wow.. that was fun, unfortunately, that also brings this ride to an end. If you face any challenges replicating what we’ve done, feel free to leave a comment and i’ll react to it soon as i can.

With this walk through, you should be able to leverage on the awesome features of Cloudinary image/video transformations to build better products with optimized images and gifs generated from videos on the fly. To learn more about Cloudinary and their vast services, feel free to check out their Documentation.
