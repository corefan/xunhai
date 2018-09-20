using UnityEngine;
using System.Collections;

public class CameraController22 : MonoBehaviour {
    public Transform focusObj;
    public float focusDistance = 10.0f;
    public float rotateAngle = 0.0f;
    Quaternion lookAtRotation;

    void Start () {

    }
    
    void Update() {
        //transform.position = focusObj.position +
        //                    Vector3.back * focusDistance +
        //                    Vector3.up * focusDistance;

        //lookAtRotation = Quaternion.LookRotation(focusObj.position - transform.position, Vector3.up);
        //transform.rotation = lookAtRotation;
        //transform.rotation = Quaternion.Euler(45, rotateY, 0);
        transform.LookAt(focusObj);

        //transform.RotateAround(focusObj.position, Vector3.up, 45 * Time.deltaTime);
    }

  
       
}
