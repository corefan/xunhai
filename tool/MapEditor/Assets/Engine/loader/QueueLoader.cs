using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace GEngine{

	/// <summary>
	/// 资源队列加载器，同一时刻只能有特定数量的资源在加载，如果多于这个数量，则只会进行排队加载
	/// </summary>
	[ExecuteInEditMode]
	public class QueueLoader : MonoSingleton<QueueLoader> {

		public bool isInMapEditor;

		/// 加载数据类型为text
		public const string TEXT = "Text";

		/// 加载二进制数据
		public const string BINARY = "Binary";

		/// 加载prefab
		public const string PREFAB = "Prefab";

		/// 加载assetbundle
		public const string ASSETS_BUNDLE = "Assetbundle";

		/// 加载字体
		public const string TTF = "TTF";

		/// 加载字体
		public const string AUDIO = "Audio";

		/// 加载字体
		public const string UNITY_3D = "UNITY_3D";

		/// 加载远程服务器上的数据
		public const string REMOTE_ASSETS = "REMOTE_ASSETS";

		/// 正在加载中的资源数量
		private int currentLoadingNumber;

		/// 加载中的资源
		private Dictionary<string, LoadIngInfo> _cachesLoadIngs;


		/// <summary>
		/// 加载各种资源，
		/// 	包括prefab预制，只能是Resource路径下的资源,加载本地的Resources资源，路径前面不用/
		/// 	audio声效,
		/// 	assetsBundle本地资源和服务器资源,
		/// 	ttf字体,
		/// 	binary本地资源和网络资源
		/// </summary>
		/// <param name="assetPath">资源路径</param>
		/// <param name="dataFormat">数据格式，默认是assetbundle</param>
		/// <param name="onComplete">加载成功回调函数</param>
		/// <param name="loadComplemeArgs">回载成功后的回调函数参数</param>
		/// <param name="onLoadError">加载失败回调函数</param>
		/// <param name="loadErrorArgs">回载失败的回调函数参数</param>
		/// <param name="onLoadProgress">显示进度回调函数</param>
		/// <param name="loadProgressArgs">显示进度时的回调参数</param>
		public void loadAssetsInEditor(string assetPath, string dataFormat = ASSETS_BUNDLE, BaseLoader.OnLoadComplete onComplete = null, object loadComplemeArgs = null, BaseLoader.OnLoadError onLoadError = null, object loadErrorArgs = null, BaseLoader.OnLoadProgress onLoadProgress = null, object loadProgressArgs = null) {
			this.isInMapEditor = true;
			//判断缓存中有没有资源，如果有就直接返回
			BaseLoader.LoadVo loadVo = CacheManager.Instance.getCacheLoadedObject (assetPath);
			if (loadVo != null) {
				StartCoroutine (DispatchSuccess (onComplete, loadVo, loadComplemeArgs));
				return;
			}

			this.doLoadAsset (assetPath, dataFormat, onComplete, loadComplemeArgs, onLoadError, loadErrorArgs, onLoadProgress, loadProgressArgs);
		}

		/// <summary>
		/// 加载各种资源，
		/// 	包括prefab预制，只能是Resource路径下的资源,加载本地的Resources资源，路径前面不用/
		/// 	audio声效,
		/// 	assetsBundle本地资源和服务器资源,
		/// 	ttf字体,
		/// 	binary本地资源和网络资源
		/// </summary>
		/// <param name="assetPath">资源路径</param>
		/// <param name="dataFormat">数据格式，默认是assetbundle</param>
		/// <param name="onComplete">加载成功回调函数</param>
		/// <param name="loadComplemeArgs">回载成功后的回调函数参数</param>
		/// <param name="onLoadError">加载失败回调函数</param>
		/// <param name="loadErrorArgs">回载失败的回调函数参数</param>
		/// <param name="onLoadProgress">显示进度回调函数</param>
		/// <param name="loadProgressArgs">显示进度时的回调参数</param>
		public void loadAssets(string assetPath, string dataFormat = ASSETS_BUNDLE, BaseLoader.OnLoadComplete onComplete = null, object loadComplemeArgs = null, BaseLoader.OnLoadError onLoadError = null, object loadErrorArgs = null, BaseLoader.OnLoadProgress onLoadProgress = null, object loadProgressArgs = null) {
			this.isInMapEditor = false;
			//判断缓存中有没有资源，如果有就直接返回
			BaseLoader.LoadVo loadVo = CacheManager.Instance.getCacheLoadedObject (assetPath);
			if (loadVo != null) {
				StartCoroutine (DispatchSuccess (onComplete, loadVo, loadComplemeArgs));
				return;
			}

			this.doLoadAsset (assetPath, dataFormat, onComplete, loadComplemeArgs, onLoadError, loadErrorArgs, onLoadProgress, loadProgressArgs);
		}




		/// <summary>
		/// 加载各种资源，
		/// 	包括prefab预制，只能是Resource路径下的资源,加载本地的Resources资源，路径前面不用/
		/// 	audio声效,
		/// 	assetsBundle本地资源和服务器资源,
		/// 	ttf字体,
		/// 	binary本地资源和网络资源
		/// </summary>
		/// <param name="assetPath">资源路径</param>
		/// <param name="dataFormat">数据格式，默认是assetbundle</param>
		/// <param name="onComplete">加载成功回调函数</param>
		/// <param name="loadComplemeArgs">回载成功后的回调函数参数</param>
		/// <param name="onLoadError">加载失败回调函数</param>
		/// <param name="loadErrorArgs">回载失败的回调函数参数</param>
		/// <param name="onLoadProgress">显示进度回调函数</param>
		/// <param name="loadProgressArgs">显示进度时的回调参数</param>
		public void loadAssetsInMapEditor(string assetPath, string dataFormat = ASSETS_BUNDLE, BaseLoader.OnLoadComplete onComplete = null, object loadComplemeArgs = null, BaseLoader.OnLoadError onLoadError = null, object loadErrorArgs = null, BaseLoader.OnLoadProgress onLoadProgress = null, object loadProgressArgs = null) {
			this.isInMapEditor = true;
			//判断缓存中有没有资源，如果有就直接返回
			BaseLoader.LoadVo loadVo = CacheManager.Instance.getCacheLoadedObject (assetPath);
			if (loadVo != null) {
				StartCoroutine (DispatchSuccess (onComplete, loadVo, loadComplemeArgs));
				return;
			}

			this.doLoadAsset (assetPath, dataFormat, onComplete, loadComplemeArgs, onLoadError, loadErrorArgs, onLoadProgress, loadProgressArgs);
		}

		/// 取销加载
		public void cancelLoad(string assetPath) {
			if (assetPath == null) {
				return;
			}

			//没有正在加载中的资源就直接返回
			if (cachesLoadIngs.ContainsKey (assetPath) == false) {
				return;
			}

			LoadIngInfo loadIngInfo = cachesLoadIngs[assetPath];
			cachesLoadIngs.Remove (assetPath);

			//当前正在加载中，就设置加载数量减1
			if (loadIngInfo.isLoading) {
				currentLoadingNumber -= 1;
			}

			///找不到对应的加载器
			if (loadIngInfo.currentLoader == null) {
				return;
			}
			loadIngInfo.currentLoader.cancelLoad ();
			this.destroyLoader (loadIngInfo.currentLoader);
			loadByQueue ();
		}

		private void doLoadAsset(string assetPath, string dataFormat = ASSETS_BUNDLE, BaseLoader.OnLoadComplete onComplete = null, object loadComplemeArgs = null, BaseLoader.OnLoadError onLoadError = null, object loadErrorArgs = null, BaseLoader.OnLoadProgress onLoadProgress = null, object loadProgressArgs = null) {
			if (LoaderUtils.cacheLoadInfo (cachesLoadIngs, assetPath, dataFormat, onComplete, loadComplemeArgs, onLoadError, loadErrorArgs, onLoadProgress, loadProgressArgs) == true) {
				return;
			}

			///同一时间最大只能加载MAX_LOADING个资源
			if (BaseLoader.MAX_LOADING <= currentLoadingNumber) {
				return;
			}

			loadByQueue ();
		}

		/// <summary>
		/// 队列加载当前缓存中的数据
		/// </summary>
		private void loadByQueue() {
			if (cachesLoadIngs.Count <= 0) {
				return;
			}

			LoadIngInfo loadIngInfo = LoaderUtils.getLoadinfInfo (cachesLoadIngs);
			if (loadIngInfo == null) {
				return;
			}

			BaseLoader baseLoader = LoaderUtils.createLoader (loadIngInfo, this.gameObject);
			///找不到对应的加载器
			if (baseLoader == null) {
				return;
			}

			currentLoadingNumber += 1;

			loadIngInfo.isLoading = true;
			loadIngInfo.currentLoader = baseLoader;

			baseLoader.onLoadError = onLoadErrorHandler;
			baseLoader.onLoadComplete = onLoadCompleteHandler;
			baseLoader.onLoadProgress = onLoadProgressHandler;
			baseLoader.loadAsset (loadIngInfo.assetPath);
		}

		/// <summary>
		/// 加载完成后的事件监听
		/// </summary>
		/// <param name="gEvent">G event.</param>
		private void onLoadCompleteHandler(BaseLoader.LoadVo loadVo, object param) {
			currentLoadingNumber -= 1;
			LoadIngInfo loadingInfo = cachesLoadIngs[loadVo.currentLoader.NavitePath];
			loadVo.dataFormat = loadingInfo.dataFormate;

			//遍历成功回调方法
			BaseLoader.OnLoadComplete completeHandler;
			for (int index = 0; index < loadingInfo.onComplete.Count; index++) {
				completeHandler = (BaseLoader.OnLoadComplete)loadingInfo.onComplete[index];
				if (completeHandler == null) 
					continue;
				if (isInMapEditor == true) {
					completeHandler.Invoke (loadVo, loadingInfo.onCompleteArgs [index]);
				} else {
					StartCoroutine (DispatchSuccess (completeHandler, loadVo, loadingInfo.onCompleteArgs [index]));
				}
			}

			//缓存游戏加载对象
			CacheManager.Instance.cache (loadingInfo.assetPath, loadVo);

			destroyLoader (loadVo.currentLoader);
			cachesLoadIngs.Remove (loadingInfo.assetPath);

			loadByQueue ();
		}

		IEnumerator DispatchSuccess(BaseLoader.OnLoadComplete callBack, BaseLoader.LoadVo loadVo, object param = null) {
			yield return 1;
			callBack (loadVo, param);
		}


		/// <summary>
		/// 加载进度的事件监听
		/// </summary>
		/// <param name="gEvent">G event.</param>
		private void onLoadProgressHandler(BaseLoader.LoadVo loadVo, object param) {
		}


		/// <summary>
		/// 加载错误的事件监听
		/// </summary>
		/// <param name="gEvent">G event.</param>
		private void onLoadErrorHandler(BaseLoader.LoadVo loadVo, object param) {
			currentLoadingNumber -= 1;

			LoadIngInfo loadingInfo = cachesLoadIngs[loadVo.currentLoader.NavitePath];
			loadVo.dataFormat = loadingInfo.dataFormate;

			//遍历失败回调方法
			BaseLoader.OnLoadError completeHandler;
			for (int index = 0; index < loadingInfo.onLoadError.Count; index++) {
				completeHandler = (BaseLoader.OnLoadError)loadingInfo.onLoadError[index];

				if (completeHandler == null) {
					continue;
				}

				if (isInMapEditor == true) {
					completeHandler.Invoke (loadVo, loadingInfo.onErrorArgs [index]);
				} else {
					StartCoroutine (DispatchError (completeHandler, loadVo, loadingInfo.onErrorArgs [index]));
				}
			}

			destroyLoader (loadVo.currentLoader);
			cachesLoadIngs.Remove (loadingInfo.assetPath);

			loadByQueue ();
		}

		IEnumerator DispatchError(BaseLoader.OnLoadError callBack, BaseLoader.LoadVo loadVo, object param) {
			yield return 1;
			callBack (loadVo, param);
		}


		/// <summary>
		/// 销毁加载所用到的角本
		/// </summary>
		/// <param name="baseLoader">Base loader.</param>
		private void destroyLoader(BaseLoader baseLoader) {
			if(this.isInMapEditor == true) { //地图编辑器模式下面加载数据
				DestroyImmediate(baseLoader);
				return;
			}
			Destroy (baseLoader);
		}


		public Dictionary<string, LoadIngInfo> cachesLoadIngs {
			get {
				if (_cachesLoadIngs == null) {
					_cachesLoadIngs = new Dictionary<string, LoadIngInfo> ();
				}
				return _cachesLoadIngs;
			}
		}

		/// <summary>
		/// 记录正在加载中的数据
		/// </summary>
		public class LoadIngInfo {
			public BaseLoader currentLoader;
			/// 加载资源路径
			public string assetPath;
			/// 加载成功回调
			public List<BaseLoader.OnLoadComplete> onComplete;
			public List<object> onCompleteArgs;

			/// 回载错误回调
			public List<BaseLoader.OnLoadError> onLoadError;
			public List<object> onErrorArgs;

			/// 加载进度回
			public List<BaseLoader.OnLoadProgress> onLoadProgress;
			public List<object> onProgressArgs;

			/// 加载数据类型
			public string dataFormate;

			/// 当前的资源是否在加载中
			public bool isLoading;
		}
	}

}

