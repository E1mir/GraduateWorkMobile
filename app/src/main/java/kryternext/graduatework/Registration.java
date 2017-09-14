package kryternext.graduatework;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import kryternext.graduatework.app.models.Storage;
import kryternext.graduatework.app.models.User;

public class Registration extends AppCompatActivity {
    private Storage storage;
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText shopName;
    private Spinner types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        this.storage = new Storage(this, "wms");
        this.storage.setActivity(this);
        username = (EditText) findViewById(R.id.usernameEditText);
        email = (EditText) findViewById(R.id.emailEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        shopName = (EditText) findViewById(R.id.shopEditText);
        types = (Spinner) findViewById(R.id.typeSpinner);

        this.storage.getTypes(types);
    }

    public void register(View view) {
        String username = this.username.getText().toString().toLowerCase().trim();
        String email = this.email.getText().toString().toLowerCase().trim();
        String password = this.password.getText().toString();
        String shopName = this.shopName.getText().toString().trim();
        String type = this.types.getSelectedItem().toString();
        if (username.isEmpty()) {
            alert("Username");
            return;
        }
        if (email.isEmpty()) {
            alert("Email");
            return;
        }
        if (password.isEmpty()) {
            alert("Password");
            return;
        }
        if (shopName.isEmpty()) {
            alert("Shop");
            return;
        }
        if (type.equals("Select type")) {
            Toast.makeText(this, "Select the type!", Toast.LENGTH_SHORT).show();
            return;
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setType(type);
        newUser.setShopName(shopName);
        newUser.setPassword(password);
        this.storage.register(newUser);
    }

    private void alert(String field) {
        Toast.makeText(this, String.format("%s field is empty!", field), Toast.LENGTH_SHORT).show();
    }
}
