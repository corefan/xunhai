using UnityEngine;
using System.Collections;
using UnityEditor;
using System;
using CTEUtil.CTE;

namespace CTEUtil.CTEEditor {
    [Serializable]
    internal class UAboutEditor {
        public void InspectorUI() {
            EditorGUILayout.BeginVertical("Box");
            EditorGUILayout.BeginHorizontal();
            EditorGUILayout.Space();
            GUILayout.Box(new GUIContent(UIcon.logo), GUIStyle.none, GUILayout.Width(128));
            EditorGUILayout.Space();
            EditorGUILayout.EndHorizontal();
            EditorGUILayout.BeginHorizontal();
            EditorGUILayout.Space();
            EditorGUILayout.BeginVertical("OL Box", GUILayout.MinHeight(1), GUILayout.Width(180));
            GUILayout.Space(5);
            GUILayout.Label("Author: Util");
            Rect rect = GUILayoutUtility.GetLastRect();
            GUILayout.Space(2);
            GUILayout.Label("Email: 303281924@qq.com");
            rect.x += 80;
            rect.width -= 80;
            rect.height = 16;
            if (GUI.Button(rect, "Open Website")) {
                Application.OpenURL("http://blog.163.com/util_xk");
            }
            EditorGUILayout.EndVertical();
            EditorGUILayout.Space();
            EditorGUILayout.EndHorizontal();
            EditorGUILayout.EndVertical();
        }
    }
}
