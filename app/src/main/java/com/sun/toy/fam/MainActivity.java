package com.sun.toy.fam;

import android.content.Intent;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.sun.toy.fam.adapter.AdapterRCVBase;
import com.sun.toy.fam.adapter.AdapterRcvSimple;
import com.sun.toy.fam.interfaces.IInitializer;
import com.sun.toy.fam.view.FloatingActionMenuLayout;
import com.sun.toy.fam.view.SimpleViewBinder;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ArrayList<String> mList;
    ArrayList<Pair<String, Integer>> listMenu = new ArrayList<>();
    private RecyclerView rcv;
    private FloatingActionMenuLayout fabMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    @Override
    public void initData() {
        super.initData();
        mList = new ArrayList<String>();

        mList.add("a");
        mList.add("b");
        mList.add("c");
        mList.add("d");
        mList.add("e");
        mList.add("f");
        mList.add("g");
        mList.add("h");
        mList.add("i");
        mList.add("j");
        mList.add("k");
        mList.add("l");
        mList.add("m");
        mList.add("n");
        mList.add("o");
        mList.add("p");
        mList.add("q");
        mList.add("r");
        mList.add("s");
        mList.add("t");
        mList.add("u");
        mList.add("v");
        mList.add("w");
        mList.add("x");
        mList.add("y");
        mList.add("z");


        listMenu.add(new Pair<>("Write", R.drawable.ic_create_white_24dp));
        listMenu.add(new Pair<>("Title", R.drawable.ic_format_size_white_24dp));
        listMenu.add(new Pair<>("Photo", R.drawable.ic_insert_photo_white_24dp));
        listMenu.add(new Pair<>("Location", R.drawable.ic_place_white_24dp));
        listMenu.add(new Pair<>("Move", R.drawable.ic_directions_run_white_24dp));
    }

    @Override
    public void initView() {
        super.initView();
        rcv = (RecyclerView) findViewById(R.id.rcv);
        fabMenu = (FloatingActionMenuLayout) findViewById(R.id.fab_menu);
        final SimpleViewBinder.RecyclerViewBuilder builder = new SimpleViewBinder.RecyclerViewBuilder(getWindow());
        builder.setAdapter(new AdapterRcvSimple(R.layout.item_rcv_simple), getSupportFragmentManager());
        builder.setList(mList);
        builder.setOnItemClickListener(new AdapterRCVBase.OnRCVItemListener() {
            @Override
            public void onItemClick(View view, int position) {
                int pos = rcv.getChildLayoutPosition(view);
                Object item = builder.getAdapter().getItem(pos);
            }
        });
        rcv = builder.build();
        fabMenu.addMenuList(listMenu);
        fabMenu.setOnClickListener(this);
        fabMenu.addScrollView(rcv);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, (String) v.getTag(), Toast.LENGTH_SHORT).show();
    }
}
