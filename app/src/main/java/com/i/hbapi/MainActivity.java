package com.i.hbapi;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.github.fujianlian.klinechart.DataHelper;
import com.github.fujianlian.klinechart.KLineChartAdapter;
import com.github.fujianlian.klinechart.KLineChartView;
import com.github.fujianlian.klinechart.KLineEntity;
import com.github.fujianlian.klinechart.draw.Status;
import com.github.fujianlian.klinechart.formatter.DateFormatter;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.kLineChartView)
    KLineChartView kLineChartView;
    @BindView(R.id.tl_time)
    TabLayout tlTime;
    @BindView(R.id.ll_more)
    LinearLayout llMore;
    @BindView(R.id.iv_boll)
    ImageView ivBoll;
    @BindView(R.id.tv_more)
    TextView tvMore;
    private KLineChartAdapter mAdapter;
    private ViewHolder mHolder;
    private PopupWindow mPopupWindow;
    private PopupWindow mBollWindow;
    private BollViewHolder mBollViewHolder;

    // 主图指标下标
    private int mainIndex = 0;
    // 副图指标下标
    private int subIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initKline();
        initData();

    }

    private void initKline() {
        mAdapter = new KLineChartAdapter();
        kLineChartView.setAdapter(mAdapter);
        kLineChartView.setDateTimeFormatter(new DateFormatter());
        kLineChartView.setGridRows(4);
        kLineChartView.setGridColumns(4);
    }


    private void initData() {
        tlTime.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.v("main", tab.getPosition() + "");
                switch (tab.getPosition()) {
                    case 0:
                        //getKline();
                        break;
                    case 1:
                        getKline("15min", "btcusdt");
                        break;
                    case 2:
                        getKline("30min", "btcusdt");
                        break;
                    case 3:
                        getKline("60min", "btcusdt");
                        break;
                    case 4:
                        getKline("1day", "btcusdt");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private boolean mNoFirst;

    private void getKline(final String period, final String symbol) {
        mNoFirst = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                doKline(period, symbol);
            }
        }, 0, 2000);

    }

    private void doKline(String period, String symbol) {
        Call<HttpResponse<List<KLineEntity>>> call = IApplication.apiService.getKline(period, 100, symbol);
        call.enqueue(new Callback<HttpResponse<List<KLineEntity>>>() {
            @Override
            public void onResponse(Call<HttpResponse<List<KLineEntity>>> call, Response<HttpResponse<List<KLineEntity>>> response) {
                List<KLineEntity> datas = response.body().getData();
                Collections.reverse(datas);
                if (!mNoFirst) {
                    kLineChartView.justShowLoading();
                }
                DataHelper.calculate(datas);
                mAdapter.addFooterData(datas);
                mAdapter.notifyDataSetChanged();
                if (!mNoFirst) {
                    kLineChartView.startAnimation();
                    kLineChartView.refreshEnd();
                }
                mNoFirst = true;


            }

            @Override
            public void onFailure(Call<HttpResponse<List<KLineEntity>>> call, Throwable t) {
                Log.v("getKline", t.toString());
            }
        });
    }

    @OnClick({R.id.ll_more, R.id.iv_boll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_more:
                View timeView = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_time, null, false);
                mPopupWindow = new PopupWindow(timeView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.showAsDropDown(llMore);
                mHolder = new ViewHolder();
                ButterKnife.bind(mHolder, timeView);
                Click click = new Click();
                mHolder.tv1m.setOnClickListener(click);
                mHolder.tv5m.setOnClickListener(click);
                mHolder.tv30m.setOnClickListener(click);
                mHolder.tvWeek.setOnClickListener(click);
                mHolder.tvMonth.setOnClickListener(click);

                break;
            case R.id.iv_boll:
                View bollView = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_boll, null, false);
                mBollWindow = new PopupWindow(bollView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                mBollWindow.setOutsideTouchable(true);
                mBollWindow.showAsDropDown(llMore);
                mBollViewHolder = new BollViewHolder();
                ButterKnife.bind(mBollViewHolder, bollView);
                BollClick bollClick = new BollClick();
                mBollViewHolder.tvMa.setOnClickListener(bollClick);
                mBollViewHolder.tvBoll.setOnClickListener(bollClick);

                mBollViewHolder.tvMacd.setOnClickListener(bollClick);
                mBollViewHolder.tvKdj.setOnClickListener(bollClick);
                mBollViewHolder.tvRsi.setOnClickListener(bollClick);
                mBollViewHolder.tvWr.setOnClickListener(bollClick);
                break;
        }
    }

    class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_1m:
                    tvMore.setText(mHolder.tv1m.getText());
                    getKline("1min", "btcusdt");
                    break;
                case R.id.tv_5m:
                    tvMore.setText(mHolder.tv5m.getText());
                    getKline("5min", "btcusdt");
                    break;
                case R.id.tv_30m:
                    tvMore.setText(mHolder.tv30m.getText());
                    getKline("30min", "btcusdt");
                    break;
                case R.id.tv_week:
                    tvMore.setText(mHolder.tvWeek.getText());
                    getKline("1week", "btcusdt");
                    break;
                case R.id.tv_month:
                    tvMore.setText(mHolder.tvMonth.getText());
                    getKline("1mon", "btcusdt");
                    break;
            }
            mPopupWindow.dismiss();
        }
    }

    class BollClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_ma:
                    if (mainIndex != 0) {
                        kLineChartView.hideSelectData();
                        mainIndex = 0;
                        mBollViewHolder.tvMa.setTextColor(Color.WHITE);
                        mBollViewHolder.tvBoll.setTextColor(getResources().getColor(R.color.hb_time));
                        kLineChartView.changeMainDrawType(Status.MA);
                    }
                    break;
                case R.id.tv_boll:
                    if (mainIndex != 1) {
                        kLineChartView.hideSelectData();
                        mainIndex = 1;
                        mBollViewHolder.tvBoll.setTextColor(Color.WHITE);
                        mBollViewHolder.tvMa.setTextColor(getResources().getColor(R.color.hb_time));
                        kLineChartView.changeMainDrawType(Status.BOLL);
                    }
                    break;
                case R.id.tv_macd:
                    break;
                case R.id.tv_kdj:
                    break;
                case R.id.tv_rsi:
                    break;
                case R.id.tv_wr:
                    break;
            }
            mPopupWindow.dismiss();
        }
    }

    static class ViewHolder {
        @BindView(R.id.tv_1m)
        TextView tv1m;
        @BindView(R.id.tv_5m)
        TextView tv5m;
        @BindView(R.id.tv_30m)
        TextView tv30m;
        @BindView(R.id.tv_week)
        TextView tvWeek;
        @BindView(R.id.tv_month)
        TextView tvMonth;
    }

    static class BollViewHolder {
        @BindView(R.id.tv_ma)
        TextView tvMa;
        @BindView(R.id.tv_boll)
        TextView tvBoll;
        @BindView(R.id.tv_macd)
        TextView tvMacd;
        @BindView(R.id.tv_kdj)
        TextView tvKdj;
        @BindView(R.id.tv_rsi)
        TextView tvRsi;
        @BindView(R.id.tv_wr)
        TextView tvWr;
    }
}
