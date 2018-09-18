using System;
using GEngine;
using System.Collections;
using System.Collections.Generic;

namespace GEngine{

	/// <summary>
	/// 缓存管理类,用于管理系统中
	/// </summary>
	public class CacheManager : MonoSingleton<CacheManager> {

		/// <summary>
		/// 缓存的load数据引用
		/// </summary>
		private Dictionary<string, BaseLoader.LoadVo> _cacheLoadVos;

		/// <summary>
		/// 缓存加载信息数据
		/// </summary>
		/// <param name="key">Key.</param>
		/// <param name="loadVo">Load vo.</param>
		public void cache(string key, BaseLoader.LoadVo loadVo) {
			if (key == null || loadVo == null) {
				return;
			}

			///对于重复key值的信息，不缓存
			if (cacheLoadVos.ContainsKey (key) == true) {
				return;
			}
			cacheLoadVos.Add (key, loadVo);
		}

		/// <summary>
		/// 删除缓存
		/// </summary>
		/// <param name="key">Key.</param>
		public void remove(string key) {
			if (key == null) {
				return;
			}

			///对于重复key值的信息，不缓存
			if (cacheLoadVos.ContainsKey (key) == false) {
				return;
			}


			BaseLoader.LoadVo loadVo = cacheLoadVos[key];
			if (loadVo == null) {
				return;
			}

			//引用记数减1
			loadVo.referenceCout -= 1;
			if (loadVo.referenceCout > 0) {
				return;
			}
			cacheLoadVos.Remove (key);
			loadVo.Dispose ();
		}

		/// <summary>
		/// 实例化缓存对象，
		/// </summary>
		/// <returns>生成具体对象</returns>
		/// <param name="key">获取key值</param>
		public BaseLoader.LoadVo getCacheLoadedObject(string key) {
			if (key == null) {
				return null;
			}


			///对于重复key值的信息，不缓存
			if (cacheLoadVos.ContainsKey (key) == false) {
				return null;
			}

			BaseLoader.LoadVo loadVo = cacheLoadVos[key];

			return loadVo;
		}


		public void dispose() {
			if (_cacheLoadVos == null) {
				return;
			}

			foreach(KeyValuePair<string, BaseLoader.LoadVo> outerDic in _cacheLoadVos) {
				if (outerDic.Value == null || outerDic.Value == null) {
					continue;
				}
				outerDic.Value.Dispose ();
			}
		}


		public Dictionary<string, BaseLoader.LoadVo> cacheLoadVos {
			get {
				if (_cacheLoadVos == null) {
					_cacheLoadVos = new Dictionary<string, BaseLoader.LoadVo> ();
				}
				return _cacheLoadVos;
			}
		}
	}
}

