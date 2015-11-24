
package so.contacts.hub.basefunction.utils;

import java.util.ArrayList;

import android.app.Activity;

/**
 * Activity管理类
 * 
 * @author lixiaohui
 */
public class ActivityMgr {

    private ArrayList<Activity> activities = new ArrayList<Activity>();

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    private static ActivityMgr sInstance = null;

    private ActivityMgr() {
    }

    public static ActivityMgr getInstance() {
        if (sInstance == null) {
            sInstance = new ActivityMgr();
        }
        return sInstance;
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activities.size(); i < size; i++) {
            Activity activity = activities.get(i);
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 结束所有Activity，除了指定name的Activity
     * 
     * @param name
     */
    public void finishAllActivityExcept(String name) {
        for (int i = 0, size = activities.size(); i < size; i++) {
            Activity activity = activities.get(i);
            if (activity != null && !activity.isFinishing()
                    && !activity.getClass().getName().equals(name)) {
                activity.finish();
            }
        }
    }

    /**
     * 结束所有Activity，除了指定name数组的Activity
     * 
     * @param names
     */
    public void finishAllActivityExcept(String... names) {
        firstTag: for (int i = 0, size = activities.size(); i < size; i++) {
            Activity activity = activities.get(i);
            if (activity == null || activity.isFinishing()) {
                continue;
            }
            for (int j = 0, len = names.length; j < len; j++) {
                if (activity.getClass().getName().equals(names[j])) {
                    continue firstTag;
                }
            }
            activity.finish();
        }
    }

    /**
     * 根据Activity的classname finish
     * 
     * @param name
     */
    public void finishActivity(String name) {
        Activity activity = getActivity(name);
        if (activity != null) {
            activity.finish();
        }
    }

    /**
     * 添加到集合中去
     * 
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (activity == null) {
            return;
        }
        activities.add(activity);
    }

    /**
     * 从集合中移除出去
     * 
     * @param activity
     */
    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    /**
     * 根据Activity的classname，从集合中找到相应的Activity。注：因为是通过classname查找的，
     * 所以不保证能找到想要的Activity实例对象
     * 
     * @param name
     * @return
     */
    public Activity getActivity(String name) {
        Activity activity = null;
        for (int i = 0, size = activities.size(); i < size; i++) {
            if (activities.get(i) != null && activities.get(i).getClass().getName().equals(name)) {
                activity = activities.get(i);
                break;
            }
        }
        return activity;
    }

    /**
     * 是否有指定的Activity 注：因为是通过classname查找的，所以不保证能找到想要的Activity实例对象
     * 
     * @param name Activity的className
     * @return
     */
    public boolean hasActivity(String name) {
        return getActivity(name) != null;
    }

    /**
     * 保证在activity stack里面只有一个singleTaskActivity的实例
     * 
     * @param singleTaskActivity
     */
    public void makesureSingleTask(Activity singleTaskActivity) {
        String className = singleTaskActivity.getClass().getName();
        for (int i = 0, size = activities.size(); i < size; i++) {
            Activity activityOld = activities.get(i);
            if (activityOld != null && activityOld.getClass().getName().equals(className)
                    && singleTaskActivity != activityOld) {
                activityOld.finish();
            }
        }
    }

}
