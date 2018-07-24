package kvartira.kz.kvartira.Basic;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import kvartira.kz.kvartira.Activity.RealtorActivity;
import kvartira.kz.kvartira.Model.Order;
import kvartira.kz.kvartira.R;

/**
 * Created by Aslan on 24.03.2016.
 */
public class GCMIntentService extends IntentService {

    public GCMIntentService() {
        super("GCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                sendNotification("Error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                sendNotification("Deleted: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                try {
                    JSONObject json = new JSONObject(extras.get("message").toString());
                    String type = json.getString("type");
                    if (type.equals("Add")) {
                        updateDatabase(extras.get("message") + "", type);
                        sendNotification(extras.get("message") + "");
                    } else if (type.equals("Delete")) {
                        updateDatabase(extras.get("message") + "", type);
                    }
                } catch (Exception e) {
                }
            }
        }
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void updateDatabase(String message, String type) {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        try {
            JSONObject json = new JSONObject(message);
            if (type.equals("Add")) {
                databaseHelper.addOrder(new Order(json.getInt("id"), json.getString("user_name"), json.getString("user_surname"),
                        "", json.getString("city_name"), json.getString("user_phone"), json.getString("address"),
                        json.getInt("rooms"), json.getInt("price"), json.getString("notation"), json.getInt("residence"),
                        json.getString("date"), json.getString("time")));
            } else if (type.equals("Delete")) {
                databaseHelper.deleteOrder(json.getInt("id"));
            }
        } catch (Exception e) {
        }
    }

    private void sendNotification(String message) {
        Intent intent = new Intent(this, RealtorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder;
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);
        try {
            JSONObject json = new JSONObject(message);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            int residence = json.getInt("residence");
            if (residence == 1) {
                builder.setContentTitle("Посуточная");
            } else if (residence == 2) {
                builder.setContentTitle("Долгосрочная");
            }
            builder.setContentText(json.getString("address"));
            builder.setContentIntent(pendingIntent);
        } catch (Exception e) {
        }

        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS;
        defaults = defaults | Notification.DEFAULT_VIBRATE;
        defaults = defaults | Notification.DEFAULT_SOUND;

        builder.setDefaults(defaults);
        builder.setAutoCancel(true);
        manager.notify(0, builder.build());
    }
}
