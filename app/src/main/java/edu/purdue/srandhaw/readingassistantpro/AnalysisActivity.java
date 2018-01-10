package edu.purdue.srandhaw.readingassistantpro;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.purdue.srandhaw.readingassistantpro.models.Annotations;

/**
 * Created by sehajbirrandhawa on 1/1/18.
 */

public class AnalysisActivity extends AppCompatActivity implements View.OnClickListener {
Button imgbtn;
    ListView lvAnnotations;
    String text = "";
String spotImage;
ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setMessage("Loading...");
        imgbtn = (Button)findViewById(R.id.imgbtn);
        imgbtn.setOnClickListener(this);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
           .cacheInMemory(true)
                .cacheOnDisk(true)
           .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())

           .defaultDisplayImageOptions(defaultOptions)

           .build();
        ImageLoader.getInstance().init(config);
        lvAnnotations = (ListView) findViewById(R.id.lvAnnotations);

        String tempText = MainActivity.resultText.getText().toString();
        text = tempText.replaceAll(" ", "%20");
        new JSONTask().execute("https://api.dandelion.eu/datatxt/nex/v1/?text=" + text + "&include=types%2Cabstract%2Ccategories&token=b6524c0df9dc48d680244ad06033fbc4");


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgbtn:
            {
                startActivity(new Intent(this, ImageActivity.class));
                break;
            }

        }
    }

    public class JSONTask extends AsyncTask<String, String, List<Annotations>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected List<Annotations> doInBackground(String... strings) {
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
                String finalJson = buffer.toString();
                List<Annotations> annotationsList = new ArrayList<>();
                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray parentArray = parentObject.getJSONArray("annotations");
                StringBuffer finalBuferedData = new StringBuffer();
                for (int i = 0; i < parentArray.length(); i++) {
                    JSONObject finalObject = parentArray.getJSONObject(i);
                    Annotations annotations = new Annotations();
                    String id = finalObject.getString("spot");
                    annotations.setSpot(id);
                    String details = finalObject.getString("abstract");
                    annotations.setAbstractDetail(details);
                    annotationsList.add(annotations);
                }
                return annotationsList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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

            return null;
        }


        protected void onPostExecute(List<Annotations> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            AnnotationsAdapter adapter = new AnnotationsAdapter(getApplicationContext(), R.layout.row, result);
            lvAnnotations.setAdapter(adapter);

        }
    }



    public class AnnotationsAdapter extends ArrayAdapter {


        private List<Annotations> annotationsList;
        private int resource;
        private LayoutInflater inflater;

        public AnnotationsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Annotations> objects) {
            super(context, resource, objects);
            annotationsList = objects;
            this.resource = resource;
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(resource, null);
            }

            TextView tvSpot;
            TextView tvAbstract;

            tvSpot = (TextView) convertView.findViewById(R.id.tvSpot);
            tvAbstract = (TextView) convertView.findViewById(R.id.tvAbstract);
            spotImage=tvSpot.getText().toString();

           // ImageLoader.getInstance().displayImage("https://source.unsplash.com/?"+tvSpot.getText().toString(), ivSpot);



            tvSpot.setText(annotationsList.get(position).getSpot());
            tvAbstract.setText(annotationsList.get(position).getAbstractDetail());

            return convertView;

        }
    }

}
