using System;
using UnityEngine;
using System.Collections;

namespace GEngine{

	/// <summary>
	/// 远程数据加载，热更需要的加载器
	/// </summary>
	[ExecuteInEditMode]
	public class CustomRemoteLoader : BaseLoader {


		/// <summary>
		/// 加载失败重试次数
		/// </summary>
		public const int TRY_LOAD_TIMES = 3;

		public const int WAIT_FOR_TIME_LOAD = 10;

		/// <summary>
		/// 当前重试资数
		/// </summary>
		private int currentTime;

		/// <summary>
		/// 当前加载器
		/// </summary>
		private WWW loader;

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
			this.AssetPath = PersistantFileManager.getRemoteResourcePath (NavitePath);



			doLoadAsset ();

		}

		private void doLoadAsset() {
			currentTime += 1; //重试次数加1

			StartCoroutine (doTryLoadAsset ());
		}


		/// <summary>
		/// 加载资源
		/// </summary>
		/// <returns>The load asset.</returns>
		private IEnumerator doTryLoadAsset() {
			if (loader != null) {
				loader.Dispose ();
			}

			System.Random random = new System.Random ();
			if (this.AssetPath.IndexOf ("?") >= 0) {
				loader = new WWW (this.AssetPath + "&random=" + random.NextDouble());
			} else {
				loader = new WWW (this.AssetPath + "?random=" + random.NextDouble());
			}

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
			if (string.IsNullOrEmpty(loader.error) == false || 
				loader.bytes == null || loader.bytes.Length <= 0) { //数据为空，认为没有加载完成
				//重试次数大于特定次数，返回
				if (currentTime > TRY_LOAD_TIMES) {
					IsDispatcherEvent = true;
					invokeLoadError (buildVo(loader, this));
					loader.Dispose ();
					return;
				}

				//重新加载
				StartCoroutine(waitTimeLoad());
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
		private static LoadVo buildVo(WWW connection, CustomRemoteLoader customLoader) {
			if (connection == null) {
				return null;
			}

			LoadVo loadVo = new LoadVo ();
			loadVo.url = connection.url;
			loadVo.nativeUrl = customLoader.NavitePath;

			try {
				loadVo.text = connection.text;
			} catch {
			}
			try {
				loadVo.currentLoader = customLoader;
			} catch {
			}

			try {
				loadVo.error = connection.error;
			} catch {
			}
			
			try {
//				loadVo.assetBundle = connection.assetBundle;
			} catch {
			}
			return loadVo;
		}
	}
}

