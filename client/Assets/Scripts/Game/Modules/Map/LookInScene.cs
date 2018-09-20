/**美术做场景时，观看场景使用***/

//adws 左右前后移动
//jk 左右旋转角度
//ec 上下移动
//ui 镜头离焦点目标远近
//nm 镜头对焦点的角度

using UnityEngine;
using System.Collections;

public class LookInScene : MonoBehaviour
{
    public float manSpeed = 3.2f;

    public Transform target;
    public float distance = 5.5f;
    public float height = 1.83f;
    public float damping = 2.0f;
    public bool smoothRotation = true;
    public float rotationDamping = 10f;
    public Vector3 targetLookAtOffset=Vector3.zero;
    public float bumperDistanceCheck = 2f;
    public float bumperCameraHeight = 10f;
    public Vector3 bumperRayOffset;

    void Awake()
    {
        if (target == null)
        {
            target = GameObject.CreatePrimitive(PrimitiveType.Capsule).transform;
            target.transform.localScale = new Vector3(0.5f, 1f, 0.5f);
            target.position = new Vector3(10f, 5f, 10f);
        }
    }

    void FixedUpdate()
    {
        Vector3 wantedPosition = target.TransformPoint(0, height, -distance);
        RaycastHit hit;
        Vector3 back = target.transform.TransformDirection(-1 * Vector3.forward);
        transform.position = Vector3.Lerp(transform.position, wantedPosition, Time.deltaTime * damping);
        Vector3 lookPosition = target.TransformPoint(targetLookAtOffset);
        if (smoothRotation){
            Quaternion wantedRotation = Quaternion.LookRotation(lookPosition - transform.position, target.up);
            transform.rotation = Quaternion.Slerp(transform.rotation, wantedRotation, Time.deltaTime * rotationDamping);
        }else
            transform.rotation = Quaternion.LookRotation(lookPosition - transform.position, target.up);
    }

    void Update()
    {
        if (Input.GetKey(KeyCode.A))
        {
            target.transform.Translate(Vector3.left * Time.deltaTime * manSpeed);
        }
        if (Input.GetKey(KeyCode.D))
        {
            target.transform.Translate(-Vector3.left * Time.deltaTime * manSpeed);
        }
        if (Input.GetKey(KeyCode.W))
        {
            target.transform.Translate(Vector3.forward * Time.deltaTime * manSpeed);
        }
        if (Input.GetKey(KeyCode.S))
        {
            target.transform.Translate(-Vector3.forward * Time.deltaTime * manSpeed);
        }
        if (Input.GetKey(KeyCode.A))
        {
            target.transform.Translate(Vector3.left * Time.deltaTime * manSpeed);
        }
        if (Input.GetKey(KeyCode.J))
        {
            Vector3 curAngle = target.transform.localEulerAngles;
            Vector3 newAngle = new Vector3(0, curAngle.y+=1, 0);
            target.transform.localEulerAngles = newAngle;
        }
        if (Input.GetKey(KeyCode.K))
        {
            Vector3 curAngle = target.transform.localEulerAngles;
            Vector3 newAngle = new Vector3(0, curAngle.y -= 1, 0);
            target.transform.localEulerAngles = newAngle;
        }

        if (Input.GetKey(KeyCode.E))
        {
            target.transform.Translate(Vector3.up * Time.deltaTime * manSpeed);
        }
        if (Input.GetKey(KeyCode.C))
        {
            target.transform.Translate(Vector3.down * Time.deltaTime * manSpeed);
        }

        if (Input.GetKey(KeyCode.U))
        {
            distance+=0.1F;
        }
        if (Input.GetKey(KeyCode.I))
        {
            distance-=0.1F;
        }

        if (Input.GetKey(KeyCode.N))
        {
            targetLookAtOffset = new Vector3(targetLookAtOffset.x, targetLookAtOffset.y += 0.1f, targetLookAtOffset.z);
        }
        if (Input.GetKey(KeyCode.M))
        {
            targetLookAtOffset = new Vector3(targetLookAtOffset.x, targetLookAtOffset.y -= 0.1f, targetLookAtOffset.z);
        }
    }
}