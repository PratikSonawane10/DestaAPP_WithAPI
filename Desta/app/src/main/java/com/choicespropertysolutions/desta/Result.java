package com.choicespropertysolutions.desta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    private TextView emptyText;
    private Toolbar resultToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        resultToolbar = (Toolbar) findViewById(R.id.resultToolbar);
        setSupportActionBar(resultToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        resultToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        
        emptyText = (TextView) findViewById(R.id.emptyText);
    }
}
