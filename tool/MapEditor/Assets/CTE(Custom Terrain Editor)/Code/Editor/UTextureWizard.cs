
using UnityEngine;
using UnityEditor;
using System.Collections;
using System.IO;
using System.Linq;
using CTEUtil.CTE;

namespace CTEUtil.CTEEditor {
    internal class UTextureWizard : EditorWindow {
        public UPass pass;
        public Vector2 v2 = Vector2.zero;
        public int subMeshIndex = 0;
        public int passIndex = -1;
        public int width = 0;
        public int height = 0;
        public string createButtonName = "Add";
        public UTerrain terrain;
        public UTerrainInspector editor;
        public static UTextureWizard GetWizard(string title, string createButtonName, UTerrainInspector editor) {
            UTextureWizard window = EditorWindow.GetWindow<UTextureWizard>(true, title);
            window.createButtonName = createButtonName;
            window.terrain = editor.terrain;
            window.editor = editor;
            window.minSize = new Vector2(300, 500);
            return window;
        }

        void OnGUI() {
            v2 = EditorGUILayout.BeginScrollView(v2);
            GUILayout.Label("Texture 0", EditorStyles.boldLabel);
            pass.textures[0] = UTextureFiled(pass.textures[0]);
            GUILayout.Label("Texture 1", EditorStyles.boldLabel);
            pass.textures[1] = UTextureFiled(pass.textures[1]);
            GUILayout.Label("Texture 2", EditorStyles.boldLabel);
            pass.textures[2] = UTextureFiled(pass.textures[2]);
            GUILayout.Label("Texture 3", EditorStyles.boldLabel);
            pass.textures[3] = UTextureFiled(pass.textures[3]);
            bool flag = pass.textures.Any(t => t.mainTex != null);
            if (!flag) {
                GUILayout.Label("Please assign a texture", "Wizard Error");
            }
            EditorGUILayout.EndScrollView();
            EditorGUILayout.BeginHorizontal();
            EditorGUILayout.Space();
            GUI.enabled = flag;
            if (GUILayout.Button(createButtonName, GUILayout.Width(100), GUILayout.Height(20))) {
                DoApply();
            }
            GUI.enabled = true;
            EditorGUILayout.EndHorizontal();
            GUILayout.Space(8);
        }
        void DoApply() {
            if (passIndex == -1){
                if (!AssetDatabase.IsSubAsset(pass.mixTex)) {
                    string path = AssetDatabase.GetAssetPath(terrain.mesh).Replace("Mesh.asset", "") + pass.mixTex.name + ".png";
                    File.WriteAllBytes(Application.dataPath + path.Substring(6), pass.mixTex.EncodeToPNG());
                    AssetDatabase.Refresh();
                    TextureImporter ti = TextureImporter.GetAtPath(path) as TextureImporter;
                    ti.isReadable = true;
                    ti.anisoLevel = 9;
                    ti.textureFormat = TextureImporterFormat.RGBA32;
                    ti.wrapMode = TextureWrapMode.Clamp;
                    ti.mipmapEnabled = false;
                    ti.npotScale = TextureImporterNPOTScale.None;

                    AssetDatabase.ImportAsset(path);
                    AssetDatabase.Refresh();
                    pass.mixTex = AssetDatabase.LoadAssetAtPath(path, typeof(Texture2D)) as Texture2D;
                }
                terrain.data.textureData.subMeshes[subMeshIndex].Add(pass);
                EditorUtility.SetDirty(terrain.data);
            }
            else {
                terrain.data.textureData.subMeshes[subMeshIndex].passes[passIndex].SetTextures(pass.textures);
            }
            Close();
        }

        UTexture UTextureFiled(UTexture tex) {
            Vector2 newV2;
            EditorGUILayout.BeginVertical("HelpBox");

            EditorGUILayout.BeginHorizontal();
            EditorGUILayout.BeginVertical(GUILayout.Width(55));
            GUILayout.Label("Texture");
            tex.mainTex = EditorGUILayout.ObjectField(tex.mainTex, typeof(Texture), false, GUILayout.Height(55), GUILayout.Width(55)) as Texture;
            EditorGUILayout.EndVertical();
            EditorGUILayout.BeginVertical(GUILayout.Width(55));
            GUILayout.Label("Normal");
            tex.normal = EditorGUILayout.ObjectField(tex.normal, typeof(Texture), false, GUILayout.Height(55), GUILayout.Width(55)) as Texture;
            EditorGUILayout.EndVertical();
            EditorGUILayout.BeginHorizontal(EditorStyles.textField);
            EditorGUILayout.BeginVertical(GUILayout.Width(10));
            GUILayout.Space(23);
            GUILayout.Label("x");
            GUILayout.Space(10);
            GUILayout.Label("y");
            EditorGUILayout.EndVertical();
            EditorGUILayout.BeginVertical();
            GUILayout.Label("Tiling");
            GUILayout.Space(5);
            newV2 = tex.tiling;
            newV2.x = EditorGUILayout.FloatField(newV2.x);
            GUILayout.Space(10);
            newV2.y = EditorGUILayout.FloatField(newV2.y);
            tex.tiling = newV2;
            EditorGUILayout.EndVertical();
            EditorGUILayout.BeginVertical();
            GUILayout.Label("Offset");
            GUILayout.Space(5);
            newV2 = tex.offset;
            newV2.x = EditorGUILayout.FloatField(newV2.x);
            GUILayout.Space(10);
            newV2.y = EditorGUILayout.FloatField(newV2.y);
            tex.offset = newV2;
            EditorGUILayout.EndVertical();
            EditorGUILayout.EndHorizontal();
            EditorGUILayout.EndHorizontal();

            EditorGUILayout.BeginHorizontal();
            EditorGUILayout.EndHorizontal();

            EditorGUILayout.EndVertical();
            return tex;
        }
    }
}
