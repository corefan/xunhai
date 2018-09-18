using System;
using UnityEngine;

[ExecuteInEditMode]
public class GameObjectDestroyCallBack : MonoBehaviour
{
	public delegate void DestroyCallBack(ClientElementInfo gameObject);

	public ClientElementInfo cellVo;

	public DestroyCallBack destroyCallBack;

	void OnDestroy() {
		if (destroyCallBack == null) {
			return;
		}
		destroyCallBack (this.cellVo);
	}
}
