package edu.purdue.srandhaw.readingassistantpro;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static EditText resultText;
    Button oneButton;
    Button twoButton;

    static String inputString;

    public void promptSpeechInput(){
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"say something");
        try {
            startActivityForResult(i, 100);
        }
        catch(ActivityNotFoundException a){
            Toast.makeText(MainActivity.this,"sorry your device doesn't support speech language",Toast.LENGTH_LONG).show();
        }
    }



    public void onActivityResult(int request_code,int result_code,Intent i){
        super.onActivityResult(request_code,result_code,i);
        switch(request_code){
            case 100: if(result_code == RESULT_OK && i!=null){
                ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                resultText.setText(result.get(0));
                inputString=resultText.getText().toString();
            }
                break;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultText = (EditText)findViewById(R.id.result);
        oneButton = (Button)findViewById(R.id.button);
        twoButton = (Button)findViewById(R.id.button2);
        oneButton.setOnClickListener(this);
        twoButton.setOnClickListener(this);




    }
    public void onClick(View v){

        switch(v.getId())
        {
            case R.id.button2:
            {
                //open login screen
                Intent i = new Intent(MainActivity.this, OptionMenu.class);
                startActivity(i);
                break;
            }
            case R.id.button:
            {
                promptSpeechInput();
                break;
            }


        }
    }
}


