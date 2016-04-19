package xiaoming.website.functionlib;

import android.content.Intent;

import java.io.Serializable;

/**
 * Created by wangxiaoming on 2016/4/18 0018,19:24.
 */
public class ItemBean implements Serializable {
    String title;
    int drawableRes;
    Intent intent;
    boolean isEnable;

    /**
     * Item bean construct
     * @param title
     * @param drawableRes
     * @param intent
     * @param isEnable
     */
    public ItemBean(String title, int drawableRes, Intent intent, boolean isEnable) {
        this.title = title;
        this.drawableRes = drawableRes;
        this.intent = intent;
        this.isEnable = isEnable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDrawableRes() {
        return drawableRes;
    }

    public void setDrawableRes(int drawableRes) {
        this.drawableRes = drawableRes;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    @Override
    public String toString() {
        return "ItemBean{" +
                "title='" + title + '\'' +
                ", drawableRes=" + drawableRes +
                ", intent=" + intent +
                ", isEnable=" + isEnable +
                '}';
    }
}
