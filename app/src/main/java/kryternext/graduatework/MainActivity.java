package kryternext.graduatework;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.bson.Document;

import kryternext.graduatework.app.models.Storage;
import kryternext.graduatework.app.models.User;
import kryternext.graduatework.app.models.UserAuth;

public class MainActivity extends AppCompatActivity {
    private Storage storage;
    private EditText usernameInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.storage = new Storage(this.getBaseContext(), "wms");
        usernameInput = (EditText) findViewById(R.id.username);
        passwordInput = (EditText) findViewById(R.id.password);
    }

    public void showAbout(View view) {
        Toast.makeText(this, "Copyright", Toast.LENGTH_SHORT).show();
    }

    public void logIn(View view) {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        if (username.isEmpty()) {
            Toast.makeText(this, "Username field is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "Password field is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        UserAuth user = new UserAuth(username, password);
        boolean authUser = this.storage.logIn(user);
        if (authUser) {
            Toast.makeText(this, "SUCCESS", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "LOL", Toast.LENGTH_SHORT).show();
        }
    }

    public void register(View view) {
        Toast.makeText(this, "Register", Toast.LENGTH_SHORT).show();
    }
}
