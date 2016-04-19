package xiaoming.website.functionlib;


import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * TODO: document your custom view class.
 */
public class RecentLayout extends HorizontalScrollView implements View.OnClickListener, View.OnLongClickListener {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;


    private HashMap<Integer, ArrayList<Object>> mData;
    private ArrayList<Integer> mIndex;
    private int functionCount = 5;
    private int itemDimension;
    private final Context mContext;
    private LinearLayout mLinearLayout;
    private int mBackGroundColor = Color.parseColor("#5894c3");

    private final int MARK_INVISIBLE = 1;
    private final int MARK_ADD = 1 << 1;
    private final int MARK_DELETE = 1 << 2;
    private final String TAG_ADD = "+";

    private final int POS_TITLE = 0;
    private final int POS_DRAWABLE = 1;
    private final int POS_MARK = 2;
    private final int POS_INTENT = 3;
    private final int POS_ENABLE = 4;

    private boolean isChooseMode = false;
    private boolean isDeletMode = false;


    public RecentLayout(Context context) {
        this(context, null);
    }

    public RecentLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecentLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initAttar(attrs, defStyle);
        initView();
    }

    public void setData(ArrayList<ItemBean> data) {
        initData(data);
        initChildView(false);
    }


    private void initView() {
        setWillNotDraw(false);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        itemDimension = getScreenWidth(mContext) / functionCount;

        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                itemDimension = getScreenWidth(mContext) / functionCount;
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        initMainView();
    }

    private void initChildView(boolean showHide) {
        if (mData == null || mData.size() == 0) {
            return;
        }
        mLinearLayout.removeAllViews();
        int tmp = 0;
        for (int i = 0; i < mIndex.size(); i++) {
            i = mIndex.get(i);

            FrameLayout f = new FrameLayout(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(itemDimension, itemDimension);
            lp.setMargins(dp2px(mContext, 20), 0, dp2px(mContext, 5), 0);
            lp.height = lp.height + itemDimension / 2;

            String title = (String) mData.get(i).get(POS_TITLE);  // TODO:  merge to class
            int drawableRes = (int) mData.get(i).get(POS_DRAWABLE);
            int markFlag = (int) mData.get(i).get(POS_MARK);
            Intent intent = (Intent) mData.get(i).get(POS_INTENT);
            boolean isEnable = (boolean) mData.get(i).get(POS_ENABLE);

            f.setTag(i);
            f.setOnClickListener(this);
            f.setOnLongClickListener(this);


            if (isEnable || showHide) {

                if (showHide && isEnable) {
                    continue;
                }

                TextView tv = new TextView(mContext);
                tv.setText(title);
                tv.setTextSize(20);                              // TODO: const
                tv.setTextColor(Color.WHITE);
                tv.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                LayoutParams lpTextView = new LayoutParams(itemDimension, itemDimension);
                lpTextView.gravity = Gravity.BOTTOM;
                lpTextView.bottomMargin = dp2px(mContext, 5);
                f.addView(tv, lpTextView);


                ImageView iv = new ImageView(mContext);      //TODO:  bitmap optimis
                iv.setImageResource(drawableRes);
                iv.setMinimumWidth(itemDimension);
                iv.setMinimumHeight(itemDimension);
                LayoutParams lpImageView = new LayoutParams(itemDimension, itemDimension);
                lpImageView.gravity = Gravity.TOP;
                lpImageView.topMargin = dp2px(mContext, 5);
                lpImageView.bottomMargin = dp2px(mContext, 5);
                f.addView(iv, lpImageView);


                ImageView markIv = new ImageView(mContext);      //TODO:  bitmap optimis
                LayoutParams lpMarkView = new LayoutParams(itemDimension / 4, itemDimension / 4);
                if (markFlag == MARK_DELETE || isDeletMode) {                                  // TODO: const
                    markIv.setVisibility(VISIBLE);
                    markIv.setImageResource(R.drawable.mark_delet);
                    lpMarkView.gravity = Gravity.TOP | Gravity.END;
                    lpMarkView.topMargin = itemDimension / 6;
                    lpMarkView.rightMargin = itemDimension / 6;
                } else if (markFlag == MARK_INVISIBLE) {
                    markIv.setVisibility(GONE);
                } else if (markFlag == MARK_ADD) {
                    markIv.setVisibility(VISIBLE);
                    markIv.setImageResource(R.drawable.mark_select);
                    lpMarkView.gravity = Gravity.BOTTOM | Gravity.END;
                    lpMarkView.bottomMargin = itemDimension / 2;
                    lpMarkView.rightMargin = itemDimension / 6;
                }
                f.addView(markIv, lpMarkView);


                mLinearLayout.addView(f, lp);
                tmp++;
            }
        }

        FrameLayout f = new FrameLayout(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(itemDimension, itemDimension);
        lp.setMargins(dp2px(mContext, 20), 0, dp2px(mContext, 5), 0);
        lp.height = lp.height + itemDimension / 2;
        Log.i("peoce", tmp + "  " + functionCount + " " + showHide);
        if (!showHide) {
            ImageView iv = new ImageView(mContext);      //TODO:  bitmap optimis
            iv.setImageResource(R.drawable.mark_add);
            iv.setMinimumWidth(itemDimension);
            iv.setMinimumHeight(itemDimension);
            LayoutParams lpImageView = new LayoutParams(itemDimension, itemDimension);
            lpImageView.gravity = Gravity.TOP;
            lpImageView.topMargin = dp2px(mContext, 5);
            lpImageView.bottomMargin = dp2px(mContext, 5);
            f.setTag(TAG_ADD);
            f.addView(iv, lpImageView);
            f.setOnClickListener(this);
            mLinearLayout.addView(f, lp);
        }

        invalidate();
    }

    private void initMainView() {
        ViewGroup.LayoutParams layoutParamsH = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParamsH);

        setHorizontalScrollBarEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);

        mLinearLayout = new LinearLayout(mContext);
        ViewGroup.LayoutParams layoutParamsV = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLinearLayout.setGravity(Gravity.START);
        mLinearLayout.setBackgroundColor(mBackGroundColor);
        mLinearLayout.setMinimumWidth(getScreenWidth(mContext));
        mLinearLayout.setAlpha(0.8f);
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        addView(mLinearLayout, layoutParamsV);
    }

    private void initAttar(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.RecentLayout, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.RecentLayout_exampleString);
        mExampleColor = a.getColor(
                R.styleable.RecentLayout_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.RecentLayout_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.RecentLayout_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.RecentLayout_exampleDrawable);
        }

        a.recycle();
    }


    private void initData(ArrayList<ItemBean> data) {
        if (data == null) {
            return;
        }
        int dataSize = data.size();
        mData = new HashMap<>();
        mIndex = new ArrayList<Integer>();
        functionCount = 0;
        int i = 0;
        for (ItemBean itemBean : data) {
            ArrayList<Object> tmpArrayList = new ArrayList<>();
            tmpArrayList.add(itemBean.getTitle());
            tmpArrayList.add(itemBean.getDrawableRes());
            tmpArrayList.add(MARK_INVISIBLE);            //  delete add normal
            tmpArrayList.add(itemBean.getIntent());
            tmpArrayList.add(itemBean.isEnable);  // is enable
            if (itemBean.isEnable) {
                functionCount++;
            }
            mData.put(i, tmpArrayList);
            mIndex.add(i++);
        }
    }

    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        return point.x;
    }

    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
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


            isChooseMode = true;


            initChildView(true);
        } else {
            int index = (int) v.getTag();
            if (isChooseMode) {
                if ((int) mData.get(index).get(POS_MARK) == MARK_INVISIBLE) {
                    mData.get(index).set(POS_MARK, MARK_ADD);
                } else {
                    mData.get(index).set(POS_MARK, MARK_INVISIBLE);
                }
                initChildView(true);
            } else if (isDeletMode) {
                mData.get(index).set(POS_ENABLE, false);
                mData.get(index).set(POS_MARK, MARK_INVISIBLE);
                functionCount--;
                initChildView(false);
            } else {
                mContext.startActivity((Intent) mData.get(index).get(POS_INTENT));
            }
        }
    }

    public void exitModify() {
        if (isChooseMode) {
            isChooseMode = false;
            addFunction();
        }
        if (isDeletMode) {
            isDeletMode = false;
            delFunction();
        }
        initChildView(false);
    }

    private void delFunction() {
        for (int i : mIndex) {
            ArrayList<Object> itemBean = mData.get(i);
            if ((int) itemBean.get(POS_MARK) == MARK_DELETE) {
                itemBean.set(POS_MARK, MARK_INVISIBLE);
                mData.put(i, itemBean);
            }
        }
    }

    private void addFunction() {
        for (int i : mIndex) {
            ArrayList<Object> itemBean = mData.get(i);
            if ((int) itemBean.get(POS_MARK) == MARK_ADD) {
                itemBean.set(POS_MARK, MARK_INVISIBLE);
                itemBean.set(POS_ENABLE, true);
                mData.put(i, itemBean);
                functionCount++;
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (!isDeletMode) {
            isDeletMode = true;
            for (int i : mIndex) {
                mData.get(i).set(POS_MARK, MARK_DELETE);
            }
            initChildView(false);
            return true;
        }
        return false;
    }
}
