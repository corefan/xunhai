using System;
using UnityEngine;

public class FloatPositionVo
{
	public int x;

	public int y;

	public int z;


    public static FloatPositionVo toPosition( Vector3 v)
    {
        FloatPositionVo vo = new FloatPositionVo();
        vo.x = (int)v.x;
        vo.y = (int)v.y;
        vo.z = (int)v.z;
        return vo;
    }

    public Vector3 toRotation()
    {
        Vector3 result = new Vector3();
        result.x = (float)this.x;
        result.y = (float)this.y;
        result.z = (float)this.z;
        return result;
    }
}
