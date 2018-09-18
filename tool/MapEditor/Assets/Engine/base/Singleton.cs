using System;

namespace GEngine{
	
	public abstract class Singleton<T> where T : class, new() {
		private class SingleNested {
			internal static readonly T _instance;
			static SingleNested() {
				Singleton<T>.SingleNested._instance = Activator.CreateInstance<T>();
			}
		}

		public static T Instance {
			get {
				return Singleton<T>.SingleNested._instance;
			}
		}
	}
}

