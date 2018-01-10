package edu.purdue.srandhaw.readingassistantpro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionMenu extends AppCompatActivity implements View.OnClickListener {
    Button tone,back,entity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_menu);
        tone = (Button)findViewById(R.id.ToneAnal);
        tone.setOnClickListener(this);
        back = (Button)findViewById(R.id.backButton);
        back.setOnClickListener(this);
        entity = (Button)findViewById(R.id.button3);
        entity.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ToneAnal:
            {
                startActivity(new Intent(this,ToneActivity.class));
                break;
            }
            case R.id.backButton:
            {
                onBackPressed();
                break;
            }
            case R.id.button3:
            {
                startActivity(new Intent(this,AnalysisActivity.class));
                break;
            }

        }
    }
}
