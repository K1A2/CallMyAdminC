package kr.co.aperturedev.callmyadminc.core.context.excuter;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import org.json.JSONException;

import kr.co.aperturedev.callmyadminc.R;
import kr.co.aperturedev.callmyadminc.internet.realtime.packet.ResponsePacket;
import kr.co.aperturedev.callmyadminc.view.activitys.TabHost;

/**
 * Created by jckim on 2017-11-30.
 */

public class ServerRequestExcuter {

    private NotificationManager notificationManager;
    private ResponsePacket responsePacket;
    private Context context;

    public ServerRequestExcuter(ResponsePacket responsePacket, Context context) {
        this.responsePacket = responsePacket;
        this.context = context;
    }

    public void requestExqute() {
        try {
            String message = responsePacket.getMessage();

            if (message.equals("call-admin")) {
                notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                Intent intent = new Intent(context, TabHost.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent content = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);



                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setAutoCancel(true);
                builder.setContentTitle("CallMyAdmin");
                builder.setContentText("호출이 도착했습니다.\n확인하세요.");
                builder.setContentIntent(content);

                notificationManager.notify(75612, builder.build());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
