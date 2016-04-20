package xiaoming.website.functionlib;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * TODO: document your custom view class.
 */
public class RecentLayout extends HorizontalScrollView implements View.OnClickListener, View.OnLongClickListener {

    final private Context mContext;


    private HashMap<Integer, ArrayList<Object>> mData;
    private ArrayList<Integer> mIndex;
    private LinearLayout mLinearLayout;

    private int mItemDimension;
    private int mViewDimension;
    private int mMarkDimension;

    private int mMargin;
    private int mMarginLeft;
    private int mMarginRight;

    private int mTitleBottonMargin;


    private int mFontSize;

    private int mBackGroundColor = Color.parseColor("#bb5894c3");

    final private String MARK_INVISIBLE = "MARK_INVISIBLE";
    final private String MARK_ADD = "MARK_ADD";
    final private String MARK_DELETE = "MARK_DELETE";

    final private int FIX_FUNCTION_COUNT = 5;
    final private String TAG_ADD = "+";
    final private int POS_TITLE = 0;
    final private int POS_DRAWABLE = 1;
    final private int POS_MARK = 2;
    final private int POS_INTENT = 3;
    final private int POS_ENABLE = 4;

    private boolean mIsChooseMode = false;
    private boolean mIsDeleteMode = false;


    public RecentLayout(Context context) {
        this(context, null);
    }

    public RecentLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecentLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView();
    }

    public HashMap<Integer, ArrayList<Object>> getData() {
        return mData;
    }

    public ArrayList<Integer> getSortIndex() {
        return mIndex;
    }

    public void setData(HashMap<Integer, ArrayList<Object>> data, ArrayList<Integer> index) {
        if (data == null && index != null) {
            return;
        }
        mData = data;
        mIndex = index;
        initChildView(false, true);
    }

    public void setRawData(ArrayList<ItemBean> data) {
        initRawData(data);
    }

    public int getBackGroundColor() {
        return mBackGroundColor;
    }

    public void setBackGroundColor(int backGroundColor) {
        mBackGroundColor = backGroundColor;
        invalidate();
    }


    private void initView() {
        setWillNotDraw(false);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        mItemDimension = Util.getScreenWidth(mContext) / FIX_FUNCTION_COUNT;
        mMarkDimension = mItemDimension / 4;
        mViewDimension = mMarkDimension * 3;
        mMargin = mItemDimension / 6;
        mMarginLeft = mItemDimension / 4;
        mMarginRight = mMarginLeft / 4;
        mFontSize = 18;
        mTitleBottonMargin = Util.dp2px(mContext, 5);

        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mItemDimension = Util.getScreenWidth(mContext) / FIX_FUNCTION_COUNT;
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        initMainView();
    }

    private void initChildView(boolean showHide, boolean showAdd) {
        if (mData == null || mData.size() == 0) {
            return;
        }

        mLinearLayout.removeAllViews();

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mItemDimension, mItemDimension);
        lp.setMargins(mMarginLeft, 0, mMarginRight, 0);
        lp.height = lp.height + mMargin + mMargin;
        for (int i : mIndex) {
            String title = (String) mData.get(i).get(POS_TITLE);
            int drawableRes = Integer.valueOf((String) mData.get(i).get(POS_DRAWABLE));
            String markFlag = (String) mData.get(i).get(POS_MARK);
            boolean isEnable = (boolean) mData.get(i).get(POS_ENABLE);

            FrameLayout f = new FrameLayout(mContext);
            f.setTag(i);
            f.setOnClickListener(this);
            f.setOnLongClickListener(this);

            if (isEnable || showHide) {
                if (showHide && isEnable) {
                    continue;
                }
                addImageView(f, drawableRes, false);
                addTextView(f, title);
                addMarkView(f, markFlag);
                mLinearLayout.addView(f, lp);
            }
        }

        if (!showHide && showAdd) {
            FrameLayout f = new FrameLayout(mContext);
            addImageView(f, R.drawable.icon_add, true);
            mLinearLayout.addView(f, lp);
        }
        invalidate();
    }

    private void addMarkView(FrameLayout f, String markFlag) {
        ImageView markIv = new ImageView(mContext);
        LayoutParams lpMarkView = new LayoutParams(mMarkDimension, mMarkDimension);
        if (markFlag.equals(MARK_DELETE) || mIsDeleteMode) {
//            new Util.BitmapWorkerTask(markIv, mMarkDimension, mMarkDimension).execute(R.drawable.mark_delet);   // will blink drawable
            markIv.setImageDrawable(getResources().getDrawable(R.drawable.mark_delet));   //  will not refresh drawable will cause memory leak
            lpMarkView.gravity = Gravity.TOP | Gravity.END;
            lpMarkView.topMargin = mMargin;
            lpMarkView.rightMargin = mMargin;
            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(markIv, "rotation", -15F, 15F);
            objectAnimator.setDuration(80);
            objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            objectAnimator.setRepeatCount(-1);
            objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
            objectAnimator.start();
            markIv.setVisibility(VISIBLE);
        } else if (markFlag.equals(MARK_INVISIBLE)) {
            markIv.setVisibility(GONE);
        } else if (markFlag.equals(MARK_ADD)) {
//            new Util.BitmapWorkerTask(markIv, mMarkDimension, mMarkDimension).execute(R.drawable.mark_select);  // will blink drawable
            markIv.setImageDrawable(getResources().getDrawable(R.drawable.mark_select));     //  will not refresh drawable will cause memory leak
            lpMarkView.gravity = Gravity.BOTTOM | Gravity.END;
            lpMarkView.bottomMargin = mMarkDimension + mTitleBottonMargin;
            lpMarkView.rightMargin = mMargin;
            markIv.setVisibility(VISIBLE);
        }
        f.addView(markIv, lpMarkView);
    }

    private void addImageView(FrameLayout f, int drawableRes, boolean isAddFlag) {
        if (isAddFlag) {
            f.setTag(TAG_ADD);
            f.setOnClickListener(this);
        }
        ImageView iv = new ImageView(mContext);
//        new Util.BitmapWorkerTask(iv, mViewDimension, mViewDimension).execute(drawableRes); // will blink drawable
        iv.setImageDrawable(getResources().getDrawable(drawableRes));   //  will not refresh drawable will cause memory leak
        iv.setMinimumWidth(mItemDimension);
        iv.setMinimumHeight(mItemDimension);
        LayoutParams lpImageView = new LayoutParams(mViewDimension, mViewDimension);
        lpImageView.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        lpImageView.topMargin = mMargin;
        f.addView(iv, lpImageView);
    }

    private void addTextView(FrameLayout f, String title) {
        TextView tv = new TextView(mContext);
        tv.setText(title);
        tv.setTextSize(mFontSize);
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        LayoutParams lpTextView = new LayoutParams(mItemDimension, mItemDimension);
        lpTextView.gravity = Gravity.BOTTOM;
        lpTextView.bottomMargin = mTitleBottonMargin;
        f.addView(tv, lpTextView);
    }


    private void initMainView() {
        ViewGroup.LayoutParams layoutParamsH = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParamsH);

        setHorizontalScrollBarEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);

        mLinearLayout = new LinearLayout(mContext);
        ViewGroup.LayoutParams layoutParamsV = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLinearLayout.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        mLinearLayout.setBackgroundColor(mBackGroundColor);
        mLinearLayout.setMinimumWidth(Util.getScreenWidth(mContext));
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        addView(mLinearLayout, layoutParamsV);
    }

    private void initRawData(ArrayList<ItemBean> data) {
        if (data == null) {
            return;
        }
        mData = new HashMap<>();
        mIndex = new ArrayList<>();
        int i = 0;
        for (ItemBean itemBean : data) {
            ArrayList<Object> tmpArrayList = new ArrayList<>();
            tmpArrayList.add(itemBean.getTitle());
            tmpArrayList.add(String.valueOf(itemBean.getDrawableRes()));
            tmpArrayList.add(String.valueOf(MARK_INVISIBLE));
            tmpArrayList.add(itemBean.getIntent().toUri(0));
            tmpArrayList.add(itemBean.isEnable);
            mData.put(i, tmpArrayList);
            mIndex.add(i++);
        }
        initChildView(false, true);
    }


    @Override
    public void onClick(View v) {
        if (v.getTag() == TAG_ADD) {
            boolean canAdd = true;
            for (int i : mIndex) {
                ArrayList<Object> itemBean = mData.get(i);
                canAdd = canAdd && (boolean) itemBean.get(POS_ENABLE);
            }
            if (canAdd) {
                Toast.makeText(mContext, "所有功能都已添加", Toast.LENGTH_SHORT).show();
                return;
            }
            mIsChooseMode = true;
            initChildView(true, true);
        } else {
            int index = (int) v.getTag();
            if (mIsChooseMode) {
                if ((mData.get(index).get(POS_MARK)).equals(MARK_INVISIBLE)) {
                    mData.get(index).set(POS_MARK, String.valueOf(MARK_ADD));
                } else {
                    mData.get(index).set(POS_MARK, String.valueOf(MARK_INVISIBLE));
                }
                initChildView(true, false);
            } else if (mIsDeleteMode) {
                if (isAllItemHide()) {
                    Toast.makeText(mContext, "最少保留一个功能", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<Object> itemBean = mData.get(index);
                    itemBean.set(POS_ENABLE, false);
                    itemBean.set(POS_MARK, MARK_INVISIBLE);
                }
                initChildView(false, false);
            } else {
                try {
                    Intent intent = Intent.parseUri((String) mData.get(index).get(POS_INTENT), 0);
                    mContext.startActivity(intent);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void exitModify() {
        if (mIsChooseMode) {
            mIsChooseMode = false;
            addFunction();
        }
        if (mIsDeleteMode) {
            mIsDeleteMode = false;
            delFunction();
        }
        initChildView(false, true);
    }

    private void delFunction() {
        for (int i : mIndex) {
            ArrayList<Object> itemBean = mData.get(i);
            if (itemBean.get(POS_MARK).equals(MARK_DELETE)) {
                itemBean.set(POS_MARK, MARK_INVISIBLE);
            }
        }
    }

    private void addFunction() {
        ArrayList<Integer> copyIndex = new ArrayList<>(mIndex);
        for (int i : copyIndex) {
            ArrayList<Object> itemBean = mData.get(i);
            if (itemBean.get(POS_MARK).equals(MARK_ADD)) {
                itemBean.set(POS_MARK, MARK_INVISIBLE);
                itemBean.set(POS_ENABLE, true);
                int index = mIndex.indexOf(i);
                mIndex.remove(index);
                mIndex.add(0, i);
            }
        }
    }

    private boolean isAllItemHide() {
        int result = 0;
        for (int i : mIndex) {
            ArrayList<Object> itemBean = mData.get(i);
            result += (boolean) itemBean.get(POS_ENABLE) ? 1 : 0;
        }
        return result <= 1;
    }


    @Override
    public boolean onLongClick(View v) {
        if (!mIsDeleteMode && !mIsChooseMode) {
            mIsDeleteMode = true;
            for (int i : mIndex) {
                mData.get(i).set(POS_MARK, MARK_DELETE);
            }
            initChildView(false, false);
            return true;
        }
        return false;
    }

    public int getmFontSize() {
        return mFontSize;
    }

    public void setmFontSize(int mFontSize) {
        this.mFontSize = mFontSize;
        invalidate();
    }
}
