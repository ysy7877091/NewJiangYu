package mytext.administrator.example.com.jiangyujiangce;

/**
 * Created by Administrator on 2016-12-31.
 */
public class Path {
    private static String _UserID;
    //private static BengZhanClass _GisIndentfyBengZhanInfor;
    private static String _WebServicesConnectionPWD="95672E3E-AD90-44BD-AF29-6A783E1BDAE5";
    //提交用户名密码，获取泵站信息地图,泵厂各个列表的webservice接口地址
    private static String _WebServicesURL="http://appwebservices.bengzhan.sytxmap.com/AppBengZhanServices.asmx";
    private static String _WebServicesYLURL = "http://ysservices.sytxmap.com/";
    //private static String _WebServicesYLURL = "http://ysservices.sytxmap.com/JCDLXXGXPT.asmx";
    //private static String _MapUrl="http://ysmapservices.sytxmap.com/ArcGis/rest/services/BengZhanWai/MapServer";
    private static String _MapUrl="http://ysmapservices.sytxmap.com/arcgis/rest/services/New/ZongTu_Wai/MapServer";
    private static String New_MapUrl="http://ysmapservices.sytxmap.com/ArcGIS/rest/services/KFQ_YuLiangJianCe/MapServer";
    private static String _BengZhanMapIndex="http://ysmapservices.sytxmap.com/arcgis/rest/services/New/ZongTu_Wai/MapServer/0";
    private static String _BengZhanZhuJi="泵站注记";
    private static String _beidouPath="http://beidoujieshou.sytxmap.com:5963/GPSService.asmx";
    //private static String _beidouPath="http://192.168.90.122:5563/GPSService.asmx";
    //private static String _beidouPath="http://beidoujieshou.sytxmap.com:5563/GPSService.asmx";
    //下载新版本的接口地址
    private static String _AppUpdateUrl="http://appwebservices.bengzhan.sytxmap.com/update/version.xml";

    public static String get_UserID()
    {
        return _UserID;
    }
    public static void set_UserID(String userID)
    {
        _UserID=userID;
    }

    /*public static BengZhanClass get_GisIndentfyBengZhanInfor()
    {
        return _GisIndentfyBengZhanInfor;
    }
    public static void set_GisIndentfyBengZhanInfor(BengZhanClass gisIndentfyBengZhanInfor)
    {
        _GisIndentfyBengZhanInfor=gisIndentfyBengZhanInfor;
    }*/


    public static String get_WebServicesConnectionPWD()
    {
        return _WebServicesConnectionPWD;
    }
    public static String get_ZanShibeidouPath()
    {
        return _beidouPath;
    }
    public static String get_WebServicesURL()
    {
        return  _WebServicesURL;
    }
    public static String get_NewMapURL()
    {
        return  New_MapUrl;
    }
    public static String get_MapUrl(){return _MapUrl;}
    public static String get_BengZhanMapUrl(){return _BengZhanMapIndex;}
    public static String get_BengZhanZhuJi(){return  _BengZhanZhuJi;}
    public static String get_AppUpdateUrl(){return _AppUpdateUrl;}
    public static String get_WebServicesYLURL(){return _WebServicesYLURL;}
}
