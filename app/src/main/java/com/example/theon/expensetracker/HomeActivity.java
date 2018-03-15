package com.example.theon.expensetracker;
import com.example.theon.expensetracker.Database.DBHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import com.example.theon.expensetracker.Database.DBHelper;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ImageButton smsTrigger,addTrigger;
    private static final int REQUEST_READ_SMS = 2;
    BarChart barChart;
    ArrayList<String> dates;
    Random random;
    ArrayList<BarEntry> barEntries;
    DBHelper dbHelper;
    ListView smsListView;
    ArrayAdapter arrayAdapter;
    ArrayList<String> smsMessagesList;

    Integer[] imgid={
            R.drawable.ic_shopping_cart_black_84dp,
            R.drawable.ic_local_dining_black_84dp,
            R.drawable.ic_movie_filter_black_84dp,
            R.drawable.ic_local_gas_station_black_84dp
    };
    private static String TAG = "HomeActivity.class";
    private static final int NUM_PAGES = 2;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        smsListView = (ListView) findViewById(R.id.expenseslist);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        addTrigger = findViewById(R.id.add);
        barChart = (BarChart) findViewById(R.id.bargraph);

        createRandomBarGraph("2016/05/05", "2016/06/01");

        populateexpenses();

        addTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ManualAdd.class);
                startActivity(intent);
            }
        });


        smsTrigger = findViewById(R.id.parsesms);
        smsTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,parseSMS.class);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            setResult(RESULT_OK, null);
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.account) {
            // Handle the camera action
        } else if (id == R.id.balance) {

        } else if (id == R.id.settings) {
            Intent intent = new Intent(getApplicationContext(), PieActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_share) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        populateexpenses();
        Toast.makeText(this, "Refreshed", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getStringExtra("methodName").equals("myMethod")){
            Toast.makeText(this, "Refreshed", Toast.LENGTH_LONG).show();
            populateexpenses();

        }
    }

    public void populateexpenses(){
        dbHelper = new DBHelper(this);

        smsMessagesList = new ArrayList<String>();
        smsMessagesList.clear();

        Cursor res = dbHelper.getAllData();
        if(res.getCount()==0){
            Log.d("DATABASE:","EMPTY!!!!!!!!!!!!!!!!!");
            return;
        }
        while (res.moveToNext()) {
            StringBuffer entry = new StringBuffer();
            entry.append("Bank : " + res.getString(1) + "\n");
            entry.append("Location : " + res.getString(2) + "\n");
            entry.append("Date : " + res.getString(3) + "\n");
            entry.append("Cost : " + res.getString(4) + "\n");
            entry.append("Category : " + res.getString(5) + "\n");
            entry.append("Type : " + res.getString(6));

            smsMessagesList.add(entry.toString());
        }
        CustomListAdapter adapter=new CustomListAdapter(this, smsMessagesList, imgid);
        //smsListView=(ListView)findViewById(R.id.list);
        smsListView.setAdapter(adapter);
        /*arrayAdapter = new ArrayAdapter<String>(
                this, R.layout.listlayputexpenses,
                R.id.Itemname,smsMessagesList);
        smsListView.setAdapter(arrayAdapter);*/
    }

    public void createRandomBarGraph(String Date1, String Date2){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

        try {
            Date date1 = simpleDateFormat.parse(Date1);
            Date date2 = simpleDateFormat.parse(Date2);

            Calendar mDate1 = Calendar.getInstance();
            Calendar mDate2 = Calendar.getInstance();
            mDate1.clear();
            mDate2.clear();

            mDate1.setTime(date1);
            mDate2.setTime(date2);

            dates = new ArrayList<>();
            dates = getList(mDate1,mDate2);

            barEntries = new ArrayList<>();
            float max = 0f;
            float value = 0f;
            random = new Random();
            for(int j = 0; j< dates.size();j++){
                max = 100f;
                value = random.nextFloat();
                Log.d(TAG, Float.toString(value));
                value = value * max;
                Log.d(TAG, "After " + Float.toString(value));
                barEntries.add(new BarEntry(value,j));
            }

        }catch(ParseException e){
            e.printStackTrace();
        }
//        DBHelper dbHelper = new DBHelper(this);
//
//        Cursor res = dbHelper.getAllData();
//        if(res.getCount()==0){
//            Log.d("DATABASE:","EMPTY!!!!!!!!!!!!!!!!!");
//            return;
//        }
//        int i = 0;
//        barEntries = new ArrayList<>();
//        dates = new ArrayList<>();
//        while (res.moveToNext()) {
//            StringBuffer entry = new StringBuffer();
//            entry.append("Id : " + res.getString(0) + "\n");
//            entry.append("Bank : " + res.getString(1) + "\n");
//            entry.append("Location : " + res.getString(2) + "\n");
//            entry.append("Date : " + res.getString(3) + "\n");
//            entry.append("Cost : " + res.getString(4) + "\n");
//            entry.append("Category : " + res.getString(5) + "\n");
//            entry.append("Type : " + res.getString(6));
//            Log.d("Debugging", res.getString(3) + " chec " + res.getString(4));
//            if(res.getString(4).length() != 0 && res.getString(3).length() != 0 ) {
//                barEntries.add(new BarEntry(Float.parseFloat(res.getString(4).replace("$","").replace(",","")), i));
//                dates.add(res.getString(3));
//            }
//            i++;
//
//
//        }
//
//
//        Collections.reverse(dates);
//        Collections.reverse(barEntries);
//        for(int a = 0; a < dates.size();a++) {
//            Log.d("reverse" , dates.get(a));
//        }

        BarDataSet barDataSet = new BarDataSet(barEntries,"Dates");
        BarData barData = new BarData(dates, barDataSet);
        barChart.setData(barData);

    }

    public ArrayList<String> getList(Calendar startDate, Calendar endDate){
        ArrayList<String> list = new ArrayList<String>();
        while(startDate.compareTo(endDate)<=0){
            list.add(getDate(startDate));
            startDate.add(Calendar.DAY_OF_MONTH,1);
        }
        return list;
    }

    public String getDate(Calendar cld){
        String curDate = cld.get(Calendar.YEAR) + "/" + (cld.get(Calendar.MONTH) + 1) + "/"
                +cld.get(Calendar.DAY_OF_MONTH);
        try{
            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(curDate);
            curDate =  new SimpleDateFormat("yyy/MM/dd").format(date);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return curDate;
    }
}


