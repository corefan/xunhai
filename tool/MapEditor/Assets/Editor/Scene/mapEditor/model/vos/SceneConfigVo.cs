using UnityEngine;
using System.Collections;
using System.Collections.Generic;
/// (输出结果)场景数据配置
public class SceneConfigVo {
	/// 当前场景id
	public int sceneId = 0;

    /// 资源resid
    public string resId = "";

	/// 场景名称
	public string sceneName = "";

	/// 场景中的gameObject的
	public List<ClientElementInfo> monster = new List<ClientElementInfo>();
	/// npc
	public List<ClientElementInfo> npcs = new List<ClientElementInfo> ();
	/// (元素)传送门
    public List<SceneElementInfo> transfer = new List<SceneElementInfo>();
    /// 玩家出生点
    public List<ClientElementInfo> playerSpawn = new List<ClientElementInfo>();
    /// 怪物刷怪区
    public List<ClientElementInfo> monsterSpawn = new List<ClientElementInfo>();
}