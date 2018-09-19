
using System.Collections.Generic;
using System.Linq;
using System.Text;
#if UNITY_EDITOR
using UnityEditor;
#endif
using UnityEngine;

namespace CTEUtil.CTE {
    [System.Serializable]
    public class UBillBoard<T> where T : UBillBoardInstance {

        public delegate void RemoveCallBack(T t);

        [SerializeField]
        protected GameObject m_GameObject;
        [SerializeField]
        MeshFilter m_MeshFilter;
        [SerializeField]
        MeshRenderer m_MeshRenderer;
        [SerializeField]
        UTerrain m_Terrain;
        [SerializeField]
        Mesh m_Mesh;
        [SerializeField]
        protected List<T> m_Instances = new List<T>();
        [SerializeField]
        Vector3 m_Center;

        internal Transform transform {
            get {
                return m_GameObject.transform;
            }
        }

        internal MeshFilter meshFilter {
            get {
                return m_MeshFilter;
            }
        }

        public T[] instances {
            get {
                return m_Instances.ToArray();
            }
        }
        [SerializeField]
        bool m_Active = true;
        internal bool active {
            get {
                return m_Active;
            }
            set {
                m_Active = value;
                m_GameObject.SetActive(value);
            }
        }
        internal Mesh mesh {
            get {
                if (m_Mesh == null) {
                    UpdateMesh();
                }
                return m_Mesh;
            }
        }
        internal Vector3 center {
            get {
                return m_Center;
            }
        }
        internal int length {
            get {
                return m_Instances.Count;
            }
        }
        internal virtual void Initialize(Transform parent, UTerrain terrain, Material mat, bool newObj) {
            if (m_Terrain == null || newObj) {
                m_Terrain = terrain;
            }
            if (m_GameObject == null || newObj) {
                m_GameObject = new GameObject("BillBoard", typeof(MeshFilter), typeof(MeshRenderer));
                m_MeshFilter = m_GameObject.GetComponent<MeshFilter>();
                m_MeshRenderer = m_GameObject.GetComponent<MeshRenderer>();
                m_GameObject.transform.parent = parent;
                m_GameObject.transform.localPosition = Vector3.zero;
                m_GameObject.transform.eulerAngles = Vector3.zero;
                m_GameObject.transform.localScale = Vector3.one;
                m_GameObject.hideFlags = HideFlags.DontSave;
            }
#if UNITY_EDITOR
            EditorUtility.SetSelectedWireframeHidden(m_GameObject.GetComponent<Renderer>(), true);
#endif
            UpdateMesh();
#if UNITY_EDITOR
            if (m_MeshFilter.sharedMesh != mesh || newObj)
                m_MeshFilter.sharedMesh = mesh;
            if (m_MeshRenderer.sharedMaterial != mat || newObj)
                m_MeshRenderer.sharedMaterial = mat;
#else
            if (m_MeshFilter.mesh == null || newObj)
                m_MeshFilter.mesh = mesh;
            if (m_MeshRenderer.material == null || newObj)
                m_MeshRenderer.material = mat;
#endif

            active = m_Active;
        }
        public UBillBoard(Transform parent, UTerrain terrain, Material mat, T centerInstance) {
            m_Center = centerInstance.position;
            Add(centerInstance);
            Initialize(parent, terrain, mat, false);
            UpdateMesh();
        }
        internal virtual void Draw(Material mat, bool show) {
            if (m_MeshRenderer.enabled != show){
                m_MeshRenderer.enabled = show;
            }
            if (!show)
                return;
#if UNITY_EDITOR
            GameObjectUtility.SetStaticEditorFlags(m_GameObject, StaticEditorFlags.BatchingStatic);
            if (m_MeshFilter.sharedMesh == null)
                m_MeshFilter.sharedMesh = mesh;
            if (m_MeshRenderer.sharedMaterial == null)
                m_MeshRenderer.sharedMaterial = mat;
#else
            if (m_MeshFilter.mesh == null)
                m_MeshFilter.mesh = mesh;
            if (m_MeshRenderer.material == null || m_MeshRenderer.material != mat)
                m_MeshRenderer.material = mat;
#endif
        }
        internal void UpdateMesh() {
            if (m_Mesh == null) {
                m_Mesh = new Mesh();
#if UNITY_EDITOR
                m_MeshFilter.sharedMesh = m_Mesh;
#else
                m_MeshFilter.mesh = m_Mesh;
#endif
            }
            m_Mesh.Clear();
            
            int length = m_Instances.Count;
            Vector3[] verts = new Vector3[length * 4];
            int[] triangles = new int[length * 6];
            Vector2[] uv = new Vector2[length * 4];
            Vector2[] uv2 = new Vector2[length * 4];
            Vector4[] tangents = new Vector4[length * 4];
            for (int i = 0; i < length; i++) {
                UBillBoardInstance instance = m_Instances[i];
                Vector3 pos = instance.position;
                Vector3 newSize = Vector3.Scale(instance.scale, instance.size);
                int vi = i * 4;

                float newHeight = newSize.y;
                float newHalfWidth = newSize.x * 0.5f;
                verts[vi + 0] = pos + new Vector3(-newHalfWidth, 0, 0);
                verts[vi + 1] = pos + new Vector3(newHalfWidth, 0, 0);
                verts[vi + 2] = pos + new Vector3(newHalfWidth, newHeight, 0);
                verts[vi + 3] = pos + new Vector3(-newHalfWidth, newHeight, 0);

                Vector4 tangent = pos;
                tangent.w = 0;
                tangents[vi + 0] = tangent;
                tangent.w = 1;
                tangents[vi + 1] = tangent;
                tangent.w = 2;
                tangents[vi + 2] = tangent;
                tangent.w = 3;
                tangents[vi + 3] = tangent;

                uv[vi + 0] = new Vector2(1, 0);
                uv[vi + 1] = new Vector2(0, 0);
                uv[vi + 2] = new Vector2(0, 1);
                uv[vi + 3] = new Vector2(1, 1);

                Vector2 newUV = new Vector2(newSize.x, newSize.y);

                uv2[vi + 0] = newUV;
                uv2[vi + 1] = newUV;
                uv2[vi + 2] = newUV;
                uv2[vi + 3] = newUV;

                int ti = i * 6;
                triangles[ti + 0] = vi + 0;
                triangles[ti + 1] = vi + 1;
                triangles[ti + 2] = vi + 2;
                triangles[ti + 3] = vi + 2;
                triangles[ti + 4] = vi + 3;
                triangles[ti + 5] = vi + 0;
            }
            m_Mesh.vertices = verts;
            m_Mesh.uv = uv;
            m_Mesh.uv2 = uv2;
            m_Mesh.tangents = tangents;
            m_Mesh.triangles = triangles;
            m_Mesh.RecalculateBounds();
            m_Mesh.RecalculateNormals();
        }
        internal void Add(T instance){
            m_Instances.Add(instance);
        }

        internal void Remove(Vector3 pos, float radius) {
            List<T> toRemove = new List<T>();
            for (int i = 0, max = m_Instances.Count; i < max; i++) {
                T instance = m_Instances[i];
                Vector3 insPos = instance.position;
                if ((insPos + m_Terrain.transform.position - pos).magnitude < radius) {
                    toRemove.Add(instance);
                }
            }
            if (toRemove.Count > 0) {
                foreach (T ins in toRemove) {
                    ins.Dispose();
                    m_Instances.Remove(ins);
                }
                UpdateMesh();
            }
        }
        public void Dispose() {
            if (m_Mesh != null) {
#if UNITY_EDITOR
                Mesh.DestroyImmediate(m_Mesh, true);
#else
                Mesh.Destroy(m_Mesh);
#endif
            }
            if (m_GameObject != null){
#if UNITY_EDITOR
                GameObject.DestroyImmediate(m_GameObject);
#else
                GameObject.Destroy(m_GameObject);
#endif
            }
            m_Instances.ForEach(ins => ins.Dispose());
        }
    }
    [System.Serializable]
    public class UBillBoardInstance : System.IDisposable {
        public UBillBoardInstance(Vector3 pos, Vector3 scale, Vector3 size) {
            m_Size = size;
            m_Pos = pos;
            m_Scale = scale;
        }

        [SerializeField]
        Vector3 m_Pos;
        public Vector3 position {
            get {
                return m_Pos;
            }
        }
        [SerializeField]
        Vector3 m_Scale;
        public Vector3 scale {
            get {
                return m_Scale;
            }
        }
        [SerializeField]
        Vector3 m_Size;
        public Vector3 size {
            get {
                return m_Size;
            }
        }
        public virtual void Dispose() {
            
        }
    }

}
