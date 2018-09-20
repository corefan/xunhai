using UnityEngine;
using System.Collections;
using System.Collections.Generic;

/*
	grow verts in blocks
	if needed verts > current grow by block
	if needed verts < .5 current block size leave
   
*/

//[ExecuteInEditMode()]
public class FS_ShadowManagerMesh : MonoBehaviour {
	public Material shadowMaterial;	
	public bool isStatic = false;
	
	int numShadows = 0;
	List<FS_ShadowSimple> shadows = new List<FS_ShadowSimple>();
	Mesh _mesh, _mesh1, _mesh2;
	bool pingPong;
	MeshFilter _filter;
	Renderer _ren;
	Vector3[] _verts;
	Vector2[] _uvs;
	Vector3[] _norms;	
	Color[] _colors;
	int[] _indices;
	
	
	public int getNumShadows(){
		return numShadows;	
	}
	
	public void Start(){
		if (isStatic){
			_CreateGeometry();			
		}
	}
	
	public void registerGeometry(FS_ShadowSimple s){
		if (s.shadowMaterial != shadowMaterial)
			Debug.LogError("Shadow did not have the same material");
		shadows.Add(s);
	}
	
	public void removeShadow(FS_ShadowSimple ss){
		shadows.Remove(ss);
	}
	
	public void recreateStaticGeometry(){
		_CreateGeometry();	
	}
	
	void LateUpdate(){
		if (!isStatic){
			_CreateGeometry();
		}
	}
	
	//mesh is double buffered to fix performance bug on iOS
	Mesh _GetMesh(){
		pingPong = !pingPong;
		if (pingPong){
			if (_mesh1 == null){
				_mesh1 = new Mesh();

				_mesh1.MarkDynamic();			

				_mesh1.hideFlags = HideFlags.DontSave;
			} else {
				_mesh1.Clear();
			}
			return _mesh1;
		} else {
			if (_mesh2 == null){
				_mesh2 = new Mesh();

				_mesh2.MarkDynamic();			
			
				_mesh2.hideFlags = HideFlags.DontSave;	
			} else {
				_mesh2.Clear();	
			}
			return _mesh2;
		}
	}
	
	int blockGrowSize = 64;
	
	void _CreateGeometry(){
		numShadows = shadows.Count;
        if (numShadows == 0 || shadows[0] == null) return;
        if (numShadows > 0) gameObject.layer = shadows[0].gameObject.layer;
		

		int count = numShadows * 4;
		_mesh = _GetMesh();

		if (_filter == null) _filter = GetComponent<MeshFilter>();
		if (_filter == null) _filter = gameObject.AddComponent<MeshFilter>();
		if (_ren == null) _ren = gameObject.GetComponent<MeshRenderer>();
		if (_ren == null){
			_ren = gameObject.AddComponent<MeshRenderer>();
			_ren.material = shadowMaterial;
		}

		if (count < 65000){
			int vidx, index;
			int indexCount = (count >> 1) * 3;
			if (_indices == null || _indices.Length != indexCount)_indices = new int[indexCount];
			
			bool doResize = false; // resize in blocks instead of every frame
			int currentSize = 0;
			if (_verts != null) currentSize = _verts.Length;
			if (count > currentSize || count < currentSize - blockGrowSize){
				doResize = true;
				count = (Mathf.FloorToInt(count / blockGrowSize) + 1) * blockGrowSize;
			}
	
			if (doResize){
				_verts = new Vector3[count];
				_uvs = new Vector2[count];
				_norms = new Vector3[count];				
				_colors = new Color[count];
			}
			
			vidx = index = 0;
			for (int i = 0; i < numShadows; i++){
				FS_ShadowSimple s = shadows[i];

				_verts[vidx] = s.corners[0];
				_verts[vidx + 1] = s.corners[1];
				_verts[vidx + 2] = s.corners[2];
				_verts[vidx + 3] = s.corners[3];
				
				_indices[index] = vidx;
				_indices[index + 1] = vidx + 1;
				_indices[index + 2] = vidx + 2;
				_indices[index + 3] = vidx + 2;
				_indices[index + 4] = vidx + 3;
				_indices[index + 5] = vidx;	
				
				_uvs[vidx].x = s.uvs.x;
				_uvs[vidx].y = s.uvs.y;
				_uvs[vidx + 1].x = s.uvs.x + s.uvs.width;
				_uvs[vidx + 1].y = s.uvs.y;
				_uvs[vidx + 2].x = s.uvs.x + s.uvs.width;
				_uvs[vidx + 2].y = s.uvs.y + s.uvs.height;
				_uvs[vidx + 3].x = s.uvs.x;
				_uvs[vidx + 3].y = s.uvs.y + s.uvs.height;				
				
				_norms[vidx] = s.normal;
				_norms[vidx + 1] = s.normal;
				_norms[vidx + 2] = s.normal;
				_norms[vidx + 3] = s.normal;
			
				_colors[vidx] = s.color;
				_colors[vidx + 1] = s.color;
				_colors[vidx + 2] = s.color;
				_colors[vidx + 3] = s.color;
			
				index += 6;
				vidx += 4;
			}

			if (doResize){
				_mesh.Clear(false);
			} else {
				_mesh.Clear(true);
			}
	
			// Set the mesh values
			_mesh.Clear();
			_mesh.name = "shadow mesh";

			_mesh.vertices = _verts;
			_mesh.uv = _uvs;
			_mesh.normals = _norms;	
			_mesh.colors = _colors;
			
			_mesh.triangles = _indices;
			_mesh.RecalculateBounds();			
			_filter.mesh = _mesh;
			if (!isStatic) shadows.Clear();			
		} else {
			if (_filter.mesh != null) _filter.mesh.Clear();
			Debug.LogError("Too many shadows. limit is " + (65000 / 4));
		}
	}
}
