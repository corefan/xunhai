using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class NormalCameraController : MonoBehaviour 
{
    public Transform player;            //角色头部（设置空物体）位置信息
    public Transform playerAgent;            //角色头部（设置空物体）位置信息
    private Vector3 tagetPostion;       //相机看向的目标点
    private Vector3 ve3;                //平滑阻尼的ref参数
    Quaternion angel;                   //相机看向目标的旋转值
    public float speed=6.0f;                 //相机移动速度
    public float upFloat=6.5f;               //Y轴上升距离
    public float backFloat=9.3f;             //Z轴与主角的距离

    // 震动标志位
    private bool _isShake = false;
    // 震动幅度
    public float shakeLevel = 4;
    // 震动时间
    public float setShakeTime = 1.2f;
    private float shakeTime = 0.0f;
    private int zhengdong = 1;
    public int pinlv = 4;
    private int dps = 0;
    public float maxZhenpin = 52.6f;
    public float minZhenpin = 52f;
    private Camera selfCamera;

    Transform _defaultAgent;
    public GameObject agent;
    Transform m_transsform;//摄像机

    public Quaternion agentRot = Quaternion.identity;

    public GameObject rain;
    public GameObject snow;
    void Awake()
    {
        if (playerAgent == null)
        {
            agent = new GameObject("playerAgent");
            playerAgent = agent.transform;
            rain = Instantiate(Resources.Load("Other/RainWithSplash")) as GameObject;
            snow = Instantiate(Resources.Load("Other/SnowWithSplash"))as GameObject;
            if(rain != null && snow != null)
            {
                snow.transform.SetParent(playerAgent, false);
                rain.transform.SetParent(playerAgent, false);
                StartCoroutine(WeatherShow());
            }
        }
        _defaultAgent = playerAgent;
        m_transsform = this.transform;//定义自己 
        selfCamera = GetComponent<Camera>();
    }

    IEnumerator WeatherShow()
    {
        while(rain != null && snow != null)
        {
            int r = Random.Range(1, 10);
            if(rain.activeSelf)
                rain.SetActive(false);
            if (snow.activeSelf)
                snow.SetActive(false);
            if(r ==2)
            {
                rain.SetActive(true);
            }else if(r == 3)
            {
                snow.SetActive(true);
            }
            yield return new WaitForSeconds(Random.Range(90, 300));
        }
        yield break;
    }

    //设置转角
    public void SetRot(float v)
    {
        if (playerAgent != null)
        {
            playerAgent.localEulerAngles = new Vector3(0, v, 0);
            agentRot = playerAgent.localRotation;
        }
    }

    //void Update()
    //{
    //    if (Input.touchCount == 1)  // 判断触摸数量为单点触摸  
    //    {
    //    }
    //    if (Input.GetMouseButton(0))
    //    {
    //    }
    //}

    void Update() //LateUpdate
    {
        if (player == null) return;
        Vector3 targetPos = player.position + Vector3.up;
        playerAgent.position = targetPos;
        playerAgent.localRotation = agentRot;
        //记录相机初始位置
        targetPos = playerAgent.position + playerAgent.up * upFloat - playerAgent.forward * backFloat;//刷新相机目标点的坐标

        angel = Quaternion.LookRotation(playerAgent.position - targetPos, Vector3.up);
        //m_transsform.localEulerAngles = Vector3.Slerp(m_transsform.localEulerAngles, angel.eulerAngles, speed * Time.deltaTime);
        m_transsform.localRotation = Quaternion.Slerp(m_transsform.localRotation, angel, speed * Time.deltaTime);
        m_transsform.position = Vector3.Slerp(m_transsform.position, targetPos,  speed * Time.deltaTime);//主角的移动和看向
        
        if (_isShake)
        {
            if (shakeTime > 0)
            {
                shakeTime -= Time.deltaTime;
                if (dps % pinlv ==0)
                {
                    if (zhengdong > 0)
                    {
                        selfCamera.fieldOfView = minZhenpin;
                        zhengdong = zhengdong * -1;
                    }
                    else if (zhengdong < 0)
                    {
                        selfCamera.fieldOfView = maxZhenpin;
                        zhengdong = zhengdong * -1;
                    }
                }
                dps++;
                if (shakeTime <= 0)
                {
                    //镜头复位
                    selfCamera.fieldOfView = 52;
                    _isShake = false;
                    shakeTime = setShakeTime;
                    dps = 0;
                }
            }
        }
    }

    /// <summary>
    /// 射线检测，主角向后检测是否有相机跟随
    /// </summary>
    /// <param name="v3">用来计算射线发射的方向</param>
    /// <returns>是否检测到</returns>
    Vector3 Function(Vector3 v3)
    {
        RaycastHit hit;
        if (Physics.Raycast(player.position, v3 - player.position, out hit, 5.0f))
        {
            if (hit.collider.tag != "MainCamera")
            {
                v3 = hit.point + transform.forward * 0.5f;
            }
        }
        return v3;
    }

    public void SetFollowTarget(Transform _focusTarget)
    {
        player = _focusTarget;
        Vector3 targetPos = player.position + Vector3.up;
        playerAgent.position = targetPos;
        playerAgent.rotation = agentRot;
        //记录相机初始位置
        targetPos = playerAgent.position + playerAgent.up * upFloat - playerAgent.forward * backFloat;//刷新相机目标点的坐标
        m_transsform.position = targetPos;//主角的移动和看向
        m_transsform.rotation =Quaternion.LookRotation(playerAgent.position - targetPos);
    }
    public void SetDuration(float v)
   {
        shakeTime = v;
   }

    public void shake()
    {
        _isShake = true;
    }

    void OnDestroy()
    {
        snow = null;
        rain = null;
        player = null;
    }
}