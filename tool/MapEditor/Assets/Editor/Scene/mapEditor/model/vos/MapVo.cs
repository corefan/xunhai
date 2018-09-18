using System;
using System.Collections.Generic;

public class MapVo
{
	/// <summary>
	/// 地图id
	/// </summary>
	public int map_id;

	/// <summary>
	/// 地图名称
	/// </summary>
	public string map_name;

	/// <summary>
	/// 地图资源id
	/// </summary>
	public int mapresid;

}

public class SceneObjVo
{
    public int id;
    public int model;
    public int type;
    public string name;
    public int map;
    public int x;
    public int y;
    public int z;
}

public class DoorVo : SceneObjVo
{
    public string toLocation;
    public int toScene;
}

