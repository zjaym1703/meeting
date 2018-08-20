package com.example.user.meet;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class GroupActivity extends AppCompatActivity {

    private ActionBar toolbar;
    static String group_n,user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        Intent it=getIntent();
        group_n=it.getStringExtra("그룹이름");//타이틀을 프래그먼트로 이동시켜야됨
        user_id=it.getStringExtra("아이디");
        toolbar=getSupportActionBar();//이건 왜 추가하냐 시발

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle(group_n);//그룹이름추가하면되겠당.

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home://홈버튼
                    //바로 처음으로 설정해놓는거
                    fragment=new TuteeFragment();
                    //bundleString(fragment);
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_dashboard:
                    fragment=new BoardFragment();
                    //bundleString(fragment);
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_notifications:
                    fragment=new TuteeFragment();
                    //bundleString(fragment);
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };

    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
       // transaction.addToBackStack(null);
        transaction.commit();
    }

    /*private void bundleString(Fragment f){//그룹이름 넘기려고 만든 변수
        Bundle bundle=new Bundle();
        bundle.putString("group",group_n);
        f.setArguments(bundle);
    }*/

}

