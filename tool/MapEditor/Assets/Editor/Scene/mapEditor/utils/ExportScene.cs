using System;
using UnityEngine;
using System.Collections.Generic;
using LitJson;
using UnityEditor;
using System.IO;


public class ExportScene {
	public const string CLIENT_CONFIG_PATH = "/Res/Cfg/{0}.json";

    public static void exportNewScene(List<GameObjectCellVo> listCellVos, EditSceneVo editSceneVo)
    {
        exportConfig(listCellVos, editSceneVo, CLIENT_CONFIG_PATH);
    }

    /// <summary>
    /// 导出服务端需要的场景数据
    /// </summary>
    /// <param name="listCellVos">List cell vos.</param>
    /// <param name="editSceneVo">Edit scene vo.</param>
    private static void exportConfig(List<GameObjectCellVo> listCellVos,EditSceneVo editSceneVo, string outPath)
    {
        SceneConfigVo sceneConfig = new SceneConfigVo();
        sceneConfig.sceneId = editSceneVo.sceneId;
        sceneConfig.sceneName = editSceneVo.mapName;
        sceneConfig.resId = editSceneVo.resId;
        GameObjectCellVo curGOVo;
        for (int index = 0; index < listCellVos.Count; index++)
        {
            curGOVo = listCellVos[index];
            if (curGOVo == null || string.IsNullOrEmpty(curGOVo.cellVo.sourceType) == true)
            {
                continue;
            }
            ClientElementInfo cellVo = curGOVo.cellVo;
            cellVo.location = new PositionVo();

            if (curGOVo.currentGameObject != null)
            {
                Vector3 pos = curGOVo.currentGameObject.transform.position;
                cellVo.location.x = Mathf.Round(pos.x * 10000) / 10000;
                cellVo.location.y = Mathf.Round(pos.y * 10000) / 10000;
                cellVo.location.z = Mathf.Round(pos.z * 10000) / 10000;
                cellVo.rotation = FloatPositionVo.toPosition(curGOVo.currentGameObject.transform.rotation.eulerAngles);
            }
            //npc
            if (cellVo.sourceType.Contains(ElementVo.ELEMENT_TYPE_NPC) == true)
            {
                sceneConfig.npcs.Add(cellVo);
                continue;
            }
            //door
            if (cellVo.sourceType.Contains(ElementVo.ELEMENT_TYPE_DOOR) == true)
            {
                sceneConfig.transfer.Add((SceneElementInfo)cellVo);
                continue;
            }
            //刷怪区
            if (cellVo.sourceType.Contains(ElementVo.ELEMENT_TYPE_MONSTERSPAWN) == true)
            {
                sceneConfig.monsterSpawn.Add(cellVo);
                continue;
            }
            //monster
            if (cellVo.sourceType.Contains(ElementVo.ELEMENT_TYPE_MONSTER) == true)
            {
                sceneConfig.monster.Add(cellVo);
                continue;
            }
            //玩家出生点
            if (cellVo.sourceType.Contains(ElementVo.ELEMENT_TYPE_PLAYERSPAWN) == true)
            {
                sceneConfig.playerSpawn.Add(cellVo);
                continue;
            }
           

        }
        string filepath = Application.dataPath + string.Format(outPath, editSceneVo.sceneId);
        FileInfo t = new FileInfo(filepath);
        if (!File.Exists(filepath))
        {
            File.Delete(filepath);
        }
        StreamWriter sw = t.CreateText();
        sw.WriteLine(JsonMapper.ToJson(sceneConfig));
        sw.Close();
        sw.Dispose();
        Debug.Log(sceneConfig.sceneName + " 导出完成!!!!!!!!!!");
    }
}
