package so.contacts.hub.basefunction.net.manager;

import so.contacts.hub.basefunction.net.bean.BaseRequestData;

/**
 * ****************************************************************
 * 文件名称 : IPTHTTP.java
 * 作 者 :   Robin
 * 创建时间 : 2015-11-7 下午2:47:10
 * 文件描述 : 葡萄服务器请求接口,分为两种:1.get请求和post请求
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-7 1.00 初始版本
 *****************************************************************
 */
public interface IPTHTTP
{
    /**
     * 
     * 同步post请求,请求业务参数需要业务自行拼装为&name1=value1&name2=value2的形式
     * @param url
     * @param queryStr
     * @return
     * String
     */
   public String post(String url, String queryStr);
   
   /**
    * 
    * 同步post请求，请求参数封装好继承BaseRequestData即可
    * @param url
    * @param baseRequestData
    * @return
    * String
    */
   public String post(String url, BaseRequestData requestData);
   
   /**
    * 
    * 异步post请求,请求业务参数需要业务自行拼装为&name1=value1&name2=value2的形式
    * @param url
    * @param queryStr
    * @param cb
    * void
    */
   public void asynPost(String url,String queryStr, IResponse cb);
   
   /**
    * 
    * 异步post请求，请求参数封装下继承BaseRequestData即可
    * @param url
    * @param requestData
    * @param cb
    * void
    */
   public void asynPost(String url, BaseRequestData requestData, IResponse cb);
   
   /**
    * 
    * 同步get请求,请求业务参数需要业务自行拼装为&name1=value1&name2=value2的形式
    * @param url
    * @param queryStr
    * @return
    * String
    */
   public String get(String url, String queryStr);
   
   /**
    * 
    * 同步get请求，请求参数封装下继承BaseRequestData即可
    * @param url
    * @param requestData
    * @return
    * String
    */
   public String get(String url, BaseRequestData requestData);
   
   /**
    * 
    * 异步get请求，请求业务参数需要业务自行拼装为&name1=value1&name2=value2的形式
    * @param url
    * @param queryStr
    * @param cb
    * void
    */
   public void asynGet(String url, String queryStr, IResponse cb);
   
   /**
    * 
    * 异步get请求，请求参数封装下继承BaseRequestData即可
    * @param url
    * @param requestData
    * @param cb
    * void
    */
   public void asynGet(String url, BaseRequestData requestData, IResponse cb);
   
   /**
    * 
    * 设置请求头部信息
    * @param key
    * @param value
    * void
    */
   public void setDefaultHeader(String key,String value);
}
