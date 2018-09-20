using UnityEngine;
using UnityEditor;
using System.Collections;

[CustomEditor(typeof(FS_ShadowManagerMesh))]
public class FS_ShadowManagerMeshEditor : Editor {

	public override void OnInspectorGUI() {	
		EditorGUIUtility.LookLikeControls();
		FS_ShadowManagerMesh smm = (FS_ShadowManagerMesh) target as FS_ShadowManagerMesh;
		if (!smm.gameObject) {
			return;
		}
		EditorGUILayout.Separator();
		EditorGUILayout.BeginVertical();
		GUILayout.Label("Number of shadows:" + smm.getNumShadows());
		GUILayout.Label("Static: " + smm.isStatic);
		GUILayout.Label("Material: " + smm.shadowMaterial.name);		
		EditorGUILayout.EndVertical();	
	}
}
