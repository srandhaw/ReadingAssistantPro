package edu.purdue.srandhaw.readingassistantpro;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;


public class ImageActivity extends AppCompatActivity {
    Button btn;
    ImageView iv;
    EditText et;
    TextView tv;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");
        tv = (TextView)findViewById(R.id.tv);
        et=(EditText)findViewById(R.id.etSpot);
        iv=(ImageView)findViewById(R.id.iv);
        btn=(Button)findViewById(R.id.imgbtn) ;


        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())

                .defaultDisplayImageOptions(defaultOptions)

                .build();
        ImageLoader.getInstance().init(config);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new JSONTask().execute("https://www.googleapis.com/customsearch/v1?key=AIzaSyCdwNHO94vgWoWmbl6z5PL9s4s1AD6VFyw&cx=000409344529889938358:t4tjuuhxmhy&q=" +et.getText());
String text =et.getText().toString();
                if(text.contains(" ")){
                    text = text.replaceAll(" ","+");
                }
                    new JSONTask().execute("https://pixabay.com/api/?key=7602257-8c07be9b15e4ce8cfaa826f6d&q="+text +"&image_type=photo");


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
                    buffer.append(temp);
                }

                String temp1 = buffer.toString();
//                String temp2 = temp1.substring(temp1.indexOf("src"));
//                String temp3 = temp2.substring(temp2.indexOf("kind"));
//                String temp4 = temp3.substring(temp3.indexOf("\"https"));
//                String temp5 = temp4.substring(temp4.indexOf("\"src"));
//                String temp6 = temp5.substring(temp5.indexOf("https"));
//                String finalImageUrl = temp6.substring(0,temp6.indexOf("\""));
//                String temp2 = temp1.substring(temp1.indexOf("\"contentUrl\":"));
//                String temp3 = temp2.substring(temp2.indexOf(": \""));
//                String imageUrl=temp3.substring(temp3.indexOf("htt"),temp3.indexOf("\","));
                String temp2 = temp1.substring(temp1.indexOf("\"previewURL\":\"h"),temp1.indexOf(".jpg"));
                temp2=temp2.replace("\"previewURL\":\"","");
                temp2=temp2+".jpg";
                return temp2;
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
            ImageLoader.getInstance().displayImage(result, iv);






        }
    }


}
