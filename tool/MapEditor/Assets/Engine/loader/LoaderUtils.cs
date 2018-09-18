using UnityEngine;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Reflection;

namespace GEngine{
	public class LoaderUtils {

		/// <summary>
		/// 加载器类型
		/// </summary>
		private static Dictionary<string, Type> _loadTypes;

		/// <summary>
		/// 缓存加载数据
		/// </summary>
		public static bool cacheLoadInfo(Dictionary<string, QueueLoader.LoadIngInfo> cachesLoadIngs, string assetPath, string dataFormat = QueueLoader.ASSETS_BUNDLE, BaseLoader.OnLoadComplete onComplete = null, object loadComplemeArgs = null, BaseLoader.OnLoadError onLoadError = null, object loadErrorArgs = null, BaseLoader.OnLoadProgress onLoadProgress = null, object loadProgressArgs = null) {
			QueueLoader.LoadIngInfo loadingInfo = null;

			if (cachesLoadIngs.ContainsKey(assetPath) == false) {
				loadingInfo = new QueueLoader.LoadIngInfo ();
				loadingInfo.assetPath = assetPath;
				loadingInfo.dataFormate = dataFormat;

				//回调方法
				loadingInfo.onLoadError = new List<BaseLoader.OnLoadError>();
				loadingInfo.onComplete = new List<BaseLoader.OnLoadComplete>();
				loadingInfo.onLoadProgress = new List<BaseLoader.OnLoadProgress>();

				//回调方法
				loadingInfo.onLoadError.Add(onLoadError);
				loadingInfo.onComplete.Add(onComplete);
				loadingInfo.onLoadProgress.Add(onLoadProgress);

				//回调参数dictionary
				loadingInfo.onErrorArgs = new List<object>();
				loadingInfo.onCompleteArgs =  new List<object>();
				loadingInfo.onProgressArgs =  new List<object>();

				//当前的回调参数
				loadingInfo.onErrorArgs.Add (loadErrorArgs);
				loadingInfo.onCompleteArgs.Add (loadComplemeArgs);
				loadingInfo.onProgressArgs.Add (loadProgressArgs);

				cachesLoadIngs.Add (assetPath, loadingInfo);

				return false;
			} else {
				loadingInfo = cachesLoadIngs [assetPath];;

				//当前的回调参数
				loadingInfo.onErrorArgs.Add (loadErrorArgs);
				loadingInfo.onCompleteArgs.Add (loadComplemeArgs);
				loadingInfo.onProgressArgs.Add (loadProgressArgs);

				//回调方法
				loadingInfo.onLoadError.Add(onLoadError);
				loadingInfo.onComplete.Add(onComplete);
				loadingInfo.onLoadProgress.Add(onLoadProgress);

				return true;
			}
		}




		/// <summary>
		/// 从队列中获取正在加载中的数据
		/// </summary>
		/// <returns>The loadinf info.</returns>
		public static QueueLoader.LoadIngInfo getLoadinfInfo(Dictionary<string, QueueLoader.LoadIngInfo> cachesLoadIngs) {
			Dictionary<string, QueueLoader.LoadIngInfo>.ValueCollection valueCol = cachesLoadIngs.Values;
			if (valueCol == null) {
				return null;
			}

			foreach (QueueLoader.LoadIngInfo loadIngInfo in valueCol) {
				if (loadIngInfo.isLoading == false) {
					return loadIngInfo;
				}
			}
			return null;
		}

		/// <summary>
		/// 创建加载类
		/// </summary>
		/// <returns>The loader.</returns>
		/// <param name="loadIngInfo">Load ing info.</param>
		public static BaseLoader createLoader(QueueLoader.LoadIngInfo loadIngInfo, GameObject gameObject) {
			if (loadIngInfo == null || loadIngInfo.dataFormate == null) {
				return null;
			}

			if (loadTypes.ContainsKey (loadIngInfo.dataFormate) == false) {
				return null;
			}

			Type loaderType = loadTypes[loadIngInfo.dataFormate];
			if (loaderType == null) {
				return null;
			}
			BaseLoader resultLoader = gameObject.AddComponent(loaderType) as BaseLoader;//;Activator.CreateInstance (loaderType.GetType());

			return resultLoader;
		}

		public static Dictionary<string, Type> loadTypes {
			get {
				if (_loadTypes == null) {
					_loadTypes = new Dictionary<string, Type> ();

					_loadTypes.Add (QueueLoader.TEXT, typeof(CustomLoader));
					_loadTypes.Add (QueueLoader.BINARY, typeof(CustomLoader));
					_loadTypes.Add (QueueLoader.PREFAB, typeof(CustomResourceLoader));
					_loadTypes.Add (QueueLoader.ASSETS_BUNDLE, typeof(CustomLoader));
					_loadTypes.Add (QueueLoader.TTF, typeof(CustomLoader));
					_loadTypes.Add (QueueLoader.AUDIO, typeof(CustomLoader));
					_loadTypes.Add (QueueLoader.UNITY_3D, typeof(CustomLoader));
					_loadTypes.Add (QueueLoader.REMOTE_ASSETS, typeof(CustomRemoteLoader));
				}
				return _loadTypes;
			}
		}
	}
}
