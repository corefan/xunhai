//unity导弹算法 预计目标点
using UnityEngine;
using System.Collections;
using UnityEngine;
using System.Collections;

public class PhycisMath
{
    public static float GetSpeed(Vector3 lastPos,Vector3 newPs,float time) {
        if (time == 0) return 0;
        return Vector3.Distance(lastPos, newPs) / time;
    }
    public static Vector3 GetDir(Vector3 lastPos, Vector3 newPs)
    {
        return (newPs - lastPos).normalized;
    }
    public static float GetDelta(float a,float b,float c) {
        return b * b - 4 * a * c;
    }
    public static float GetRad(float dis, float angle)
    {
        return -(2 * dis * Mathf.Cos(angle * Mathf.Deg2Rad));
    }
    public static float GetPom(float a, float b)
    {
        return 1-Mathf.Pow(a,b);
    }
    public static float GetSqrtOfMath(float a,float b, float d) {
        float a1 = (-b + Mathf.Sqrt(d)) / (2 * a);
        float a2 = (-b - Mathf.Sqrt(d)) / (2 * a);

        return a1>a2?a1:a2;
    }
    public Vector3 GetHitPoint() {
        return Vector3.zero;
    }
}
public class SpeedTest : MonoBehaviour {
    private float lastTime;
    private Vector3 lastPos;
    private float dtime;
    public Vector3 CurrentVector;
    public float Speed;

    void OnEnable() {
        lastTime = Time.time;
        lastPos = transform.position;
    }
    void Update () {
        dtime = Time.time - lastTime;
        if (dtime > 0) {
            lastTime = Time.time;
            Speed = PhycisMath.GetSpeed(lastPos, transform.position, dtime);
            CurrentVector = PhycisMath.GetDir(lastPos, transform.position);
            if (Mathf.Abs(Speed)<0.001f)
            {
                CurrentVector = transform.TransformDirection(Vector3.forward);
            }
            lastPos = transform.position;
        }
    }
}

public class RadarOfRocket : MonoBehaviour {
    //导弹的轨道计算是基于Transform的， 纯数学的计算，这样更精确，适用性更好
    public Transform target;//目标
    private SpeedTest rocketSpeed;
    private SpeedTest targetSpeed;
    private Vector3 targetDir;
    private float angle;
    private float distence;

    private bool isAim = false;
	private Vector3 aimPos;
    public bool IsAim
    {
        get { return isAim; }
        set { isAim = value; }
    }
    public Vector3 AimPos
    {
        get { return aimPos; }
        set { aimPos = value; }
    }
    void checkTarget() {
        if (!(rocketSpeed=GetComponent<SpeedTest>()))
        {
            gameObject.AddComponent<SpeedTest>();
            rocketSpeed = GetComponent<SpeedTest>();
        }
        if (target &&! (targetSpeed = target.GetComponent<SpeedTest>()))
        {
            target.gameObject.AddComponent<SpeedTest>();
            targetSpeed = target.GetComponent<SpeedTest>();
        }
    }
    void Update() {
        if (target)
        TestAim();
    }
    public void TestAim() {
        if (Mathf.Abs(targetSpeed.Speed) < 0.01f) { //物体的速度过小，则默认物体是静止的。
            isAim = true;
            aimPos = target.position;
        }else {
            targetDir = transform.position - target.position;
            angle = Vector3.Angle(targetDir, targetSpeed.CurrentVector);
            distence = targetDir.magnitude;
            float a = PhycisMath.GetPom((rocketSpeed.Speed / targetSpeed.Speed), 2);
            float b = PhycisMath.GetRad(distence, angle);
            float c = distence * distence;
            float d = PhycisMath.GetDelta(a, b, c);
            isAim = d >= 0 && !float.IsNaN(d) && !float.IsInfinity(d);
            if (isAim){
                float r = PhycisMath.GetSqrtOfMath(a, b, d);
                if (r < 0) isAim = false;//如果得出的是负值，则代表交点有误
                aimPos = target.transform.position + targetSpeed.CurrentVector * r;
            }
        } 
    }
	void OnEnable() {
        checkTarget();
    }
}