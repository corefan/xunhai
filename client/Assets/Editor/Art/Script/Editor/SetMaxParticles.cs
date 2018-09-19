using UnityEngine;
using UnityEditor;
using System.Collections;
using System.Collections.Generic;


public class SetMaxParticles : Editor 
{
	private List<int> psCounts = new List<int>();
	public int psCount = 0;

	public void Gui(GameObject go, int input)
	{
		if (go != null) 
		{
			if (input != 0) 
			{
				if (GUILayout.Button("设置特效粒子MaxParticles"))
				{
					SetMaxPS(go);
				}
			}
		}
	}

	public void Change()
	{
		psCounts.Clear();
		psCount = 0;
	}

	/// <summary>
	/// 刷新比较粒子最大数
	/// </summary>
	/// <param name="go">当前选择的特效</param>
	/// <param name="input">最大粒子数</param>
	public void update (GameObject go, int input) 
	{
		if (go != null && input > psCount) 
		{
			ShuaXinPSNum(go);
		}
	}

	private void SetMaxPS(GameObject go)
	{
		if (AssetDatabase.Contains(Selection.activeGameObject) && Selection.activeGameObject.transform.root == Selection.activeGameObject.transform) 
		{
			var obj = AssetDatabase.LoadAssetAtPath<GameObject>(AssetDatabase.GetAssetPath(Selection.activeGameObject));
			var psS = obj.GetComponentsInChildren<ParticleSystem>();
			if (psCounts.Count > 0) 
			{
				for (int i = 0; i < psCounts.Count; i++) 
				{
					psS[i].maxParticles = psCounts[i];
				}
			}
		}
		if (!AssetDatabase.Contains(go)) 
		{
			var psS = go.GetComponentsInChildren<ParticleSystem>();
			if (psCounts.Count > 0) 
			{
				for (int i = 0; i < psCounts.Count; i++) 
				{
					psS[i].maxParticles = psCounts[i];
				}
			}
		}
		psCounts.Clear();
		psCount = 0;

	}

	private void ShuaXinPSNum(GameObject go)
	{
		psCounts.Clear();
		var psS = go.GetComponentsInChildren<ParticleSystem>();
		if (psS.Length > 0) 
		{
			for (int i = 0; i < psS.Length; i++) 
			{
				var psNum = psS[i].GetParticles(new ParticleSystem.Particle[psS[i].maxParticles]);
				psCounts.Add(psNum);
			}
			var num = 0;
			foreach (var item in psCounts) 
			{
				num = num + item;
			}
			if (psCount < num) 
			{
				psCount = num;
			}
		}
	}
}
