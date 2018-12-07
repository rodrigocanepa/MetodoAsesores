package projects.solucionescolabora.com.metodoasesores;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;

/**
 * Created by rodrigocanepacruz on 06/11/18.
 */

public class NotificationHelper extends ContextWrapper {

    private static final String CHANNEL_ID = "metodoasesores";
    private static final String CHANNEL_NAME = "metodo channel";
    private NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    private SharedPreferences sharedPreferences;

    public NotificationHelper(Context base) {
        super(base);
        createChannels();
    }

    private void createChannels(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager().createNotificationChannel(notificationChannel);
        }

    }

    public NotificationManager getManager(){
        if(manager == null)
            manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        return manager;
    }

    public Notification.Builder getChanelNotification(String title, String body){

        Intent i = new Intent(this, LogOrRegActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentText(body)
                    .setContentTitle(title)
                    .setStyle(new Notification.BigTextStyle().bigText(body))
                    .setSmallIcon(R.drawable.m_sin_fondo)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
        }
        else{

            return new Notification.Builder(this)
                    .setSmallIcon(R.drawable.m_sin_fondo)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    //.setSound(soundUri)
                    .setContentIntent(pendingIntent);
        }
    }
}
