using UnityEngine;
using System.Collections;
using System.Collections.Generic;
public class CanYing : MonoBehaviour {
	public float interval = 0.03f;   //每0.1秒生成一个残影
	public float lifeCycle = 1.0f;    //残影存在时间 
	float lastCombinedTime = 0.0f;    //上一次组合的时间
	MeshFilter[] meshFilters=null;    //存贮对象包含的MeshFilter组件
	//MeshRenderer[] meshRenderers=null;   //存贮对象包含的MeshRenderer组件
	SkinnedMeshRenderer[] skinnedMeshRenderers=null; //存贮对象包含的skinnedMeshRenderers组件
	List<GameObject> objs=new List<GameObject>();    //存储残影
	// Use this for initialization
	void Start () {
		meshFilters=gameObject.GetComponentsInChildren<MeshFilter>();
		skinnedMeshRenderers=gameObject.GetComponentsInChildren<SkinnedMeshRenderer>();
	}

	void OnDisable(){//消灭所有残影
		foreach(GameObject go in objs){
			DestroyImmediate(go);
		}
		objs.Clear();
		objs=null;
	}
	void OnEnable(){//消灭所有残影
		objs=new List<GameObject>(); 
	}
	// Update is called once per frame
	void Update () {
		if(Time.time - lastCombinedTime > interval)
		{
			lastCombinedTime=Time.time;
			//控制skinnedMeshRenderers
			for(int i=0;skinnedMeshRenderers!=null && i<skinnedMeshRenderers.Length;++i)
			{
				Mesh mesh= new Mesh();
				//skinnedMeshRenderers.BakeMesh取出的mesh是原始mesh不会随动画改变
				skinnedMeshRenderers[i].BakeMesh(mesh);
				GameObject go = new GameObject();
				//设置layer层（可自行修改或去掉）
				go.layer=LayerMask.NameToLayer("Default");
				//物体是否被隐藏、保存在场景中或被用户修改？
				//HideFlags.HideAndDontSave; 不能显示在层级面板并且不能保存到场景。
				go.hideFlags=HideFlags.HideAndDontSave;
				//将mesh赋给残影
				MeshFilter meshFilter=go.AddComponent<MeshFilter>();
				meshFilter.mesh=mesh;
				//将材质球赋给残影
				MeshRenderer meshRenderer = go.AddComponent<MeshRenderer>();
				meshRenderer.material=skinnedMeshRenderers[i].material;
				//残影的淡入淡出
				InitFadeInObj(go,skinnedMeshRenderers[i].transform.position, skinnedMeshRenderers[i].transform.rotation,lifeCycle);
			}
			//控制meshFilters
			for(int i=0;meshFilters!=null && i<meshFilters.Length;++i)
			{
				GameObject go = Instantiate(meshFilters[i].gameObject) as GameObject;
				InitFadeInObj(go,meshFilters[i].transform.position, meshFilters[i].transform.rotation, lifeCycle);
			}
		}
	}
	//残影的淡入淡出  对象、位置、旋转、存在时间
	private void InitFadeInObj(GameObject go ,Vector3 position,Quaternion rotation,float lifeCycle)
	{
		go.hideFlags=HideFlags.HideAndDontSave;
		go.transform.position=position;
		go.transform.rotation=rotation;
		FadeInOut fi=go.AddComponent<FadeInOut>();
		fi.lifeCycle=lifeCycle;
		objs.Add(go);//加入列表
	}
}