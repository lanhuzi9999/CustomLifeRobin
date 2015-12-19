package so.contacts.hub.basefunction.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import so.contacts.hub.ContactsApp;
import so.contacts.hub.basefunction.account.bean.PTUser;
import so.contacts.hub.basefunction.account.manager.AccountManager;
import so.contacts.hub.basefunction.config.Config;
import so.contacts.hub.basefunction.location.LBSManager;
import so.contacts.hub.basefunction.operate.cms.bean.CMSResponseBaseData;
import so.contacts.hub.basefunction.storage.db.CMSDataDB;

import android.R.integer;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;

import com.lives.depend.utils.LogUtil;
import com.putao.live.R;

public class YellowUtil
{
    private static final String TAG = "YellowUtil";

    public static final String TargetActivity = "TargetActivity";

    public static final String TargetParams = "TargetParams";

    /**
     * start modified by zjh at 2014/09/12 黄页静态数据 部分常量标识
     */
    // 黄页静态数据 parentId = 0
    public static final int YELLOW_PAGE_PARENTID_ALL = 0;

    // 黄页静态数据 "全部"的 categoryId
    public static final int YELLOW_PAGE_CATEGORY_ID_ALL = 1;

    public static final int YELLOW_PAGE_CATEGORY_ID_HOT = 1;

    // 黄页静态数据 "常用"的 categoryId
    public static final int YELLOW_PAGE_CATEGORY_ID_OFFEN = 2;

    // 黄页静态数据 默认lastSort = -1;
    public static final int YELLOW_CATEGORY_DEFAULT_LASTSORT = -1;

    // 黄页静态数据 默认remindCode = -1;
    public static final int YELLOW_CATEGORY_DEFAULT_REMIND_CODE = -1;

    /**
     * 编辑类型，通过该字段可扩展更多功能 editType = 0：默认值 editType = 1：不可删除 editType = 2：用户添加类型
     */
    public static final int YELLOW_CATEGORY_EDITTYPE_DEFAULT = 0;

    public static final int YELLOW_CATEGORY_EDITTYPE_NOT_DEL = 1;

    public static final int YELLOW_CATEGORY_EDITTYPE_USER_ADD = 2;

    /**
     * 被改变类型 change_type = 0：默认值 change_type = 1：由用户改变 change_type = 2：由服务器改变
     */
    public static final int YELLOW_CATEGORY_CHANGE_TYPE_DEFAULT = 0;

    public static final int YELLOW_CATEGORY_CHANGE_TYPE_USER_MODITY = 1;

    public static final int YELLOW_CATEGORY_CHANGE_TYPE_SERVICE_MODITY = 2;

    /**
     * end modified by zjh at 2014/09/12
     */
    public static final String TargetIntentParams = "TargetIntentParams"; // intent传输target_params参数

    public static final String ClickIntentParams = "ClickIntentParams"; // intent传输点击扩展参数

    public static final String ServiceIdParams = "ServiceIdParams"; // intent传输ServiceId参数

    public static final String CP_INFO_PARAMS = "CpInfoParams"; // intent传输cp信息参数
                                                                // add by hyl
                                                                // 2015-3-25

    public static final String SEARCH_INFO_PARAMS = "SearchInfoParams"; // intent传输搜索域信息参数
                                                                        // add
                                                                        // by cj
                                                                        // 2015-04-11

    /*
     * add by zj 2015-5-13 start 同步联想支付控制
     */
    public static final String P_PAYMENT_CONFIG_VERSION = "p_payment_config_version";

    // end 2015-5-13 by zj

    // public static final String DefCategoryActivity =
    // so.contacts.hub.ui.yellowpage.YellowPageCategoryActivity.class
    // .getName(); // 显示类别和列表activity
    // public static final String DefWubaCityActivity =
    // so.contacts.hub.ui.yellowpage.YellowPageWubaCityActivity.class
    // .getName(); // 显示58同城activity
    //
    // public static String selectedCity(Context context) {
    // SharedPreferences pref = context.getSharedPreferences(
    // ConstantsParameter.SHARED_PREFS_YELLOW_PAGE,
    // Context.MODE_MULTI_PROCESS);
    // return pref.getString(ConstantsParameter.YELLOW_PAGE_SELECTED_CITY, "");
    // }
    //
    // public static void saveCity(Context context, String name) {
    // SharedPreferences pref = context.getSharedPreferences(
    // ConstantsParameter.SHARED_PREFS_YELLOW_PAGE,
    // Context.MODE_MULTI_PROCESS);
    // Editor e = pref.edit();
    // e.putString(ConstantsParameter.YELLOW_PAGE_SELECTED_CITY, name);
    // e.commit();
    // }
    //
    // /**
    // * 获取常用城市(定过位或手动选择过的城市)
    // * @author jsy
    // * @since 2015-5-7
    // * @return 常用城市
    // */
    // public static LinkedList<String> getOffenUsedCity(Context context){
    // SharedPreferences pref = context.getSharedPreferences(
    // ConstantsParameter.SHARED_PREFS_YELLOW_PAGE,
    // Context.MODE_MULTI_PROCESS);
    // String cityStr =
    // pref.getString(ConstantsParameter.YELLOW_PAGE_OFFEN_USED_CITY, "");
    // LinkedList<String> cityList = new LinkedList<String> ();
    // if(!TextUtils.isEmpty(cityStr)){
    // String [] citys = cityStr.split(",");
    // for(int i = 0 ; i<citys.length;i++){
    // cityList.add(citys[i]);
    // }
    // }
    // LogUtil.d(TAG, "getOffenUsedCity citystr="+cityStr+"cityList="+cityList);
    // return cityList;
    // }
    //
    // /**
    // * 保存常用城市(定过位或手动选择过的城市)
    // * @author jsy
    // * @since 2015-5-7
    // */
    // public static void saveOffenUsedCity(Context context, String cityName){
    // if(TextUtils.isEmpty(cityName)){
    // return;
    // }
    //
    // SharedPreferences pref = context.getSharedPreferences(
    // ConstantsParameter.SHARED_PREFS_YELLOW_PAGE,
    // Context.MODE_MULTI_PROCESS);
    // LinkedList<String> cityList = getOffenUsedCity(context);
    // // modify by wxy 2015-05-18 start
    // // 修改因首页定位城市之后常用城市只显示两个的BUG
    // /**
    // * old code :
    // * if (!cityList.contains(cityName)) {
    // * if(cityList.size()==3){
    // * cityList.remove(0);
    // * }
    // * cityList.add(cityName);
    // * }
    // */
    // if (!cityList.contains(cityName)) {
    // if(cityList.size()==4){
    // cityList.remove(0);
    // }
    // cityList.add(cityName);
    // }
    // else{
    // cityList.remove(cityName);
    // cityList.addLast(cityName);
    // }
    // // end by wxy 2015-05-18
    // String cityStr = cityList.get(0);
    // if(cityList.size()>1){
    // for (int i = 1; i < cityList.size(); i++) {
    // cityStr = cityStr+","+cityList.get(i);
    // }
    // }
    // LogUtil.d(TAG, "saveOffenUsedCity city="+cityStr);
    // Editor e = pref.edit();
    // e.putString(ConstantsParameter.YELLOW_PAGE_OFFEN_USED_CITY, cityStr);
    // e.commit();
    // }
    //
    // /**
    // * add by gwq at 2015-4-10
    // *
    // * 初始城市信息,以首页当前选择城市为准
    // * 如果当前选择城市为空取上一次定位城市
    // * 如果上次定位城市为空，默认城市为深圳
    // */
    // public static String initCity(Context context) {
    // String city = null;
    // city = YellowUtil.selectedCity(context);
    // if(!TextUtils.isEmpty(city)) {
    // return city;
    // }
    // city = LBSServiceGaode.getCity();
    // if(!TextUtils.isEmpty(city)) {
    // return city;
    // }
    //
    // return context.getString(R.string.putao_shenzhen);
    //
    // }
    //
    // //判断是否为电话号码或者座机号码包括区号以及分机号码，返回布尔值
    // public static boolean isNumeric(String input) {
    // if ("360".equals(input) || "361".equals(input)) {
    // return false;
    // }
    // return PhoneNumberUtils.isGlobalPhoneNumber(input);
    // // String
    // regex="1([\\d]{10})|((\\+[0-9]{2,4})?\\(?[0-9]+\\)?-?)?[0-9]{7,8}";
    // // Pattern p = Pattern.compile(regex);
    // // return p.matcher(input).matches();
    // }
    //
    //
    // /**
    // * 加载CMS默认静态数据
    // * add by zjh 2015-03-19
    // */
    // public static void loadCmsDefaultData(){
    // CMSDataDB cmsDataDB = Config.getDatabaseHelper().getCMSDataDB();
    // boolean hasCommServicesData = false;
    // boolean hasCategoryServicesData = false;
    // if( cmsDataDB.getCommonServicesData() != null ){//常用服务
    // hasCommServicesData = true;
    // }
    // if( cmsDataDB.getCategoryServicesData() != null ){//分类服务
    // hasCategoryServicesData = true;
    // }
    // if( hasCommServicesData && hasCategoryServicesData ){
    // //说明内置静态数据已经初始化,避免不必要的文件加载
    // return;
    // }
    // LogUtil.i(TAG, "loadCmsDefaultData init start.");
    //
    // InputStream inputStream = null;
    // InputStreamReader inputStreamReader = null;
    // BufferedReader buffReader = null;
    // StringBuffer strBuffer = new StringBuffer("");
    // String line = null;
    // try {
    // inputStream =
    // ContactsApp.getInstance().getAssets().open("cms_default_data.txt");
    // inputStreamReader = new InputStreamReader(inputStream, "utf-8");
    // buffReader = new BufferedReader(inputStreamReader);
    // while ((line = buffReader.readLine()) != null) {
    // strBuffer.append(line);
    // strBuffer.append("\n");
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // } finally{
    // try {
    // if (inputStream != null){
    // inputStream.close();
    // }
    // if (inputStreamReader != null){
    // inputStreamReader.close();
    // }
    // if (buffReader != null){
    // buffReader.close();
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // String cmsDataStr = strBuffer.toString();
    // if( TextUtils.isEmpty(cmsDataStr) ){
    // return;
    // }
    // JSONArray jsonArray = null;
    // try {
    // jsonArray = new JSONArray(cmsDataStr);
    // int size = jsonArray.length();
    // JSONObject jsonObject = null;
    // CMSResponseBaseData defaultData = null;
    // for(int i = 0; i < size; i++){
    // jsonObject = (JSONObject) jsonArray.get(i);
    // int type = jsonObject.getInt("type");
    // String data = jsonObject.getString("data");
    // int version = jsonObject.getInt("version");
    // defaultData = new CMSResponseBaseData();
    // defaultData.setData_version(version);
    // defaultData.setData(data);
    // if( type == 1 ){
    // //常用服务
    // if( !hasCommServicesData ){
    // //没有数据时，则添加默认数据
    // LogUtil.i(TAG, "loadCmsDefaultData init common service data.");
    // cmsDataDB.updateCommonServicesData(ContactsApp.getInstance(),defaultData);
    // }
    // }else if( type == 2 ){
    // //分类服务
    // if( !hasCategoryServicesData ){
    // //没有数据时，则添加默认数据
    // LogUtil.i(TAG, "loadCmsDefaultData init category service data.");
    // /**
    // * modify by ffh 2015-3-20 start
    // * old code : cmsDataDB.insertCategoryService(defaultData);
    // */
    // cmsDataDB.insertOrUpdateCategoryService(ContactsApp.getInstance(),defaultData);
    // List<FunView> funViewList = cmsDataDB.getAllFunView(defaultData);
    // cmsDataDB.insertOrUpdateServiceList(funViewList);
    // /* end 2015-3-20 by ffh */
    //
    // /**
    // * 已取消个人服务功能，注释该功能
    // * modify by zjh 2015-04-29 start
    // */
    // //loadUserServiceDefaultData(ContactsApp.getInstance(),cmsDataDB,
    // funViewList);// add by zjh 2015-03-21
    // /** modify by zjh 2015-04-29 end */
    // }
    // }else if( type == 3 ){
    // //猜我喜欢
    // if( TextUtils.isEmpty( GuessLikeUtil.loadData("")) ){
    // //没有数据时，则添加默认数据
    // LogUtil.i(TAG, "loadCmsDefaultData init guess like data.");
    // GuessLikeUtil.saveCache(0, data);
    // }
    // }
    // }
    //
    // } catch (JSONException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    //
    // 加载cms默认数据
    public static void loadCmsDefaultData()
    {
        CMSDataDB cmsDataDB = Config.getDatabaseHelper().getCmsDataDB();
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        String line = null;
        StringBuffer stringBuffer = new StringBuffer("");
        // 1.从assets读取默认数据，创建流对象
        // 2.inputStream--->inputStreamReader, 字节流--字符流
        // 3.inputStreamReader--->BufferedReader， 带缓冲区的字符流
        try
        {
            inputStream = ContactsApp.getInstance().getAssets().open("cms_default_data.txt");
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            // 4.一行一行读取BufferedReader，存储到Stringbuffer
            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(line);
                stringBuffer.append("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (inputStream != null)
                {
                    inputStream.close();
                }
                if (inputStreamReader != null)
                {
                    inputStreamReader.close();
                }
                if (bufferedReader != null)
                {
                    bufferedReader.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        String cmsDataStr = stringBuffer.toString();
        // 5.Stringbuffer--->jsonArray，分两部分："type": 1,常用服务；"type": 2,全部服务
        JSONArray jsonArray = null;
        int size = 0;
        JSONObject jsonObject = null;
        CMSResponseBaseData defaultData = null;
        try
        {
            jsonArray = new JSONArray(cmsDataStr);
            size = jsonArray.length();
            if (jsonArray != null)
            {
                for (int i = 0; i < size; i++)
                {
                    jsonObject = (JSONObject) jsonArray.get(i);
                    defaultData = new CMSResponseBaseData();
                    int type = jsonObject.getInt("type");
                    int version = jsonObject.getInt("version");
                    String data = jsonObject.getString("data");
                    defaultData.setData_version(version);
                    defaultData.setData(data);
                    if (type == 1)
                    {
                        // 6.获取jsonArray子元素，将dataversion和data存到数据库(需要创建数据库)
                        // 常用服务 ,创建数据库
                        cmsDataDB.insertOrUpdateCommonServicesData(ContactsApp.getInstance(), defaultData);
                    }
                    else if (type == 2)
                    {
                        // 分类服务（全部服务）
                    }
                    else if (type == 3)
                    {
                        // 猜你喜欢（附近精选）
                    }
                }
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    // /**
    // * 初始化个人常用数据
    // * 在第一次加载分类服务时，如果个人服务没有数据，则添加默认的个人服务数据
    // * add by zjh 2015-03-21
    // */
    // public static void loadUserServiceDefaultData(Context context,CMSDataDB
    // cmsDataDB, List<FunView> funViewList){
    // if( funViewList == null ){
    // return;
    // }
    // List<FunView> userServiceList = cmsDataDB.getUserServicesList(false);
    // if( userServiceList != null &&userServiceList.size() > 0 ){
    // //个人服务已经存在
    // return;
    // }
    // List<FunView> userServiceListTemp = new ArrayList<FunView>();
    // for(FunView funView : funViewList ){
    // int serviceId = funView.getId();
    // if( serviceId == 61 || serviceId == 16 || serviceId == 19 ){
    // //默认个人服务：充话费:61、查余额:16、打车:19
    // userServiceListTemp.add(funView);
    // }
    // }
    // if( userServiceListTemp.size() > 0 ){
    // cmsDataDB.updateUserServiceList(context,userServiceListTemp);
    // }
    // }
    //
    // /**
    // * 加载默认黄页类别静态数据
    // */
    // // public static void loadDefaultCategoryDB() {
    // // YellowPageDB db =
    // ContactsAppUtils.getInstance().getDatabaseHelper().getYellowPageDBHelper();
    // // InputStream in = null;
    // // BufferedReader br = null;
    // //
    // // String encoding = "utf-8";
    // // try {
    // // in = ContactsApp.getInstance().getAssets().open("putao_category.txt");
    // // br = new BufferedReader(new java.io.InputStreamReader(in, encoding));
    // //
    // // int count = 0;
    // // String line = null;
    // // while ((line = br.readLine()) != null) {
    // // if (count++ == 0 || TextUtils.isEmpty(line)) {
    // // continue;
    // // }
    // //
    // // String elements[] = line.split("\t");
    // // if (elements == null || elements.length < 12) {
    // // continue;
    // // }
    // //
    // // CategoryBean cb = new CategoryBean();
    // // cb.setCategory_id(Long.parseLong(elements[0]));
    // // cb.setName(elements[1]);
    // //
    // // if (!TextUtils.isEmpty(elements[2])) {
    // // cb.setShow_name(elements[2]);
    // // }
    // // if (!TextUtils.isEmpty(elements[3])) {
    // // cb.setSort(Integer.parseInt(elements[3]));
    // // }
    // // if (!TextUtils.isEmpty(elements[4])) {
    // // cb.setLastSort(Integer.parseInt(elements[4]));
    // // }
    // //
    // // if (!TextUtils.isEmpty(elements[5])) {
    // // // 大图
    // // cb.setIcon(elements[5]);
    // // }
    // // if (!TextUtils.isEmpty(elements[6])) {
    // // // 大图按下图标
    // // cb.setPressIcon(elements[6]);
    // // }
    // // if (!TextUtils.isEmpty(elements[7])) {
    // // // 前缀小图标
    // // cb.setIconLogo(elements[7]);
    // // }
    // //
    // // if (!TextUtils.isEmpty(elements[8])) {
    // // cb.setTarget_activity(elements[8]);
    // // }
    // // if (!TextUtils.isEmpty(elements[9])) {
    // // cb.setTarget_params(elements[9]);
    // // }
    // // if (!TextUtils.isEmpty(elements[10])) {
    // // cb.setParent_id(Long.parseLong(elements[10]));
    // // }
    // // if (!TextUtils.isEmpty(elements[11])) {
    // // cb.setRemind_code(Integer.parseInt(elements[11]));
    // // }
    // // if (elements.length >= 13 && !TextUtils.isEmpty(elements[12])) {
    // // cb.setKey_tag(elements[12]);
    // // }
    // // if (elements.length >= 14 && !TextUtils.isEmpty(elements[13])) {
    // // cb.setSearch_sort(Integer.parseInt(elements[13]));
    // // }
    // // if (elements.length > 14 && !TextUtils.isEmpty(elements[14])) {
    // // cb.setOld_parent_id(Integer.parseInt(elements[14]));
    // // }
    // // if (elements.length > 15 && !TextUtils.isEmpty(elements[15])) {
    // // cb.setHot_icon(elements[15]);
    // // }
    // // if (elements.length > 16 && !TextUtils.isEmpty(elements[16])) {
    // // cb.setHot_sort(Integer.parseInt(elements[16]));
    // // }
    // // db.insert(cb);
    // // }
    // // LogUtil.d(TAG, "loadDefaultCategoryDB total: " + count);
    // // } catch (IOException e) {
    // // e.printStackTrace();
    // // LogUtil.i(TAG, e.getMessage());
    // // } finally {
    // // try {
    // // if (br != null)
    // // br.close();
    // // if (in != null)
    // // in.close();
    // // } catch (IOException e) {
    // // e.printStackTrace();
    // // }
    // // }
    // // }
    //
    //
    // /**
    // * 加载默认黄页ITEM静态数据
    // * modify by zjh 2015-05-14
    // */
    // public static void loadDefaultItemDB() {
    // YellowPageDB db = ContactsAppUtils.getInstance().getDatabaseHelper()
    // .getYellowPageDBHelper();
    // if ( db.getCategoryItemCount() > 0 ) {
    // //有数据，则不需要进行加载
    // return;
    // }
    //
    // InputStream in = null;
    // BufferedReader br = null;
    //
    // String encoding = "utf-8";
    // int count = 0;
    // try {
    // in = ContactsApp.getInstance().getAssets()
    // .open("putao_category_item.txt");
    // br = new BufferedReader(new java.io.InputStreamReader(in, encoding));
    //
    // String line = null;
    // while ((line = br.readLine()) != null) {
    // if (count++ == 0 || TextUtils.isEmpty(line)) {
    // continue;
    // }
    // String elements[] = line.split("\t");
    // if (elements == null || elements.length < 9) {
    // continue;
    // }
    //
    // ItemBean ib = new ItemBean();
    // ib.setProvider(0); // 提供方，默认0葡萄
    // ib.setItem_id(NumberUtil.parseLongSafe(elements[0]));
    // ib.setCategory_id(NumberUtil.parseLongSafe(elements[1]));
    // ib.setName(elements[2]); // 名字
    //
    // if (!TextUtils.isEmpty(elements[3])) {
    // ib.setIcon(elements[3]);
    // }
    // if (!TextUtils.isEmpty(elements[4])) {
    // ib.setSort(NumberUtil.parseIntSafe(elements[4]));
    // }
    // if (!TextUtils.isEmpty(elements[5])) {
    // ib.setTarget_activity(elements[5]);
    // }
    // if (!TextUtils.isEmpty(elements[6])) {
    // ib.setTarget_params(elements[6]);
    // }
    // if (!TextUtils.isEmpty(elements[7])) {
    // ib.setContent(elements[7]);
    // }
    // if (!TextUtils.isEmpty(elements[8])) {
    // ib.setRemind_code(NumberUtil.parseIntSafe(elements[8]));
    // }
    // if (elements.length >= 10 && !TextUtils.isEmpty(elements[9])) {
    // // 关键字标签
    // ib.setKey_tag(elements[9]);
    // }
    // if (elements.length >= 11 && !TextUtils.isEmpty(elements[10])) {
    // ib.setSearch_sort(NumberUtil.parseIntSafe(elements[10]));
    // }
    //
    // db.insert(ib);
    // }
    // LogUtil.d(TAG, "loadDefaultItemDB total: " + count);
    // } catch (IOException e) {
    // e.printStackTrace();
    // LogUtil.i(TAG, e.getMessage() + " count=" + count);
    // } finally {
    // try {
    // if (br != null)
    // br.close();
    // if (in != null)
    // in.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    //
    //
    //
    // /**
    // * 从后台加载快递数据
    // * add by gwq 2015/3/17
    // */
    //
    // public static void loadDefaultExpressDB(String requsetStr) {
    // BufferedReader br = null;
    // List<Express> list = new ArrayList<Express>();
    //
    // try {
    // URL url = new URL(requsetStr);
    // HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    // br = new BufferedReader(new
    // InputStreamReader(connection.getInputStream(), "utf-8"));
    // String line = null;
    //
    // while ((line = br.readLine()) != null) {
    // if (TextUtils.isEmpty(line)) {
    // continue;
    // }
    // String elements[] = line.split("\t");
    // if (elements == null || elements.length < 4) {
    // continue;
    // }
    // Express express = new Express();
    // express.setName(elements[0]);
    // express.setPinyin(elements[1]);
    // express.setLogo(elements[2]);
    // express.setPhone(elements[3]);
    //
    // list.add(express);
    //
    // }
    //
    // if( list.size() > 0 ){
    // YellowPageDB db = ContactsAppUtils.getInstance().getDatabaseHelper()
    // .getYellowPageDBHelper();
    // db.insertExpressList(list);
    // }
    // } catch (MalformedURLException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // } finally {
    // if (null != br) {
    // try {
    // br.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    // }
    //
    // public static void loadDefaultExpressDB() {
    // InputStream in = null;
    // BufferedReader br = null;
    // String encoding = "utf-8";
    //
    // try {
    // in = ContactsApp.getInstance().getAssets()
    // .open("putao_express.txt");
    // br = new BufferedReader(new InputStreamReader(in, encoding));
    // String line = null;
    //
    // YellowPageDB db = ContactsAppUtils.getInstance().getDatabaseHelper()
    // .getYellowPageDBHelper();
    // while ((line = br.readLine()) != null) {
    // if (TextUtils.isEmpty(line)) {
    // continue;
    // }
    // String elements[] = line.split("\t");
    // if (elements == null || elements.length < 4) {
    // continue;
    // }
    // Express express = new Express();
    // express.setName(elements[0]);
    // express.setPinyin(elements[1]);
    // express.setLogo(elements[2]);
    // express.setPhone(elements[3]);
    // db.insertExpress(express);
    // }
    //
    // } catch (IOException e) {
    // e.printStackTrace();
    // } finally {
    // try {
    // if (br != null)
    // br.close();
    // if (in != null)
    // in.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    //
    //
    // /**
    // * 从后台加载同城火车票火车站数据
    // * add by gwq 2015/3/17
    // *
    // * @param requestStr
    // */
    // public static void loadTrainTicketDB(String requestStr) {
    // List<TongChengCity> cityList = new ArrayList<TongChengCity>();
    //
    // BufferedReader br = null;
    // URL url = null;
    // try {
    // url = new URL(requestStr);
    // HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    // br = new BufferedReader(new
    // InputStreamReader(connection.getInputStream(), "utf-8"));
    //
    // String line = null;
    // while ((line = br.readLine()) != null) {
    // if (TextUtils.isEmpty(line)) {
    // continue;
    // }
    // String elements[] = line.split("\t");
    // if (elements == null || elements.length < 4) {
    // continue;
    // }
    // TongChengCity city = new TongChengCity();
    // city.setStationCode(elements[0]);
    // city.setStationName(elements[1]);
    // city.setQuanPin(elements[2]);
    // city.setJianPin(elements[3]);
    // city.setStationPY(PinyinHelper.getInstance().getFullPinyin(elements[1]));
    // cityList.add(city);
    // }
    //
    // if( cityList.size() > 0 ){
    // YellowPageTrainDB db = ContactsAppUtils.getInstance().getDatabaseHelper()
    // .getTrainDBHelper();
    // db.insertTongChengCityList(cityList);
    // }
    // } catch (MalformedURLException e) {
    // e.printStackTrace();
    // } catch (UnsupportedEncodingException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // } finally {
    // try {
    // if (null != br) {
    // br.close();
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    //
    // /**
    // * 加载同城火车票火车站的数据
    // * add by lisheng start 2014-11-24 19:09:25
    // */
    // public static void loadTrainTicketDB() {
    // InputStream in = null;
    // BufferedReader br = null;
    // String encoding = "utf-8";
    // List<TongChengCity> cityList = new ArrayList<TongChengCity>();
    // try {
    // in = ContactsApp.getInstance().getAssets()
    // .open("putao_train_ticket.txt");
    // br = new BufferedReader(new InputStreamReader(in, encoding));
    // String line = null;
    // while ((line = br.readLine()) != null) {
    // if (TextUtils.isEmpty(line)) {
    // continue;
    // }
    // String elements[] = line.split("\t");
    // if (elements == null || elements.length < 4) {
    // continue;
    // }
    // TongChengCity city = new TongChengCity();
    // city.setStationCode(elements[0]);
    // city.setStationName(elements[1]);
    // city.setQuanPin(elements[2]);
    // city.setJianPin(elements[3]);
    // city.setStationPY(PinyinHelper.getInstance().getFullPinyin(elements[1]));
    // cityList.add(city);
    // }
    //
    // if( cityList.size() > 0 ){
    // YellowPageTrainDB db = ContactsAppUtils.getInstance().getDatabaseHelper()
    // .getTrainDBHelper();
    // db.insertTongChengCityList(cityList);
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // } finally {
    // try {
    // if (br != null)
    // br.close();
    // if (in != null)
    // in.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    //
    // /**
    // * 从后台加载水电煤数据
    // * add by gwq 2015/3/7
    // *
    // * @param requestStr
    // */
    // public static void loadWaterElectricityGasDB(String requestStr) {
    // List<WaterElectricityGasBean> beanList = new
    // ArrayList<WaterElectricityGasBean>();
    //
    // BufferedReader br = null;
    // URL url = null;
    // try {
    // url = new URL(requestStr);
    // HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    // br = new BufferedReader(new
    // InputStreamReader(connection.getInputStream(), "utf-8"));
    //
    // String line = null;
    // boolean isFirstRead = true;
    // while ((line = br.readLine()) != null) {
    // if (TextUtils.isEmpty(line)) {
    // continue;
    // }
    // if (isFirstRead) {
    // isFirstRead = false;
    // continue;
    // }
    // String elements[] = line.split("\t");
    // if (elements == null || elements.length < 4) {
    // continue;
    // }
    // WaterElectricityGasBean bean = new WaterElectricityGasBean();
    // /**
    // * modified by wcy 2015-4-8 start
    // * for bug java.lang.ArrayIndexOutOfBoundsException: length=4; index=4
    // * old code: private static DatabaseHelper mDatabaseHelper = null;
    // bean.setProduct_id(elements[0]);
    // bean.setProvince(elements[1]);
    // bean.setCity(elements[2]);
    // bean.setCompany(elements[3]);
    // bean.setWeg_type(Integer.valueOf(elements[4]));
    // beanList.add(bean);
    // */
    // try {
    // bean.setProduct_id(elements[0]);
    // bean.setProvince(elements[1]);
    // bean.setCity(elements[2]);
    // bean.setCompany(elements[3]);
    // bean.setWeg_type(NumberUtil.parseIntSafe(elements[4]));
    // beanList.add(bean);
    // } catch (ArrayIndexOutOfBoundsException e) {
    // beanList.add(bean);
    // }
    // /** modified by wcy 2015-4-8 end */
    // }
    //
    // if (beanList.size() > 0) {
    // WaterElectricityGasDB db =
    // ContactsAppUtils.getInstance().getDatabaseHelper()
    // .getWaterElectricityGasDB();
    // db.insertWaterElectricityGasList(beanList);
    // }
    // } catch (MalformedURLException e) {
    // e.printStackTrace();
    // } catch (UnsupportedEncodingException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // } finally {
    // if (null != br) {
    // try {
    // br.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    // }
    //
    // /**
    // * 加载水电煤数据
    // * add by gwq at 2015-4-3
    // */
    //
    // public static void loadWaterElectricityGasDB() {
    // InputStream in = null;
    // BufferedReader br = null;
    // List<WaterElectricityGasBean> list = new
    // ArrayList<WaterElectricityGasBean>();
    // try {
    // in = ContactsApp.getInstance().getAssets().open("putao_weg_data.txt");
    // br = new BufferedReader(new InputStreamReader(in, "utf-8"));
    // String line = null;
    // boolean isFirstRead = true;
    // while ((line = br.readLine()) != null) {
    // if (TextUtils.isEmpty(line)) {
    // continue;
    // }
    // if (isFirstRead) {
    // isFirstRead = false;
    // continue;
    // }
    // String elements[] = line.split("\t");
    // if (elements == null || elements.length < 4) {
    // continue;
    // }
    // WaterElectricityGasBean bean = new WaterElectricityGasBean();
    // bean.setProduct_id(elements[0]);
    // bean.setProvince(elements[1]);
    // bean.setCity(elements[2]);
    // bean.setCompany(elements[3]);
    // bean.setWeg_type(NumberUtil.parseIntSafe(elements[4]));
    // list.add(bean);
    // }
    //
    // if (list.size() > 0) {
    // WaterElectricityGasDB db =
    // ContactsAppUtils.getInstance().getDatabaseHelper().getWaterElectricityGasDB();
    // db.insertWaterElectricityGasList(list);
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // } finally {
    // try{
    // if( in != null ){
    // in.close();
    // }
    // if( br != null ){
    // br.close();
    // }
    // }catch(Exception e){
    //
    // }
    // }
    // }
    //
    //
    //
    // /**
    // * 从后台加载电影数据
    // * <p/>
    // * add by gwq 2015/3/17
    // *
    // * @param requestStr
    // */
    // public static void loadMovieCityList(String requestStr) {
    // ArrayList<MovieCity> cityList = new ArrayList<MovieCity>();
    // BufferedReader br = null;
    // try {
    // URL url = new URL(requestStr);
    // HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    // br = new BufferedReader(new
    // InputStreamReader(connection.getInputStream(), "utf-8"));
    //
    // String line = null;
    // while ((line = br.readLine()) != null) {
    // if (line == null || "".equals(line)) {
    // continue;
    // }
    // String elements[] = line.split("\\|");
    // if (elements == null || elements.length < 2) {
    // continue;
    // }
    //
    // MovieCity movieCity = new MovieCity();
    // movieCity.setCityname(elements[0]);
    // movieCity.setCitycode(elements[1]);
    // cityList.add(movieCity);
    // }
    //
    // if (cityList.size() > 0) {
    // MovieDB db =
    // ContactsAppUtils.getInstance().getDatabaseHelper().getMovieDB();
    // db.insertMovieCity(cityList);
    // }
    // } catch (MalformedURLException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // } finally {
    // if (null != br) {
    // try {
    // br.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    // }
    //
    // /**
    // * 加载电影片数据
    // * add by hyl 2015-1-7
    // */
    // public static void loadMovieCityList() {
    // InputStream in = null;
    // BufferedReader br = null;
    // String encoding = "utf-8";
    // ArrayList<MovieCity> cityList = new ArrayList<MovieCity>();
    // try {
    // in = ContactsApp.getInstance().getAssets().open("putao_movie_city.txt");
    // br = new BufferedReader(new InputStreamReader(in, encoding));
    // String line = null;
    //
    // while ((line = br.readLine()) != null) {
    // if (line == null || "".equals(line)) {
    // continue;
    // }
    // String elements[] = line.split("\\|");
    // if (elements == null || elements.length < 2) {
    // continue;
    // }
    //
    // MovieCity movieCity = new MovieCity();
    // movieCity.setCityname(elements[0]);
    // movieCity.setCitycode(elements[1]);
    // cityList.add(movieCity);
    // }
    // if (cityList.size() > 0) {
    // MovieDB db =
    // ContactsAppUtils.getInstance().getDatabaseHelper().getMovieDB();
    // db.insertMovieCity(cityList);
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // } finally {
    // try {
    // if (br != null) {
    // br.close();
    // }
    // if (in != null) {
    // in.close();
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    //
    // /**
    // * 从后台加载城市列表
    // * add by gwq 2015/3/17
    // */
    //
    // public static void loadAllCityList(String requestStr) {
    // BufferedReader br = null;
    // ArrayList<CityBean> cityList = new ArrayList<CityBean>();
    //
    // try {
    // URL url = new URL(requestStr);
    // HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    // br = new BufferedReader(new
    // InputStreamReader(connection.getInputStream(), "utf-8"));
    // String line = null;
    // while ((line = br.readLine()) != null) {
    // if (line == null || "".equals(line)) {
    // continue;
    // }
    // String elements[] = line.split("\t");
    // if (elements == null || elements.length < 17) {
    // continue;
    // }
    //
    // CityBean cityBean = new CityBean();
    // cityBean.setCityName(elements[0]);
    // cityBean.setCityPy(elements[1]);
    // cityBean.setSelfId(NumberUtil.parseIntSafe(elements[2]));
    // cityBean.setParentId(NumberUtil.parseIntSafe(elements[3]));
    // cityBean.setCityType(NumberUtil.parseIntSafe(elements[4]));
    // cityBean.setDistrictCode(elements[5]);
    // cityBean.setCityHot(NumberUtil.parseIntSafe(elements[6]));
    //
    // cityBean.setWubaState(NumberUtil.parseIntSafe(elements[7]));
    // if (!TextUtils.isEmpty(elements[8])) {
    // cityBean.setWubaCode(elements[8]);
    // }
    //
    // cityBean.setElongState(NumberUtil.parseIntSafe(elements[9]));
    // if (!TextUtils.isEmpty(elements[10])) {
    // cityBean.setElongCode(elements[10]);
    // }
    //
    // cityBean.setTongchengState(NumberUtil.parseIntSafe(elements[11]));
    // if (!TextUtils.isEmpty(elements[12])) {
    // cityBean.setTongchengCode(elements[12]);
    // }
    //
    // cityBean.setGewaraState(NumberUtil.parseIntSafe(elements[13]));
    // if (!TextUtils.isEmpty(elements[14])) {
    // cityBean.setGewaraCode(elements[14]);
    // }
    //
    // cityBean.setGaodeState(NumberUtil.parseIntSafe(elements[15]));
    // if (!TextUtils.isEmpty(elements[16])) {
    // cityBean.setGaodeCode(elements[16]);
    // }
    //
    // cityList.add(cityBean);
    // }
    //
    // if( cityList.size() > 0 ){
    // CityListDB db =
    // ContactsAppUtils.getInstance().getDatabaseHelper().getCityListDB();
    // db.insertCityList(cityList);
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // } finally {
    // try {
    // if (br != null) {
    // br.close();
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    //
    //
    // }
    //
    // /**
    // * 读取所有 城市数据
    // * add zjh 2014-12-13
    // */
    // public static void loadAllCityList() {
    // InputStream in = null;
    // BufferedReader br = null;
    // String encoding = "utf-8";
    // ArrayList<CityBean> cityList = new ArrayList<CityBean>();
    // try {
    // in = ContactsApp.getInstance().getAssets().open("putao_citylist.txt");
    // br = new BufferedReader(new InputStreamReader(in, encoding));
    // String line = null;
    // while ((line = br.readLine()) != null) {
    // if (line == null || "".equals(line)) {
    // continue;
    // }
    // String elements[] = line.split("\t");
    // if (elements == null || elements.length < 17) {
    // continue;
    // }
    //
    // CityBean cityBean = new CityBean();
    // cityBean.setCityName(elements[0]);
    // cityBean.setCityPy(elements[1]);
    // cityBean.setSelfId(NumberUtil.parseIntSafe(elements[2]));
    // cityBean.setParentId(NumberUtil.parseIntSafe(elements[3]));
    // cityBean.setCityType(NumberUtil.parseIntSafe(elements[4]));
    // cityBean.setDistrictCode(elements[5]);
    // cityBean.setCityHot(NumberUtil.parseIntSafe(elements[6]));
    //
    // cityBean.setWubaState(NumberUtil.parseIntSafe(elements[7]));
    // if (!TextUtils.isEmpty(elements[8])) {
    // cityBean.setWubaCode(elements[8]);
    // }
    //
    // cityBean.setElongState(NumberUtil.parseIntSafe(elements[9]));
    // if (!TextUtils.isEmpty(elements[10])) {
    // cityBean.setElongCode(elements[10]);
    // }
    //
    // cityBean.setTongchengState(NumberUtil.parseIntSafe(elements[11]));
    // if (!TextUtils.isEmpty(elements[12])) {
    // cityBean.setTongchengCode(elements[12]);
    // }
    //
    // cityBean.setGewaraState(NumberUtil.parseIntSafe(elements[13]));
    // if (!TextUtils.isEmpty(elements[14])) {
    // cityBean.setGewaraCode(elements[14]);
    // }
    //
    // cityBean.setGaodeState(NumberUtil.parseIntSafe(elements[15]));
    // if (!TextUtils.isEmpty(elements[16])) {
    // cityBean.setGaodeCode(elements[16]);
    // }
    //
    // cityList.add(cityBean);
    // }
    //
    // if (cityList.size() > 0) {
    // ContactsAppUtils.getInstance().getDatabaseHelper().getCityListDB().insertCityList(cityList);
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // } finally {
    // try {
    // if (br != null) {
    // br.close();
    // }
    // if (in != null) {
    // in.close();
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    //
    // /**
    // * add by gwq on 2015-4-23
    // *
    // * 从后台加载违章城市数据
    // */
    //
    // public static void loadViolationCity(String requestStr) {
    // BufferedReader reader = null;
    // String encoding = "utf-8";
    // List<ViolationCityBean> list = new ArrayList<ViolationCityBean>();
    //
    // try {
    // URL url = new URL(requestStr);
    // HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    // reader = new BufferedReader(new
    // InputStreamReader(connection.getInputStream(), encoding));
    //
    // String line = null;
    // while ((line = reader.readLine()) != null) {
    // if(TextUtils.isEmpty(line)){
    // continue;
    // }
    // String elements[] = line.split("\\|");
    // if(elements == null || elements.length != 4) {
    // continue;
    // }
    //
    // ViolationCityBean bean = new ViolationCityBean();
    // bean.setProvince(elements[0]);
    // bean.setCityName(elements[1]);
    // bean.setPinyin(elements[2]);
    // bean.setCarProvince(elements[3]);
    //
    // list.add(bean);
    // }
    //
    // if(list.size() > 0) {
    // ContactsAppUtils.getInstance().getDatabaseHelper().getViolatioCityDB().insertCityList(list);
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // } finally {
    // try {
    // if(null != reader) {
    // reader.close();
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    //
    // }
    //
    // /**
    // * add by gwq on 2015-4-22
    // *
    // * 读取本地违章城市列表
    // */
    // public static void loadViolationCity() {
    // InputStream in = null;
    // BufferedReader reader = null;
    // List<ViolationCityBean> list = new ArrayList<ViolationCityBean>();
    //
    // try {
    // in =
    // ContactsApp.getInstance().getAssets().open("putao_violation_city.txt");
    // reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
    //
    // String line = null;
    // while ((line = reader.readLine()) != null) {
    // if(TextUtils.isEmpty(line)){
    // continue;
    // }
    // String elements[] = line.split("\\|");
    // if(elements == null || elements.length != 4) {
    // continue;
    // }
    //
    // ViolationCityBean bean = new ViolationCityBean();
    // bean.setProvince(elements[0]);
    // bean.setCityName(elements[1]);
    // bean.setPinyin(elements[2]);
    // bean.setCarProvince(elements[3]);
    //
    // list.add(bean);
    // }
    //
    // if(list.size() > 0) {
    // ContactsAppUtils.getInstance().getDatabaseHelper().getViolatioCityDB().insertCityList(list);
    // }
    //
    // }catch (IOException e) {
    // e.printStackTrace();
    // } finally {
    // try {
    // if(null != reader) reader.close();
    // if(null != in) in.close();
    // }catch (IOException e) {
    // e.printStackTrace();
    // }
    //
    // }
    // }
    //
    // /**
    // * 从后台加载电话号码归属地数据
    // * <p/>
    // * add by gwq 2015/3/7
    // */
    //
    // public static void loadTelAreaData(String requsetStr, Context context) {
    // InputStream in = null;
    // FileOutputStream out = null;
    // String fileName = "putao_telArea.sf";
    // try {
    // File file = new File(context.getFilesDir(), fileName);
    // if(file.exists()) {
    // file.delete();
    // }
    //
    // URL url = new URL(requsetStr);
    //
    // HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    // in = connection.getInputStream();
    //
    // File dbFile = new File(context.getFilesDir(), fileName);
    // out = new FileOutputStream(dbFile);
    //
    // byte[] buffer = new byte[1024];
    // int len = 0;
    // while ((len = in.read(buffer)) != -1) {
    // out.write(buffer, 0, len);
    // }
    // out.flush();
    // } catch (MalformedURLException e) {
    // e.printStackTrace();
    // } catch (UnsupportedEncodingException e) {
    // e.printStackTrace();
    // } catch (IOException e) {
    // e.printStackTrace();
    // } finally {
    // if (null != out) {
    // try {
    // out.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // if (null != in) {
    // try {
    // in.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    //
    // }
    //
    // /**
    // * add by gwq at 2015-4-3
    // * 从本地加载电话号码数据库
    // * @param context
    // */
    // public static void loadTelAreaData(Context context) {
    // String fileName = "putao_telArea.sf";
    // InputStream inStream = null;
    // FileOutputStream outStream = null;
    // try {
    // File dbFile = new File(context.getFilesDir(), fileName);
    // if(dbFile.exists()) {
    // return;
    // }
    // inStream = context.getResources().getAssets().open(fileName);
    // outStream = new FileOutputStream(dbFile);
    // byte[] buffer = new byte[1024];
    // int len = 0;
    // while ((len = inStream.read(buffer)) != -1) {
    // outStream.write(buffer, 0, len);
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // } finally {
    // try {
    // if(null != outStream)
    // outStream.close();
    // if(null != inStream)
    // inStream.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    //
    // /**
    // * 从本地加载电话归属地数据
    // * @param context
    // */
    //
    // public static void loadGpsLocation(final Context context) {
    // if (NetUtil.isNetworkAvailable(context)) {
    // new Thread(new Runnable() {
    // @Override
    // public void run() {
    // LBSServiceGaode.process_activate(context, new LBSServiceListener() {
    //
    // @Override
    // public void onLocationFailed() {
    // // TODO Auto-generated method stub
    // LBSServiceGaode.deactivate();
    //
    // }
    //
    // @Override
    // public void onLocationChanged(String city, double latitude,
    // double longitude, long time) {
    // if (!TextUtils.isEmpty(city)) {
    // LBSServiceGaode.deactivate();
    // if
    // (city.endsWith(ContactsApp.getInstance().getString(R.string.putao_common_city)))
    // {
    // city = city.substring(0, city.length() - 1);
    // }
    //
    // SharedPreferences pref = context.getSharedPreferences(
    // ConstantsParameter.YELLOW_PAGE_GPSLOCATION,
    // Context.MODE_MULTI_PROCESS);
    // Editor editor = pref.edit();
    // editor.putString(
    // ConstantsParameter.YELLOW_PAGE_GPSLOCATION_LATITUDE,
    // latitude + "");
    // editor.putString(
    // ConstantsParameter.YELLOW_PAGE_GPSLOCATION_LONGTITUDE,
    // longitude + "");
    // editor.putString(ConstantsParameter.YELLOW_PAGE_GPSLOCATION_CITY,
    // city + "");
    // editor.commit();
    //
    // } else {
    // onLocationFailed();
    // }
    // }
    // });
    // }
    // }).start();
    // }
    // }
    //
    // // add by wxy 2015-04-23
    // // 从后台加载机票城市数据
    // public static void loadFlightCityDB(String requestStr) {
    // List<FlightCity> beanList = new ArrayList<FlightCity>();
    //
    // BufferedReader br = null;
    // URL url = null;
    // try {
    // url = new URL(requestStr);
    // HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    // br = new BufferedReader(new
    // InputStreamReader(connection.getInputStream(), "utf-8"));
    //
    // String line = null;
    // while ((line = br.readLine()) != null) {
    // if (TextUtils.isEmpty(line)) {
    // continue;
    // }
    // String elements[] = line.split("\t");
    // if (elements == null || elements.length < 3) {
    // continue;
    // }
    // FlightCity bean = new FlightCity();
    // try {
    // bean.setName(elements[0]);
    // bean.setPinyin(elements[1]);
    // bean.setType(elements[2]);
    // beanList.add(bean);
    // } catch (ArrayIndexOutOfBoundsException e) {
    // beanList.add(bean);
    // }
    // }
    //
    // if (beanList.size() > 0) {
    // QunarFlightDB db = ContactsAppUtils.getInstance().getDatabaseHelper()
    // .getQunarFlightDB();
    // db.insertFlightCity(beanList);
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // } finally {
    // if (null != br) {
    // try {
    // br.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }
    // }
    //
    //
    // }
    //
    // /**
    // * 加载机票城市数据
    // * add by wxy at 2015-4-23
    // */
    //
    // public static void loadFlightCityDB() {
    // InputStream in = null;
    // BufferedReader br = null;
    // List<FlightCity> list = new ArrayList<FlightCity>();
    // try {
    // in = ContactsApp.getInstance().getAssets().open("putao_flight_city.txt");
    // br = new BufferedReader(new InputStreamReader(in, "utf-8"));
    // String line = null;
    // while ((line = br.readLine()) != null) {
    // if (TextUtils.isEmpty(line)) {
    // continue;
    // }
    // String elements[] = line.split("\t");
    // if (elements == null || elements.length < 3) {
    // continue;
    // }
    // FlightCity bean = new FlightCity();
    // bean.setName(elements[0]);
    // bean.setPinyin(elements[1]);
    // bean.setType(elements[2]);
    // list.add(bean);
    // }
    //
    // if (list.size() > 0) {
    // QunarFlightDB db =
    // ContactsAppUtils.getInstance().getDatabaseHelper().getQunarFlightDB();
    // db.insertFlightCity(list);
    // }
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // } finally {
    // if( br != null ){
    // try{
    // br.close();
    // }catch(Exception e){
    // }
    // }
    // if( in != null ){
    // try{
    // in.close();
    // }catch(Exception e){
    // }
    // }
    // }
    // }
    //
    // public static String loadLocalTextFileString(String path) {
    // File f = new File(path);
    // if (!f.exists()) {
    // return null;
    // }
    // InputStreamReader inputReader = null;
    // BufferedReader bufferReader = null;
    // StringBuffer strBuffer = new StringBuffer();
    // try {
    // InputStream inputStream = new FileInputStream(f);
    // inputReader = new InputStreamReader(inputStream);
    // bufferReader = new BufferedReader(inputReader);
    // String line = null;
    // while ((line = bufferReader.readLine()) != null) {
    // strBuffer.append(line);
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // } finally {
    // try {
    // if( inputReader != null ){
    // inputReader.close();
    // }
    // if( bufferReader != null ){
    // bufferReader.close();
    // }
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // }
    // return strBuffer.toString();
    // }
    //
    // //code moved by lxh
     /**
     * 获取与后台通信需要携带的Cookie参数，新老接口都要带上 added by cj 2015/02/10
     *
     * @return
     */
    public static String getCookieParamVal()
    {
        PTUser ptUsr = AccountManager.getInstance().getPtUser();

        /**
         * 增加每次请求上报的数据,版本,appid,渠道,open_token,机型,城市,经纬度 cookie中有城市中文,需要编码
         */
        // version, appid, channel, pt_token
        StringBuffer cookieBuf = new StringBuffer();
        cookieBuf.append("app_id=").append(SystemUtil.getAppid(ContactsApp.getInstance())).append(";channel=")
                .append(SystemUtil.getChannelNo(ContactsApp.getInstance())).append(";version=")
                .append(SystemUtil.getAppVersion(ContactsApp.getInstance())).append(";dev_no=")
                .append(SystemUtil.getDeviceId(ContactsApp.getInstance())).append(";band=")
                .append(SystemUtil.getMachine()).append(";city=")
                .append(URLEncoder.encode(LBSManager.getInstance().getCity())).append(";loc=")
                .append(String.valueOf(LBSManager.getInstance().getLatitude())).append(",")
                .append(String.valueOf(LBSManager.getInstance().getLongitude()));
        if (ptUsr != null)
        {
            cookieBuf.append(";pt_token=").append(ptUsr.getPt_token());
        }
        // 去掉换行符,以免Cookie字段value换行
        String cookie = cookieBuf.toString().replaceAll("\n", "");
        LogUtil.d(TAG, "cookie=" + cookie);
        return cookie;
    }
    //
    // /**
    // * 获取支付配置 数据版本
    // * @author zj
    // * @since 2015-5-13
    // * @param context
    // * @return
    // */
    // public static int getPaymentConfigVersion(Context context) {
    // SharedPreferences
    // sp=context.getSharedPreferences(ConstantsParameter.SHARED_PREFS_YELLOW_PAGE,
    // Context.MODE_MULTI_PROCESS);
    // return sp.getInt(P_PAYMENT_CONFIG_VERSION,0);
    // }
    //
    // /**
    // * 保存opconfig cms数据版本
    // * @author zj
    // * @since 2015-5-13
    // * @param context
    // * @param version
    // */
    // public static void setPaymentConfigVersion(Context context, int version)
    // {
    // SharedPreferences
    // sp=context.getSharedPreferences(ConstantsParameter.SHARED_PREFS_YELLOW_PAGE,
    // Context.MODE_MULTI_PROCESS);
    // sp.edit().putInt(P_PAYMENT_CONFIG_VERSION, version).commit();
    // }

}
