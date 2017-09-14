package kryternext.graduatework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import kryternext.graduatework.R;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
    }

    public void register(View view) {
        this.finish();
    }
}
