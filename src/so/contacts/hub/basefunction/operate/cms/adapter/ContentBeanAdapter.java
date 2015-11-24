package so.contacts.hub.basefunction.operate.cms.adapter;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import so.contacts.hub.basefunction.imageloader.DataLoader;
import so.contacts.hub.basefunction.imageloader.image.ImageLoaderFactory;
import so.contacts.hub.basefunction.operate.cms.bean.ClickAction;
import so.contacts.hub.basefunction.operate.cms.bean.ContentBean;
import so.contacts.hub.basefunction.utils.Utils;
import so.contacts.hub.basefunction.widget.adapter.BaseListViewAdapter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.putao.live.R;

/**
 * 文件名称: ContentBeanAdapter.java <br>
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有 <br>
 * 创建人员: wcy <br>
 * 文件描述: 首页商品流适配器
 * 修改时间: 2015-9-12 下午2:45:02 <br>
 * 修改历史： 2015-9-12 1.00 初始版本
 */
public class ContentBeanAdapter extends BaseListViewAdapter
{
    private List<Object> mContentBeans;// 商品list

    private DataLoader mDataLoader;
    
    private Context mContext;
    
    public ContentBeanAdapter(Context context,List<Object> contentBeans) {
        mContentBeans = contentBeans;
        mDataLoader = new ImageLoaderFactory(context).getNormalLoaderWithNoCorner(false, R.drawable.putao_home_white);
        mContext = context;
    }

    @Override
    public DataLoader getmImageLoader()
    {
        return mDataLoader;
    }

    @Override
    public int getCount()
    {
        return Utils.isEmptyList(mContentBeans) ? 0 : mContentBeans.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mContentBeans.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        GoodsHolder holder = null;
        if (position >= getCount())
        {
            return null;
        }
        ContentBean bean = (ContentBean) mContentBeans.get(position);
        if (convertView == null)
        {
            convertView = View.inflate(mContext, R.layout.putao_chosen_goods_item_home, null);
        }
        else
        {
            holder = (GoodsHolder) convertView.getTag();
        }
        if (holder == null)
        {
            holder = new GoodsHolder();
            holder.goods_img = (ImageView) convertView.findViewById(R.id.goods_img);
            holder.goods_title = (TextView) convertView.findViewById(R.id.goods_title);
            holder.goods_desc = (TextView) convertView.findViewById(R.id.goods_desc);
            holder.goods_current_price = (TextView) convertView.findViewById(R.id.goods_current_price);
            holder.service_name = (TextView) convertView.findViewById(R.id.service_name);
            holder.top_divider = convertView.findViewById(R.id.top_divider);
            convertView.setTag(holder);
        }
        if (position == 0)
        {
            holder.top_divider.setVisibility(View.GONE);
        }
        else {
            holder.top_divider.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(bean.getImg_url()))
        {
            mDataLoader.loadData(bean.getImg_url(), holder.goods_img);
        }
        else
        {
            holder.goods_img.setImageBitmap(null);
        }
        /*if (!TextUtils.isEmpty(bean.getTitle_color()))
        {
            holder.goods_title.setTextColor(Color.parseColor(bean.getTitle_color()));
        }
        if (!TextUtils.isEmpty(bean.getDescription_color()))
        {
            holder.goods_desc.setTextColor(Color.parseColor(bean.getDescription_color()));
        }*/
        if (!TextUtils.isEmpty(bean.getName_color()))
        {
            holder.service_name.setTextColor(Color.parseColor(bean.getName_color()));
        }
        if (!TextUtils.isEmpty(bean.getPrice_color()))
        {
            holder.goods_current_price.setTextColor(Color.parseColor(bean.getPrice_color()));
        }
        if (!TextUtils.isEmpty(bean.getTitle()))
        {
            holder.goods_title.setText(bean.getTitle().trim());
        }
        if (!TextUtils.isEmpty(bean.getDescription()))
        {
            holder.goods_desc.setText(bean.getDescription());
        }
        if (!TextUtils.isEmpty(bean.getName()))
        {
            holder.service_name.setText(bean.getName());
            holder.service_name.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.service_name.setText(getCpName(bean));
            //holder.service_name.setVisibility(View.INVISIBLE);
        }
        double price = Double.parseDouble(bean.getPrice()+"");
        if (price != -1)
        {
            String desc = bean.getPrice_desc();
            int len = 0;
            if (!TextUtils.isEmpty(desc)) {
                len = desc.length();
            } else {
                desc = "";
            }
            String str = Utils.deleteZeroForPrice(price + "") + desc;
            SpannableString ss = new SpannableString(str);  
            ss.setSpan(new StyleSpan(Typeface.BOLD), 0, str.length() - len,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE); 
            ss.setSpan(new AbsoluteSizeSpan(Utils.sp2px(mContext, 16)), 0, str.length() - len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (!TextUtils.isEmpty(bean.getPrice_desc_color()) && len != 0)
            {
                ss.setSpan(new ForegroundColorSpan(Color.parseColor(bean.getPrice_desc_color())), str.length() - len, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            holder.goods_current_price.setText(ss);
        }
        else
        {
            holder.goods_current_price.setText("");
        }
        return convertView;
    }
    
    private String getCpName(ContentBean bean) {
        String cpName = null;
        if(!TextUtils.isEmpty(bean.getClick_action().getCp_info())){
            JSONObject jsonObject;
            try
            {
                jsonObject = new JSONObject(bean.getClick_action().getCp_info());
                cpName = jsonObject.optString("provider");
                if (!TextUtils.isEmpty(cpName))
                {
                    String[] s=cpName.split(" ");
                    if (s.length>=2)
                    {
                        cpName=s[1];
                    }
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(cpName))
        {
            cpName=mContext.getString(R.string.putao_cp_provider2,cpName);
        }else {
            cpName="";
        }
        return cpName;
    }
    
    public void onItemClick(int position) {
        if(position >= 0 && position < getCount() ){
            ContentBean bean = (ContentBean) mContentBeans.get(position);
            if (bean!=null)
            {
                // 统计不限制数量
//                MobclickAgentUtil.onEvent(mContext, UMengEventIds.CNT_HOME_PICK_GOODS_,
//                        mContext.getString(R.string.putao_analytics_home_pick_pos, (position + 1)));
//                 MobclickAgentUtil.onEvent(mContext, UMengEventIds.CNT_HOME_PICK_GOODS,bean.getTitle());
//                
//                MobclickAgentUtil.onEventPutao(mContext,  UMengEventIds.CNT_GOODS_CONT_CLICK_+bean.getOpen_cp_id()+"_"+bean.getOpen_service_id()+"_"+bean.getOpen_goods_id());
//                ClickAction clickAction= bean.getClick_action();
//                if (clickAction!=null&&clickAction.getClick_type()==ClickAction.CLICK_TYPE_H5)
//                {
//                    clickAction.setCp_info(null);
//                    clickAction.setShow_title(mContext.getString(R.string.putao_open_goodsdtl_title));
//                } 
//                ServiceClickAgentUtil.doClickAction(mContext, bean.getClick_action(), 0,true);
            }
            
        }
    }
    
    public void clearCache() {
        if (mDataLoader != null) {
            mDataLoader.clearCache();
            mDataLoader = null;
        }
    }

    public static class GoodsHolder
    {
        public TextView service_name;

        public ImageView goods_img;

        public TextView goods_title;

        public TextView goods_desc;

        public TextView goods_current_price;
        
        public View top_divider;
        

        //public TextView goods_prime_price;

        //public TextView goods_sold;

        //public TextView goods_distance;

    }
}
