package appmanager.com.appmanager;

import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.multithreaddownload.DownloadInfo;
import com.aspsine.multithreaddownload.DownloadManager;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import appmanager.com.appmanager.adapter.MyGridViewAdapter;
import appmanager.com.appmanager.adapter.MyViewPagerAdapter;
import appmanager.com.appmanager.bean.AdminPwdResponse;
import appmanager.com.appmanager.bean.ApkBean;
import appmanager.com.appmanager.bean.GetApksResult;
import appmanager.com.appmanager.multithreaddownload.demo.DataSource;
import appmanager.com.appmanager.multithreaddownload.demo.entity.AppInfo;
import appmanager.com.appmanager.multithreaddownload.demo.listener.OnItemClickListener;
import appmanager.com.appmanager.multithreaddownload.demo.service.DownloadService;

import appmanager.com.appmanager.multithreaddownload.demo.ui.adapter.ListViewAdapter;
import appmanager.com.appmanager.multithreaddownload.demo.util.Utils;
import appmanager.com.appmanager.net.NetRequestLisener;
import appmanager.com.appmanager.net.NetRequestUtils;
import appmanager.com.appmanager.utils.DateUtil;
import appmanager.com.appmanager.view.gridpasswordview.DragFloatActionButton;
import appmanager.com.appmanager.view.gridpasswordview.GridPasswordView;
import appmanager.com.appmanager.view.gridpasswordview.Util;
import appmanager.com.appmanager.view.gridpasswordview.floatview.FloatWindowService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainActivity extends AppCompatActivity implements OnItemClickListener<AppInfo> {

    /**
     * 普通应用
     */
    private List<ApkResponse> apkListResponse;
    private ViewGroup points;//小圆点指示器
    private ImageView[] ivPoints;//小圆点图片集合
    private ViewPager viewPager;
    private int totalPage;//总的页数
    private int mPageSize = 12;//每页显示的最大数量
    private List<ApkResponse> listDatas;//总的数据源
    private List<View> viewPagerList;//GridView作为一个View对象添加到ViewPager集合中
    private int currentPage;//当前页

    GridPasswordView pswView;
    Dialog dialog, listDialog;

    private List<AppInfo> mAppInfos;
    private ListViewAdapter mAdapter;
    ListView listView;
    private File mDownloadDir;
    private String adminPwd;
    private MainActivity.DownloadReceiver mReceiver;
    private WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    private static WindowManager windowManager;
    private static ImageView imageView;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Toolbar toolbar;

    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1101;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS) {
            if (!hasPermission()) {
                //若用户未开启权限，则引导用户开启“Apps with usage access”权限
                startActivityForResult(
                        new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                        MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
            }
        }
    }

    //检测用户是否对本app开启了“Apps with usage access”权限
    private boolean hasPermission() {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), getPackageName());
        }
        return mode == AppOpsManager.MODE_ALLOWED;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!hasPermission()) {
                startActivityForResult(
                        new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                        MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
            }
        }
        if (sp == null) {
            sp = getSharedPreferences("app_info", Context.MODE_PRIVATE);
        }
        editor = sp.edit();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextClock textClock = (TextClock)findViewById(R.id.textClock);
        textClock.setFormat12Hour("yyyy年MM月dd日   EEEE   HH:mm:ss");
        TextView ly_version = (TextView)findViewById(R.id.ly_version);
        ly_version.setText(String.format("版本号%s", getSoftVersion(this)));


        dialog = new Dialog(this,
                android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pay_dialog);
        dialog.setCancelable(false);
        Button btn_close = (Button) dialog.findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        pswView = (GridPasswordView) dialog.findViewById(R.id.pswView);
        pswView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {

            }

            @Override
            public void onInputFinish(String psw) {

                if (TextUtils.equals(psw, adminPwd)) {
                    Intent intent = new Intent(MainActivity.this, AppManagerActivity.class);
                    startActivity(intent);
                    dialog.cancel();
                } else {
                    Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                }
                pswView.clearPassword();

            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                //dialogView.scrollTo(0, 30);
                pswView.forceInputViewGetFocus();
                //dialogView.scrollTo(0, 30);
            }
        });
        //dialog.show();

        //初始化控件
        initViews();
        if (!TextUtils.isEmpty(getAppInfo()))
            dealAppInfo(getAppInfo());
        if (!TextUtils.isEmpty(getPwds()))
            dealPwds(getPwds());
        getAdminPwd();
        Intent intent = new Intent(MainActivity.this, FloatWindowService.class);
        startService(intent);
    }
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
    private void saveAppInfo(String  appInfo) {
        editor.putString("appInfo",  appInfo);
        editor.commit();
    }
    private String getAppInfo() {
        return sp.getString("appInfo", "");
    }
    private void savePwds(String  pwds) {
        editor.putString("pwds",  pwds);
        editor.commit();
    }
    private String getPwds() {
        return sp.getString("pwds", "");
    }
    /**
     * 获取版本信息
     */
    public static String getSoftVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo localPackageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return localPackageInfo.versionName;
        } catch (Exception e) {

            return "";
        }

    }
    private void getAppInfos() {
        if (isNetworkAvailable(this)) {
            NetRequestUtils.callMetroNetRequestPost("apps", new NetRequestLisener() {
                @Override
                public void success(String result) {
                    saveAppInfo(result);
                    dealAppInfo(result);
                }

                @Override
                public void error(String error) {

                }
            });
        }
    }
    private void dealAppInfo(String appInfo) {
        Gson gson = new Gson();
        GetApksResult getApksResult = gson.fromJson(appInfo, GetApksResult.class);
        apkListResponse = getApksResult.getData();
        MyApplication.apkResponseList = apkListResponse;
        setDatas();
        initPages();
    }
    private void dealPwds(String pwds) {
        Gson gson = new Gson();
        AdminPwdResponse adminPwdResponse = gson.fromJson(pwds, AdminPwdResponse.class);
        String curDate = DateUtil.dateFormat(new Date(), "yyyyMMdd");
        System.out.println("date==" + curDate);
        List<AdminPwdResponse.DataBean> datas = adminPwdResponse.getData();
        for(AdminPwdResponse.DataBean data : datas) {
            if (TextUtils.equals(curDate, String.valueOf(data.getId()))) {
                adminPwd = data.getPwd();
                ((TextView)findViewById(R.id.tv_pwd_v)).setText(String.format("P%s", curDate));

                System.out.println("adminPwd == " + adminPwd);
                return;
            }
        }
        adminPwd = datas.get(datas.size() - 1).getPwd();
        ((TextView)findViewById(R.id.tv_pwd_v)).setText(String.format("P%d", datas.get(datas.size()- 1).getId()));
        System.out.println("admin pwds length == " + adminPwd);
    }

    private void getAdminPwd() {
        NetRequestUtils.callMetroNetRequestPost("pwds", new NetRequestLisener() {
            @Override
            public void success(String result) {
                savePwds(result);
                dealPwds(result);
            }

            @Override
            public void error(String error) {

            }
        });
    }

    public void goAppList(View v) {
        showListDialog();
    }

    public void goAllApp(View v) {
        dialog.show();
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //初始化小圆点指示器
        points = (ViewGroup) findViewById(R.id.points);
    }

    private void setDatas() {
        if (listDatas == null)
           listDatas = new ArrayList<>();
        listDatas.clear();
        MyApplication.allApkPkgNames.clear();
        MyApplication.apkPkgNames.clear();
        MyApplication.apkPkgNames.add(getPackageName());
        for (int i = 0; i < apkListResponse.size(); i++) {

            if (apkListResponse.get(i).getType().equals("DLJ")) {
                String pkgName = apkListResponse.get(i).getPkg();
                MyApplication.apkPkgNames.add(pkgName);
                if (Utils.isAppInstalled(this, pkgName))
                    listDatas.add(apkListResponse.get(i));
            }
            MyApplication.allApkPkgNames.add(apkListResponse.get(i).getPkg());
        }
    }


    /**
     * 改变点点点的切换效果
     *
     * @param selectItems
     */
    private void setImageBackground(int type, int selectItems) {
        if (type == 0) {
            for (int i = 0; i < ivPoints.length; i++) {
                if (i == selectItems) {
                    ivPoints[i].setBackgroundResource(R.drawable.page__selected_indicator);
                } else {
                    ivPoints[i].setBackgroundResource(R.drawable.page__normal_indicator);
                }
            }
        }

    }

    public void initPages() {
        LayoutInflater inflater = LayoutInflater.from(this);
        //总的页数，取整（这里有三种类型：Math.ceil(3.5)=4:向上取整，只要有小数都+1  Math.floor(3.5)=3：向下取整  Math.round(3.5)=4:四舍五入）
        totalPage = (int) Math.ceil(listDatas.size() * 1.0 / mPageSize);

        viewPagerList = new ArrayList<>();

        for (int i = 0; i < totalPage; i++) {
            //每个页面都是inflate出一个新实例
            GridView gridView = (GridView) inflater.inflate(R.layout.gridview_layout, viewPager, false);
            gridView.setAdapter(new MyGridViewAdapter(this, listDatas, i, mPageSize));

            viewPagerList.add(gridView);
        }

        //设置ViewPager适配器
        viewPager.setAdapter(new MyViewPagerAdapter(viewPagerList));

        //小圆点指示器
        ivPoints = new ImageView[totalPage];
        points.removeAllViews();
        for (int i = 0; i < ivPoints.length; i++) {
            ImageView imageView = new ImageView(this);
            //设置图片的宽高
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
            if (i == 0) {
                imageView.setBackgroundResource(R.drawable.page__selected_indicator);
            } else {
                imageView.setBackgroundResource(R.drawable.page__normal_indicator);
            }
            ivPoints[i] = imageView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 20;//设置点点点view的左边距
            layoutParams.rightMargin = 20;//设置点点点view的右边距
            points.addView(imageView, layoutParams);
        }

        //设置ViewPager滑动监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                setImageBackground(0, position);
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void showListDialog() {

        //if (listDialog == null) {

            listDialog = new AlertDialog.Builder(this).create();
            listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            listDialog.show();

            View view = LayoutInflater.from(this).inflate(R.layout.fragment_list_view, null);
            TextView tv_close = (TextView) view.findViewById(R.id.tv_close);
            tv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listDialog.dismiss();
                }
            });
            listView = (ListView) view.findViewById(R.id.listView);
            mDownloadDir = new File(Environment.getExternalStorageDirectory(), "Download");
            mAdapter = new ListViewAdapter();
            mAdapter.setOnItemClickListener(this);
            DataSource.getInstance().setData(apkListResponse);
            mAppInfos = DataSource.getInstance().getData();

            for (AppInfo info : mAppInfos) {
                DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(info.getUrl());
                if (downloadInfo != null) {
                    info.setProgress(downloadInfo.getProgress());
                    info.setDownloadPerSize(Utils.getDownloadPerSize(downloadInfo.getFinished(), downloadInfo.getLength()));
                    info.setStatus(AppInfo.STATUS_PAUSED);

                    if (Utils.isAppInstalled(this, info.getPackageName())) {
                        info.setStatus(AppInfo.STATUS_INSTALLED);
                    } else {
                        info.setStatus(AppInfo.STATUS_PAUSED);
                    }
                } else {
                    File apk = new File(mDownloadDir, info.getName() + ".apk");

                    if (Utils.isAppInstalled(this, info.getPackageName())) {
                        if (Utils.isNeedUpdate(this, info.getPackageName(), info.getVerCode())) {
                            info.setStatus(AppInfo.STATUS_RENEWABLE);
                            apk.delete();
                        } else {
                            info.setStatus(AppInfo.STATUS_INSTALLED);
                        }

                    } else if (apk.isFile() && apk.exists()) {
                        info.setStatus(AppInfo.STATUS_COMPLETE);
                    } else {
                        info.setStatus(AppInfo.STATUS_NOT_DOWNLOAD);
                    }
                }
            }
            mAdapter.setData(mAppInfos);
            listView.setAdapter(mAdapter);
            //dialogWindow.setContentView(view);
            listDialog.setContentView(view);
        //}
        listDialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        hideBottomUIMenu();
        getAppInfos();
        register();
    }

    @Override
    public void onPause() {
        super.onPause();
        unRegister();
    }

    @Override
    public void onItemClick(View v, final int position, final AppInfo appInfo) {
        System.out.println("appInfo status===" + appInfo.getStatus());


        if (appInfo.getStatus() == AppInfo.STATUS_DOWNLOADING || appInfo.getStatus() == AppInfo.STATUS_CONNECTING) {
            pause(appInfo.getUrl());
        } else if (appInfo.getStatus() == AppInfo.STATUS_COMPLETE ) {
            install(appInfo);
            mAppInfos.get(position).setStatus(AppInfo.STATUS_INSTALLED);
            if (isCurrentListViewItemVisible(position)) {
                ListViewAdapter.ViewHolder holder = getViewHolder(position);
                //holder.tvStatus.setText(appInfo.getStatusText());
                holder.btnDownload.setText(appInfo.getButtonText());
            }
            listDialog.dismiss();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while(true) {
                        if (Utils.isAppInstalled(MainActivity.this, appInfo.getPackageName()))
                        {
                            System.out.println("已安装==" + appInfo.getPackageName());
                            break;
                        }
                    }
                }
            }).start();
        } else if (appInfo.getStatus() == AppInfo.STATUS_INSTALLED) {
            unInstall(appInfo);
            mAppInfos.get(position).setStatus(AppInfo.STATUS_COMPLETE);
            if (isCurrentListViewItemVisible(position)) {
                ListViewAdapter.ViewHolder holder = getViewHolder(position);
                //holder.tvStatus.setText(appInfo.getStatusText());
                holder.btnDownload.setText(appInfo.getButtonText());
            }
            listDialog.dismiss();
        } else {
            if (isCurrentListViewItemVisible(position)) {
                ListViewAdapter.ViewHolder holder = getViewHolder(position);
                //holder.tvStatus.setText(appInfo.getStatusText());
                holder.btnDownload.setText(appInfo.getButtonText());
                //holder.btnDownload.setVisibility(View.GONE);
            }
            download(position, appInfo.getUrl(), appInfo);
        }
    }

    private void register() {
        mReceiver = new MainActivity.DownloadReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.ACTION_DOWNLOAD_BROAD_CAST);
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
    }

    private void unRegister() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        }
    }

    private void download(int position, String tag, AppInfo info) {
        DownloadService.intentDownload(this, position, tag, info);
    }

    private void pause(String tag) {
        DownloadService.intentPause(this, tag);
    }

    private void pauseAll() {
        DownloadService.intentPauseAll(this);
    }

    private void install(AppInfo appInfo) {
        Utils.installApp(this, new File(mDownloadDir, appInfo.getName() + ".apk"));
    }

    private void unInstall(AppInfo appInfo) {
        Utils.unInstallApp(this, appInfo.getPackageName());
    }

    class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            String SYSTEM_REASON = "reason";
            String SYSTEM_HOME_KEY = "homekey";
            String SYSTEM_HOME_KEY_LONG = "recentapps";
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (TextUtils.equals(reason, SYSTEM_HOME_KEY)) {
                    //表示按了home键,程序到了后台
                    Toast.makeText(getApplicationContext(), "home", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.equals(reason, SYSTEM_HOME_KEY_LONG)){
                    //表示长按home键,显示最近使用的程序列表
                }
            }
            if (action == null || !action.equals(DownloadService.ACTION_DOWNLOAD_BROAD_CAST)) {
                return;
            }
            final int position = intent.getIntExtra(DownloadService.EXTRA_POSITION, -1);
            final AppInfo tmpInfo = (AppInfo) intent.getSerializableExtra(DownloadService.EXTRA_APP_INFO);
            if (tmpInfo == null || position == -1) {
                return;
            }
            final AppInfo appInfo = mAppInfos.get(position);
            final int status = tmpInfo.getStatus();
            switch (status) {
                case AppInfo.STATUS_CONNECTING:
                    appInfo.setStatus(AppInfo.STATUS_CONNECTING);
                    if (isCurrentListViewItemVisible(position)) {
                        ListViewAdapter.ViewHolder holder = getViewHolder(position);
                        //holder.tvStatus.setText(appInfo.getStatusText());
                        holder.btnDownload.setText(appInfo.getButtonText());
                    }
                    break;

                case AppInfo.STATUS_DOWNLOADING:
                    appInfo.setStatus(AppInfo.STATUS_DOWNLOADING);
                    appInfo.setProgress(tmpInfo.getProgress());
                    appInfo.setDownloadPerSize(tmpInfo.getDownloadPerSize());
                    if (isCurrentListViewItemVisible(position)) {
                        ListViewAdapter.ViewHolder holder = getViewHolder(position);
                        //holder.tvDownloadPerSize.setText(appInfo.getDownloadPerSize());
                        holder.btnDownload.setProgress(appInfo.getProgress());
                        //holder.tvStatus.setText(appInfo.getStatusText());

                    }
                    break;
                case AppInfo.STATUS_COMPLETE:
                    appInfo.setStatus(AppInfo.STATUS_COMPLETE);
                    appInfo.setProgress(tmpInfo.getProgress());
                    appInfo.setDownloadPerSize(tmpInfo.getDownloadPerSize());
                    File apk = new File(mDownloadDir, appInfo.getName() + ".apk");
                    if (apk.isFile() && apk.exists()) {
                        String packageName = Utils.getApkFilePackage(MainActivity.this, apk);
                        appInfo.setPackageName(packageName);
//                        if (Utils.isAppInstalled(MainActivity.this, packageName)) {
//                            appInfo.setStatus(AppInfo.STATUS_INSTALLED);
//                        }
                    }

                    if (isCurrentListViewItemVisible(position)) {
                        ListViewAdapter.ViewHolder holder = getViewHolder(position);
                        //holder.tvStatus.setText(appInfo.getStatusText());
                        holder.btnDownload.setProgress(appInfo.getProgress());
                        holder.btnDownload.setText(appInfo.getButtonText());
                        //holder.tvDownloadPerSize.setText(appInfo.getDownloadPerSize());

                    }
                    install(appInfo);
                    listDialog.dismiss();
                    break;

                case AppInfo.STATUS_PAUSED:
                    appInfo.setStatus(AppInfo.STATUS_PAUSED);
                    if (isCurrentListViewItemVisible(position)) {
                        ListViewAdapter.ViewHolder holder = getViewHolder(position);
                        //holder.tvStatus.setText(appInfo.getStatusText());
                        holder.btnDownload.setText(appInfo.getButtonText());
                    }
                    break;
                case AppInfo.STATUS_NOT_DOWNLOAD:
                    appInfo.setStatus(AppInfo.STATUS_NOT_DOWNLOAD);
                    appInfo.setProgress(tmpInfo.getProgress());
                    appInfo.setDownloadPerSize(tmpInfo.getDownloadPerSize());
                    if (isCurrentListViewItemVisible(position)) {
                        ListViewAdapter.ViewHolder holder = getViewHolder(position);
                        //holder.tvStatus.setText(appInfo.getStatusText());
                        holder.btnDownload.setText(appInfo.getButtonText());
                        holder.btnDownload.setProgress(appInfo.getProgress());
                        //holder.tvDownloadPerSize.setText(appInfo.getDownloadPerSize());
                    }
                    break;
                case AppInfo.STATUS_DOWNLOAD_ERROR:
                    appInfo.setStatus(AppInfo.STATUS_DOWNLOAD_ERROR);
                    appInfo.setDownloadPerSize("");
                    if (isCurrentListViewItemVisible(position)) {
                        ListViewAdapter.ViewHolder holder = getViewHolder(position);
                        //holder.tvStatus.setText(appInfo.getStatusText());
                        //holder.tvDownloadPerSize.setText("");
                        holder.btnDownload.setText(appInfo.getButtonText());
                    }
                    break;
            }
        }
    }

    private boolean isCurrentListViewItemVisible(int position) {
        int first = listView.getFirstVisiblePosition();
        int last = listView.getLastVisiblePosition();
        return first <= position && position <= last;
    }

    private ListViewAdapter.ViewHolder getViewHolder(int position) {
        int childPosition = position - listView.getFirstVisiblePosition();
        View view = listView.getChildAt(childPosition);
        return (ListViewAdapter.ViewHolder) view.getTag();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
}
