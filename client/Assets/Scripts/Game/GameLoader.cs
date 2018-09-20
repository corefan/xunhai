#region 说明
// 功  能： GameLoader.cs
// 描  述： 为游戏程序最开始加载进入时的【场景】，对版本的检查及资源加载【准备｜更新】，---相当于游戏的程序壳
// 时  间： 2016-06-30 17:59:15
// 作  者： zwx
// E-mail： zhuang_wx@qq.com
// 项目名： SKGame
#endregion

using UnityEngine;
using FairyGUI;
using LuaFramework;
//using System;

/// <summary>
/// 为游戏程序最开始加载进入时的【场景】，对版本的检查及资源加载【准备｜更新】，---相当于游戏的程序壳
/// </summary>
public class GameLoader : MonoSingleton<GameLoader>
{
	// 是否已经初始化
	bool isInited = false;
	void Awake()
	{
		if (Instance != null && Instance.isInited) return;
		Init();
		isInited = true;
	}

	// 对相关的单例与要初始化的数值在这里进行，最后进行版本检查加载
	private void Init()
	{
		Application.targetFrameRate = 60;

		AsyncTextureConst.AppName = LuaFramework.AppConst.AppName;
		AsyncTextureConst.AssetDir = LuaFramework.AppConst.AssetDir;
		AsyncTextureConst.DebugMode = LuaFramework.AppConst.DebugMode;
		AsyncTextureConst.ExtName = LuaFramework.AppConst.ExtName;
		
		Stage.Instantiate();
#if UNITY_STANDALONE_WIN || UNITY_STANDALONE_OSX || UNITY_EDITOR
	CopyPastePatch.Apply();
#endif
		// uiRoot container
		UIPackage.AddPackage("UIRoot/Root");
		LayersMgr.CreateInstance().CreateFUI("UIRoot", "Root");
		//UIObjectFactory.SetLoaderExtension(typeof(GSprite));
		UIConfig.inputHighlightColor = new Color(0x2e, 0x33, 0x41);
		UIConfig.defaultFont = "方正粗圆简体";//"SIMYOU,Microsoft YaHei";
		// 产品版本一般不启动 -> 启动异常捕获 (在发布设置 > 预编译加上 XH 或者为 AppConst.DebugMode)
#if XH
		ListonException();
		return;
#endif
		if(AppConst.DebugMode)
			ListonException();
	}

	void Start()
	{
        // print(VersionUtil.GetTime());
        // if (VersionUtil.GetTime() > 1550378061000)//时间超过，进不了程序
        //     Application.Quit();
		Application.runInBackground = true;//保持后台运行
		Debug.Log("client::start()" + "  使用wifi无线：" + Util.IsWifi + "  网络可用状态：" + Util.NetAvailable); //当网络不可用时
		AppFacade.Instance.StartUp();//启动游戏
	}

	#region 报错收集与显示

	/// 游戏运行时的日志bug信息， ［开发时开启＝>监听严重错误时的收集］
	void ListonException()
	{
		ShowFPS sFPS = gameObject.AddComponent<ShowFPS>();
		Application.logMessageReceived += OnLog;
	}

	void OnLog(string message, string stacktrace, LogType type)
	{
		if (LogType.Exception == type || LogType.Error == type){
			ErrorWindow.ShowError(message + "\n" + stacktrace + "\n");
		}
	}

	//void OnApplicationPause(bool pauseStatus)
	//{
	//	Debug.Log("App -->暂停  " + pauseStatus);
	//	if(GameManager.initialize)
	//	  Util.CallMethod("GameLoader", "OnApplicationPause", pauseStatus);
	//}
	void OnApplicationFocus(bool focusStatus)
	{
		if(GameManager.initialize)
			Util.CallMethod("GameLoader", "OnApplicationFocus", focusStatus);
		// if (focusStatus)
		// {
		//	 byte[] bs = new byte[1000000];
		//	 for (int i = 0; i < 1000000; i++)
		//	 {
		//		 bs[i] = (byte)Util.Random(0, 1);
		//	 }
		//	 byte[] cp = Ionic.Zlib.ZlibStream.CompressBuffer(bs);
		//	 Debug.Log(bs.Length+"   "+cp.Length);
		//	 byte[] cp2 = Ionic.Zlib.DeflateStream.CompressBuffer(bs);
		//	 Debug.Log(bs.Length + "   " + cp2.Length);
		//	 byte[] cp3 = Ionic.Zlib.GZipStream.CompressBuffer(bs);
		//	 Debug.Log(bs.Length + "   " + cp3.Length);

		//	 Util.CallMethod("GameLoader", "GetBytes", cp3);
		// }
	}

	#endregion
}

