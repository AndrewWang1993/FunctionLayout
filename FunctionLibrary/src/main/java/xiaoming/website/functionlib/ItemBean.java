package xiaoming.website.functionlib;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangxiaoming on 2016/4/18 0018,19:24.
 */
public class ItemBean implements Parcelable {
    String title;
    int drawableRes;
    Intent intent;
    boolean isEnable;

    /**
     * Item bean construct
     *
     * @param title       title name
     * @param drawableRes picture icon
     * @param intent      jump intent
     * @param isEnable    whether initial show
     */
    public ItemBean(String title, int drawableRes, Intent intent, boolean isEnable) {
        this.title = title;
        this.drawableRes = drawableRes;
        this.intent = intent;
        this.isEnable = isEnable;
    }

    protected ItemBean(Parcel in) {
        title = in.readString();
        drawableRes = in.readInt();
        intent = in.readParcelable(Intent.class.getClassLoader());
        isEnable = in.readByte() != 0;
    }

    public static final Creator<ItemBean> CREATOR = new Creator<ItemBean>() {
        @Override
        public ItemBean createFromParcel(Parcel in) {
            return new ItemBean(in);
        }

        @Override
        public ItemBean[] newArray(int size) {
            return new ItemBean[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(drawableRes);
        dest.writeParcelable(intent, flags);
        dest.writeByte((byte) (isEnable ? 1 : 0));
    }
}
