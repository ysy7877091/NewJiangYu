package mytext.administrator.example.com.jiangyujiangce;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.model.PublicOneListInterface;
import net.model.ZhuCeRunnable;

/**
 * Created by Administrator on 2017/5/9.
 */

public class ZhuCe extends AppCompatActivity implements PublicOneListInterface{
    private TextView ZhuCe_NameText;
    private EditText ZhuCe_Name;

    private EditText ZhuCe_TelePhoneNum;
    private TextView ZhuCe_TelePhoneNumText;

    private EditText ZhuCe_PassWord;
    private TextView ZhuCe_PassWordText;

    private  EditText ZhuCe_SurePassWord;
    private TextView ZhuCe_SurePassWordText;

    private EditText ZhuCe_YanZhengMa;
    private TextView ZhuCe_YanZhengMaText;
    private MyProgressDialog ProgressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.zhuce_layout);
        CommonMethod.setStatuColor(ZhuCe.this,R.color.tj9);
        init();
    }
    private void init(){
        //返回
        Button Zhuce_Back = (Button)findViewById(R.id.Zhuce_Back);
        Zhuce_Back.setOnClickListener(new ZhuCeListener());
        //姓名
        ZhuCe_Name = (EditText)findViewById(R.id.ZhuCe_Name);
        ZhuCe_NameText = (TextView)findViewById(R.id.ZhuCe_NameText);
        ZhuCe_Name.setOnFocusChangeListener(new ZhuCe_FocusChangeListener());
        //手机号码
        ZhuCe_TelePhoneNum = (EditText)findViewById(R.id.ZhuCe_TelePhoneNum);
        ZhuCe_TelePhoneNumText = (TextView)findViewById(R.id.ZhuCe_TelePhoneNumText);
        ZhuCe_TelePhoneNum .setOnFocusChangeListener(new ZhuCe_FocusChangeListener());
        //密码
        ZhuCe_PassWord = (EditText)findViewById(R.id.ZhuCe_PassWord);
        ZhuCe_PassWordText = (TextView)findViewById(R.id.ZhuCe_PassWordText);
        ZhuCe_PassWord .setOnFocusChangeListener(new ZhuCe_FocusChangeListener());
        //再次输入密码
        ZhuCe_SurePassWord= (EditText)findViewById(R.id.ZhuCe_SurePassWord);
        ZhuCe_SurePassWordText = (TextView)findViewById(R.id.ZhuCe_SurePassWordText);
        ZhuCe_SurePassWord.setOnFocusChangeListener(new ZhuCe_FocusChangeListener());
        //验证码
        ZhuCe_YanZhengMa= (EditText)findViewById(R.id.ZhuCe_YanZhengMa);
        ZhuCe_YanZhengMaText = (TextView)findViewById(R.id.ZhuCe_YanZhengMaText);
        ZhuCe_YanZhengMa.setOnFocusChangeListener(new ZhuCe_FocusChangeListener());
        //注册
        ImageView registerSubmit = (ImageView)findViewById(R.id.registerSubmit);
        registerSubmit.setOnClickListener(new ZhuCeListener());

    }
    //请求成功回调
    @Override
    public void onGetDataSuccess(String succmessage) {
        cancelDialg();
        Message msg =Message.obtain();
        msg.obj=succmessage;
        handler.sendMessage(msg);
        Log.e("warn",succmessage);

    }
    //请求失败回调
    @Override
    public void onGetDataError(String errmessage) {
        cancelDialg();
        Message msg =Message.obtain();
        msg.obj=errmessage;
        handler.sendMessage(msg);
        Log.e("warn",errmessage);
    }
    //请求无数据回调
    @Override
    public void onEmptyData(String Emptymessage) {
        cancelDialg();
        Message msg =Message.obtain();
        msg.obj=Emptymessage;
        handler.sendMessage(msg);
    }

    private class ZhuCeListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case  R.id.registerSubmit:
                                        boolean isPass = YanZheng();
                                        if(!isPass==true){return;}
                                        ProgressDialog = new MyProgressDialog(ZhuCe.this,false,"注册中...");
                                        new ZhuCeRunnable(ZhuCe_Name.getText().toString(),
                                                ZhuCe_TelePhoneNum.getText().toString()
                                                ,ZhuCe_PassWord.getText().toString(),
                                                ZhuCe_YanZhengMa.getText().toString()).getShopsData(ZhuCe.this);
                                        break;
                case  R.id.Zhuce_Back:
                                        finish();
                                        break;
            }
        }
    }
    private class ZhuCe_FocusChangeListener implements View.OnFocusChangeListener{

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()){
                case  R.id.ZhuCe_Name:
                                        if(hasFocus){
                                            ZhuCe_NameText.setBackgroundResource(R.color.tj9);
                                        }else{
                                            ZhuCe_NameText.setBackgroundResource(R.color.tj11);
                                        }
                                        break;
                case  R.id.ZhuCe_TelePhoneNum:
                                        if(hasFocus){
                                            ZhuCe_TelePhoneNumText.setBackgroundResource(R.color.tj9);
                                        }else{
                                            ZhuCe_TelePhoneNumText.setBackgroundResource(R.color.tj11);
                                        }
                                        break;
                case  R.id.ZhuCe_PassWord:
                                        if(hasFocus){
                                            ZhuCe_PassWordText.setBackgroundResource(R.color.tj9);
                                        }else{
                                            ZhuCe_PassWordText.setBackgroundResource(R.color.tj11);
                                        }
                                        break;
                case  R.id.ZhuCe_SurePassWord:
                                        if(hasFocus){
                                            ZhuCe_SurePassWordText.setBackgroundResource(R.color.tj9);
                                        }else{
                                            ZhuCe_SurePassWordText.setBackgroundResource(R.color.tj11);
                                        }
                                        break;
                case  R.id.ZhuCe_YanZhengMa:
                                        if(hasFocus){
                                            ZhuCe_YanZhengMaText.setBackgroundResource(R.color.tj9);
                                        }else{
                                            ZhuCe_YanZhengMaText.setBackgroundResource(R.color.tj11);
                                        }
                                        break;
            }
        }
    }
    private boolean YanZheng(){
        if(ZhuCe_Name.getText().toString().equals("")||ZhuCe_Name.getText().toString()==null){
            Toast.makeText(ZhuCe.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(ZhuCe_TelePhoneNum.getText().toString().equals("")||ZhuCe_TelePhoneNum.getText().toString()==null){
            Toast.makeText(ZhuCe.this, "电话号码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(ZhuCe_PassWord.getText().toString().equals("")||ZhuCe_PassWord.getText().toString()==null){
            Toast.makeText(ZhuCe.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!ZhuCe_PassWord.getText().toString().equals(ZhuCe_SurePassWord.getText().toString())){
            Toast.makeText(ZhuCe.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(ZhuCe_YanZhengMa.getText().toString().equals("")||ZhuCe_YanZhengMa.getText().toString()==null){
            Toast.makeText(ZhuCe.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void cancelDialg(){
        if(ProgressDialog!=null){
            ProgressDialog.dismiss();
            ProgressDialog=null;
        }
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str =(String)msg.obj;
            if(str.contains("RETURNNUM")){
               int index= str.indexOf("RETURNNUM");
                String result =str.substring(index+1);
                int index1 = result.indexOf("=");
                int index2 = result.indexOf(";");
                String result1=result.substring(index1+1,index2);
                if(result1.equals("0")){
                    Toast.makeText(ZhuCe.this, "注册成功", Toast.LENGTH_SHORT).show();
                    finish();
                }else if(result1.equals("1")){
                    Toast.makeText(ZhuCe.this, "验证码错误", Toast.LENGTH_SHORT).show();
                }else if(result1.equals("2")){
                    Toast.makeText(ZhuCe.this, "账号已存在", Toast.LENGTH_SHORT).show();
                }else if(result1.equals("3")){
                    Toast.makeText(ZhuCe.this, "注册失败，请联系管理员", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(ZhuCe.this, "注册失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelDialg();
    }
}
