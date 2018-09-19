using UnityEngine;
using System.Collections;

[AddComponentMenu("Control/OverLook")]
public class OverLook : MonoBehaviour
{
	/// <summary>
	/// 鼠标和触控激活区域。
	/// </summary>
	public Rect mouseActiveRect = new Rect (0, 0, 1, 1);
	/// <summary>
	/// 鸟瞰目标点。
	/// </summary>
	public Transform target;
	/// <summary>
	/// 鸟瞰相机与地平面的最小夹角。
	/// </summary>
	public float yMinLimit = 0;
	/// <summary>
	/// 鸟瞰相机与地平面的最大夹角。
	/// </summary>
	public float yMaxLimit = 80;
	/// <summary>
	/// 鸟瞰相机和目标点之间的初始距离。
	/// </summary>
	public float initDistance = 20;
	/// <summary>
	/// 鸟瞰相机和目标点之间的最小距离。
	/// </summary>
	public float minDistance = 5;
	/// <summary>
	/// 鸟瞰相机和目标点之间的最大距离。
	/// </summary>
	public float maxDistance = 100;
	/// <summary>
	/// 鸟瞰相机和目标点之间的缩放速度。
	/// </summary>
	public float wheelSpeed = 30;
	/// <summary>
	/// 相机操控的速度。
	/// </summary>
	public Vector2 speed = Vector2.one;
	/// <summary>
	/// 缓冲时间。
	/// </summary>
	public float smoothTime = 0.4f;
	public float distance = 0;
	
	//public bool useMouse;
	private float smoothDistance = 0;
	private float distanceVelocity = 0;
	private Vector3 velocityV3 = Vector3.zero;
	private Vector3 smooth = Vector3.zero;
	private Vector3 lastMousePos = Vector3.zero;
	private Vector3 saveMousePos = Vector3.zero;
	private Vector2 lastTP1 = Vector2.zero;
	private Vector2 lastTP2 = Vector2.zero;

	void Start ()
	{
		distance = Vector3.Distance (target.position, transform.position);
		smoothDistance = distance;
		transform.position = transform.rotation * new Vector3 (0, 0, -smoothDistance) + target.position;
		lastMousePos = Input.mousePosition;
		saveMousePos = new Vector3 (transform.eulerAngles.y, transform.eulerAngles.x, 0);
		saveMousePos.y = Mathf.Clamp (saveMousePos.y, yMinLimit, yMaxLimit);
		smooth = saveMousePos;
		mouseActiveRect = new Rect (Screen.width * mouseActiveRect.x, Screen.height * mouseActiveRect.y, Screen.width * mouseActiveRect.width, Screen.height * mouseActiveRect.height);
	}
	
	void OnEnable ()
	{
		distance = Vector3.Distance (target.position, transform.position);
		smoothDistance = distance;
		transform.position = transform.rotation * new Vector3 (0, 0, -smoothDistance) + target.position;
		lastMousePos = Input.mousePosition;
		saveMousePos = new Vector3 (transform.eulerAngles.y, transform.eulerAngles.x, 0);
		saveMousePos.y = Mathf.Clamp (saveMousePos.y, yMinLimit, yMaxLimit);
		smooth = saveMousePos;
	}

	void Update ()
	{
		if (target) {
			
		#if UNITY_EDITOR
		    MouseControl ();	
		#elif UNITY_IPHONE
		    TouchControl(); 
		#else
		     MouseControl ();
		#endif
			distance = Mathf.Clamp (distance, minDistance, maxDistance);
			smoothDistance = Mathf.SmoothDamp (smoothDistance, distance, ref distanceVelocity, smoothTime);
			smooth = Vector3.SmoothDamp (smooth, saveMousePos, ref velocityV3, smoothTime);
			transform.rotation = Quaternion.Euler (smooth.y, smooth.x, 0);
			transform.position = transform.rotation * new Vector3 (0, 0, -smoothDistance) + target.position;
		}
	}

	Vector2 V2SmoothDamp (Vector2 current, Vector2 target, ref Vector2 currentVelocity, float smoothTime)
	{
		current.x = Mathf.SmoothDamp (current.x, target.x, ref currentVelocity.x, smoothTime);
		current.y = Mathf.SmoothDamp (current.y, target.y, ref currentVelocity.y, smoothTime);
		return current;
	}
	
	void TouchControl ()
	{
		if (Input.touchCount == 2) {
			if (!mouseActiveRect.Contains (Input.GetTouch (0).position)) {
				return;
			}
			if (Input.GetTouch (1).phase == TouchPhase.Began) {
				lastTP1 = Input.GetTouch (0).position;
				lastTP2 = Input.GetTouch (1).position;
			}
			distance -= (Vector2.Distance (Input.GetTouch (0).position, Input.GetTouch (1).position) - Vector2.Distance (lastTP1, lastTP2)) * wheelSpeed * 0.01f;
			lastTP1 = Input.GetTouch (0).position;
			lastTP2 = Input.GetTouch (1).position;
		} else {
			if (!mouseActiveRect.Contains (Input.mousePosition)) {
				return;
			}
			if (Input.GetMouseButton (0)) {
				if (Input.GetMouseButtonDown (0)) {
					lastMousePos = Input.mousePosition;
				}
				saveMousePos.x += (Input.mousePosition.x - lastMousePos.x) * speed.x;
				saveMousePos.y -= (Input.mousePosition.y - lastMousePos.y) * speed.y;
				lastMousePos = Input.mousePosition;
				saveMousePos.y = Mathf.Clamp (saveMousePos.y, yMinLimit, yMaxLimit);
			}
		}
	}
	
	void MouseControl ()
	{
		if (!mouseActiveRect.Contains (Input.mousePosition)) {
			return;
		}
		if (Input.GetMouseButton (0)) {
			if (Input.GetMouseButtonDown (0)) {
				lastMousePos = Input.mousePosition;
			}
			saveMousePos.x += (Input.mousePosition.x - lastMousePos.x) * speed.x;
			saveMousePos.y -= (Input.mousePosition.y - lastMousePos.y) * speed.y;
			lastMousePos = Input.mousePosition;
			saveMousePos.y = Mathf.Clamp (saveMousePos.y, yMinLimit, yMaxLimit);		
		}
		distance -= Input.GetAxis ("Mouse ScrollWheel") * wheelSpeed;
	}
}
