package com.optimove.mobile.develop.optimovepushnotificationpoc;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.piwik.sdk.PiwikApplication;
import org.piwik.sdk.TrackHelper;
import org.piwik.sdk.Tracker;
import org.piwik.sdk.plugins.CustomDimensions;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by yossi_c on 16/1/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "opti-MsgService";
    private static final String ACTION_NOTIFICATION = "com.optimove.mobile.develop.optimovepushnotificationpoc.action.notification";
    private static final String NOTIFICATIONID = "fromNotification";
    private static final String NOTIFICATIONMSGID ="NOTIICATION_MSGID";
    public MyFirebaseMessagingService() {
        super();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        String msgId = remoteMessage.getMessageId();
        Tracker tracker = ((PiwikApplication) getApplication()).getTracker();
        tracker.track(
                new CustomDimensions()
                        .set(1, remoteMessage.getMessageId())
        );


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            String type = "";
            String content = "";
            String title = "";
            String imageUrl = "";
            if(data.containsKey("type") == true)
            {
                type = data.get("type").toString();

            }

            if(data.containsKey("title") == true)
            {
                title = data.get("title").toString();

            }



            if(data.containsKey("content") == true)
            {
                content = data.get("content").toString();
            }



            try {

                switch(type){

                    case "Simple":
                        break;

                    case "BigText":

                        String veryLongText = "";
                        if(data.containsKey("veryLongText") == true)
                        {
                            veryLongText = data.get("veryLongText").toString();
                        }

                        sendNotificationBigText( msgId, title, content, veryLongText);
                        break;

                    case "BigPicture":


                        if(data.containsKey("imageurl") == true)
                        {
                            imageUrl = data.get("imageurl").toString();

                        }
                        sendNotificationBigPicture( msgId, imageUrl, title, content);

                        break;

                    case "StyleInbox":

                        String bigContentTitle = "";
                        if(data.containsKey("bigContentTitle") == true)
                        {
                            bigContentTitle = data.get("bigContentTitle").toString();
                        }

                        String[] rowsArray = {};

                        if(data.containsKey("rows") == true)
                        {
                            String rows = data.get("rows");
                            rowsArray = rows.split(",");
                        }

                        sendNotificationInboxstyle( msgId, title, content, bigContentTitle, rowsArray);
                        break;

                    case "CustomView":
                        String bigImageUrl = "";

                        if(data.containsKey("big_imageurl") == true)
                        {
                            bigImageUrl = data.get("big_imageurl").toString();
                        }

                        if(data.containsKey("imageurl") == true)
                        {
                            imageUrl = data.get("imageurl").toString();

                        }

                        sendNotificationCustomView( msgId, bigImageUrl, imageUrl, title, content);
                        break;

                }
                //sendNotificationFromData(title, content,  imageUrl);
               // sendNotificationCustomView(imageUrl, title, content);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String msgId, String title, String content) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("fromNotification", true);
        intent.putExtra("NotificationMsgId", msgId);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotificationFromData(String msgId, String title, String content, String sUrl) throws IOException, ExecutionException, InterruptedException {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP );
        intent.putExtra("fromNotification", true);
        intent.putExtra("NotificationMsgId", msgId);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        URL url = new URL(sUrl);
        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setLargeIcon(bmp)
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());



    }

    private void sendNotificationCustomView(String msgId, String bigImageUrl, String imgUrl, String title, String content ) throws IOException, ExecutionException, InterruptedException {



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        // END_INCLUDE(notificationCompat)

        // BEGIN_INCLUDE(intent)
        //Create Intent to launch this Activity again if the notification is clicked.
        Intent mServiceIntent = new Intent(getApplicationContext(), SDKNotificationHandler.class);
        mServiceIntent.setAction(ACTION_NOTIFICATION);
        mServiceIntent.putExtra(NOTIFICATIONID, "CustomView");
        mServiceIntent.putExtra(NOTIFICATIONMSGID, msgId);
        PendingIntent pendingIntentNotificationService = PendingIntent.getService(this, 12, mServiceIntent, PendingIntent.FLAG_ONE_SHOT);

        builder.setContentIntent(pendingIntentNotificationService);
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

        // Build the notification
        Notification notification = builder.build();
        // END_INCLUDE(buildNotification)

        // BEGIN_INCLUDE(customLayout)
        // Inflate the notification layout as RemoteViews
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);

        // Set text on a TextView in the RemoteViews programmatically.
        final String time = DateFormat.getTimeInstance().format(new Date()).toString();
        final String text = getResources().getString(R.string.collapsed, time);
       // contentView.setTextViewText(R.id.custom_content_title_not_expanded, text);
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
            param._url = imgUrl;

            TestUtils.DownloadingTaskParams paramBig = new TestUtils.DownloadingTaskParams();
            paramBig._context = this;
            paramBig._url = bigImageUrl;

            TestUtils.DownloadFilesTask taskBigImage = (TestUtils.DownloadFilesTask) new TestUtils.DownloadFilesTask().execute(paramBig);
            TestUtils.DownloadFilesTask taskImage = (TestUtils.DownloadFilesTask) new TestUtils.DownloadFilesTask().execute(param);
            Bitmap bitmap = taskImage.get();
            Bitmap bigBitmap = taskBigImage.get();

            contentView.setImageViewBitmap (R.id.imageView, bitmap);
            contentView.setTextViewText(R.id.custom_content_title_not_expanded, title);
            contentView.setTextViewText(R.id.custom_content_text_not_expanded, content);

            expandedView.setImageViewBitmap (R.id.imageView, bigBitmap);
            expandedView.setTextViewText(R.id.custom_title_txt, title);
            expandedView.setTextViewText(R.id.custom_content_txt, content);

            //url = http://www.optimove.com/wp-content/uploads/2015/10/Hero_ill1.png
            notification.bigContentView = expandedView;

        }
        // END_INCLUDE(customLayout)

        // START_INCLUDE(notify)
        // Use the NotificationManager to show the notification
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(0, notification);
    }

    private void sendNotificationBigPicture(String msgId, String imgUrl, String title, String content) throws IOException, ExecutionException, InterruptedException {


        TestUtils.DownloadingTaskParams param = new TestUtils.DownloadingTaskParams();
        param._context = this;
        param._url = imgUrl;

//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP );
//        intent.putExtra("fromNotification", true);
//        intent.putExtra("NotificationMsgId", msgId);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//

        Intent mServiceIntent = new Intent(getApplicationContext(), SDKNotificationHandler.class);
        mServiceIntent.setAction(ACTION_NOTIFICATION);
        mServiceIntent.putExtra(NOTIFICATIONID, "BigPicture");
        mServiceIntent.putExtra(NOTIFICATIONMSGID, msgId);
        PendingIntent pendingIntentNotificationService = PendingIntent.getService(this, 12, mServiceIntent, PendingIntent.FLAG_ONE_SHOT);




        TestUtils.DownloadFilesTask task = (TestUtils.DownloadFilesTask) new TestUtils.DownloadFilesTask().execute(param);
        Bitmap bitmap = task.get();

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntentNotificationService);

        NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
        bigPictureStyle.bigPicture(bitmap);
        mBuilder.setStyle(bigPictureStyle);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void sendNotificationInboxstyle(String msgId, String title, String content, String bigContentTitle, String[] rows) throws IOException {
        String sUrl = "http://image10.bizrate-images.com/resize?sq=60&uid=2216744464";
        URL url = new URL(sUrl);
        // Bitmap aBitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_expanded_notification);

        Intent mServiceIntent = new Intent(getApplicationContext(), SDKNotificationHandler.class);
        mServiceIntent.setAction(ACTION_NOTIFICATION);
        mServiceIntent.putExtra(NOTIFICATIONID, "InboxStyle");
        mServiceIntent.putExtra(NOTIFICATIONMSGID, msgId);
        PendingIntent pendingIntentNotificationService = PendingIntent.getService(this, 12, mServiceIntent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(pendingIntentNotificationService)
                .setLargeIcon(largeIcon);
        NotificationCompat.InboxStyle inboxStyle =
                new NotificationCompat.InboxStyle();
// Sets a title for the Inbox in expanded layout
        inboxStyle.setBigContentTitle(bigContentTitle);
// Moves events into the expanded layout
        for (int i=0; i < rows.length; i++) {

            inboxStyle.addLine(rows[i]);
        }
// Moves the expanded layout object into the notification object.
        mBuilder.setStyle(inboxStyle);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }

    private void sendNotificationBigText(String msgId, String title, String content, String veryLongText)throws IOException {

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_expanded_notification);


        Intent mServiceIntent = new Intent(getApplicationContext(), SDKNotificationHandler.class);
        mServiceIntent.setAction(ACTION_NOTIFICATION);
        mServiceIntent.putExtra(NOTIFICATIONID, "BigText");
        mServiceIntent.putExtra(NOTIFICATIONMSGID, msgId);


        PendingIntent pendingIntentNotificationService = PendingIntent.getService(this, 12, mServiceIntent, PendingIntent.FLAG_ONE_SHOT);


//        Intent intent = new Intent(this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP );
//        intent.putExtra(NOTIFICATIONID, true);
//        intent.putExtra(NOTIFICATIONMSGID, msgId);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 11, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(title)
                .setContentIntent(pendingIntentNotificationService)//(pendingIntent)
                .setContentText(content);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(veryLongText);
        mBuilder.setStyle(bigTextStyle);

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(3, mBuilder.build());

    }

}