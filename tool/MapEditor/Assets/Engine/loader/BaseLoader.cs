using System;
using UnityEngine;

namespace GEngine{


	/// <summary>
	/// 加载基类
	/// </summary>
	public abstract class BaseLoader : MonoBehaviour {
		/// <summary>
		/// 同一时间最大加载资源数量
		/// </summary>
		public static int MAX_LOADING = 2;

		/// <summary>
		/// 加载完成回调
		/// </summary>
		public delegate void OnLoadComplete (BaseLoader.LoadVo loadVo, object param);

		/// <summary>
		/// 加错误回调
		/// </summary>
		public delegate void OnLoadError (BaseLoader.LoadVo loadVo, object param);

		/// <summary>
		/// 加载进度回调
		/// </summary>
		public delegate void OnLoadProgress (BaseLoader.LoadVo loadVo, object param);

		private OnLoadError _onLoadError;

		private OnLoadComplete _onLoadComplete;

		private OnLoadProgress _onLoadProgress;

		/// <summary>
		/// 是否正在加载资源中
		/// </summary>
		private bool _isLoading;

		/// <summary>
		/// 是否己经抛出事件
		/// </summary>
		private bool _isDispatcherEvent;


		/// <summary>
		/// 资源路径
		/// </summary>
		private string _assetPath;

		/// <summary>
		/// 资源的原始路径
		/// </summary>
		private string _navitePath;

		/// <summary>
		/// 是否己经取销加载
		/// </summary>
		private bool _isCancelLoad;


		/// <summary>
		/// 用于基类自己实现自己的加载方法
		/// </summary>
		/// <param name="assetPath">Asset path.</param>
		/// <param name="isRemoveFile">If set to <c>true</c> is remove file.</param>
		public abstract void loadAsset(string assetPath);



		/// <summary>
		/// 取消加载
		/// </summary>
		public abstract void cancelLoad ();


		/// <summary>
		/// 加载成功后的回调函数
		/// </summary>
		/// <param name="loadVo">Load vo.</param>
		protected void invokeLoadComplete(BaseLoader.LoadVo loadVo) {
			if (_onLoadComplete == null) {
				return;
			}
			_onLoadComplete.Invoke (loadVo, null);
		}


		/// <summary>
		/// 加载进度的回调函数
		/// </summary>
		/// <param name="loadVo">Load vo.</param>
		protected void invokeLoadProgress(BaseLoader.LoadVo loadVo) {
			if (_onLoadProgress == null) {
				return;
			}
			_onLoadProgress.Invoke (loadVo, null);
		}


		/// <summary>
		/// 加载失败后的回调函数
		/// </summary>
		/// <param name="loadVo">Load vo.</param>
		protected void invokeLoadError(BaseLoader.LoadVo loadVo) {
			if (_onLoadError == null) {
				return;
			}
			_onLoadError.Invoke (loadVo, null);
		}




		public OnLoadError  onLoadError {
			set {
				_onLoadError = value;
			}
			get {
				return _onLoadError;
			}
		}



		public OnLoadComplete onLoadComplete {
			set {
				_onLoadComplete = value;
			}
			get {
				return _onLoadComplete;
			}
		}



		public OnLoadProgress onLoadProgress {
			set {
				 _onLoadProgress = value;
			}
			get {
				return _onLoadProgress;
			}
		}

		/// <summary>
		/// 是否是取销加载
		/// </summary>
		/// <value><c>true</c> 己经取消 <c>false</c>.没有取销</value>
		public bool IsCancelLoad {
			get {
				return _isCancelLoad;
			}
			set {
				_isCancelLoad = value;
			}
		}


		/// <summary>
		/// 是否正在加载资源中
		/// </summary>
		/// <value><c>true</c> 表示正在加载资源中 <c>false</c>.表示没有加载了</value>
		public bool IsLoading {
			get {
				return _isLoading;
			}
			set {
				_isLoading = value;
			}
		}

		/// <summary>
		/// 是否己经抛出了错误或者是加载成功事件
		/// </summary>
		/// <value><c>true</c> 己经抛出 <c>false</c>.表示没有抛出</value>
		public bool IsDispatcherEvent {
			get {
				return _isDispatcherEvent;
			}
			set {
				_isDispatcherEvent = value;
			}
		}

		/// <summary>
		/// 加载资源路径
		/// </summary>
		public string AssetPath {
			get {
				return _assetPath;
			}
			set {
				_assetPath = value;
			}
		}
		/// <summary>
		/// 加载资源路径
		/// </summary>
		public string NavitePath {
			get {
				return _navitePath;
			}
			set {
				_navitePath = value;
			}
		}

		/// <summary>
		/// 加载数类
		/// </summary>
		public class LoadVo {
			/// <summary>
			/// 请求的原来地址
			/// </summary>
			public string nativeUrl;

			/// <summary>
			/// 引用记数
			/// </summary>
			public int referenceCout;

			public BaseLoader currentLoader;

			/// <summary>
			/// 加载本地资源时，所引用的gameobject类
			/// </summary>
			public object gameObject;


			/// <summary>
			/// 二进制数据
			/// </summary>
			public byte[] bytes;

			/// <summary>
			/// 声音
			/// </summary>
			public AudioClip audioClip;

			/// <summary>
			/// 动画帖图
			/// </summary>
			//public MovieTexture movie;

			/// <summary>
			/// 文本数据
			/// </summary>
			public string text;

			/// <summary>
			/// 请求地址
			/// </summary>
			public string url;

			/// <summary>
			/// 错误信息
			/// </summary>
			public string error;

			/// <summary>
			/// 数据类型
			/// </summary>
			public string dataFormat;

			/// <summary>
			/// 资源引用
			/// </summary>
			public AssetBundle assetBundle;

			public void Dispose() {
				error = null;
				url = null;
				text = null;
				bytes = null;

				if (audioClip != null) {
					audioClip.UnloadAudioData ();

					Destroy (audioClip);
				}

				if (assetBundle != null) {
					assetBundle.Unload (true);
				}


				if (gameObject != null) {
					Type t = gameObject.GetType();
					if(t != null && (t.Equals(typeof(GameObject)) && t.Equals(typeof(AssetBundle)) == false)) {
//						Resources.UnloadAsset ((GameObject)gameObject);
					}
				}
			}
		}
	}
}

