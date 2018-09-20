using UnityEngine;
using UnityEditor;
using System.Collections;

[CustomEditor(typeof(FS_ShadowSimple))]
public class FS_ShadowSimpleEditor : Editor {
	
	GUIContent doVisiblitityCullingGUIContent = new GUIContent("Cull non-visible", "Cull shadows outside the fustrum of the 'main' camera. Culling incurs some performance overhead" +
															" so if most of your shadows are always visible you may be better off not using.");
	
	public override void OnInspectorGUI() {	
		EditorGUIUtility.LookLikeControls();
		FS_ShadowSimple s = (FS_ShadowSimple) target as FS_ShadowSimple;
		if (!s.gameObject) {
			return;
		}
		EditorGUILayout.Separator();
		DrawDefaultInspector();		
		EditorGUILayout.BeginVertical();
		s.maxProjectionDistance = EditorGUILayout.FloatField("Max projection distance", s.maxProjectionDistance);
		s.girth = EditorGUILayout.FloatField("Shadow size:", s.girth);	
		s.shadowHoverHeight = EditorGUILayout.FloatField("Shadow hover height:", s.shadowHoverHeight);
		s.isStatic = EditorGUILayout.Toggle("Static", s.isStatic);
		s.shadowMaterial = (Material) EditorGUILayout.ObjectField("Shadow Material", s.shadowMaterial, typeof(Material), true);
		
		s.uvs = EditorGUILayout.RectField("Shadow Material UV Rect", s.uvs);
		
		EditorGUILayout.LabelField("Incoming light direction can be specified by ");
		EditorGUILayout.LabelField("   -- a vector (infinately distant light source)");
		EditorGUILayout.LabelField("   -- a game object (usually a light)");
		s.useLightSource = EditorGUILayout.Toggle("Use light source game object", s.useLightSource);
		if (s.useLightSource){
			s.lightSource = (GameObject) EditorGUILayout.ObjectField("Light Source", s.lightSource, typeof(GameObject), true);
			EditorGUILayout.LabelField("With prospective projection shadows will get bigger as object approaches light source");
			s.isPerspectiveProjection = EditorGUILayout.Toggle("Use Perspective Projection", s.isPerspectiveProjection);
		} else {
			s.lightDirection = EditorGUILayout.Vector3Field("Light Direction Vector", s.lightDirection);
		}
		s.doVisibilityCulling = EditorGUILayout.Toggle(doVisiblitityCullingGUIContent, s.doVisibilityCulling);
		EditorGUILayout.EndVertical();	

	}
}
