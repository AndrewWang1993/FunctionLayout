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
    RecentLayout rl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl);
        assert mRelativeLayout != null;
        mRelativeLayout.setOnTouchListener(this);
        rl = (RecentLayout) findViewById(R.id.recentlayout);
        assert rl != null;
        rl.setData(initData());
    }

    private ArrayList<ItemBean> initData() {
        Intent intent = new Intent(this, TestActivity.class);

        ArrayList<ItemBean> data = new ArrayList<>();

        data.add(new ItemBean("故事", R.drawable.icon_children, intent, true));
        data.add(new ItemBean("相声", R.drawable.icon_crosstalk, intent, true));
        data.add(new ItemBean("跳舞", R.drawable.icon_dance, intent, true));
        data.add(new ItemBean("游戏", R.drawable.icon_game, intent, true));
        data.add(new ItemBean("音乐", R.drawable.icon_music, intent, true));
        data.add(new ItemBean("戏曲", R.drawable.icon_opera, intent, false));
        data.add(new ItemBean("财经", R.drawable.icon_stock, intent, false));

        return data;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        rl.exitModify();
        return false;
    }
}
