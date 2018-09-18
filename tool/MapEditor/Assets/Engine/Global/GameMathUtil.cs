using System;
using UnityEngine;
using System.Collections.Generic;

public class GameMathUtil
{
    ////服务端格子宽高(无单位)
    ////public const int SERVER_CELL_WIDTH = 50; //z

    ///// <summary>
    ///// 服务端需要的格子的大小是整数，客户端的
    ///// </summary>
    //public const int COEFFICIENT = 100;// 比例换算值

    ///// 坐标点转成服服务器坐标点
    //public static Vector3 ConvertClientVectorToServerVector(Vector3 input) {
    //    Vector3 result = input * COEFFICIENT;
    //    result.x = Mathf.FloorToInt (result.x);
    //    result.y = Mathf.FloorToInt (result.y);
    //    result.z = Mathf.FloorToInt (result.z);
    //    return result;
    //}

    ///// xz平面下，两点间的距离
    //public static float caculateDistance(Vector3 from, Vector3 to) {
    //    return caculateDistance(from.x, from.z, to.x, to.z);
    //}

    ///// 计算两个点的距离
    //public static float caculateDistance(float x1, float z1, float x2, float z2) {
    //    return Math.Abs((float)Math.Sqrt(Math.Pow (x1 - x2, 2) + Math.Pow (z1 - z2, 2)));// + Math.Pow (y1 - y2, 2)
    //}
    ///// 计算角色当前所在的位置的高度，包括地形的高度
    //public static float CaculatePointHeight(Vector3 point) {
    //    var sample = point + Vector3.up * 300;

    //    RaycastHit hit;
    //    Ray ray = new Ray(sample, Vector3.down);
    //    if (Physics.Raycast(ray, out hit)) {
    //        // //接着，如果你的项目中没有使用地形元素，而是用美术建模形成的地形话，那么就需要通过射线来取得“地形”上的两个点。如下图所示。
    //        //括括桥，3dmax创建的地形等等
    //        //hit.collider最好为meshcollider
    //        if(hit.collider.gameObject.tag != "Terrain") {
    //            Debug.DrawLine(sample,hit.point);
    //            return hit.point.y;
                
    //        }
    //    }
    //    if (Terrain.activeTerrain != null) { //计算出当前的地形的高度
    //        return Terrain.activeTerrain.SampleHeight (sample);
    //    }
    //    return 0.0f;
    //}
}
