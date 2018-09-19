using UnityEngine;
using System.Collections;
using UnityEditor;
using System;
using CTEUtil.CTE;

namespace CTEUtil.CTEEditor {
    [Serializable]
    internal class USettingsEditor {
        [SerializeField]
        UTerrain terrain;
        public USettingsEditor(UTerrainInspector editor){
            terrain = editor.terrain;
        }
        public void InspectorUI() {
            GUILayout.Space(4);
            EditorGUILayout.BeginVertical("OL Box", GUILayout.MinHeight(1));
            GUILayout.Label("Terrain Settings");
            GUILayout.Space(2);
            GUIStyle style = new GUIStyle(GUI.skin.textArea);
            style.fontSize = 9;
            style.normal.background = null;
            style.onNormal.background = null;
            GUILayout.Label("", style);
            EditorGUILayout.EndVertical();
            GUILayout.Space(4);
            EditorGUILayout.BeginVertical("OL Box", GUILayout.MinHeight(1));
            terrain.drawGrass = EditorGUILayout.Toggle("Draw Grass", terrain.drawGrass);
            if (terrain.data.grassDistance < 0) {
                terrain.data.grassDistance = 0;
            }
            terrain.data.grassDistance = EditorGUILayout.FloatField("Grass Distance", terrain.data.grassDistance);
            terrain.drawTree = EditorGUILayout.Toggle("Draw Tree", terrain.drawTree);
            if (terrain.data.treeBillBoardStart < 0) {
                terrain.data.treeBillBoardStart = 0;
            }
            terrain.data.treeBillBoardStart = EditorGUILayout.FloatField("Tree BillBoard Start", terrain.data.treeBillBoardStart);
            if (terrain.data.treeDistance < terrain.data.treeBillBoardStart) {
                terrain.data.treeDistance = terrain.data.treeBillBoardStart;
            }
            terrain.data.treeDistance = EditorGUILayout.FloatField("Tree Distance", terrain.data.treeDistance);
            EditorGUILayout.EndVertical();
        }
        
    }
}
