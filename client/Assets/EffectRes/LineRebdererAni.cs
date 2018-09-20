#region 脚本说明
/*----------------------------------------------------------------
// 脚本作用：实现Line拖尾效果
// 创建者：黑仔
//----------------------------------------------------------------*/
#endregion

using UnityEngine;
using System.Collections;

public class LineRebdererAni : MonoBehaviour 
{
	
	public int lengthOfLineRenderer = 20;
	public Vector3 DingDian;
	public Vector3 SuiJi;
	public Vector3 PingPong;
	public float GenSuiSpeed = 3f;
	public float RaoDongSpeed = 0.1f;
	public bool Follow = false;
	public float DingDianJuli = 1;

	private LineRenderer lineR;
	private Vector3[] posDian;
	void Start() 
	{

		lineR = this.GetComponent<LineRenderer>();
		lineR.SetVertexCount(lengthOfLineRenderer);

		posDian = new Vector3[lengthOfLineRenderer];

		for (int i = 0; i < lengthOfLineRenderer; i++) 
		{
			if (i == 0) 
			{
				posDian[i] = this.transform.position;
				lineR.SetPosition(i, posDian[i]);
			}
			else 
			{
				posDian[i] = posDian[0] + ((DingDian / (lengthOfLineRenderer - 1)) * i);
				lineR.SetPosition(i, posDian[i]);
			}
		}
	}
	void Update() 
	{
		DingDianGenSui();
		for (int i = 0; i < lengthOfLineRenderer; i++) 
		{
			if (i == 0) 
			{
				posDian[i] = this.transform.position;
				lineR.SetPosition(i, posDian[i]);
			}
			else 
			{
				posDian[i] = Vector3.Lerp(posDian[i], posDian[i - 1] + (DingDian / (lengthOfLineRenderer - 1)) + PingPongPianYi(i), Time.deltaTime * GenSuiSpeed);
				posDian[i] += SuiJiPianYi();
				lineR.SetPosition(i,posDian[i]);
			}
		}
	}

	private Vector3 SuiJiPianYi()
	{
		Vector3 v = new Vector3(0,0,0);
		v.x = Random.Range(-SuiJi.x, SuiJi.x);
		v.y = Random.Range(-SuiJi.y, SuiJi.y);
		v.z = Random.Range(-SuiJi.z, SuiJi.z);
		v = Quaternion.FromToRotation(Vector3.forward, DingDian - posDian[0]) * v;
		return v;
	}
	private Vector3 PingPongPianYi(int i)
	{
		if (RaoDongSpeed == 0) 
		{
			RaoDongSpeed = 0.001f;
		}
		Vector3 v = new Vector3(0,0,0);

		v.x = Mathf.Sin(i + Time.time * RaoDongSpeed) * PingPong.x;
		v.y = Mathf.Sin(i + RaoDongSpeed * Time.time) * PingPong.y;
		v.z = Mathf.Sin(i + RaoDongSpeed * Time.time) * PingPong.z;
		v = Quaternion.FromToRotation(Vector3.forward, DingDian - posDian[0]) * v;
		return v;
	}

	private void DingDianGenSui()
	{
		if (Follow) 
		{
			DingDian = transform.forward * DingDianJuli;
		}
	}
}