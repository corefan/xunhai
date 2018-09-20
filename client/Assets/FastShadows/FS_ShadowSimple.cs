using UnityEngine;
using System.Collections;

public class FS_MeshKey{
	public bool isStatic;
	public Material mat;
	
	public FS_MeshKey(Material m, bool s){
		isStatic = s;
		mat = m;
	}
}

[AddComponentMenu("Fast Shadows/Simple Shadow")]
public class FS_ShadowSimple : MonoBehaviour {
	[HideInInspector()] public float maxProjectionDistance = 100f;
	[HideInInspector()] public float girth = 2f;
	[HideInInspector()] public float shadowHoverHeight = .02f;
	public LayerMask layerMask = ~0;
	[HideInInspector()] public Material shadowMaterial;
	[HideInInspector()] public bool isStatic = false;
	
	[HideInInspector()] public bool useLightSource = false;
	[HideInInspector()] public GameObject lightSource = null;
	[HideInInspector()] public Vector3 lightDirection = new Vector3(0f, -1, 0f);
	[HideInInspector()] public bool isPerspectiveProjection = false;
	[HideInInspector()] public bool doVisibilityCulling = false;
	 public Vector3 shadowpostion = new Vector3(0f, 0.02f, 0f);
	
	[HideInInspector()] public Rect uvs = new Rect(0f, 0f, 1f, 1f);
	
	float _girth;
	bool _isStatic;
	
	Vector3 _lightDirection = Vector3.zero;
	
	bool isGoodPlaneIntersect = false;
	Color gizmoColor = Color.white;
	
	//corner points of the generated quad
	Vector3[] _corners = new Vector3[4];
	
	Ray r = new Ray();
	RaycastHit rh = new RaycastHit();
	Bounds bounds = new Bounds();
	
	public Vector3[] corners{
		get {return _corners;}
	}	
	
	Color _color = new Color(1f,1f,1f,0f);
	public Color color{
		get {return _color;}
	}
	
	Vector3 _normal;
	public Vector3 normal{
		get {return _normal;}
	}
	
	// four points girth distance from shadowcaster
	GameObject[] cornerGOs = new GameObject[4];
	GameObject shadowCaster;
	Plane shadowPlane = new Plane();
	FS_MeshKey meshKey;
	
	void Awake () {
		_isStatic = isStatic;
		if (shadowMaterial == null){
			shadowMaterial = (Material) Resources.Load("FS_ShadowMaterial");
			if (shadowMaterial == null) Debug.LogWarning("Shadow Material is not set for " + name);
		} 
		if (isStatic){
			CalculateShadowGeometry();
		}
	}
	
	public void CalculateShadowGeometry(){
		Vector3 proj;
		if (shadowMaterial == null){
			return;
		}
		
		if (useLightSource && lightSource == null){
			useLightSource = false;
			Debug.LogWarning("No light source object given using light direction vector.");
		}
		if (useLightSource){
			Vector3 ls = transform.position - lightSource.transform.position;
			float mag = ls.magnitude;
			if (mag != 0f){
				lightDirection = ls / mag;
			} else {
				return; //object is on top of light source	
			}
		} else if (lightDirection != _lightDirection || lightDirection == Vector3.zero){ //light direction is dirty
			if (lightDirection == Vector3.zero){
				Debug.LogWarning("Light Direction vector cannot be zero. assuming -y.");
				lightDirection = -Vector3.up;
			}
			lightDirection.Normalize();	
			_lightDirection = lightDirection;
		}
		
		if (shadowCaster == null || girth != _girth){
			if (shadowCaster == null){
				shadowCaster = new GameObject("shadowSimple");
				cornerGOs = new GameObject[4];
				for (int i = 0; i < 4; i++){
					GameObject c = cornerGOs[i] = new GameObject("c" + i);	
					c.transform.parent = shadowCaster.transform;
				}
				shadowCaster.transform.parent = transform;		
				shadowCaster.transform.localPosition = shadowpostion;

				shadowCaster.transform.localRotation = Quaternion.identity;
				shadowCaster.transform.localScale = Vector3.one;
			}
			if (Mathf.Abs(Vector3.Dot(transform.forward, lightDirection)) < .9f){
				proj = transform.forward - Vector3.Dot(transform.forward, lightDirection) * lightDirection; 
			} else {
				proj = transform.up - Vector3.Dot(transform.up, lightDirection) * lightDirection;
			}
			shadowCaster.transform.rotation = Quaternion.LookRotation(proj, -lightDirection);			
			cornerGOs[0].transform.position =  shadowCaster.transform.position + girth * (shadowCaster.transform.forward - shadowCaster.transform.right);
			cornerGOs[1].transform.position =  shadowCaster.transform.position + girth * (shadowCaster.transform.forward + shadowCaster.transform.right);
			cornerGOs[2].transform.position =  shadowCaster.transform.position + girth * (-shadowCaster.transform.forward + shadowCaster.transform.right);
			cornerGOs[3].transform.position =  shadowCaster.transform.position + girth * (-shadowCaster.transform.forward - shadowCaster.transform.right);						
			_girth = girth;
		}		
		Transform t = shadowCaster.transform;		
		
		r.origin = t.position;
		r.direction = lightDirection;
		
		if (maxProjectionDistance > 0f && Physics.Raycast(r, out rh, maxProjectionDistance, layerMask)){
			if (doVisibilityCulling && !isPerspectiveProjection){
				Plane[] camPlanes = FS_ShadowManager.Manager().getCameraFustrumPlanes();
				bounds.center = rh.point;
				bounds.size = new Vector3(2f*girth,2f*girth,2f*girth);
				if (!GeometryUtility.TestPlanesAABB(camPlanes,bounds)){
					return;	
				}
			}
			
			// Rotate Shadowcaster 
			//project forward or up vector onto a plane whos normal is lightDirection
			if (Mathf.Abs(Vector3.Dot(transform.forward, lightDirection)) < .9f){
				proj = transform.forward - Vector3.Dot(transform.forward, lightDirection) * lightDirection;
			} else {
				proj = transform.up - Vector3.Dot(transform.up, lightDirection) * lightDirection;
			}
			shadowCaster.transform.rotation = Quaternion.Lerp(shadowCaster.transform.rotation, Quaternion.LookRotation(proj, -lightDirection), .01f);			
			
			float alpha;
			float dist = rh.distance - shadowHoverHeight;
			alpha = 1.0f - dist / maxProjectionDistance;
			if (alpha < 0f)
				return;
			alpha = Mathf.Clamp01(alpha);
			_color.a = alpha;
			
			_normal = rh.normal;
			Vector3 hitPoint = rh.point - shadowHoverHeight * lightDirection;
			shadowPlane.SetNormalAndPosition(_normal, hitPoint);
			
			isGoodPlaneIntersect = true;
			
			float rayDist = 0f;
			float mag = 0f;
			if (useLightSource && isPerspectiveProjection){
				r.origin = lightSource.transform.position;
				Vector3 lightSource2Corner = cornerGOs[0].transform.position - lightSource.transform.position;
				mag = lightSource2Corner.magnitude;
				r.direction = lightSource2Corner / mag;
				isGoodPlaneIntersect = isGoodPlaneIntersect && shadowPlane.Raycast(r, out rayDist);
				_corners[0] = r.origin + r.direction * rayDist;	
				
				lightSource2Corner = cornerGOs[1].transform.position - lightSource.transform.position;
				r.direction = lightSource2Corner / mag;
				isGoodPlaneIntersect = isGoodPlaneIntersect && shadowPlane.Raycast(r, out rayDist);
				_corners[1] = r.origin + r.direction * rayDist;
				
				lightSource2Corner = cornerGOs[2].transform.position - lightSource.transform.position;
				r.direction = lightSource2Corner / mag;
				isGoodPlaneIntersect = isGoodPlaneIntersect && shadowPlane.Raycast(r, out rayDist);
				_corners[2] = r.origin + r.direction * rayDist;

				lightSource2Corner = cornerGOs[3].transform.position - lightSource.transform.position;
				r.direction = lightSource2Corner / mag;
				isGoodPlaneIntersect = isGoodPlaneIntersect && shadowPlane.Raycast(r, out rayDist);
				_corners[3] = r.origin + r.direction * rayDist;
				if (doVisibilityCulling){
					Plane[] camPlanes = FS_ShadowManager.Manager().getCameraFustrumPlanes();
					bounds.center = rh.point;
					bounds.size = Vector3.zero;
					bounds.Encapsulate(_corners[0]);
					bounds.Encapsulate(_corners[1]);
					bounds.Encapsulate(_corners[2]);
					bounds.Encapsulate(_corners[3]);
					if (!GeometryUtility.TestPlanesAABB(camPlanes,bounds)){
						return;	
					}
				}
			} else {
				//r.origin = cornerGOs[0].transform.position;
                //isGoodPlaneIntersect = shadowPlane.Raycast(r, out rayDist);
                //if (isGoodPlaneIntersect == false && rayDist == 0f)
                //    return;
                //else
                //    isGoodPlaneIntersect = true;
                //_corners[0] = r.origin + r.direction * rayDist;

                //r.origin = cornerGOs[1].transform.position;
                //isGoodPlaneIntersect = shadowPlane.Raycast(r, out rayDist);
                //if (isGoodPlaneIntersect == false && rayDist == 0f)
                //    return;
                //else
                //    isGoodPlaneIntersect = true;
                //_corners[1] = r.origin + r.direction * rayDist;

                //r.origin = cornerGOs[2].transform.position;
                //isGoodPlaneIntersect = shadowPlane.Raycast(r, out rayDist);
                //if (isGoodPlaneIntersect == false && rayDist == 0f)
                //    return;
                //else
                //    isGoodPlaneIntersect = true;
                //_corners[2] = r.origin + r.direction * rayDist;

                //r.origin = cornerGOs[3].transform.position;
                //isGoodPlaneIntersect = shadowPlane.Raycast(r, out rayDist);
                //if (isGoodPlaneIntersect == false && rayDist == 0f)
                //    return;
                //else
                //    isGoodPlaneIntersect = true;
                //_corners[3] = r.origin + r.direction * rayDist;

                r.origin = cornerGOs[0].transform.position;
                _corners[0] = r.origin;
                r.origin = cornerGOs[1].transform.position;
                _corners[1] = r.origin;
                r.origin = cornerGOs[2].transform.position;
                _corners[2] = r.origin;
                r.origin = cornerGOs[3].transform.position;
                _corners[3] = r.origin;
			}
			
			if (isGoodPlaneIntersect){
				if (meshKey == null || meshKey.mat != shadowMaterial || meshKey.isStatic != isStatic) meshKey = new FS_MeshKey(shadowMaterial, isStatic);
				FS_ShadowManager.Manager().registerGeometry(this, meshKey);	
				gizmoColor = Color.white;
			} else {
				gizmoColor = Color.magenta;
			}
		} else {
			isGoodPlaneIntersect = false;
			gizmoColor = Color.red;	
		}
	}
	
	void Update(){
		if (_isStatic != isStatic){
			if (isStatic == true){ //becoming static
				meshKey = new FS_MeshKey(shadowMaterial, isStatic);
				CalculateShadowGeometry();
				FS_ShadowManager.Manager().RecalculateStaticGeometry(null, meshKey);	
				
			} else { //becoming dynamic
				FS_MeshKey oldMeshKey = meshKey;
				meshKey = new FS_MeshKey(shadowMaterial, isStatic);
				FS_ShadowManager.Manager().RecalculateStaticGeometry(this, oldMeshKey);	
			}
			_isStatic = isStatic;
		}
		if (!isStatic){
			CalculateShadowGeometry();
		}
	}
	
	void OnDrawGizmos(){
		if (shadowCaster != null){
			Gizmos.color = Color.yellow;
			Gizmos.DrawRay(shadowCaster.transform.position, shadowCaster.transform.up);
			Gizmos.DrawRay(shadowCaster.transform.position, shadowCaster.transform.forward);
			Gizmos.DrawRay(shadowCaster.transform.position, shadowCaster.transform.right);
			Gizmos.color = Color.blue;
			Gizmos.DrawRay(shadowCaster.transform.position, transform.forward);
			Gizmos.color = gizmoColor;
			if (isGoodPlaneIntersect){
				Gizmos.DrawLine(cornerGOs[0].transform.position, corners[0]);
				Gizmos.DrawLine(cornerGOs[1].transform.position, corners[1]);
				Gizmos.DrawLine(cornerGOs[2].transform.position, corners[2]);
				Gizmos.DrawLine(cornerGOs[3].transform.position, corners[3]);
				
				Gizmos.DrawLine(cornerGOs[0].transform.position, cornerGOs[1].transform.position);
				Gizmos.DrawLine(cornerGOs[1].transform.position, cornerGOs[2].transform.position);
				Gizmos.DrawLine(cornerGOs[2].transform.position, cornerGOs[3].transform.position);
				Gizmos.DrawLine(cornerGOs[3].transform.position, cornerGOs[0].transform.position);
				
				Gizmos.DrawLine(corners[0], corners[1]);
				Gizmos.DrawLine(corners[1], corners[2]);
				Gizmos.DrawLine(corners[2], corners[3]);
				Gizmos.DrawLine(corners[3], corners[0]);
				
				//draw the bounds
				//Gizmos.color = Color.blue;
				//Gizmos.DrawWireCube(bounds.center,bounds.size);
			}
		}
	}
}
