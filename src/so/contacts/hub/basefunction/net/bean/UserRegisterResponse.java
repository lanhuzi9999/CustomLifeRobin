package so.contacts.hub.basefunction.net.bean;

import java.util.List;


/**
 * ****************************************************************
 * 文件名称 : UserRegisterResponse.java
 * 作 者 :   Robin
 * 创建时间 : 2015-11-25 上午12:54:32
 * 文件描述 : 鉴权凭证接口 Response
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-11-25 1.00 初始版本
 *****************************************************************
 */
public class UserRegisterResponse extends BaseResponseData {

    public int registerStatus;//[帐户切换状态，0表正常，1表不能切换(已绑定凭证用户尝试绑定到另一个用户上)]
    public String pt_uid;//[用户id]
    public String pt_token;//[用户登录标识]
    public String open_token;//[open token] 
    public List<RelateUser> relateUsers;//[凭证列表]
    
}
