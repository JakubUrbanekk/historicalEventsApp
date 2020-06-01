package com.example.inzynierka.addons;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.inzynierka.MainActivity;
import com.example.inzynierka.R;
import com.example.inzynierka.Report.ListOfReports.ListOfReportsActivity;

import org.apache.commons.lang3.StringUtils;

import androidx.appcompat.app.AlertDialog;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomAlertDialog {
     Activity context;

     public void createAlertDialog(int titleID, String messageID) {
          AlertDialog alertDialog = new AlertDialog.Builder(context).create();
          alertDialog.setTitle(titleID);
          alertDialog.setMessage(messageID);
          alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                  (dialog, which) -> {
                      Intent intent = new Intent(context, ListOfReportsActivity.class);
                      context.startActivity(intent);
                  }
          );
          alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Anuluj", (dialog, which) -> dialog.dismiss());

          alertDialog.show();
     }
}
