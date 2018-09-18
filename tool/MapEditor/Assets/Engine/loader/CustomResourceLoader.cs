using UnityEngine;
using System.Collections;
using System.Threading;


/// <summary>
/// 本地资源加载器
/// </summary>
namespace GEngine{

	/// <summary>
	/// 可以加载Resource下的所有的资源文件
	/// </summary>
	[ExecuteInEditMode]
	public class CustomResourceLoader : BaseLoader {
		

		/// <summary>
		/// 异步加载
		/// </summary>
		private object request;

		public CustomResourceLoader() {
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

			this.IsLoading = true;
			this.NavitePath = assetPath;
			this.AssetPath = PersistantFileManager.getRemoteResourcePath (assetPath);;

			StartCoroutine (doLoadAsset ());
		}

		public override void cancelLoad () {
			IsCancelLoad = true;
		}

		/// <summary>
		/// 同步加载资源,包括配置文件，声音，prefab等资源
		/// </summary>
		/// <returns>The asset.</returns>
		/// <param name="assetPath">Asset path.</param>
		public object loadAssetSyn(string assetPath) {
			return Resources.Load<Object>(this.AssetPath);
		}


		/// <summary>
		/// 加载资源
		/// </summary>
		/// <returns>The load asset.</returns>
		private IEnumerator doLoadAsset() {
			request = Resources.Load<Object>(this.AssetPath);
			yield return request;

			///己经取销加载
			if (IsCancelLoad == true) {
				yield break;
			}
			//加载完成
			if (IsDispatcherEvent == false) {
				if (request == null) {
					invokeLoadError (buildVo(request, this));
				} else {
					invokeLoadComplete (buildVo(request, this));
				}
				IsDispatcherEvent = true;
			}
		}

		/// <summary>
		/// 构建返回数据
		/// </summary>
		/// <returns>The vo.</returns>
		/// <param name="connection">Connection.</param>
		private static LoadVo buildVo(object connection, CustomResourceLoader customLoader) {

			LoadVo loadVo = new LoadVo ();
			loadVo.currentLoader = customLoader;
			loadVo.gameObject = connection;
			loadVo.nativeUrl = customLoader.NavitePath;

			return loadVo;
		}

	}
}
