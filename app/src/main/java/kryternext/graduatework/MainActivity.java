package kryternext.graduatework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import kryternext.graduatework.app.DatabaseController;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void showAbout(View view) {
        Toast.makeText(this, "Copyright", Toast.LENGTH_SHORT).show();
    }

    public void logIn(View view) {
        //Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();
        try {
            DatabaseController controller = new DatabaseController(this);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void register(View view) {
        Toast.makeText(this, "Register", Toast.LENGTH_SHORT).show();
    }
}
