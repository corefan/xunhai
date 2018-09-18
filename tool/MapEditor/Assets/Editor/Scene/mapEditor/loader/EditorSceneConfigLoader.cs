using System;
using UnityEngine;
using System.IO;
using GEngine;
using System.Collections;
using System.Collections.Generic;
using LitJson;



/// <summary>
/// 场景配置文件加载器
/// </summary>
public class EditorSceneConfigLoader
{
	public static SceneConfigVo importScene(int sceneId) {
        string filepath = Application.dataPath + string.Format(ExportScene.CLIENT_CONFIG_PATH, sceneId);
		string results = FileTool.LoadFileContent(filepath);
		if (results == null)
			return null;
        SceneConfigVo cfgVo = (SceneConfigVo)JsonParser.parser(results, typeof(SceneConfigVo));
        return cfgVo;
    }
}
