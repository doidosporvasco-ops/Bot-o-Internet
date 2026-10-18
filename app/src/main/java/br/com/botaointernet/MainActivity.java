package br.com.botaointernet;

import android.app.*;
import android.os.*;
import android.content.*;
import android.provider.Settings;
import android.net.Uri;
import android.graphics.Color;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {
    private TextView status;
    private int dp(float v) { return (int)(v*getResources().getDisplayMetrics().density+.5f); }
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        LinearLayout root = new LinearLayout(this); root.setOrientation(1); root.setPadding(dp(24),dp(36),dp(24),dp(24)); root.setBackgroundColor(Color.rgb(247,249,252));
        TextView title = new TextView(this); title.setText("Botão Internet"); title.setTextSize(27); title.setTextColor(Color.rgb(22,34,54)); title.setTypeface(null,1); root.addView(title);
        TextView desc = new TextView(this); desc.setText("Atalho flutuante para o painel de internet do Android 11+. O botão não altera os dados móveis sozinho."); desc.setTextSize(16); desc.setPadding(0,dp(14),0,dp(18)); root.addView(desc);
        status = new TextView(this); status.setTextSize(15); root.addView(status);
        addButton(root,"1. Autorizar botão sobre outros apps",()->{Intent i=new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:"+getPackageName())); startActivity(i);});
        addButton(root,"2. Iniciar botão flutuante",()->{
            if(!Settings.canDrawOverlays(this)) { Toast.makeText(this,"Autorize a sobreposição primeiro",Toast.LENGTH_LONG).show(); return; }
            if(Build.VERSION.SDK_INT>=33 && checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)!=android.content.pm.PackageManager.PERMISSION_GRANTED) requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS},42);
            Intent i=new Intent(this,BubbleService.class); i.setAction(BubbleService.START); startForegroundService(i); updateStatus();
        });
        addButton(root,"Parar botão flutuante",()->{Intent i=new Intent(this,BubbleService.class); i.setAction(BubbleService.STOP); startService(i); updateStatus();});
        TextView note=new TextView(this); note.setText("Configurações do botão: toque e arraste para mover. Toque uma vez para abrir o painel. A cor azul indica que o atalho está disponível, NÃO o estado dos dados móveis.\n\nPrivacidade: sem internet, anúncios, rastreamento ou acessibilidade."); note.setPadding(0,dp(22),0,0); note.setTextSize(14); root.addView(note);
        ScrollView scroll=new ScrollView(this); scroll.addView(root); setContentView(scroll);
    }
    private void addButton(LinearLayout root,String label,Runnable action) { Button b=new Button(this); b.setText(label); root.addView(b,new LinearLayout.LayoutParams(-1,dp(60))); b.setOnClickListener(v->action.run()); }
    private void updateStatus(){status.setText(Settings.canDrawOverlays(this)?"Permissão de sobreposição: autorizada":"Permissão de sobreposição: pendente");}
    @Override protected void onResume(){super.onResume();updateStatus();}
}
