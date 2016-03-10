package com.sh.owner.codetest_1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private HorizontalListView hlv;
    private Activity context;
    private ViewPager vp;
    public static final int sdk = android.os.Build.VERSION.SDK_INT;
    private ImageView[] array_IV_indicator;
    private GradientDrawable gradientDrawable_selected, gradientDrawable2;
    private TextView tv_itemname;
    private ArrayList<String> al_hlv;
    private LinearLayout lLayoutIndicator;
    private ArrayList<String> al_vp;
    private Button btn1, btn2, btn3;
    private LinearLayout ll_buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hlv = (HorizontalListView) findViewById(R.id.hlv_top);
        vp = (ViewPager) findViewById(R.id.vp);
        tv_itemname = (TextView) findViewById(R.id.tv_itemname);
        lLayoutIndicator = (LinearLayout) findViewById(R.id.lLayoutIndicator);
        ll_buttons = (LinearLayout) findViewById(R.id.ll_buttons);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        context = MainActivity.this;
        Populate_hlv();
        Populate_vp();

    }

    private void Populate_vp() {
        int size = context.getResources().getDimensionPixelSize(R.dimen.five);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
        layoutParams.setMargins(size, 0, size, 0);

        gradientDrawable_selected = new GradientDrawable();
        gradientDrawable_selected.setSize(size, size);
        gradientDrawable_selected.setShape(GradientDrawable.OVAL);
        gradientDrawable_selected.setColor(Color.RED);

        gradientDrawable2 = new GradientDrawable();
        gradientDrawable2.setSize(size, size);
        gradientDrawable2.setShape(GradientDrawable.OVAL);
        gradientDrawable2.setColor(Color.BLUE);

        array_IV_indicator = new ImageView[4];
        for (int i = 0; i < 4; i++) {
            array_IV_indicator[i] = new ImageView(context);
            array_IV_indicator[i].setLayoutParams(layoutParams);
            lLayoutIndicator.addView(array_IV_indicator[i]);
        }
        al_vp = new ArrayList<String>();
        al_vp.add("VP 1");
        al_vp.add("VP 2");
        al_vp.add("VP 3");
        al_vp.add("VP 4");
        Adapter_vp adapter_vp = new Adapter_vp(context, al_vp);
        vp.setAdapter(adapter_vp);

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                set_indicator(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    protected void set_indicator(int currentPage2) {
        if (sdk >= 16) {
            for (int i = 0; i < array_IV_indicator.length; i++) {
                array_IV_indicator[i].setBackground(gradientDrawable2);
            }
            array_IV_indicator[currentPage2].setBackground(gradientDrawable_selected);
        } else {
            for (int i = 0; i < array_IV_indicator.length; i++) {
                array_IV_indicator[i].setBackgroundDrawable(gradientDrawable2);
            }
            array_IV_indicator[currentPage2].setBackgroundDrawable(gradientDrawable_selected);
        }
    }

    private void Populate_hlv() {
        al_hlv = new ArrayList<String>();
        al_hlv.add("Item 1");
        al_hlv.add("Item 2");
        al_hlv.add("Item 3");
        al_hlv.add("Item 4");
        al_hlv.add("Item 5");

        ArrayAdapter<String> aa = new ArrayAdapter<String>(context, R.layout.listtextv, al_hlv);
        hlv.setAdapter(aa);
        hlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv_itemname.setText(al_hlv.get(position));
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                ll_buttons.setBackgroundColor(Color.RED);
                break;
            case R.id.btn2:
                ll_buttons.setBackgroundColor(Color.GREEN);
                break;
            case R.id.btn3:
                ll_buttons.setBackgroundColor(Color.BLUE);
                break;
        }
    }

    public class Adapter_vp extends PagerAdapter {
        private Context context;
        private LayoutInflater inflater;
        private ArrayList<String> arrList;

        public Adapter_vp(Context context, ArrayList<String> arrayList_WishList_Images) {
            this.context = context;
            this.arrList = arrayList_WishList_Images;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return arrList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((FrameLayout) object);
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            View convertView = inflater.inflate(R.layout.list_row_view_pager, container, false);
            ImageView iv_Image = (ImageView) convertView.findViewById(R.id.iv_View_Pager);
            iv_Image.setImageResource(R.drawable.ic_launcher);

            ((ViewPager) container).addView(convertView, 0);

            TextView tv_Offer_Name = (TextView) convertView.findViewById(R.id.tv_vp);
            tv_Offer_Name.setVisibility(View.VISIBLE);
            tv_Offer_Name.setText("" + (position + 1));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "" + (position + 1), Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((FrameLayout) object);
        }
    }
}
