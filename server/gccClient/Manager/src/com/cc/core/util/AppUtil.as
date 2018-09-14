package  com.cc.core.util
{
	import flash.display.DisplayObject;
	import flash.system.ApplicationDomain;
	import flash.utils.Dictionary;

	public class AppUtil
	{
		
		private static var cacheAppMap:Dictionary = new Dictionary();
		
		/** 根据类获取缓存实例 */
		public static function getAppByClass(appClass:Class):*{
//			app = new appClass();
//			return app;
			if(appClass == null) return null;
			var app:DisplayObject = cacheAppMap[appClass] as DisplayObject;
			if(app == null){
				app = new appClass();
				cacheAppMap[appClass] = app;
			}
			return app;
		}
		/** 根据类名获取缓存实例 */
		public static function getAppByClassName(domain:ApplicationDomain, className:String):DisplayObject{
			var appClass:Class = domain.getDefinition(className) as Class;
			return getAppByClass(appClass);
		}
		
		/** 判断app是否缓存 */
		public static function checkAppCache(appClass:Class):Boolean {
			if(appClass == null) return false;
			return cacheAppMap[appClass];
		}
		
		/** 得到缓存中实例 */
		public static function getAppFromCache(appClass:Class):DisplayObject{
			if(appClass == null) return null;
			return cacheAppMap[appClass];
		}
	}
}