using UnityEngine;
using System.Collections;

public class PlayerController : MonoBehaviour {
    public float speed = 6.0f;
    public float speedScale = 2.0f;
    static float changeDirection = 1.0f;
    static Transform myTransform;

    // Use this for initialization
    void Start () {
        myTransform = transform;
    }

    public static void UpdateChangeDirection() {
        if (changeDirection == -1) {
            myTransform.Rotate(new Vector3(0,180,0));
        }
        changeDirection *= -1;
    }

    Vector3 moveDirection;
    Quaternion changeRotation;
	// Update is called once per frame
	void Update () {
        
            moveDirection = Vector3.zero;
            if (Input.GetKey(KeyCode.W))
            {
                moveDirection += Vector3.forward;
            }
            if (Input.GetKey(KeyCode.S))
            {
                moveDirection += Vector3.back;
            }
            if (Input.GetKey(KeyCode.A))
            {
                moveDirection += Vector3.left;
            }
            if (Input.GetKey(KeyCode.D))
            {
                moveDirection += Vector3.right;
            }

            if (!moveDirection.Equals(Vector3.zero))
            {
                changeRotation = Quaternion.Euler(0, transform.rotation.eulerAngles.y + Quaternion.FromToRotation(transform.forward, moveDirection).eulerAngles.y, 0);
                transform.rotation = Quaternion.Lerp(transform.rotation, changeRotation, Time.deltaTime * 8);
            }
            transform.Translate(-transform.InverseTransformDirection(moveDirection) * speed * Time.deltaTime);
       
    }
}
