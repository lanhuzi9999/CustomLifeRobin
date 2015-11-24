
package so.contacts.hub.basefunction.operate.cms.bean;

import java.io.Serializable;

/**
 * 精选页分类(商品分类)
 * @author zj
 * @since 2015-6-6
 */
public class ChoicenesCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 未选中时的图片   网络图片url/本地图片名称 */
    public String img_url;
    
    /** 选中时的图片  网络图片url/本地图片名称 */
    public String img_url_d;

    /** 选中时的背景颜色  例:fffeab77*/
    public String background_color;

    /** 分类名称 */
    public String category_name; 
    
    /** 分类id */
    public String category_id;

    @Override
    public String toString() {
        return "GoodsCategory [img_url=" + img_url + ", categoryName=" + category_name
                + ", categoryId=" + category_id + "]";
    }

}
