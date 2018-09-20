#if DONGHAI || YUNYOU
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

        /// <summary>
        /// 如果开启更新模式，前提必须启动框架自带服务器端。
        /// 否则就需要自己将StreamingAssets里面的所有内容
        /// 复制到自己的Webserver上面，并修改下面的WebUrl。
        /// </summary>
        public const bool UpdateMode = !DebugMode;                       //更新模式-默认关闭 
        public const bool LuaBundleMode = !DebugMode;                    //Lua代码AssetBundle模式 (纯lua可运行)
        public const bool LuaByteMode = false;                       //Lua字节码模式-默认关闭 
        public const bool ShowCompanyNameWithTips = !DebugMode;

        public const int TimerInterval = 1;
        public const int GameFrameRate = 30;                        //游戏帧频

        public const string AppName = "LuaFramework";               //应用程序名称
        public const string LuaTempDir = "Lua/";                    //临时目录
        public const string AppPrefix = AppName + "_";              //应用程序前缀
        public const string ExtName = ".unity3d";                   //素材扩展名
        public const string AssetDir = "StreamingAssets";           //素材目录 

#if ONLINE // 线上
    #if UNITY_ANDROID
        public const string plat = "android/";
    #elif (UNITY_IPHONE || UNITY_IOS)
        public const string plat = "ios/";
    #else
        public const string plat = "window/";
    #endif
#else // 调试
    #if UNITY_ANDROID
        public const string plat = "androiddebug/";
    #elif (UNITY_IPHONE || UNITY_IOS)
        public const string plat = "iosdebug/";
    #else
        public const string plat = "window/";
    #endif
#endif


        public static string host
        {
#if ONLINE // 线上cdn
    #if XH // 迅海外网测试服资源
            get {
                GameName = "大唐诛仙";
                PlatId = "XH";
                return "http://cdn.171game.com/xh/data/";
                }
    #elif SHENHE   // 审核服
             get {
                GameName = "大唐诛仙";
                PlatId = "SHENHE";
                return "http://cdn.171game.com/shenhe/data/";
                }
    #elif DONGHAI // 东海运营资源
            get {
                GameName = "修仙魔域";
                PlatId = "DONGHAI";
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
                return "http://cdn.171game.com/yunyou/data/";
                }
    #else
            get {
                GameName = "xxx";
                PlatId = "xxx";
                return "http://127.0.0.1/data/";
                }
    #endif
#else // 本地cdn
        get {
            GameName = "xxx";
            PlatId = "";
            return "http://127.0.0.1/data/";
            }
#endif
        }
        public static string WebUrl = host + plat + "StreamingAssets/";    //各平台资源地址
        

        public static string payURL = "http://sdk.171game.com:9000/iappay/"; // 自己平台 iOS内购服务器上传地址

        public static string UserId = string.Empty;                 //用户ID
        public static int SocketPort = 0;                           //Socket服务器端口
        public static string SocketAddress = string.Empty;          //Socket服务器地址

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