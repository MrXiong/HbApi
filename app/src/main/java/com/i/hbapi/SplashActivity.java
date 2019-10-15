package com.i.hbapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.btn_transaction)
    Button btnTransaction;
    @BindView(R.id.btn_wallet)
    Button btnWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_transaction, R.id.btn_wallet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_transaction:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.btn_wallet:
                startActivity(new Intent(this, WalletActivity.class));
                break;
        }
    }
}
