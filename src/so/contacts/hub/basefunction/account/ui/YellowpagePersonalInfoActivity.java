package so.contacts.hub.basefunction.account.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.BreakIterator;

import com.putao.live.R;

import android.R.integer;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.style.BulletSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import so.contacts.hub.BaseActivity;
import so.contacts.hub.basefunction.widget.dialog.CommonDialog;
import so.contacts.hub.basefunction.widget.dialog.CommonDialogFactory;

public class YellowpagePersonalInfoActivity extends BaseActivity implements OnClickListener
{
    private static final String TAG = "YellowpagePersonalInfoActivity";

    private static final String IMAGE_FILE_NAME = "head_image.jpg";

    private Uri mHeadIconUri = null;

    private static final int CODE_GALLERY_REQUEST = 0xa0;

    private static final int CODE_CAMERA_REQUEST = 0xa1;

    private static final int CODE_RESULT_REQUEST = 0xa2;

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 480;

    private static int output_Y = 480;

    /**
     * view start
     */
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.putao_personal_data_layout);
        mHeadIconUri = Uri.fromFile(new File(getExternalCacheDir(), IMAGE_FILE_NAME));
        initView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        initData();
    }

    /**
     * 初始化数据 void
     */
    private void initData()
    {

    }

    /**
     * 初始化布局 void
     */
    private void initView()
    {
        initHeadImageView();
        initCityInfoView();
        initGenderInfoView();
        initBirthdayInfoView();
        initCommInfoView();
        initSetPasswordView();
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

        mHeadImageDialog = CommonDialogFactory.getListCommonDialog(this);
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

        // Intent intentFromGallery = new Intent(Intent.ACTION_PICK,
        // MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
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

                    break;
                case CODE_RESULT_REQUEST:
                    if (mHeadIconUri != null)
                    {
                        setImageToHeadView();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 提取裁剪之后的图片，设置头像
     * 
     * @param intent void
     */
    private void setImageToHeadView()
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

            mHeadImageView.setImageBitmap(photo);
        }
    }

    /**
     * 裁剪照片
     * 
     * @param galleryUri void
     */
    private void cropRawPhoto(Uri uri)
    {
        // Intent intentCrop = new Intent("com.android.camera.action.CROP");
        // intentCrop.setDataAndType(uri, "image/*");
        // // 设置裁剪
        // intentCrop.putExtra("crop", "true");
        //
        // // aspectX , aspectY :宽高的比例
        // intentCrop.putExtra("aspectX", 1);
        // intentCrop.putExtra("aspectY", 1);
        //
        // // outputX , outputY : 裁剪图片宽高
        // intentCrop.putExtra("outputX", output_X);
        // intentCrop.putExtra("outputY", output_Y);
        // intentCrop.putExtra("return-data", true);

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
