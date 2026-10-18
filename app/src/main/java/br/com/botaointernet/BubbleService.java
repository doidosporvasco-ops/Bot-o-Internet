package br.com.botaointernet;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.os.*;
import android.provider.Settings;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
public class BubbleService extends Service {
    public static final String START="br.com.botaointernet.START", STOP="br.com.botaointernet.STOP";
    private static final String CHANNEL="botao_internet_servico";
    private WindowManager wm; private TextView bubble; private WindowManager.LayoutParams params;
    private int dp(int v){return (int)(v*getResources().getDisplayMetrics().density+.5f);}
    @Override public void onCreate(){super.onCreate(); NotificationManager nm=getSystemService(NotificationManager.class); nm.createNotificationChannel(new NotificationChannel(CHANNEL,"Botão flutuante",NotificationManager.IMPORTANCE_LOW));}
    private Notification notification(){Intent open=new Intent(this,MainActivity.class); PendingIntent pi=PendingIntent.getActivity(this,0,open,PendingIntent.FLAG_IMMUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
        return new Notification.Builder(this,CHANNEL).setSmallIcon(android.R.drawable.ic_menu_manage).setContentTitle("Botão Internet ativo").setContentText("Toque no círculo para abrir o painel de internet").setContentIntent(pi).setOngoing(true).build();}
    @Override public int onStartCommand(Intent intent,int flags,int startId){if(intent!=null && STOP.equals(intent.getAction())){stopSelf();return START_NOT_STICKY;}
        startForeground(1001,notification()); if(bubble==null && Settings.canDrawOverlays(this)) showBubble(); return START_STICKY;}
    private void showBubble(){wm=(WindowManager)getSystemService(WINDOW_SERVICE); bubble=new TextView(this); bubble.setText("↕"); bubble.setTextSize(27); bubble.setTextColor(Color.WHITE); bubble.setGravity(Gravity.CENTER);
        GradientDrawable bg=new GradientDrawable(); bg.setColor(Color.rgb(26,115,232)); bg.setCornerRadius(dp(60)); bg.setStroke(dp(2),Color.WHITE); bubble.setBackground(bg); bubble.setElevation(dp(6));
        params=new WindowManager.LayoutParams(dp(58),dp(58),WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,PixelFormat.TRANSLUCENT); params.gravity=Gravity.TOP|Gravity.START; params.x=dp(8); params.y=dp(230);
        bubble.setOnTouchListener(new View.OnTouchListener(){int sx,sy;float downX,downY;boolean moved;
            @Override public boolean onTouch(View v,android.view.MotionEvent e){switch(e.getAction()){
                case MotionEvent.ACTION_DOWN:sx=params.x;sy=params.y;downX=e.getRawX();downY=e.getRawY();moved=false;return true;
                case MotionEvent.ACTION_MOVE: if(Math.abs(e.getRawX()-downX)>dp(8)||Math.abs(e.getRawY()-downY)>dp(8)) moved=true;
                    if(moved){params.x=sx+(int)(e.getRawX()-downX);params.y=sy+(int)(e.getRawY()-downY);wm.updateViewLayout(bubble,params);}return true;
                case MotionEvent.ACTION_UP:if(!moved) openInternetPanel();return true;
            }return false;}});wm.addView(bubble,params);
    }
    private void openInternetPanel(){try{Intent i=new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);startActivity(i);}catch(Exception ex){try{Intent i=new Intent(Settings.ACTION_WIRELESS_SETTINGS);i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);startActivity(i);}catch(Exception ignored){Toast.makeText(this,"Não foi possível abrir as configurações",Toast.LENGTH_SHORT).show();}}}
    @Override public void onDestroy(){if(wm!=null && bubble!=null){try{wm.removeView(bubble);}catch(Exception ignored){} bubble=null;}super.onDestroy();}
    @Override public android.os.IBinder onBind(Intent intent){return null;}
}
