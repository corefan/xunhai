using UnityEngine;
using System.Collections;
using UnityEditor;

namespace CTEUtil.CTEEditor {
    internal class UHeightEditor : UEditor {

        protected override int selectedBrush {
            get {
                return EditorPrefs.GetInt("CTEHeightSelectedBrush" + instanceID, -1);
            }
            set {
                EditorPrefs.SetInt("CTEHeightSelectedBrush" + instanceID, value);
            }
        }

        int m_FallOff {
            get {
                return EditorPrefs.GetInt("CTEHeightFallOff" + instanceID, 0);
            }
            set {
                EditorPrefs.SetInt("CTEHeightFallOff" + instanceID, value);
            }
        }
        float m_Hardness {
            get {
                return EditorPrefs.GetFloat("CTEHeightHardness" + instanceID, 1);
            }
            set {
                EditorPrefs.SetFloat("CTEHeightHardness" + instanceID, value);
            }
        }
        public UHeightEditor(UTerrainInspector editor)
            : base(editor) {
            m_Title = "Paint Height";
            m_Intro = "Hold down \"Ctrl + Right Mouse Button\" to paint";
            m_ShowBrushUI = false;
        }

        protected override void Settings() {
            Rect rect = GUILayoutUtility.GetLastRect();
            rect.x = rect.width + rect.x - 50;
            rect.width = 50;
            if (GUI.Button(rect, "Reset")) {
                if (EditorUtility.DisplayDialog("Warning", "Are you sure to reset 'Mesh'?", "OK", "Cancel")) {
                    m_Editor.terrain.ResetMesh();
                }
            }
            EditorGUILayout.BeginVertical("OL Box", GUILayout.MinHeight(1));
            GUILayout.Space(4);
            m_FallOff = EditorGUILayout.Popup("Fall Off", m_FallOff, new string[] { "Gauss", "Linear", "Needle" });
            base.Settings();
            m_Hardness = EditorGUILayout.Slider("Hardness", m_Hardness, -30, 30);
            GUILayout.Space(2);
            EditorGUILayout.EndVertical();
        }
        protected override void CtrlAndClick() {
            //m_Editor.terrain.data.active = false;
            //m_Editor.terrain.data.textureData.active = false;
        }
        protected override void CtrlAndHoldDown() {
            float h = m_Hardness;
            PaintHeight(h);
            m_Editor.terrain.UpdateSubMesh();
        }

        protected override void ShiftAndHoldDown() {
        }
        public override void SceneUI() {
            base.SceneUI();
            Event e = Event.current;
            switch(e.type){
                case EventType.MouseUp:
                    break;
                case EventType.KeyUp:
                    m_Editor.terrain.UpdateSubMesh();
                    //m_Editor.terrain.data.active = true;
                    //m_Editor.terrain.data.textureData.active = true;
                    MeshCollider mc = m_Editor.terrain.GetComponent<MeshCollider>();
                    mc.enabled = false;
                    mc.enabled = true;
                    EditorUtility.SetDirty(m_Editor.terrain.data);
                    break;
            }
        }
        private void PaintHeight(float hardness) {
            GameObject terrain = m_Editor.terrain.gameObject;
            Vector2 size = UEditorTools.GetSizeOfMesh(m_Editor.terrain.mesh, terrain.transform);
            float multiple;
            if (size.x > size.y) {
                multiple = size.y;
            }
            else {
                multiple = size.x;
            }
            Event e = Event.current;
            Ray ray = HandleUtility.GUIPointToWorldRay(e.mousePosition);
            RaycastHit[] hits = Physics.RaycastAll(ray);
            float bs = m_BrushSize;
            int fallOff = m_FallOff;
            foreach (RaycastHit hit in hits) {
                if (hit.collider.gameObject == terrain) {
                    Vector3 relativePoint = terrain.transform.InverseTransformPoint(hit.point);
                    DeformMesh(m_Editor.terrain.mesh, relativePoint, (float)(hardness * 30 * Time.deltaTime * multiple), bs, fallOff);
                }
            }
        }

        float LinearFalloff(float distance, float inRadius) {
            return Mathf.Clamp01((float)(1.0 - distance / inRadius));
        }

        float GaussFalloff(float distance, float inRadius) {
            return Mathf.Clamp01(Mathf.Pow(360.0f, (float)(-Mathf.Pow(distance / inRadius, 2.5f) - 0.01f)));
        }

        float NeedleFalloff(float dist, float inRadius) {
            return (float)(-(dist * dist) / (inRadius * inRadius) + 1.0);
        }

        void DeformMesh(Mesh mesh, Vector3 position, float power, float inRadius, int fallOff) {
            Vector3[] vertices = mesh.vertices;
            Vector3[] normals = mesh.normals;
            float sqrRadius = inRadius * inRadius;
            float sqrMagnitude;
            float distance;
            float falloffMode;
            Vector3 averageNormal = Vector3.zero;
            for (int i = 0; i < vertices.Length; i++) {
                sqrMagnitude = (vertices[i] - position).sqrMagnitude;
                if (sqrMagnitude > sqrRadius)
                    continue;
                distance = Mathf.Sqrt(sqrMagnitude);
                falloffMode = LinearFalloff(distance, inRadius);
                averageNormal += falloffMode * normals[i];
            }
            averageNormal = averageNormal.normalized;

            for (int i = 0; i < vertices.Length; i++) {
                sqrMagnitude = (vertices[i] - position).sqrMagnitude;
                if (sqrMagnitude > sqrRadius)
                    continue;
                distance = Mathf.Sqrt(sqrMagnitude);
                switch (fallOff) {
                    case 0:
                        falloffMode = GaussFalloff(distance, inRadius);
                        break;
                    case 1:
                        falloffMode = NeedleFalloff(distance, inRadius);
                        break;
                    default:
                        falloffMode = LinearFalloff(distance, inRadius);
                        break;
                }
                vertices[i] += averageNormal * falloffMode * power;
            }
            mesh.vertices = vertices;
            mesh.RecalculateNormals();
            mesh.RecalculateBounds();
        }
    }
}
