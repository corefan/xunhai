using UnityEngine;
using System.Collections;
using System;


namespace GEngine{

	/// <summary>
	/// 加载本地资源文件,,,WWW类在Dispose时，他引用的所有资源资源也会一同给销毁掉
	/// 但是如果有对象引用着WWW某个属性时，是不会被销毁掉的
	/// </summary>
	[ExecuteInEditMode]
	public class CustomLoader : BaseLoader {

		/// <summary>
		/// 加载失败重试次数
		/// </summary>
		public const int TRY_LOAD_TIMES = 3;

		public const int WAIT_FOR_TIME_LOAD = 5;

		/// <summary>
		/// 当前重试资数
		/// </summary>
		private int startTime;

		/// <summary>
		/// 当前加载器
		/// </summary>
		public WWW loader;

		/// <summary>
		/// 当前正在加载的资源是否是远程服务器资源
		/// </summary>
		//private bool isRemoveFile;

		public CustomLoader() {
		}

		// Update is called once per frame
		void Update () {

			if (IsDispatcherEvent) {
				return;
			}

			if (loader == null) {
				return;
			}
		}


		/// <summary>
		/// 加载资源
		/// </summary>
		/// <param name="assetPath">资源路径，可能是本地资源，也可能是远程服务器上的资源</param>
		/// <param name="isRemoveFile">是否是远程服务器资源</param>
		public override void loadAsset(string assetPath) {
			if (IsLoading == true) {
				return;
			}
			IsLoading = true;
			NavitePath = assetPath;
			this.AssetPath = PersistantFileManager.getLocalSourcePath (NavitePath);

			doLoadAsset ();

		}

		private void doLoadAsset() {
			startTime += 1; //重试次数加1
			StartCoroutine(doTryLoadAsset());
		}


		/// <summary>
		/// 加载资源
		/// </summary>
		/// <returns>The load asset.</returns>
		private IEnumerator doTryLoadAsset() {
			if (loader != null) {
				loader.Dispose ();
			}

			loader = new WWW (this.AssetPath);
			yield return loader;
			tryLoadAgain ();
		}

		/// <summary>
		/// 重新尝试加载数据
		/// </summary>
		/// <returns>The load again.</returns>
		private void tryLoadAgain() {
			///己经取销加载，不需要发送事件，取消加载不处理任何逻辑
			if (IsCancelLoad == true) {
				return;
			}

			//加载完成，发送事件
			if (string.IsNullOrEmpty(loader.error) == false) {
				//重试次数大于特定次数，返回
				if (startTime > TRY_LOAD_TIMES) {
					IsDispatcherEvent = true;
					invokeLoadError (buildVo(loader, this));
					loader.Dispose ();
					return;
				}

				//重新加载
				StartCoroutine(waitTimeLoad ());
			} else {

				//己经加载完成
				invokeLoadComplete(buildVo(loader, this));
				loader.Dispose ();
			}
		}

		/// <summary>
		/// 等待5秒后重新加载
		/// </summary>
		/// <returns>The time load.</returns>
		private IEnumerator waitTimeLoad() {
			yield return new WaitForSeconds (WAIT_FOR_TIME_LOAD);

			doLoadAsset ();
		}


		/// <summary>
		/// 取消加载
		/// </summary>
		public override void cancelLoad() {
			if (loader != null) {
				loader.Dispose ();
			}
			IsCancelLoad = true;
		}





		/// <summary>
		/// 构建返回数据
		/// </summary>
		/// <returns>The vo.</returns>
		/// <param name="connection">Connection.</param>
		private static LoadVo buildVo(WWW connection, CustomLoader customLoader) {
			if (connection == null) {
				return null;
			}

			LoadVo loadVo = new LoadVo ();

			try {
				loadVo.bytes = connection.bytes;
				loadVo.currentLoader = customLoader;
				loadVo.text = connection.text;
				loadVo.url = connection.url;
				loadVo.nativeUrl = customLoader.NavitePath;
				loadVo.error = connection.error;
				loadVo.assetBundle = connection.assetBundle;
//				loadVo.audioClip = connection.audioClip;
			} catch {
			}
			return loadVo;
		}
	}

}