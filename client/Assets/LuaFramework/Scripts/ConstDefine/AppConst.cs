#if DONGHAI || YUNYOU || ZHONGFU
//
#else //XH SHENHE

#define _LOCAL_GAME_

#endif

using UnityEngine;
using System;
using System.Collections;
using System.Collections.Generic;

namespace LuaFramework {
    public class AppConst {
        public const bool DebugMode = true; //调试模式-用于内部测试(发布手机版本时，改成false后再编译资源)
        
        public static bool DebugEngine = DebugMode;
        public static string GameName = "";
        public static string PlatId = "";
        public static int GameId = 0; //游戏平台返回id
        public static int SubGameId = 0; //游戏平台返回子id

        public static bool isSDKPlat = false;//是否是sdk平台
        public static bool isAppleIAP = false;//是否为苹果内购
        public static int IAPPriceUnit = 1;//充值价格单位默认元(1) 角(10) 分(100)

        //登录请求
        public static string webIP = "";
        public static string webPort = "";

        /// <summary>
        /// 如果开启更新模式，前提必须启动框架自带服务器端。
        /// 否则就需要自己将StreamingAssets里面的所有内容
        /// 复制到自己的Webserver上面，并修改下面的WebUrl。
        /// </summary>
        public const bool UpdateMode = !DebugMode;                       //更新模式-默认关闭 
        public const bool LuaBundleMode = !DebugMode;                    //Lua代码AssetBundle模式 (纯lua可运行)
        public const bool LuaByteMode = false;                       //Lua字节码模式-默认关闭 
        public const bool ShowCompanyNameWithTips = !DebugMode;     //是否显示平台宣传图

        public const int TimerInterval = 1;
        public const int GameFrameRate = 30;                        //游戏帧频

        public const string AppName = "LuaFramework";               //应用程序名称
        public const string LuaTempDir = "Lua/";                    //临时目录
        public const string AppPrefix = AppName + "_";              //应用程序前缀
        public const string ExtName = ".unity3d";                   //素材扩展名
        public const string AssetDir = "StreamingAssets";           //素材目录 


    #if UNITY_ANDROID
        public const string plat = "android/";
    #elif (UNITY_IPHONE || UNITY_IOS)
        public const string plat = "ios/";
    #else
        public const string plat = "window/";
    #endif



        public static string host
        {
#if ONLINE // 线上cdn
    #if DONGHAI // 东海运营资源
            get {
                GameName = "修仙魔域";
                PlatId = "DONGHAI";
                IAPPriceUnit = 100;
                webIP = "sdk.171game.com";
                webPort = "8001";
				isAppleIAP = true;
                isSDKPlat = true;
                return "http://cdn.171game.com/donghai/data/";
                }
    #elif YUNYOU // 云游运营资源
             get {
                #if DTXMZ
                    GameName = "大唐降魔传";
                    GameId = 90001;
                    SubGameId = 1;
                #elif DTXX
                    GameName = "大唐仙侠";
                    GameId = 90001;
                    SubGameId = 2;
                #elif XMCQ
                    GameName = "降魔苍穹";
                    GameId = 90001;
                    SubGameId = 3;
                #endif
                PlatId = "YUNYOU";
                webIP = "sdk.171game.com";
                webPort = "8002";
				isAppleIAP = true;
                isSDKPlat = true;
                return "http://cdn.171game.com/yunyou/data/";
                }
    #elif ZHONGFU // 中富 拉克丝运营资源
             get {
                #if XJCA
                    GameName = "仙剑长安";
                    GameId = 90002;
                    SubGameId = 1271;
                #elif YXJ
                    GameName = "御仙诀";
                    GameId = 90002;
                    SubGameId = 10012;
                #elif FTJ
                    GameName = "焚天诀";
                    GameId = 90002;
                    SubGameId = 10080;
                #elif BZXX
                    GameName = "百转修仙";
                    GameId = 90002;
                    SubGameId = 324;
                #elif HYXX
                    GameName = "幻域修仙";
                    GameId = 90002;
                    SubGameId = 53;
                #elif XDZZ
                    GameName = "仙道至尊";
                    GameId = 90002;
                    SubGameId = 106;
                #elif XXDJZ
                    GameName = "仙侠大劫主";
                    GameId = 90002;
                    SubGameId = 323;
                #elif XXDZ
                    GameName = "修仙道主";
                    GameId = 90002;
                    SubGameId = 325;
                #elif DTSHY
                    GameName = "大唐山海缘";
                    GameId = 90002;
                    SubGameId = 799;
                #elif YMDL
                    GameName = "妖魔大陆";
                    GameId = 90002;
                    SubGameId = 150000007;
                #elif YJXML
                    GameName = "御剑降魔录";
                    GameId = 90002;
                    SubGameId = 150000008;
                #elif ZYQX
                    GameName = "斩妖奇侠";
                    GameId = 90002;
                    SubGameId = 150000009;
                #elif ZY
                    GameName = "诛妖";
                    GameId = 90002;
                    SubGameId = 150000010;
                #elif DTXXZ
                    GameName = "大唐修仙传";
                    GameId = 90002;
                    SubGameId = 1;
                #elif FBGD
                    GameName = "风暴国度";
                    GameId = 90002;
                    SubGameId = 2;
                #elif TGFS
                    GameName = "太古封神";
                    GameId = 90002;
                    SubGameId = 6327;
                #elif TGFML
                    GameName = "太古伏魔录";
                    GameId = 90002;
                    SubGameId = 6328;
                #elif WDJZ
                    GameName = "武动九州";
                    GameId = 90002;
                    SubGameId = 6333;
                #elif XXHML
                    GameName = "仙侠幻梦录";
                    GameId = 90002;
                    SubGameId = 6010;
                #endif
                PlatId = "ZHONGFU";
                webIP = "sdk.171game.com";
                webPort = "8003";
				isAppleIAP = true;
                isSDKPlat = true;
                return "http://cdn.171game.com/zhongfu/data/";
                }
    #elif YINHE // 中富 银河运营资源
             get {
                #if JZJY
                    GameName = "九州剑雨";
                    GameId = 90002;
                    SubGameId = 11;//14794201675774004271890517145696
                 #elif TZJ
                    GameName = "天之剑";
                    GameId = 90002;
                    SubGameId = 14;//42502198570981479543300619931585
                #elif GJCQ
                    GameName = "古剑苍穹";
                    GameId = 90002;
                    SubGameId = 10;//63620843552925504802633906886083
                #elif TJXK
                    GameName = "天剑侠客";
                    GameId = 90002;
                    SubGameId = 13;//17007992390336531871017804085036
                #elif TGXM
                    GameName = "太古仙盟";
                    GameId = 90002;
                    SubGameId = 12;//82390626298656150197506985273232
                #endif
                PlatId = "ZHONGFU";
                webIP = "sdk.171game.com";
                webPort = "8003";
                isAppleIAP = true;
                isSDKPlat = true;
                return "http://cdn.171game.com/zhongfu/data/";
                }
    #elif YTB // 中富 优托邦运营资源
             get {
                #if STXXZ
                    GameName = "隋唐修仙传";
                    GameId = 90002;
                    SubGameId = 6089;
                 #elif XYXT
                    GameName = "逍遥仙途";
                    GameId = 90002;
                    SubGameId = 6094;
                #elif XXXY
                    GameName = "修仙侠隐";
                    GameId = 90002;
                    SubGameId = 15;
                #endif
                PlatId = "ZHONGFU";
                webIP = "sdk.171game.com";
                webPort = "8003";
                isAppleIAP = true;
                isSDKPlat = true;
                return "http://cdn.171game.com/zhongfu/data/";
                }
    #elif JULIANG // 聚量
             get {
                #if JBSH
                    GameName = "剑霸山河";
                    GameId = 90002;
                    SubGameId = 3;
                #endif
                PlatId = "JULIANG";
                webIP = "sdk.171game.com";
                webPort = "8004";
                isAppleIAP = false;
                isSDKPlat = true;
                IAPPriceUnit=100;
                return "http://cdn.171game.com/juliang/data/";
                }
    #elif SHENHE   // 审核服
             get {
                GameName = "大唐诛仙";
                PlatId = "SHENHE";
                webIP = "sdk.171game.com";
                webPort = "8000";
                return "http://cdn.171game.com/shenhe/data/";
                }
    #else   //XH 迅海外网测试服资源
            get {
                GameName = "大唐诛仙";
                PlatId = "XH";
                webIP = "sdk.171game.com";
                webPort = "8000";
                DebugEngine = true;
                return "http://cdn.171game.com/xh/data/";
                }
    #endif
#else // 本地cdn
        get {
            GameName = "xxx";
            PlatId = "";
            webIP = "192.168.0.200";
            webPort = "8000";
            DebugEngine = true;
            return "http://192.168.0.200/data/";
            }
#endif
        }
        public static string WebUrl = host + plat + "StreamingAssets/";    //各平台资源地址
        

        public static string payURL = "http://sdk.171game.com:9000/iappay/"; // 自己平台 iOS内购服务器上传地址

        // ui ab资源的数据后缀
        public const string uiDataSubfix = "_desc" + ExtName;
        // ui ab资源的资源后缀
        public const string uiResSubfix = "_res" + ExtName;

        public static string FrameworkRoot {
            get {
                return Application.dataPath + "/" + AppName;
            }
        }
    }
}