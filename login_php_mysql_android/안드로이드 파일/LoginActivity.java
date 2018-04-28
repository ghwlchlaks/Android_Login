package com.example.ghwlc.db_test;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG ="login Activity" ;
    TextView changeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        changeText = (TextView)findViewById(R.id.changeText);

        Intent intent = getIntent();
        final String id= intent.getExtras().getString("id");
        final String password= intent.getExtras().getString("password");

        Button view_dialog = (Button)findViewById(R.id.changeBtn);
        view_dialog.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)
            {

                LayoutInflater inflater = getLayoutInflater();
                final View dialogView= inflater.inflate(R.layout.change_profile,null);

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("비밀번호 변경");
                builder.setView(dialogView);
                builder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText change_pass = (EditText)dialogView.findViewById(R.id.changePass);
                        String changedpass = change_pass.getText().toString();

                        ChangedPass task= new ChangedPass();
                        task.execute(id,password,changedpass);
                        Log.e("id,pass,changedpass " , id+ " " +password+" "+changedpass);
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        beforeActivity();
                    }
                });
                builder.show();
            }
        });

        Button backBtn = (Button)findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view)
            {
                Intent backIntent = new Intent(getBaseContext(),MainActivity.class);
                startActivity(backIntent);
            }
        });
    }
    public void beforeActivity()
    {
        Intent before_intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(before_intent);
    }
    public class ChangedPass extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(LoginActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setTitle("변경 상태");
            builder.setPositiveButton("확인",null);

            progressDialog.dismiss();
            changeText.setText(result);
            String[] result_split = result.split("<body>");

            String[] final_result = result_split[1].split("</body>");
            String status = final_result[0].split(",")[0];
            String changedpass = final_result[0].split(",")[1];
            Toast.makeText(getBaseContext(),final_result[0],Toast.LENGTH_SHORT).show();

            if(status.equals("fail"))
            {
                builder.setMessage("변경 실패");
            }
            else if (status.equals("success"))
            {
                builder.setMessage("비밀번호를 "+changedpass+"로 변경 하였습니다.");
            }
            else
            {
                builder.setMessage("오류가 발생했습니다.");
            }
            builder.show();

        }


        @Override
        protected String doInBackground(String... params) {
            String id = (String)params[0];
            String password =(String)params[1];
            String changedpass = (String)params[2];

            String serverURL = "http://192.168.152.128/changedPass.php";
            String postParameters ="id="+id+"&password="+password+"&changedpass="+changedpass;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                //httpURLConnection.setRequestProperty("content-type", "application/json");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
}
