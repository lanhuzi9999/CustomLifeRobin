package so.contacts.hub.basefunction.account.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.lives.depend.theme.dialog.CommonDialog;
import com.lives.depend.theme.dialog.CommonDialogFactory;
import com.putao.live.R;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

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
import so.contacts.hub.basefunction.account.bean.BasicUserInfoBean;
import so.contacts.hub.basefunction.config.Config;
import so.contacts.hub.basefunction.imageloader.DataLoader;
import so.contacts.hub.basefunction.imageloader.image.ImageLoaderFactory;
import so.contacts.hub.basefunction.storage.db.PersonInfoDB;
import so.contacts.hub.basefunction.utils.QiNiuCloudManager;
import so.contacts.hub.basefunction.utils.SystemUtil;

public class YellowpagePersonalInfoActivity extends BaseActivity implements OnClickListener
{
    private static final String TAG = "YellowpagePersonalInfoActivity";

    private static final String IMAGE_FILE_NAME = "head_image.jpg";

    private static final int CODE_GALLERY_REQUEST = 0xa0;

    private static final int CODE_CAMERA_REQUEST = 0xa1;

    private static final int CODE_RESULT_REQUEST = 0xa2;

    private static final int CODE_UPLOAD_SUCCESS = 0xa3;

    private static final int CODE_UPLOAD_FIAL = 0xa4;

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

    // 性别
    private TextView mGenderTextView;

    private RelativeLayout mBirthdayLayout;

    // 生日
    private TextView mBirthdayTextView;

    private RelativeLayout mCommInfoLayout;

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

    // ===============================view end========================
    // 个人信息是否有修改
    private boolean mChangeFlag = false;

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 480;

    private static int output_Y = 480;

    // 保存头像的uri
    private Uri mHeadIconUri = null;

    // 头像上传到七牛服务器后返回的图片地址
    private String mHeadIconStr;

    // 头像加载器
    private DataLoader mImageLoader;

    // 是否已经设置过密码
    private int mHasSetPassword;

    // 选中的地区
    private String mCitySTR;

    // 选择的性别
    private String mGenderSTR;

    // 选择的生日
    private String mBirthdaySTR;

    private PersonInfoDB mPersonInfoDB;

    // 主线程handler
    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case CODE_UPLOAD_SUCCESS:
                    if (mImageLoader != null)
                    {
                        mImageLoader.loadData(mHeadIconStr, mHeadImageView);
                    }
                    break;
                case CODE_UPLOAD_FIAL:
                    Toast.makeText(YellowpagePersonalInfoActivity.this,
                            getString(R.string.putao_personal_data_upload_icon_fail), Toast.LENGTH_SHORT).show();
                    break;
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
        initView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        initData();
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
        // 1.保存到数据库里面
        BasicUserInfoBean bean = new BasicUserInfoBean(0, mHeadIconStr, null, 0, null);
        mPersonInfoDB.insertData(bean);

        // 2.上传到服务器
    }

    /**
     * 初始化数据 void
     */
    private void initData()
    {
        String imageUrl = mPersonInfoDB.queryImgUrl();
        if (!TextUtils.isEmpty(imageUrl) && mImageLoader != null)
        {
            mImageLoader.loadData(imageUrl, mHeadImageView);
        }
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
     * 初始化title
     * void
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
        // 地区
        mCityDataLayout = (RelativeLayout) findViewById(R.id.putao_personal_data_city_ll);
        mCityDataLayout.setOnClickListener(this);
        mCityTextView = (TextView) findViewById(R.id.putao_personal_data_city_tv);
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
                            mHeadIconStr = Config.BUCKET_NAME_URL + key;
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
                mHeadImageDialog.show();
                break;

            default:
                break;
        }
    }

    @Override
    protected boolean needReset()
    {
        return true;
    }
}
