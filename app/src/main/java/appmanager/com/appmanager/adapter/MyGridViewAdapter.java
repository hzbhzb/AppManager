package appmanager.com.appmanager.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import appmanager.com.appmanager.ApkResponse;
import appmanager.com.appmanager.MyApplication;
import appmanager.com.appmanager.R;
import appmanager.com.appmanager.bean.ApkBean;
import appmanager.com.appmanager.bean.LruBitmapCache;
import appmanager.com.appmanager.multithreaddownload.demo.util.Utils;

/**
 * Created by huangzhebin on 2017/6/17.
 */

public class MyGridViewAdapter extends BaseAdapter {

    private List<ApkResponse> listData;
    private LayoutInflater inflater;
    private Context context;
    private int mIndex;//页数下标，表示第几页，从0开始
    private int mPagerSize;//每页显示的最大数量
    private static ImageLoader mImageLoader;

    public MyGridViewAdapter(Context context,List<ApkResponse> listData,int mIndex,int mPagerSize) {
        this.context = context;
        this.listData = listData;
        this.mIndex = mIndex;
        this.mPagerSize = mPagerSize;
        inflater = LayoutInflater.from(context);
        mImageLoader = new ImageLoader(MyApplication.getRequestQueue(), new LruBitmapCache());
    }

    /**
     * 先判断数据集的大小是否足够显示满本页？listData.size() > (mIndex + 1)*mPagerSize
     * 如果满足，则此页就显示最大数量mPagerSize的个数
     * 如果不够显示每页的最大数量，那么剩下几个就显示几个 (listData.size() - mIndex*mPagerSize)
     */
    @Override
    public int getCount() {
        return listData.size() > (mIndex + 1)*mPagerSize ? mPagerSize : (listData.size() - mIndex*mPagerSize);
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position + mIndex * mPagerSize);
    }

    @Override
    public long getItemId(int position) {
        return position + mIndex * mPagerSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.gridview_item,parent,false);
            holder = new ViewHolder();
            holder.proName = (TextView) convertView.findViewById(R.id.proName);
            holder.imgUrl = (NetworkImageView) convertView.findViewById(R.id.imgUrl);
            // 设置默认的图片
            holder.imgUrl.setDefaultImageResId(R.mipmap.ic_launcher);
            // 设置图片加载失败后显示的图片
            holder.imgUrl.setErrorImageResId(R.drawable.error_photo);
            holder.imgUrl.setVisibility(View.VISIBLE);
            ((ImageView)convertView.findViewById(R.id.imgUrl1)).setVisibility(View.GONE);
            convertView.setTag(holder);
            //重新确定position（因为拿到的是总的数据源，数据源是分页加载到每页的GridView上的，为了确保能正确的点对不同页上的item）
            final int pos = position + mIndex*mPagerSize;//假设mPagerSize=8，假如点击的是第二页（即mIndex=1）上的第二个位置item(position=1),那么这个item的实际位置就是pos=9
            ApkResponse bean = listData.get(pos);
            holder.proName.setText(bean.getName());

            // 开始加载网络图片
            holder.imgUrl.setImageUrl(bean.getLogo(), mImageLoader);
            //添加item监听
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = Utils.isexit(context, listData.get(pos).getPkg());
                    context.startActivity(intent);

                    Toast.makeText(context,"你点击了 "+listData.get(pos).getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder{
        private TextView proName;
        private NetworkImageView imgUrl;
    }
}
