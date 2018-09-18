using UnityEngine;
using UnityEditor;
using GEngine;
using UnityEditor.SceneManagement;
using System.Collections.Generic;

/// <summary>
/// 地图编辑器功能筛选，是导入地图还是，新键地图
/// </summary>
[ExecuteInEditMode]
public class MapEditorFunctionSelector : EditorWindow {

    /**地图配置数据*/
    public static SceneConfigVo sceneConfigVo;
    /**服务端编辑数据*/
    MapEditorSceneModel serverModel;

    string sceneAssetsRoot = @"Assets/scene/{0}.unity";

    string configUrl = Application.dataPath + "/Res/config/";

    //将所有游戏场景导出为XML格式
    [MenuItem ("CustomEditor/地图编辑器")]
	static void ExportXML () {
		Rect  wr = new Rect (0,0,310,260);//创建窗口
		MapEditorFunctionSelector window = (MapEditorFunctionSelector)EditorWindow.GetWindowWithRect (typeof (MapEditorFunctionSelector),wr,true,"地图编辑器");	
		window.Show();
	}

	void Awake() {
		MapImpotrEditorModel.Instance.clearImportModel ();
        serverModel = MapEditorSceneModel.Instance;
        serverModel.mapVo = null;
		serverModel.mapList = null;
		serverModel.mapInfos = null;

        //Debug.Log(configUrl);
        string json = FileTool.LoadFileContent(configUrl, "map.json");
        serverModel.mapList = (ServerMapList)JsonParser.parser(json, typeof(ServerMapList));
        json = FileTool.LoadFileContent(configUrl, "monster.json");
        serverModel.monsterList = (MonsterList)JsonParser.parser(json, typeof(MonsterList));
        json = FileTool.LoadFileContent(configUrl, "npc.json");
        serverModel.npcList = (NpcList)JsonParser.parser(json, typeof(NpcList));
        json = FileTool.LoadFileContent(configUrl, "door.json");
        serverModel.doorList = (DoorList)JsonParser.parser(json, typeof(DoorList));
        json = FileTool.LoadFileContent(configUrl, "playerSpawn.json");
        serverModel.playerSpawn = (PlayerSpawnList)JsonParser.parser(json, typeof(PlayerSpawnList));
        json = FileTool.LoadFileContent(configUrl, "monsterSpawn.json");
        serverModel.monsterSpawn = (MonsterSpawnList)JsonParser.parser(json, typeof(MonsterSpawnList));
	}
    
	//绘制窗口时调用
	void OnGUI () {
        if (serverModel == null) return;//可能还没有

		//正在加载地图列表提示
		if (serverModel.mapList == null) {
			GUILayout.Space (20);
			GUILayout.Label ("加载地图信息");
			return;
		}

		if (GUILayout.Button ("选择地图", GUILayout.Width (300), GUILayout.Height (20))) {
			GenericMenu toolsMenu = new GenericMenu();
			createItems (toolsMenu);

			toolsMenu.DropDown(new Rect(Screen.width - 216 - 40, 0, 0, 16));
			EditorGUIUtility.ExitGUI();
		}
	}
    
	/// 创建下拉列表
	private void createItems(GenericMenu toolsMenu) {
		if (serverModel.mapList == null || serverModel.mapList.data == null || serverModel.mapList.data.Count <= 0) {
			toolsMenu.AddDisabledItem(new GUIContent("场景数据为空"));
			return;
		}
		MapVo serverMapVo = null;
		for (int index = 0; index < serverModel.mapList.data.Count; index++) {
			serverMapVo = serverModel.mapList.data[index];
			if (serverMapVo == null) continue;
            toolsMenu.AddItem(new GUIContent(serverMapVo.map_id.ToString() + "|" + serverMapVo.map_name), false, OnTools_OptimizeSelected, index);
		}
	}
    /// 菜单选择中执行
	void OnTools_OptimizeSelected(object userData) {
		if (serverModel.mapList == null) return;
		MapVo mapVo = serverModel.mapList.data[(int)userData];
		if (mapVo == null) return;
		serverModel.mapVo = mapVo;

        int curMapId = mapVo.map_id;
        InitMapInfos(curMapId);
	}

    private List<SceneObjVo> GetSceneObjOnListByMapId(List<SceneObjVo> sources, int mapId)
    {
        List<SceneObjVo> list = new List<SceneObjVo>();
        for (int i = 0; i < sources.Count; i++){
            SceneObjVo vo = sources[i];
            if (vo.map == mapId)
                list.Add(vo);
        }
        return list;
    }
    private List<DoorVo> GetSceneElementOnListByMapId(List<DoorVo> sources, int mapId)
    {
        List<DoorVo> list = new List<DoorVo>();
        for (int i = 0; i < sources.Count; i++)
        {
            DoorVo vo = sources[i];
            if (vo.map == mapId)
                list.Add(vo);
        }
        return list;
    }


    private void InitMapInfos(int mapId)
    {
        MapInfos info = new MapInfos();

        info.allmonster = serverModel.monsterList.data;
        info.npcs = GetSceneObjOnListByMapId(serverModel.npcList.data, mapId);
        info.transfers = GetSceneElementOnListByMapId(serverModel.doorList.data, mapId);
        info.playerSpawn = GetSceneObjOnListByMapId(serverModel.playerSpawn.data, mapId);
        info.monsterSpawn = GetSceneObjOnListByMapId(serverModel.monsterSpawn.data, mapId);
        
        serverModel.mapInfos = info;

        string sid = serverModel.mapVo.mapresid.ToString();
        EditorSceneManager.SaveCurrentModifiedScenesIfUserWantsTo(); //询问有修改的场景时的 保存当前修改场景
        EditorSceneManager.OpenScene(string.Format(sceneAssetsRoot, sid));// 打开场景
        EditorMapView.mapName = serverModel.mapVo.map_name;
        
        sceneConfigVo = EditorSceneConfigLoader.importScene(mapId);// 导入场景数据
        if (sceneConfigVo != null) {
            sceneConfigVo.resId = sid;
            for (int i = 0; i < sceneConfigVo.monster.Count; i++)
            {
                ClientElementInfo monInfo = sceneConfigVo.monster[i];
                for (int j = 0; j < serverModel.monsterList.data.Count; j++)
                {
                    SceneObjVo monVo = serverModel.monsterList.data[j];
                }
            }

            info.monsters = sceneConfigVo.monster;
        }

        //加载服务器传来的怪物数据
        EditorNpcLoader.load();//载入NPC
        EditorElementLoader.load();// 载入传送门
        EditorMonsterLoader.load();// 载入Monster
        EditorPlayerSpawnLoader.load();// 载入玩家出生点
        EditorMonsterSpawnLoader.load();// 载入刷怪区

        Rect wr = new Rect(0, 0, 310, 260);
        MapEditor window = (MapEditor)GetWindowWithRect(typeof(MapEditor), wr, true, "地图编辑器");
        window.editSceneVo.isImportMap = true;
        window.editSceneVo.sceneId = mapId;
        window.Show();

        this.Close();
    }
}
