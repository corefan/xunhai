using UnityEngine;
using System.Collections;
using UnityEditor;
using UnityEngine.UI;
using System.Collections.Generic;
using LitJson;
using System.IO;

public class EditorMapView {
    public static string mapName = "";// 地图名
	public EditSceneVo editSceneVo;/// 引用
    public bool ShowEditorBlock = false;

	public void OnGUI() {
		GUILayout.BeginVertical ();//地图
		GUILayout.BeginHorizontal ();//地图名称
		GUILayout.Label("地图名称:" ,GUILayout.Width(80), GUILayout.Height(20));
		mapName = GUILayout.TextField (mapName, GUILayout.Width(200), GUILayout.Height(20));
        editSceneVo.mapName = mapName;
		GUILayout.EndHorizontal ();
		GUILayout.EndVertical ();
        
        GUILayout.Space(20);//导出地图数据
        GUILayout.BeginVertical();
        GUILayout.BeginHorizontal(); //导出地图数据
        if (GUILayout.Button("导出元素布局", GUILayout.Width(300), GUILayout.Height(30)))
        {
            ExportScene.exportNewScene(MapEditorModel.Instance.getCellVos(), editSceneVo);
            ShowEditorBlock = true;
        }
        GUILayout.EndHorizontal();
        GUILayout.EndVertical();
        if (ShowEditorBlock)
        {
            GUILayout.Space(20);//编辑网格障碍
            GUILayout.BeginVertical();
            GUILayout.BeginHorizontal(); //编辑网格障碍
            if (GUILayout.Button("编辑网格障碍", GUILayout.Width(300), GUILayout.Height(30)))
            {
                ShowEditorBlock = false;
                EditorWindow.GetWindow(typeof(MapEditor)).Close();
                NavMeshExtension.CreateNavMeshManager.Init();
            }
            GUILayout.EndHorizontal();
            GUILayout.EndVertical();
        }
	}
}

