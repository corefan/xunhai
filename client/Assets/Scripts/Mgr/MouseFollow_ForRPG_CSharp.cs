using UnityEngine;
using System.Collections;
public class MouseFollow_ForRPG_CSharp : MonoBehaviour {
    public GameObject target;
    public float ZoomSpeed = 30;//镜头缩放速率
    public float MovingSpeed = 1;//镜头移动速率
    public float RotateSpeed = 1; //镜头旋转速率
    public float distance = 20;//设置距离角色的距离
    public float ViewAngle = 30;//设置镜头斜视的角度
    void Start () {
        if(target){
            transform.rotation = Quaternion.Euler(ViewAngle, target.transform.rotation.eulerAngles.y,0 );
            transform.position = transform.rotation * new Vector3(0,0,-distance)+target.transform.position;
        }
    }
    void Update () {
        Quaternion rotation;
        Vector3 position;
        float delta_x,delta_y;
        float delta_rotation_x,delta_rotation_y;
        if(target){
            if (Input.GetMouseButton(0))
            {
                delta_x = Input.GetAxis("Mouse X") * MovingSpeed;
                delta_y = Input.GetAxis("Mouse Y") * MovingSpeed;
                rotation = Quaternion.Euler(0, transform.rotation.eulerAngles.y, 0);
                transform.position = rotation * new Vector3(-delta_x, 0, -delta_y) + transform.position;
            }
            else
            {
                if (Input.GetAxis("Mouse ScrollWheel") != 0)
                {
                    distance += -Input.GetAxis("Mouse ScrollWheel") * ZoomSpeed;
                }
                if (Input.GetMouseButton(1))
                {
                    delta_rotation_x = Input.GetAxis("Mouse X") * RotateSpeed;
                    delta_rotation_y = -Input.GetAxis("Mouse Y") * RotateSpeed;
                    transform.Rotate(0, delta_rotation_x, 0, Space.World);
                    transform.Rotate(delta_rotation_y, 0, 0);
                }
                else {
                    transform.rotation = Quaternion.Slerp(transform.rotation,
                    Quaternion.Euler(transform.rotation.eulerAngles.x, target.transform.rotation.eulerAngles.y,0
                    ),Time.deltaTime*2);
                }
                transform.position = transform.rotation* new Vector3(0,0,-distance)+ target.transform.position;
            }
        }
    }
}