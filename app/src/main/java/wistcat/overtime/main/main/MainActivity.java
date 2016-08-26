package wistcat.overtime.main.main;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import wistcat.overtime.App;
import wistcat.overtime.R;
import wistcat.overtime.base.AbsBaseActivity;

/**
 * 主界面
 *
 * @author wistcat
 */
public class MainActivity extends AbsBaseActivity {

    private static final String TAG_STATE = "state";
    private static final int QUIT_DURATION = 2000;
    private static final String[] mTitles = new String[]{"日常", "任务", "分类"};

    private DrawerLayout mDrawerLayout;
    private ViewPager mViewPager;
    private boolean isQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayShowTitleEnabled(false);
            ab.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            ab.setDisplayHomeAsUpEnabled(true);
        }

        // drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimary);

        // navigation
        NavigationView navigationView = (NavigationView) findViewById(R.id.drawer_nav);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        // TODO ...
                        default:
                            break;
                    }
                    //
                    item.setCheckable(true);
                    mDrawerLayout.closeDrawers();
                    return true;
                }
            });
        }

        // viewpager
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        if (savedInstanceState == null) {
            mViewPager.setCurrentItem(1);
        }

        // tabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

    }

    @Override
    public void onResume() {
        super.onResume();
        checkAndLock();
    }

    /* FIXME：本地锁的设计，不过还没想好 */
    private void checkAndLock() {
        // TODO
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    } else if (isQuit) {
                        onBackPressed();
                    } else {
                        // FIXME: 之后需要修改退出逻辑，将从侧拉菜单按钮退出
                        isQuit = true;
                        App.showToast("再按一次退出");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isQuit = false;
                            }
                        }, QUIT_DURATION);
                    }
                    return true;
                case KeyEvent.KEYCODE_MENU:
                    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                    } else {
                        mDrawerLayout.openDrawer(GravityCompat.START);
                    }
                    return true;
                default:
                    break;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(TAG_STATE, mViewPager.getCurrentItem());
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        mViewPager.setCurrentItem(inState.getInt(TAG_STATE));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        doRelease();
    }

    private void doRelease() {
        // TODO: ...
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment ret = null;
            switch (position) {
                case 0:
                    ret = new DisplayFragment();
                    break;
                case 1:
                    ret = new TasksFragment();
                    break;
                case 2:
                    ret = new PartsFragment();
                    break;
                default:
                    break;
            }
            return ret;
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }
}
