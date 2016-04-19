package xiaoming.website.functionlib;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
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

    final private Context mContext;

    private HashMap<Integer, ArrayList<Object>> mData;
    private ArrayList<Integer> mIndex;
    private LinearLayout mLinearLayout;

    private int functionCount = 5;
    private int itemDimension;
    private int mBackGroundColor = Color.parseColor("#bb5894c3");

    final private int MARK_INVISIBLE = 1;
    final private int MARK_ADD = 1 << 1;
    final private int MARK_DELETE = 1 << 2;

    final private String TAG_ADD = "+";
    final private int POS_TITLE = 0;
    final private int POS_DRAWABLE = 1;
    final private int POS_MARK = 2;
    final private int POS_INTENT = 3;
    final private int POS_ENABLE = 4;

    private boolean isChooseMode = false;
    private boolean isDeleteMode = false;


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

    public void setData(ArrayList<ItemBean> data) {
        initData(data);
    }


    private void initView() {
        setWillNotDraw(false);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        itemDimension = Util.getScreenWidth(mContext) / functionCount;

        ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                itemDimension = Util.getScreenWidth(mContext) / functionCount;
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

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(itemDimension, itemDimension);
        lp.setMargins(Util.dp2px(mContext, 20), 0, Util.dp2px(mContext, 5), 0);
        lp.height = lp.height + itemDimension / 2;
        for (int i : mIndex) {
            String title = (String) mData.get(i).get(POS_TITLE);
            int drawableRes = (int) mData.get(i).get(POS_DRAWABLE);
            int markFlag = (int) mData.get(i).get(POS_MARK);
            Intent intent = (Intent) mData.get(i).get(POS_INTENT);
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
            addImageView(f, R.drawable.mark_add, true);
            mLinearLayout.addView(f, lp);
        }
        invalidate();
    }

    private void addMarkView(FrameLayout f, int markFlag) {
        ImageView markIv = new ImageView(mContext);
        LayoutParams lpMarkView = new LayoutParams(itemDimension / 4, itemDimension / 4);
        if (markFlag == MARK_DELETE || isDeleteMode) {
            markIv.setImageResource(R.drawable.mark_delet);
            lpMarkView.gravity = Gravity.TOP | Gravity.END;
            lpMarkView.topMargin = itemDimension / 6;
            lpMarkView.rightMargin = itemDimension / 6;
            markIv.setVisibility(VISIBLE);
        } else if (markFlag == MARK_INVISIBLE) {
            markIv.setVisibility(GONE);
        } else if (markFlag == MARK_ADD) {
            markIv.setImageResource(R.drawable.mark_select);
            lpMarkView.gravity = Gravity.BOTTOM | Gravity.END;
            lpMarkView.bottomMargin = itemDimension / 2;
            lpMarkView.rightMargin = itemDimension / 6;
            markIv.setVisibility(VISIBLE);
        }
        f.addView(markIv, lpMarkView);
    }

    private void addImageView(FrameLayout f, int drawableRes, boolean isAddFlag) {
        if (isAddFlag) {
            f.setTag(TAG_ADD);
            f.setOnClickListener(this);
        }
        ImageView iv = new ImageView(mContext);      //TODO:  bitmap optimis
        iv.setImageResource(drawableRes);
        iv.setMinimumWidth(itemDimension);
        iv.setMinimumHeight(itemDimension);
        LayoutParams lpImageView = new LayoutParams(itemDimension, itemDimension);
        lpImageView.gravity = Gravity.TOP;
        lpImageView.topMargin = Util.dp2px(mContext, 5);
        lpImageView.bottomMargin = Util.dp2px(mContext, 5);
        f.addView(iv, lpImageView);
    }

    private void addTextView(FrameLayout f, String title) {
        TextView tv = new TextView(mContext);
        tv.setText(title);
        tv.setTextSize(20);
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
        LayoutParams lpTextView = new LayoutParams(itemDimension, itemDimension);
        lpTextView.gravity = Gravity.BOTTOM;
        lpTextView.bottomMargin = Util.dp2px(mContext, 5);
        f.addView(tv, lpTextView);
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
        mLinearLayout.setMinimumWidth(Util.getScreenWidth(mContext));
        mLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        addView(mLinearLayout, layoutParamsV);
    }

    private void initData(ArrayList<ItemBean> data) {
        if (data == null) {
            return;
        }
        mData = new HashMap<>();
        mIndex = new ArrayList<>();
        functionCount = 0;
        int i = 0;
        for (ItemBean itemBean : data) {
            ArrayList<Object> tmpArrayList = new ArrayList<>();
            tmpArrayList.add(itemBean.getTitle());
            tmpArrayList.add(itemBean.getDrawableRes());
            tmpArrayList.add(MARK_INVISIBLE);
            tmpArrayList.add(itemBean.getIntent());
            tmpArrayList.add(itemBean.isEnable);
            functionCount += itemBean.isEnable ? 1 : 0;
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
            isChooseMode = true;
            initChildView(true, true);
        } else {
            int index = (int) v.getTag();
            if (isChooseMode) {
                if ((int) mData.get(index).get(POS_MARK) == MARK_INVISIBLE) {
                    mData.get(index).set(POS_MARK, MARK_ADD);
                } else {
                    mData.get(index).set(POS_MARK, MARK_INVISIBLE);
                }
                initChildView(true, false);
            } else if (isDeleteMode) {
                ArrayList<Object> itemBean = mData.get(index);
                itemBean.set(POS_ENABLE, false);
                itemBean.set(POS_MARK, MARK_INVISIBLE);
                functionCount--;
                initChildView(false, false);
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
        if (isDeleteMode) {
            isDeleteMode = false;
            delFunction();
        }
        initChildView(false, true);
    }

    private void delFunction() {
        for (int i : mIndex) {
            ArrayList<Object> itemBean = mData.get(i);
            if ((int) itemBean.get(POS_MARK) == MARK_DELETE) {
                itemBean.set(POS_MARK, MARK_INVISIBLE);
            }
        }
    }

    private void addFunction() {
        ArrayList<Integer> copyIndex = new ArrayList<>(mIndex);
        for (int i : copyIndex) {
            ArrayList<Object> itemBean = mData.get(i);
            if ((int) itemBean.get(POS_MARK) == MARK_ADD) {
                itemBean.set(POS_MARK, MARK_INVISIBLE);
                itemBean.set(POS_ENABLE, true);
                int index = mIndex.indexOf(i);
                mIndex.remove(index);
                mIndex.add(0, i);
                functionCount++;
            }
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (!isDeleteMode) {
            isDeleteMode = true;
            for (int i : mIndex) {
                mData.get(i).set(POS_MARK, MARK_DELETE);
            }
            initChildView(false, false);
            return true;
        }
        return false;
    }
}
