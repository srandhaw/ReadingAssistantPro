package edu.purdue.srandhaw.readingassistantpro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

/**
 * Created by sehajbirrandhawa on 1/5/18.
 */

public class WordMeaningActivity extends AppCompatActivity{
    EditText ettext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordmeaning);
        ettext = (EditText)findViewById(R.id.ettext);

    }
}
