package mytext.administrator.example.com.jiangyujiangce;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import java.util.ArrayList;
import java.util.List;
import javabeen.cn.JiangYuShuJu;
import javabeen.cn.StringTemplate;

/**
 * Created by 王聿鹏 on 2017/5/10.
 * <p>
 * 描述 ：历史查询
 */

public class QuYu_YuLiang extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_quYu_name;

    private ImageView iv_fanhui_icon1;
    private String startTime;
    private String endTime;
    private HorizontalBarChart barChart;
    private MyProgressDialog progressDialog = null;
    private List<JiangYuShuJu> list = new ArrayList<>();
    private String quYu_name;
    private boolean isMore100=false;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.quanqu_yuliang);
        init();
    }

    private void init() {

        barChart = (HorizontalBarChart) findViewById(R.id.YLchart);
        iv_fanhui_icon1 = (ImageView) findViewById(R.id.iv_fanhui_icon1);
        iv_fanhui_icon1.setOnClickListener(this);
        tv_quYu_name = (TextView) findViewById(R.id.tv_quYu_Name);

        startTime = getIntent().getStringExtra("StartTime");
        endTime = getIntent().getStringExtra("EndTime");
        quYu_name = getIntent().getStringExtra("Name");
        id = getIntent().getStringExtra("ID");
        tv_quYu_name.setText(quYu_name);

        if (quYu_name.equals("全区")) {
            progressDialog = new MyProgressDialog(QuYu_YuLiang.this, false, "正在加载中...");
            new Thread(Get_CheckAllRainFallHistory_List).start();
        }else {
            progressDialog = new MyProgressDialog(QuYu_YuLiang.this, false, "正在加载中...");
            new Thread(Get_CheckRainFallHistory_List).start();
        }
    }

    Runnable Get_CheckRainFallHistory_List = new Runnable() {
        @Override
        public void run() {
            try {
                // 命名空间
                String nameSpace = "http://tempuri.org/";
                // 调用的方法名称
                String methodName = "Get_CheckRainFallHistory_List";
                // EndPoint
                String endPoint = "http://beidoujieshou.sytxmap.com:5963/GPSService.asmx";
                // SOAP Action
                String soapAction = "http://tempuri.org/Get_CheckRainFallHistory_List";
                // 指定WebService的命名空间和调用的方法名
                SoapObject rpc = new SoapObject(nameSpace, methodName);

                rpc.addProperty("id",id);
                rpc.addProperty("startTime", startTime);
                Log.e("warn", startTime);
                rpc.addProperty("endTime", endTime);
                Log.e("warn", endTime);
                // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(rpc);
                HttpTransportSE ht = new HttpTransportSE(endPoint, 10000);
                ht.debug = true;
                Log.e("warn", "4444");
                try {
                    // 调用WebService
                    ht.call(soapAction, envelope);
                } catch (Exception e) {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    handlerGet_CheckRainFallHistory_List.sendMessage(msg);
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
                        //SoapObject soapProvince = (SoapObject) envelope.bodyIn;
                        sb.append(soapProvince.getProperty("TIME").toString() + ",");
                        if (i == count1 - 1) {
                            sb.append(soapProvince.getProperty("ValueX").toString());
                        } else {
                            sb.append(soapProvince.getProperty("ValueX").toString() + "|");
                        }
                    }
                    Log.e("warn", sb.toString());
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = sb.toString();
                    handlerGet_CheckRainFallHistory_List.sendMessage(msg);
                }
            } catch (Exception e) {
                Message msg = Message.obtain();
                msg.what = 0;
                handlerGet_CheckRainFallHistory_List.sendMessage(msg);
            }
        }
    };
    Handler handlerGet_CheckRainFallHistory_List = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int i = msg.what;
            if (i == 0) {
                progressDialog.dismiss();
                Toast.makeText(QuYu_YuLiang.this, "网络或服务器异常", Toast.LENGTH_SHORT).show();
            } else if (i == 1) {
                progressDialog.dismiss();
                String str = (String) msg.obj;
                Log.e("warn", str);
                String[] objects = str.split("\\|");
                for (int j = 0; j < objects.length; j++) {
                    if (objects[j].length() > 0) {
                        String[] values = objects[j].split(",");
                        JiangYuShuJu jy = new JiangYuShuJu();
                        jy.setTIME(values[0]);
                        jy.setValueX(values[1]);
                        list.add(jy);
                    }
                }
                showData();
            }
        }
    };
    Runnable Get_CheckAllRainFallHistory_List = new Runnable() {
        @Override
        public void run() {
            try {
                // 命名空间
                String nameSpace = "http://tempuri.org/";
                // 调用的方法名称
                String methodName = "Get_CheckAllRainFallHistory_List";
                // EndPoint
                String endPoint = "http://beidoujieshou.sytxmap.com:5963/GPSService.asmx";
                // SOAP Action
                String soapAction = "http://tempuri.org/Get_CheckAllRainFallHistory_List";
                // 指定WebService的命名空间和调用的方法名
                SoapObject rpc = new SoapObject(nameSpace, methodName);

                rpc.addProperty("startTime", startTime);
                Log.e("warn", startTime);
                rpc.addProperty("endTime", endTime);
                Log.e("warn", endTime);
                // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(rpc);
                HttpTransportSE ht = new HttpTransportSE(endPoint, 10000);
                ht.debug = true;
                Log.e("warn", "4444");
                try {
                    // 调用WebService
                    ht.call(soapAction, envelope);
                } catch (Exception e) {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    handlerGet_CheckAllRainFallHistory_List.sendMessage(msg);
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
                        //SoapObject soapProvince = (SoapObject) envelope.bodyIn;
                        sb.append(soapProvince.getProperty("TIME").toString() + ",");
                        if (i == count1 - 1) {
                            sb.append(soapProvince.getProperty("ValueX").toString());
                        } else {
                            sb.append(soapProvince.getProperty("ValueX").toString() + "|");
                        }
                    }
                    Log.e("warn", sb.toString());
                    Message msg = Message.obtain();
                    msg.what = 1;
                    msg.obj = sb.toString();
                    handlerGet_CheckAllRainFallHistory_List.sendMessage(msg);
                }
            } catch (Exception e) {
                Message msg = Message.obtain();
                msg.what = 0;
                handlerGet_CheckAllRainFallHistory_List.sendMessage(msg);
            }
        }
    };
    Handler handlerGet_CheckAllRainFallHistory_List = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int i = msg.what;
            if (i == 0) {
                progressDialog.dismiss();
                Toast.makeText(QuYu_YuLiang.this, "网络或服务器异常", Toast.LENGTH_SHORT).show();
            } else if (i == 1) {
                progressDialog.dismiss();
                String str = (String) msg.obj;
                Log.e("warn", str);
                String[] objects = str.split("\\|");
                for (int j = 0; j < objects.length; j++) {
                    if (objects[j].length() > 0) {
                        String[] values = objects[j].split(",");
                        JiangYuShuJu jy = new JiangYuShuJu();
                        jy.setTIME(values[0]);
                        jy.setValueX(values[1]);
                        list.add(jy);
                    }
                }
                showData();
            }
        }
    };
    private void ColorMethod(float y,List<Integer> list){
        if (y > 300) {
            list.add(getResources().getColor(R.color.yl17));
        } else if (y >= 200) {
            list.add(getResources().getColor(R.color.yl16));
        } else if (y >= 150) {
            list.add(getResources().getColor(R.color.yl15));
        } else if (y >= 130) {
            list.add(getResources().getColor(R.color.yl14));
        } else if (y >= 110) {
            list.add(getResources().getColor(R.color.yl13));
        } else if (y >= 90) {
            list.add(getResources().getColor(R.color.yl12));
        } else if (y >= 70) {
            list.add(getResources().getColor(R.color.yl11));
        } else if (y >= 50) {
            list.add(getResources().getColor(R.color.yl10));
        } else if (y >= 40) {
            list.add(getResources().getColor(R.color.yl09));
        } else if (y >= 30) {
            list.add(getResources().getColor(R.color.yl08));
        } else if (y >= 20) {
            list.add(getResources().getColor(R.color.yl07));
        } else if (y >= 15) {
            list.add(getResources().getColor(R.color.yl06));
        } else if (y >= 10) {
            list.add(getResources().getColor(R.color.yl05));
        } else if (y >= 6) {
            list.add(getResources().getColor(R.color.yl04));
        } else if (y >= 2) {
            list.add(getResources().getColor(R.color.yl03));
        } else if (y >= 1) {
            list.add(getResources().getColor(R.color.yl02));
        }else if(y>=0){
            list.add(getResources().getColor(R.color.yl01));
        }
    }

    private void showData(){

        if(list!=null&list.size()>0){
            List<Integer> list1 = new ArrayList<>();//装载柱状图颜色
            Float[] yl = new Float[list.size()];//雨量数据
            String[] appName = new String[list.size()];//时间
            float start = 0f;
            ArrayList<BarEntry> yVals1 = new ArrayList<>();
            barChart.getXAxis().setAxisMinValue(start);
            int len = StringTemplate.YLStringTemplate.length+1;//数据长度
            int[] myColors = new int[len];//取颜色长度
            String[] lbls = new String[len];//取比量值长度
            for (int i = 0; i < len; i++) {//取数据
                if(i==len-1){
                    lbls[i] = "警戒线";
                    myColors[i] = Color.rgb(255,33,33);
                }else{
                    lbls[i] = StringTemplate.YLStringTemplate[i];
                    myColors[i] = ColorTemplate.YL_Simaple[i];
                }
            }
            for (int i = 0; i<list.size(); i++) {
                if (list.size()> 0) {
                    appName[list.size()-1-i] =list.get(i).getTIME();//时间
                    yl[i] = Float.parseFloat(list.get(list.size()-1-i).getValueX());//数据
                    if(yl[i]>=100){isMore100=true;}
                    yVals1.add(new BarEntry(yl[i],i));
                    ColorMethod(yl[i],list1);
                }
            }
            barChart.setDrawBarShadow(false);
            barChart.setDrawValueAboveBar(true);
            barChart.setDescription("");
            barChart.setMaxVisibleValueCount(60);
            barChart.setPinchZoom(false);
            barChart.setDrawGridBackground(false);
            // 是否可以缩放
            barChart.setScaleEnabled(true);
            // 集双指缩放
            barChart.setPinchZoom(false);
            // 隐藏右边的坐标轴
            barChart.getAxisRight().setEnabled(false);
            //动画
            barChart.animateY(2500);
            barChart.animateX(2500);
            //X轴
            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setSpaceBetweenLabels(1);
            xAxis.setDrawLabels(true);//是否显示X轴数值
            xAxis.setTextSize(8);
            xAxis.setAxisMinValue(-0.5f); //设置x轴坐标起始值为-0.5 防止其实条形图被切去一半
            //Y轴
            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setLabelCount(5, false);//设置y轴数据个数
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setSpaceTop(15f);
            leftAxis.setAxisMinValue(0f);

            if(isMore100){
                //可以设置一条警戒线，如下：
                LimitLine ll = new LimitLine(2,"");//第一个参数为警戒线在坐标轴的位置，第二个参数为警戒线描述
                ll.setLineColor(Color.rgb(255,33,33));
                ll.setLineWidth(1f);
                ll.setTextColor(Color.GRAY);
                ll.setTextSize(12f);
                leftAxis.addLimitLine(ll);
                isMore100 =false;
            }
            Legend l = barChart.getLegend();//设置比例图
            l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
            l.setFormSize(8f);
            l.setTextSize(8f);
            l.setXEntrySpace(4f);
            l.setCustom(myColors, lbls);
            BarDataSet set1;
            //判断图表中原来是否有数据
            if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
                set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
                set1.setYVals(yVals1);//设置图表柱形图数值
                barChart.getData().notifyDataChanged();
                barChart.notifyDataSetChanged();
            } else {
                set1 = new BarDataSet(yVals1, "雨量监测");
                set1.setColors(list1);//设置柱状图的各种颜色，上面已设置ColorTemplate.MATERIAL_COLORS的值
                set1.setBarSpacePercent(30f);//设置柱间空白的宽度
                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);
                BarData data = new BarData(appName,dataSets);
                data.setValueTextSize(7f);
                data.setDrawValues(true);
                data.setValueTextColor(Color.BLACK);
                barChart.setData(data);// 设置数据
            }
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_fanhui_icon1:
                finish();
                break;
        }
    }
}

