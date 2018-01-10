package edu.purdue.srandhaw.readingassistantpro;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;

public class ToneActivity extends AppCompatActivity {
    ImageView iv;
    Button toneButt;
    TextView tv;
    String text ="";
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tone);
        iv = (ImageView) findViewById(R.id.imageView);
dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");
        toneButt = (Button) findViewById(R.id.toneButton);
        tv = (TextView) findViewById(R.id.textView3);
        String tempText = MainActivity.resultText.getText().toString();
        text = tempText.replaceAll(" ","%20");
        toneButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONTask().execute("https://api.dandelion.eu/datatxt/sent/v1/?lang=en&text="+text+"&token=b6524c0df9dc48d680244ad06033fbc4");
            }
        });


    }

    public class JSONTask extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader bfr = null;
            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                bfr = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String temp = "";
                while ((temp = bfr.readLine()) != null) {
                    if(temp.contains("negative")){
                        return "Negative";
                    }
                    else if(temp.contains("positive")){
                        return "Positive";
                    }
                    else if(temp.contains("neutral")){
                        return "Neutral";
                    }
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (bfr != null) {
                    try {
                        bfr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return "No internet connection found.";
        }


        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            tv.setText(result);
            if(result.equalsIgnoreCase("negative")){

                iv.setImageResource(R.drawable.sad);
            }
            else if(result.equalsIgnoreCase("positive")){

                iv.setImageResource(R.drawable.happy);
            }
            else if(result.equalsIgnoreCase("neutral")){

                iv.setImageResource(R.drawable.neutral);
            }


        }
    }


}
