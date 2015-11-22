package com.ridesforme.ridesforme.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.android.gms.gcm.Task;
import com.ridesforme.ridesforme.DetalhePesquisaCaronaActivity;
import com.ridesforme.ridesforme.R;

/**
 * Created by Marcos Antônio on 10/10/2015.
 */
public class NotificationUtils {

    private static PendingIntent criarPendingIntent(Context contexto, String texto, int id){
        Intent intent = new Intent(contexto, DetalhePesquisaCaronaActivity.class);
        intent.putExtra("notificacao_solicitar_carona", texto);

        TaskStackBuilder mBuilder = TaskStackBuilder.create(contexto);
        mBuilder.addParentStack(DetalhePesquisaCaronaActivity.class);
        mBuilder.addNextIntent(intent);

        return mBuilder.getPendingIntent(id, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void criarNotificacao(Context context, String texto, int id){
        PendingIntent pendingIntent = criarPendingIntent(context, texto, id);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setDefaults(Notification.DEFAULT_ALL);
        notificationBuilder.setSmallIcon(R.drawable.ic_carona);
        notificationBuilder.setContentTitle(context.getString(R.string.title_notificacao_SolicitarCarona));
        notificationBuilder.setContentText(texto);
        notificationBuilder.setContentIntent(pendingIntent);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);

        managerCompat.notify(id, notificationBuilder.build());
    }







    public void removerNotificacao(Context contexto){
        NotificationManagerCompat nm = NotificationManagerCompat.from(contexto);
        nm.cancel(1);
    }
}
