package com.lives.depend.theme.switchbutton;

import com.lives.depend.theme.ThemeController;
import android.os.Build;
import android.view.View;
import android.view.ViewStub;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

/**
 * ****************************************************************<br>
 * 文件名称 : SwitchThemeController.java<br>
 * 作 者 : zjh<br>
 * 创建时间 : 2015-11-10 下午12:07:19<br>
 * 文件描述 : Switch主题控制器<br>
 * 版权声明 : 深圳市葡萄信息技术有限公司 版权所有<br>
 * 修改历史 : 2015-11-10 1.00 初始版本<br>
 ***************************************************************** 
 */
public class SwitchThemeController
{

    /**
     * Switch默认的样式
     */
    private Switch mSwitch = null;

    /**
     * Switch自定义样式
     */
    private SwitchButton mSwitchButton = null;

    public SwitchThemeController(View view, int defaultSwitchId, int customSwitchId)
    {
        if (view == null || defaultSwitchId <= 0 || customSwitchId <= 0)
        {
            throw new IllegalArgumentException("You want to use Switch, but view or id is not valid.");
        }
        if (ThemeController.isDefaultTheme())
        {
            if (Build.VERSION.SDK_INT >= 14)
            {// 4.0及以上可以使用Switch
                ViewStub viewStub = (ViewStub) view.findViewById(defaultSwitchId);
                mSwitch = (Switch) viewStub.inflate();
            }
            else
            {
                ViewStub viewStub = (ViewStub) view.findViewById(customSwitchId);
                mSwitchButton = (SwitchButton) viewStub.inflate();
            }
        }
        else
        {
            ViewStub viewStub = (ViewStub) view.findViewById(customSwitchId);
            mSwitchButton = (SwitchButton) viewStub.inflate();
        }
    }

    public void setChecked(boolean checked)
    {
        if (ThemeController.isDefaultTheme())
        {
            if (mSwitch != null)
            {
                mSwitch.setChecked(checked);
            }
            else
            {
                if (mSwitchButton != null)
                {
                    mSwitchButton.setChecked(checked);
                }
            }
        }
        else
        {
            if (mSwitchButton != null)
            {
                mSwitchButton.setChecked(checked);
            }
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener)
    {
        if (ThemeController.isDefaultTheme())
        {
            if (mSwitch != null)
            {
                mSwitch.setOnCheckedChangeListener(onCheckedChangeListener);
            }
            else
            {
                if (mSwitchButton != null)
                {
                    mSwitchButton.setOnCheckedChangeListener(onCheckedChangeListener);
                }
            }
        }
        else
        {
            if (mSwitchButton != null)
            {
                mSwitchButton.setOnCheckedChangeListener(onCheckedChangeListener);
            }
        }
    }

    public void setCheckedWithOutAnimation(boolean checked)
    {
        if (ThemeController.isDefaultTheme())
        {
            if (mSwitch != null)
            {
                mSwitch.setChecked(checked);
            }
            else
            {
                if (mSwitchButton != null)
                {
                    mSwitchButton.setCheckedWithOutAnimation(checked);
                }
            }
        }
        else
        {
            if (mSwitchButton != null)
            {
                mSwitchButton.setCheckedWithOutAnimation(checked);
            }
        }
    }

}
