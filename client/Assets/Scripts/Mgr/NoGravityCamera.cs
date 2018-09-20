using UnityEngine;
public class NoGravityCamera : MonoBehaviour
{

    public Transform character;
    public Vector3 positionVector;
    public Vector3 lookVector;
    private SmoothFollowerObj posFollow;
    private SmoothFollowerObj lookFollow;
    private Vector3 lastVelocityDir;
    private Vector3 lastPos;

    public bool isInit = false;

    // Use this for initialization
    void Start()
    {
        if (character == null) return;

        positionVector = new Vector3(0, 2, 4);
        lookVector = new Vector3(0, 0, 1.5f);
        //posFollow = new SmoothFollowerObj(0.5f, 0.5f);
        //lookFollow = new SmoothFollowerObj(0.1f, 0.0f);
        posFollow = new SmoothFollowerObj(0.5f);
        lookFollow = new SmoothFollowerObj(0.1f);
        posFollow.Update(transform.position, 0, true);
        lookFollow.Update(character.position, 0, true);
        lastVelocityDir = character.forward;
        lastPos = character.position;
    }

    // Update is called once per frame
    void LateUpdate()
    {
        if (!isInit && character != null)
        {
            isInit = true;
            Start();
        }

        if (character == null) return;
        lastVelocityDir += (character.position - lastPos) * 8;
        lastPos = character.position;
        lastVelocityDir += character.forward * Time.deltaTime;
        lastVelocityDir = lastVelocityDir.normalized;
        Vector3 horizontal = transform.position - character.position;
        Vector3 horizontal2 = horizontal;
        Vector3 vertical = character.up;
        Vector3.OrthoNormalize(ref vertical, ref horizontal2);
        if (horizontal.sqrMagnitude > horizontal2.sqrMagnitude) horizontal = horizontal2;
        transform.position = posFollow.Update(
            character.position + horizontal * Mathf.Abs(positionVector.z) + vertical * positionVector.y,
            Time.deltaTime
        );

        horizontal = lastVelocityDir;
        Vector3 look = lookFollow.Update(character.position + horizontal * lookVector.z - vertical * lookVector.y, Time.deltaTime);
        transform.rotation = Quaternion.FromToRotation(transform.forward, look - transform.position) * transform.rotation;
    }
}