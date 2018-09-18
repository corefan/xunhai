using UnityEngine;
using UnityEditor;
using System.Collections;
using CTEUtil.CTE;
using System.Linq;
using System.Reflection;
using System.IO;
namespace CTEUtil.CTEEditor {
	[CustomEditor(typeof(UTerrain))]
	internal sealed class UTerrainInspector : Editor {
		int selectedTool {
			get {
				return EditorPrefs.GetInt("CTETerrainToolSelected", -1);
			}
			set {
				EditorPrefs.SetInt("CTETerrainToolSelected", value);
			}
		}
		public UTerrain terrain;
		[SerializeField]
		UTextureEditor texture;
		[SerializeField]
		UTreeEditor tree;
		[SerializeField]
		UGrassEditor grass;
		[SerializeField]
		USettingsEditor settings;
		[SerializeField]
		UAboutEditor about;
		[SerializeField]
		UHeightEditor height;
		[SerializeField]
		Transform m_Detail;
		
		void CheckField() {
			if (terrain == null)
				terrain = target as UTerrain;
			if (texture == null) {
				texture = new UTextureEditor(this);
			}
			if (tree == null) {
				tree = new UTreeEditor(this);
			}
			if (grass == null) {
				grass = new UGrassEditor(this);
			}
			if (height == null) {
				height = new UHeightEditor(this);
			}
			if (settings == null) {
				settings = new USettingsEditor(this);
			}
			if (about == null) {
				about = new UAboutEditor();
			}
		}
		void OnEnable() {
			SetScriptIcon();
		}
		
		void SetScriptIcon() {
			SerializedObject so = new SerializedObject(MonoScript.FromMonoBehaviour(target as UTerrain));
			so.Update();
			so.FindProperty("m_Icon").objectReferenceValue = UIcon.scriptIcon;
			so.ApplyModifiedProperties();
		}
		
		public override void OnInspectorGUI() {
			CheckField();
			if (terrain.transform.lossyScale != Vector3.one) {
				EditorGUILayout.LabelField("Transform.lossyScale must equal Vector3.one!");
				if (selectedTool != -1)
					selectedTool = -1;
				return;
			}
			//base.OnInspectorGUI();
			EditorGUI.BeginChangeCheck();
			selectedTool = ToolBar(selectedTool);
			GUILayout.Space(4);
			switch(selectedTool){
			case 0:
				height.InspectorUI();
				break;
			case 1:
				texture.InspectorUI();
				break;
			case 2:
				tree.InspectorUI();
				break;
			case 3:
				grass.InspectorUI();
				break;
			case 4:
				settings.InspectorUI();
				break;
			case 5:
				about.InspectorUI();
				break;
			default:
				NoSelectedTool();
				break;
			}
			GUILayout.Space(5);
			if (EditorGUI.EndChangeCheck()){
				EditorUtility.SetDirty(terrain.data);
			}
		}
		
		void OnSceneGUI() {
			CheckField();
			switch (selectedTool) {
			case 0:
				height.SceneUI();
				break;
			case 1:
				texture.SceneUI();
				break;
			case 2:
				tree.SceneUI();
				break;
			case 3:
				grass.SceneUI();
				break;
			case 4:
				break;
			case 5:
				break;
			default:
				break;
			}
		}
		
		int ToolBar(int index) {
			string[] strs = new string[] { "Height", "Texture", "Tree", "Grass", "Settings", "About" };
			Texture2D[] icons = new Texture2D[] { UIcon.height, UIcon.texture, UIcon.tree, UIcon.grass, UIcon.set, UIcon.about };
			GUIContent[] contents = new GUIContent[6];
			for (int i = 0; i < contents.Length; i++) {
				contents[i] = new GUIContent(icons[i], strs[i]);
			}
			Rect rect = EditorGUILayout.BeginHorizontal();
			EditorGUILayout.Space();
			index = GUILayout.Toolbar(index, contents, "Command", GUILayout.MaxWidth(200f));
			EditorGUILayout.Space();
			EditorGUILayout.EndHorizontal();
			rect.y += 3;
			rect.width = 30;
			if (GUI.Button(rect,new GUIContent(UIcon.save,"Save"))){
				Save();
			}
			return index;
		}
		void NoSelectedTool() {
			EditorGUILayout.BeginVertical("HelpBox");
			GUILayout.Label("No tool selected");
			GUILayout.Label("Please select a tool");
			EditorGUILayout.EndVertical();
		}
		
		void OnDestroy() {
			if (height != null)
				height.Dispose();
			if (texture != null)
				texture.Dispose();
			if (tree != null)
				tree.Dispose();
			if (grass != null)
				grass.Dispose();
		}
		
		public void Save() {
			terrain.data.textureData.subMeshes.ToList().ForEach(sm => sm.passes.ToList().ForEach(p => p.mixTex = SaveMixTexture(p.mixTex)));
			EditorUtility.SetDirty(terrain.data);
			terrain.data.Dispose();
			AssetDatabase.SaveAssets();
			terrain.data.Initialize(terrain);
		}
		Texture2D SaveMixTexture(Texture2D tex) {
			if (tex != null) {
				string path = AssetDatabase.GetAssetPath(tex);
				if (path.Length > 0) {
					byte[] textureBytes = tex.EncodeToPNG();
					File.WriteAllBytes(path, textureBytes);
				}
				AssetDatabase.ImportAsset(path);
				AssetDatabase.Refresh();
				tex = AssetDatabase.LoadAssetAtPath(path, typeof(Texture2D)) as Texture2D;
			}
			return tex;
		}
	}
}
