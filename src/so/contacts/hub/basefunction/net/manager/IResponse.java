package so.contacts.hub.basefunction.net.manager;

/**
 * ****************************************************************
 * 文件名称 : IResponse.java
 * 作 者 :   Robin
 * 创建时间 : 2015-11-7 下午3:02:42
 * 文件描述 : 请求相应接口
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-7 1.00 初始版本
 *****************************************************************
 */
public interface IResponse
{
    /**
     * 
     * 请求成功
     * 
     * @param content void
     */
    public void onSuccess(String content);

    /**
     * 
     * 请求失败
     * 
     * @param errorCode void
     */
    public void onFail(int errorCode);
}
