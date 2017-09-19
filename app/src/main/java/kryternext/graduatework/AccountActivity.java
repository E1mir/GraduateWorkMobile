package kryternext.graduatework;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import kryternext.graduatework.app.models.Storage;
import kryternext.graduatework.app.models.User;
import kryternext.graduatework.app.services.StringUtils;

public class AccountActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView greeting;
    private TextView email;
    private MenuItem balance;
    private User user;
    private RelativeLayout mainGreeting;
    private TableLayout accountInfo;
    private ListView ordersLV;
    private Storage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        user = (User) getIntent().getSerializableExtra("USER");
        storage = new Storage(this, "wms");
        storage.setActivity(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainGreeting = (RelativeLayout) findViewById(R.id.main_content_layout);
        accountInfo = (TableLayout) findViewById(R.id.account_content_layout);
        ordersLV = (ListView) findViewById(R.id.orders_content_layout);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.account, menu);
        return true;
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
        this.storage.refreshPage(user.getUsername().toLowerCase());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            this.storage.refreshPage(user.getUsername().toLowerCase());
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            mainGreeting.setVisibility(RelativeLayout.INVISIBLE);
            accountInfo.setVisibility(TableLayout.VISIBLE);
            ordersLV.setVisibility(ListView.INVISIBLE);
            fab.hide();
        } else if (id == R.id.nav_orders) {
            accountInfo.setVisibility(TableLayout.INVISIBLE);
            mainGreeting.setVisibility(RelativeLayout.INVISIBLE);
            ordersLV.setVisibility(ListView.VISIBLE);
            fab.show();
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AccountActivity.this, NewOrder.class);
                    intent.putExtra("USER", user);
                    startActivity(intent);
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

    private String getFormattedText(String field, String text) {
        return String.format("%s: %s", field, StringUtils.getCapitalizedText(text));
    }
}
