using System;
using UnityEngine;

public class GameObjectCellVo
{
	public ClientElementInfo cellVo;

	/// 当前的GameObject
	public GameObject currentGameObject;

	public void dispose() {
		if (currentGameObject != null) {
			currentGameObject.transform.parent = null;
			MonoBehaviour.DestroyImmediate (currentGameObject);
		}
	}
}