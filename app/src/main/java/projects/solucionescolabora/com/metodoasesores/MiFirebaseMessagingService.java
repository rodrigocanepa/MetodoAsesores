package projects.solucionescolabora.com.metodoasesores;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.SyncStateContract;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import projects.solucionescolabora.com.metodoasesores.Fragments.ConsultoresFragment;

/**
 * Created by rodrigocanepacruz on 06/11/18.
 */

public class MiFirebaseMessagingService extends FirebaseMessagingService {

    //private NotificationManager notifManager;
    private NotificationHelper notificationHelper;

    private SharedPreferences sharedPreferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String from = remoteMessage.getFrom();
        Log.i("FIREBASE", from);
        notificationHelper = new NotificationHelper(this);

        if(remoteMessage.getNotification() != null){
            Log.d("NOTIFICACION", remoteMessage.getNotification().getBody());
            Notification.Builder builder = notificationHelper.getChanelNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            notificationHelper.getManager().notify(0, builder.build());
            //mostrarNotifiacion(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            }


        if(remoteMessage.getData().size()>0){

        }

    }

    private void mostrarNotifiacion(String title, String body){


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "hola";
            String description = "es una prueba";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("prueba", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        // show notification
        Intent intent = new Intent(this, LogOrRegActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // 0 is request code
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(body)
                        .setAutoCancel(true)
                        //.setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);


        // 0 is id of notification
        notificationManager.notify(0, notificationBuilder.build());
    }
}
