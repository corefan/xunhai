using UnityEngine;
using UnityEditor;

public class EffectShadowClose : EditorWindow 
{
	private static string effectPath;
	[MenuItem("Assets/查找关闭文件夹下的预设投影(HZ)")]
	public static void SetEffectShadowClose()
	{
		var dqObj = Selection.activeObject;
		if (dqObj == null) 
		{
			Debug.LogWarning("请选择一个文件夹！！！！！！！！！！！！！！");
			return;
		}
		var dqPath = AssetDatabase.GetAssetPath(dqObj);
		var dqFloderPath = dqPath;
		effectPath = dqFloderPath;
		Debug.Log("当前搜索的文件夹路径是： " + effectPath);

		var allGuids = AssetDatabase.FindAssets("t:GameObject", new string[] {effectPath});
		if (allGuids.Length > 0) 
		{
			int i = 0;
			ShowProgress(0, 0, 0);
			int m_jishu = 0;
			foreach (var guid in allGuids) 
			{
				var assetPath = AssetDatabase.GUIDToAssetPath(guid);
				var asset = AssetDatabase.LoadMainAssetAtPath(assetPath);
				var assetObj = asset as GameObject;
				var psRendS = assetObj.GetComponentsInChildren<ParticleSystemRenderer>();
				foreach (var psRend in psRendS) 
				{
					if (psRend != null) 
					{
						if (psRend.shadowCastingMode == UnityEngine.Rendering.ShadowCastingMode.On || psRend.receiveShadows == true) 
						{
							psRend.shadowCastingMode = UnityEngine.Rendering.ShadowCastingMode.Off;
							psRend.receiveShadows = false;
							i++;
						}
					}
				}

				var meshRends = assetObj.GetComponentsInChildren<MeshRenderer>();
				foreach (var meshRend in meshRends) 
				{
					if (meshRend != null) 
					{
						if (meshRend.shadowCastingMode == UnityEngine.Rendering.ShadowCastingMode.On || meshRend.receiveShadows == true) 
						{
							meshRend.shadowCastingMode = UnityEngine.Rendering.ShadowCastingMode.Off;
							meshRend.receiveShadows = false;
							i++;
						}
					}
				}

				var meshSkinRends = assetObj.GetComponentsInChildren<SkinnedMeshRenderer>();
				foreach (var meshSkinRend in meshSkinRends) 
				{
					if (meshSkinRend != null) 
					{
						if (meshSkinRend.shadowCastingMode == UnityEngine.Rendering.ShadowCastingMode.On || meshSkinRend.receiveShadows == true) 
						{
							meshSkinRend.shadowCastingMode = UnityEngine.Rendering.ShadowCastingMode.Off;
							meshSkinRend.receiveShadows = false;
							i++;
						}
					}
				}

				ShowProgress((float)m_jishu / (float)allGuids.Length, allGuids.Length, m_jishu);
				m_jishu++;
			}
			EditorUtility.ClearProgressBar();
			if (i == 0) 
			{
				Debug.LogWarning(">>>>>>>>>>>>>>>!特效预设的投影都是关闭的！<<<<<<<<<<<<<<<<<<<<<<<<<<");
			}
			if (i > 0) 
			{
				Debug.LogWarning(">>>>>>>>>>>>>>>!" + effectPath + " 中的特效预设投影设置完成！<<<<<<<<<<<<<<<<<<<<<<<<<<");
			}
		}
	}

	public static void ShowProgress(float val, int total, int cur)
	{
		EditorUtility.DisplayProgressBar("查找中！", string.Format("查找 ({0}/{1}), 请稍等...", cur, total), val);
	}
}
