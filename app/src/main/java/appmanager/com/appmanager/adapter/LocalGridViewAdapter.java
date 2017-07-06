package appmanager.com.appmanager.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
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

import appmanager.com.appmanager.MyApplication;
import appmanager.com.appmanager.R;
import appmanager.com.appmanager.bean.ApkBean;
import appmanager.com.appmanager.bean.LocalAppInfo;
import appmanager.com.appmanager.bean.LruBitmapCache;

/**
 * Created by huangzhebin on 2017/7/5.
 */

public class LocalGridViewAdapter extends BaseAdapter {

    private List<LocalAppInfo> listData;
    private LayoutInflater inflater;
    private Context context;
    private int mIndex;//页数下标，表示第几页，从0开始
    private int mPagerSize;//每页显示的最大数量
    private static ImageLoader mImageLoader;

    public LocalGridViewAdapter(Context context,List<LocalAppInfo> listData,int mIndex,int mPagerSize) {
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
        LocalGridViewAdapter.ViewHolder holder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.gridview_item,parent,false);
            holder = new LocalGridViewAdapter.ViewHolder();
            holder.proName = (TextView) convertView.findViewById(R.id.proName);
            holder.imgUrl = (NetworkImageView) convertView.findViewById(R.id.imgUrl);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imgUrl1);
            // 设置默认的图片
            //holder.imgUrl.setDefaultImageResId(R.mipmap.ic_launcher);
            // 设置图片加载失败后显示的图片
            //holder.imgUrl.setErrorImageResId(R.drawable.error_photo);
            convertView.setTag(holder);
            //重新确定position（因为拿到的是总的数据源，数据源是分页加载到每页的GridView上的，为了确保能正确的点对不同页上的item）
            final int pos = position + mIndex*mPagerSize;//假设mPagerSize=8，假如点击的是第二页（即mIndex=1）上的第二个位置item(position=1),那么这个item的实际位置就是pos=9
            LocalAppInfo localAppInfo = listData.get(pos);
            holder.proName.setText(localAppInfo.getAppName());
            if (localAppInfo.isSystemApp()) {
                //holder.imgUrl.setDefaultImageResId(localAppInfo.getIconResId());
                holder.imageView.setImageDrawable(localAppInfo.getIcon());
                holder.imgUrl.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);
            } else {
                // 设置默认的图片
                holder.imgUrl.setDefaultImageResId(R.mipmap.ic_launcher);
                // 设置图片加载失败后显示的图片
                holder.imgUrl.setErrorImageResId(R.drawable.error_photo);
                holder.imgUrl.setImageUrl(localAppInfo.getIconUrl(), mImageLoader);
                holder.imageView.setVisibility(View.GONE);
                holder.imgUrl.setVisibility(View.VISIBLE);
            }

            //
            //添加item监听
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"你点击了 "+listData.get(pos).getAppName(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            holder = (LocalGridViewAdapter.ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder{
        private TextView proName;
        private NetworkImageView imgUrl;
        private ImageView imageView;
    }
}
