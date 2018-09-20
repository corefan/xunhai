using UnityEngine;
using System.Collections;
public class TestOnSector : MonoBehaviour
{
    public Transform Target;
    private float SkillDistance = 5;//扇形距离
    private float SkillJiaodu = 90;//扇形的角度
    void OnGUI()
    {
        if (Target == null) return;
        float distance = Vector3.Distance(transform.position, Target.position);//距离
        Vector3 norVec = transform.rotation * Vector3.forward * 4;//此处*5只是为了画线更清楚,可以不要
        Vector3 temVec = Target.position - transform.position;
        Debug.DrawLine(transform.position, norVec, Color.red);//画出技能释放者面对的方向向量
        Debug.DrawLine(transform.position, Target.position, Color.green);//画出技能释放者与目标点的连线
        float jiajiao = Mathf.Acos(Vector3.Dot(norVec.normalized, temVec.normalized)) * Mathf.Rad2Deg;//计算两个向量间的夹角
        if (distance < SkillDistance)
        {
            if (jiajiao <= SkillJiaodu * 0.5f)
            {
                Debug.Log("在扇形范围内");
            }
        }
    }
}

//using UnityEngine;
//using System.Collections;
//public class TestOnSector : MonoBehaviour
//{
//    public Transform target;
//    public float fowardDist = 9f;//前方跨度
//    public float rlDist = 3f; // 左右各跨度(分左右等距)
//    void Update()
//    {
//        if (target == null) return;
//        Quaternion r = transform.rotation;
//        Vector3 left = (transform.position + (r * Vector3.left) * rlDist);
//        Debug.DrawLine(transform.position, left, Color.red);
//        Vector3 right = (transform.position + (r * Vector3.right) * rlDist);
//        Debug.DrawLine(transform.position, right, Color.red);
//        Vector3 leftEnd = (left + (r * Vector3.forward) * fowardDist);
//        Debug.DrawLine(left, leftEnd, Color.red);
//        Vector3 rightEnd = (right + (r * Vector3.forward) * fowardDist);
//        Debug.DrawLine(right, rightEnd, Color.red);
//        Debug.DrawLine(leftEnd, rightEnd, Color.red);
//        Vector3 point = target.position;
//        if (isINRect(point, leftEnd, rightEnd, right, left))
//            Debug.Log("target in this !!!");
//        else
//            Debug.Log("target not in this !!!");
//    }
//    private float Multiply(float p1x, float p1y, float p2x, float p2y, float p0x, float p0y)
//    {
//        return ((p1x - p0x) * (p2y - p0y) - (p2x - p0x) * (p1y - p0y));
//    }
//    bool isINRect(Vector3 point, Vector3 leftEnd, Vector3 rightEnd, Vector3 right, Vector3 left)
//    {
//        float x = point.x;
//        float y = point.z;
//        float v0x = leftEnd.x;
//        float v0y = leftEnd.z;
//        float v1x = rightEnd.x;
//        float v1y = rightEnd.z;
//        float v2x = right.x;
//        float v2y = right.z;
//        float v3x = left.x;
//        float v3y = left.z;
//        if (Multiply(x, y, v0x, v0y, v1x, v1y) * Multiply(x, y, v3x, v3y, v2x, v2y) <= 0 && Multiply(x, y, v3x, v3y, v0x, v0y) * Multiply(x, y, v2x, v2y, v1x, v1y) <= 0)
//            return true;
//        else
//            return false;
//    }
//}

//using UnityEngine;
//using System.Collections;
//public class TestOnSector : MonoBehaviour
//{
//    public Transform target;
//    public float angle = 60f;
//    public float distance = 5f;
//    void Update()
//    {
//        if (target == null) return;
//        Vector3 direction = target.position - transform.position;
//        if (Vector3.Angle(direction, transform.forward) < angle)
//            if (Vector3.Distance(target.position, transform.position) < distance)
//                print("我已经锁定你了！");
//    }
//}


//炮弹轨迹
//using UnityEngine;
//using System.Collections;
//public class TestOnSector : MonoBehaviour
//{
//    public GameObject target;
//
//	private Vector3 target;
//    public float speed = 10;
//    private float distanceToTarget;
//    private bool move = true;
//
//    void Start ()
//    {
//
//		Vector3 pos = transform.position;
//		target = transform.forward * 5 + transform.position;
//		distanceToTarget = Vector3.Distance (this.transform.position, target); //target.transform.position
//        StartCoroutine (Shoot ());
//    }
//    
//    IEnumerator Shoot ()
//	{
//        
//		while (move) {
//			Vector3 targetPos = target;//target.transform.position
//			this.transform.LookAt (targetPos);
//			float angle = Mathf.Min (1, Vector3.Distance (this.transform.position, targetPos) / distanceToTarget) * 45;
//			this.transform.rotation = this.transform.rotation * Quaternion.Euler (Mathf.Clamp (-angle, -42, 42), 0, 0);
//			float currentDist = Vector3.Distance (this.transform.position, target);//target.transform.position
//			print ("currentDist" + currentDist);
//			if (currentDist < 0.2f) {
//				move = false;
//				transform.localRotation = Quaternion.identity;
//			}
//            this.transform.Translate (Vector3.forward * Mathf.Min (speed * Time.deltaTime, currentDist));
//            yield return null;
//        }
//    }
//}

//using UnityEngine;
//using System.Collections;
//public class TestOnSector : MonoBehaviour
//{
//	public const float g = 9.8f;
//    public GameObject target;
//    public float speed = 10;
//    private float verticalSpeed;
//	private float time;
//    void Start()
//    {
//        float tmepDistance = Vector3.Distance(transform.position, target.transform.position);
//        float tempTime = tmepDistance / speed;
//        float riseTime, downTime;
//        riseTime = downTime = tempTime / 2;
//        verticalSpeed = g * riseTime;
//        transform.LookAt(target.transform.position);
//    }
//
//    void Update()
//    {
////        if (transform.position.y < target.transform.position.y)
////            return;
//		if(Vector3.Distance (transform.position, target.transform.position)<1f)
//            return;
//        time += Time.deltaTime;
//        float test = verticalSpeed - g*time;
//        transform.Translate(transform.up * test * Time.deltaTime, Space.World);
//		transform.Translate(transform.forward * speed * Time.deltaTime, Space.World);
//    }
//}

//using UnityEngine;
//using System.Collections;
//using System;
//public class TestOnSector : MonoBehaviour
//{
//	public const float g = 9.8f;
// 
//    public GameObject target;
//    public float speed = 10;
//    private float verticalSpeed;
//    private Vector3 moveDirection;
// 
////    private float angleSpeed;
////    private float angle;
//	private float time;
//
//	bool isInit;
//
//    void Init()
//    {
//       float tmepDistance = Vector3.Distance(transform.position, target.transform.position);
//        float tempTime = tmepDistance / speed;
//        float riseTime, downTime;
//        riseTime = downTime = tempTime / 2;
//         verticalSpeed = g * riseTime;
//        transform.LookAt(target.transform.position);
// 
////        float tempTan = verticalSpeed / speed;
////        double hu = Math.Atan(tempTan);
////        angle = (float)(180 / Math.PI * hu);
////        transform.eulerAngles = new Vector3(-angle, transform.eulerAngles.y, transform.eulerAngles.z);
////        angleSpeed = angle / riseTime;
// 
//        moveDirection = target.transform.position - transform.position;
//    }
//
//    void Update ()
//	{
//		if (target == null) 
//			return;
//		 else 
//			if (!isInit)
//				Init();
//		
////        if (transform.position.y < target.transform.position.y )
//		if(Vector3.Distance (this.transform.position, target.transform.position)<0.2f)
//            return;
//        time += Time.deltaTime;
//        float test = verticalSpeed - g * time;
//        transform.Translate(moveDirection.normalized * speed * Time.deltaTime, Space.World);
//        transform.Translate(Vector3.up * test * Time.deltaTime,Space.World);
////        float testAngle = -angle + angleSpeed * time;
////        transform.eulerAngles = new Vector3(testAngle, transform.eulerAngles.y, transform.eulerAngles.z);
//    }
//}


/// <summary>
/// 弓箭轨迹模拟
/// </summary>
// using UnityEngine;
// using System.Collections;
// public class TestOnSector : MonoBehaviour {
//    public float Power=10;//这个代表发射时的速度/力度等，可以通过此来模拟不同的力大小
//    public float Angle=45;//发射的角度，这个就不用解释了吧
//    public float Gravity = -10;//这个代表重力加速度
//    private Vector3 MoveSpeed;//初速度向量
//    private Vector3 GritySpeed = Vector3.zero;//重力的速度向量，t时为0
//    private float dTime;//已经过去的时间
// 	void Start () { //通过一个公式计算出初速度向量 角度*力度
// 		MoveSpeed = Quaternion.Euler(new Vector3(-Angle, 0, 0)) * Vector3.forward * Power;
// 	}
// 	void Update () {
// 		GritySpeed.y = Gravity * (dTime += Time.fixedDeltaTime);//v = at ; //计算物体的重力速度
// 		transform.Translate(MoveSpeed * Time.fixedDeltaTime); //位移模拟轨迹
// 		transform.Translate(GritySpeed * Time.fixedDeltaTime);
// 	}
// }

/// <summary>
/// 弓箭轨迹模拟
/// </summary>
// using UnityEngine;
// using System.Collections;
// public class TestOnSector : MonoBehaviour
// {
//    public float Power = 10;//这个代表发射时的速度/力度等，可以通过此来模拟不同的力大小
//    public float Angle = 45;//发射的角度，这个就不用解释了吧
//    public float Gravity = -10;//这个代表重力加速度
//    private Vector3 MoveSpeed;//初速度向量
//    private Vector3 GritySpeed = Vector3.zero;//重力的速度向量，t时为0
//    private float dTime;//已经过去的时间
//    private Vector3 currentAngle;
//    void Start()
//    {
// 		//通过一个公式计算出初速度向量//角度*力度
//        MoveSpeed = Quaternion.Euler(new Vector3(0, 0, Angle)) * Vector3.right * Power;
//        currentAngle = Vector3.zero;
//    }
//    // Update is called once per frame
//    void Update()
//    {
// 		GritySpeed.y = Gravity * (dTime += Time.deltaTime);//v = at ; //计算物体的重力速度
// 		transform.position += (MoveSpeed + GritySpeed) * Time.deltaTime;//位移模拟轨迹
//        currentAngle.z = Mathf.Atan((MoveSpeed.y + GritySpeed.y) / MoveSpeed.x) * Mathf.Rad2Deg;
//        transform.eulerAngles = currentAngle;
//    }
// }


/// <summary>
/// 弓箭轨迹模拟
/// </summary>
// using UnityEngine;
// using System.Collections;
// public class TestOnSector : MonoBehaviour
// {
// 	public float time=3;//代表从A点出发到B经过的时长
//    public Transform pointA;//点A
//    public Transform pointB;//点B
//    public float g=-10;//重力加速度
//    // Use this for initialization
//    private Vector3 speed;//初速度向量
//    private Vector3 Gravity;//重力向量
//    void Start () {
//        transform.position = pointA.position;//将物体置于A点
//        //通过一个式子计算初速度
//        speed = new Vector3((pointB.position.x - pointA.position.x)/time,
//            (pointB.position.y-pointA.position.y)/time-0.5f*g*time,
//            (pointB.position.z - pointA.position.z) / time);
//        Gravity = Vector3.zero;//重力初始速度为0
//    }
//    private float dTime=0;
//    // Update is called once per frame
//    void FixedUpdate () {
//        Gravity.y = g * (dTime += Time.fixedDeltaTime);//v=at
//        transform.Translate(speed*Time.fixedDeltaTime);//模拟位移
//        transform.Translate(Gravity * Time.fixedDeltaTime);
//    }
// }


/// <summary>
/// 导弹 #
/// </summary>
//using UnityEngine;
//using System.Collections;
//public class TestOnSector : MonoBehaviour
//{
//	public GameObject m_target;
//	public float m_move_speed = 3.5f;
//	public float m_rotation_speed = 1.0f;
//	public float m_distance = 0.5f;
//	
//	public bool is_forward_flag;
//	
//	void Start ()
//	{
//		m_target = GameObject.Find("100");
//	}
//	void Update () 
//	{
//		move ();
//	}
//	void move ()
//	{
//		Quaternion targetRotation = Quaternion.LookRotation(m_target.transform.position - transform.position);
//		transform.rotation = Quaternion.Slerp(transform.rotation,targetRotation,m_rotation_speed);
//		transform.Translate(Vector3.forward * Time.deltaTime * m_move_speed);
//		if(Vector3.Distance(transform.position,m_target.transform.position) <= m_distance)
//		{
//			Destroy(m_target);
//			Destroy(this.gameObject);
//		}
//	}
//}


/// <summary>
/// AI follow.
/// </summary>
//using UnityEngine;
//using System.Collections;
//
//public class TestOnSector : MonoBehaviour {
//    public GameObject man;
//    public GameObject missile;
//
//    public float manSpeed = 6f;
//    public float missileSpeed = 4f;
//    public float missileRotateSpeed = 2f;
//
//    bool whehterShooted = false;
//
//    float distance;
//    float collisionDistance;
//    // Use this for initialization
//    void Start () {
//        if (man!=null && missile!=null)
//        {
//            //float manWidth = man.GetComponent<MeshFilter>().mesh.bounds.size.x*man.transform.localScale.x;
//            //float missileLength = missile.GetComponent<MeshFilter>().mesh.bounds.size.z * missile.transform.localScale.z;
//			//print("manWidth:"+manWidth.ToString()+",missileLength:"+missileLength.ToString());
//            collisionDistance = 1f;//manWidth / 2 + missileLength / 2;
//    
//        }
//    }
//    
//    // Update is called once per frame
//    void Update () {
//
//        if (Input.GetKey (KeyCode.LeftArrow))
//        {
//            man.transform.Translate(Vector3.left*Time.deltaTime*manSpeed);
//        }
//        if (Input.GetKey(KeyCode.RightArrow))
//        {
//            man.transform.Translate(-Vector3.left * Time.deltaTime * manSpeed);
//        }
//        if (Input.GetKey(KeyCode.UpArrow))
//        {
//            man.transform.Translate(Vector3.forward * Time.deltaTime * manSpeed);
//        }
//        if (Input.GetKey(KeyCode.DownArrow))
//        {
//            man.transform.Translate(-Vector3.forward * Time.deltaTime * manSpeed);
//        }
//        if (Input.GetKeyDown(KeyCode.S))//按下S 发射导弹
//        {
//            whehterShooted = true;
//        }
//        if (whehterShooted && missile!=null)
//        {
//            distance = Vector3.Distance(man.transform.position, missile.transform.position);
//
//            ////导弹朝向人  法一
//            //missile.transform.LookAt(man.transform);          
//
//            //导弹朝向人  法二            
//            Quaternion missileRotation = Quaternion.LookRotation(man.transform.position - missile.transform.position, Vector3.up);
//            //missile.transform.rotation = Quaternion.Slerp(missile.transform.rotation, missileRotation, Time.deltaTime * missileRotateSpeed);
//            missile.transform.rotation = missileRotation;
//           
//
//            //导弹朝向人   法三  (不好用)          
//            //Vector3 targetDirection = man.transform.position - missile.transform.position;
//            //float angle = Vector3.Angle(targetDirection,missile.transform.forward);//取得两个向量间的夹角
//            //print("angle:"+angle.ToString());
//            //if (angle > 5)
//            //{
//            //    missile.transform.Rotate(Vector3.up, angle);
//            //}
//
//            missile.transform.Translate(Vector3.forward * Time.deltaTime * missileSpeed);
//
//            //检测是否发生碰撞。这里通过两者的distance来判断
//            if (distance<=collisionDistance)
//            {
//                Destroy(missile);
//            }
//        }
//    }
//}
///旋转
//using UnityEngine;
//using System.Collections;
//public class TestOnSector : MonoBehaviour
//{
//    public float smooth = 2.0F;
//    public float tiltAngle = 30.0F;
//    void Update()
//    {
//        if (Input.GetKey(KeyCode.Space))
//        {
//            transform.Rotate(new Vector3(0, smooth, 0));
//        }
//        else
//        {
//            float tiltAroundZ = Input.GetAxis("Horizontal") * tiltAngle;
//            float tiltAroundX = Input.GetAxis("Vertical") * tiltAngle;
//            Quaternion target = Quaternion.Euler(tiltAroundX, 0, tiltAroundZ);
//            transform.rotation = Quaternion.Slerp(transform.rotation, target, Time.deltaTime * smooth);
//        }
//
//    }
//}

//unity3d 带缓冲的镜头拉近效果
//SmoothFollowWithCameraBumper.js
//一些游戏中，当玩家发现了某样东西，摄像机会马上移动过去，但在快到的时候会减慢速度，这个代码实现的就是那个效果。
//using UnityEngine;
//using System.Collections;
//
//public class TestOnSector : MonoBehaviour {
//	public float manSpeed = 3.2f;
//
//	public Transform target;
//	public float distance = 3.5f;
//	public float height = 2.0f;
//	public float damping = 2.0f;
//	public bool smoothRotation = true;
//	public float rotationDamping = 10f;
//	public Vector3 targetLookAtOffset;
//	public float bumperDistanceCheck = 2f;
//	public float bumperCameraHeight = 10f;
//	public Vector3 bumperRayOffset;
//
//	void FixedUpdate ()
//	{
//		Vector3 wantedPosition = target.TransformPoint(0, height, -distance);
//		RaycastHit hit;
//		Vector3 back = target.transform.TransformDirection(-1 * Vector3.forward); 
//		// cast the bumper ray out from rear and check to see if there is anything behind
//		if (Physics.Raycast(target.TransformPoint(bumperRayOffset), back, out hit, bumperDistanceCheck)) {
//			// clamp wanted position to hit position
//			wantedPosition.x = hit.point.x;
//			wantedPosition.z = hit.point.z;
//			wantedPosition.y = Mathf.Lerp(hit.point.y + bumperCameraHeight, wantedPosition.y, Time.deltaTime * damping);
//		}
//		transform.position = Vector3.Lerp(transform.position, wantedPosition, Time.deltaTime * damping);
//		Vector3 lookPosition = target.TransformPoint(targetLookAtOffset);
//		if (smoothRotation) {
//			Quaternion wantedRotation = Quaternion.LookRotation(lookPosition - transform.position, target.up);
//			transform.rotation = Quaternion.Slerp(transform.rotation, wantedRotation, Time.deltaTime * rotationDamping);
//		} else {
//			transform.rotation = Quaternion.LookRotation(lookPosition - transform.position, target.up);
//		}
//	}
//
//    void Update ()
//	{
//		if (Input.GetKey (KeyCode.LeftArrow)) {
//			target.transform.Translate (Vector3.left * Time.deltaTime * manSpeed);
//		}
//		if (Input.GetKey (KeyCode.RightArrow)) {
//			target.transform.Translate (-Vector3.left * Time.deltaTime * manSpeed);
//		}
//		if (Input.GetKey (KeyCode.UpArrow)) {
//			target.transform.Translate (Vector3.forward * Time.deltaTime * manSpeed);
//		}
//		if (Input.GetKey (KeyCode.DownArrow)) {
//			target.transform.Translate (-Vector3.forward * Time.deltaTime * manSpeed);
//		}
//	}
//
//}


//using UnityEngine;
//using System.Collections;
//
//public class TestOnSector : MonoBehaviour {
//	public Transform target;
//	public float smoothTime = 0.5f;
//	private Vector3 camVelocity = Vector3.zero;
//	public Camera mainCam;
//
//	public Vector3 distance = new Vector3(0, 2.4f, -8);
//	public float dist = -8f;
//
//	public float characterSpeed = 3.2f;//角色移动速度
//	public float rotSpeed = 5.0f;//人转向速度
//
//	private Vector3 manPos;//人要走到的目的地
//	public Transform boss;//镜头兼并目标点
//
//	public float nearChange = 3f;//接近改变距离
////	public float lockChangeCD = 2f;//锁定再次改变cd
////	private float lockChangeTime = 0f;//切换手柄与镜头时间变量
//
//	void Awake () {
//		mainCam = Camera.main;
//		manPos = target.position;
//		distance = new Vector3(0, 2.4f, dist);
//	}
//
//    void Update ()
//	{
//		//lockChangeTime -= Time.deltaTime;
//		int changeDir = distance.z < 0 ? 1 : -1;
//		if (Input.GetKey (KeyCode.A)) {
//			manPos = changeDir * (Vector3.left) + target.position;
//		}
//		if (Input.GetKey (KeyCode.D)) {
//			manPos = changeDir * ((-Vector3.left)) + target.position;
//		}
//		if (Input.GetKey (KeyCode.W)) {
//			manPos = changeDir * (Vector3.forward) + target.position;
//		}
//		if (Input.GetKey (KeyCode.S)) {
//			manPos = changeDir * ((-Vector3.forward)) + target.position;
//		}
//
//		if (target.position != manPos) {
//			target.transform.position = Vector3.Slerp (target.transform.position, manPos, Time.deltaTime * characterSpeed);
//			Quaternion q = GetRot (target.position, manPos);
//			target.rotation = Quaternion.Slerp (target.rotation, q, Time.deltaTime * rotSpeed);
//		}
//	}
//
//	void LateUpdate ()
//	{
//		Vector3 targetPos = target.position + distance;
//		if (boss) {
//			targetPos = (boss.position - target.position) / 2 + target.position + distance;
//			float dist = Vector3.Distance (boss.position, target.position);
//			//float h = dist/Mathf.Tan(60*Mathf.PI/180);
//			//if(h < 2.4f)h=2.4f;
//			//if(h > 5f)h=5f;
//			//distance.y = h;
//			if (dist > nearChange) { // && lockChangeTime < 0
//				//lockChangeTime = lockChangeCD;
//				//做cd，或让主角转向锁成前方移动
//				if (boss.position.z - target.position.z < 0) {
//					distance.z = dist;
//				} else {
//					distance.z = -dist;
//				}
//			}
//
//			Vector3 lookPos = (boss.position - target.position) * 1/5 + target.position;
//			//1
//			//transform.LookAt(new Vector3(lookPos.x, 0, lookPos.z));
//			//2
//			//Quaternion q = GetRot (transform.position, new Vector3(lookPos.x, 0, lookPos.z));
//			//transform.rotation = Quaternion.Slerp (transform.rotation, q, Time.deltaTime * rotSpeed);
//			//3
////			Quaternion q = GetRot (transform.position, new Vector3(lookPos.x, 0, lookPos.z));
////			transform.rotation = q;
//			//4
////			Quaternion targetRotation = Quaternion.LookRotation(boss.position - transform.position);
////			transform.rotation = Quaternion.Slerp(transform.rotation,targetRotation, Time.deltaTime*rotSpeed);
//			//5
//
//			Quaternion targetRotation = Quaternion.LookRotation(lookPos - transform.position);
//			transform.rotation = Quaternion.Slerp(transform.rotation,targetRotation, Time.deltaTime*rotSpeed);
//		}
//		transform.position = Vector3.SmoothDamp(transform.position, targetPos, ref camVelocity,smoothTime);
//	}
//
//	public Quaternion GetRot (Vector3 curPos, Vector3 targetPos)
//	{
//	    targetPos = new Vector3(targetPos.x, curPos.y, targetPos.z);
//		return Quaternion.LookRotation(targetPos - curPos, Vector3.up);
//	}
//}


//using UnityEngine;
//using System.Collections;

//public class TestOnSector : MonoBehaviour {
//    Transform tran1;
//    Transform tran2;
//    Material mat1;
//    Material mat2;
//    void Awake () {
//        tran1 = this.transform.Find(1002+"_body");
//        tran2 = this.transform.Find(1002+"_weapon01");
//        if(tran2 == null)
//            tran2 = this.transform.Find(1002+"_weapon");
//        mat1 = tran1.gameObject.GetComponent<Renderer>().material;
//        mat2 = tran2.gameObject.GetComponent<Renderer>().material;
//        mat1.SetFloat("_ShadowAlpha", 0.3f);
//        mat2.SetFloat("_ShadowAlpha", 0.3f);

//        mat1.SetVector("_ShadowColor", new Vector4(0f, 0f, 0f, 0.3f));
//        mat2.SetVector("_ShadowColor", new Vector4(0f, 0f, 0f, 0.3f));

//        mat1.SetVector("_ShadowDir", new Vector4(-0.2f, 1f, 0.2f, 1f));
//        mat2.SetVector("_ShadowDir", new Vector4(-0.2f, 1f, 0.2f, 1f));
//    }

//    void Update ()
//    {
//        mat1.SetFloat("_ShadowHeight", this.transform.position.y);
//        mat2.SetFloat("_ShadowHeight", this.transform.position.y);
//    }

//    void LateUpdate ()
//    {
		
//    }
//}




