package com.example.secondassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class SecondActivity extends AppCompatActivity {
    private EditText edtName;
    private EditText edtDirectedBy;
    private EditText edtStarring;
    private EditText edtOpeningOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        edtName=findViewById(R.id.edtName);
        edtDirectedBy=findViewById(R.id.edtDirectedBy);
        edtStarring=findViewById(R.id.edtStarring);
        edtOpeningOn=findViewById(R.id.edtOpeningOn);
    }

    public void btnAddOnClick(View view) {
        String restURL="http://192.168.1.108:80/rest/addMovie.php";
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},1234);
        }else{
            SendPostRequest runner=new SendPostRequest();
            runner.execute(restURL);
        }
        Intent intent=new Intent();
        setResult(RESULT_OK,intent);
        finish();
    }


    private String processRequest(String restURL)throws UnsupportedEncodingException{
        String name=edtName.getText().toString();
        String directedBy=edtDirectedBy.getText().toString();
        String starring=edtStarring.getText().toString();
        String openingOn=edtOpeningOn.getText().toString();

        String data= URLEncoder.encode("name","UTF-8")
                +"="+URLEncoder.encode(name,"UTF-8");
        data+="&"+URLEncoder.encode("directedBy","UTF-8")
                +"="+URLEncoder.encode(directedBy,"UTF-8");
        data+="&"+URLEncoder.encode("starring","UTF-8")
                +"="+URLEncoder.encode(starring,"UTF-8");
        data+="&"+URLEncoder.encode("openingOn","UTF-8")
                +"="+URLEncoder.encode(openingOn,"UTF-8");

        String text="";
        BufferedReader bufferedReader=null;

        try{
            URL url=new URL(restURL);
            URLConnection urlConnection=url.openConnection();
            urlConnection.setDoOutput(true);
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(urlConnection.getOutputStream());
            outputStreamWriter.write(data);
            outputStreamWriter.flush();

            bufferedReader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder=new StringBuilder();
            String line="";

            while ((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line+"\n");
            }

            text=stringBuilder.toString();
        }catch (Exception e){
            Log.d("Exception",e.getLocalizedMessage());
        }finally {
            try{
                bufferedReader.close();
            }catch (Exception e){
                Log.d("Exception",e.getLocalizedMessage());
            }
        }

        return text;
    }

    private class SendPostRequest extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls) {
            try{
                return processRequest(urls[0]);
            }catch (UnsupportedEncodingException e){
                Log.d("Exception",e.getLocalizedMessage());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(SecondActivity.this,s,Toast.LENGTH_LONG).show();
        }
    }
}
