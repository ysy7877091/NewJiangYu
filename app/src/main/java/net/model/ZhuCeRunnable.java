package net.model;

import android.os.Message;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Objects;

import mytext.administrator.example.com.jiangyujiangce.Path;

/**
 * Created by Administrator on 2017/5/9.
 */

public class ZhuCeRunnable implements PublicOneListMehtodInterface{
    private String name;
    private String TelePhoneNum;
    private String PassWord;
    private String  YanZhengMa;
    private PublicOneListInterface getData;
    public ZhuCeRunnable(String name,String TelePhoneNum,String PassWord,String YanZhengMa){
        this.name=name;
        this.TelePhoneNum =TelePhoneNum;
        this.PassWord = PassWord;
        this.YanZhengMa = YanZhengMa;
    }
    private Object object;
    @Override
    public void getShopsData(PublicOneListInterface getDataListener) {
        this.getData = getDataListener;
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            try{
                                // 命名空间
                                String nameSpace = "http://tempuri.org/";
                                // 调用的方法名称
                                String methodName = "Insert_RainLoginUser";
                                // EndPoint
                                String endPoint = "http://beidoujieshou.sytxmap.com:5963/GPSService.asmx";
                                // SOAP Action
                                String soapAction = "http://tempuri.org/Insert_RainLoginUser";
                                // 指定WebService的命名空间和调用的方法名
                                SoapObject rpc = new SoapObject(nameSpace, methodName);
                                //设置需调用WebService接口需要传入的参数CarNum
                                rpc.addProperty("loginName",TelePhoneNum);
                                rpc.addProperty("loginPwd",PassWord);
                                rpc.addProperty("NAME",name);
                                rpc.addProperty("IDCode",YanZhengMa);
                                // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
                                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
                                envelope.dotNet = true;
                                envelope.setOutputSoapObject(rpc);
                                HttpTransportSE ht = new HttpTransportSE(endPoint,10000);
                                ht.debug=true;
                                Log.e("warn","50");
                                (new MarshalBase64()).register(envelope);
                                try {
                                    // 调用WebService
                                    ht.call(soapAction,envelope);
                                    object = envelope.getResponse();
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
                                getData.onGetDataSuccess(object.toString()+"");
                            } catch (Exception e){
                                getData.onGetDataError("网路或服务器异常");
                            }
                        }
                    }.start();
    }

}
