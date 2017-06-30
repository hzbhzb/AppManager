package appmanager.com.appmanager.multithreaddownload.demo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import appmanager.com.appmanager.R;
import appmanager.com.appmanager.multithreaddownload.demo.entity.AppInfo;
import appmanager.com.appmanager.multithreaddownload.demo.ui.fragment.AppDetailFragment;


public class AppDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_detail);
        Intent intent = getIntent();
        AppInfo appInfo = (AppInfo) intent.getSerializableExtra("EXTRA_APPINFO");
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AppDetailFragment.newInstance(appInfo))
                    .commit();
        }
        getSupportActionBar().setTitle(appInfo.getName());
    }


}
