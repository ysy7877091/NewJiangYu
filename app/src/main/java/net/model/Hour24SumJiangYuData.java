package net.model;

import android.util.Log;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.List;

import javabeen.cn.PublicBeen;
import mytext.administrator.example.com.jiangyujiangce.Path;

/**
 * Created by Administrator on 2017/4/25.
 */

public class Hour24SumJiangYuData implements PublicMethodInterface{

    private List<PublicBeen> list = new ArrayList<>();
    @Override
    public void getShopsData(final PublicInterface getDataListener) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Log.e("warn", "30");
                    // 命名空间
                    String nameSpace = "http://tempuri.org/";
                    // 调用的方法名称Get_OilAlarmInfo_List
                    String methodName = "Get_RealTimeRainfall_List";
                    // EndPoint
                    String endPoint = Path.get_ZanShibeidouPath();
                    // SOAP Action
                    String soapAction = "http://tempuri.org/Get_RealTimeRainfall_List";
                    // 指定WebService的命名空间和调用的方法名
                    SoapObject rpc = new SoapObject(nameSpace, methodName);
                    //设置需调用WebService接口需要传入的参数CarNum
                    // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(rpc);

                    HttpTransportSE ht = new HttpTransportSE(endPoint,10000);
                    ht.debug = true;
                    try {
                        // 调用WebService
                        ht.call(soapAction, envelope);
                    } catch (Exception e) {
                        String msg = e.getMessage();
                        if (e instanceof java.net.SocketTimeoutException) {
                            msg = "连接服务器超时，请检查网络";
                        } else if (e instanceof java.net.UnknownHostException) {
                            msg = "未知服务器，请检查配置";
                        }
                        getDataListener.onGetDataError(msg);
                    }
                    SoapObject object;
                    object = (SoapObject) envelope.getResponse();
                    // 得到服务器传回的数据 数据时dataset类型的
                    int count1 = object.getPropertyCount();
                    if (count1 == 0) {
                        getDataListener.onGetDataError("无雨量数据");
                        return;
                    }
                    Log.e("warn", String.valueOf(count1));
                    if (count1 > 0) {

                        for (int i = 0; i < count1; i++) {
                            PublicBeen jy = new PublicBeen();

                            SoapObject soapProvince = (SoapObject) object.getProperty(i);

                            jy.setJy24ValueX(soapProvince.getProperty("ValueX").toString());
                            Log.e("warn",soapProvince.getProperty("ValueX").toString());
                            jy.setJy24DevName(soapProvince.getProperty("DevName").toString());
                            Log.e("warn",soapProvince.getProperty("DevName").toString());
                            list.add(jy);
                        }
                        getDataListener.onGetDataSuccess(list);
                    }
                } catch (Exception e) {
                    getDataListener.onGetDataError("网络或服务器异常");
                }
            }

        }.start();


    }
}
