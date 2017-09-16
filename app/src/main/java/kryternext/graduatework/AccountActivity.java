package kryternext.graduatework;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.Locale;

import kryternext.graduatework.app.models.User;
import kryternext.graduatework.app.services.StringUtils;

public class AccountActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView greeting;
    private TextView email;
    private MenuItem balance;
    private User user;
    private Timestamp timestamp;
    private RelativeLayout mainGreeting;
    private TableLayout accountInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        user = (User) getIntent().getSerializableExtra("USER");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mainGreeting = (RelativeLayout) findViewById(R.id.main_content_layout);
        accountInfo = (TableLayout) findViewById(R.id.account_content_layout);
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
            //LinearLayout accountInfoLayout = (LinearLayout) findViewById(R.id.account_information_content_layout);
            mainGreeting.setVisibility(RelativeLayout.INVISIBLE);
            accountInfo.setVisibility(TableLayout.VISIBLE);
            String username = user.getUsername();
            String email = user.getEmail();
            String shop = user.getShopName();
            String type = user.getType();
            String orders = "0";
            String balance = String.format(Locale.ENGLISH, "%.2f $", user.getBalance());
            TextView usernameTV = (TextView) findViewById(R.id.account_username_USER);
            TextView emailTV = (TextView) findViewById(R.id.account_email_USER);
            TextView shopTV = (TextView) findViewById(R.id.account_shop_USER);
            TextView typeTV = (TextView) findViewById(R.id.account_type_USER);
            TextView ordersTV = (TextView) findViewById(R.id.account_orders_USER);
            TextView balanceTV = (TextView) findViewById(R.id.account_balance_USER);
            usernameTV.setText(getFormattedText("Username", username));
            emailTV.setText(getFormattedText("Email", email));
            shopTV.setText(getFormattedText("Shop", shop));
            typeTV.setText(getFormattedText("Type", type));
            ordersTV.setText(getFormattedText("Orders", orders));
            balanceTV.setText(balance);
            fab.hide();
        } else if (id == R.id.nav_orders) {
            accountInfo.setVisibility(TableLayout.INVISIBLE);
            mainGreeting.setVisibility(RelativeLayout.INVISIBLE);
            fab.show();

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AccountActivity.this, NewOrder.class);
                    intent.putExtra("accountType", user.getType());
                    startActivity(intent);
                    //timestamp = new Timestamp(System.currentTimeMillis());
                    //timestamp.getTime();
                    //Snackbar.make(view, String.valueOf(timestamp), Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
