package so.contacts.hub.basefunction.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * ****************************************************************
 * 文件名称 : CustomGridView.java
 * 作 者 :   ffh
 * 创建时间 : 2015-7-11 下午3:07:22
 * 文件描述 : 重写OnMeasure用于解决ListView与GridView嵌套，GridView显示不全的问题
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有
 * 修改历史 : 2015-7-11 1.00 初始版本
 *****************************************************************
 */
public class ExpandGridView extends GridView {

	public ExpandGridView(Context context) {
		super(context);
	}

	public ExpandGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ExpandGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
