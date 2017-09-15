package kryternext.graduatework;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import kryternext.graduatework.app.models.Storage;
import kryternext.graduatework.app.models.UserAuth;
import kryternext.graduatework.app.services.CheckNetwork;

public class MainActivity extends AppCompatActivity {
    private Storage storage;
    private EditText usernameInput;
    private EditText passwordInput;
    private boolean isConnected;

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
            fieldAlert(view, "Username");
            return;
        }
        if (password.isEmpty()) {
            fieldAlert(view, "Password");
            return;
        }
        UserAuth user = new UserAuth(username, password);
        isConnected = CheckNetwork.isInternetAvailable(this);
        if (isConnected) {
            this.storage.logIn(user);
            passwordInput.setText("");
        } else {
            noInternetConnectionAlert(view);
        }
    }

    public void registration(View view) {
        isConnected = CheckNetwork.isInternetAvailable(this);
        if (isConnected) {
            Intent register = new Intent(this, Registration.class);
            startActivity(register);
        } else {
            noInternetConnectionAlert(view);
        }
    }

    private void noInternetConnectionAlert(View view) {
        Snackbar.make(view, "No internet connection!", Snackbar.LENGTH_LONG).setAction("Action", null).show();

    }

    void fieldAlert(View view, String field) {
        Snackbar.make(view, String.format("%s field is empty!", field), Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
}
