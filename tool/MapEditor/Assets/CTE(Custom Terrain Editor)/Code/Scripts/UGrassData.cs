using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
namespace CTEUtil.CTE {
    [System.Serializable]
    public sealed class UGrassData : System.IDisposable {
        [SerializeField]
        List<UGrass> m_Grasses = new List<UGrass>();
        [SerializeField]
        UTerrain m_Terrain;
        [SerializeField]
        GameObject m_GameObject;
        public UGrass[] grasses {
            get {
                return m_Grasses.ToArray();
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
            if (m_Terrain == null || newObj){
                m_Terrain = data.terrain;
            }
            if (m_GameObject == null || newObj) {
                m_GameObject = new GameObject("Grass");
                m_GameObject.transform.parent = data.gameObject.transform;
                m_GameObject.transform.localEulerAngles = Vector3.zero;
                m_GameObject.transform.localPosition = Vector3.zero;
                m_GameObject.transform.localScale = Vector3.one;
                m_GameObject.hideFlags = HideFlags.DontSave;
            }
            m_Grasses.ForEach(g => g.Initialize(this, newObj));
            active = m_Active;
        }
        public void Add(Texture2D tex) {
            Add(new UGrass(tex, this));
        }
        public void Add(UGrass grass) {
            m_Grasses.Add(grass);
        }
        public void RemoveAt(int index) {
            m_Grasses[index].Dispose();
            m_Grasses.RemoveAt(index);
        }
        public void Draw(Camera camera) {
            m_Grasses.ToList().ForEach(g => g.Draw(camera));
        }
        public void Dispose() {
            m_Grasses.ForEach(g => g.Dispose());
        }
    }
    [System.Serializable]
    public sealed class UGrass : System.IDisposable {
        const int m_MaxVector = 1000;
        const int m_MaxRadius = 20;
        const string m_ShaderPath = "Hidden/CTE/Billboard";
        [SerializeField]
        GameObject m_GameObject;
        [SerializeField]
        List<UGrassGroup> m_Groups = new List<UGrassGroup>();
        [SerializeField]
        Texture2D m_Texture;
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
        public UGrassGroup[] groups {
            get {
                return m_Groups.ToArray();
            }
        }
        public Texture2D texture {
            get {
                return m_Texture;
            }
            set {
                m_Texture = value;
                material.mainTexture = value;
            }
        }
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
        public void Initialize(UGrassData data) {
            Initialize(data, false);
        }
        internal void Initialize(UGrassData data, bool newObj) {
            if (m_Terrain == null || newObj){
                m_Terrain = data.terrain;
            }
            if (m_GameObject == null || newObj) {
                m_GameObject = new GameObject(texture.name);
                m_GameObject.transform.parent = data.gameObject.transform;
                m_GameObject.transform.localPosition = Vector3.zero;
                m_GameObject.transform.localEulerAngles = Vector3.zero;
                m_GameObject.transform.localScale = Vector3.one;
                m_GameObject.hideFlags = HideFlags.DontSave;
            }
            m_Material = null;
            m_Groups.ForEach(g => g.Initialize(m_GameObject.transform, m_Terrain, material, newObj));
            m_ApplyGroup = new List<int>();
            active = m_Active;
        }
        public UGrass(Texture2D texture, UGrassData data) {
            m_Texture = texture;
            Initialize(data);
        }

        public void Draw(Camera camera) {
            Vector3 euler = camera.transform.eulerAngles;
            material.SetVector("_BillboardRotAndStart", new Vector4(euler.x, euler.y, euler.z, 0));
            Vector3 pos = camera.transform.position - m_GameObject.transform.position;
            float dis = m_Terrain.data.grassDistance;
            material.SetVector("_BillboardPosAndDis", new Vector4(pos.x, pos.y, pos.z, dis));

            Vector3 tgtPos = camera.transform.position;
#if UNITY_EDITOR
            m_Groups = m_Groups.Where(g => {
                if (g.length > 0) {
                    return true;
                }
                g.Dispose();
                return false;
            }).ToList();
#endif
            m_Groups.ToList().ForEach(g => {
                if (g.active) {
                    Vector3 center = g.center + g.transform.position;
                    Vector3 proPos = new Vector3(tgtPos.x, center.y, tgtPos.z);
                    bool show = Vector3.Distance(center, proPos) - m_MaxRadius < dis;
                    g.Draw(material, show);
                }
            });
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

        public void AddArea(UGrassInstance instance, float density){
            int groupIndex = GetGroupIndex(instance);
            UGrassGroup group = m_Groups[groupIndex];
            if (CheckGrassDistance(group,instance.position,density)){
                group.Add(instance);
                if (!m_ApplyGroup.Contains(groupIndex)){
                    m_ApplyGroup.Add(groupIndex);
                }
            }
            
        }

        int GetGroupIndex(UGrassInstance instance) {
            int index = -1;
            for (int i = 0; i < m_Groups.Count; i++) {
                UGrassGroup g = m_Groups[i];
                if (Vector3.Distance(instance.position, g.center) < m_MaxRadius && g.length < m_MaxVector) {
                    index = i;
                    break;
                }
            }
            if (index == -1){
                m_Groups.Add(new UGrassGroup(m_GameObject.transform, m_Terrain, material, instance));
                index = m_Groups.Count - 1;
                if (!m_ApplyGroup.Contains(index)) {
                    m_ApplyGroup.Add(index);
                }
            }
            return index;
        }

        bool CheckGrassDistance(UGrassGroup group, Vector3 position, float density) {
            foreach (UGrassInstance ins in group.instances) {
                if ((ins.position - position).magnitude < density) {
                    return false;
                }
            }
            return true;
        }
        public void Add(Vector3 pos, float width, float height) {
            Add(new UGrassInstance(pos, new Vector3(width,height,1),Vector3.one));
        }

        public void Add(UGrassInstance instance) {
            UGrassGroup group = null;
            for (int i = 0; i < m_Groups.Count; i++) {
                UGrassGroup g = m_Groups[i];
                if (Vector3.Distance(instance.position, g.center) < m_MaxRadius && g.length < m_MaxVector) {
                    group = g;
                    if (!m_ApplyGroup.Contains(i))
                        m_ApplyGroup.Add(i);
                }
            }
            if (group == null) {
                if (!m_ApplyGroup.Contains(m_Groups.Count))
                    m_ApplyGroup.Add(m_Groups.Count);
                m_Groups.Add(new UGrassGroup(m_GameObject.transform, m_Terrain, material, instance));
            }
            else {
                group.Add(instance);
            }
        }

        public void Remove(Vector3 pos, float radius) {
            List<UGrassGroup> toRemove = new List<UGrassGroup>();
            for (int i = 0, max = m_Groups.Count; i < max; i++){
                UGrassGroup g = m_Groups[i];
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
            m_ApplyGroup.Clear();
        }
    }

    [System.Serializable]
    public sealed class UGrassGroup : UBillBoard<UGrassInstance> {
        public UGrassGroup(Transform parent, UTerrain terrain, Material mat, UGrassInstance centerInstance)
            : base(parent, terrain, mat, centerInstance) {

        }
    }
    [System.Serializable]
    public sealed class UGrassInstance : UBillBoardInstance {
        public UGrassInstance(Vector3 pos, Vector3 scale, Vector3 size)
            : base(pos, scale, size) {
        }
    }
}