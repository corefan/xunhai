using UnityEngine;
using System.Collections;
using UnityEditor;
using CTEUtil.CTE;
using System.Linq;
using System.Collections.Generic;
using System.IO;
namespace CTEUtil.CTEEditor {
    internal class UTextureEditor : UEditor {
        const int maxSize = 512;
        protected override int selectedBrush {
            get {
                return EditorPrefs.GetInt("CTETextureSelectedBrush" + instanceID, -1);
            }
            set {
                EditorPrefs.SetInt("CTETextureSelectedBrush" + instanceID, value);
            }
        }
        int m_SelectedSM {
            get {
                return EditorPrefs.GetInt("CTETextureSelectedSM" + instanceID, 0);
            }
            set {
                EditorPrefs.SetInt("CTETextureSelectedSM" + instanceID, value);
            }
        }
        int m_SelectedPass {
            get {
                return EditorPrefs.GetInt("CTETextureSelectedPass" + instanceID, 0);
            }
            set {
                EditorPrefs.SetInt("CTETextureSelectedPass" + instanceID, value);
            }
        }

        int m_SelectedTex {
            get {
                return EditorPrefs.GetInt("CTETextureSelectedTex" + instanceID, -1);
            }
            set {
                EditorPrefs.SetInt("CTETextureSelectedTex" + instanceID, value);
            }
        }
        float m_Hardness {
            get {
                return EditorPrefs.GetFloat("CTETextureHardness" + instanceID, 1);
            }
            set {
                EditorPrefs.SetFloat("CTETextureHardness" + instanceID, value);
            }
        }
        USubMesh subMesh {
            get {
                return m_Editor.terrain.data.textureData.subMeshes[m_SelectedSM];
            }
        }
        UPass pass {
            get {
                return subMesh.passes[m_SelectedPass];
            }
        }

        //Texture texture {
        //    get {
        //        if (m_Selected == -1)
        //            return null;
        //        return m_Editor.terrain.data.textureData.sumMeshes[m_SelectedSM].textures[m_Selected];
        //    }
        //}

        public UTextureEditor(UTerrainInspector editor)
            : base(editor) {
                m_Title = "Paint Textures";
            m_Intro = "Click \"Ctrl + Right Mouse Button\" to paint";
        }
        protected override void OtherUI() {
            base.OtherUI();
            SubMeshField();
            PassesField();
            GUILayout.Space(-18);
        }
        protected override void Settings() {
            EditorGUILayout.BeginVertical("OL Box", GUILayout.MinHeight(1));
            GUILayout.Space(2);
            base.Settings();
            m_Hardness = EditorGUILayout.Slider("Hardness:", m_Hardness, 0.1f, 5.0f);
			if (subMesh.passes.Length > 0) {
				pass.specularColor = EditorGUILayout.ColorField("Specular Color:", pass.specularColor);
				pass.shininess = EditorGUILayout.Slider("Shininess:", pass.shininess, 0.03f, 1.0f);
			}
            EditorGUILayout.EndVertical();
        }

        public override void SceneUI() {
            base.SceneUI();
            if (m_Brush != null)
                m_Brush.projector.transform.localEulerAngles = new Vector3(90, 90, -90);
        }
        protected override void CtrlAndClick() {}

        protected override void CtrlAndHoldDown() {
            if (m_SelectedTex == -1 || selectedBrush == -1)
                return;
            Ray ray = HandleUtility.GUIPointToWorldRay(Event.current.mousePosition);
            RaycastHit hit;
            if (Physics.Raycast(ray, out hit)) {
                if (hit.collider.gameObject == m_Editor.terrain.gameObject) {
                    Paint(hit);
                }
            }
        }
        protected override void ShiftAndHoldDown() {}
        float CheckBrush(Vector2 insideUnitCircle) {
            int ix = (int)((insideUnitCircle.x + 1) * 32);
            int iy = (int)((insideUnitCircle.y + 1) * 32);
            float blendFactor = m_Brush.GetStrengthInt(ix, iy);
            return blendFactor;
        }

        void Paint(RaycastHit hit) {
            int texIndex = m_SelectedTex;
            float hardness = m_Hardness;
            int passIndex = m_SelectedPass;
            float brushSize = m_BrushSize;
            USubMesh sm = subMesh;
            UPass pass = sm[passIndex];
            int texW = pass.mixTex.width;
            int texH = pass.mixTex.height;

            int centerX = Mathf.FloorToInt(hit.textureCoord.x * texW);
            int centerY = Mathf.FloorToInt(hit.textureCoord.y * texH);

            Vector2 bounds = UEditorTools.GetSizeOfMesh(sm.mesh, m_Editor.terrain.transform);
            int pixelSize = 0;
            if (texW > texH) {
                pixelSize = (int)((brushSize / bounds.x) * texW);
            }
            else {
                pixelSize = (int)((brushSize / bounds.y) * texH);
            }

            int x = Mathf.Clamp(centerX - pixelSize, 0, texW - 1);
            int y = Mathf.Clamp(centerY - pixelSize, 0, texH - 1);
            int width = Mathf.Clamp(2 * pixelSize, 0, texW - x);
            int height = Mathf.Clamp(2 * pixelSize, 0, texH - y);
            List<Color[]> srcPixels = new List<Color[]>();
            sm.passes.ToList().ForEach(p => srcPixels.Add(p.mixTex.GetPixels(x, y, width, height, 0)));
            for (int i = 0, max = srcPixels.Count; i < max; i++) {
                Color[] pixels = srcPixels[i];
                for (int h = 0; h < height; h++) {
                    for (int w = 0; w < width; w++) {
                        Color targetColor;
                        if (i == passIndex) {
                            targetColor = GetColor(texIndex);
                        }
                        else {
                            targetColor = Color.clear;
                        }
                        int ix = (int)(w * 64 / width);
                        int iy = (int)(h * 64 / height);
                        int index = (h * width) + w;
                        float blendFactor = m_Brush.GetStrengthInt(ix, iy) * hardness * 0.1f;
                        pixels[index] = Color.Lerp(pixels[index], targetColor, blendFactor);
                    }
                }
                sm[i].Paint(x, y, width, height, pixels);
            }
        }

        void SaveMixTexture(Texture2D tex) {
            if (tex != null) {
                string path = AssetDatabase.GetAssetPath(tex);
                if (path.Length > 0) {
                    byte[] textureBytes = tex.EncodeToPNG();
                    File.WriteAllBytes(path, textureBytes);
                }
                AssetDatabase.Refresh();
            }
        }
        Color GetColor(int index) {
            index = Mathf.Clamp(index, 0, 3);
            if (index == 0)
                return (new Color(1, 0, 0, 0));
            if (index == 1)
                return (new Color(0, 1, 0, 0));
            if (index == 2)
                return (new Color(0, 0, 1, 0));
            if (index == 3)
                return (new Color(0, 0, 0, 1));
            return Color.clear;
        }

        void SubMeshField() {
            int selected = m_SelectedSM;
            GUILayout.Label("SubMeshes", EditorStyles.boldLabel);
            Mesh[] subMeshes = m_Editor.terrain.GetAllSubMesh();
            m_SelectedSM = UEditorTools.SelectionGrid(m_SelectedSM, subMeshes.Select(sm => {
                Texture2D tex = null;
                while (!CheckPreviewReady(sm, ref tex)) {
                }
                tex = AssetPreview.GetAssetPreview(sm);
                return tex;
            }).ToArray(), 0x40, "No SubMesh.");
            if (selected != m_SelectedSM){
                m_SelectedPass = 0;
                m_SelectedTex = -1;
            }
        }
        void PassesField() {
            GUILayout.Label("Passes", EditorStyles.boldLabel);
            List<Rect> passRect = new List<Rect>();
            GUILayout.BeginVertical("OL Box");
            GUILayout.Space(4);
            if (subMesh.passes.Length > 0){
                for (int i = 0, max = subMesh.passes.Length; i < max; i++) {
                    GUILayout.Box("", EditorStyles.textField, GUILayout.Height(50), GUILayout.MinWidth(235));
                    passRect.Add(GUILayoutUtility.GetLastRect());
                }
            }
            else {
                GUILayout.Label("No Pass Added.");
                GUILayout.Space(15);
            }
            GUILayout.Space(2);
            GUILayout.EndVertical();
            for (int i = 0; i < passRect.Count; i++){
                subMesh[i] = PassField(passRect[i], subMesh[i], i);
            }
            GUILayout.Space(2);
            EditorGUILayout.BeginHorizontal();
            EditorGUILayout.Space();
            if (GUILayout.Button("Edit Passes...", EditorStyles.popup, GUILayout.Width(90))) {
                GenericMenu menu = new GenericMenu();
                if (subMesh.passes.Length > 3) {
                    menu.AddDisabledItem(new GUIContent("Add Pass"));
                    m_Editor.Repaint();
                }
                else {
                    menu.AddItem(new GUIContent("Add Pass"), false, () => {
                        UTextureWizard wizard = UTextureWizard.GetWizard("Add Pass", "Add", m_Editor);
                        wizard.subMeshIndex = m_SelectedSM;
                        int width = 0, height = 0;
                        GetTexSize(ref width, ref height);
                        UPass pass = null;
                        if (subMesh.passes.Length == 0)
                            pass = new UPass(true, GetClearMix(subMesh.passes.Length));
                        else
                            pass = new UPass(false, GetClearMix(subMesh.passes.Length));
                        wizard.pass = pass;
                    });
                }
                
                if (subMesh.passes.Length == 0) {
                    menu.AddDisabledItem(new GUIContent("Edit Pass"));
                    m_Editor.Repaint();
                }
                else {
                    menu.AddItem(new GUIContent("Edit Pass"), false, () => {
                        UTextureWizard wizard = UTextureWizard.GetWizard("Edit Pass", "Apply", m_Editor);
                        wizard.pass = new UPass(pass);
                        wizard.subMeshIndex = m_SelectedSM;
                        wizard.passIndex = m_SelectedPass;
                        m_Editor.Repaint();
                    });
                }
                if (m_SelectedPass == 0) {
                    menu.AddDisabledItem(new GUIContent("Remove Pass"));
                }
                else {
                    menu.AddItem(new GUIContent("Remove Pass"), false, () => {
                        m_Editor.terrain.data.textureData.subMeshes[m_SelectedSM].RemoveAt(m_SelectedPass);
                        m_SelectedPass = 0;
                        m_Editor.Repaint();
                    });
                }
                menu.ShowAsContext();
                Event.current.Use();
            }
            EditorGUILayout.EndHorizontal();
        }

        Texture2D GetClearMix(int index){
            int width = 0, height = 0;
            GetTexSize(ref width, ref height);
            Texture2D tex = new Texture2D(width, height, TextureFormat.RGBA32, false);
            Color[] colors = new Color[width * height];
            for (int i = 0, max = width * height; i < max; i++){
                if (index != 0)
                    colors[i] = Color.clear;
                else
                    colors[i] = new Color(1, 0, 0, 0);
            }
            tex.SetPixels(colors);
            tex.Apply();
            tex.name = string.Format("SubMesh{0}_SplatAlpha{1}", m_SelectedSM.ToString(), index.ToString());
            return tex;
        }

        void GetTexSize(ref int width, ref int height){
            Vector2 size = UEditorTools.GetSizeOfMesh(subMesh.mesh, m_Editor.terrain.transform);
            if (size.x < size.y) {
                width = (int)(maxSize * size.x / size.y);
                height = maxSize;
            }
            else if (size.x >= size.y) {
                height = (int)(maxSize * size.y / size.x);
                width = maxSize;
            }
        }
        UPass PassField(Rect rect, UPass pass, int index) {
            Event e = Event.current;
            bool selected = index == m_SelectedPass;
            if (e.type == EventType.mouseDown && rect.Contains(e.mousePosition)) {
                m_SelectedPass = index;
                m_SelectedTex = -1;
            }
            Color bgColor = GUI.backgroundColor;
            GUI.color = new Color(0.5f,0.8f,0.5f,0.5f);
            Rect newRect = new Rect(rect);
            newRect.x += 2;
            newRect.y += 2;
            newRect.width -= 4;
            newRect.height -= 4;
            GUIStyle style = new GUIStyle("ServerUpdateChangesetOn");
            if (selected && Event.current.type == EventType.repaint)
                style.Draw(newRect, false, false, false, false);
            GUI.color = bgColor;
            GUI.BeginGroup(rect);
            newRect = new Rect(4,4,0,rect.height-9);
            newRect.width = 30;
            style = new GUIStyle("OL Box");
            style.normal.textColor = new Color(0.7f, 0.7f, 0.7f);
            style.fontStyle = FontStyle.Bold;
            style.fontSize = 14;
            style.alignment = TextAnchor.MiddleCenter;
            bgColor = GUI.backgroundColor;
            GUI.backgroundColor = new Color(1, 1, 1, 0.8f);
            if (GUI.Button(newRect, index.ToString(), style)){
                m_SelectedPass = index;
                m_SelectedTex = -1;
            }
            GUI.backgroundColor = bgColor;
            newRect.x += newRect.width;
            GUIStyle btnStyle = new GUIStyle("OL Box");
            btnStyle.alignment = TextAnchor.MiddleCenter;
            btnStyle.onNormal = (new GUIStyle("PR Ping")).active;
            btnStyle.border = new RectOffset(7, 7, 7, 7);
            btnStyle.padding = new RectOffset(5, 5, 5, 5);
            newRect.width = 0;
            for (int i = 0, max = pass.textures.Length; i < max; i++){
                if (pass.textures[i].mainTex == null)
                    continue;
                newRect.x += newRect.width + 4;
                newRect.width = rect.height;
                if (selected){
                    if (GUI.Toggle(newRect, i == m_SelectedTex, pass.textures[i].mainTex, btnStyle)) {
                        m_SelectedTex = i;
                        m_Editor.Repaint();
                    }
                }
                else {
                    GUI.Toggle(newRect, false, pass.textures[i].mainTex, btnStyle);
                }
            }
            newRect.x += newRect.width;
            newRect.width = rect.width - newRect.x;
            newRect.height = rect.height;
            if (GUI.Button(newRect, "", GUIStyle.none)) {
                m_SelectedPass = index;
                m_SelectedTex = -1;
            }
            GUI.EndGroup();
            return pass;
        }
        bool CheckPreviewReady(Object obj, ref Texture2D texture) {
            texture = AssetPreview.GetAssetPreview(obj);
            return !AssetPreview.IsLoadingAssetPreview(obj.GetInstanceID());
        }
    }
}
