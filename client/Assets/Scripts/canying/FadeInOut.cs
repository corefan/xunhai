using UnityEngine;
using System.Collections;
/// 淡入淡出脚本
public class FadeInOut : MonoBehaviour {
	public float lifeCycle=2.0f;     //残影存在时间
	float startTime;          //产生的时间
	Material mat = null;       //材质球
	// Use this for initialization
	void Start () {
		startTime=Time.time;
		MeshRenderer meshRenderer = GetComponent<MeshRenderer>();
		//如果没有meshRenderer  或者  没有材质  禁用对象
		if(!meshRenderer || !meshRenderer.material)
			base.enabled=false;
		else{
			mat=meshRenderer.material;
			//ReplaceSharder(); //替换材质球  可不用
		}
	}

	// Update is called once per frame
	void Update ()
	{
		float time = Time.time - startTime;
		//存在时间到了就消灭掉
		if (time > lifeCycle)
			DestroyImmediate (gameObject);
		else {//通过remainderTime来控制残影的透明度
			float remainderTime = lifeCycle - time;
			if (mat) {//如果得到了材质球
				//武器特效时使用 控制sharder透明度
				Color col;
				if (mat.HasProperty ("_Color")) {
					col = mat.GetColor ("_Color");
					col.a = remainderTime;
					mat.SetColor ("_Color", col);
				}
				if(mat.HasProperty("_OutlineColor")){
					col = mat.GetColor("_OutlineColor");
					col.a = remainderTime;
					mat.SetColor("_OutlineColor",col);
				}
			}
		}
	}
	//替换材质球
	private void ReplaceSharder()
	{
		 if(mat.shader.name.Equals("Custom/Toon/Basic Outline"))
		 {
		 	mat.shader = Shader.Find("Custom/Toon/Basic Outline Replace");
		 }
		 else if(mat.shader.name.Equals("Custom/Toon/Basic"))
		 {
		 	mat.shader=Shader.Find("Custom/Toon/Basic Replace");
		 }
		 else
		 {
		 	Debug.LogError("Can't find target sharder");
		 }
	}
}