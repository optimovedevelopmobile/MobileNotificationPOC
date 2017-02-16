package com.optimove.mobile.develop.optimovepushnotificationpoc;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RemoteViews;

import com.facebook.CallbackManager;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.appinvite.AppInviteReferral;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.piwik.sdk.PiwikApplication;
import org.piwik.sdk.TrackHelper;
import org.piwik.sdk.Tracker;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private CallbackManager callbackManager;
    private Tracker mPiwikTracker;
    private static final String TAG = "opti-MsgService";
    private static final String ACTION_NOTIFICATION = "com.optimove.mobile.develop.optimovepushnotificationpoc.action.notification";
    private static final String NOTIFICATIONID = "fromNotification";
    private static final String NOTIFICATIONMSGID ="NOTIICATION_MSGID";

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

        Tracker tracker = ((PiwikApplication) getApplication()).getTracker();
        TrackHelper.track().screen("/MainActivity").title("MainActivity").with(tracker);


        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:1055457349234:android:233f15c4971138c2") // Required for Analytics.
                .setApiKey("AIzaSyDrds9sKgr6DaCV9eeynay8BFqW6BtP8sg") // Required for Auth.
                .setDatabaseUrl("https://optimovepoc.firebaseio.com/") // Required for RTDB.
                .build();
        FirebaseApp.initializeApp(this /* Context */, options, "secondary");
        // Retrieve my other app.
        FirebaseApp app = FirebaseApp.getInstance("secondary");
        // Get the database for the other app.
        FirebaseDatabase secondaryDatabase = FirebaseDatabase.getInstance(app);
        DatabaseReference myRef = secondaryDatabase.getReference("message");

        myRef.setValue("Hello, World!");




        // Build GoogleApiClient with AppInvite API for receiving deep links
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(AppInvite.API)
                .build();

        // Check if this app was launched from a deep link. Setting autoLaunchDeepLink to true
        // would automatically launch the deep link if one is found.
        boolean autoLaunchDeepLink = false;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(@NonNull AppInviteInvitationResult result) {
                                if (result.getStatus().isSuccess()) {
                                    // Extract deep link from Intent
                                    Intent intent = result.getInvitationIntent();
                                    String deepLink = AppInviteReferral.getDeepLink(intent);

                                    // Handle the deep link. For example, open the linked
                                    // content, or apply promotional credit to the user's
                                    // account.
                                    Log.d(TAG, "getInvitation:  deep link is found.");
                                    // ...
                                } else {
                                    Log.d(TAG, "getInvitation: no deep link found.");
                                }
                            }
                        });


        Intent currIntent = getIntent();
        String action = currIntent.getAction();
        if (getIntent().getExtras() != null) {
            final String notificationId = currIntent.getStringExtra(NOTIFICATIONID);
            final String notificationMsgId = currIntent.getStringExtra(NOTIFICATIONMSGID);

            Bundle b = getIntent().getExtras();
            if(b != null) {

                boolean cameFromNotification = b.getBoolean("fromNotification");
                String msgId = b.getString("NotificationMsgId");
                if (cameFromNotification == true) {
                    Date currentTime = Calendar.getInstance().getTime();
                    TrackHelper.track()
                            .screen("/MainActivity")
                            .title("MainActivity")
                            .variable(1, "InteractionWithNotification", currentTime.toString())
                            .variable(2, "NotificationMsgId", msgId)
                            .with(getTracker());


                }
            }
        }

        //createNotification();
//        try {
//            bigNotificationInboxstyle();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//           // bigNotificationBigText();
//            //bigNotificationBigPicture();
//         //   NotificationBigPicture();
//          //  NotificationCustomView();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Intent currIntent = getIntent();
        String action = currIntent.getAction();
        if (getIntent().getExtras() != null) {
            final String notificationId = currIntent.getStringExtra(NOTIFICATIONID);
            final String notificationMsgId = currIntent.getStringExtra(NOTIFICATIONMSGID);

            Bundle b = getIntent().getExtras();

            if(b != null)
            {
                boolean cameFromNotification = b.getBoolean("fromNotification");
                String msgId = b.getString("NotificationMsgId");
                if (cameFromNotification == true) {
                    Date currentTime = Calendar.getInstance().getTime();
                    TrackHelper.track()
                            .screen("/MainActivity")
                            .title("MainActivity")
                            .variable(1, "InteractionWithNotification", currentTime.toString())
                            .variable(2, "NotificationMsgId", msgId)
                            .with(getTracker());


                }
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();



        Intent currIntent = getIntent();
        String action = currIntent.getAction();
        if (getIntent().getExtras() != null) {
            final String notificationId = currIntent.getStringExtra(NOTIFICATIONID);
            final String notificationMsgId = currIntent.getStringExtra(NOTIFICATIONMSGID);


            Bundle b = getIntent().getExtras();
            if(b != null) {
                boolean cameFromNotification = b.getBoolean("fromNotification");
                String msgId = b.getString("NotificationMsgId");
                if (cameFromNotification == true) {
                    Date currentTime = Calendar.getInstance().getTime();
                    TrackHelper.track()
                            .screen("/MainActivity")
                            .title("MainActivity")
                            .variable(1, "InteractionWithNotification", currentTime.toString())
                            .variable(2, "NotificationMsgId", msgId)
                            .with(getTracker());


                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent currIntent = getIntent();
        String action = currIntent.getAction();
        if (getIntent().getExtras() != null) {
            final String notificationId = currIntent.getStringExtra(NOTIFICATIONID);
            final String notificationMsgId = currIntent.getStringExtra(NOTIFICATIONMSGID);
            Bundle b = getIntent().getExtras();
            if(b != null) {
                boolean cameFromNotification = b.getBoolean("fromNotification");
                String msgId = b.getString("NotificationMsgId");
                if (cameFromNotification == true) {
                    Date currentTime = Calendar.getInstance().getTime();
                    TrackHelper.track()
                            .screen("/MAinActivity")
                            .title("MAinActivity")
                            .variable(1, "InteractionWithNotification", currentTime.toString())
                            .variable(2, "NotificationMsgId", msgId)
                            .with(getTracker());


                }
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        try {
            switch (item.getItemId()) {
                case R.id.normal_notification:
                    createNotification();
                    TrackHelper.track()
                            .screen("/custom_vars")
                            .title("Custom Vars")
                            .variable(3, "third", "var")
                            .variable(4, "createNotification", "var")
                            .with(getTracker());
                    return true;
                case R.id.style_inbox_notification:
                    bigNotificationInboxstyle();

                    TrackHelper.track()
                            .screen("/custom_vars")
                            .title("Custom Vars")
                            .variable(5, "fourth", "var")
                            .variable(6, "bigNotificationInboxstyle", "var")
                            .with(getTracker());
                    return true;
                case R.id.big_texte_notification:
                    bigNotificationBigText();
                    return true;
                case R.id.big_picture_notification:
                    bigNotificationBigPicture();
                    return true;
                case R.id.customView:
                    NotificationCustomView();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);

    }

    private void createNotification() {

        Intent activityintent = new Intent(this, MainActivity.class);
        activityintent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(this, 0, activityintent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(intent);
        builder.setTicker(getString(R.string.ticer__notif_text));
        builder.setContentTitle(" Normal Simple notification");
        builder.setContentText("I'm the Normal Simple notification");
        builder.setSmallIcon(R.drawable.ic_stat_ic_notification);
        builder.setAutoCancel(true);
        Notification notification = builder.build();

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, notification);

    }


    public void bigNotificationInboxstyle() throws IOException {
        String sUrl = "http://image10.bizrate-images.com/resize?sq=60&uid=2216744464";
        URL url = new URL(sUrl);
       // Bitmap aBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_expanded_notification);

        Intent activityintent = new Intent(this, MainActivity.class);
        activityintent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0, activityintent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("Event tracker")
                .setContentText("Events received")
                .setContentIntent(intent)
                .setLargeIcon(largeIcon);
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
        String[] events = new String[6];
        events[0] = "event number 1 ...";
        events[1] = "event number 2 ...";
        events[2] = "event number 3 ...";
        events[3] = "event number 4 ...";
        events[4] = "event number 5 ...";
        events[5] = "event number 6 ...";
// Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle("Event tracker details:");
// Moves events into the expanded layout
        for (int i=0; i < events.length; i++) {

            inboxStyle.addLine(events[i]);
        }
// Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void bigNotificationBigText() throws IOException {

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_expanded_notification);

        Intent activityintent = new Intent(this, MainActivity.class);
        activityintent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0, activityintent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("Event tracker")
                .setContentIntent(intent)
                .setContentText("Events received");
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText("my very very very very very very very very very very very very very very very very very very very very very very very very long string ");
        mBuilder.setStyle(bigTextStyle);

//        Notification notif = new Notification.Builder(this)
//                .setContentTitle("---- bigNotificationBigText ---- ")
//                .setContentText("bigNotif Subjects" )
//                .setSmallIcon(R.drawable.ic_stat_ic_notification)
//                .setLargeIcon(largeIcon)
//                .setStyle(new Notification.BigTextStyle()
//                        .bigText("my very very very very very very very very very very very very very very very very very very very very very very very very long string "))
//                .build();

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(3, mBuilder.build());

    }


    private void bigNotificationBigPicture() throws IOException, ExecutionException, InterruptedException {

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_big_picture);
        TestUtils.DownloadingTaskParams param = new TestUtils.DownloadingTaskParams();
        param._context = this;
        param._url = "https://s29.postimg.org/hxclkgw9z/finance_marketer_6501.jpg";

        TestUtils.DownloadFilesTask task = (TestUtils.DownloadFilesTask) new TestUtils.DownloadFilesTask().execute(param);
        Bitmap bitmap = task.get();

        Intent activityintent = new Intent(this, MainActivity.class);
        activityintent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0, activityintent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("BigPicture ContentTitle")
                .setContentText("BigPicture tContentText")
                .setContentIntent(intent);

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.bigPicture(bitmap);
        mBuilder.setStyle(bigPictureStyle);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void NotificationBigPictureNew() throws IOException {

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_expanded_notification);

        Intent activityintent = new Intent(this, MainActivity.class);
        activityintent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0, activityintent,PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setContentText("BigPicture ContentText");
        mBuilder.setSubText("BigPicture Sub Text");
        mBuilder.setSmallIcon(R.drawable.ic_stat_ic_notification);
        mBuilder.setLargeIcon(largeIcon);
        mBuilder.setContentIntent(intent);

        Notification.BigPictureStyle bigPictureStyle = new Notification.BigPictureStyle();
        bigPictureStyle.bigPicture(largeIcon);
        mBuilder.setStyle(bigPictureStyle);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void NotificationCustomView() throws IOException, ExecutionException, InterruptedException {



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        // END_INCLUDE(notificationCompat)

        // BEGIN_INCLUDE(intent)
        //Create Intent to launch this Activity again if the notification is clicked.
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intent);
        // END_INCLUDE(intent)

        // BEGIN_INCLUDE(ticker)
        // Sets the ticker text
        builder.setTicker("ticker ....");

        // Sets the small icon for the ticker
        builder.setSmallIcon(R.drawable.ic_stat_ic_notification);
        // END_INCLUDE(ticker)

        // BEGIN_INCLUDE(buildNotification)
        // Cancel the notification when clicked
        builder.setAutoCancel(true);

        builder.setContentIntent(intent);

        // Build the notification
        Notification notification = builder.build();
        // END_INCLUDE(buildNotification)

        // BEGIN_INCLUDE(customLayout)
        // Inflate the notification layout as RemoteViews
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);

        // Set text on a TextView in the RemoteViews programmatically.
        final String time = DateFormat.getTimeInstance().format(new Date()).toString();
        final String text = getResources().getString(R.string.collapsed, time);
        contentView.setTextViewText(R.id.custom_content_title_not_expanded, text);
/* Workaround: Need to set the content view here directly on the notification.
         * NotificationCompatBuilder contains a bug that prevents this from working on platform
         * versions HoneyComb.
         * See https://code.google.com/p/android/issues/detail?id=30495
         */
        notification.contentView = contentView;

        // Add a big content view to the notification if supported.
        // Support for expanded notifications was added in API level 16.
        // (The normal contentView is shown when the notification is collapsed, when expanded the
        // big content view set here is displayed.)
        if (Build.VERSION.SDK_INT >= 16) {
            // Inflate and set the layout for the expanded notification view
            RemoteViews expandedView =
                    new RemoteViews(getPackageName(), R.layout.notification_expanded);

            TestUtils.DownloadingTaskParams param = new TestUtils.DownloadingTaskParams();
            param._context = this;
            param._url = "https://s28.postimg.org/ijbcdv5h9/man_Flying.jpg";

            TestUtils.DownloadFilesTask task = (TestUtils.DownloadFilesTask) new TestUtils.DownloadFilesTask().execute(param);
            Bitmap bitmap = task.get();

            contentView.setImageViewBitmap (R.id.imageView, bitmap);
            expandedView.setImageViewBitmap (R.id.imageView, bitmap);

            //url = http://www.optimove.com/wp-content/uploads/2015/10/Hero_ill1.png
            notification.bigContentView = expandedView;

        }
        // END_INCLUDE(customLayout)

        // START_INCLUDE(notify)
        // Use the NotificationManager to show the notification
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, notification);
    }

    private void createCustomNotification() {
        // BEGIN_INCLUDE(notificationCompat)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        // END_INCLUDE(notificationCompat)

        // BEGIN_INCLUDE(intent)
        //Create Intent to launch this Activity again if the notification is clicked.
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intent);
        // END_INCLUDE(intent)

        // BEGIN_INCLUDE(ticker)
        // Sets the ticker text
        builder.setTicker(getString(R.string.ticer__notif_text));

        // Sets the small icon for the ticker
        builder.setSmallIcon(R.drawable.ic_stat_ic_notification);
        // END_INCLUDE(ticker)

        // BEGIN_INCLUDE(buildNotification)
        // Cancel the notification when clicked
        builder.setAutoCancel(true);

        // Build the notification
        Notification notification = builder.build();
        // END_INCLUDE(buildNotification)

        // BEGIN_INCLUDE(customLayout)
        // Inflate the notification layout as RemoteViews
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);

        // Set text on a TextView in the RemoteViews programmatically.
        final String time = DateFormat.getTimeInstance().format(new Date()).toString();
        final String text = getResources().getString(R.string.collapsed, time);
        contentView.setTextViewText(R.id.custom_content_title_not_expanded, text);

        /* Workaround: Need to set the content view here directly on the notification.
         * NotificationCompatBuilder contains a bug that prevents this from working on platform
         * versions HoneyComb.
         * See https://code.google.com/p/android/issues/detail?id=30495
         */
        notification.contentView = contentView;

        // Add a big content view to the notification if supported.
        // Support for expanded notifications was added in API level 16.
        // (The normal contentView is shown when the notification is collapsed, when expanded the
        // big content view set here is displayed.)
        if (Build.VERSION.SDK_INT >= 16) {
            // Inflate and set the layout for the expanded notification view
            RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.notification_expanded);

            notification.bigContentView = expandedView;
            builder.setCustomBigContentView(expandedView);

        }
        // END_INCLUDE(customLayout)

        // START_INCLUDE(notify)
        // Use the NotificationManager to show the notification
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, notification);
        // END_INCLUDE(notify)
    }

    private Tracker getTracker() {
        return ((PiwikApplication) getApplication()).getTracker();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        int i = 1;
    }

//    private class DownloadingTaskParams{
//
//        String _url;
//        Context _context;
//    }
//    private class DownloadFilesTask extends AsyncTask<DownloadingTaskParams, Integer, Bitmap> {
//
//
//        @Override
//        protected Bitmap doInBackground(DownloadingTaskParams... downloadingTaskParamses) {
//            DownloadingTaskParams  parm = downloadingTaskParamses[0];
//
//            Bitmap createdBMP = null;
//            try {
//                createdBMP = Picasso.with(parm._context).load("https://s28.postimg.org/ijbcdv5h9/man_Flying.jpg").get();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return createdBMP;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//        }
//
//        @Override
//        protected void onProgressUpdate(Integer... values) {
//            super.onProgressUpdate(values);
//        }
//    }

}
