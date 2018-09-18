using UnityEngine;
using System.Collections.Generic;

/// <summary>
/// 某个地图的数据
/// </summary>
public class MapInfos
{
	/// <summary>
	/// 所有npc
	/// </summary>
	public List<SceneObjVo> npcs;

	/// <summary>
	/// 所有的怪物列表(未必存在场景中的)
	/// </summary>
	public List<SceneObjVo> allmonster;
    
	/// <summary>
	/// 当前场景怪物
	/// </summary>
	public List<ClientElementInfo> monsters;

	/// <summary>
	/// (传送门) 场景元素
	/// </summary>
    public List<DoorVo> transfers;

    /// <summary>
    /// 玩家出生点
    /// </summary>
    public List<SceneObjVo> playerSpawn;

    /// <summary>
    /// 刷怪区
    /// </summary>
    public List<SceneObjVo> monsterSpawn;
}
