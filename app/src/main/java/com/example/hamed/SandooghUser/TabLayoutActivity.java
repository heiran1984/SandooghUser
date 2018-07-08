package com.example.hamed.SandooghUser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabLayoutActivity extends MyActivity {


    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_layout);
        //toolbar=(Toolbar)findViewById(R.id.toolBar);
       // setSupportActionBar(toolbar);

        tabLayout=(TabLayout) findViewById(R.id.tabLayout);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new OzvFragment(),"اعضاء");
        viewPagerAdapter.addFragments(new VamFragment(),"وام ها");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        changeTabsFont();
        //ActionBar actionBar=getSupportActionBar();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbar_text);
        getSupportActionBar().setElevation(0);
        /*
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setElevation(0);
       // actionBar.setTitle("صندوق قرض الحسنه");

        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.actionbar_text, null);

        actionBar.setCustomView(v);
        */


    }


    private void changeTabsFont() {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/IRANSansMobile.ttf"));
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()){

            case R.id.LogOut:
                SharedPrefManager.getInstance(this).logout();
                startActivity(new Intent(this,LoginActivity.class));
                finish();
                break;
        }
        return true;

    }
}
