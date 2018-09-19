using UnityEngine;
using System.Collections;
using System.Linq;
#if UNITY_EDITOR
using UnityEditor;
#endif
using System.Collections.Generic;
namespace CTEUtil.CTE{
    [System.Serializable]
    public sealed class UTerrainData : ScriptableObject, System.IDisposable {
        [SerializeField]
        UTerrain m_Terrain;
        [SerializeField]
        GameObject m_GO;
        [SerializeField]
        float m_GrassDis = 100;
        [SerializeField]
        float m_TreeDis = 3000;
        [SerializeField]
        float m_TreeBillBoardStart = 100;
        [SerializeField]
        UTreeData m_TreeData = new UTreeData();
        [SerializeField]
        UGrassData m_GrassData = new UGrassData();
        [SerializeField]
        UTextureData m_TextureData = new UTextureData();
        internal UTerrain terrain {
            get {
                return m_Terrain;
            }
        }
        internal GameObject gameObject {
            get {
                return m_GO;
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
                m_GO.SetActive(value);
            }
        }
        public float grassDistance {
            get {
                return m_GrassDis;
            }
            set {
                m_GrassDis = value;
            }
        }
        public float treeDistance {
            get {
                return m_TreeDis;
            }
            set {
                m_TreeDis = value;
            }
        }
        public float treeBillBoardStart {
            get {
                return m_TreeBillBoardStart;
            }
            set {
                m_TreeBillBoardStart = value;
            }
        }
        public UGrassData grassData {
            get {
                return m_GrassData;
            }
        }
        public UTreeData treeData {
            get {
                return m_TreeData;
            }
        }
        public UTextureData textureData {
            get {
                return m_TextureData;
            }
        }

        public void Initialize(UTerrain terrain){
            Initialize(terrain, false);
        }

        internal void Initialize(UTerrain  terrain, bool newObj) {
            if (m_Terrain == null || newObj) {
                m_Terrain = terrain;
            }
            if (m_GO == null || newObj) {
                m_GO = new GameObject("Detail");
#if UNITY_EDITOR
                Undo.RegisterCreatedObjectUndo(m_GO, "Created CTE Terrain");
#endif
                m_GO.transform.parent = terrain.transform;
                m_GO.transform.localPosition = Vector3.zero;
                m_GO.transform.localEulerAngles = Vector3.zero;
                m_GO.transform.localScale = Vector3.one;
                m_GO.transform.parent = null;
                m_GO.hideFlags = HideFlags.HideAndDontSave;
            }
            m_GrassData.Initialize(this, newObj);
            m_TreeData.Initialize(this, newObj);
            m_TextureData.Initialize(this, newObj);
            active = m_Active;
        }
        internal void Draw() {
            if (m_GO == null)
                return;
            Camera camera = null;
            if (m_GO.transform.parent == terrain.transform) {
                m_GO.transform.parent = null;
            }
            if (m_GO.transform.position != terrain.transform.position) {
                m_GO.transform.position = terrain.transform.position;
            }
#if UNITY_EDITOR
            Refresh();
            m_GO.hideFlags = HideFlags.HideAndDontSave;
            if (!EditorApplication.isPlaying && SceneView.currentDrawingSceneView != null)
                camera = SceneView.currentDrawingSceneView.camera;
            else if (Camera.current != null)
#endif
                camera = Camera.current;
            Draw(camera);

        }

        public void Draw(Camera camera) {
			if (m_GO.transform.localScale != m_Terrain.transform.lossyScale) {
				m_GO.transform.localScale = m_Terrain.transform.lossyScale;
			}
            if (camera == null)
                return;
            if (treeData != null)
                treeData.Draw(camera);
            if (grassData != null)
                grassData.Draw(camera);
            if (textureData != null)
                textureData.Draw();
        }
#if UNITY_EDITOR
        void Refresh() {
            List<Object> list = Selection.objects.ToList();
            m_GO.GetComponentsInChildren<Transform>().ToList().ForEach(trans => {
                GameObjectUtility.SetStaticEditorFlags(trans.gameObject, StaticEditorFlags.BatchingStatic | GameObjectUtility.GetStaticEditorFlags(terrain.gameObject));
                if (trans.gameObject.layer != terrain.gameObject.layer) {
                    trans.gameObject.layer = terrain.gameObject.layer;
                }
                if (trans.gameObject != m_Terrain.gameObject && list.Contains(trans.gameObject)) {
                    list.Remove(trans.gameObject);
                    if (!list.Contains(m_Terrain.gameObject))
                        list.Add(m_Terrain.gameObject);
                }
            });
            m_Terrain.gameObject.GetComponentsInChildren<Transform>().ToList().ForEach(trans => {
                if (trans.gameObject != terrain.gameObject){
                    GameObjectUtility.SetStaticEditorFlags(trans.gameObject, GameObjectUtility.GetStaticEditorFlags(terrain.gameObject));
                }
                if (trans.gameObject.layer != terrain.gameObject.layer) {
                    trans.gameObject.layer = terrain.gameObject.layer;
                }
                if (trans.gameObject != m_Terrain.gameObject && list.Contains(trans.gameObject)) {
                    list.Remove(trans.gameObject);
                    if (!list.Contains(m_Terrain.gameObject))
                        list.Add(m_Terrain.gameObject);
                }
            });
            Selection.objects = list.ToArray();
        }
#endif
        public void Dispose() {
#if UNITY_EDITOR
            GameObject.DestroyImmediate(m_GO, true);
#else
            GameObject.Destroy(m_GO);
#endif
            m_GrassData.Dispose();
            m_TreeData.Dispose();
            m_TextureData.Dispose();
        }
    }
}
