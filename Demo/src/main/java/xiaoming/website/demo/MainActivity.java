package xiaoming.website.demo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;

import xiaoming.website.functionlib.ItemBean;
import xiaoming.website.functionlib.RecentLayout;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    RelativeLayout mRelativeLayout = null;
    RecentLayout mRecentLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        mRelativeLayout.setOnTouchListener(this);
        mRecentLayout = (RecentLayout) findViewById(R.id.recentlayout);
    }

    @Override
    protected void onResume() {
        HashMap<Integer, ArrayList<Object>> map = restoreRecentFunction();
        ArrayList<Integer> sortIndex = restoreFunctionSort();
        Log.i("peoce", "restore   --->" + map.toString());
        if (map != null && sortIndex != null) {
            mRecentLayout.setData(map, sortIndex);
        } else {
            mRecentLayout.setRawData(initData());
        }
        super.onResume();
    }

    private ArrayList<ItemBean> initData() {
        Intent intent = new Intent(this, TestActivity.class);
        ArrayList<ItemBean> data = new ArrayList<>();

        Intent intent1 = (Intent) intent.clone();
        intent1.putExtra(TestActivity.KEY, "故事");
        data.add(new ItemBean("故事", R.drawable.icon_children, intent1, true));

        Intent intent2 = (Intent) intent.clone();
        intent2.putExtra(TestActivity.KEY, "相声");
        data.add(new ItemBean("相声", R.drawable.icon_crosstalk, intent2, true));

        Intent intent3 = (Intent) intent.clone();
        intent3.putExtra(TestActivity.KEY, "跳舞");
        data.add(new ItemBean("跳舞", R.drawable.icon_dance, intent3, true));

        Intent intent4 = (Intent) intent.clone();
        intent4.putExtra(TestActivity.KEY, "游戏");
        data.add(new ItemBean("游戏", R.drawable.icon_game, intent4, true));

        Intent intent5 = (Intent) intent.clone();
        intent5.putExtra(TestActivity.KEY, "音乐");
        data.add(new ItemBean("音乐", R.drawable.icon_music, intent5, true));

        Intent intent6 = (Intent) intent.clone();
        intent6.putExtra(TestActivity.KEY, "戏曲");
        data.add(new ItemBean("戏曲", R.drawable.icon_opera, intent6, false));

        Intent intent7 = (Intent) intent.clone();
        intent7.putExtra(TestActivity.KEY, "财经");
        data.add(new ItemBean("财经", R.drawable.icon_stock, intent7, false));

        return data;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mRecentLayout.exitModify();
        return false;
    }

    @Override
    protected void onPause() {
        saveRecentFunctionAndSort(mRecentLayout.getData(), mRecentLayout.getSortIndex());
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        saveRecentFunctionAndSort(mRecentLayout.getData(), mRecentLayout.getSortIndex());
        super.onDestroy();
    }

    private void saveRecentFunctionAndSort(HashMap<Integer, ArrayList<Object>> data, ArrayList<Integer> sortIndex) {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        String index = gson.toJson(sortIndex);
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("tag", json);
        editor.putString("index", index);
        editor.apply();
    }


    private HashMap<Integer, ArrayList<Object>> restoreRecentFunction() {
        Gson gson = new Gson();
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        String json = sp.getString("tag", "null");
        if (json != null) {
            return gson.fromJson(json, new TypeToken<HashMap<Integer, ArrayList<Object>>>() {
            }.getType());
        }
        return null;
    }

    private ArrayList<Integer> restoreFunctionSort() {
        Gson gson = new Gson();
        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        String json = sp.getString("index", "null");
        if (json != null) {
            return gson.fromJson(json, new TypeToken<ArrayList<Integer>>() {
            }.getType());
        }
        return null;
    }

}
