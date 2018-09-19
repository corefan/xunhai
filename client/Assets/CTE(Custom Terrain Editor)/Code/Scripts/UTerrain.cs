using UnityEngine;
using System.Collections;
using System.Linq;
using System.Collections.Generic;

#if UNITY_EDITOR
using UnityEditor;
using UnityEditorInternal;
#endif

namespace CTEUtil.CTE {
	#if UNITY_EDITOR
	[ExecuteInEditMode, RequireComponent(typeof(MeshCollider)), AddComponentMenu("CTE")]
	#endif
	public class UTerrain : MonoBehaviour {
		[SerializeField, HideInInspector]
		Vector3 m_Rot;
		[SerializeField, HideInInspector]
		UTerrainData m_Data;
		[SerializeField, HideInInspector]
		Mesh[] m_SubMesh;
		[SerializeField, HideInInspector]
		bool m_DrawTree = true;
		[SerializeField, HideInInspector]
		bool m_DrawGrass = true;
		[SerializeField, HideInInspector]
		Mesh m_NewMesh;
		[SerializeField, HideInInspector]
		Material[] m_OldMaterials = new Material[0];
		internal Material[] oldMaterials {
			get {
				return m_OldMaterials;
			}
		}
		public UTerrainData data {
			get {
				return m_Data;
			}
		}
		public Mesh mesh {
			get {
				return m_NewMesh;
			}
		}
		public bool drawTree {
			get {
				return m_DrawTree;
			}
			set {
				m_DrawTree = value;
				if (enabled && m_Data.treeData.active != value)
					m_Data.treeData.active = value;
			}
		}
		public bool drawGrass {
			get {
				return m_DrawGrass;
			}
			set {
				m_DrawGrass = value;
				if (enabled && m_Data.grassData.active != value)
					m_Data.grassData.active = value;
			}
		}
		
		[SerializeField, HideInInspector]
		MeshFilter m_MF;
		#if UNITY_EDITOR
		const string dataPath = "Assets/CTE(Custom Terrain Editor)/Datas";
        [SerializeField, HideInInspector]
        bool m_Awake = false;
        [SerializeField, HideInInspector]
		Mesh m_OldMesh;
        [SerializeField, HideInInspector]
		MeshCollider m_MC;
		[SerializeField, HideInInspector]
		MeshRenderer m_MR;
        #endif
        void Awake() {
			if (transform.lossyScale != Vector3.one)
				return;
			if (GetComponents<UTerrain>().Length > 1) {
				Debug.LogError("UTerrain has been add.");
				#if UNITY_EDITOR
				DestroyImmediate(this);
				#else
				Destroy(this);
				#endif
				return;
			}
			if (transform.lossyScale != Vector3.one) {
				Debug.LogError("Transform.lossyScale != Vector3.one!");
				#if UNITY_EDITOR
				DestroyImmediate(this);
				#else
				Destroy(this);
				#endif
				return;
			}
			#if UNITY_EDITOR
			if (!EditorApplication.isPlaying && !m_Awake) {
				if (!CheckComponent())
					return;
				CreateData();
			}
			#endif
			m_Data.Initialize(this);
			#if UNITY_EDITOR
			m_Awake = true;
			#endif
			m_Rot = transform.eulerAngles;
			if (m_MF == null){
				m_MF = gameObject.AddComponent<MeshFilter>();
			}
			m_MF.mesh = m_NewMesh;
            #if UNITY_EDITOR
            if (EditorApplication.isPlaying) return;
			m_MF.hideFlags = HideFlags.HideInInspector;
			m_MR.hideFlags = HideFlags.HideInInspector;
			#endif
		}
		void OnEnable() {
			if (transform.lossyScale != Vector3.one)
				return;
			if (drawGrass && m_Data.gameObject != null)
				m_Data.grassData.active = true;
			if (drawTree && m_Data.gameObject != null)
				m_Data.treeData.active = true;
			#if UNITY_EDITOR
			if (!EditorApplication.isPlaying)
				EditorApplication.update += m_Data.Draw;
			#endif
		}
		
		void OnWillRenderObject() {
			m_Data.Draw();
		}
		void Update() {
			if (transform.eulerAngles != m_Rot)
				transform.eulerAngles = m_Rot;
			if (transform.localScale != Vector3.one)
				transform.localScale = Vector3.one;
			#if UNITY_EDITOR
			if (!data.active) {
				for (int i = 0, max = mesh.subMeshCount; i < max; i++) {
					#if UNITY_5 && !UNITY_5_0
					Graphics.DrawMesh(mesh, transform.localToWorldMatrix, oldMaterials[i], gameObject.layer, SceneView.lastActiveSceneView.camera, i);
					#else
					Graphics.DrawMesh(mesh, transform.localToWorldMatrix, oldMaterials[i], gameObject.layer, SceneView.currentDrawingSceneView.camera, i);
					#endif
				}
			}
			#endif
		}
		void OnDisable() {
			if (transform.lossyScale != Vector3.one)
				return;
			if (m_Data.textureData.terrain == this) {
				if (m_Data.gameObject != null)
					m_Data.grassData.active = false;
				if (m_Data.gameObject != null)
					m_Data.treeData.active = false;
			}
			#if UNITY_EDITOR
			if (!EditorApplication.isPlaying){
				EditorApplication.update -= m_Data.Draw;
				if (m_MF == null)
					m_MF = gameObject.AddComponent<MeshFilter>();
				if (m_MR == null)
					m_MR = gameObject.AddComponent<MeshRenderer>();
				m_MF.hideFlags = HideFlags.HideInInspector;
				m_MR.hideFlags = HideFlags.HideInInspector;
			}
			#endif
		}
		void OnDestroy() {
			if (transform.lossyScale != Vector3.one)
				return;
			#if UNITY_EDITOR
			if (EditorApplication.isPlaying || !m_Awake)
				return;
			m_MF.hideFlags = HideFlags.None;
			m_MR.hideFlags = HideFlags.None;
			m_MF.sharedMesh = m_OldMesh;
			m_MR.sharedMaterials = m_OldMaterials;
			m_MC.sharedMesh = m_OldMesh;
			EditorUtility.SetSelectedWireframeHidden(m_MR, false);
			PrefabUtility.DisconnectPrefabInstance(gameObject);
			#endif
			if (m_Data.textureData.terrain == this) {
				m_Data.Dispose();
			}
		}
		public void ResetMesh() {
			#if UNITY_EDITOR
			m_NewMesh.Clear();
			m_NewMesh.subMeshCount = m_OldMesh.subMeshCount;
			m_NewMesh.vertices = m_OldMesh.vertices;
			m_NewMesh.normals = m_OldMesh.normals;
			m_NewMesh.tangents = m_OldMesh.tangents;
			for (int i = 0, max = m_OldMesh.subMeshCount; i < max; i++) {
				m_NewMesh.SetTriangles(m_OldMesh.GetTriangles(i), i);
			}
			m_NewMesh.bounds = m_OldMesh.bounds;
			m_NewMesh.colors = m_OldMesh.colors;
			m_NewMesh.uv = m_OldMesh.uv;
			m_NewMesh.uv2 = m_OldMesh.uv2;
			m_MC.enabled = false;
			m_MC.enabled = true;
			UpdateSubMesh();
			#else
			Debug.LogError("'UTerrain.ResetMesh()' only can be used in editor.");
			#endif
		}
		public Mesh GetSubMesh(int index) {
			if (m_SubMesh[index] == null) {
				m_SubMesh[index] = UpdateSubMesh(index, m_SubMesh[index]);
			}
			return m_SubMesh[index];
		}
		public Mesh[] GetAllSubMesh() {
			return m_SubMesh;
		}
		public void UpdateSubMesh() {
			if (m_SubMesh == null) {
				m_SubMesh = new Mesh[m_NewMesh.subMeshCount];
			}
			for (int i = 0, max = m_SubMesh.Length; i < max; i++) {
				m_SubMesh[i] = UpdateSubMesh(i, m_SubMesh[i]);
			}
		}
		Mesh UpdateSubMesh(int index, Mesh subMesh) {
			if (subMesh == null){
				subMesh = Instantiate(m_NewMesh) as Mesh;
				subMesh.triangles = m_NewMesh.GetTriangles(index);
				subMesh.name = "SubMesh" + index.ToString();
			}
			else {
				subMesh.vertices = m_NewMesh.vertices;
				subMesh.RecalculateNormals();
				subMesh.RecalculateBounds();
			}
			return subMesh;
		}
		#if UNITY_EDITOR
		bool CheckComponent() {
			m_MF = GetComponent<MeshFilter>();
			if (m_MF == null) {
				Debug.LogError("Require Component 'MeshFilter'.");
				DestroyImmediate(this);
				return false;
			}
			if (m_MF.sharedMesh == null) {
				Debug.LogError("MeshFilter.Mesh is null.");
				DestroyImmediate(this);
				return false;
			}
			m_OldMesh = m_MF.sharedMesh;
			m_MR = GetComponent<MeshRenderer>();
			if (m_MR == null) {
				Debug.LogError("Require Component 'MeshRenderer'.");
				DestroyImmediate(this);
				return false;
			}
			if (m_MR.sharedMaterials.Length == 0) {
				Debug.LogError("MeshRenderer.Materials.Length is 0.");
				DestroyImmediate(this);
				return false;
			}
			m_OldMaterials = m_MR.sharedMaterials;
			m_MR.hideFlags = HideFlags.HideInInspector;
			m_MR.sharedMaterials = new Material[0];
			m_MF.sharedMesh = m_NewMesh;
			m_MF.hideFlags = HideFlags.HideInInspector;
			m_MC = GetComponent<MeshCollider>();
			return true;
		}
		void CreateData() {
			int i, max;
			UTools.CheckAndCreateFolder(dataPath);
			string path = AssetDatabase.GUIDToAssetPath(AssetDatabase.CreateFolder(dataPath, name));
			m_NewMesh = new Mesh();
			m_NewMesh.name = "Mesh";
			m_NewMesh.subMeshCount = m_OldMesh.subMeshCount;
			m_NewMesh.vertices = m_OldMesh.vertices;
			m_NewMesh.normals = m_OldMesh.normals;
			m_NewMesh.tangents = m_OldMesh.tangents;
			for (i = 0, max = m_OldMesh.subMeshCount; i < max; i++) {
				m_NewMesh.SetTriangles(m_OldMesh.GetTriangles(i), i);
			}
			m_NewMesh.bounds = m_OldMesh.bounds;
			m_NewMesh.colors = m_OldMesh.colors;
			m_NewMesh.uv = m_OldMesh.uv;
			m_NewMesh.uv2 = m_OldMesh.uv2;
			AssetDatabase.CreateAsset(m_NewMesh, path + "/" + m_NewMesh.name + ".asset");
			
			UpdateSubMesh();
			
			for (i = 0, max = m_SubMesh.Length; i < max; i++) {
				Mesh sm = m_SubMesh[i];
				AssetDatabase.CreateAsset(sm, path + "/" + sm.name + ".asset");
			}
			m_MC.sharedMesh = m_NewMesh;
			
			m_Data = ScriptableObject.CreateInstance<UTerrainData>();
			m_Data.name = "Data";
			AssetDatabase.CreateAsset(m_Data, path + "/" + m_Data.name + ".asset");
			AssetDatabase.ImportAsset(path);
		}
		[ContextMenu("Copy Component")]
		void CoverCopyComponent() {
			Debug.LogWarning("UTerrain can't be copied.");
		}
		#endif
	}
}
