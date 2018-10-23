package it.geosolutions.savemybike.data.service;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Random;

import it.geosolutions.savemybike.R;
import it.geosolutions.savemybike.data.Constants;
import it.geosolutions.savemybike.ui.activity.LoginActivity;

/**
 * Manager for user notification (tracks status update, badges, prizes)
 */
public class UserNotificationManager {
    private Context mCtx;
    private static UserNotificationManager mInstance;
    private static int NOTIFICATION_ID = 42;

    private UserNotificationManager(Context context) {
        mCtx = context;
        // create channels
        android.app.NotificationManager mNotificationManager =
                (android.app.NotificationManager) (android.app.NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // valid tracks
            NotificationChannel validChanel = new NotificationChannel(Constants.Channels.TRACKS_VALID_ID, Constants.Channels.TRACKS_VALID_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            validChanel.setShowBadge(true);
            mNotificationManager.createNotificationChannel(validChanel);

            // invalid tracks
            NotificationChannel invalidChannel = new NotificationChannel(Constants.Channels.TRACK_INVALID_ID, Constants.Channels.TRACKS_INVALID_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            invalidChannel.setShowBadge(false);
            mNotificationManager.createNotificationChannel(invalidChannel);
        }
    }
    public static synchronized UserNotificationManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new UserNotificationManager(context);
        }
        return mInstance;
    }
    public void notifyTrackValid() {
        android.app.NotificationManager mNotificationManager =
                (android.app.NotificationManager) (android.app.NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mCtx, Constants.Channels.TRACKS_VALID_ID)
                        .setSmallIcon(R.drawable.ic_line_track)
                        .setBadgeIconType(R.drawable.ic_line_track)
                        .setContentTitle(mCtx.getResources().getString(R.string.validation_error_title))
                        .setContentText(mCtx.getResources().getString(R.string.validation_error_description));

        Intent resultIntent = new Intent(mCtx, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        if (mNotificationManager != null) {
            mNotificationManager.notify( NOTIFICATION_ID , mBuilder.build());
        }
    }
    public void notifyTrackInvalid(String reason) {
        android.app.NotificationManager mNotificationManager =
                (android.app.NotificationManager) (android.app.NotificationManager) mCtx.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mCtx, Constants.Channels.TRACK_INVALID_ID)
                        .setSmallIcon(R.drawable.ic_line_track)
                        .setBadgeIconType(R.drawable.ic_line_track)
                        .setContentTitle(mCtx.getResources().getString(R.string.validation_error_title))
                        .setContentText(mCtx.getResources().getString(R.string.validation_error_description));

        Intent resultIntent = new Intent(mCtx, LoginActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);

        if (mNotificationManager != null) {
            mNotificationManager.notify( NOTIFICATION_ID , mBuilder.build());
        }
    }


}
