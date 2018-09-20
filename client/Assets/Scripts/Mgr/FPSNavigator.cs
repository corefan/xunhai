using UnityEngine;
using System.Collections;

public class FPSNavigator : MonoBehaviour
{


    public KeyCode mKeyLeft = KeyCode.A;
    public KeyCode mKeyRight = KeyCode.D;
    public KeyCode mKeyForward = KeyCode.W;
    public KeyCode mKeyBackward = KeyCode.S;

    public float mKeyStrokeMoveStep = 0.07f;    //metre  

    private CharacterController controller;
    private Vector3 mMoveDir;

    void Start()
    {

        controller = GetComponent<CharacterController>();
        //Debug.Log("---->"+controller.isTrigger);
        
    }

    // Update is called once per frame  
    void Update()
    {

        Vector3 vDir = Vector3.zero;
        if (Input.GetKey(mKeyLeft))
        {
            vDir.z -= mKeyStrokeMoveStep;
        }
        if (Input.GetKey(mKeyRight))
        {
            vDir.z += mKeyStrokeMoveStep;
        }

        if (Input.GetKey(mKeyForward))
        {
            vDir.x -= mKeyStrokeMoveStep;
        }
        if (Input.GetKey(mKeyBackward))
        {
            vDir.x += mKeyStrokeMoveStep;
        }
        mMoveDir = transform.rotation * vDir;

        controller.Move(mMoveDir);

    }
}