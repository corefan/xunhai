

using System.Collections.Generic;
using System.Linq;
using System.Text;
#if UNITY_EDITOR
using UnityEditor;
#endif
using UnityEngine;

namespace CTEUtil.CTE {
    [System.Serializable]
    public sealed class UTextureData : System.IDisposable {
        [SerializeField]
        List<USubMesh> m_SubMeshes = new List<USubMesh>();
        [SerializeField]
        UTerrain m_Terrain;
        [SerializeField]
        GameObject m_GameObject;

        internal GameObject gameObject {
            get {
                return m_GameObject;
            }
        }
        [SerializeField]
        bool m_Active = true;
        public bool active {
            get {
                return m_Active;
            }
            set {
                m_Active = value;
                m_GameObject.SetActive(m_Active);
            }
        }
        public USubMesh[] subMeshes {
            get {
                return m_SubMeshes.ToArray();
            }
        }
        public UTerrain terrain {
            get {
                return m_Terrain;
            }
        }
        public  void Initialize(UTerrainData data) {
            Initialize(data, false);
        }
        internal void Initialize(UTerrainData data, bool newObj) {
            if (m_Terrain == null || newObj)
                m_Terrain = data.terrain;
            Transform trans = terrain.transform.Find("SubMesh");
            if (trans != null)
                m_GameObject = trans.gameObject;
            if (m_GameObject == null || newObj) {
                m_GameObject = new GameObject("SubMesh");
                m_GameObject.transform.parent = terrain.transform;
                m_GameObject.transform.localPosition = Vector3.zero;
                m_GameObject.transform.localEulerAngles = Vector3.zero;
                m_GameObject.transform.localScale = Vector3.one;
                m_GameObject.hideFlags = HideFlags.HideInHierarchy;
            }
            int i, max;
            if (m_SubMeshes.Count == 0) {
                for (i = 0, max = m_Terrain.GetAllSubMesh().Length; i < max; i++) {
                    m_SubMeshes.Add(new USubMesh(this, i));
                }
            }
            for (i = 0, max = m_SubMeshes.Count; i < max; i++) {
                m_SubMeshes[i].Initialize(this, i, newObj);
            }
            active = m_Active;
        }
        public void Draw() {
#if UNITY_EDITOR
            m_GameObject.hideFlags = HideFlags.HideInHierarchy;
#endif
            m_SubMeshes.ForEach(sm => sm.Draw());
        }
        public void Dispose() {
            m_SubMeshes.ForEach(sm => sm.Dispose());
            if (m_GameObject != null)
#if UNITY_EDITOR
                GameObject.DestroyImmediate(m_GameObject);
#else
                GameObject.Destroy(m_GameObject);
#endif
        }
    }
    [System.Serializable]
    public sealed class USubMesh : System.IDisposable {
        [SerializeField]
        UTerrain m_Terrain;
        [SerializeField]
        GameObject m_GameObject;
        [SerializeField]
        List<UPass> m_Passes = new List<UPass>();
        [SerializeField]
        Mesh m_Mesh;
        [SerializeField]
        int m_Index;
        internal GameObject gameObject {
            get {
                return m_GameObject;
            }
        }
        public CombineInstance combinInstance {
            get{
                return new CombineInstance() {mesh = m_Mesh,subMeshIndex = m_Index,transform = m_GameObject.transform.localToWorldMatrix
                };
            }
        }
        public Mesh mesh {
            get {
                return m_Mesh;
            }
        }
        public UPass[] passes {
            get {
                return m_Passes.ToArray();
            }
        }
        public UPass this[int index]{
            get {
                return m_Passes[index];
            }
            set {
                m_Passes[index] = value;
            }
        }
        public void Initialize(UTextureData data, int index) {
            Initialize(data, index, false);
        }
        internal void Initialize(UTextureData data, int index, bool newObj) {
            m_Mesh = data.terrain.GetSubMesh(index);
            m_Index = index;
            if (m_Terrain == null || newObj)
                m_Terrain = data.terrain;
            Transform trans = data.gameObject.transform.Find("SubMesh" + index.ToString());
            if (trans != null)
                m_GameObject = trans.gameObject;
            if (m_GameObject == null || newObj) {
                m_GameObject = new GameObject("SubMesh" + index.ToString(), typeof(MeshFilter), typeof(MeshRenderer));
                m_GameObject.transform.parent = data.gameObject.transform;
                m_GameObject.transform.localPosition = Vector3.zero;
                m_GameObject.transform.localEulerAngles = Vector3.zero;
                m_GameObject.transform.localScale = Vector3.one;
                m_GameObject.GetComponent<MeshFilter>().sharedMesh = m_Mesh;
                m_GameObject.hideFlags = HideFlags.None;
                if (m_Passes.Count > 0)
                    UpdateMats();
                else
#if UNITY_EDITOR
                    m_GameObject.GetComponent<MeshRenderer>().sharedMaterial = m_Terrain.oldMaterials[index];
#else
                    m_GameObject.GetComponent<MeshRenderer>().material = m_Terrain.oldMaterials[index];
#endif
            }
#if UNITY_EDITOR
            EditorUtility.SetSelectedWireframeHidden(m_GameObject.GetComponent<Renderer>(), true);
#endif
            m_Passes.ForEach(g => g.Initialize());
        }
        public USubMesh(UTextureData data, int index) {
            Initialize(data, index);
        }
        public void Add(UPass pass) {
            m_Passes.Add(pass);
            UpdatePasses();
            UpdateMats();
        }
        public void RemoveAt(int index) {
            m_Passes[index].Dispose();
            m_Passes.RemoveAt(index);
            UpdatePasses();
            UpdateMats();
        }
        public void Draw() {
            UpdateMats();
        }
        void UpdatePasses() {
            m_Passes.ForEach(pass => pass.UpdateMat());
        }
        void UpdateMats() {
            if (m_Passes.Count == 0)
                return;
#if UNITY_EDITOR
            m_GameObject.GetComponent<MeshRenderer>().sharedMaterials = m_Passes.Select(pass => pass.material).ToArray();
#else
            m_GameObject.GetComponent<MeshRenderer>().materials = m_Passes.Select(pass => pass.material).ToArray();
#endif
        }
        public void Dispose() {
            m_Passes.ForEach(p => p.Dispose());
            if (m_GameObject != null)
#if UNITY_EDITOR
                GameObject.DestroyImmediate(m_GameObject);
#else
                GameObject.Destroy(m_GameObject);
#endif
        }
    }

    [System.Serializable]
    public sealed class UPass : System.IDisposable {
        [SerializeField]
        Color m_SpecularColor = new Color(0.5f, 0.5f, 0.5f, 1);
        [SerializeField]
        float m_Shininess = 0.078125f;
        [SerializeField]
        UTexture[] m_Texs = new UTexture[4];
        [SerializeField]
        Material m_Mat;
        [SerializeField]
        Texture2D m_MixTex;
        [SerializeField]
        bool m_IsFirst = false;

        public float shininess {
            get {
                return m_Shininess;
            }
            set {
                if (m_Shininess != value) {
                    m_Shininess = value;
                    UpdateMat();
                }
            }
        }
        public Color specularColor {
            get {
                return m_SpecularColor;
            }
            set {
                if (m_SpecularColor != value) {
                    m_SpecularColor = value;
                    UpdateMat();
                }
            }
        }
        public UTexture[] textures {
            get {
                return m_Texs;
            }
        }
        public Texture2D mixTex {
            get {
                return m_MixTex;
            }
            set{
                m_MixTex = value;
            }
        }
        public Material material {
            get {
                if (m_Mat == null) {
                    UpdateMat();
                }
                return m_Mat;
            }
        }
        internal void UpdateMat() {
            bool isNormal = false;
            for (int i = 0, max = m_Texs.Length; i < max; i++) {
                if (m_Texs[i].normal != null) {
                    isNormal = true;
                    break;
                }
            }
            string str0 = "First";
            string str1 = "";
            if (isNormal)
                str1 = " Bumped Specular";
            if (!m_IsFirst)
                str0 = "Add";
            string shaderName = string.Format("Hidden/CTE/{0}Pass{1}", str0, str1);
            Shader shader = Shader.Find(shaderName);
            if (m_Mat == null) {
                m_Mat = new Material(shader);
            }
            m_Mat.shader = shader;
            if (isNormal) {
                m_Mat.SetTexture("_Normal0", m_Texs[0].normal);
                m_Mat.SetTextureScale("_Normal0", m_Texs[0].tiling);
                m_Mat.SetTextureOffset("_Normal0", m_Texs[0].offset);
                m_Mat.SetTexture("_Normal1", m_Texs[1].normal);
                m_Mat.SetTextureScale("_Normal1", m_Texs[2].tiling);
                m_Mat.SetTextureOffset("_Normal1", m_Texs[1].offset);
                m_Mat.SetTexture("_Normal2", m_Texs[2].normal);
                m_Mat.SetTextureScale("_Normal2", m_Texs[2].tiling);
                m_Mat.SetTextureOffset("_Normal2", m_Texs[2].offset);
                m_Mat.SetTexture("_Normal3", m_Texs[3].normal);
                m_Mat.SetTextureScale("_Normal3", m_Texs[3].tiling);
                m_Mat.SetTextureOffset("_Normal3", m_Texs[3].offset);
            }
            m_Mat.SetTexture("_Splat0", m_Texs[0].mainTex);
            m_Mat.SetTextureScale("_Splat0", m_Texs[0].tiling);
            m_Mat.SetTextureOffset("_Splat0", m_Texs[0].offset);
            m_Mat.SetTexture("_Splat1", m_Texs[1].mainTex);
            m_Mat.SetTextureScale("_Splat1", m_Texs[1].tiling);
            m_Mat.SetTextureOffset("_Splat1", m_Texs[1].offset);
            m_Mat.SetTexture("_Splat2", m_Texs[2].mainTex);
            m_Mat.SetTextureScale("_Splat2", m_Texs[2].tiling);
            m_Mat.SetTextureOffset("_Splat2", m_Texs[2].offset);
            m_Mat.SetTexture("_Splat3", m_Texs[3].mainTex);
            m_Mat.SetTextureScale("_Splat3", m_Texs[3].tiling);
            m_Mat.SetTextureOffset("_Splat3", m_Texs[3].offset);
            m_Mat.SetTexture("_Control", mixTex);
            if (m_IsFirst){
                m_Mat.mainTexture = m_Texs[0].mainTex;
                m_Mat.mainTextureScale = m_Texs[0].tiling;
                m_Mat.mainTextureOffset = m_Texs[0].offset;
            }
            m_Mat.SetColor("_SpecColor", m_SpecularColor);
            m_Mat.SetFloat("_Shininess", m_Shininess);
        }
        public void Paint(int x, int y, int blockWidth, int blockHeight, Color[] colors) {
            mixTex.SetPixels(x, y, blockWidth, blockHeight, colors);
            mixTex.Apply();
            
        }
        public void SetTexture(int index, UTexture tex) {
            m_Texs[index] = tex;
        }
        public void SetTextures(UTexture[] texs) {
            SetTextures(texs[0], texs[1], texs[2], texs[3]);
            UpdateMat();
        }
        public void SetTextures(UTexture tex0, UTexture tex1, UTexture tex2, UTexture tex3) {
            m_Texs = new UTexture[4] { tex0, tex1, tex2, tex3 };
            UpdateMat();
        }

        public UPass(UPass pass) {
            m_IsFirst = pass.m_IsFirst;
            int i, max;
            for (i = 0, max = m_Texs.Length; i < max; i++) {
                m_Texs[i] = pass.m_Texs[i];
            }
            m_MixTex = pass.m_MixTex;
            Initialize();
        }

        public UPass(bool firstPass, Texture2D mix) {
            m_IsFirst = firstPass;
            int i, max;
            for (i = 0, max = m_Texs.Length; i < max; i++) {
                m_Texs[i] = new UTexture(null);
            }
            m_MixTex = mix;
            Initialize();
        }
        public UPass() {
            int i, max;
            for (i = 0, max = m_Texs.Length; i < max; i++) {
                m_Texs[i] = new UTexture(null);
            }
        }


        public void Initialize() {
            UpdateMat();
        }
        public void Dispose() {
            if (m_Mat != null)
#if UNITY_EDITOR
                Material.DestroyImmediate(m_Mat);
#else
                Material.Destroy(m_Mat);
#endif
        }
    }
    [System.Serializable]
    public struct UTexture {
        [SerializeField]
        Texture m_Normal;
        [SerializeField]
        Texture m_MainTex;
        [SerializeField]
        Vector2 m_Tiling;
        [SerializeField]
        Vector2 m_Offset;
        public Vector2 tiling {
            get {
                return m_Tiling;
            }
            set {
                m_Tiling = value;
            }
        }
        public Vector2 offset {
            get {
                return m_Offset;
            }
            set {
                m_Offset = value;
            }
        }
        public Texture normal {
            get {
                return m_Normal;
            }
            set {
                m_Normal = value;
            }
        }
        public Texture mainTex {
            get {
                return m_MainTex;
            }
            set {
                m_MainTex = value;
            }
        }
        public UTexture(Texture mainTex) {
            m_Tiling = Vector2.one;
            m_Offset = Vector2.zero;
            m_MainTex = mainTex;
            m_Normal = null;
        }
        public UTexture(Texture mainTex, Texture normal, Vector2 tiling, Vector2 offset) {
            m_Tiling = tiling;
            m_Offset = offset;
            m_MainTex = mainTex;
            m_Normal = normal;
        }
    }
}