using UnityEngine;
using FairyGUI;

public class CubeHit : MonoBehaviour
{
	void OnMouseDown()
	{
		if (!Stage.isTouchOnUI)
			Debug.Log("hit the cube");
	}
}
