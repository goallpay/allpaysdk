package com.vtpayment.plugin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.allpayx.sdk.AllPayEngine;
import com.allpayx.sdk.constants.AllPayConst;
import com.allpayx.sdk.util.HttpUtil;

import com.vtpayment.plugin.util.Base64;
import com.vtpayment.plugin.util.JsonUtil;
import com.vtpayment.plugin.util.PayUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private static final String[] currency = {"CNY","USD","INR","JPY"};
    private static String payType[];
    private static final String[] paymentSchemas = {"AP","WX","UP","CA"};

    private ProgressDialog progressDialog;
    Map<String, String> params = new HashMap<String, String>();
    private EditText mAmoutEidt;
    private EditText mOrderNumEidt;
    private EditText mMerID;
    private Spinner mPaymentSchemaSpinner;
    private Spinner mCurrencySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        payType = new String[4];
        payType[0] = getResources().getString(R.string.pay_type_apmp);
        payType[1] = getResources().getString(R.string.pay_type_wx);
        payType[2] = getResources().getString(R.string.pay_type_up);
        payType[3] = getResources().getString(R.string.pay_type_ca);
        // 通用参数

        params.put("version", "VER000000005");
        params.put("charSet", "UTF-8");
        params.put("transType", "PURC");
        params.put("orderNum", getOrderNum());
        params.put("orderAmount", "0.1");
        params.put("orderCurrency", "CNY");
        params.put("frontURL", "nil");
        params.put("merReserve", "一个又大又圆的苹果");
        params.put("acqID", "99020344");
        params.put("backURL", "https://testapi.allpayx.com/test");
        params.put("merID", "600039253112226");
        params.put("paymentSchema", "AP");
        params.put("goodsInfo", "一个又大又圆的苹果");
        params.put("transTime", getTransTime());
        params.put("signType", "MD5");
        params.put("tradeFrom", "APP");

        String detailInfo = "[{\"goods_name\":\"Jovan 祖梵 白麝香女士古龙水 Cologne 96ml\",\"quantity\":1}]";
        try {
            detailInfo = Base64.encodeBytes(detailInfo.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        params.put("detailInfo", detailInfo);


        initLayout();
        initListener();
        update();

    }

    private void initLayout() {
        mAmoutEidt = (EditText) findViewById(R.id.edit_amount);
        mOrderNumEidt = (EditText) findViewById(R.id.edit_ordernum);
        mMerID = (EditText) findViewById(R.id.edit_merid);
        ArrayAdapter<String> currencyArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, currency);
        currencyArrayAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mCurrencySpinner = (Spinner) findViewById(R.id.spinner_currency);
        mCurrencySpinner.setAdapter(currencyArrayAdapter);

        ArrayAdapter<String> paymentSchemaArrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, payType);
        paymentSchemaArrayAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mPaymentSchemaSpinner = (Spinner) findViewById(R.id.spinner_paymentschema);
        mPaymentSchemaSpinner.setAdapter(paymentSchemaArrayAdapter);

    }

    private void initListener() {
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                params.put("orderAmount", mAmoutEidt.getText().toString());
                params.put("orderCurrency", mCurrencySpinner.getSelectedItem().toString());
                String paymentSchema = paymentSchemas[mPaymentSchemaSpinner.getSelectedItemPosition()];
                params.put("paymentSchema", paymentSchema);

                switch (paymentSchema) {

                }

                pay(params);
            }
        });
    }

    private void update() {
        params.put("orderNum", getOrderNum());
        mMerID.setText(params.get("merID"));
        mOrderNumEidt.setText(params.get("orderNum"));
        mAmoutEidt.setText(params.get("orderAmount"));
    }


    private void pay(final Map<String, String> params) {
        progressDialog = ProgressDialog.show(this, "", "模拟网关获取tn...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                params.remove("signature");
                params.put("signature", PayUtil.getSign(params, "rfb301ir902rhyvtpd8ubjox1kh2kmon", "MD5"));
                //String result = HttpUtil.reqStr("http://172.30.1.103:8080/app/pay", params, HttpUtil.METHOD_POST); //本地
                // String result = HttpUtil.reqStr("https://api.allpayx.com/app/pay", params, HttpUtil.METHOD_POST); //生产
                // final String result = HttpUtil.reqStr("http://172.30.1.112:8080/api/unifiedorder", params, HttpUtil.METHOD_POST);//测试

                final String result = HttpUtil.reqStr("https://testapi.allpayx.com/api/unifiedorder", params, HttpUtil.METHOD_POST); //生产

                progressDialog.dismiss();
                Log.i(TAG, "result=" + result);
                String tn = JsonUtil.getParam(result, "tn");
                if (!TextUtils.isEmpty(tn)) {

                    Log.i(TAG, "tn=" + tn);
                    AllPayEngine.pay(MainActivity.this, tn, false);
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast toast = Toast.makeText(
                                    getApplicationContext(), "获取tn失败:" + result,
                                    Toast.LENGTH_SHORT);
                            toast.show();

                        }
                    });
                }
            }
        }).start();

    }


    private String getOrderNum() {
        Date now = new Date();
        SimpleDateFormat spf = new SimpleDateFormat("yyyyMMddHHmmss");
        return "ap" + spf.format(now).substring(1);
    }


    private String getTransTime() {
        Date now = new Date();
        SimpleDateFormat spf = new SimpleDateFormat("yyyyMMddHHmmss");
        return spf.format(now);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data == null) {
            return;
        }

        if (requestCode == AllPayConst.VTPAY_PAY_REQUESR_CODE
                && resultCode == AllPayConst.VTPAY_PAY_RESULT_CODE) {

            update();
            String result = data.getExtras().getString("pay_result");
            Toast toast = Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_SHORT);
            toast.show();

        }

    }


}