package mytext.administrator.example.com.jiangyujiangce;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

import net.model.Hour24SumJiangYuData;
import net.model.PublicInterface;

import java.util.ArrayList;
import java.util.List;

import javabeen.cn.PublicBeen;

/**
 * Created by Administrator on 2017/5/11.
 */

public class QuanQuJiangYu24Hour extends AppCompatActivity implements PublicInterface{
    private HorizontalBarChart barChart;
    private MyProgressDialog progressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.quanqujiangyu24hour_layout);
        CommonMethod.setStatuColor(QuanQuJiangYu24Hour.this,R.color.white);
        init();
    }
    private void init(){
        Button JY24hour_Back =(Button)findViewById(R.id.JY24hour_Back);
        JY24hour_Back.setOnClickListener(new QuanQuJiangYu24HourListener());
        Button JY24hour_History =(Button)findViewById(R.id.JY24hour_History);
        JY24hour_History.setOnClickListener(new QuanQuJiangYu24HourListener());
        barChart =(HorizontalBarChart)findViewById(R.id.YLchart);
        YLRequest();
    }
    private List<PublicBeen> list;
    @Override
    public void onGetDataSuccess(List<PublicBeen> list) {
        cancelDialog();
        this.list = list;
        Message msg = Message.obtain();
        msg.obj = "999";
        handler.sendMessage(msg);
    }

    @Override
    public void onGetDataError(String errmessage) {
        cancelDialog();
        Message msg = Message.obtain();
        msg.obj = errmessage;
        handler.sendMessage(msg);
    }

    @Override
    public void onEmptyData(String Emptymessage) {
        cancelDialog();
        Message msg = Message.obtain();
        msg.obj = Emptymessage;
        handler.sendMessage(msg);
    }

    private class  QuanQuJiangYu24HourListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.JY24hour_Back:
                                            finish();
                                            break;
                case R.id.JY24hour_History:
                                                Intent intent = new Intent(QuanQuJiangYu24Hour.this,LiShi_ChaXun.class);
                                                startActivity(intent);
                                                finish();
                                                break;
            }
        }
    }
    //请求雨量数据
    private void YLRequest(){
        /*if(getActivity()!=null){
            progressDialog =new MyProgressDialog(getActivity(),false,"正在加载中...");
        }*/
        //new Thread(networkGetYuLiangInfor).start();
        progressDialog = new MyProgressDialog(QuanQuJiangYu24Hour.this,false,"正在加载中...");
        new Hour24SumJiangYuData().getShopsData(this);
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str =(String)msg.obj;
            if(str.equals("999")){
                showData();
            }else{
                Toast.makeText(QuanQuJiangYu24Hour.this,str, Toast.LENGTH_SHORT).show();
            }
        }
    };
    private boolean isMore100=false;
    private void showData(){
        if(list!=null&list.size()>0){
            List<Integer> list1 = new ArrayList<Integer>();//装载柱状图颜色
            Float[] yl = new Float[list.size()];//雨量
            String[] appName = new String[list.size()];//区域名
            float start = 0f;
            ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
            barChart.getXAxis().setAxisMinValue(start);
            int j=0;
            for (int i = 0; i<list.size(); i++) {

                if ( list.size()> 1) {
                    appName[list.size()-1-i] =list.get(i).getJy24DevName();//区域名
                    yl[i] = Float.parseFloat(list.get(list.size()-1-i).getJy24ValueX());//数据
                    if(yl[i]>=100){isMore100=true;}
                    yVals1.add(new BarEntry(yl[i],i));
                    ColorMethod(yl[i],list1);//

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
            //xAxis.setTypeface(mTfLight);
            xAxis.setDrawGridLines(false);
            xAxis.setSpaceBetweenLabels(1);
            xAxis.setDrawLabels(true);//是否显示X轴数值
            //xAxis.setGranularity(1f); // only intervals of 1 day
            //xAxis.setLabelCount(objects.length,true);//label长度=返回数据长度
            //xAxis.setValueFormatter(xAxisFormatter);//X轴数据
            xAxis.setTextSize(8);
            xAxis.setAxisMinValue(-0.5f); //设置x轴坐标起始值为-0.5 防止其实条形图被切去一半

            //AxisValueFormatter custom = new MyAxisValueFormatterYL();
            //Y轴
            YAxis leftAxis = barChart.getAxisLeft();
            //leftAxis.setTypeface(mTfLight);
            leftAxis.setLabelCount(5, false);//设置y轴数据个数
            //leftAxis.setLabelCount(8, false);
            //leftAxis.setValueFormatter(custom);
            leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
            leftAxis.setSpaceTop(15f);
            leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true)

            if(isMore100){
                //可以设置一条警戒线，如下：
                LimitLine ll = new LimitLine(100, "");//第一个参数为警戒线在坐标轴的位置，第二个参数为警戒线描述
                ll.setLineColor(Color.rgb(255,33,33));
                ll.setLineWidth(1f);
                ll.setTextColor(Color.GRAY);
                ll.setTextSize(12f);
                // .. and more styling options
                leftAxis.addLimitLine(ll);
                isMore100 =false;
            }




            Legend l = barChart.getLegend();//设置比例图
            l.setEnabled(false);

            BarDataSet set1;
            //判断图表中原来是否有数据
            if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
                set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
                //set1 = new BarDataSet(yVals1,"");
                set1.setYVals(yVals1);//设置图表柱形图数值
                barChart.getData().notifyDataChanged();
                barChart.notifyDataSetChanged();

            } else {
                set1 = new BarDataSet(yVals1, "雨量监测");
                set1.setColors(list1);//设置柱状图的各种颜色，上面已设置ColorTemplate.MATERIAL_COLORS的值
                set1.setBarSpacePercent(30f);//设置柱间空白的宽度
                //set1.setBarSpacePercent(10);
                // set custom labels and colors
                //设置柱状图颜色，第一个color.rgb为第一个柱状图颜色。以此类推
                //set1.setColors(new int[]{Color.rgb(255,241,226),Color.rgb(155,241,226)});
                ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
                dataSets.add(set1);

                //BarData data = new BarData(dataSets);
                BarData data = new BarData(appName,dataSets);
                data.setValueTextSize(7f);
                data.setDrawValues(true);
                data.setValueTextColor(Color.BLACK);
                //data.setValueFormatter(new CustomerValueFormatter());
                //data.setValueTypeface(mTfLight);
                //data.setBarWidth(0.8f);
                //mChart.setExtraOffsets(0,0,0,200);//此种方法可以一次设置上下左右偏移量。根据自己数据哪个地方显示不全，对应调用方法。
                barChart.setData(data);// 设置数据
            }
        }
    }
    //根据柱形图数据不同设置不同柱形图对应的颜色
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
    private void cancelDialog(){
        if(progressDialog !=null){
                progressDialog .dismiss();
                progressDialog=null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelDialog();
    }
}
