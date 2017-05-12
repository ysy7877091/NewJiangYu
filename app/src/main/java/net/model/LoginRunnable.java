package net.model;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import mytext.administrator.example.com.jiangyujiangce.Path;

/**
 * Created by Administrator on 2017/5/9.
 */

public class LoginRunnable implements PublicOneListMehtodInterface {
    private String user;
    private String pwd;
    private PublicOneListInterface getData;;
    public LoginRunnable(String user,String pwd){
        this.user=user;
        this.pwd=pwd;
    }
    @Override
    public void getShopsData(PublicOneListInterface getDataListener) {
        this.getData=getDataListener;
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Log.e("warn", "30");
                    // 命名空间
                    String nameSpace = "http://tempuri.org/";
                    // 调用的方法名称Get_OilAlarmInfo_List
                    String methodName = "Get_RainLoginOKOrNo";
                    // EndPoint
                    String endPoint = Path.get_ZanShibeidouPath();
                    // SOAP Action
                    String soapAction = "http://tempuri.org/Get_RainLoginOKOrNo";
                    // 指定WebService的命名空间和调用的方法名
                    SoapObject rpc = new SoapObject(nameSpace, methodName);
                    //设置需调用WebService接口需要传入的参数CarNum
                    //rpc.addProperty("CARNUM",OilHistory_carNum.getText().toString());
                    //Log.e("warn",TelePhoneNum+":"+PassWord+":"+name+":"+YanZhengMa);
                    rpc.addProperty("loginName",user);
                    rpc.addProperty("loginPwd", pwd);
                    // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
                    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                    envelope.dotNet = true;
                    envelope.setOutputSoapObject(rpc);

                    HttpTransportSE ht = new HttpTransportSE(endPoint,20000);
                    ht.debug = true;
                    try {
                        // 调用WebService
                        ht.call(soapAction, envelope);
                    } catch (Exception e) {
                        String msg1 = e.getMessage();
                        if (e instanceof java.net.SocketTimeoutException) {
                            msg1 = "连接服务器超时，请检查网络";
                            getData.onGetDataError(msg1);
                            return;
                        } else if (e instanceof java.net.UnknownHostException) {
                            msg1 = "未知服务器，请检查配置";
                            getData.onGetDataError(msg1);
                            return;
                        }

                    }
                    SoapObject object;
                    object = (SoapObject) envelope.getResponse();
                    // 得到服务器传回的数据 数据时dataset类型的
                    int count1 = object.getPropertyCount();
                    if(count1 ==0){
                        getData.onEmptyData("服务器数据为空");
                        return;
                    }
                    Log.e("warn", String.valueOf(count1));
                    if (count1 > 0) {
                        Log.e("warn","-----------------------------");
                        SoapObject soapProvince = (SoapObject) envelope.bodyIn;
                        Log.e("warn",soapProvince.getProperty("Get_RainLoginOKOrNoResult").toString()+":返回id");//dataset数据类型
                        String str = soapProvince.getProperty("Get_RainLoginOKOrNoResult").toString();
                        getData.onGetDataSuccess(str);
                    }
                } catch (Exception e) {
                    getData.onGetDataError("网路或服务器异常");
                }
            }
        }.start();

    }
}
