public static class PathManager {
	/// <summary>
	///  不同平台下的资源位置
	/// </summary>
	private static string _DataRoot = null;

	/// <summary>
	/// 持久化路径
	/// </summary>
	private static string _PersistentDataPath;


	public static string DataRoot {
		get {
			return _DataRoot;
		}
		set {
			_DataRoot = value;
		}
	}

	public static string PersistentDataPath {
		get {
			return _PersistentDataPath;
		}
		set {
			_PersistentDataPath = value;
		}
	}


	/// <summary>
	/// 获得 Application.persisitentDataPath 下的路径
	/// </summary>
	/// <returns>The persisitent data path.</returns>
	/// <param name="url">URL.</param>
	public static string GetPersisitentDataPath(string url){
		return PersistentDataPath + "/" + url;
	}


}
