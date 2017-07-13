package appmanager.com.appmanager;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextClock;

import com.aspsine.multithreaddownload.DownloadInfo;
import com.aspsine.multithreaddownload.DownloadManager;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
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
import appmanager.com.appmanager.view.gridpasswordview.GridPasswordView;

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
    private int mPageSize = 8;//每页显示的最大数量
    private List<ApkBean> listDatas;//总的数据源
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        TextClock textClock = (TextClock)findViewById(R.id.textClock);
        textClock.setFormat12Hour("yyyy-MM-dd  hh:mm:ss ");
        Logger log = LoggerFactory.getLogger(MainActivity.class);
        log.info("hello world");
        getAdminPwd();
        dialog = new Dialog(this,
                android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pay_dialog);
        pswView = (GridPasswordView) dialog.findViewById(R.id.pswView);
        pswView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {

            }

            @Override
            public void onInputFinish(String psw) {
                if (!TextUtils.equals(psw, adminPwd)) {
                    Intent intent = new Intent(MainActivity.this, AppManagerActivity.class);

                    //intent.putExtra("EXTRA_TYPE", AppListActivity.TYPE.TYPE_LISTVIEW);
                    startActivity(intent);
                }
                dialog.cancel();
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

        NetRequestUtils.callMetroNetRequestPost("apps", new NetRequestLisener() {
            @Override
            public void success(String result) {
                Gson gson = new Gson();
                GetApksResult getApksResult = gson.fromJson(result, GetApksResult.class);
                apkListResponse = getApksResult.getData();
                MyApplication.apkResponseList = apkListResponse;
                setDatas();
                initPages();
                System.out.println("size===" + apkListResponse.size());
                System.out.println("vername====" + apkListResponse.get(0).getVerName());

            }

            @Override
            public void error(String error) {

            }
        });

    }

    private void getAdminPwd() {
        NetRequestUtils.callMetroNetRequestPost("pwds", new NetRequestLisener() {
            @Override
            public void success(String result) {
                Gson gson = new Gson();
                AdminPwdResponse adminPwdResponse = gson.fromJson(result, AdminPwdResponse.class);
                adminPwd = adminPwdResponse.getData().get(0).getPwd();
                System.out.println("admin pwds length == " + adminPwdResponse.getData().size());
            }

            @Override
            public void error(String error) {

            }
        });
    }

    public void goAppList(View v) {
        showListDialog();
//        Intent intent = new Intent(MainActivity.this, AppListActivity.class);
//        intent.putExtra("EXTRA_TYPE", AppListActivity.TYPE.TYPE_LISTVIEW);
//        startActivity(intent);
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
        listDatas = new ArrayList<>();
        for (int i = 0; i < apkListResponse.size(); i++) {
            ApkBean apkBean = new ApkBean();
            apkBean.setDownUrl(apkListResponse.get(i).getPath());
            apkBean.setImgUrl(apkListResponse.get(i).getLogo());
            apkBean.setProName(apkListResponse.get(i).getName());
            if (apkListResponse.get(i).getType().equals("DLJ")) {
                MyApplication.apkPkgNames.add(apkListResponse.get(i).getPkg());
                listDatas.add(apkBean);
            }


        }
        listDatas.addAll(listDatas);
        //sys_listDatas.addAll(sys_listDatas);
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
            //添加item点击监听
            /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int pos = position + currentPage*mPageSize;
                    Log.i("TAG","position的值为："+position + "-->pos的值为："+pos);
                    Toast.makeText(MainActivity.this,"你点击了 "+listDatas.get(pos).getProName(),Toast.LENGTH_SHORT).show();
                }
            });*/
            //每一个GridView作为一个View对象添加到ViewPager集合中
            viewPagerList.add(gridView);
        }

        //设置ViewPager适配器
        viewPager.setAdapter(new MyViewPagerAdapter(viewPagerList));

        //小圆点指示器
        ivPoints = new ImageView[totalPage];

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

        if (listDialog == null) {
//            listDialog = new Dialog(this,
//                    android.R.style.Theme_Translucent_NoTitleBar);
            listDialog = new AlertDialog.Builder(this).create();
            listDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            listDialog.show();

            Window dialogWindow = listDialog.getWindow();

            dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
            dialogWindow.setGravity(Gravity.CENTER);
            View view = LayoutInflater.from(this).inflate(R.layout.fragment_list_view, null);

            listView = (ListView) view.findViewById(R.id.listView);
            mDownloadDir = new File(Environment.getExternalStorageDirectory(), "Download");
            mAdapter = new ListViewAdapter();
            mAdapter.setOnItemClickListener(this);
            DataSource.getInstance().setData(apkListResponse);
            mAppInfos = DataSource.getInstance().getData();
            System.out.println("mAppInfos size==" + mAppInfos.size());
            for (AppInfo info : mAppInfos) {
                DownloadInfo downloadInfo = DownloadManager.getInstance().getDownloadInfo(info.getUrl());
                if (downloadInfo != null) {
                    info.setProgress(downloadInfo.getProgress());
                    info.setDownloadPerSize(Utils.getDownloadPerSize(downloadInfo.getFinished(), downloadInfo.getLength()));
                    info.setStatus(AppInfo.STATUS_PAUSED);
                    System.out.println("app size==" + info.getDownloadPerSize());
                    if (Utils.isAppInstalled(this, info.getPackageName())) {
                        info.setStatus(AppInfo.STATUS_INSTALLED);
                    } else {
                        info.setStatus(AppInfo.STATUS_PAUSED);
                    }
                } else {
                    File apk = new File(mDownloadDir, info.getName() + ".apk");

                    if (Utils.isAppInstalled(this, info.getPackageName())) {
                        info.setStatus(AppInfo.STATUS_INSTALLED);
                    } else if (apk.isFile() && apk.exists()) {
                        info.setStatus(AppInfo.STATUS_COMPLETE);
                    } else {
                        info.setStatus(AppInfo.STATUS_NOT_DOWNLOAD);
                    }
                }
            }
            mAdapter.setData(mAppInfos);
            listView.setAdapter(mAdapter);
            dialogWindow.setContentView(view);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//            lp.x = (int) (Utils.getScreenWidth(this) * 0.15); // 新位置X坐标
//            lp.y = (int) (Utils.getScreenHeight(this) * 0.1); // 新位置Y坐标
            lp.width = (int) (Utils.getScreenWidth(this) * 0.7); // 宽度
            lp.height = (int) (Utils.getScreenHeight(this) * 0.8); // 高度

            //lp.alpha = 0.7f; // 透明度
            dialogWindow.setAttributes(lp);
        }
        listDialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
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
        } else if (appInfo.getStatus() == AppInfo.STATUS_COMPLETE) {
            install(appInfo);
        } else if (appInfo.getStatus() == AppInfo.STATUS_INSTALLED) {
            unInstall(appInfo);
        } else {
            download(position, appInfo.getUrl(), appInfo);
        }
    }

    private void register() {
        mReceiver = new MainActivity.DownloadReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadService.ACTION_DOWNLOAD_BROAD_CAST);
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
                        holder.tvStatus.setText(appInfo.getStatusText());
                        holder.btnDownload.setText(appInfo.getButtonText());
                    }
                    break;

                case AppInfo.STATUS_DOWNLOADING:
                    appInfo.setStatus(AppInfo.STATUS_DOWNLOADING);
                    appInfo.setProgress(tmpInfo.getProgress());
                    appInfo.setDownloadPerSize(tmpInfo.getDownloadPerSize());
                    if (isCurrentListViewItemVisible(position)) {
                        ListViewAdapter.ViewHolder holder = getViewHolder(position);
                        holder.tvDownloadPerSize.setText(appInfo.getDownloadPerSize());
                        holder.progressBar.setProgress(appInfo.getProgress());
                        holder.tvStatus.setText(appInfo.getStatusText());
                        holder.btnDownload.setText(appInfo.getButtonText());
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
                        if (Utils.isAppInstalled(MainActivity.this, packageName)) {
                            appInfo.setStatus(AppInfo.STATUS_INSTALLED);
                        }
                    }

                    if (isCurrentListViewItemVisible(position)) {
                        ListViewAdapter.ViewHolder holder = getViewHolder(position);
                        holder.tvStatus.setText(appInfo.getStatusText());
                        holder.btnDownload.setText(appInfo.getButtonText());
                        holder.tvDownloadPerSize.setText(appInfo.getDownloadPerSize());
                        holder.progressBar.setProgress(appInfo.getProgress());
                    }
                    break;

                case AppInfo.STATUS_PAUSED:
                    appInfo.setStatus(AppInfo.STATUS_PAUSED);
                    if (isCurrentListViewItemVisible(position)) {
                        ListViewAdapter.ViewHolder holder = getViewHolder(position);
                        holder.tvStatus.setText(appInfo.getStatusText());
                        holder.btnDownload.setText(appInfo.getButtonText());
                    }
                    break;
                case AppInfo.STATUS_NOT_DOWNLOAD:
                    appInfo.setStatus(AppInfo.STATUS_NOT_DOWNLOAD);
                    appInfo.setProgress(tmpInfo.getProgress());
                    appInfo.setDownloadPerSize(tmpInfo.getDownloadPerSize());
                    if (isCurrentListViewItemVisible(position)) {
                        ListViewAdapter.ViewHolder holder = getViewHolder(position);
                        holder.tvStatus.setText(appInfo.getStatusText());
                        holder.btnDownload.setText(appInfo.getButtonText());
                        holder.progressBar.setProgress(appInfo.getProgress());
                        holder.tvDownloadPerSize.setText(appInfo.getDownloadPerSize());
                    }
                    break;
                case AppInfo.STATUS_DOWNLOAD_ERROR:
                    appInfo.setStatus(AppInfo.STATUS_DOWNLOAD_ERROR);
                    appInfo.setDownloadPerSize("");
                    if (isCurrentListViewItemVisible(position)) {
                        ListViewAdapter.ViewHolder holder = getViewHolder(position);
                        holder.tvStatus.setText(appInfo.getStatusText());
                        holder.tvDownloadPerSize.setText("");
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
}
