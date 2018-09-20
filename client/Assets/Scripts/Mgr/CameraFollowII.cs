using UnityEngine;
using System.Collections;

public class CameraFollowII : MonoBehaviour {

    public Transform target;
    public float distance;
    public float targetHeight;
    public float PitchAngle;
    private float x = 0.0f;
    private float y = 0.0f;

    void Start()
    {
        var angles = transform.eulerAngles;
        x = angles.x;
        y = angles.y;
    }

    void LateUpdate()
    {
        if (!target)
            return;

        y = target.eulerAngles.y;

        // ROTATE CAMERA:
        Quaternion rotation = Quaternion.Euler(x - PitchAngle, y, 0);
        transform.rotation = rotation;

        // POSITION CAMERA:
        Vector3 position = target.position - (rotation * Vector3.forward * distance + new Vector3(0, -targetHeight, 0));
        transform.position = position;
    }
}
