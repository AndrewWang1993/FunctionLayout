package xiaoming.website.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;

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
        mRecentLayout.setData(initData());
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
}
