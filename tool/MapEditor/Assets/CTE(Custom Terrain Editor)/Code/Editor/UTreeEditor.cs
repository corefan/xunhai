using UnityEngine;
using System.Collections;
using UnityEditor;
using System.Linq;
using System.Collections.Generic;
using CTEUtil.CTE;
namespace CTEUtil.CTEEditor {
    [System.Serializable]
    internal class UTreeEditor : UEditor {

        protected override int selectedBrush {
            get {
                return EditorPrefs.GetInt("CTETreeSelectedBrush" + instanceID, -1);
            }
            set {
                EditorPrefs.SetInt("CTETreeSelectedBrush" + instanceID, value);
            }
        }

        int m_Selected {
            get {
                return EditorPrefs.GetInt("CTETreeSelected" + instanceID, -1);
            }
            set {
                EditorPrefs.SetInt("CTETreeSelected" + instanceID, value);
            }
        }
        int m_Model {
            get {
                return EditorPrefs.GetInt("CTETreeModel" + instanceID, 0);
            }
            set {
                EditorPrefs.SetInt("CTETreeModel" + instanceID, value);
            }
        }
        float m_Density {
            get {
                return EditorPrefs.GetFloat("CTETreeDensity" + instanceID, 10f);
            }
            set {
                EditorPrefs.SetFloat("CTETreeDensity" + instanceID, value);
            }
        }
        float m_Height {
            get {
                return EditorPrefs.GetFloat("CTETreeHeight" + instanceID, 1);
            }
            set {
                EditorPrefs.SetFloat("CTETreeHeight" + instanceID, value);
            }
        }
        int m_HeightVar {
            get {
                return EditorPrefs.GetInt("CTETreeHeightVar" + instanceID, 10);
            }
            set {
                EditorPrefs.SetInt("CTETreeHeightVar" + instanceID, value);
            }
        }
        float m_Width {
            get {
                return EditorPrefs.GetFloat("CTETreeWidth" + instanceID, 1);
            }
            set {
                EditorPrefs.SetFloat("CTETreeWidth" + instanceID, value);
            }
        }
        int m_WidthVar {
            get {
                return EditorPrefs.GetInt("CTETreeWidthVar" + instanceID, 10);
            }
            set {
                EditorPrefs.SetInt("CTETreeWidthVar" + instanceID, value);
            }
        }
        float m_Length {
            get {
                return EditorPrefs.GetFloat("CTETreeLength" + instanceID, 1);
            }
            set {
                EditorPrefs.SetFloat("CTETreeLength" + instanceID, value);
            }
        }
        int m_LengthVar {
            get {
                return EditorPrefs.GetInt("CTETreeLengthVar" + instanceID, 10);
            }
            set {
                EditorPrefs.SetInt("CTETreeLengthVar" + instanceID, value);
            }
        }

        UTree tree {
            get {
                if (m_Selected == -1)
                    return null;
                return m_Editor.terrain.data.treeData.trees[m_Selected];
            }
        }

        public UTreeEditor(UTerrainInspector editor)
            : base(editor) {
                m_Title = "Paint Trees";
            m_Intro = "Click \"Ctrl + Right Mouse Button\" to paint or hold down \"Shift + Right Mouse Button\" to erase";
        }
        protected override void OtherUI() {
            base.OtherUI();
            GUILayout.Label("Trees", EditorStyles.boldLabel);
            TreeField();
            GUILayout.Space(-18);
        }
        protected override void Settings() {
            EditorGUILayout.BeginVertical("OL Box");
            if (m_Selected != -1) {
                GUILayout.Space(4);
                tree.active = EditorGUILayout.Toggle("Draw", tree.active);
                tree.color = EditorGUILayout.ColorField("Color", tree.color);
                tree.emission = EditorGUILayout.ColorField("Emission", tree.emission);
                tree.cutoff = EditorGUILayout.Slider("Cutoff", tree.cutoff, 0.01f, 1);
                Rect rect = GUILayoutUtility.GetLastRect();
                GUILayout.Space(4);
                Handles.color = new Color(0, 0, 0, 0.4f);
                Handles.DrawLine(new Vector2(rect.x - 3, rect.y + 20), new Vector2(rect.x + rect.width + 3, rect.y + 20));
            }
            else {
                GUILayout.Space(4);
            }
            m_Height = EditorGUILayout.Slider("Height", m_Height * 100, 5, 500) * 0.01f;
            m_HeightVar = EditorGUILayout.IntSlider("Variation", m_HeightVar, 0, 30);
            GUILayout.Space(4);
            m_Width = EditorGUILayout.Slider("Width", m_Width * 100, 5, 500) * 0.01f;
            m_WidthVar = EditorGUILayout.IntSlider("Variation", m_WidthVar, 0, 30);
            GUILayout.Space(4);
            m_Length = EditorGUILayout.Slider("Length", m_Length * 100, 5, 500) * 0.01f;
            m_LengthVar = EditorGUILayout.IntSlider("Variation", m_LengthVar, 0, 30);
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
                m_Density = EditorGUILayout.Slider("Density:", m_Density, 2, 50);
            EditorGUILayout.EndVertical();
            m_Model = GUI.Toolbar(toolBarRect, m_Model, new string[] { "Single", "Group" }, "dragtab");
        }

        public override void SceneUI() {
            base.SceneUI();
        }

        protected override void CtrlAndClick() {
            if (m_Selected == -1 || m_Model == 1 || !tree.active)
                return;
            Ray ray = HandleUtility.GUIPointToWorldRay(Event.current.mousePosition);
            RaycastHit hit;
            if (Physics.Raycast(ray, out hit)) {
                if (hit.collider.gameObject == m_Editor.terrain.gameObject) {
                    float newW = UEditorTools.RandomOfVaration(m_Width, m_WidthVar);
                    float newH = UEditorTools.RandomOfVaration(m_Height, m_HeightVar);
                    float newL = UEditorTools.RandomOfVaration(m_Length, m_LengthVar);
                    tree.Add(hit.point - m_Editor.terrain.transform.position, new Vector3(newW, newH, newL));
                }
            }
            tree.Apply();
        }

        protected override void CtrlAndHoldDown() {
            if (m_Selected == -1 || m_Model == 0 || !tree.active)
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
            if (m_Selected == -1 || !tree.active)
                return;
            Ray ray = HandleUtility.GUIPointToWorldRay(Event.current.mousePosition);
            RaycastHit hit;
            if (Physics.Raycast(ray, out hit)) {
                if (hit.collider.gameObject == m_Editor.terrain.gameObject) {
                    tree.Remove(hit.point, m_BrushSize);
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
                    if (hit.collider.gameObject == m_Editor.terrain.gameObject) {
                        float newW = UEditorTools.RandomOfVaration(m_Width, m_WidthVar);
                        float newH = UEditorTools.RandomOfVaration(m_Height, m_HeightVar);
                        float newL = UEditorTools.RandomOfVaration(m_Length, m_LengthVar);
                        tree.AddArea(hit.point - m_Editor.terrain.transform.position, new Vector3(newW, newH, newL), newDensity);
                    }
                }
            }
            tree.Apply();
        }

        bool CheckPreviewReady(Object obj, ref Texture2D texture) {
            texture = AssetPreview.GetAssetPreview(obj);
            return !AssetPreview.IsLoadingAssetPreview(obj.GetInstanceID());
        }

        void TreeField() {
            m_Selected = UEditorTools.SelectionGridImageAndText(m_Selected, m_Editor.terrain.data.treeData.trees.Select(tree => {
                Texture2D tex = null;
                while (!CheckPreviewReady(tree.prefab, ref tex)) {
                }
                tex = AssetPreview.GetAssetPreview(tree.prefab);
                tex.name = tree.prefab.name;
                return tex;
            }).ToArray(), 0x40, "No Tree Added.");
            GUILayout.Space(2);
            EditorGUILayout.BeginHorizontal();
            EditorGUILayout.Space();
            if (GUILayout.Button("Edit Trees...", EditorStyles.popup, GUILayout.Width(80))) {
                GenericMenu menu = new GenericMenu();
                menu.AddItem(new GUIContent("Add Tree"), false, () => {
                    UTreeWizard.GetWizard("Add Tree", "Add").InitializeDefaults(m_Editor);
                });
                if (m_Selected == -1) {
                    menu.AddDisabledItem(new GUIContent("Edit Tree"));
                    m_Editor.Repaint();
                }
                else {
                    menu.AddItem(new GUIContent("Edit Tree"), false, () => {
                        UTreeWizard wizard = UTreeWizard.GetWizard("Edit Tree", "Apply");
                        wizard.InitializeDefaults(m_Editor);
                        UTree ut = m_Editor.terrain.data.treeData.trees[m_Selected];
                        wizard.tree = ut.prefab;
                        wizard.billBoardTexture = ut.texture;
                        wizard.treeIndex = m_Selected;
                        wizard.OnWizardUpdate();
                        m_Editor.Repaint();
                    });
                }
                if (m_Selected == -1) {
                    menu.AddDisabledItem(new GUIContent("Remove Tree"));
                }
                else {
                    menu.AddItem(new GUIContent("Remove Tree"), false, () => {
                        m_Editor.terrain.data.treeData.RemoveAt(m_Selected);
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
