#region 截屏工具
// 功  能： 截屏工具
// 描  述： 截屏工具
// 时  间： 2016-04-01 15:14:50
// 作  者： chunzi
// E-mail： zhuang_wx@qq.com
// 项目名： xgame
#endregion

using UnityEngine;
using System.Collections;
/// <summary>
/// 截屏工具
/// </summary>
public class CaptureScreen {
	/// <summary>
	/// 截一张png图片(游戏中某一帧下的画面)
	/// </summary>
	/// <param name="saveFileName">eg:Screenshot.png</param>
	public static void Capture(string saveFileName){
		Application.CaptureScreenshot(saveFileName, 0);  
	}

	/// <summary>  
	/// 利用Texture2D截屏(游戏中某一帧下的画面):
	///	截全屏 Capture( new Rect( Screen.width*0f, Screen.height*0f, Screen.width*1f, Screen.height*1f));
	///	截中间4分之 Capture( new Rect( Screen.width*0.25f, Screen.height*0.25f, Screen.width*0.5f, Screen.height*0.5f));
	/// </summary>  
	/// <returns>Texture2D</returns>  
	/// <param name="rect">Rect.截图的区域，左下角为o点</param> 
	/// <param name="saveFileName">eg:Application.dataPath + "/Screenshot.png"</param> 
	public static Texture2D Capture(Rect rect, string saveFileName)   
	{  
	    // 先创建一个的空纹理，大小可根据实现需要来设置  
	    Texture2D screenShot = new Texture2D((int)rect.width, (int)rect.height, TextureFormat.RGB24,false);  
	  
	    // 读取屏幕像素信息并存储为纹理数据，  
	    screenShot.ReadPixels(rect, 0, 0);  
	    screenShot.Apply();  
	  
	    // 然后将这些纹理数据，成一个png图片文件  
	    byte[] bytes = screenShot.EncodeToPNG();  
		string filename = saveFileName;//Application.dataPath + "/Screenshot.png";  
	    System.IO.File.WriteAllBytes(filename, bytes);  
	    //Debug.Log(string.Format("截屏了一张图片: {0}", filename));
	    // 最后，我返回这个Texture2d对象，这样我们直接，所这个截图图示在游戏中，当然这个根据自己的需求的。  
	    return screenShot;  
	}  

	/// <summary>  
	/// 对指定相机截图
	/// </summary>  
	/// <returns>Texture2D</returns>  
	/// <param name="camera">Camera.要被截屏的相机</param>  
	/// <param name="saveFileName">eg:Application.dataPath + "/Screenshot.png"</param> 
	/// <param name="rect">Rect.截屏的区域</param>  
	public static Texture2D Capture(Camera camera, string saveFileName, Rect rect) //Camera[] otherCamera=null  (多相机合成截图) 
	{  
	    // 创建一个RenderTexture对象  
	    RenderTexture rt = new RenderTexture((int)rect.width, (int)rect.height, 0);  
	    // 临时设置相关相机的targetTexture为rt, 并手动渲染相关相机  
	    camera.targetTexture = rt;  
	    camera.Render();  
		/**ps: --- 如果这样加上多个相机，可以实现只截图某几个指定的相机一起看到的图像。  
		int len = otherCamera.Length;
		for (int i = 0; i < len; i++) {
			Camera cam = otherCamera[i];
			cam.targetTexture = rt;
			cam.Render();
		} 
        //*/ 
	  
	    // 激活这个rt, 并从中读取像素。  
	    RenderTexture.active = rt;  
	    Texture2D screenShot = new Texture2D((int)rect.width, (int)rect.height, TextureFormat.RGB24,false);  
	    screenShot.ReadPixels(rect, 0, 0);// 注：这个时候，它是从RenderTexture.active中读取像素  
	    screenShot.Apply();  
	  
	    // 重置相关参数，以使用camera继续在屏幕上显示  
	    camera.targetTexture = null;  
		/**ps: --- 如果这样加上多个相机，可以实现只截图某几个指定的相机一起看到的图像。  
		int len = otherCamera.Length;
		for (int i = 0; i < len; i++) {
			Camera cam = otherCamera[i];
			cam.targetTexture = null;
		} 
        //*/ 
	    RenderTexture.active = null; // JC: added to avoid errors  
	    GameObject.Destroy(rt);  
	    // 最后将这些纹理数据，成一个png图片文件  
	    byte[] bytes = screenShot.EncodeToPNG();  
		string filename = saveFileName;//Application.dataPath + "/Screenshot.png";  
	    System.IO.File.WriteAllBytes(filename, bytes);  
	    //Debug.Log(string.Format("截屏了一张照片: {0}", filename));  
	    return screenShot;  
	}  
}

