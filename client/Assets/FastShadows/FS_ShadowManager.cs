using UnityEngine;
using System.Collections;
using System.Collections.Generic;

//[ExecuteInEditMode()] 
public class FS_ShadowManager : MonoBehaviour {
    private static FS_ShadowManager _manager;
	private Dictionary<Material,FS_ShadowManagerMesh> shadowMeshes = new Dictionary<Material,FS_ShadowManagerMesh>();
	private Dictionary<Material,FS_ShadowManagerMesh> shadowMeshesStatic = new Dictionary<Material,FS_ShadowManagerMesh>();
	
	void Start(){
        FS_ShadowManager[] ms = FindObjectsOfType(typeof(FS_ShadowManager)) as FS_ShadowManager[];
		if (ms.Length > 1){
			Debug.LogWarning("There should only be one FS_ShadowManger in the scene. Found " + ms.Length);	
		}
	}
	
	void OnApplicationQuit(){
		shadowMeshes.Clear();
		shadowMeshesStatic.Clear();
	}
	
	//Singleton, returns this manager.
    public static FS_ShadowManager Manager(){
        if (_manager == null) {
            FS_ShadowManager sm = FindObjectOfType(typeof(FS_ShadowManager)) as FS_ShadowManager;
			if (sm == null){
            	GameObject go = new GameObject("FS_ShadowManager");
				_manager = go.AddComponent<FS_ShadowManager>();
			} else {
				_manager = sm;	
			}
        }
        return _manager;
    }
	
	public void RecalculateStaticGeometry(FS_ShadowSimple removeShadow){
		FS_MeshKey mk = new FS_MeshKey(removeShadow.shadowMaterial, true);
		RecalculateStaticGeometry(removeShadow, mk);
	}
	
	public void RecalculateStaticGeometry(FS_ShadowSimple removeShadow, FS_MeshKey meshKey){
		if (shadowMeshesStatic.ContainsKey(meshKey.mat)){
            FS_ShadowManagerMesh sm = shadowMeshesStatic[meshKey.mat] as FS_ShadowManagerMesh;	
			if (removeShadow != null) sm.removeShadow(removeShadow);
			sm.recreateStaticGeometry();
		}
	}
	
	public void registerGeometry(FS_ShadowSimple s, FS_MeshKey meshKey){
		FS_ShadowManagerMesh m;
		if (meshKey.isStatic){
			if (!shadowMeshesStatic.ContainsKey(meshKey.mat)){
				GameObject g = new GameObject("ShadowMeshStatic_" + meshKey.mat.name);
				g.transform.parent = transform;
				m = g.AddComponent<FS_ShadowManagerMesh>();
				m.shadowMaterial = s.shadowMaterial;
				m.isStatic = true;
				shadowMeshesStatic.Add(meshKey.mat,m);				
			} else {
				m = (FS_ShadowManagerMesh) shadowMeshesStatic[meshKey.mat];	
			}
		} else {
			if (!shadowMeshes.ContainsKey(meshKey.mat)){
				GameObject g = new GameObject("ShadowMesh_" + meshKey.mat.name);
				g.transform.parent = transform;
				m = g.AddComponent<FS_ShadowManagerMesh>();
				m.shadowMaterial = s.shadowMaterial;
				m.isStatic = false;
				shadowMeshes.Add(meshKey.mat,m);
			} else {
				m = (FS_ShadowManagerMesh) shadowMeshes[meshKey.mat];	
			}
		}
		m.registerGeometry(s);		
	}
	
	int frameCalcedFustrum = 0;
	Plane[] fustrumPlanes;
	public Plane[] getCameraFustrumPlanes(){
		if (Time.frameCount != frameCalcedFustrum || fustrumPlanes == null){
			Camera mc = Camera.main;
			if (mc == null){
				Debug.LogWarning("No main camera could be found for visibility culling.");	
				fustrumPlanes = null;
			} else {
				fustrumPlanes = GeometryUtility.CalculateFrustumPlanes(mc);
				frameCalcedFustrum = Time.frameCount;
			}
		}
		return fustrumPlanes;
	}
}
