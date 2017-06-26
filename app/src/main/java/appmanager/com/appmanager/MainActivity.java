package appmanager.com.appmanager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import appmanager.com.appmanager.adapter.MyGridViewAdapter;
import appmanager.com.appmanager.adapter.MyViewPagerAdapter;
import appmanager.com.appmanager.bean.ApkBean;
import appmanager.com.appmanager.net.NetRequestLisener;
import appmanager.com.appmanager.net.NetRequestUtils;
import appmanager.com.appmanager.view.gridpasswordview.GridPasswordView;

public class MainActivity extends AppCompatActivity {

    /**
     * 普通应用
     */
    private List<ApkResponse> apkListResponse;
    private ViewGroup points;//小圆点指示器
    private ImageView[] ivPoints;//小圆点图片集合
    private ViewPager viewPager;
    private int totalPage;//总的页数
    private int mPageSize = 4;//每页显示的最大数量
    private List<ApkBean> listDatas;//总的数据源
    private List<View> viewPagerList;//GridView作为一个View对象添加到ViewPager集合中
    private int currentPage;//当前页

    /**
     * 系统应用
     */
    private ViewGroup sys_points;//小圆点指示器
    private ImageView[] sys_ivPoints;//小圆点图片集合
    private ViewPager sys_viewPager;
    private int sys_totalPage;//总的页数
    private int sys_mPageSize = 4;//每页显示的最大数量
    private List<ApkBean> sys_listDatas;//总的数据源
    private List<View> sys_viewPagerList;//GridView作为一个View对象添加到ViewPager集合中
    private int sys_currentPage;//当前页

    GridPasswordView pswView;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
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
        dialog.show();
        //初始化控件
        iniViews();

        NetRequestUtils.callMetroNetRequestPost(new NetRequestLisener() {
            @Override
            public void success(String result) {
                try {
                    apkListResponse = new ArrayList<ApkResponse>();
                    JSONArray jsonArray = new JSONArray(result);
                    for(int i = 0; i < jsonArray.length(); i++) {
                        ApkResponse response;
                        Gson gson = new Gson();
                        response = gson.fromJson(jsonArray.get(i).toString(), ApkResponse.class);
                        apkListResponse.add(response);
                    }
                    setDatas();
                    initPages();
                    System.out.println("size===" + apkListResponse.size());
                    System.out.println("vername====" + apkListResponse.get(0).getVerName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void error(String error) {

            }
        });

    }

    private void iniViews() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //初始化小圆点指示器
        points = (ViewGroup) findViewById(R.id.points);
        sys_viewPager = (ViewPager) findViewById(R.id.sys_viewPager);
        sys_points = (ViewGroup) findViewById(R.id.sys_points);
    }

    private void setDatas() {
        listDatas = new ArrayList<>();
        sys_listDatas = new ArrayList<>();
        for(int i=0; i<apkListResponse.size(); i++){
            listDatas.add(new ApkBean(apkListResponse.get(i).getTypeName(), apkListResponse.get(i).getIcon()));
            sys_listDatas.add(new ApkBean(apkListResponse.get(i).getTypeName(), apkListResponse.get(i).getIcon()));
        }
        listDatas.addAll(listDatas);
        //sys_listDatas.addAll(sys_listDatas);
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
        } else {
            for (int i = 0; i < sys_ivPoints.length; i++) {
                if (i == selectItems) {
                    sys_ivPoints[i].setBackgroundResource(R.drawable.page__selected_indicator);
                } else {
                    sys_ivPoints[i].setBackgroundResource(R.drawable.page__normal_indicator);
                }
            }
        }

    }

    public void initPages() {
        LayoutInflater inflater = LayoutInflater.from(this);
        //总的页数，取整（这里有三种类型：Math.ceil(3.5)=4:向上取整，只要有小数都+1  Math.floor(3.5)=3：向下取整  Math.round(3.5)=4:四舍五入）
        totalPage = (int) Math.ceil(listDatas.size() * 1.0 / mPageSize);
        sys_totalPage = (int)Math.ceil(sys_listDatas.size() * 1.0 / sys_mPageSize);
        viewPagerList = new ArrayList<>();
        sys_viewPagerList = new ArrayList<>();
        for(int i=0;i<totalPage;i++){
            //每个页面都是inflate出一个新实例
            GridView gridView = (GridView) inflater.inflate(R.layout.gridview_layout,viewPager,false);
            gridView.setAdapter(new MyGridViewAdapter(this,listDatas,i,mPageSize));
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
        for(int i=0; i < sys_totalPage; i++){
            //每个页面都是inflate出一个新实例
            GridView gridView = (GridView) inflater.inflate(R.layout.gridview_layout, sys_viewPager, false);
            gridView.setAdapter(new MyGridViewAdapter(this, sys_listDatas, i, sys_mPageSize));
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
            sys_viewPagerList.add(gridView);
        }
        //设置ViewPager适配器
        viewPager.setAdapter(new MyViewPagerAdapter(viewPagerList));
        sys_viewPager.setAdapter(new MyViewPagerAdapter(sys_viewPagerList));
        //小圆点指示器
        ivPoints = new ImageView[totalPage];
        sys_ivPoints = new ImageView[sys_totalPage];
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
        for(int i=0;i<sys_ivPoints.length;i++){
            ImageView imageView = new ImageView(this);
            //设置图片的宽高
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10,10));
            if(i == 0){
                imageView.setBackgroundResource(R.drawable.page__selected_indicator);
            }else{
                imageView.setBackgroundResource(R.drawable.page__normal_indicator);
            }
            sys_ivPoints[i] = imageView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 20;//设置点点点view的左边距
            layoutParams.rightMargin = 20;//设置点点点view的右边距
            sys_points.addView(imageView,layoutParams);
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
        sys_viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                setImageBackground(1, position);
                sys_currentPage = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
