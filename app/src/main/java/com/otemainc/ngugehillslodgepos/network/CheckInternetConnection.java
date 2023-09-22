package com.otemainc.ngugehillslodgepos.network;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.provider.Settings;

import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.otemainc.ngugehillslodgepos.R;

public class CheckInternetConnection {
    Context ctx;

    public CheckInternetConnection(Context context){
        ctx=context;
    }

    public void checkConnection(){

        if(!isInternetConnected()) {
            final FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(ctx)
                    .setBackgroundColor(R.color.primary)
                    .setimageResource(R.drawable.internetconnection)
                    .setTextTitle(R.string.no_internet)
                    .setTextSubTitle(R.string.no_connection_message)
                    .setBody(R.string.no_connection)
                    .setPositiveButtonText(R.string.connect_now)
                    .setPositiveColor(R.color.secondary)
                    .setOnPositiveClicked((view, dialog) -> {
                        if(isInternetConnected()){
                            dialog.dismiss();
                        }else {
                            Intent dialogIntent = new Intent(Settings.ACTION_SETTINGS);
                            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            ctx.startActivity(dialogIntent);
                        }
                    })
                    .setNegativeButtonText(R.string.cancel)
                    .setNegativeColor(R.color.red)
                    .setOnNegativeClicked((view, dialog) -> dialog.dismiss())
                    .setBodyGravity(FancyAlertDialog.TextGravity.CENTER)
                    .setTitleGravity(FancyAlertDialog.TextGravity.CENTER)
                    .setSubtitleGravity(FancyAlertDialog.TextGravity.CENTER)
                    .setCancelable(false)
                    .build();
            alert.show();
        }
    }

    private boolean isInternetConnected() {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

}
