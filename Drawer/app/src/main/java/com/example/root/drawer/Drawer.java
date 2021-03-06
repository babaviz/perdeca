package com.example.root.drawer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private static ViewPager mViewPager;
    public static ArrayList<String> Options=new ArrayList<>();
    public static ArrayList<String> factors=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        dw= getResources().getDrawable(R.drawable.input);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        final FloatingActionButton comp = (FloatingActionButton) findViewById(R.id.compare);
        comp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mViewPager.getCurrentItem()==2){
                    if(Options.size() < Integer.parseInt(opc.getText().toString().trim()) ||
                            factors.size()<Integer.parseInt(opf.getText().toString().trim()))
                    {
                        //create warning
                        new AlertDialog.Builder(mViewPager.getContext()).setTitle("Missing Info").setMessage("Kindly provide all data specified to continue").setNeutralButton("Close", null).show();
                    }else {
                        addweitage();
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    }
                }
                else if(mViewPager.getCurrentItem()==3) {
                    if(!validate_weight())
                    {
                        //create warning
                       // new AlertDialog.Builder(mViewPager.getContext()).setTitle("Incorect data").setMessage("Check ").setNeutralButton("Close", null).show();
                    }else {
                        //Provide information and time hestimate
                        // Use the Builder class for convenient dialog construction
                        AlertDialog.Builder builder = new AlertDialog.Builder(mViewPager.getContext());
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);

                            }
                        })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //finish();
                                    }
                                });
                        // Create the AlertDialog object and return it
                        int sum=countquizs();
                        int millis=sum*5*1000;//estimation time
                        long hours = TimeUnit.MILLISECONDS.toHours(millis);
                        millis -= TimeUnit.HOURS.toMillis(hours);
                        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
                        millis -= TimeUnit.MINUTES.toMillis(minutes);
                        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

                        String time=hours+"hrs "+minutes+"Min "+seconds+"Sec";
                        builder.setMessage("Question: " + sum + "\nTime estimation(5 sec each): " + time + "\nReady to start survey?\n");
                        builder.setTitle("Survey");
                        builder.show();
                    }
                }else {
                    mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                }


            }
        });

        final FloatingActionButton back = (FloatingActionButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
            }
        });
        back.hide();

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position > 0) {
                    back.show();
                } else {
                    back.hide();
                }
                if (position == mViewPager.getAdapter().getCount() - 1) {
                    comp.hide();
                } else {
                    comp.show();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //disable swipping
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }

        });


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    public int countquizs(){
        int counter=0;
        for(int i=1;i<=factors.size();i++){
            for(int p=1;p<Options.size();p++){
                for(int k=p+1;k<=Options.size();k++){
                    counter++;
                }
            }
        }
       return counter;
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

    //Alert




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_new) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_Draft) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        Toast.makeText(getApplicationContext(),"This is for testing",Toast.LENGTH_LONG).show();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public static void next(){
        mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
    }

    public static boolean exist(ArrayList elements,String element){

        for (Object temp:elements) {
            if(temp.toString().equalsIgnoreCase(element))
                return true;
        }
        return false;
    }

    static EditText opc;
    static EditText opf;
    static GridLayout grid;
    static Drawable dw;
    static ArrayList<EditText> weitages=new ArrayList<>();
    static ArrayList<Integer> weight_numbers=new ArrayList<>();

    public static void addweitage(){
        int counter=0;
        for (String temp:factors) {
            TextView tx=new TextView(mViewPager.getContext());
            tx.setText(temp);
            tx.setTextSize(24);
            tx.setPadding(10, 10, 10, 10);
            grid.addView(tx);

            final EditText edit=new EditText(mViewPager.getContext());
            edit.setPadding(20, 20, 20, 20);
            edit.setText((++counter) + "");
            edit.setBackgroundDrawable(dw);
            edit.setMinWidth(150);
            edit.setInputType(InputType.TYPE_CLASS_NUMBER);
            grid.addView(edit);
            weitages.add(edit);
            }
        }

    static boolean validate_weight(){
        for (EditText edit:weitages) {
            String strEnteredVal = edit.getText().toString();

            if (!strEnteredVal.equals("")) {
                int num = Integer.parseInt(strEnteredVal);
                if (num > 1 && num <= factors.size()) {
                    if (!exist(weight_numbers, num + "")) {
                        edit.setText("" + num);
                        weight_numbers.add(num);
                    } else {
                       // edit.setText("0");
                        Toast.makeText(mViewPager.getContext(),"Weightage can not be shared",Toast.LENGTH_LONG).show();
                        weight_numbers.clear();
                        return false;
                    }
                } else {
                    //edit.setText("0");
                    Toast.makeText(mViewPager.getContext(),"Weightage can only lay from 1 to number of factors",Toast.LENGTH_LONG).show();
                    weight_numbers.clear();
                    return false;
                }
            }else
                edit.setText("0");

        }
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 8 total pages.
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            int pos=getArguments().getInt(ARG_SECTION_NUMBER);
            if(pos==1){
                View rootView = inflater.inflate(R.layout.input, container, false);
                opc=(EditText)rootView.findViewById(R.id.options);
                opf=(EditText)rootView.findViewById(R.id.factors);
                return rootView;
            }
            else if(pos==2){
                final View rootView = inflater.inflate(R.layout.options, container, false);
                final TextView info=(TextView)rootView.findViewById(R.id.counter);
                //add option

                final Button addop=(Button)rootView.findViewById(R.id.optionadd);
                        addop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EditText option=(EditText)rootView.findViewById(R.id.option_name);
                                if(!option.getText().toString().trim().isEmpty()){
                                    TextView mTextView =new TextView(rootView.getContext());//(TextView) rootView.findViewById(R.id.temp_op); //Creating new TextView
                                    mTextView.setText(" "+(Options.size()+1)+". "+option.getText() + "");    //Setting attributes
                                    mTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.items));
                                    mTextView.setTextColor(Color.BLACK);
                                    mTextView.setTextSize(20);
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                                    mTextView.setLayoutParams(params);
                                    LinearLayout CD = (LinearLayout)rootView.findViewById(R.id.options_Layout);

                                    int opnumber=Integer.parseInt(opc.getText().toString().trim());
                                    if(!exist(Options,option.getText().toString())) {
                                        if(Options.size()< opnumber) {
                                            Options.add(option.getText().toString());
                                            CD.addView(mTextView);
                                        }//Adding it to the container
                                    }
                                    else{
                                        Toast.makeText(rootView.getContext(),"Already Exists",Toast.LENGTH_SHORT).show();
                                    }

                                    info.setText(Options.size()+" added, "+(opnumber-Options.size())+" Remaining");
                                    if(Options.size()==opnumber){
                                        info.setTextColor(Color.RED);
                                    }else {
                                        info.setTextColor(Color.parseColor("#000000"));
                                    }
                                    option.setText("");
                                }else {
                                    Toast.makeText(rootView.getContext(),"Option name missing",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                return rootView;
            }
            else if(pos==3){
                final View rootView = inflater.inflate(R.layout.factors, container, false);
                final Button addfact=(Button)rootView.findViewById(R.id.add_factor);
                final TextView info=(TextView)rootView.findViewById(R.id.factor_info);
                addfact.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText fact=(EditText)rootView.findViewById(R.id.factor_name);
                        if(!fact.getText().toString().trim().isEmpty()){
                            TextView mTextView =new TextView(rootView.getContext());//(TextView) rootView.findViewById(R.id.temp_op); //Creating new TextView
                            mTextView.setText(" "+(factors.size()+1)+". "+fact.getText() + "");    //Setting attributes
                            mTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.items));
                            mTextView.setTextColor(Color.BLACK);
                            mTextView.setTextSize(20);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                            mTextView.setLayoutParams(params);
                            LinearLayout CD = (LinearLayout)rootView.findViewById(R.id.factors_layout);

                            int opnumber=Integer.parseInt(opf.getText().toString().trim());
                            if(!exist(factors,fact.getText().toString())) {
                                if(factors.size()< opnumber) {
                                    factors.add(fact.getText().toString());
                                    CD.addView(mTextView);
                                }//Adding it to the container
                            }
                            else{
                                Toast.makeText(rootView.getContext(),"Already Exists",Toast.LENGTH_SHORT).show();
                            }

                            info.setText(factors.size()+" added, "+(opnumber-factors.size())+" Remaining");
                            if(factors.size()==opnumber){
                                info.setTextColor(Color.RED);
                            }else {
                                info.setTextColor(Color.parseColor("#000000"));
                            }
                            fact.setText("");
                        }else {
                            Toast.makeText(rootView.getContext(),"Option name missing",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                return rootView;
            }
            else if(pos==4){
                View rootView = inflater.inflate(R.layout.weightage, container, false);
                grid=(GridLayout)rootView.findViewById(R.id.weightage_layout);
                addweitage();
                return rootView;
            }

            else if(pos==5){
                View rootView = inflater.inflate(R.layout.page1, container, false);

                Button yes=(Button)rootView.findViewById(R.id.yes);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        next();
                    }
                });

                Button no=(Button)rootView.findViewById(R.id.no);
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                    }
                });

                return rootView;
            }
            else if(pos==6){
                View rootView = inflater.inflate(R.layout.page2, container, false);

                Button yes1=(Button)rootView.findViewById(R.id.yes1);
                yes1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
                    }
                });

                Button no1=(Button)rootView.findViewById(R.id.no1);
                no1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
                    }
                });

                return rootView;
            }
            else if(pos==7){
                View rootView = inflater.inflate(R.layout.page3, container, false);

                Button yes2=(Button)rootView.findViewById(R.id.yes2);
                yes2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
                    }
                });

                Button no2=(Button)rootView.findViewById(R.id.no3);
                no2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
                    }
                });
                return rootView;
            }
            else if(pos==8){
                View rootView = inflater.inflate(R.layout.weightage, container, false);
                return rootView;
            }
            else {
                View rootView = inflater.inflate(R.layout.survey, container, false);
                return rootView;
            }/*
            else if (pos == 2){
                View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

                return rootView;
            }else{
                View rootView = inflater.inflate(R.layout.comparison, container, false);

                return rootView;
            }
            }*/
        }
    }
}
