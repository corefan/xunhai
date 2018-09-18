using GEngine;

public class MapEditorSceneModel  : Singleton<MapEditorSceneModel>
{
    public NpcList npcList;
    public MonsterList monsterList;
    public DoorList doorList;
    public ServerMapList mapList;
    public PlayerSpawnList playerSpawn;
    public MonsterSpawnList monsterSpawn;

	/// <summary>
	/// 某个场景的信息
	/// </summary>
	private MapInfos _mapInfos;

	/// <summary>
	/// 当前选中的场景数据
	/// </summary>
	private MapVo _MapVo;

	public MapInfos mapInfos {
		set {
			if (value == null) {
				return;
			}
			_mapInfos = value;
		}
		get {
			return _mapInfos;
		}
	}

	public MapVo mapVo {
		set {
			if (value == null) {
				return;
			}
			_MapVo = value;
		}
		get {
			return _MapVo;
		}
	}
}

