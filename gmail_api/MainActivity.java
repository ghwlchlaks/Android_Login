package com.example.ghwlc.email_test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressDialog dialog;
    EditText et;
    GMailSender sender;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bt = (Button) this.findViewById(R.id.bt_help);
        bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        et = (EditText) this.findViewById(R.id.et);
        sender = new GMailSender("ghwlchlaks@gmail.com", "zayqiedyuklawjov"); // SUBSTITUTE ID PASSWORD
        timeThread();
    }

    public void timeThread() {

        dialog = new ProgressDialog(this);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Wait...");
        dialog.setMessage("의견을 보내는 중입니다.");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
        new Thread(new Runnable() {

            public void run() {
                // TODO Auto-generated method stub
                try {
                    sender.sendMail("의견보내기", // subject.getText().toString(),
                            et.getText().toString(), // body.getText().toString(),
                            "test", // from.getText().toString(),
                            "ghwlchlaks@naver.com" // to.getText().toString()
                    );
                    sleep(3000);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                    Toast.makeText(MainActivity.this, "신청 실패", Toast.LENGTH_SHORT)
                            .show();

                }
                dialog.dismiss();
            }

            private void sleep(int i) {
                // TODO Auto-generated method stub

            }

        }).start();
    }

}
