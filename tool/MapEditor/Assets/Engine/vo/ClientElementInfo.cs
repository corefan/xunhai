using System;
using System.Collections.Generic;


public class AutoId
{
    public static int id = 100;
}

public class ClientElementInfo{
	private int _id=0;/// 唯一id
	public int id {
		get {
			if (_id == 0) 
				_id = AutoId.id++;
			return _id;
		}
		set {
			_id = value;
		}
	}
    public string sourceId = ""; /// 资源编号
	public string sourceType;/// 类型
	public PositionVo location;/// 资源在场景中的位置
	public FloatPositionVo rotation;/// 旋转
	public int model;///model
    public void setSourceName(string value) {
		if (string.IsNullOrEmpty (value) == true) {
			return;
		}
		value = value.Replace ("(Clone)", "");

		string[] values = value.Split ('_');
		sourceType = values[0];
		sourceId = values[1];
	}
	public string getSourceName() {
        if (sourceId == "")
            sourceId = "100";
        return sourceType + "_" + sourceId;
    }
}
public class SceneElementInfo:ClientElementInfo
{
    public string toLocation = "0, 0 ,0";
    public int toScene;
}

public class SceneDoorInfo
{
    public List<float> toLocation;
    public int toScene;

    private int _id = 0;/// 唯一id
    public int id
    {
        get
        {
            if (_id == 0)
                _id = AutoId.id++;
            return _id;
        }
        set
        {
            _id = value;
        }
    }
    public string sourceId = ""; /// 资源编号
    public string sourceType;/// 类型
    public PositionVo location;/// 资源在场景中的位置
    public FloatPositionVo rotation;/// 旋转
    public int model;///model
}