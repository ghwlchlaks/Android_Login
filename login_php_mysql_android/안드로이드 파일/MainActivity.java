package com.example.ghwlc.db_test;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "phptest_MainActivity";

    private EditText mEditTextName;
    private EditText mEditTextAddress;
    private EditText mEditTextPassword;
    private EditText mEditTextId;
    private TextView mTextViewResult;
    String inputaddress="";
    String findID="";
    String findPass="";
    String id="";
    String password="";
    //email
    ProgressDialog dialog;
    GMailSender sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditTextId = (EditText)findViewById(R.id.editText_main_id);
        mEditTextPassword = (EditText)findViewById(R.id.editText_main_password);
        mEditTextName = (EditText)findViewById(R.id.editText_main_name);
        mEditTextAddress = (EditText)findViewById(R.id.editText_main_address);

        mTextViewResult = (TextView)findViewById(R.id.textView_main_result);

        Button buttonInsert = (Button)findViewById(R.id.button_main_insert);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = mEditTextId.getText().toString();
                password = mEditTextPassword.getText().toString();
                String name = mEditTextName.getText().toString();
                String address = mEditTextAddress.getText().toString();

                InsertData task = new InsertData();
                task.execute(id,password,name,address);

                mEditTextName.setText("");
                mEditTextAddress.setText("");
                mEditTextId.setText("");
                mEditTextPassword.setText("");
            }
        });

        Button buttonLogin= (Button)findViewById(R.id.button_main_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 id = mEditTextId.getText().toString();
                 password = mEditTextPassword.getText().toString();

                LoginData task = new LoginData();
                task.execute(id,password);

                mEditTextId.setText("");
                mEditTextPassword.setText("");


            }
        });

        Button buttonID = (Button)findViewById(R.id.button_main_findid);
        buttonID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputaddress = mEditTextAddress.getText().toString();

                findIdEmail task = new findIdEmail();
                task.execute(inputaddress);
                mEditTextAddress.setText("");
            }
        });

        Button buttonPass = (Button)findViewById(R.id.button_main_findpassword);
        buttonPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputaddress= mEditTextAddress.getText().toString();
                String id = mEditTextId.getText().toString();

                findPassEmail task = new findPassEmail();
                task.execute(id,inputaddress);

                mEditTextAddress.setText("");
                mEditTextId.setText("");
            }
        });
    }
    public class findPassEmail extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("이메일 상태");
            builder.setPositiveButton("확인",null);

            progressDialog.dismiss();
            mTextViewResult.setText(result);

            String[] result_split = result.split("<body>");

            String[] final_result = result_split[1].split("</body>");
            String status = final_result[0].split(",")[0];
            String password = final_result[0].split(",")[1];

            if(status.equals("fail"))
            {
                builder.setMessage("이메일 정보 또는 아이디가 일치하지 않습니다.");
            }
            else if (status.equals("success"))
            {
                findPass =  password ;
                sender = new GMailSender("ghwlchlaks@gmail.com","zayqiedyuklawjov");
                PasstimeThread();
                builder.setMessage("회원님의 비밀번호 보냈습니다요!");
            }
            else
            {
                builder.setMessage("데이터베이스 오류");
            }
            builder.show();

            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {
            String id = (String)params[0];
            String address = (String)params[1];

            String serverURL = "http://192.168.152.128/findpass.php";
            String postParameters ="id=" +id+ "&email=" + address;

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

                Log.d(TAG, "EmailData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
    public void PasstimeThread() {

        dialog = new ProgressDialog(this);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Wait...");
        dialog.setMessage("의견을 보내는 중입니다.");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    sender.sendMail("과제)회원님의 비밀번호 왔어요~", // subject.getText().toString(),
                            "회원님의 비밀번호는 : "+findPass +" 입니다.", // body.getText().toString(),
                            "ghwlchlaks@gmail.com", // from.getText().toString(),
                            inputaddress // to.getText().toString()
                    );
                    sleep(3000);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                    Toast.makeText(MainActivity.this, "신청 실패", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
            private void sleep(int i) {
            }
        }).start();
    }

    public class findIdEmail extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("이메일 상태");
            builder.setPositiveButton("확인",null);

            progressDialog.dismiss();
            mTextViewResult.setText(result);

            String[] result_split = result.split("<body>");

            String[] final_result = result_split[1].split("</body>");
            String status = final_result[0].split(",")[0];
            String id = final_result[0].split(",")[1];


           if(status.equals("fail"))
            {
                builder.setMessage("이메일 정보가 일치하지 않습니다.");
            }
            else if (status.equals("success"))
            {
                findID =  id ;
                sender = new GMailSender("ghwlchlaks@gmail.com","zayqiedyuklawjov");
                IDtimeThread();
                builder.setMessage("회원님의 아이디 보냈습니다요!");
            }
            else
            {
                builder.setMessage("이메일 정보 오류");
            }
            builder.show();

            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String address = (String)params[0];

            String serverURL = "http://192.168.152.128/findid.php";
            String postParameters ="email=" + address;

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

                Log.d(TAG, "EmailData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
    public void IDtimeThread() {

        dialog = new ProgressDialog(this);
        dialog = new ProgressDialog(this);
        dialog.setTitle("Wait...");
        dialog.setMessage("의견을 보내는 중입니다.");
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                   sender.sendMail("과제)회원님의 아이디 왔어요~", // subject.getText().toString(),
                           "회원님의 아이디는 : "+findID +" 입니다.", // body.getText().toString(),
                            "ghwlchlaks@gmail.com", // from.getText().toString(),
                            inputaddress // to.getText().toString()
                    );
                    sleep(3000);
                } catch (Exception e) {
                    Log.e("SendMail", e.getMessage(), e);
                    Toast.makeText(MainActivity.this, "신청 실패", Toast.LENGTH_SHORT).show();
                }
               dialog.dismiss();
           }
            private void sleep(int i) {
            }
        }).start();
    }

    public class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("로그인 상태");
            builder.setPositiveButton("확인",null);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            String[] result_split = result.split("<body>");

            String[] final_result = result_split[1].split("</body>");
            Toast.makeText(getBaseContext(),final_result[0],Toast.LENGTH_SHORT).show();
            if(final_result[0].equals("fail"))
            {
                builder.setMessage("동일한 id가 있습니다.");
            }
            else if (final_result[0].equals("success"))
            {
                builder.setMessage("회원가입 성공");
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
            String name = (String)params[2];
            String address = (String)params[3];

            String serverURL = "http://192.168.152.128/insert.php";
            String postParameters ="id="+id+"&password="+password+ "&name=" + name + "&address=" + address;


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

    public class LoginData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("로그인 상태");
            builder.setPositiveButton("확인",null);

            progressDialog.dismiss();
            mTextViewResult.setText(result);
            String[] result_split = result.split("<body>");

            String[] final_result = result_split[1].split("</body>");
            Toast.makeText(getBaseContext(),final_result[0],Toast.LENGTH_SHORT).show();
            if(final_result[0].equals("fail"))
            {
                builder.setMessage("로그인 정보가 일치하지 않습니다.");
            }
            else if (final_result[0].equals("success"))
            {
                builder.setMessage("로그인 성공!");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent login_intent = new Intent(getApplicationContext(),LoginActivity.class);
                        login_intent.putExtra("id",id);
                        login_intent.putExtra("password",password);
                        Log.e("mainactivity",id+" "+password);
                        startActivity(login_intent);
                    }
                });

            }
            else
            {
                builder.setMessage("로그인 정보가 일치하지 않습니다.");
            }
            builder.show();

            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {
            String id = (String)params[0];
            String password =(String)params[1];

            String serverURL = "http://192.168.152.128/login.php";
            String postParameters ="id="+id+"&password="+password;


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
