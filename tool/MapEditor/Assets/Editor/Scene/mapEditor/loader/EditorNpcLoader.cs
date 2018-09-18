using System;
using UnityEngine;
using UnityEditor;
using System.Collections.Generic;
using GEngine; 


/// <summary>
/// npc资源加载类
/// </summary>
public class EditorNpcLoader {

	/// <summary>
	/// 加载npc资源
	/// </summary>
	public static void load() {
		if (MapEditorSceneModel.Instance.mapInfos.npcs == null) {
			return;
		}

		ClientElementInfo cellVo = null;
        SceneObjVo vo = null;

		for (int index = 0; index < MapEditorSceneModel.Instance.mapInfos.npcs.Count; index++) {
			vo = MapEditorSceneModel.Instance.mapInfos.npcs[index];
			cellVo = new ClientElementInfo ();
            cellVo.id = vo.id;
            cellVo.model = vo.model;
            cellVo.sourceType = ElementVo.ELEMENT_TYPE_NPC;
            cellVo.location = GetVOPosition(cellVo.model);
            cellVo.rotation = GetVORotation(cellVo.model);
            GameObject currentGameObject = AssetDatabase.LoadAssetAtPath<GameObject> (string.Format("Assets/Res/EditObj/{0}.prefab" , cellVo.getSourceName()));
			MapImpotrEditorModel.Instance.addLoadedInfo (cellVo, currentGameObject);
		}
	}

    public static PositionVo GetVOPosition(int model)
    {
        PositionVo pos = new PositionVo();
        SceneConfigVo sceneCfg = MapEditorFunctionSelector.sceneConfigVo;
        if (sceneCfg == null) return pos;
        List<ClientElementInfo> list = sceneCfg.npcs;
        if (list == null) return pos;
        ClientElementInfo info = null;
        for (int i = 0; i < list.Count; i++)
        {
            info = list[i];
            if (info.model == model)
            {
                pos.x = info.location.x;
                pos.y = info.location.y;
                pos.z = info.location.z;
                return pos;
            }
        }
        return pos;
    }

    public static FloatPositionVo GetVORotation(int model)
    {
        SceneConfigVo sceneCfg = MapEditorFunctionSelector.sceneConfigVo;
        if (sceneCfg == null) return null;
        List<ClientElementInfo> list = sceneCfg.npcs;
        if (list == null) return null;
        ClientElementInfo info = null;
        for (int i = 0; i < list.Count; i++)
        {
            info = list[i];
            if(info.model == model)
            {
                return info.rotation;
            }
        }
        return null;
    }
}
