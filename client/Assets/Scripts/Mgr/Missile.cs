using UnityEngine;
using System.Collections;
[RequireComponent(typeof(RadarOfRocket))]
public class Missile : MonoBehaviour {
    private RadarOfRocket radar;
    public float Speed = 10;
    public float RoteSpeed = 3;
    public float Noise = 0;
    void OnEnable() {
        radar = GetComponent<RadarOfRocket>();
    }
    void Update() {
        Fly();
        if (radar.IsAim) {
            FlyToTarget(radar.AimPos-transform.position);
        }
    }
    private void FlyToTarget(Vector3 point) {
        if (point != Vector3.zero) {
            Quaternion missileRotation = Quaternion.LookRotation(point, Vector3.up);
            transform.rotation = Quaternion.Slerp(transform.rotation, missileRotation, Time.deltaTime * RoteSpeed);
        }
    }
    private void Fly() {
        Move(transform.forward.normalized+transform.right*Mathf.PingPong(Time.time,0.5f)*Noise, Speed*Time.deltaTime);
    }
    public void Move(Vector3 dir, float speed){
        transform.Translate(dir*speed, Space.World);
    }
    void OnTriggerEnter(Collider other)
    {
        print("hit");
    }
}