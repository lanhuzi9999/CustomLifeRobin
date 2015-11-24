package so.contacts.hub.basefunction.operate.cms.util;

import so.contacts.hub.basefunction.config.Config;

public class CMSCommonConfig
{
    /*
     * secret为CMS系统与客户端的约定值
     */
    public static final String CMS_SECRET = "kksd%sj*77";
    
    /**
     * cms域名
     */
    public static final String CMS_SERVER_HOST = Config.NEW_CMS_SERVER_HOST;
    
    /**
     * cms内容商品流接口
     */
    public static final String CMS_URL_STREAMS = CMS_SERVER_HOST+"/view/streams";
    
}
