package mytext.administrator.example.com.jiangyujiangce;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ant.liao.GifView;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISDynamicMapServiceLayer;
import com.esri.android.map.ags.ArcGISLayerInfo;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.Feature;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.tasks.ags.query.Query;

import net.model.Hour24SumJiangYuData;
import net.model.PublicInterface;

import java.util.List;
import java.util.UUID;

import javabeen.cn.PublicBeen;

/**
 * Created by Administrator on 2017/5/10.
 */

public class JiangYuMap extends AppCompatActivity implements PublicInterface{
    private MapView mMapView;
    private GifView gifLoadGis;
    private int mLoadIcoChiCun = 140;
    private String mCurrentBengZhanDaiMa = "";
    private boolean mIsIdentfy = false;
    private RelativeLayout rlLoadView;////gifview的父布局
    private ArcGISDynamicMapServiceLayer layer;
    private ArcGISLayerInfo layerInforZJ = null;
    private MyProgressDialog ProgressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.jiangyumap_layout);
        CommonMethod.setStatuColor(JiangYuMap.this,R.color.white);
        init();
    }
    private void init(){
        Button map_Back = (Button)findViewById(R.id.map_Back);
        map_Back.setOnClickListener(new JiangYuMapListener());
        Button map_History = (Button)findViewById(R.id.map_History);
        map_History.setOnClickListener(new JiangYuMapListener());
        mMapView = (MapView)findViewById(R.id.mapView);
        ImageView map_select = (ImageView) findViewById(R.id.map_select);
        map_select.setOnClickListener(new JiangYuMapListener());
        ImageView map_TongJi = (ImageView) findViewById(R.id.map_TongJi);
        map_TongJi.setOnClickListener(new JiangYuMapListener());
        gifLoadGis =(GifView)findViewById(R.id.gifLoadGis);
        getMap();
    }
    private void getMap(){

        layer = new ArcGISDynamicMapServiceLayer(Path.get_NewMapURL());
        layer.refresh();//刷新地图
        //layer.getLayers()[1].setVisible(false);
        //添加地图

        mMapView.addLayer(layer);
        //mMapView.zoomout();
        mMapView.setOnSingleTapListener(mOnSingleTapListener);//单击地图上的泵站
        mMapView.setOnStatusChangedListener(new mMapViewChangListener());

        //Button btn_clean = (Button) findViewById(R.id.btn_clean);//清空雨量图层
        rlLoadView = (RelativeLayout) findViewById(R.id.layoutLoadGISView);//gifview的父布局
        gifLoadGis = (GifView) findViewById(R.id.gifLoadGis); ////加载的动画
        WindowManager wm =getWindowManager();
        //gifview控件刚开始加载的背景
        gifLoadGis.setGifImage(R.drawable.load);//加载的动画
        //getLayoutParams()方法 和 setLayoutParams()方法 重新设置控件布局
        //设置gifview的margin值
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) gifLoadGis.getLayoutParams();
        lp.setMargins((wm.getDefaultDisplay().getWidth() - mLoadIcoChiCun) / 2, (wm.getDefaultDisplay().getHeight() - mLoadIcoChiCun) / 2 - 200, (wm.getDefaultDisplay().getWidth() - mLoadIcoChiCun) / 2, (wm.getDefaultDisplay().getHeight() - mLoadIcoChiCun) / 2);
        gifLoadGis.setLayoutParams(lp);
        rlLoadView.setVisibility(View.VISIBLE);
        gifLoadGis.showAnimation();//加载的动画
        new Hour24SumJiangYuData().getShopsData(JiangYuMap.this);

    }
    private List<PublicBeen> list;
    @Override
    public void onGetDataSuccess(List<PublicBeen> list) {
        this.list=list;
        Message msg = Message.obtain();
        msg.obj="99";
        get24HourYLData.sendMessage(msg);
    }

    @Override
    public void onGetDataError(String errmessage) {
        Message msg = Message.obtain();
        msg.obj=errmessage;
        get24HourYLData.sendMessage(msg);
    }

    @Override
    public void onEmptyData(String Emptymessage) {
        Message msg = Message.obtain();
        msg.obj=Emptymessage;
        get24HourYLData.sendMessage(msg);
    }

    private  class  JiangYuMapListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.map_Back:TuiChu();break;
                case R.id.map_History:
                                        Intent intent = new Intent(JiangYuMap.this,LiShi_ChaXun.class);
                                        startActivity(intent);
                                        break;
                case R.id.map_select:break;
                case R.id.map_TongJi:
                                        Intent intent1 = new Intent(JiangYuMap.this,QuanQuJiangYu24Hour.class);
                                        startActivity(intent1);
                                        break;
            }
        }
    }

    private class mMapViewChangListener implements OnStatusChangedListener {//OnStatusChangedListener接口用于监听MapView或Layer（图层）状态变化的监听器

        @Override
        public void onStatusChanged(Object o, STATUS status) {
            if (status == STATUS.INITIALIZED) {
            } else if (status == STATUS.LAYER_LOADED) {
                if (layer != null) {
                    if (layer.getLayers() != null) {

                        layerInforZJ = layer.getLayers()[0];
                        layerInforZJ.getName();
                        Log.e("warn",layerInforZJ.getLayers().length+"");
                        /* List<String> list1=null;
                        for(int i=0;i<list.size();i++){
                           Legend l= list.get(i);
                                   list1=l.getValues();

                        }
                        for(int j=0;j<list1.size();j++){
                            Log.e("warn","list1:"+list1.get(j));
                        }*/


                        Log.e("warn","图层总长度"+layer.getLayers().length);
                        for (int i = 0; i < layer.getLayers().length; i++) {
                            ArcGISLayerInfo layerInfor = layer.getLayers()[i];
                            layerInfor.setVisible(true);
                            Log.e("GISActivity地图服务加载", "图层名称：" + layerInfor.getName() + "");
                        }
                    }
                }
                rlLoadView.setVisibility(View.INVISIBLE);
                //gifLoadGis.showCover();
                //Toast.makeText(GISActivity.this, "地图加载成功", Toast.LENGTH_SHORT).show();
            } else if (status == STATUS.INITIALIZATION_FAILED) {
                Toast.makeText(getApplicationContext(), "地图加载失败", Toast.LENGTH_SHORT).show();
                rlLoadView.setVisibility(View.INVISIBLE);
                gifLoadGis.showCover();
            } else if (status == STATUS.LAYER_LOADING_FAILED) {
                Toast.makeText(getApplicationContext(), "图层加载失败", Toast.LENGTH_SHORT).show();
                rlLoadView.setVisibility(View.INVISIBLE);
                gifLoadGis.showCover();
            }
        }
    }
    private String queryLayer = Path.get_NewMapURL()+"/0";
    Point point;
    OnSingleTapListener mOnSingleTapListener = new OnSingleTapListener() {
        @Override
        public void onSingleTap(float x, float y) {
            point=mMapView.toMapPoint(x,y);
            AsyncQueryTask ayncQuery = new AsyncQueryTask();
            ayncQuery.execute(x,y);
        }
    };
    //点击地图 获取相应的区域
    private class AsyncQueryTask extends AsyncTask<Float, Void, FeatureSet> {

        @Override
        protected FeatureSet doInBackground(Float... queryArray) {

            if (queryArray == null || queryArray.length <= 1)
                return null;
            float x=queryArray[0];
            float y=queryArray[1];
            //Query query = new Query();
            Query query = new Query();
            query.setGeometry(point);
            query.setReturnGeometry(true);
            query.setOutFields(new String[] {"*"});
            //query.setWhere("1=1");
            //query.setOutFields(new String[] { "县名称_1", "乡名称_1", "村名称_1",
                  //  "地块名称", "统一编号" });
           /*QueryParameters qParameters = new QueryParameters();    //创建查询参数对象
            //SpatialReference sr = SpatialReference.create();//设置空间参考坐标系
            qParameters.setGeometry(point);//设置识别位置
            //qParameters.setOutSpatialReference(sr);//设置输出坐标系
            qParameters.setReturnGeometry(true);//指定是否返回几何对象*/
            com.esri.core.tasks.ags.query.QueryTask qTask = new com.esri.core.tasks.ags.query.QueryTask(queryLayer);
            try {
                FeatureSet results = qTask.execute(query);//执行识别任务
                return results;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(FeatureSet results) {

            if (results != null) {
                Graphic[]  grs =  results.getGraphics();
                if(grs.length>0) {
                    Graphic graphic = results.getGraphics()[0];
                    // String[] names = graphic.getAttributeNames();

                    String xian = getValue(graphic, "DocPath", "");//点击时获取的点击区域名称 RefName获取名称参数 DocPath获取参数
                    Log.e("warn", xian);
                }
            }
        }
    }
    String getValue(Graphic graphic, String key, String defaultVal) {
        Object obj = graphic.getAttributeValue(key);
        if (obj == null)
            return defaultVal;
        else
            return obj.toString();
    }
    private void TuiChu(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("是否退出应用");
        //builder.setTitle("是否退出应用");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            TuiChu();
        }
        return super.onKeyDown(keyCode, event);
    }
    String targetServerURL = Path.get_NewMapURL();
    //降雨量分布
    Runnable getlayer = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < list.size(); i++) {
                String url = targetServerURL.concat("/0");
                // 查询所需的参数类
                Query query = new Query();
                //String whereClause = queryParams[1];
                query.setText(list.get(i).getJy24DevName().toString());
                /*SpatialReference sr = SpatialReference.create(102100);// 建立一个空间参考
                // WKID_WGS84_WEB_MERCATOR_AUXILIARY_SPHERE（102100）
                query.setGeometry(new Envelope(-20147112.9593773, 557305.257274575,
                        -6569564.7196889, 11753184.6153385));// 设置查询空间范围
                query.setOutSpatialReference(sr);// 设置查询输出的坐标系*/
                query.setReturnGeometry(true);// 是否返回空间信息
                //query.setWhere(whereClause);// where条件

                com.esri.core.tasks.ags.query.QueryTask qTask = new com.esri.core.tasks.ags.query.QueryTask(url);// 查询任务类
                FeatureSet fs = null;
                Log.i(null, "doInBackground is running !");
                try {
                    fs = qTask.execute(query);// 执行查询，返回查询结果
                } catch (Exception e) {
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString("value",list.get(i).getJy24ValueX());
                    msg.obj = fs;
                    msg.setData(bundle);
                    handler.sendMessage(msg);

                }
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString("value", list.get(i).getJy24ValueX());
                msg.obj = fs;
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        }
    };

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FeatureSet fs = (FeatureSet) msg.obj;
            fs.getDisplayFieldName();
            Log.e("warn", fs.getDisplayFieldName());
            String s1 = msg.getData().get("value").toString();
            float count = Float.valueOf(s1);
            if (fs != null) {
                Graphic[] grs = fs.getGraphics();
                Log.e("warn",grs.toString());
                Log.e("warn", grs.length + "adsds");
                Log.e("warn", fs.getGeometryType().name());
                if (grs.length > 0) {

                    // gl = new GraphicsLayer();
                    // SimpleRenderer sr = new SimpleRenderer(
                    // new SimpleFillSymbol(Color.RED));// 设置渲染器
                    // gl.setRenderer(sr);
                    GraphicsLayer graphicsLayer = new GraphicsLayer();//创建新图层对象
                    SimpleFillSymbol symbol = null;//图层样式对象
                    //某一部分图层颜色
                    if(count > 300){
                        symbol = new SimpleFillSymbol(Color.parseColor("#FFC6F7"));
                    } else if (count >=200) {
                        symbol = new SimpleFillSymbol(Color.parseColor("#FE00F7"));
                    } else if (count >= 150) {
                        symbol = new SimpleFillSymbol(Color.parseColor("#C500BE"));
                    } else if (count >= 130) {
                        symbol = new SimpleFillSymbol(Color.parseColor("#8E0090"));
                    } else if (count >= 110) {
                        symbol = new SimpleFillSymbol(Color.parseColor("#9B0005"));
                    } else if (count >= 90) {
                        symbol = new SimpleFillSymbol(Color.parseColor("#C60005"));
                    } else if (count >= 70) {
                        symbol = new SimpleFillSymbol(Color.parseColor("#FF0005"));
                    } else if (count >= 50) {
                        symbol = new SimpleFillSymbol(Color.parseColor("#FF9305"));
                    } else if (count >= 40) {
                        symbol = new SimpleFillSymbol(Color.parseColor("#FFC905"));
                    } else if (count >= 30) {
                        symbol = new SimpleFillSymbol(Color.parseColor("#FFFA05"));
                    } else if (count >= 20) {
                        symbol = new SimpleFillSymbol(Color.parseColor("#2FFF00"));
                    } else if (count >= 15) {
                        symbol = new SimpleFillSymbol(Color.parseColor("#3E9609"));
                    } else if (count >= 10) {
                        symbol = new SimpleFillSymbol(Color.parseColor("#0066FA"));
                    } else if (count >= 6) {
                        symbol = new SimpleFillSymbol(Color.parseColor("#0098FD"));
                    } else if (count >= 2) {
                        symbol = new SimpleFillSymbol(Color.parseColor("#00CBFB"));
                    } else if (count >= 1) {
                        symbol = new SimpleFillSymbol(Color.parseColor("#9AFEFD"));
                    } else if (count >= 0) {
                        symbol = new SimpleFillSymbol(Color.parseColor("#C3BEBD"));
                    }
                    graphicsLayer.setRenderer(new SimpleRenderer(symbol));
                    graphicsLayer.addGraphics(grs);//生成图层
                    graphicsLayer.setName("#YL" + UUID.randomUUID());
                    mMapView.addLayer(graphicsLayer);//将新图层放到mapview地图层上
                    mMapView.postInvalidate();

                }
            }
        }
    };
    //获取余量区域数据 用于判断降雨量分布颜色
    private Handler get24HourYLData = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String str = (String)msg.obj;
            if(str.equals("99")){
                new Thread(getlayer).start();
            }else{
                Toast.makeText(JiangYuMap.this, "获取雨量分布失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
