package com.example.secondassignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ListView listMovies;
    final private int REQUEST_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listMovies=findViewById(R.id.listMovies);
        String url="http://192.168.1.108:80/rest/listMovies.php";
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},1234);
        }else{
            DownloadTextTask runner=new DownloadTextTask();
            runner.execute(url);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    public void addOptionOnClick(MenuItem item) {
        startActivityForResult(new Intent("android.intent.action.SecondActivity"),REQUEST_CODE);
    }

    public void onActivityResult(int requestCode,int resultCode, Intent intent){
        String urlUpdate="http://192.168.1.108:80/rest/listMovies.php";
        if(requestCode==requestCode){
            if(resultCode==RESULT_OK)
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET},1234);
            }else{
                DownloadTextTask runner=new DownloadTextTask();
                runner.execute(urlUpdate);
            }
        }
    }

    private void populateListView(ArrayList<String> list) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item, list);
        listMovies.setAdapter(arrayAdapter);

        listMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this,DetailsActivity.class);
                intent.putExtra("Movie",(position+1));
                startActivity(intent);
            }
        });
    }
    private InputStream openHttpConnection(String urlString) throws IOException {
        InputStream inputStream=null;
        int response=-1;

        URL url=new URL(urlString);
        URLConnection urlConnection=url.openConnection();

        if(!(urlConnection instanceof HttpURLConnection)){
            throw new IOException("Not an HTTP connection");
        }
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setAllowUserInteraction(false);
            httpURLConnection.setInstanceFollowRedirects(true);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            response = httpURLConnection.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
            }
        }catch (Exception e){
            Log.d("networking",e.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return inputStream;
    }

    private String downloadText(String urlString){
        int BUFFER_SIZE=2000;
        InputStream inputStream=null;
        try{
            inputStream=openHttpConnection(urlString);
        }catch (IOException e){
            Log.d("networking",e.getLocalizedMessage());
            return "";
        }

        InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
        int charRead;
        String stringResult="";
        char[] inputBuffer=new char[BUFFER_SIZE];
        try{
            while((charRead=inputStreamReader.read(inputBuffer))>0){
                String readString=String.valueOf(inputBuffer,0,charRead);
                stringResult+=readString;
                inputBuffer=new char[BUFFER_SIZE];
            }
            inputStream.close();
        }catch (IOException e){
            Log.d("networking",e.getLocalizedMessage());
            return "";
        }
        return stringResult;
    }

    private class DownloadTextTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls) {
            return downloadText(urls[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            Gson gson=new Gson();
            Type type=new TypeToken<ArrayList<String>>(){}.getType();
            ArrayList<String> list=gson.fromJson(s,type);
            populateListView(list);
        }
    }
}
