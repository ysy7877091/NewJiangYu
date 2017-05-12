package mytext.administrator.example.com.jiangyujiangce;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Calendar;

import static mytext.administrator.example.com.jiangyujiangce.R.id.tv_endTime;
import static mytext.administrator.example.com.jiangyujiangce.R.id.tv_startTime;

/**
 * Created by 王聿鹏 on 2017/5/10.
 * <p>
 * 描述 ：历史查询
 */

public class LiShi_ChaXun extends AppCompatActivity implements View.OnClickListener{

    private String Start_year = "";
    private String Start_monthOfYear = "";
    private String Start_dayOfMonth = "";
    private String Start = "";
    private String end = "";
    private String sub_StartTime = "";
    private String sub_EndTime = "";
    private ImageView iv_fanhui_icon;
    private ImageView iv_lishi_icon;
    private TextView tv_endTime1;
    private TextView tv_startTime1;
    private ImageView iv_sanJiao;
    private ImageView iv_souSuo;
    private TextView tv_quyu;

    private String ID;
    private String val;
    private MyProgressDialog ProgressDialog;
    private String[] arr;//泵站ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.lishijilu_layout);

        CommonMethod.setStatuColor(LiShi_ChaXun.this,R.color.white);
        init();

    }
    private void init() {

        iv_fanhui_icon = (ImageView) findViewById(R.id.iv_fanhui_icon);
        tv_startTime1 = (TextView) findViewById(tv_startTime);
        tv_endTime1 = (TextView) findViewById(tv_endTime);
        iv_sanJiao = (ImageView) findViewById(R.id.iv_sanJiao);
        iv_souSuo = (ImageView) findViewById(R.id.iv_souSuo);
        tv_quyu = (TextView) findViewById(R.id.tv_quyu);

        iv_fanhui_icon.setOnClickListener(this);

        tv_startTime1.setOnClickListener(this);
        tv_endTime1.setOnClickListener(this);
        iv_sanJiao.setOnClickListener(this);
        iv_souSuo.setOnClickListener(this);
        tv_quyu.setOnClickListener(this);

    }

    private void SelectStartTime(final int state) {

        AlertDialog.Builder builder = new AlertDialog.Builder(LiShi_ChaXun.this);
        if (state == 0) {
            builder.setTitle("请选择开始时间");
        } else if (state == 1) {
            builder.setTitle("请选择结束时间");
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.date_layout, null);
        builder.setView(view);
        DatePicker dp = (DatePicker) view.findViewById(R.id.dp);
        Calendar c = Calendar.getInstance();
        int Now_year = c.get(Calendar.YEAR);
        int Now_monthOfYear = c.get(Calendar.MONTH);
        int Now_dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        //选择默认弹出的当前时间 及 datepicker未改变的时间
        Start_year = String.valueOf(Now_year);
        Start_monthOfYear = String.valueOf(Now_monthOfYear + 1);
        Start_dayOfMonth = String.valueOf(Now_dayOfMonth);
        //初始化年月日
        dp.init(Now_year, Now_monthOfYear, Now_dayOfMonth, new DatePicker.OnDateChangedListener() {
            //改变后的时间 时间改变后才执行这个方法
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Start_year = String.valueOf(year);
                Start_monthOfYear = String.valueOf(monthOfYear + 1);
                Start_dayOfMonth = String.valueOf(dayOfMonth);
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //当选择默认当前时间时Start_year Start_monthOfYear Start_dayOfMonth 都为null;

                if (state == 0) {

                        Start = Start_year + "年" + " " + Start_monthOfYear + "月" + Start_dayOfMonth + "日";
                        sub_StartTime = Start_year + "-" + Start_monthOfYear + "-" + Start_dayOfMonth;//提交到服务器上的时间
                        tv_startTime1.setText(Start_year + "年" + " " + Start_monthOfYear + "月" + Start_dayOfMonth + "日");
                        tv_startTime1.setTextSize(18);
                        SpannableStringBuilder spanBuilder = new SpannableStringBuilder(Start_year + "年" + " " + Start_monthOfYear + "月" + Start_dayOfMonth + "日");
                        spanBuilder.setSpan(new TextAppearanceSpan(null, 0, 30, null, null), 0, 5, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        tv_startTime1.setText(spanBuilder);
                        tv_startTime1.setTextColor(getResources().getColor(R.color.black));


                } else if (state == 1) {

                        end = Start_year + "年" + "" + Start_monthOfYear + "月" + Start_dayOfMonth + "日";
                        sub_EndTime = Start_year + "-" + Start_monthOfYear + "-" + Start_dayOfMonth;//提交到服务器上的时间
                        tv_endTime1.setText(Start_year + "年" + " " + Start_monthOfYear + "月" + Start_dayOfMonth + "日");
                        tv_endTime1.setTextSize(18);
                        SpannableStringBuilder spanBuilder = new SpannableStringBuilder(Start_year + "年" + " " + Start_monthOfYear + "月" + Start_dayOfMonth + "日");
                        spanBuilder.setSpan(new TextAppearanceSpan(null, 0, 30, null, null), 0, 5, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                        tv_endTime1.setText(spanBuilder);
                        tv_endTime1.setTextColor(getResources().getColor(R.color.black));

                } else {
                    Toast.makeText(getApplicationContext(), "应用程序错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    private void SelectQuYu() {
        if(arr_Name!=null&&arr_Name.length>1){
            selectDialog();
            return;
        }
        ProgressDialog = new MyProgressDialog(LiShi_ChaXun.this,false,"加载中...");
        new Thread(networkGetYuLiangInfor).start();
    }
    Runnable networkGetYuLiangInfor = new Runnable() {
        @Override
        public void run() {
            try {
                // 命名空间
                String nameSpace = "http://tempuri.org/";
                // 调用的方法名称
                String methodName = "Get_RainStationName_List";
                // EndPoint
                String endPoint = "http://beidoujieshou.sytxmap.com:5963/GPSService.asmx";
                // SOAP Action
                String soapAction = "http://tempuri.org/Get_RainStationName_List";
                // 指定WebService的命名空间和调用的方法名
                SoapObject rpc = new SoapObject(nameSpace, methodName);
                // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(rpc);

                HttpTransportSE ht = new HttpTransportSE(endPoint,20000);
                ht.debug = true;
                Log.e("warn", "50");
                try {
                    // 调用WebService
                    ht.call(soapAction, envelope);
                } catch (Exception e) {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    handlerGetYuLiangList.sendMessage(msg);
                }
                SoapObject object;
                // 开始调用远程方法
                object = (SoapObject) envelope.getResponse();
                // 得到服务器传回的数据 返回的数据时集合 每一个count是一个及集合的对象
                int count1 = object.getPropertyCount();
                if (count1 > 0) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < count1; i++) {
                        SoapObject soapProvince = (SoapObject) object.getProperty(i);

                        sb.append(soapProvince.getProperty("ID").toString() + ",");
                        if (i == count1 - 1) {
                            sb.append(soapProvince.getProperty("NAME").toString());
                        } else {
                            sb.append(soapProvince.getProperty("NAME").toString() + "|");
                        }
                    }
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = sb.toString();
                    handlerGetYuLiangList.sendMessage(msg);
                }
            } catch (Exception e) {
                Message msg = Message.obtain();
                msg.what = 0;
                handlerGetYuLiangList.sendMessage(msg);
            }
        }
    };
    private String arr_Name[];
    private String arr_ID[] ;
    Handler handlerGetYuLiangList = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String val = (String) msg.obj;
            Log.e("warn", val);
            if (val.toString().equals("0")) {
                cancelDialg();
                Toast.makeText(getApplicationContext(), "获取雨量信息失败,网络或者服务器异常", Toast.LENGTH_SHORT).show();
            } else {
                cancelDialg();
                String[] objects = val.split("\\|");
                arr_Name = new String[objects.length+1];
                arr_ID = new String[objects.length+1];
                for (int i = 0; i < objects.length; i++) {
                    if (objects[i].length() > 0) {
                        String[] values = objects[i].split(",");
                        if (values.length > 1) {
                            arr_Name[i+1] = values[1].toString();
                            arr_ID[i+1] = values[0].toString();
                        }
                    }
                }
                arr_Name[0] = "全区";
                arr_ID[0]="";
                selectDialog();
            }
        }
    };
    private void selectDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LiShi_ChaXun.this);
        builder.setTitle("请选择");
        builder.setSingleChoiceItems(arr_Name, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String val = arr_Name[i];
                tv_quyu.setText(val);
                tv_quyu.setTextColor(getResources().getColor(R.color.black));
                ID=arr_ID[i];
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    private void cancelDialg(){
        if(ProgressDialog!=null){
            ProgressDialog.dismiss();
            ProgressDialog=null;
        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_fanhui_icon:finish();break;
            case R.id.tv_quyu:
                SelectQuYu();
                break;


            case R.id.iv_sanJiao:
                SelectQuYu();
                break;
            case tv_startTime:
                SelectStartTime(0);
                break;
            case tv_endTime:
                SelectStartTime(1);
                break;
            case R.id.iv_souSuo:
                String str = tv_quyu.getText().toString().trim();
                String startTime = tv_startTime1.getText().toString().trim();
                String endTime = tv_endTime1.getText().toString().trim();

                if (str.equals("请选择")) {
                    Toast.makeText(LiShi_ChaXun.this, "请选择区域", Toast.LENGTH_SHORT).show();
                } else {
                    if (startTime.equals("选择开始日期") || endTime.equals("选择结束日期")) {
                        Toast.makeText(getApplicationContext(), "请选择日期", Toast.LENGTH_SHORT).show();
                    } else {

                        Intent intent = new Intent(LiShi_ChaXun.this, QuYu_YuLiang.class);

                        intent.putExtra("Name", tv_quyu.getText().toString().trim());
                        intent.putExtra("StartTime", sub_StartTime);
                        intent.putExtra("EndTime", sub_EndTime);
                        intent.putExtra("ID", ID);

                        startActivity(intent);
                    }
                }
                break;
        }
    }
}