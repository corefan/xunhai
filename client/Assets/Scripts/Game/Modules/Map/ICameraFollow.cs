using UnityEngine;
using System.Collections;

public class ICameraFollow : MonoBehaviour
{
    //玩家
    public Transform target;
    // 摄像机距离玩家距离
    public float distance = 10.0f;
    // 设想距离玩家的高度
    public float height = 5.0f;
    //鼠标滚轴速度控制参数
    private float scrollSpeed = 100F;
    //鼠标滚轴最大滚动距离
    private float maxScrollDistance = 50F;
    //鼠标滚轴最小滚动距离
    private float minScrollDistance = 2F;

    void Start()
    {
    }

    void LateUpdate()
    {
        if (target == null)
            return;
        //if (Input.GetAxis("Mouse ScrollWheel") != 0)
        //{
        //    float dis = Input.GetAxis("Mouse ScrollWheel") * scrollSpeed * Time.deltaTime;
        //    distance -= dis;
        //    distance = distance > maxScrollDistance ? maxScrollDistance : distance;
        //    distance = distance < minScrollDistance ? minScrollDistance : distance;
        //}
        transform.position = target.position;
        transform.position += Vector3.forward * distance;
        transform.position = new Vector3(transform.position.x, transform.position.y + height, transform.position.z);
        transform.LookAt(target);
    }

    public void SwitchCamera(Transform transform)
    {
        this.target = transform;
    }
}