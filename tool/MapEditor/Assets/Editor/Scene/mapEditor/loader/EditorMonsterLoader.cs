using System;
using UnityEngine;
using UnityEditor;
using System.Collections.Generic;
using GEngine; 


/// <summary>
///  怪物资源加载器
/// </summary>
public class EditorMonsterLoader
{

	/// <summary>
	/// 加载monster资源
	/// </summary>
	public static void load() {
		if (MapEditorSceneModel.Instance.mapInfos.monsters == null) {
			return;
		}
		ClientElementInfo cellVo = null;
		ClientElementInfo vo = null;

        for (int index = 0; index < MapEditorSceneModel.Instance.mapInfos.monsters.Count; index++)
        {
            vo = MapEditorSceneModel.Instance.mapInfos.monsters[index];
            AutoId.id = Math.Max(vo.id, AutoId.id);
			cellVo = new ClientElementInfo ();
            cellVo.model = vo.model;
            cellVo.id = vo.id;
            //cellVo.entityType = vo.entityType;
            cellVo.sourceType = ElementVo.ELEMENT_TYPE_MONSTER;

            //cellVo.localScale = vo.localScale;
            cellVo.location = vo.location;
            cellVo.rotation = vo.rotation;

            GameObject currentGameObject = AssetDatabase.LoadAssetAtPath<GameObject>(string.Format("Assets/Res/EditObj/{0}.prefab", cellVo.getSourceName()));
			MapImpotrEditorModel.Instance.addLoadedInfo (cellVo, currentGameObject);		
		}
	}

}

