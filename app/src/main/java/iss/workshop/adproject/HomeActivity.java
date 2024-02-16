package iss.workshop.adproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContentInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import iss.workshop.adproject.Adapters.MyHomeAdapter;

public class HomeActivity extends AppCompatActivity {//viewPager也需要适配器，这里用的是homeAdapter
    ViewPager2 viewPager2;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    View headerView;
    TextView usernameText;
    ImageView imageView;
    ActivityResultLauncher<Intent> resultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        viewPager2 = findViewById(R.id.view_pager);
        navigationView = findViewById(R.id.nav_view);

        headerView = navigationView.getHeaderView(0);
        usernameText = headerView.findViewById(R.id.usernameText);
        imageView = headerView.findViewById(R.id.imageView);
        Glide.with(this)
                .load(pref.getString("headPicture",null))
                .circleCrop()
                .into(imageView);
        usernameText.setText("Hello, "+pref.getString("userName",null));
        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        initViewpager2(viewPager2);

        Intent intent = getIntent();
        if (intent.getBooleanExtra("SHOW_HISTORY_FRAGMENT",false)){
            viewPager2.setCurrentItem(2);
        }


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.profile){
                    Intent intent = new Intent(HomeActivity.this, UpdateProfile.class);//直接用this,NavigationView.OnNavigationItemSelectedListener 接口的实例，而不是 Activity 的实例。
                    resultLauncher.launch(intent);
                } else if (item.getItemId()==R.id.settings) {
                    //进入
                } else if (item.getItemId()==R.id.logout) {
                    new AlertDialog.Builder(HomeActivity.this)
                            .setTitle("Logout")
                            .setMessage("Are you sure you want to logout?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.clear();
                                    editor.commit();
                                    Intent intent = new Intent(HomeActivity.this, Login.class);
                                    startActivity(intent);
                                    // Optionally, if you want to remove the HomeActivity from the stack so the user can't go back to it
                                    finish();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }


                drawerLayout.closeDrawer(GravityCompat.START);//点击抽屉里面的item之后，自动关闭抽屉
                return true;
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int position = 0; // 默认位置
                if (item.getItemId() == R.id.homeImage) {
                    position = 0;
                } else if (item.getItemId() == R.id.historyImage) {
                    position = 2;
                } else if (item.getItemId() == R.id.searchImage) {
                    position = 1;
                } else if (item.getItemId() == R.id.uploadImage) {
                    position = 3;
                }
//                getSupportFragmentManager().beginTransaction().replace(R.id.view_pager, selectedFragment).commit();
//                return true;Viewpager应该通过适配器来管理fragment，切换删除添加，而不是通过FragmentManager
                viewPager2.setCurrentItem(position, true); // 切换到指定位置的 Fragment
                return true;
            }
        });

        registerForResult();
    }

    public void initViewpager2(ViewPager2 viewPager2){
        MyHomeAdapter myHomeAdapter = new MyHomeAdapter(getSupportFragmentManager(),getLifecycle());

        List<Fragment>fragments = new ArrayList<>();
        myHomeAdapter.setFragments(fragments);
        fragments.add(new BlogsViewingFragment());
        fragments.add(new BlankFragment2());
        fragments.add(new HistoryViewingFragment());
        fragments.add(new BlankFragment4());
        viewPager2.setAdapter(myHomeAdapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.setSelectedItemId(getBottomNavigationItemId(position));//通过这个方法来使得图片跟随页面一起变化
            }
        });//监听页面变化的消息，当页面变化时传递当前页面的位置，也就是当前页面对应的fragment在集合中的索引
    }


    private void registerForResult() {
        resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
                    result->{//检查结果码为 RESULT_OK
                        if (result.getResultCode()==AppCompatActivity.RESULT_OK){
                            Toast.makeText(this,"successfully update your profile",Toast.LENGTH_LONG).show();
                        }
                    }
        );
    }

    private int getBottomNavigationItemId(int position) {
        switch (position) {
            case 0:
                return R.id.homeImage;
            case 1:
                return R.id.searchImage;
            case 2:
                return R.id.historyImage;
            case 3:
                return R.id.uploadImage;
            default:
                return 0;
        }
    }
}