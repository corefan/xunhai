using UnityEngine;
using System.Collections;

namespace GEngine{
	/// 单例基类
	public class MonoSingleton<T> : MonoBehaviour where T : MonoSingleton<T>
	{
	    /// 内部自身实例
		private static T instance = null;

	    /// 给外部自身实例
		public static T Instance {
			get {
				if (instance == null)
				{
					instance = FindObjectOfType(typeof(T)) as T;
					if (instance == null)
					{
						instance = new GameObject("_" + typeof(T).FullName).AddComponent<T>();
						//DontDestroyOnLoad(instance);
					}
				}
				return instance;
			}
		}

	    /// 退出时销毁
		void OnApplicationQuit () 
		{ 
			if (instance != null) {
				GameObject.Destroy(instance);
			}
			instance = null; 
		}

	    /// 构建并获得单例
		public static T CreateInstance ()
		{
			if (Instance != null) Instance.OnCreate();
			return Instance;
		}

	    /// 子类中，进行重写，用于生成单例前的一些初始工作
		protected virtual void OnCreate ()
		{
			gameObject.hideFlags = HideFlags.HideAndDontSave;
		}
	}
}