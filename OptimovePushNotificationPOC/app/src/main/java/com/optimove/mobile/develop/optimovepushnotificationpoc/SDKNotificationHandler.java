package com.optimove.mobile.develop.optimovepushnotificationpoc;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import org.piwik.sdk.PiwikApplication;
import org.piwik.sdk.TrackHelper;
import org.piwik.sdk.Tracker;

import java.util.Calendar;
import java.util.Date;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SDKNotificationHandler extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.optimove.mobile.develop.optimovepushnotificationpoc.action.FOO";
    private static final String ACTION_BAZ = "com.optimove.mobile.develop.optimovepushnotificationpoc.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.optimove.mobile.develop.optimovepushnotificationpoc.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.optimove.mobile.develop.optimovepushnotificationpoc.extra.PARAM2";

    private static final String ACTION_NOTIFICATION = "com.optimove.mobile.develop.optimovepushnotificationpoc.action.notification";
    private static final String NOTIFICATIONID = "fromNotification";
    private static final String NOTIFICATIONMSGID ="NOTIICATION_MSGID";

    public SDKNotificationHandler() {
        super("SDKNotificationHandler");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SDKNotificationHandler.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, SDKNotificationHandler.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_NOTIFICATION.equals(action)) {
                final String notificationId = intent.getStringExtra(NOTIFICATIONID);
                final String notificationMsgId = intent.getStringExtra(NOTIFICATIONMSGID);
                handleActionFoo(notificationId, notificationMsgId);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String notification, String msgId) {
        // TODO: Handle action Foo

        Date currentTime = Calendar.getInstance().getTime();
        TrackHelper.track()
                .screen("/Main Screen")
                .title("Main Activity")
                .variable(1, "InteractionNotificationType", notification)
                .variable(2, "NotificationMsgId", msgId)
                .variable(3, "NotificationActivation", currentTime.toString())
                .with(getTracker());
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Tracker getTracker() {
        return ((PiwikApplication) getApplication()).getTracker();
    }
}
