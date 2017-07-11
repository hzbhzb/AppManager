package appmanager.com.appmanager;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import appmanager.com.appmanager.adapter.LocalGridViewAdapter;
import appmanager.com.appmanager.adapter.MyGridViewAdapter;
import appmanager.com.appmanager.adapter.MyViewPagerAdapter;
import appmanager.com.appmanager.bean.ApkBean;
import appmanager.com.appmanager.bean.LocalAppInfo;

public class AppManagerActivity extends AppCompatActivity {

    /**
     * 第三方应用
     */
    private List<ApkResponse> apkListResponse;
    private ViewGroup points;//小圆点指示器
    private ImageView[] ivPoints;//小圆点图片集合
    private ViewPager viewPager;
    private int totalPage;//总的页数
    private int mPageSize = 8;//每页显示的最大数量
    private List<LocalAppInfo> listDatas;//总的数据源
    private List<View> viewPagerList;//GridView作为一个View对象添加到ViewPager集合中
    private int currentPage;//当前页

    private static final int GET_ALL_APP_FINISH = 1;

    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case GET_ALL_APP_FINISH :

                    initPages();

                    break;

                default :
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_manager);
        initViews();
    }

    private void initViews() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //初始化小圆点指示器
        points = (ViewGroup) findViewById(R.id.points);
        new Thread() {
            @Override
            public void run() {
                super.run();
                getAllApps();
                handler.sendEmptyMessage(GET_ALL_APP_FINISH);
            }
        }.start();
    }

    public void getAllApps(){

        PackageManager packageManager = getPackageManager();
        listDatas = new ArrayList<LocalAppInfo>();
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setAction(Intent.ACTION_MAIN);

        PackageManager manager = getPackageManager();
        List<ResolveInfo> resolveInfoList = manager.queryIntentActivities(intent, 0);
        LocalAppInfo myAppInfo;

        for(ResolveInfo info:resolveInfoList){
            myAppInfo = new LocalAppInfo();
            //拿到包名
            String packageName = info.resolvePackageName;
            //拿到应用程序的信息
            //ApplicationInfo appInfo = info.;
            //拿到应用程序的图标
            Drawable icon = info.loadIcon(manager);
            //拿到应用程序的大小
            //long codesize = packageStats.codeSize;
            //Log.i("info", "-->"+codesize);
            //拿到应用程序的程序名
            String appName = info.loadLabel(packageManager).toString();

            myAppInfo.setPackageName(packageName);
            myAppInfo.setAppName(appName);
            myAppInfo.setIcon(icon);
            myAppInfo.setIconResId(info.getIconResource());
            myAppInfo.setSystemApp(true);
            if (!MyApplication.apkPkgNames.contains(myAppInfo.getPackageName())) {
                listDatas.add(myAppInfo);
            }


        }
        for (ApkResponse apkResponse : MyApplication.apkResponseList) {

        }
    }

    /**
     * 改变点点点的切换效果
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

        for(int i=0;i<totalPage;i++){
            //每个页面都是inflate出一个新实例
            GridView gridView = (GridView) inflater.inflate(R.layout.gridview_layout,viewPager,false);
            gridView.setAdapter(new LocalGridViewAdapter(this,listDatas,i,mPageSize));
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

        for(int i=0;i<ivPoints.length;i++){
            ImageView imageView = new ImageView(this);
            //设置图片的宽高
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10,10));
            if(i == 0){
                imageView.setBackgroundResource(R.drawable.page__selected_indicator);
            }else{
                imageView.setBackgroundResource(R.drawable.page__normal_indicator);
            }
            ivPoints[i] = imageView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 20;//设置点点点view的左边距
            layoutParams.rightMargin = 20;//设置点点点view的右边距
            points.addView(imageView,layoutParams);
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
}
