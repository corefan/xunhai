using UnityEngine;
using System.Collections;
using UnityEditor;
using CTEUtil.CTE;
using System.Linq;
using System.Collections.Generic;
namespace CTEUtil.CTEEditor {
    [System.Serializable]
    internal class UGrassEditor : UEditor {

        protected override int selectedBrush {
            get {
                return EditorPrefs.GetInt("CTEGrassSelectedBrush" + instanceID, -1);
            }
            set {
                EditorPrefs.SetInt("CTEGrassSelectedBrush" + instanceID, value);
            }
        }

        int m_Selected {
            get {
                return EditorPrefs.GetInt("CTEGrassSelected" + instanceID, -1);
            }
            set {
                EditorPrefs.SetInt("CTEGrassSelected" + instanceID, value);
            }
        }
        int m_Model {
            get {
                return EditorPrefs.GetInt("CTEGrassModel" + instanceID, 0);
            }
            set {
                EditorPrefs.SetInt("CTEGrassModel" + instanceID, value);
            }
        }
        float m_Density {
            get {
                return EditorPrefs.GetFloat("CTEGrassDensity" + instanceID, 0.5f);
            }
            set {
                EditorPrefs.SetFloat("CTEGrassDensity" + instanceID, value);
            }
        }
        float m_Height {
            get {
                return EditorPrefs.GetFloat("CTEGrassHeight" + instanceID, 1);
            }
            set {
                EditorPrefs.SetFloat("CTEGrassHeight" + instanceID, value);
            }
        }
        int m_HeightVar {
            get {
                return EditorPrefs.GetInt("CTEGrassHeightVar" + instanceID, 10);
            }
            set {
                EditorPrefs.SetInt("CTEGrassHeightVar" + instanceID, value);
            }
        }
        float m_Width {
            get {
                return EditorPrefs.GetFloat("CTEGrassWidth" + instanceID, 1);
            }
            set {
                EditorPrefs.SetFloat("CTEGrassWidth" + instanceID, value);
            }
        }
        int m_WidthVar {
            get {
                return EditorPrefs.GetInt("CTEGrassWidthVar" + instanceID, 10);
            }
            set {
                EditorPrefs.SetInt("CTEGrassWidthVar" + instanceID, value);
            }
        }

        UGrass grass {
            get {
                if (m_Selected == -1)
                    return null;
                return m_Editor.terrain.data.grassData.grasses[m_Selected];
            }
        }

        public UGrassEditor(UTerrainInspector editor)
            : base(editor) {
                m_Title = "Paint Grasses";
            m_Intro = "Click \"Ctrl + Right Mouse Button\" to paint or hold down \"Shift + Right Mouse Button\" to erase";
        }
        protected override void OtherUI() {
            base.OtherUI();
            GUILayout.Label("Grasses", EditorStyles.boldLabel);
            GrassField();
            GUILayout.Space(-18);
        }
        protected override void Settings() {
            EditorGUILayout.BeginVertical("OL Box");
            if (m_Selected != -1) {
                GUILayout.Space(4);
                grass.active = EditorGUILayout.Toggle("Draw", grass.active);
                grass.color = EditorGUILayout.ColorField("Color", grass.color);
                grass.emission = EditorGUILayout.ColorField("Emission", grass.emission);
                grass.cutoff = EditorGUILayout.Slider("Cutoff", grass.cutoff, 0.01f, 1);
                Rect rect = GUILayoutUtility.GetLastRect();
                GUILayout.Space(4);
                Handles.color = new Color(0, 0, 0, 0.4f);
                Handles.DrawLine(new Vector2(rect.x - 3, rect.y + 20), new Vector2(rect.x + rect.width + 3, rect.y + 20));
            }
            else {
                GUILayout.Space(4);
            }
            m_Height = EditorGUILayout.Slider("Height", m_Height, 0.11f, 20);
            m_HeightVar = EditorGUILayout.IntSlider("Variation", m_HeightVar, 0, 30);
            GUILayout.Space(4);
            m_Width = EditorGUILayout.Slider("Width", m_Width, 0.1f, 20);
            m_WidthVar = EditorGUILayout.IntSlider("Variation", m_WidthVar, 0, 30);
            GUILayout.Space(2);
            EditorGUILayout.EndVertical();
            GUILayout.Space(8);
            EditorGUILayout.BeginHorizontal();
            Rect toolBarRect = GUILayoutUtility.GetRect(GUIContent.none, "dragtab", GUILayout.Width(120));
            EditorGUILayout.Space();
            EditorGUILayout.EndHorizontal();
            GUILayout.Space(-1);
            EditorGUILayout.BeginVertical("OL Box");
            base.Settings();
            if (m_Model == 1)
                m_Density = EditorGUILayout.Slider("Density:", m_Density, 0.1f, 5);
            EditorGUILayout.EndVertical();
            m_Model = GUI.Toolbar(toolBarRect, m_Model, new string[] { "Single", "Group" }, "dragtab");
        }

        public override void SceneUI() {
            base.SceneUI();
        }
        protected override void CtrlAndClick() {
            if (m_Selected == -1 || m_Model == 1 || !grass.active)
                return;
            Ray ray = HandleUtility.GUIPointToWorldRay(Event.current.mousePosition);
            RaycastHit hit;
            if (Physics.Raycast(ray, out hit)) {
                if (hit.collider.gameObject == m_Editor.terrain.gameObject) {
                    float newW = UEditorTools.RandomOfVaration(m_Width, m_WidthVar);
                    float newH = UEditorTools.RandomOfVaration(m_Height, m_HeightVar);
                    grass.Add(hit.point - m_Editor.terrain.transform.position, newW, newH);
                }
            }
            grass.Apply();
        }

        protected override void CtrlAndHoldDown() {
            if (m_Selected == -1 || m_Model == 0 || !grass.active)
                return;
            Ray ray = HandleUtility.GUIPointToWorldRay(Event.current.mousePosition);
            RaycastHit hit;
            if (Physics.Raycast(ray, out hit)) {
                if (hit.collider.gameObject == m_Editor.terrain.gameObject) {
                    AddArea(hit.point, hit.normal);
                }
            }
        }
        protected override void ShiftAndHoldDown() {
            if (m_Selected == -1 || !grass.active)
                return;
            Ray ray = HandleUtility.GUIPointToWorldRay(Event.current.mousePosition);
            RaycastHit hit;
            if (Physics.Raycast(ray, out hit)) {
                if (hit.collider.gameObject == m_Editor.terrain.gameObject) {
                    grass.Remove(hit.point, m_BrushSize);
                }
            }
        }
        float CheckBrush(Vector2 insideUnitCircle) {
            int ix = (int)((insideUnitCircle.x + 1) * 32);
            int iy = (int)((insideUnitCircle.y + 1) * 32);
            float blendFactor = m_Brush.GetStrengthInt(ix, iy);
            return blendFactor;
        }
        void AddArea(Vector3 position, Vector3 normal) {
            int num = 0;
            Vector3 extent = Vector3.one;
            extent.y = 0f;
            float num2 = m_BrushSize / ((extent.magnitude * m_Density) * 0.5f);
            int num3 = (int)((num2 * num2) * 0.5f);
            num3 = Mathf.Clamp(num3, 0, 100);

            List<UBillBoardInstance> list = new List<UBillBoardInstance>();

            for (int i = 1; (i < num3) && (num < num3); i++) {
                Vector2 insideUnitCircle = UnityEngine.Random.insideUnitCircle;
                float blendFactor = CheckBrush(insideUnitCircle);
                float newDensity;
                if (blendFactor == 0)
                    continue;
                else {
                    newDensity = m_Density / blendFactor;
                }
                insideUnitCircle.x *= m_BrushSize;
                insideUnitCircle.y *= m_BrushSize;

                Vector3 off = new Vector3(insideUnitCircle.x, 0f, insideUnitCircle.y);
                Vector3 pos = Vector3.Cross(normal, off);
                Vector3 position2 = position + pos;
                Vector3 nom = position2 - (position2 + normal * 10);
                Vector3 formPos = position2 + normal * 50;

                Ray ray = new Ray(formPos, nom);
                RaycastHit[] hits = Physics.RaycastAll(ray);
                list.Clear();
                foreach (RaycastHit hit in hits) {
                    if (hit.collider.gameObject == m_Editor.terrain.gameObject){
                        float newW = UEditorTools.RandomOfVaration(m_Width, m_WidthVar);
                        float newH = UEditorTools.RandomOfVaration(m_Height, m_HeightVar);
                        grass.AddArea(new UGrassInstance(hit.point - m_Editor.terrain.transform.position, new Vector3(newW,newH,1), Vector3.one), newDensity);
                    }
                }
            }
            grass.Apply();
        }

        void GrassField() {
            m_Selected = UEditorTools.SelectionGrid(m_Selected, m_Editor.terrain.data.grassData.grasses.Select(grass => grass.texture).ToArray(), 0x40, "No Grass Added.");
            GUILayout.Space(2);
            EditorGUILayout.BeginHorizontal();
            EditorGUILayout.Space();
            if (GUILayout.Button("Edit Grasses...", EditorStyles.popup, GUILayout.Width(93))) {
                GenericMenu menu = new GenericMenu();
                menu.AddItem(new GUIContent("Add Grass"), false, () => {
                    UGrassWizard.GetWizard("Add Grass", "Add").InitializeDefaults(m_Editor);
                    m_Editor.Repaint();
                });
                if (m_Selected == -1) {
                    menu.AddDisabledItem(new GUIContent("Edit Grass"));
                }
                else {
                    menu.AddItem(new GUIContent("Edit Grass"), false, () => {
                        UGrassWizard wizard = UGrassWizard.GetWizard("Edit Grass", "Apply");
                        wizard.InitializeDefaults(m_Editor);
                        wizard.texture = grass.texture;
                        wizard.grassIndex = m_Selected;
                        wizard.OnWizardUpdate();
                        m_Editor.Repaint();
                    });
                }
                if (m_Selected == -1) {
                    menu.AddDisabledItem(new GUIContent("Remove Grass"));
                }
                else {
                    menu.AddItem(new GUIContent("Remove Grass"), false, () => {
                        m_Editor.terrain.data.grassData.RemoveAt(m_Selected);
                        m_Selected = -1;
                        m_Editor.Repaint();
                    });
                }
                menu.ShowAsContext();
                Event.current.Use();
            }
            EditorGUILayout.EndHorizontal();
        }
    }
}
