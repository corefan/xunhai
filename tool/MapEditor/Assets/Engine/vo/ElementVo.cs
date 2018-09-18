using UnityEngine;
using GEngine;

/// <summary>
/// 元素数据模型（如场景上的人，怪，草，树，花，水，石头，特效等）
/// </summary>
public abstract class ElementVo {
	/// 玩家(出生点)类型
	public const string ELEMENT_TYPE_PLAYERSPAWN = "PlayerSpawn";
    /// 玩家(出生点)类型
    public const string ELEMENT_TYPE_MONSTERSPAWN = "MonsterSpawn";
	/// 敌人类型
	public const string ELEMENT_TYPE_MONSTER = "Monster";
	/// npc类型
	public const string ELEMENT_TYPE_NPC = "Npc";
	/// 传送门类型
	public const string ELEMENT_TYPE_DOOR = "Door";

}

