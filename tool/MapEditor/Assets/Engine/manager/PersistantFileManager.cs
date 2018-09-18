using System;
using UnityEngine;
using System.IO;

namespace GEngine{


	/// <summary>
	/// 持久化文件管理类,,,
	/// </summary>
	public class PersistantFileManager {

		public const char FILE_SPLIT = '/';

		/// <summary>
		/// 持久化路径
		/// 持久化路径是,,,,Application. persistentDataPath + 原路径
		/// </summary>
		public static readonly string PersistentPath = PathManager.GetPersisitentDataPath("");

		/// <summary>
		/// 不同平台下的资源位置
		/// </summary>
		public static readonly string PathURL = PathManager.DataRoot;

		public static string REMOTE_SOURCE_PATH = "";


		/// <summary>
		/// 根据当前缓存中是否有数据，如果有数据就从缓存中拿取数据，如果没有的话，就直接加载本地数据
		/// </summary>
		/// <returns>The URL.</returns>
		/// <param name="nativeUrl">Native URL.</param>
		public static string getLocalSourcePath(string sourcePath) {
			//缓存中有数据直接返回缓存目录地址
			if (fileExist (sourcePath) == true) {
				return PersistentPath + sourcePath;
			}
			MonoBehaviour.print ("不同平台下的资源位置" + PathURL);
			//缓存中没有数据，直接返回原始路径
			return PathURL + sourcePath;
		}

		/// <summary>
		/// Resources下面的目录在模块内是否有缓存，如果有，就直接返回
		/// </summary>
		/// <returns>The resource path.</returns>
		/// <param name="nativeUrl">Native URL.</param>
		public static string getRemoteResourcePath(string url) {
			//缓存中有数据直接返回缓存目录地址
//			if (fileExist (url) == true) {
//				return url;
//			}
			//缓存中没有数据，直接返回原始路径
			return REMOTE_SOURCE_PATH + url;
		}


		/// <summary>
		/// 缓存中是否存在文件
		/// 如果文件目录为xxxx/xxxxx/xxxxxx/xxx.data缓存目录就为PersistentPath/xxxx/xxxxx/xxxxxx/xxx.data
		/// </summary>
		/// <returns><c>true</c>, if exist was filed, <c>false</c> otherwise.</returns>
		/// <param name="dataUrl">Data URL.</param>
		public static bool fileExist(string dataUrl) {
			if(string.IsNullOrEmpty(dataUrl) == true) 
				return false;
			FileInfo fileInfo = null;
			string currentFilePath = PersistentPath;
			string[] files = dataUrl.Split (new char[]{FILE_SPLIT});

			for (int index = 0; index < files.Length; index++) {

				if (string.IsNullOrEmpty (files [index]) == true) {
					continue;
				}
				currentFilePath = currentFilePath + FILE_SPLIT + files[index];//

				if (index == files.Length - 1) {
					//文件存在
					fileInfo = new FileInfo(currentFilePath);
					if (fileInfo.Exists == true) {
						return true;
					}
				} else {
					//创建文件夹
					if (Directory.Exists (currentFilePath) == false) {
						return false;
					}
				}
			}
			return true;
		}
	}
}

