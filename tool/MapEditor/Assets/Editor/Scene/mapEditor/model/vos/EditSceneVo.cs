using System;
using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class EditSceneVo
{
	/// 场景名字
	public string mapName = "**";

    /// 资源resid
    public string resId = "";

	/// 导入场景id
	public int sceneId;

	/// 导入场景格子状态
	public SceneConfigVo sceneConfigVo;

	/// 是不是导入地图
	public bool isImportMap;

	/// 地图宽和高
	public Vector3 mapSize;
}

