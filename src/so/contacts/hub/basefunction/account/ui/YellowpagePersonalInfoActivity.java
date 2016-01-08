package so.contacts.hub.basefunction.account.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.lives.depend.theme.dialog.CommonDialog;
import com.lives.depend.theme.dialog.CommonDialogFactory;
import com.lives.depend.theme.dialog.progress.AbstractProgressDialog;
import com.lives.depend.utils.LogUtil;
import com.putao.live.R;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import android.R.integer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.text.style.DynamicDrawableSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import so.contacts.hub.BaseActivity;
import so.contacts.hub.basefunction.account.adapter.WheelNumericAdapter;
import so.contacts.hub.basefunction.account.adapter.WheelTextAdapter;
import so.contacts.hub.basefunction.account.bean.BasicUserInfoBean;
import so.contacts.hub.basefunction.account.manager.AccountManager;
import so.contacts.hub.basefunction.config.Config;
import so.contacts.hub.basefunction.imageloader.DataLoader;
import so.contacts.hub.basefunction.imageloader.image.ImageLoaderFactory;
import so.contacts.hub.basefunction.net.bean.RelateUser;
import so.contacts.hub.basefunction.net.bean.UpLoadUserBasicInfoRequestData;
import so.contacts.hub.basefunction.net.manager.IResponse;
import so.contacts.hub.basefunction.net.manager.PTHTTPManager;
import so.contacts.hub.basefunction.storage.db.CityListDB;
import so.contacts.hub.basefunction.storage.db.PersonInfoDB;
import so.contacts.hub.basefunction.utils.QiNiuCloudManager;
import so.contacts.hub.basefunction.utils.SystemUtil;
import so.contacts.hub.basefunction.widget.wheel.OnWheelChangedListener;
import so.contacts.hub.basefunction.widget.wheel.WheelView;

public class YellowpagePersonalInfoActivity extends BaseActivity implements OnClickListener, OnWheelChangedListener
{
    private static final String TAG = "YellowpagePersonalInfoActivity";

    private static final String IMAGE_FILE_NAME = "head_image.jpg";

    private static final int CODE_GALLERY_REQUEST = 0xa0;

    private static final int CODE_CAMERA_REQUEST = 0xa1;

    private static final int CODE_RESULT_REQUEST = 0xa2;

    private static final int CODE_UPLOAD_SUCCESS = 0xa3;

    private static final int CODE_UPLOAD_FIAL = 0xa4;

    private static final int CODE_SHOW_WHEEL_CITY_DIALOG = 0xa5;

    private static final int CODE_INIT_BASIC_USER_DATA = 0xa6;

    // ===============================view start========================
    // 设置密码提示
    private LinearLayout mDataHintLayout;

    // 去除提示
    private ImageView mHintDisappearImageView;

    private RelativeLayout mHeadDataLayout;

    // 头像
    private ImageView mHeadImageView;

    private RelativeLayout mCityDataLayout;

    private CommonDialog mHeadImageDialog;

    // 地区
    private TextView mCityTextView;

    private RelativeLayout mGenderLayout;

    private WheelView mProvinceWheelView;

    private WheelView mCityWheelView;

    private CommonDialog mCityDialog;

    // 性别
    private TextView mGenderTextView;

    private RelativeLayout mBirthdayLayout;

    private CommonDialog mGenderDialog;

    // 生日
    private TextView mBirthdayTextView;

    private RelativeLayout mCommInfoLayout;

    private CommonDialog mBirthdayDialog;
    
    private WheelView mYearWheelView;
    
    private int mCurrentYear;
    
    private WheelNumericAdapter mYearAdapter;
    
    private WheelView mMonthWheelView;
    
    private WheelNumericAdapter mMonthAdapter;
    
    private WheelView mDayWheelView;
    
    private int mMaxDay;
    
    private WheelNumericAdapter mDayAdapter;
    
    // 常用信息
    private TextView mCommInfoTextView;

    private RelativeLayout mHomeAddressLayout;

    // 常用地址
    private TextView mHomeAddressTextView;

    private RelativeLayout mCarInfoLayout;

    // 车辆信息
    private TextView mCarInfoTextView;

    // 已绑定手机号
    private TextView mBindPhoneTextView;

    // 设置密码
    private RelativeLayout mSetPassWordLayout;

    // 安全性低
    private TextView mNotSafeTextView;

    // 退出登录
    private Button mLogOutButton;

    private CommonDialog mLogOutDialog;

    // ===============================view end========================
    // 个人信息是否有修改
    private boolean mChangeFlag = false;

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 480;

    private static int output_Y = 480;

    // 保存头像的uri
    private Uri mHeadIconUri = null;

    // 头像加载器
    private DataLoader mImageLoader;

    // 是否已经设置过密码
    private int mHasSetPassword;

    // 头像上传到七牛服务器后返回的图片地址
    private String mHeadPicSTR;

    // 选中的地区
    private String mCitySTR;

    // 选择的性别
    private String mGenderSTR;

    // 选择的生日
    private String mBirthdaySTR;

    // 个人信息数据库
    private PersonInfoDB mPersonInfoDB;

    // 省份列表
    private LinkedList<String> mProvincesList = new LinkedList<String>();

    // 省份适配器
    private WheelTextAdapter mProvinceAdapter;

    // 城市列表
    private LinkedList<String> mCitiesList = new LinkedList<String>();

    // 城市适配器
    private WheelTextAdapter mCityAdapter;

    // 城市列表数据库
    private CityListDB mCityListDB;

    // 省份城市集合
    private LinkedList<ProvinceItem> mProvinceItems = new LinkedList<ProvinceItem>();

    // 用户基础数据
    private BasicUserInfoBean mBasicUserInfoBean;

    // 男
    private String male;

    // 女
    private String female;

    // 主线程handler
    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case CODE_UPLOAD_SUCCESS:
                    dismissLoadingDialog();
                    if (mImageLoader != null)
                    {
                        mImageLoader.loadData(mHeadPicSTR, mHeadImageView);
                    }
                    break;
                case CODE_UPLOAD_FIAL:
                    dismissLoadingDialog();
                    Toast.makeText(YellowpagePersonalInfoActivity.this,
                            getString(R.string.putao_personal_data_upload_icon_fail), Toast.LENGTH_SHORT).show();
                    break;
                case CODE_SHOW_WHEEL_CITY_DIALOG:
                    showWheelCityDialog();
                case CODE_INIT_BASIC_USER_DATA:
                    dismissLoadingDialog();
                    initBasicInfoData();
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.putao_personal_data_layout);
        mHeadIconUri = Uri.fromFile(new File(getExternalCacheDir(), IMAGE_FILE_NAME));
        mImageLoader = new ImageLoaderFactory(this).getStatusAvatarLoader();
        mPersonInfoDB = Config.getDatabaseHelper().getPersonInfoDB();
        mCityListDB = Config.getDatabaseHelper().getCityListDB();
        initView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        updateBasicInfoData();
        initCommonInfoData();
    }

    /**
     * 从数据库获取基本数据，并显示 void
     */
    private void updateBasicInfoData()
    {
        if(mBasicUserInfoBean == null)
        {
            showLoadingDialog();
        }
        // 1.从数据库获取用户基本数据 2.handler发送message到界面显示
        Config.execute(new Runnable()
        {
            @Override
            public void run()
            {
                mBasicUserInfoBean = mPersonInfoDB.queryData();
                mHandler.sendEmptyMessage(CODE_INIT_BASIC_USER_DATA);
            }
        });
    }

    /**
     * 界面显示用户基本数据 void
     */
    private void initBasicInfoData()
    {
        if (mBasicUserInfoBean == null)
        {
            return;
        }
        else
        {
            // 是否已经设置过密码
            mHasSetPassword = mBasicUserInfoBean.getHas_set_password();
            if (mHasSetPassword != 1)
            {
                findViewById(R.id.putao_not_safty).setVisibility(View.VISIBLE);
            }
        }
        // 设置头像
        String imageUrl = mBasicUserInfoBean.getHead_pic();
        if (!TextUtils.isEmpty(imageUrl) && mImageLoader != null)
        {
            mHeadPicSTR = imageUrl;
            mImageLoader.loadData(imageUrl, mHeadImageView);
        }
        else
        {
            mHeadImageView.setImageResource(R.drawable.putao_menu_acc_headimg_logined);
        }
        // 设置地区
        String city = mBasicUserInfoBean.getCity();
        if (!TextUtils.isEmpty(city))
        {
            mCitySTR = city;
        }
        else
        {
            // 根据定位的城市，得到省份，拼接出mCitySTR
        }
        mCityTextView.setText(mCitySTR);
        // 设置性别
        int gender = mBasicUserInfoBean.getGender();
        if (gender == 0)
        {
            mGenderSTR = male;
        }
        else if (gender == 1)
        {
            mGenderSTR = female;
        }
        mGenderTextView.setText(mGenderSTR);
        // 设置生日
        mBirthdaySTR = mBasicUserInfoBean.getBirthday();
        if (!TextUtils.isEmpty(mBirthdaySTR))
        {
            mBirthdayTextView.setText(mBirthdaySTR);
        }
        else
        {
            mBirthdayTextView.setText("1989-10-15");
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        // 在onpause里面做数据保存操作，只要个人中心界面不可见，就进行数据保存。保存主要分为两个方面：1.保存到数据库
        // 2.上传到服务器。当然了每次有信息修改的时候才会做保存，所以需要设置开关
        if (mChangeFlag)
        {
            saveUserBasicInfo();
        }
    }

    /**
     * 用户信息有变化的时候，保存用户信息 void
     */
    private void saveUserBasicInfo()
    {
        // 将更新后的信息备份到本地数据库
        int sex = -1;
        if (!TextUtils.isEmpty(mGenderSTR))
        {
            if (mGenderSTR.equals(male))
            {
                sex = 0;
            }
            else if (mGenderSTR.equals(female))
            {
                sex = 1;
            }
            else
            {
                sex = -1;
            }
        }

        // 1.保存到数据库里面
        final BasicUserInfoBean bean = new BasicUserInfoBean(0, mHeadPicSTR, mCitySTR, sex, null);
        // 数据库操作要放到子线程
        Config.execute(new Runnable()
        {
            @Override
            public void run()
            {
                mPersonInfoDB.insertData(bean);
            }
        });

        // 2.上传到服务器
        UpLoadUserBasicInfoRequestData requestData = new UpLoadUserBasicInfoRequestData(this, mHeadPicSTR, mCitySTR,
                sex + "", mBirthdaySTR);
        PTHTTPManager.getHttp().asynPost(Config.UPLOAD_BASIC_INFO_URL, requestData, new IResponse()
        {

            @Override
            public void onSuccess(String content)
            {
                try
                {
                    JSONObject object = new JSONObject(content);
                    String ret_code = null;
                    if (!object.isNull("ret_code"))
                    {
                        ret_code = object.getString("ret_code");
                    }
                    if (!"0000".equals(ret_code))
                    {
                        Toast.makeText(YellowpagePersonalInfoActivity.this,
                                getString(R.string.putao_personal_edit_upload_fail), Toast.LENGTH_SHORT).show();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFail(int errorCode)
            {
                Toast.makeText(YellowpagePersonalInfoActivity.this,
                        getString(R.string.putao_personal_edit_upload_fail), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化数据 void
     */
    private void initCommonInfoData()
    {
        // 设置已绑定的手机号码
        String bindPhone = "";
        RelateUser relateUser = AccountManager.getInstance().getRelateUser(RelateUser.TYPE_PHONE);
        if (relateUser != null)
        {
            bindPhone = AccountManager.getInstance().getDisplayName(relateUser);
        }
        mBindPhoneTextView.setText(bindPhone);
    }

    /**
     * 初始化布局 void
     */
    private void initView()
    {
        initTitleView();
        initHeadImageView();
        initCityInfoView();
        initGenderInfoView();
        initBirthdayInfoView();
        initCommInfoView();
        initSetPasswordView();
    }

    /**
     * 初始化title void
     */
    private void initTitleView()
    {
        setTitle(R.string.putao_personal_data_title);
        findViewById(R.id.back_layout).setOnClickListener(this);
    }

    /**
     * 初始化设置密码布局 void
     */
    private void initSetPasswordView()
    {
        // 绑定手机号
        mBindPhoneTextView = (TextView) findViewById(R.id.putao_personal_data_phone_number_tv);
        // 设置密码
        mSetPassWordLayout = (RelativeLayout) findViewById(R.id.putao_set_password);
        mSetPassWordLayout.setOnClickListener(this);
        mNotSafeTextView = (TextView) findViewById(R.id.putao_not_safty);
        // 退出登录
        mLogOutButton = (Button) findViewById(R.id.login_out_btn);
        mLogOutButton.setOnClickListener(this);
    }

    /**
     * 初始化常用信息布局 void
     */
    private void initCommInfoView()
    {
        // 常用信息
        mCommInfoLayout = (RelativeLayout) findViewById(R.id.putao_common_info);
        mCommInfoLayout.setOnClickListener(this);
        mCommInfoTextView = (TextView) findViewById(R.id.putao_common_info_hint_tv);
        // 常用地址
        mHomeAddressLayout = (RelativeLayout) findViewById(R.id.putao_personal_data_home_address_rl);
        mHomeAddressLayout.setOnClickListener(this);
        mHomeAddressTextView = (TextView) findViewById(R.id.putao_personal_data_home_address_hint_tv);
        // 车辆信息
        mCarInfoLayout = (RelativeLayout) findViewById(R.id.putao_personal_data_license_number_rl);
        mCarInfoLayout.setOnClickListener(this);
        mCarInfoTextView = (TextView) findViewById(R.id.putao_personal_data_license_number_hint_tv);
    }

    /**
     * 初始化生日信息布局 void
     */
    private void initBirthdayInfoView()
    {
        // 生日
        mBirthdayLayout = (RelativeLayout) findViewById(R.id.putao_personal_data_birthday_ll);
        mBirthdayLayout.setOnClickListener(this);
        mBirthdayTextView = (TextView) findViewById(R.id.putao_personal_data_birthday_tv);
    }

    /**
     * 初始化性别布局 void
     */
    private void initGenderInfoView()
    {
        male = getString(R.string.putao_personal_data_male);
        female = getString(R.string.putao_personal_data_femel);
        // 性别
        mGenderLayout = (RelativeLayout) findViewById(R.id.putao_personal_data_gender_ll);
        mGenderLayout.setOnClickListener(this);
        mGenderTextView = (TextView) findViewById(R.id.putao_personal_data_gender_tv);
    }

    /**
     * 初始化地区布局 void
     */
    private void initCityInfoView()
    {
        mCityDataLayout = (RelativeLayout) findViewById(R.id.putao_personal_data_city_ll);
        mCityDataLayout.setOnClickListener(this);
        mCityTextView = (TextView) findViewById(R.id.putao_personal_data_city_tv);
    }

    /**
     * 显示省份城市滑轮 void
     */
    private void showCityDialog()
    {
        // 加载省份城市数据,如果已经加载过就不再加载
        if (mProvinceItems.size() == 0 || mProvinceItems.isEmpty())
        {
            showLoadingDialog();
            Config.execute(new Runnable()
            {

                @Override
                public void run()
                {
                    // 从数据库加载数据
                    loadCityData();
                    // 数据加载完毕之后再显示滑轮框
                    mHandler.sendEmptyMessage(CODE_SHOW_WHEEL_CITY_DIALOG);
                }
            });
        }
        else
        {
            showWheelCityDialog();
        }

    }

    private void showWheelCityDialog()
    {
        dismissLoadingDialog();
        if (mCityDialog == null)
        {
            mCityDialog = CommonDialogFactory.getDialog(this, R.style.Theme_Ptui_Dialog_Wheel);
            mCityDialog.setTitle(getString(R.string.putao_personal_data_area));
            mCityDialog.setNegativeButton(getString(R.string.putao_cancel), new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    mCityDialog.dismiss();
                }
            });
            mCityDialog.setPositiveButton(getString(R.string.putao_confirm), new OnClickListener()
            {

                @Override
                public void onClick(View view)
                {
                    mCityDialog.dismiss();
                    mChangeFlag = true;
                    // 提取滑轮中的省份和城市
                    ProvinceItem provinceItem = mProvinceItems.get(0);
                    String city = "";
                    int provinceIndex = mProvinceWheelView.getCurrentItem();
                    if (provinceIndex >= 0 && provinceIndex < mProvinceItems.size())
                    {
                        provinceItem = mProvinceItems.get(provinceIndex);
                        mCitiesList.clear();
                        mCitiesList.addAll(provinceItem.cityList);
                        int cityIndex = mCityWheelView.getCurrentItem();
                        if (cityIndex >= 0 && cityIndex < mCitiesList.size())
                        {
                            city = mCitiesList.get(cityIndex);
                            if ("---".equals(city))
                            {
                                city = "";
                            }
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append(provinceItem.provinceName).append("  ").append(city);
                        mCitySTR = sb.toString();
                        mCityTextView.setText(mCitySTR);
                    }

                }
            });

            LinearLayout wheelContainer = (LinearLayout) mCityDialog.getContainerLayout();
            // 只有省市两级联动，把中间的wheelview去掉
            wheelContainer.findViewById(R.id.wheel_center).setVisibility(View.GONE);
            mProvinceWheelView = (WheelView) wheelContainer.findViewById(R.id.wheel_left);
            mProvinceWheelView.setVisibleItems(3);
            mProvinceAdapter = new WheelTextAdapter(this, mProvincesList);
            mProvinceWheelView.setViewAdapter(mProvinceAdapter);
            mProvinceWheelView.addChangingListener(this);

            mCityWheelView = (WheelView) wheelContainer.findViewById(R.id.wheel_right);
            mCityWheelView.setVisibleItems(3);
            mCityAdapter = new WheelTextAdapter(this, mCitiesList);
            mCityWheelView.setViewAdapter(mCityAdapter);
            mCityWheelView.addChangingListener(this);
        }

        // 显示已设定的城市，或显示默认第一个
        if (!TextUtils.isEmpty(mCitySTR))
        {
            // 省份和城市之间是以两个空格来分开的
            String[] area = mCitySTR.split("  ");
            String province = "";
            String city = "";
            if (area.length > 0)
            {
                province = area[0];
            }
            if (area.length > 1)
            {
                city = area[1];
            }
            // 遍历省份列表
            if (!mProvinceItems.isEmpty() && !TextUtils.isEmpty(province))
            {
                for (int i = 0; i < mProvinceItems.size(); i++)
                {
                    if (mProvinceItems.get(i).provinceName.equals(province))
                    {
                        mProvinceWheelView.setCurrentItem(i);
                        mCitiesList.clear();
                        mCitiesList.addAll(mProvinceItems.get(i).cityList);
                        break;
                    }
                }
            }
            // 遍历城市列表
            if (!mCitiesList.isEmpty() && !TextUtils.isEmpty(city))
            {
                for (int j = 0; j < mCitiesList.size(); j++)
                {
                    if (mCitiesList.get(j).equals(city))
                    {
                        mCityWheelView.setCurrentItem(j);
                        break;
                    }
                }
            }
        }
        else
        {
            mProvinceWheelView.setCurrentItem(0);
            mProvinceAdapter.setData(mProvincesList);
            mCityWheelView.setCurrentItem(0);
            mCityAdapter.setData(mCitiesList);
        }

        mCityDialog.show();
    }

    /**
     * 加载省份城市数据 void
     */
    private void loadCityData()
    {
        if (mProvinceItems != null && !mProvinceItems.isEmpty())
        {
            return;
        }
        LinkedList<String> provinceList = mCityListDB.getDistrictNameByParentId(this, 0);
        if (provinceList != null && provinceList.size() > 0)
        {
            mProvincesList.clear();
            mProvincesList.addAll(provinceList);
            mProvinceItems.clear();
            for (String province : provinceList)
            {
                ProvinceItem provinceItem = new ProvinceItem();
                provinceItem.provinceName = province;
                // 根据省份名字去查询省份id
                int provinceId = mCityListDB.getCityIdByCityName(province);
                // 获取每个省份对应的城市列表
                LinkedList<String> cityList = mCityListDB.getDistrictNameByParentId(this, provinceId);
                if (cityList != null && cityList.size() > 0)
                {
                    provinceItem.cityList.addAll(cityList);
                }
                // provinceItem.cityList.add("---");
                mProvinceItems.add(provinceItem);
            }
        }
    }

    /**
     * 初始化头部布局 void
     */
    private void initHeadImageView()
    {
        // 设置密码提示
        mDataHintLayout = (LinearLayout) findViewById(R.id.putao_personal_data_hint_ll);
        mDataHintLayout.setOnClickListener(this);
        mHintDisappearImageView = (ImageView) findViewById(R.id.putao_personal_data_hint_disappear_iv);
        mHintDisappearImageView.setOnClickListener(this);
        // 头像
        mHeadDataLayout = (RelativeLayout) findViewById(R.id.putao_personal_data_icon_rl);
        mHeadDataLayout.setOnClickListener(this);
        mHeadImageView = (ImageView) findViewById(R.id.putao_personal_data_icon_iv);
    }

    /**
     * 相机拍摄图片作为头像 void
     */
    protected void takePictureByCamera()
    {
        // 没有sd卡，直接返回
        if (!SystemUtil.hasSdcard())
        {
            return;
        }
        Intent intentByCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentByCapture.putExtra(MediaStore.EXTRA_OUTPUT, mHeadIconUri);
        startActivityForResult(intentByCapture, CODE_CAMERA_REQUEST);
    }

    /**
     * 从相册选取图片 void
     */
    protected void chooseImageFromGallery()
    {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_PICK);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_CANCELED)
        {
            return;
        }
        else if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case CODE_GALLERY_REQUEST:
                    Uri galleryUri = intent.getData();
                    cropRawPhoto(galleryUri);
                    break;
                case CODE_CAMERA_REQUEST:
                    cropRawPhoto(mHeadIconUri);
                    break;
                case CODE_RESULT_REQUEST:
                    if (mHeadIconUri != null)
                    {
                        Bitmap photo = null;
                        try
                        {
                            photo = Media.getBitmap(getContentResolver(), mHeadIconUri);
                        }
                        catch (FileNotFoundException e)
                        {
                            e.printStackTrace();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        if (photo != null)
                        {
                            // 同时将图片上传到七牛服务器
                            uploadImgFile(photo);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 账户头像上传到七牛服务器
     * 
     * @param photo void
     */
    private void uploadImgFile(final Bitmap photo)
    {
        if(mProgressDialog == null)
        {
            mProgressDialog = CommonDialogFactory.getProgressDialog(this, R.style.Theme_Ptui_Dialog_Progress);
            mProgressDialog.setMessage(getString(R.string.putao_personal_data_uploading_icon));
        }
        mProgressDialog.show();
        mChangeFlag = true;
        Config.execute(new Runnable()
        {
            @Override
            public void run()
            {
                if (photo == null)
                {
                    if (mHandler != null)
                    {
                        mHandler.sendEmptyMessage(CODE_UPLOAD_FIAL);
                    }
                }
                // 首先将photo转成字节数组
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(CompressFormat.JPEG, 100, stream);
                // 上传到七牛服务器的具体内容，字节数组
                byte[] data = stream.toByteArray();

                // 上传管理器
                UploadManager uploadManager = new UploadManager();
                Map<String, String> params = new HashMap<String, String>();
                UploadOptions opt = new UploadOptions(params, null, true, null, null);
                // 从葡萄服务器获取token
                String token = QiNiuCloudManager.getInstance().getUploadToken(
                        Config.YELLOW_PAGE_FEEDBACK_IMG_UPLOAD_TOKEN);
                if (TextUtils.isEmpty(token))
                {
                    if (mHandler != null)
                    {
                        mHandler.sendEmptyMessage(CODE_UPLOAD_FIAL);
                    }
                }
                // 上传文件需要的key，对应的是指定七牛服务上的文件名
                String key = QiNiuCloudManager.getInstance().getExpectKey(mHeadIconUri.toString());
                // 上传结果回调
                UpCompletionHandler upCompletionHandler = new UpCompletionHandler()
                {

                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject res)
                    {
                        if (info.isOK())
                        {
                            // 上传成功,存储于七牛服务器中的图片地址
                            mHeadPicSTR = Config.BUCKET_NAME_URL + key;
                            // handler发消息到主线程更新ui
                            if (mHandler != null)
                            {
                                mHandler.sendEmptyMessage(CODE_UPLOAD_SUCCESS);
                            }
                        }
                        else
                        {
                            if (mHandler != null)
                            {
                                mHandler.sendEmptyMessage(CODE_UPLOAD_FIAL);
                            }
                        }
                    }
                };
                // 上传操作
                uploadManager.put(data, key, token, upCompletionHandler, null);
            }
        });

    }

    /**
     * 裁剪照片
     * 
     * @param galleryUri void
     */
    private void cropRawPhoto(Uri uri)
    {
        Intent intentCrop = new Intent("com.android.camera.action.CROP");
        intentCrop.setDataAndType(uri, "image/*");
        intentCrop.putExtra("crop", "true");
        intentCrop.putExtra("aspectX", 1);
        intentCrop.putExtra("aspectY", 1);
        intentCrop.putExtra("outputX", output_X);
        intentCrop.putExtra("outputY", output_Y);
        intentCrop.putExtra("outputFormat", "JPEG");
        intentCrop.putExtra("noFaceDetection", true);
        intentCrop.putExtra("return-data", false);
        intentCrop.putExtra(MediaStore.EXTRA_OUTPUT, mHeadIconUri);
        startActivityForResult(intentCrop, CODE_RESULT_REQUEST);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onClick(View view)
    {
        int id = view.getId();
        switch (id)
        {
            case R.id.back_layout:
                onBackPressed();
                break;
            case R.id.putao_personal_data_icon_rl:
                showHeadImageDialog();
                break;
            case R.id.login_out_btn:
                showLogOutDialog();
                break;
            case R.id.putao_personal_data_city_ll:
                showCityDialog();
                break;
            case R.id.putao_personal_data_gender_ll:
                showGenderDialog();
                break;
            case R.id.putao_personal_data_birthday_ll:
                showBirthdayDialog();
                break;
            case R.id.putao_set_password:
                setPassWord();
                break;
            default:
                break;
        }
    }
    
    /**
     * 设置密码
     * void
     */
    private void setPassWord()
    {
        Intent intent = new Intent(YellowpagePersonalInfoActivity.this, YellowpageModifyPasswordActivity.class);
        RelateUser relateUser = AccountManager.getInstance().getRelateUser(RelateUser.TYPE_PHONE);
        String showStr = relateUser.accName.substring(0, 3) + " " + relateUser.accName.substring(3, 7) + " "
                + relateUser.accName.substring(7);
        intent.putExtra("old_phone", showStr);
        startActivity(intent);
    }

    /**
     * 弹出生日选择框
     * void
     */
    private void showBirthdayDialog()
    {
        if (mBirthdayDialog == null)
        {
            mBirthdayDialog = CommonDialogFactory.getDialog(this, R.style.Theme_Ptui_Dialog_Wheel);
            mBirthdayDialog.setTitle(getString(R.string.putao_personal_data_birthday));
            mBirthdayDialog.setNegativeButton(R.string.putao_cancel, new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                   mBirthdayDialog.dismiss();
                }
            });
            mBirthdayDialog.setPositiveButton(R.string.putao_confirm, new OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    mBirthdayDialog.dismiss();
                    mChangeFlag = true;
                    int year = mYearWheelView.getCurrentItem() + mCurrentYear - 100;
                    int month = mMonthWheelView.getCurrentItem() + 1;
                    int day = mDayWheelView.getCurrentItem() + 1;
                    StringBuilder sb = new StringBuilder();
                    sb.append(year).append("-").append(month).append("-").append(day);
                    mBirthdaySTR = sb.toString();
                    mBirthdayTextView.setText(mBirthdaySTR);
                }
            });
            LinearLayout wheelContainer = (LinearLayout) mBirthdayDialog.getContainerLayout();
            // 年，年月的滚动控件不用设置滚动事件监听
            Calendar calendar = Calendar.getInstance();
            mCurrentYear = calendar.get(Calendar.YEAR);
            mYearWheelView = (WheelView) wheelContainer.findViewById(R.id.wheel_left);
            mYearWheelView.setVisibleItems(3);
            mYearAdapter = new WheelNumericAdapter(this, mCurrentYear-100, mCurrentYear, getString(R.string.putao_personal_data_year));
            mYearWheelView.setViewAdapter(mYearAdapter);
            //月
            mMonthWheelView = (WheelView) wheelContainer.findViewById(R.id.wheel_center);
            mMonthWheelView.setVisibleItems(3);
            mMonthAdapter = new WheelNumericAdapter(this, 1, 12, getString(R.string.putao_personal_data_month));
            mMonthWheelView.setViewAdapter(mMonthAdapter);
            //日
            mDayWheelView = (WheelView) wheelContainer.findViewById(R.id.wheel_right);
            mDayWheelView.setVisibleItems(3);
            OnWheelChangedListener listener = new OnWheelChangedListener()
            {
                @Override
                public void onChanged(WheelView wheel, int oldValue, int newValue)
                {
                  //只要是年月发生变化，就要去检查日是否也联动发生变化
                  updateDays(mYearWheelView, mMonthWheelView, mDayWheelView);
                }
            };
            mYearWheelView.addChangingListener(listener);
            mMonthWheelView.addChangingListener(listener);
            updateDays(mYearWheelView, mMonthWheelView, mDayWheelView);
        }
        //mBirthdaySTR是否为空
        if (!TextUtils.isEmpty(mBirthdaySTR))
        {
            try
            {
                String[] date = mBirthdaySTR.split("-");
                String year = "";
                String month = "";
                String day = "";
                if (date.length > 0)
                {
                    year = date[0];
                }
                // 将year转为为year index
                if (!TextUtils.isEmpty(year))
                {
                    int yearIndex = Integer.parseInt(year) - (mCurrentYear - 100);
                    mYearWheelView.setCurrentItem(yearIndex);
                }
                if (date.length > 1)
                {
                    month = date[1];
                }
                //将month转化为month index
                if(!TextUtils.isEmpty(month))
                {
                    int monthIndex = Integer.parseInt(month) - 1;
                    mMonthWheelView.setCurrentItem(monthIndex);
                }
                if(date.length > 2)
                {
                    day = date[2];
                }
                //将day转化为day index
                if(!TextUtils.isEmpty(day))
                {
                    int dayIndex = Integer.parseInt(day) - 1;
                    mDayWheelView.setCurrentItem(dayIndex);
                }
            }
            catch (Exception e)
            {
                LogUtil.w("YellowpagePersonalInfoActivity", "catch Exception throw by choosePicture", e);
            }
        }
        else
        {
            mYearWheelView.setCurrentItem(73);
        }
        mBirthdayDialog.show();
    }
    
    /**
     * 只要是年月发生变化，就要去检查日是否也联动发生变化(比如有些年份二月28天，有些年份是29天)
     * 
     * @param mYearWheelView2
     * @param mMonthWheelView2
     * @param mDayWheelView2 void
     */
    private void updateDays(WheelView year, WheelView month, WheelView day)
    {
        // 选择的年月对应的那个月总共有多少天
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year.getCurrentItem());
        calendar.set(Calendar.MONTH, month.getCurrentItem());
        mMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        mDayAdapter = new WheelNumericAdapter(this, 1, mMaxDay, getString(R.string.putao_personal_data_day));
        mDayWheelView.setViewAdapter(mDayAdapter);
    }

    /**
     * 显示性别弹出框 void
     */
    private void showGenderDialog()
    {
        if (mGenderDialog == null)
        {
            mGenderDialog = CommonDialogFactory.getDialog(this, R.style.Theme_Ptui_Dialog_ListView);
            mGenderDialog.setTitle(getString(R.string.putao_personal_data_gender));
            ArrayList<String> genderList = new ArrayList<String>();
            genderList.add(male);
            genderList.add(female);
            mGenderDialog.setSingleChoiceListViewDatas(genderList);
            mGenderDialog.setListViewItemClickListener(new OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    mGenderDialog.dismiss();
                    mChangeFlag = true;
                    // 0是男，1是女
                    if (position == 0)
                    {
                        mGenderSTR = male;
                        mGenderTextView.setText(male);
                    }
                    else if (position == 1)
                    {
                        mGenderSTR = female;
                        mGenderTextView.setText(female);
                    }
                }
            });
        }
        //默认选择的是男吧
        if (!TextUtils.isEmpty(mGenderSTR))
        {
            if (mGenderSTR.equals(male))
            {
                mGenderDialog.getListView().setItemChecked(0, true);
            }
            else if (mGenderSTR.equals(female))
            {
                mGenderDialog.getListView().setItemChecked(1, true);
            }
        }
        else
        {
            mGenderDialog.getListView().setItemChecked(0, true);
        }
        mGenderDialog.show();
    }

    /**
     * 设置头像弹出框 void
     */
    private void showHeadImageDialog()
    {
        mHeadImageDialog = CommonDialogFactory.getDialog(this, R.style.Theme_Ptui_Dialog_ListView);
        mHeadImageDialog.setTitle(R.string.putao_personal_data_icon);
        String[] imageList = new String[]
        { getString(R.string.putao_personal_data_get_icon_from_gallery),
                getString(R.string.putao_personal_data_get_icon_from_camera) };
        mHeadImageDialog.setListViewDatas(imageList);
        mHeadImageDialog.setListViewItemClickListener(new OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                mHeadImageDialog.dismiss();
                // 很据position的不同,选中相册还是相机
                if (position == 0)
                {
                    // 相册
                    chooseImageFromGallery();
                }
                else
                {
                    // 相机
                    takePictureByCamera();
                }

            }
        });
        mHeadImageDialog.show();
    }

    /**
     * 退出登录:弹框，退出登录清空ptuser的信息即可 void
     */
    private void showLogOutDialog()
    {
        mLogOutDialog = CommonDialogFactory.getDialog(YellowpagePersonalInfoActivity.this,
                R.style.Theme_Ptui_Dialog_OkCancel);
        mLogOutDialog.setTitle(R.string.putao_common_prompt);
        mLogOutDialog.setMessage(R.string.putao_msg_logout_dialog);
        mLogOutDialog.setPositiveButton(R.string.putao_confirm, new OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                mLogOutDialog.dismiss();
                // 登出加载框
                showLoadingDialog(false);
                // 如果个人信息有改动，需要保存个人信息
                if (mChangeFlag)
                {
                    saveUserBasicInfo();
                    mChangeFlag = false;
                }
                AccountManager.getInstance().logout(YellowpagePersonalInfoActivity.this);
                dismissLoadingDialog();
                // 直接返回menufragment
                setResult(RESULT_OK);
                finish();
            }
        });
        mLogOutDialog.setNegativeButton(R.string.putao_cancel, new OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                mLogOutDialog.dismiss();
            }
        });
        mLogOutDialog.show();
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue)
    {
        if (wheel == mProvinceWheelView)
        {
            updateCities();
        }
        else if (wheel == mCityWheelView)
        {
            // 改变选中条目的字体颜色
        }
    }

    /**
     * 当省份滑动改变时，相应的改变城市 void
     */
    private void updateCities()
    {
        // 获取当前选中省份的索引
        int currentIndex = mProvinceWheelView.getCurrentItem();
        ProvinceItem currentProvince = mProvinceItems.get(currentIndex);
        LinkedList<String> cityList = currentProvince.cityList;
        // 清空城市列表
        mCitiesList.clear();
        if (cityList != null && cityList.size() > 0)
        {
            mCitiesList.addAll(cityList);
        }
        else
        {
            mCitiesList.add("---");
        }
        mCityAdapter.setData(mCitiesList);
        mCityWheelView.setCurrentItem(0);
    }

    class ProvinceItem
    {
        private String provinceName = "";

        private LinkedList<String> cityList = new LinkedList<String>();
    }
}
