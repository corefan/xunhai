using UnityEngine;
using System.Collections;

public class SmoothFollowerObj
{

    private Vector3 targetPosition;
    private Vector3 position;
    private Vector3 velocity;
    private float smoothingTime;
    private float prediction;

    public SmoothFollowerObj(float smoothingTime)
    {
        targetPosition = Vector3.zero;
        position = Vector3.zero;
        velocity = Vector3.zero;
        this.smoothingTime = smoothingTime;
        prediction = 1;
    }

    public SmoothFollowerObj(float smoothingTime, float prediction)
    {
        targetPosition = Vector3.zero;
        position = Vector3.zero;
        velocity = Vector3.zero;
        this.smoothingTime = smoothingTime;
        this.prediction = prediction;
    }

    // Update should be called once per frame
    public Vector3 Update(Vector3 targetPositionNew, float deltaTime)
    {
        Vector3 targetVelocity = (targetPositionNew - targetPosition) / deltaTime;
        targetPosition = targetPositionNew;

        float d = Mathf.Min(1, deltaTime / smoothingTime);
        velocity = velocity * (1 - d) + (targetPosition + targetVelocity * prediction - position) * d;

        position += velocity * Time.deltaTime;
        return position;
    }

    public Vector3 Update(Vector3 targetPositionNew, float deltaTime, bool reset)
    {
        if (reset)
        {
            targetPosition = targetPositionNew;
            position = targetPositionNew;
            velocity = Vector3.zero;
            return position;
        }
        return Update(targetPositionNew, deltaTime);
    }

    public Vector3 GetPosition() { return position; }
    public Vector3 GetVelocity() { return velocity; }
}
