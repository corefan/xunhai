using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
#if UNITY_EDITOR
using UnityEditor;
#endif
namespace CTEUtil.CTE {
    [System.Serializable]
    public sealed class UTreeData : System.IDisposable {
        [SerializeField]
        List<UTree> m_Trees = new List<UTree>();
        [SerializeField]
        UTerrain m_Terrain;
        [SerializeField]
        GameObject m_GameObject;
        public UTree[] trees {
            get {
                return m_Trees.ToArray();
            }
        }
        internal UTerrain terrain {
            get {
                return m_Terrain;
            }
            set {
                m_Terrain = value;
            }
        }
        internal GameObject gameObject {
            get {
                return m_GameObject;
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
                if (m_GameObject.activeSelf != value)
                    m_GameObject.SetActive(value);
            }
        }
        public void Initialize(UTerrainData data) {
            Initialize(data, false);
        }
        internal void Initialize(UTerrainData data, bool newObj) {
            if (m_Terrain == null || newObj) {
                m_Terrain = data.terrain;
            }
            if (m_GameObject == null || newObj) {
                m_GameObject = new GameObject("Tree");
                m_GameObject.transform.parent = data.gameObject.transform;
                m_GameObject.transform.localEulerAngles = Vector3.zero;
                m_GameObject.transform.localPosition = Vector3.zero;
                m_GameObject.transform.localScale = Vector3.one;
                m_GameObject.hideFlags = HideFlags.DontSave;
            }
            active = m_Active;
            m_Trees.ForEach(g => g.Initialize(this, newObj));
        }

        

        public void Add(GameObject tree, Texture2D texture) {
            Add(new UTree(tree, this, texture));
        }
        public void Add(UTree grass) {
            m_Trees.Add(grass);
        }
        public void RemoveAt(int index) {
            m_Trees[index].Dispose();
            m_Trees.RemoveAt(index);
        }
        public void Draw(Camera camera) {
            m_Trees.ToList().ForEach(g => g.Draw(camera));
        }
        public void Dispose() {
            m_Trees.ForEach(g => g.Dispose());
        }
    }
    [System.Serializable]
    public sealed class UTree : System.IDisposable {
        const int m_MaxVector = 1000;
        const int m_MaxRadius = 50;
        const string m_ShaderPath = "Hidden/CTE/Billboard";
        [SerializeField]
        GameObject m_GameObject;
        [SerializeField]
        GameObject m_Prefab;
        [SerializeField]
        List<UTreeGroup> m_Groups = new List<UTreeGroup>();
        [SerializeField]
        Texture2D m_Texture;
        [SerializeField]
        byte[] m_TexBytes;
        [SerializeField]
        UTerrain m_Terrain;
        [SerializeField]
        Material m_Material;
        [SerializeField]
        Color m_Color = Color.white;
        [SerializeField]
        Color m_Emission = Color.gray;
        [SerializeField]
        float m_Cutoff = 0.5f;
        [SerializeField]
        Mesh m_TreeMesh;
        internal Transform transform {
            get {
                return m_GameObject.transform;
            }
        }
        public GameObject prefab {
            get {
                return m_Prefab;
            }
            set {
                m_Prefab = value;
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
                if (m_GameObject.activeSelf != value)
                    m_GameObject.SetActive(value);
            }
        }
        public UTerrain terrain {
            get {
                return m_Terrain;
            }
        }
        public UTreeGroup[] groups {
            get {
                return m_Groups.ToArray();
            }
        }
        public Texture2D texture {
            get {
                if (m_Texture == null){
                    m_Texture = new Texture2D(128, 128);
                    m_Texture.LoadImage(m_TexBytes);
                }
                return m_Texture;
            }
            set {
#if UNITY_EDITOR
                if (value != null){
                    m_TexBytes = new byte[0];
                    m_Texture = value;
                    material.mainTexture = m_Texture;
                }
                else {
                    Texture2D tex = null;
                    while (!CheckPreviewReady(prefab,ref tex)) {
                    }
                    tex = AssetPreview.GetAssetPreview(prefab);
                    m_TexBytes = GetBytes(tex);
                    m_Texture = new Texture2D(128, 128);
                    m_Texture.LoadImage(m_TexBytes);
                    material.mainTexture = m_Texture;
                }
#else
                m_Texture = value;
#endif
            }
        }
#if UNITY_EDITOR
        bool CheckPreviewReady(Object obj, ref Texture2D texture) {
            texture = AssetPreview.GetAssetPreview(obj);
            return !AssetPreview.IsLoadingAssetPreview(obj.GetInstanceID());
        }
        byte[] GetBytes(Texture2D texture) {
            int newX = 0, newY = 0, newW = 0, newH = 0;
            int x, y;
            if (texture != null) {
                Color background = texture.GetPixel(0, 0);
                for (x = 0; x < texture.width; x++) {
                    for (y = 0; y < texture.height; y++) {
                        Color c = texture.GetPixel(x, y);
                        if (c != background) {
                            if (newX == 0) {
                                newX = x;
                            }
                            if (x > newW) {
                                newW = x;
                            }
                        }
                    }
                }
                for (y = 0; y < texture.height; y++) {
                    for (x = 0; x < texture.width; x++) {
                        Color c = texture.GetPixel(x, y);
                        if (c != background) {
                            if (newY == 0) {
                                newY = y;
                            }
                            if (y > newH) {
                                newH = y;
                            }
                        }
                    }
                }
            }
            Texture2D tex = new Texture2D(newW - newX, newH - newY);
            Color[] colors = texture.GetPixels(newX, newY, newW - newX, newH - newY, 0);
            tex.SetPixels(colors);
            Color bg = tex.GetPixel(0, 0);
            for (x = 0; x < tex.width; x++) {
                for (y = 0; y < tex.height; y++) {
                    Color c = tex.GetPixel(x, y);
                    if (c == bg) {
                        tex.SetPixel(x, y, new Color(c.r, c.g, c.b, 0));
                    }
                }
            }
            tex.Apply();
            return tex.EncodeToPNG();
        }
#endif

        public Material material {
            get {
                if (m_Material == null) {
                    m_Material = new Material(Shader.Find(m_ShaderPath));
                    m_Material.mainTexture = texture;
                    m_Material.SetFloat("_Cutoff", m_Cutoff);
                    m_Material.color = m_Color;
                    m_Material.SetColor("_Emission", m_Emission);
                }
                return m_Material;
            }
        }

        public Color color {
            get {
                return m_Color;
            }
            set {
                m_Color = value;
                material.color = value;
            }
        }
        public Color emission {
            get {
                return m_Emission;
            }
            set {
                m_Emission = value;
                material.SetColor("_Emission", value);
            }
        }
        public float cutoff {
            get {
                return m_Cutoff;
            }
            set {
                m_Cutoff = value;
                material.SetFloat("_Cutoff", value);
            }
        }
        public void Initialize(UTreeData data){
            Initialize(data, false);
        }
        internal void Initialize(UTreeData data, bool newObj) {
            if (m_Terrain == null || newObj) {
                m_Terrain = data.terrain;
            }
            if (m_GameObject == null || newObj) {
                m_GameObject = new GameObject(prefab.name);
                m_GameObject.transform.parent = data.gameObject.transform;
                m_GameObject.transform.localPosition = Vector3.zero;
                m_GameObject.transform.localEulerAngles = Vector3.zero;
                m_GameObject.transform.localScale = Vector3.one;
                m_GameObject.hideFlags = HideFlags.DontSave;
            }
            m_Material = null;
            m_Groups.ForEach(g => g.Initialize(m_GameObject.transform, m_Terrain, material, newObj));
#if UNITY_EDITOR
            m_TreeMesh = prefab.GetComponent<MeshFilter>().sharedMesh;
#else
            m_TreeMesh = prefab.GetComponent<MeshFilter>().mesh;
#endif
            m_ApplyGroup = new List<int>();
            active = m_Active;
        }

        public UTree(GameObject treePrefab, UTreeData data, Texture2D billboardTex) {
            m_Prefab = treePrefab;
            texture = billboardTex;
            Initialize(data);
        }

        public void Draw(Camera camera) {
            Vector3 euler = camera.transform.eulerAngles;
            float start = m_Terrain.data.treeBillBoardStart;
            material.SetVector("_BillboardRotAndStart", new Vector4(euler.x, euler.y, euler.z, start));
            Vector3 position = camera.transform.position;
            Vector3 pos = position - m_GameObject.transform.position;
            float dis = m_Terrain.data.treeDistance;
            material.SetVector("_BillboardPosAndDis", new Vector4(pos.x, pos.y, pos.z, dis));


            m_Groups = m_Groups.Where(g => {
                if (g.length > 0){
                    return true;
                }
                g.Dispose();
                return false;
            }).ToList();
            m_Groups.ForEach(g => {
                if (g.active) {
                    Vector3 center = g.center + g.transform.position;
                    Vector3 proPos = new Vector3(position.x, center.y, position.z);
                    bool show = Vector3.Distance(center, proPos) - m_MaxRadius < dis;
                    g.Draw(material, show);
                    if (show){
                        DrawTrees(g, camera);
                    }
                }
            });
        }

        void DrawTrees(UTreeGroup group, Camera camera) {
            float start = m_Terrain.data.treeBillBoardStart;
            Vector3 pos = camera.transform.position - group.transform.position;
            Vector3 center = group.center;
            Vector3 proPos = new Vector3(pos.x, center.y, pos.z);
            if (Vector3.Distance(center, proPos) - m_MaxRadius < start){
                group.instances.ToList().ForEach(ins => {
                    ins.active = Vector3.Distance(ins.position, pos) < start;
                });
            }
        }

        List<int> m_ApplyGroup = new List<int>();
        public void Apply() {
            m_ApplyGroup.ForEach(i => {
                try{
                    m_Groups[i].UpdateMesh();
                }
                catch {
                    
                }
            });
            m_ApplyGroup.Clear();
        }

        public void AddArea(Vector3 pos, Vector3 scale, float density) {
            UTreeInstance instance = new UTreeInstance(pos, scale, m_TreeMesh.bounds.size);
            int groupIndex = GetGroupIndex(instance);
            UTreeGroup group = m_Groups[groupIndex];
            if (CheckGrassDistance(group,instance.position,density)){
                group.Add(instance);
                if (!m_ApplyGroup.Contains(groupIndex)){
                    m_ApplyGroup.Add(groupIndex);
                }
            }
        }

        int GetGroupIndex(UTreeInstance instance) {
            int index = -1;
            for (int i = 0; i < m_Groups.Count; i++) {
                UTreeGroup g = m_Groups[i];
                if (Vector3.Distance(instance.position, g.center) < m_MaxRadius && g.length < m_MaxVector) {
                    index = i;
                    break;
                }
            }
            if (index == -1){
                m_Groups.Add(new UTreeGroup(this, instance));
                index = m_Groups.Count - 1;
                if (!m_ApplyGroup.Contains(index)) {
                    m_ApplyGroup.Add(index);
                }
            }
            return index;
        }

        bool CheckGrassDistance(UTreeGroup group, Vector3 position, float density) {
            foreach (UTreeInstance ins in group.instances) {
                if ((ins.position - position).magnitude < density) {
                    return false;
                }
            }
            return true;
        }
        public void Add(Vector3 pos, Vector3 scale) {
            Add(new UTreeInstance(pos, scale, m_TreeMesh.bounds.size));
        }

        public void Add(UTreeInstance instance) {
            UTreeGroup group = null;
            for (int i = 0; i < m_Groups.Count; i++) {
                UTreeGroup g = m_Groups[i];
                if (Vector3.Distance(instance.position, g.center) < m_MaxRadius && g.length < m_MaxVector) {
                    group = g;
                    if (!m_ApplyGroup.Contains(i))
                        m_ApplyGroup.Add(i);
                }
            }
            if (group == null) {
                if (!m_ApplyGroup.Contains(m_Groups.Count))
                    m_ApplyGroup.Add(m_Groups.Count);
                m_Groups.Add(new UTreeGroup(this, instance));
            }
            else {
                group.Add(instance);
            }
        }

        public void Remove(Vector3 pos, float radius) {
            List<UTreeGroup> toRemove = new List<UTreeGroup>();
            for (int i = 0, max = m_Groups.Count; i < max; i++){
                UTreeGroup g = m_Groups[i];
                if (Vector3.Distance(pos, g.center + m_Terrain.transform.position) - radius > m_MaxRadius)
                    continue;
                g.Remove(pos, radius);
                if (g.length == 0) {
                    toRemove.Add(g);
                    m_ApplyGroup.Remove(i);
                }
                else {
                    if (!m_ApplyGroup.Contains(i))
                        m_ApplyGroup.Add(i);
                }
            }
            toRemove.ForEach(g => {
                m_Groups.Remove(g);
                g.Dispose();
            });
        }

        public void Dispose() {
            if (m_Material != null) {
#if UNITY_EDITOR
                Material.DestroyImmediate(m_Material);
#else
                Material.Destroy(m_Material);
#endif
            }
            if (m_GameObject != null) {
#if UNITY_EDITOR
                GameObject.DestroyImmediate(m_GameObject);
#else
                GameObject.Destroy(m_GameObject);
#endif
            }
            m_Groups.ToList().ForEach(g => g.Dispose());
        }
    }

    [System.Serializable]
    public sealed class UTreeGroup : UBillBoard<UTreeInstance> {
        [SerializeField]
        GameObject m_Prefab;

        public GameObject prefab {
            get {
                return m_Prefab;
            }
        }

        public UTreeGroup(UTree tree, UTreeInstance centerInstance)
            : base(tree.transform, tree.terrain, tree.material, centerInstance) {
                m_Prefab = tree.prefab;
        }

        internal override void Initialize(Transform parent, UTerrain terrain, Material mat, bool newObj) {
            base.Initialize(parent, terrain, mat, newObj);
            if (newObj)
                m_Instances.ForEach(ins => ins.NewInit(transform, prefab));
        }
        internal override void Draw(Material mat, bool show) {
            base.Draw(mat, show);
            m_Instances.ForEach(ins => ins.Draw(transform, prefab));
        }
    }
    [System.Serializable]
    public sealed class UTreeInstance : UBillBoardInstance {
        [SerializeField]
        GameObject m_GameObject;

        public bool active {
            get {
                return m_GameObject.activeSelf;
            }
            set {
                if (m_GameObject.activeSelf != value)
                    m_GameObject.SetActive(value);
            }
        }
        
        public UTreeInstance(Vector3 pos, Vector3 scale, Vector3 size) : base (pos,scale,size) {
        }
        internal void NewInit(Transform parent, GameObject treePrefab) {
            m_GameObject = GameObject.Instantiate(treePrefab) as GameObject;
            m_GameObject.transform.parent = parent;
            m_GameObject.transform.localPosition = position;
            m_GameObject.transform.localScale = scale;
            m_GameObject.transform.localRotation = Quaternion.identity;
            m_GameObject.hideFlags = HideFlags.DontSave;
            active = false;
#if UNITY_EDITOR
            EditorUtility.SetSelectedWireframeHidden(m_GameObject.GetComponent<Renderer>(), true);
#endif
        }
        internal void Draw(Transform parent, GameObject treePrefab) {
            if (m_GameObject == null){
                m_GameObject = GameObject.Instantiate(treePrefab) as GameObject;
                m_GameObject.transform.parent = parent;
                m_GameObject.transform.localPosition = position;
                m_GameObject.transform.localScale = scale;
                m_GameObject.transform.localRotation = Quaternion.identity;
                m_GameObject.hideFlags = HideFlags.DontSave;
                active = false;
            }
#if UNITY_EDITOR
            EditorUtility.SetSelectedWireframeHidden(m_GameObject.GetComponent<Renderer>(), true);
#endif
        }

        public override void Dispose() {
            base.Dispose();
            if (m_GameObject != null) {
#if UNITY_EDITOR
                GameObject.DestroyImmediate(m_GameObject);
#else
                GameObject.Destroy(m_GameObject);
#endif
            }
        }
    }
}