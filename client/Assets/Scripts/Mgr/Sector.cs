using UnityEngine;
using System.Collections;

public class Sector : MonoBehaviour {

    public Transform enemy;//获取目标
    public int Distance = 2;//扇形半径
    Vector3 h;//自身坐标

    // Update is called once per frame
    void Update()
    {
        h = transform.position;//实时更新自身坐标
        Quaternion qr = transform.rotation;//自身旋转
        Quaternion right = transform.rotation * Quaternion.AngleAxis(30, Vector3.up);//以自身Y轴旋转15度
        Quaternion left = transform.rotation * Quaternion.AngleAxis(30, Vector3.down);//以自身Y轴旋转-15度
        Vector3 f = h + (qr * Vector3.forward) * Distance;//得到h正前方距离为Distance的坐标
        Debug.DrawLine(h, f, Color.red);//绘制一条以h到f的红色的线
        Vector3 l = h + (left * Vector3.forward) * Distance;//得到以h的Y轴旋转-15度距离为Distance的坐标
        Debug.DrawLine(h, l, Color.red);//绘制一条以h到l的红色的线
        Vector3 r = h + (right * Vector3.forward) * Distance;//得到以h的Y轴旋转15度距离为Distance的坐标
        Debug.DrawLine(h, r, Color.red);//绘制一条以h到r的红色的线
        if (enemy)
        {
            Vector3 e = enemy.position;//得到enemy的坐标
            Vector3 forward = transform.TransformDirection(Vector3.forward);//从自身坐标到世界坐标变换方向
            Vector3 toOther = enemy.position - transform.position;//得到以enemy到自身的向量
            if (Vector3.Dot(forward, toOther) > 0)//判断enemy是否在自身的前方
            {
                Vector3 e0l = e - l;//e到l的向量
                Vector3 e0r = e - r;//e到r的向量
                Vector3 h0l = h - l;//h到l的向量
                Vector3 h0r = h - r;//h到r的向量
                if (Vector3.Angle(e0l, h0l) < 90 && Vector3.Angle(e0r, h0r) < 90)//判断是否在扇形内
                {
                    Debug.Log("在正前方30度扇形内");
                    enemy.GetComponent<Renderer>().material.color = Color.green;//在扇形内enemy变绿色
                }
                else
                {
                    Debug.Log("不在正前方30度扇形内");
                    enemy.GetComponent<Renderer>().material.color = Color.white;//不在扇形内enemy变白色
                }
            }
            else
            {
                enemy.GetComponent<Renderer>().material.color = Color.white;//在扇形后面enemy变白色
            }
        }
    }
}
