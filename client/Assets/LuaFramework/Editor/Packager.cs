using UnityEditor;
using UnityEngine;
using System.IO;
using System.Text;
using System.Collections;
using System.Collections.Generic;
using System.Diagnostics;
using LuaFramework;

public class Packager {
    // 应该程序版本号(c#代码或dll或者scene改动（不包含edtior下的），相应改动此处序号)，
    // 这个打包时会被打入为 v1.v2.v3.v4 中的v1 表示游戏里要安装最新版本
    static string appVersion = "1"; 

	static List<string> paths = new List<string>();
	static List<string> files = new List<string>();
	static List<AssetBundleBuild> maps = new List<AssetBundleBuild>();

	///-----------------------------------------------------------
	static string[] exts = { ".txt", ".xml", ".lua", ".assetbundle", ".json" };
	static bool CanCopy(string ext) {   //能不能复制
		foreach (string e in exts) {
			if (ext.Equals(e)) return true;
		}
		return false;
	}
    
    /// <summary>
    /// 打包所有的场景资源，此处填场景资源id
    /// </summary>
    static void BuildAllScene(){
        if(AppConst.DebugMode)
            return;
        //*
        // AddSceneABMap("1002");
        // AddSceneABMap("1003");
        // AddSceneABMap("1004");
        AddSceneABMap("1005");

        // AddSceneABMap("1001");

        AddSceneABMap("2001");
        AddSceneABMap("2002");
        AddSceneABMap("2003");
        AddSceneABMap("2004");
        AddSceneABMap("2005");
        // AddSceneABMap("2006");
        AddSceneABMap("2007");
        AddSceneABMap("2008");
        AddSceneABMap("2009");
        AddSceneABMap("2014");

        AddSceneABMap("3001");
        AddSceneABMap("3002");
        AddSceneABMap("3003");
        // AddSceneABMap("3004");
        AddSceneABMap("3006");
        AddSceneABMap("3014");

        AddSceneABMap("4001");
        AddSceneABMap("4002");
        AddSceneABMap("4003");
        AddSceneABMap("4004");
        AddSceneABMap("4005");
        AddSceneABMap("4006");
        AddSceneABMap("4007");
        AddSceneABMap("4008");
        AddSceneABMap("4009");

        AddSceneABMap("5001");
        AddSceneABMap("5002");
        AddSceneABMap("5003");
        AddSceneABMap("5004");
        AddSceneABMap("5005");
        AddSceneABMap("5006");
        AddSceneABMap("5007");
        AddSceneABMap("5008");
        AddSceneABMap("5009");
        AddSceneABMap("5010");
        AddSceneABMap("5011");
        AddSceneABMap("5012");
        AddSceneABMap("5013");
        AddSceneABMap("5014");
        AddSceneABMap("5015");

        AddSceneABMap("6001");
        AddSceneABMap("6002");
        //*/
    }

	/// <summary>
	/// 构建 ab 游戏资源 
	/// </summary>
	static void BuildAssetsBundle()
	{
		BuildNormalAB("Assets/Res/Audio");
		BuildNormalAB("Assets/Res/Prefabs");
		BuildNormalAB("Assets/IGSoft_Resources/Projects/Effect");
        BuildAllUI();
        BuildAllScene();
	}

	/// <summary>
	/// 构建UI AB
	/// </summary>
	static void BuildAllUI()
	{
		string path = "Assets/Res/UI";
		string[] files = Directory.GetFiles(path);
		foreach (var item in files)
		{
			string head = Path.GetFileNameWithoutExtension(item);
			if (head.IndexOf(".") > -1 || head.IndexOf("@") > -1 || item.EndsWith(".meta") || item.Contains(".DS_Store") || item.Contains("说明") || item.ToLower().Contains("readme") )
				continue;
			AddBuildUIABOnMap(head);
		}
	}

	[MenuItem("LuaFramework/Build iPhone Resource", false, 100)]
	public static void BuildiPhoneResource()
    {
#if UNITY_IOS && UNITY_5_3_OR_NEWER
            BuildTarget target;
            target = BuildTarget.iOS;
            BuildAssetResource(target);
#elif UNITY_IPHONE
            BuildTarget target;
		    target = BuildTarget.iPhone;
		    BuildAssetResource(target);
#endif
    }

	[MenuItem("LuaFramework/Build Android Resource", false, 101)]
	public static void BuildAndroidResource() {
#if UNITY_ANDROID 
		BuildAssetResource(BuildTarget.Android);
#endif
	}

	[MenuItem("LuaFramework/Build Windows Resource", false, 102)]
	public static void BuildWindowsResource() {
#if UNITY_STANDALONE_WIN
        BuildAssetResource(BuildTarget.StandaloneWindows);
#elif UNITY_STANDALONE_OSX
        BuildAssetResource(BuildTarget.StandaloneOSXIntel64);
#endif
    }

	[MenuItem("LuaFramework/(win)FUI", false, 104)]
	public static void BuildFUIResource()
	{
		ClearBefore();
		BuildAllUI();
#if UNITY_STANDALONE_WIN
        BuildMapSingle(BuildTarget.StandaloneWindows);
#elif UNITY_STANDALONE_OSX
        BuildMapSingle(BuildTarget.StandaloneOSXIntel64);
#endif
	}
	[MenuItem("LuaFramework/(win)FX-MAKER", false, 105)]
	public static void BuildFXMAKERResource()
	{
		ClearBefore();
        BuildNormalAB("Assets/IGSoft_Resources/Projects/Effect");
#if UNITY_STANDALONE_WIN
        BuildMapSingle(BuildTarget.StandaloneWindows);
#elif UNITY_STANDALONE_OSX
        BuildMapSingle(BuildTarget.StandaloneOSXIntel64);
#endif
	}
	[MenuItem("LuaFramework/(win)Scene", false, 106)]
	public static void BuildsceneResource()
	{
		ClearBefore();
        BuildAllScene();
#if UNITY_STANDALONE_WIN
        BuildMapSingle(BuildTarget.StandaloneWindows);
#elif UNITY_STANDALONE_OSX
        BuildMapSingle(BuildTarget.StandaloneOSXIntel64);
#endif
	}
	[MenuItem("LuaFramework/(win)Icon", false, 107)]
	public static void BuildIconTexturesResource()
	{
		ClearBefore();
		BuildNormalAB("Assets/Res/Icon");
#if UNITY_STANDALONE_WIN
        BuildMapSingle(BuildTarget.StandaloneWindows);
#elif UNITY_STANDALONE_OSX
        BuildMapSingle(BuildTarget.StandaloneOSXIntel64);
#endif
	}
	[MenuItem("LuaFramework/(win)Prefabs", false, 108)]
	public static void BuildPrefabsEffectResource()
	{
		ClearBefore();
		BuildNormalAB("Assets/Res/Prefabs");
#if UNITY_STANDALONE_WIN
        BuildMapSingle(BuildTarget.StandaloneWindows);
#elif UNITY_STANDALONE_OSX
        BuildMapSingle(BuildTarget.StandaloneOSXIntel64);
#endif
	}
	[MenuItem("LuaFramework/(win)Audio", false, 109)]
	public static void BuildAudioResource()
	{
		ClearBefore();
		BuildNormalAB("Assets/Res/Audio");
#if UNITY_STANDALONE_WIN
        BuildMapSingle(BuildTarget.StandaloneWindows);
#elif UNITY_STANDALONE_OSX
        BuildMapSingle(BuildTarget.StandaloneOSXIntel64);
#endif
	}

    [MenuItem("LuaFramework/(Android)FUI", false, 203)]
    public static void BuildAndroidFUIResource()
    {
        ClearBefore();
        BuildAllUI();
#if UNITY_ANDROID 
        BuildMapSingle(BuildTarget.Android);
#endif
    }
    [MenuItem("LuaFramework/(Android)FX-MAKER", false, 204)]
    public static void BuildAndroidFXMAKERResource()
    {
        ClearBefore();
        BuildNormalAB("Assets/IGSoft_Resources/Projects/Effect");
#if UNITY_ANDROID 
        BuildMapSingle(BuildTarget.Android);
#endif
    }
    [MenuItem("LuaFramework/(Android)Scene", false, 205)]
    public static void BuildAndriodSceneResource()
    {
        ClearBefore();
        BuildAllScene();
#if UNITY_ANDROID 
        BuildMapSingle(BuildTarget.Android);
#endif
    }
    [MenuItem("LuaFramework/(Android)Icon", false, 206)]
    public static void BuildAndroidIconTexturesResource()
    {
        ClearBefore();
        BuildNormalAB("Assets/Res/Icon");
#if UNITY_ANDROID 
        BuildMapSingle(BuildTarget.Android);
#endif
    }
    [MenuItem("LuaFramework/(Android)Prefabs", false, 207)]
    public static void BuildAndroidPrefabsEffectResource()
    {
        ClearBefore();
        BuildNormalAB("Assets/Res/Prefabs");
#if UNITY_ANDROID 
        BuildMapSingle(BuildTarget.Android);
#endif
    }
    [MenuItem("LuaFramework/(Android)Audio", false, 208)]
    public static void BuildAndroidAudioResource()
    {
        ClearBefore();
        BuildNormalAB("Assets/Res/Audio");
#if UNITY_ANDROID 
        BuildMapSingle(BuildTarget.Android);
#endif
    }
     [MenuItem("LuaFramework/批量合并替换当前场景重复的Material", false, 210)]
    static private void DeleteSameMaterial()
    {
        Dictionary<string, string> dicMaterial = new Dictionary<string, string>();
        MeshRenderer[] meshRenderers = Resources.FindObjectsOfTypeAll<MeshRenderer>();
        string rootPath = Directory.GetCurrentDirectory();
        for (int i = 0; i < meshRenderers.Length; i++)
        {
            MeshRenderer meshRender = meshRenderers[i];
            Material[] newMaterials = new Material[meshRender.sharedMaterials.Length];
            for (int j = 0; j < meshRender.sharedMaterials.Length; j++)
            {
                Material m = meshRender.sharedMaterials[j];

                string mPath = AssetDatabase.GetAssetPath(m);
                if (!string.IsNullOrEmpty(mPath) && mPath.Contains("Assets/"))
                {
                    string fullPath = Path.Combine(rootPath, mPath);
                    string text = File.ReadAllText(fullPath).Replace(" m_Name: " + m.name, "");

                    string change;
                    if (!dicMaterial.TryGetValue(text, out change))
                    {
                        dicMaterial[text] = mPath;
                        change = mPath;
                        UnityEngine.Debug.Log("改变：" + mPath);
                    }
                    newMaterials[j] = AssetDatabase.LoadAssetAtPath<Material>(change);
                }
            }
            meshRender.sharedMaterials = newMaterials;
        }
    }

     //用于本地测试单独打包清除之前的
     static void ClearBefore()
     {
         string streamPath = Application.streamingAssetsPath;
         if (!Directory.Exists(streamPath))
             Directory.CreateDirectory(streamPath);
         AssetDatabase.Refresh();
         maps.Clear();
     }
    // 单独打包生成独里map
     static void BuildMapSingle(BuildTarget target)
     {
         string resPath = "Assets/" + AppConst.AssetDir;
         BuildAssetBundleOptions options = BuildAssetBundleOptions.ChunkBasedCompression | BuildAssetBundleOptions.DeterministicAssetBundle;
         BuildPipeline.BuildAssetBundles(resPath, maps.ToArray(), options, target);
         AssetDatabase.Refresh();
     }

	/// <summary>
	/// 生成绑定素材
	/// </summary>
	public static void BuildAssetResource(BuildTarget target) {
		if (Directory.Exists(Util.DataPath)) {
			Directory.Delete(Util.DataPath, true);
		}
		string streamPath = Application.streamingAssetsPath;
		if (Directory.Exists(streamPath)) {
			Directory.Delete(streamPath, true);
		}
		Directory.CreateDirectory(streamPath);
		AssetDatabase.Refresh();

		maps.Clear();
		if (AppConst.LuaBundleMode) {
			HandleLuaBundle();
		} else {
			HandleLuaFile();
		}

		BuildAssetsBundle();
		
		string resPath = "Assets/" + AppConst.AssetDir;

        BuildAssetBundleOptions options = BuildAssetBundleOptions.ChunkBasedCompression | BuildAssetBundleOptions.DeterministicAssetBundle;

		BuildPipeline.BuildAssetBundles(resPath, maps.ToArray(), options, target);
		BuildFileIndex();

		string streamDir = (Application.dataPath + "/" + AppConst.LuaTempDir).Replace("\\", "/");
		if (Directory.Exists(streamDir)) Directory.Delete(streamDir, true);
		AssetDatabase.Refresh();
	}

	/// 资源打包
	public static void AddBuildMap(string bundleName, string pattern, string path) {
		string[] files = Directory.GetFiles(path, pattern);// 获取路径path 下 pattern的所有文件 string[] files = Directory.GetFiles(path, "(*.mp3|*.ogg)");
		UnityEngine.Debug.Log(path + pattern);
		if (files.Length == 0) return;

		for (int i = 0; i < files.Length; i++) {
			files[i] = files[i].Replace('\\', '/');
			UnityEngine.Debug.Log(files[i]);
		}
		AssetBundleBuild build = new AssetBundleBuild();
		build.assetBundleName = bundleName;
		build.assetNames = files;
		maps.Add(build);
	}

	#region ui
	/// Fui资源打包 FairyGUI.UIPackage.AddPackage(ab_desc, ab_res) => byte , @sprite.byte + png + alets
	static void AddBuildUIABOnMap(string moduleName)
	{
		_BuildUIABOnMap(moduleName, true);
		_BuildUIABOnMap(moduleName, false);
	}
	static void _BuildUIABOnMap (string moduleName, bool isData)
	{
		string uiPath = "Assets/Res/UI";
		AssetBundleBuild build = new AssetBundleBuild ();
		string[] sources = Directory.GetFiles (uiPath);
		List<string> list = new List<string> ();
		if (isData) {
			foreach (var item in sources) {
				if (item.EndsWith(".meta") || item.Contains(".DS_Store")) continue;
				if (item.IndexOf (moduleName + ".bytes") != -1) {
					list.Add (item.Replace ('\\', '/'));
					break;
				}
			}
			build.assetBundleName = "ui/"+moduleName.ToLower() + AppConst.uiDataSubfix;
		} else {
			foreach (var item in sources) {
				if (item.EndsWith(".meta") || item.Contains(".DS_Store")) continue;
				if (item.IndexOf (moduleName + "@sprites.bytes") != -1 || item.IndexOf(moduleName + "@atlas") != -1) {
					list.Add (item.Replace ('\\', '/'));
					continue;
				}
			}
			build.assetBundleName = "ui/"+moduleName.ToLower() + AppConst.uiResSubfix;
		}
		if(list.Count == 0)return;
		build.assetNames = list.ToArray();
		maps.Add(build);
	}
	#endregion

	#region 场景资源
	static void AddSceneABMap(string sceneID)
	{
		string[] files = { string.Format("Assets/scene/{0}.unity", sceneID) };
		if (files.Length == 0) return;
		for (int i = 0; i < files.Length; i++)
		{
			files[i] = files[i].Replace('\\', '/');
		}
		AssetBundleBuild build = new AssetBundleBuild();
		build.assetBundleName = string.Format("scene/{0}", sceneID) + AppConst.ExtName;
		build.assetNames = files;
		maps.Add(build);
	}
	#endregion

	#region 遍历指定目录下的所有文件生成单个AB
	/// 普通AB 单个生成
	static void BuildNormalAB(string path)
	{
		string root = path.Replace('\\', '/');

		List<string> tmp = new List<string>();
		tmp.Add(path);
		GetAllDirList(path, tmp);
		string head = Path.GetFileNameWithoutExtension(root);
		foreach (var item in tmp)
		{
			// Debugger.Log(item + " | " + head);
			string[] files = Directory.GetFiles(item);
			foreach (string f in files)
			{
				string p = f.Replace('\\', '/');
                if (p.EndsWith(".meta") || p.Contains(".DS_Store") || p.Contains("说明") 
                    || p.ToLower().Contains("readme") || p.ToLower().Contains("Build.bat") 
                    || p.ToLower().Contains("protoc-gen-lua-tools.exe")) continue;

				string assetName = p.Substring(p.LastIndexOf(root));
				string bundleName = head + "/" + p.Substring(p.LastIndexOf(root)).Replace(root + "/", "").Replace(Path.GetExtension(p), "") + AppConst.ExtName;
                //Util.Log(assetName + " || " + bundleName + "  " + head + "  " + root);
				AddSingleBuildMap(bundleName, assetName);
			}
		}
	}
	//获取所有目录及子目录
	static void GetAllDirList(string strBaseDir, List<string> list)
	{
		if (list == null) return;
		DirectoryInfo di = new DirectoryInfo(strBaseDir);
		DirectoryInfo[] diA = di.GetDirectories();
		for (int i = 0; i < diA.Length; i++)
		{
			list.Add(diA[i].FullName); //diA[i].FullName是某个子目录的绝对地址，把它记录在List<string>中
			GetAllDirList(diA[i].FullName, list);
		}
	}
	//生成单个AB文件
	static void AddSingleBuildMap(string bundleName, string assetName)
	{
		assetName = assetName.Replace('\\', '/');
		AssetBundleBuild build = new AssetBundleBuild();
		build.assetBundleName = bundleName.ToLower();
		build.assetNames = new string[] { assetName };
		maps.Add(build);
	}
	#endregion

	/// <summary>
	/// 处理Lua代码包
	/// </summary>
	static void HandleLuaBundle() {
		string streamDir = Application.dataPath + "/" + AppConst.LuaTempDir;
		if (!Directory.Exists(streamDir)) Directory.CreateDirectory(streamDir);

		string[] srcDirs = { CustomSettings.luaDir, CustomSettings.FrameworkPath + "/ToLua/Lua" };
		for (int i = 0; i < srcDirs.Length; i++) {
			if (AppConst.LuaByteMode) {
				string sourceDir = srcDirs[i];
				string[] files = Directory.GetFiles(sourceDir, "*.lua", SearchOption.AllDirectories);
				int len = sourceDir.Length;

				if (sourceDir[len - 1] == '/' || sourceDir[len - 1] == '\\') {
					--len;
				}
				for (int j = 0; j < files.Length; j++) {
					string str = files[j].Remove(0, len);
					string dest = streamDir + str + ".bytes";
					string dir = Path.GetDirectoryName(dest);
					Directory.CreateDirectory(dir);
					EncodeLuaFile(files[j], dest);
				}	
			} else {
				ToLuaMenu.CopyLuaBytesFiles(srcDirs[i], streamDir);
			}
		}
		string[] dirs = Directory.GetDirectories(streamDir, "*", SearchOption.AllDirectories);
		for (int i = 0; i < dirs.Length; i++) {
			string name = dirs[i].Replace(streamDir, string.Empty);
			name = name.Replace('\\', '_').Replace('/', '_');
			name = "lua/lua_" + name.ToLower() + AppConst.ExtName;

			string path = "Assets" + dirs[i].Replace(Application.dataPath, "");
			AddBuildMap(name, "*.bytes", path);
		}
		AddBuildMap("lua/lua" + AppConst.ExtName, "*.bytes", "Assets/" + AppConst.LuaTempDir);

		//-------------------------------处理非Lua文件----------------------------------
		string luaPath = AppDataPath + "/StreamingAssets/lua/";
		for (int i = 0; i < srcDirs.Length; i++) {
			paths.Clear(); files.Clear();
			string luaDataPath = srcDirs[i].ToLower();
			Recursive(luaDataPath);
			foreach (string f in files) {
				if (f.EndsWith(".meta") || f.EndsWith(".lua")) continue;
				string newfile = f.Replace(luaDataPath, "");
				string path = Path.GetDirectoryName(luaPath + newfile);
				if (!Directory.Exists(path)) Directory.CreateDirectory(path);

				string destfile = path + "/" + Path.GetFileName(f);
				File.Copy(f, destfile, true);
			}
		}
		AssetDatabase.Refresh();
	}

	/// <summary>
	/// 处理Lua文件
	/// </summary>
	static void HandleLuaFile() {
		string resPath = AppDataPath + "/StreamingAssets/";
		string luaPath = resPath + "/lua/";

		//----------复制Lua文件----------------
		if (!Directory.Exists(luaPath)) {
			Directory.CreateDirectory(luaPath); 
		}
		string[] luaPaths = { AppDataPath + "/LuaFramework/lua/", 
							  AppDataPath + "/LuaFramework/Tolua/Lua/" };

		for (int i = 0; i < luaPaths.Length; i++) {
			paths.Clear(); files.Clear();
			string luaDataPath = luaPaths[i].ToLower();
			Recursive(luaDataPath);
			int n = 0;
			foreach (string f in files) {
				if (f.EndsWith(".meta")) continue;
				string newfile = f.Replace(luaDataPath, "");
				string newpath = luaPath + newfile;
				string path = Path.GetDirectoryName(newpath);
				if (!Directory.Exists(path)) Directory.CreateDirectory(path);

				if (File.Exists(newpath)) {
					File.Delete(newpath);
				}
				if (AppConst.LuaByteMode) {
					EncodeLuaFile(f, newpath);
				} else {
					File.Copy(f, newpath, true);
				}
				UpdateProgress(n++, files.Count, newpath);
			} 
		}
		EditorUtility.ClearProgressBar();
		AssetDatabase.Refresh();
	}

	/// <summary>
	/// 生成识别资源路径
	/// </summary>
	static void BuildFileIndex() {
		string resPath = AppDataPath + "/StreamingAssets/";
		string newFilePath = resPath + "/files.txt";//创建版本文件列表
		paths.Clear();
		files.Clear();
		if (File.Exists(newFilePath)) File.Delete(newFilePath);
        paths.Clear(); files.Clear();
        Recursive(resPath);

		FileStream fs = new FileStream(newFilePath, FileMode.CreateNew);
		StreamWriter sw = new StreamWriter(fs);
		string luaAB = "";//用于处理 lua.ab 与 lua.ab.manifest 序列成同一个md5
		string hashCode = "";
		for (int i = 0; i < files.Count; i++) {
			string file = files[i];
			string ext = Path.GetExtension(file);
			if (ext.Equals(".meta") || ext.Equals(".svn") || ext.Equals(".txt") 
				|| ext.Contains(".DS_Store") || ext.Contains(".exe") || ext.Contains(".bat")) continue;
			string md5 = "";
			if (file.IndexOf("lua/") == -1)
			{
				md5 = Util.md5file(file);
			}else{
				if (luaAB !="" && file.IndexOf(luaAB) != -1)
				{
					md5 = hashCode;
				}
				else
				{
					md5 = Util.md5file(file);
					luaAB = file;
					hashCode = md5;
				}
			}
			string value = file.Replace(resPath, string.Empty);
			sw.WriteLine(value + "|" + md5);
		}
		//格式： v1.v2.v3.v4 其中 vx代表序号
		//版号表示：v1程序更新(全部)，v2（非UI）资源, v3 UI资源， v4 lua脚本
		sw.WriteLine(appVersion+".0.0.0");
		sw.Close(); fs.Close();
	}

	/// 数据目录
	public static string AppDataPath {
		get { return Application.dataPath.ToLower(); }
	}

	// 遍历目录及其子目录
	static void Recursive(string path) {
		string[] names = Directory.GetFiles(path);
		string[] dirs = Directory.GetDirectories(path);
		foreach (string filename in names) {
			string ext = Path.GetExtension(filename);// 扩展名
            if (ext.Equals(".meta") || ext.Equals(".svn") || ext.Equals(".txt")
                || ext.Contains(".DS_Store") || ext.Contains(".exe") || ext.Contains(".bat")) continue;
			files.Add(filename.Replace('\\', '/'));
		}
		foreach (string dir in dirs) {
			paths.Add(dir.Replace('\\', '/'));
			Recursive(dir);
		}
	}

	static void UpdateProgress(int progress, int progressMax, string desc) {
		string title = "Processing...[" + progress + " - " + progressMax + "]";
		float value = (float)progress / (float)progressMax;
		EditorUtility.DisplayProgressBar(title, desc, value);
	}

	public static void EncodeLuaFile(string srcFile, string outFile) {
		if (!srcFile.ToLower().EndsWith(".lua")) {
			File.Copy(srcFile, outFile, true);
			return;
		}
		bool isWin = true;
		string luaexe = string.Empty;
		string args = string.Empty;
		string exedir = string.Empty;
		string currDir = Directory.GetCurrentDirectory();
		if (Application.platform == RuntimePlatform.WindowsEditor) {
			isWin = true;
			luaexe = "luajit.exe";
			args = "-b " + srcFile + " " + outFile;
			exedir = AppDataPath.Replace("assets", "") + "LuaEncoder/luajit/";
		} else if (Application.platform == RuntimePlatform.OSXEditor) {
			isWin = false;
			luaexe = "./luac";
			args = "-o " + outFile + " " + srcFile;
			exedir = AppDataPath.Replace("assets", "") + "LuaEncoder/luavm/";
		}
		Directory.SetCurrentDirectory(exedir);
		ProcessStartInfo info = new ProcessStartInfo();
		info.FileName = luaexe;
		info.Arguments = args;
		info.WindowStyle = ProcessWindowStyle.Hidden;
		info.ErrorDialog = true;
		info.UseShellExecute = isWin;
		Util.Log(info.FileName + " " + info.Arguments);

		Process pro = Process.Start(info);
		pro.WaitForExit();
		Directory.SetCurrentDirectory(currDir);
	}

	[MenuItem("LuaFramework/Build Protobuf-lua-gen File")]
	public static void BuildProtobufFile()
	{
        //string dir = AppDataPath.Replace("assets", "LuaProto/proto"); //AppDataPath + "/LuaFramework/Lua/3rd/pblua";//proto路径
        string root = AppDataPath.Substring(0, AppDataPath.Substring(0, AppDataPath.IndexOf("/assets")).LastIndexOf("/")+1);
        string dir = root + "parseproto/src/file/xhgame/proto";
		UnityEngine.Debug.Log(dir);  
		paths.Clear();
		files.Clear();
		Recursive(dir);

		string protoc = AppDataPath.Replace("assets", "luaproto") + "/protoc.exe";
		string protoc_gen_dir = string.Format("\"" + AppDataPath.Replace("assets", "luaproto") + "/protoc-gen-lua/plugin/protoc-gen-lua.bat\""); //"\"d:/protoc-gen-lua/plugin/protoc-gen-lua.bat\"";
		string outPath = AppDataPath + "/LuaFramework/Lua/SKGame/Proto"; //"./";
		foreach (string f in files)
		{
			string name = Path.GetFileName(f);
			string ext = Path.GetExtension(f);
			//UnityEngine.Debug.Log(name + "   " + ext);
			if (!ext.Equals(".proto")) continue;

			ProcessStartInfo info = new ProcessStartInfo();
			info.FileName = protoc;
			info.Arguments = string.Format(" --lua_out=\"{0}\" --plugin=protoc-gen-lua={1} {2}", outPath, protoc_gen_dir, name);
			//" --lua_out=\"c:/work\" --plugin=protoc-gen-lua=" + protoc_gen_dir + " " + name;
			info.WindowStyle = ProcessWindowStyle.Hidden;
			info.UseShellExecute = true;
			info.WorkingDirectory = dir;
			info.ErrorDialog = true;
			Util.Log(info.FileName + " " + info.Arguments);

			Process pro = Process.Start(info);
			pro.WaitForExit();
		}
		AssetDatabase.Refresh();
	}
}