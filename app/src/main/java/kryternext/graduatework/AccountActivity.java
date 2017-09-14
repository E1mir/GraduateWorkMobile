package kryternext.graduatework;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Locale;

import kryternext.graduatework.app.models.User;

public class AccountActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView greeting;
    private TextView email;
    private MenuItem balance;
    private User user;
    private Timestamp timestamp;
    private ConstraintLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        user = (User) getIntent().getSerializableExtra("USER");
        content = (ConstraintLayout) findViewById(R.id.contentAccount);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        Menu menu = navigationView.getMenu();
        greeting = (TextView) header.findViewById(R.id.nav_greeting);
        email = (TextView) header.findViewById(R.id.nav_email);
        balance = menu.findItem(R.id.nav_balance);
        initHeader();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initHeader() {
        balance.setTitle(String.format(Locale.ENGLISH, "Balance: %.2f $", user.getBalance()));
        greeting.setText(String.format("Welcome %s", user.getUsername()));
        email.setText(user.getEmail());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        int id = item.getItemId();
        if (id == R.id.nav_balance) {
            // Handle the camera action
            return false;
        } else if (id == R.id.nav_info) {
            final int N = 11; // total number of textviews to add
            // create an empty array;
            for (int i = 0; i <= N; i++) {
                // create a new textview
                final TextView rowTextView = new TextView(this);
                // set some properties of rowTextView or something
                rowTextView.setText("This is row #" + i + "\n");
                content.addView(rowTextView);
                // add the textview to the linearlayout
                // save a reference to the textview for later
            }

            fab.hide();
        } else if (id == R.id.nav_orders) {
            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    timestamp = new Timestamp(System.currentTimeMillis());
                    Snackbar.make(view, String.valueOf(timestamp), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        } else if (id == R.id.nav_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Alert")
                    .setMessage("Do you really want to exit?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Toast.makeText(AccountActivity.this, "Logged out!", Toast.LENGTH_SHORT).show();
                            AccountActivity.this.finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
