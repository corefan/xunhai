#region 说明
// 功  能： CameraController.cs
// 描  述： 控制器
// 时  间： 2016-08-06 10:54:06
// 作  者： zwx
// E-mail： zhuang_wx@qq.com
// 项目名： SKGame
#endregion

using DG;
using DG.Tweening;
using FairyGUI;
using System.Collections;
using UnityEngine;

/// <summary>
/// 相机跟随
/// </summary>
public class CameraController : MonoBehaviour
{
    [Header("0.5到1之间")]
    public float paramA = 0.8f;
    [Header("0到2之间")]
    float paramB = 0.7f;
    [Header("0到2之间")]
    float paramC = 1.0f;
    [Header("boss内圈")]
    public float bossInnerDistance = 0.0f;

    private bool isTest = false;
    /// 镜头偏离目标的方向
    private Vector3 m_Offset = new Vector3(1.0f, 0.8f, -1.0f);
    public Vector3 m_cameraPos = Vector3.zero;
    /// 照相机追随的目标 
    public Transform followTarget;
    /// 照相机追随的boss
    public Transform boss;
    /// 速度
    private Vector3 velocity;
    /// 上一次更新到的位置
    private Vector3 m_lastPos = Vector3.zero;
    /// 镜头相对目标X轴旋转角度 
    public float m_fAngleX = 0.0f;

    /// 镜头相对目标Y轴旋转角度
    public float m_fAngleY = 0.0f;

    /// 是否处于缓动状态
    private bool m_bTween = true;
    /// 缓动时间
    private float m_fTweenTime = 0.5f;
    /// 内圈 安定范围
    public float m_InnerRing = 5f;
    /// 外圈 安定范围
    private float m_OuterRing = 12f;
    /// 摄像机X轴偏移参数 I
    public float m_rotX_paraI = 0.1f;
    /// 摄像机X轴偏移参数 II
    public float m_rotX_paraII = 0.003f;
    /// 最大值X偏移
    public float m_rotXMax = 28f;
    /// 缩放最小比例
    public float m_scaleMinimum = 0.6f;
    /// 缩放最大比例
    public float m_scaleMaxmum = 2.5f;
    /// 摄像机缩放参数I
    public float m_cameraScale_paraI = 0.3f;
    /// 摄像机缩放参数II
    public float m_cameraScale_paraII = 0.2f;
    /// 摄像机缓动速度 参数I
    public float m_cTween_paraI = 0.1f;
    /// 摄像机缓动速度 参数II
    public float m_cTween_paraII = 0.03f;
    //private float pos_para = 0.8f;
    /// 记录进入安定范围的角度
    private float curDisAng = 0;
    /// 记录进入安定范围摄像机X轴偏移
    private float oneRotX = -100;
    private Vector3 scallF = Vector3.zero;
    private Vector3 behindScall = Vector3.zero;
    private Vector3 pos;
    /// 镜头离目标的距离
    public float m_fDis = 30f;
    /// 距离小于此值时，直接放回跟随点
    private float m_fNearDis = 0.003f;
    /// 外圈默认距离
    public float wQDefaultDis = 5f;
    /// 内圈默认距离
    public float nQDefaultDis = 9.4f;
    /// 外圈最大距离
    public float wQMaxDis = 6.78f;

    void Start()
    {
        velocity = Vector3.zero;
        //Camera.main.farClipPlane = 80;
    }
    public void SetCameraTest(bool boo)
    {
        isTest = boo;
    }

    /// <summary>
    /// 设置相机偏移参数
    /// </summary>
    public void SetCameraOffset(Vector3 cameraPos, float fAngleX, float fAngleY, float fDis, float InnerRing, float OuterRing, float rotX_paraI,
        float rotX_paraII, float rotXMax, float scaleMinimum, float cameraScale_paraI, float cameraScale_paraII, float fTweenTime,
        float cTween_paraI, float cTween_paraII, float fNearDis, float wQDefaultDis, float nQDefaultDis, float wQMaxDis, float tweenTime = 0.3f)
    {
        m_cameraPos = cameraPos;
        m_fAngleX = fAngleX;
        m_fAngleY = fAngleY;
        m_fDis = fDis;
        m_InnerRing = InnerRing;
        m_OuterRing = OuterRing;
        m_rotX_paraI = rotX_paraI;
        m_rotX_paraII = rotX_paraII;
        m_rotXMax = rotXMax;
        m_scaleMinimum = scaleMinimum;
        m_cameraScale_paraI = cameraScale_paraI;
        m_cameraScale_paraII = cameraScale_paraII;
        m_fTweenTime = fTweenTime;
        m_cTween_paraI = cTween_paraI;
        m_cTween_paraII = cTween_paraII;
        m_fNearDis = fNearDis;
        this.wQDefaultDis = wQDefaultDis;
        this.nQDefaultDis = nQDefaultDis;
        this.wQMaxDis = wQMaxDis;
        this.tweenTime = tweenTime;
           
        ClacCameraOffset();

        if (followTarget)
        {
            if (isFirstSet || (!preM_Offset.Equals(m_Offset) && !preM_cameraPos.Equals(m_cameraPos)) || preM_fAngleX != m_fAngleX || preF_fAngleY != m_fAngleY)
            {
                if (isFirstSet)
                    isNeedChangeAngleOfView = false;
                else
                    isNeedChangeAngleOfView = true;
                targetTweenPos = followTarget.position + m_cameraPos + m_Offset;
                tagetTweenUpdatePosQ = Quaternion.Euler(m_fAngleX, m_fAngleY, transform.rotation.z);
                preM_cameraPos = m_cameraPos;
                preM_Offset = m_Offset;
                preM_fAngleX = m_fAngleX;
                preF_fAngleY = m_fAngleY;
            }
        }
   }

    /// 初始化设置
    bool isFirstSet = true;
    /// 上一次偏移量记录
    Vector3 preM_Offset;
    /// 上一次相机位置记录
    Vector3 preM_cameraPos;
    /// 上一次X轴旋角度偏移记录
    float preM_fAngleX;
    /// 上一次Y轴旋角度偏移记录
    float preF_fAngleY;
    /// 计算镜头偏移
    private void ClacCameraOffset()
    {
        Quaternion rot = new Quaternion();
        rot.eulerAngles = new Vector3(m_fAngleX, m_fAngleY, 0.0f);
        Matrix4x4 mat = new Matrix4x4();
        mat.SetTRS(Vector3.zero, rot, Vector3.one);
        m_Offset = -mat.GetColumn(2) * m_fDis;
    }

    /// 相机追随基础类
    public void pupdate()
    {
        if (followTarget == null) return;
        if (boss == null)
        {
            //每帧及时更新目标点
            targetTweenPos = followTarget.position + m_cameraPos + m_Offset;
            if (isNeedChangeAngleOfView)
                ChangeAngleOfView();
            else 
                NormalFollow();
        }else
            BossCameraHandler2();

        if (isTestFocus) 
            StartFocusBoss();
    }

    public bool isTestFocus = false;
    public float distanceScale;
    public float focusHeight;
    public float focusXRotation;
    public float focusYRotation;

    public void SetFocusBossParam(float pDistanceScale, float pFocusHeight, float pFocusXRotation, float pFocusYRotation) {
        distanceScale = pDistanceScale;
        focusHeight = pFocusHeight;
        focusXRotation = pFocusXRotation;
        focusYRotation = pFocusYRotation;
    }

    /// 聚焦boss视距
    float FocusBossDis = -9f;
    /// 开始聚焦boss
    public void StartFocusBoss()
    {
        m_fDis = FocusBossDis;
        ClacCameraOffset();

        transform.position = followTarget.position + (boss.position - followTarget.position)* distanceScale + (new Vector3(0, focusHeight, 0));
        transform.LookAt(boss.position + (new Vector3(focusYRotation, focusXRotation, 0)));
        boss.LookAt(followTarget.position);
        isFocusOnBoss = true;
    }

    Vector3 stopFocusBossStartPosition;
    Quaternion stopFocusBossStartRotation;
    Vector3 stopFocusBossTweenToPosition;
    Quaternion stopFocusBossTweenToRotation;
    float stopFocusBossStartTime;
    float stopFocusBossTweenCostTime = 0.3f;
    /// 是否聚焦于boss
    bool isFocusOnBoss;
    /// 是否需要朝向缓动
    bool isNeedLooKAtTween;

    /// <summary>
    /// 结束聚焦Boss
    /// </summary>
    public void StopFocusBoss()
    {
        stopFocusBossStartTime = Time.time;
        stopFocusBossStartPosition = transform.position;
        stopFocusBossStartRotation = transform.rotation;
        StartCoroutine(TweenToBossCamerra());
    }
    /// <summary>
    /// 缓动到boss相机
    /// </summary>
    /// <returns></returns>
    IEnumerator TweenToBossCamerra()
    {
        while (true)
        {
            transform.position = Vector3.Lerp(stopFocusBossStartPosition, stopFocusBossTweenToPosition, (Time.time - stopFocusBossStartTime) / stopFocusBossTweenCostTime);
            transform.rotation = Quaternion.Lerp(stopFocusBossStartRotation, stopFocusBossTweenToRotation, (Time.time - stopFocusBossStartTime) / stopFocusBossTweenCostTime);
            if (Vector3.Distance(transform.position, stopFocusBossTweenToPosition) < 0.000002f)
            {
                transform.position = stopFocusBossTweenToPosition;
                transform.rotation = stopFocusBossTweenToRotation;
                isFocusOnBoss = false;
                //isNeedLooKAtTween = true;
                //GlobalDispatcher.GetInstance().DispatchEvent("Stop_Camera_Focus_Boss");
                yield break;
            }
            yield return new WaitForSeconds(0);
        }
    }

    /// <summary>
    /// 跟随主角
    /// </summary>
    public void NormalFollow()
    {
        comparePoint = followTarget.position + m_cameraPos + m_Offset;
        if (!lastTweenUpdatePos.Equals(comparePoint))
        {
            transform.position = comparePoint;
            if (isFirstSet)//初始化LookAt一次
            {
                transform.LookAt(followTarget.position + m_cameraPos);
                isFirstSet = false;
            }
            lastTweenUpdatePos = transform.position;
        }
    }

    /// 上一次缓动更新到位置
    Vector3 lastTweenUpdatePos;
    /// 上一次缓动更新到的角度
    Quaternion lastTweenUpdateQ;
    /// 切换视角要达到的目标位置
    Vector3 targetTweenPos;
    /// 切换视角要达到的目标角度
    Quaternion tagetTweenUpdatePosQ;
    /// 是否需要切换视角
    bool isNeedChangeAngleOfView = false;
    /// 跟随比较点
    Vector3 comparePoint;
    /// 缓动耗时
    float tweenTime = 0.3f;
    /// 缓动开始时间标记
    float tweenStartTime;
    /// 是否切换视角中
    bool isTweenChanging = false;

    /// <summary>
    /// 相机视角切换(postiion、rotation插值)
    /// </summary>
    void ChangeAngleOfView()
    {
        if (!isTweenChanging)
        {
            lastTweenUpdatePos = transform.position;
            lastTweenUpdateQ = transform.rotation;
            if (Vector3.Distance(transform.position, targetTweenPos) > 0.000002f || !transform.rotation.Equals(tagetTweenUpdatePosQ) )
            {
                tweenStartTime = Time.time;
                StartCoroutine(NormalChangeTween());
                isTweenChanging = true;
            }
        }
    }

    /// <summary>
    /// 切换视角协程
    /// </summary>
    IEnumerator NormalChangeTween()
    {
        while (true)
        {
            if (!transform) yield break;
            transform.position = Vector3.Lerp(lastTweenUpdatePos, targetTweenPos, (Time.time - tweenStartTime) / tweenTime);
            transform.rotation = Quaternion.Lerp(lastTweenUpdateQ, tagetTweenUpdatePosQ, (Time.time - tweenStartTime) / tweenTime);
            if (Vector3.Distance(transform.position, targetTweenPos) < 0.000002f)
            {
                isNeedChangeAngleOfView = false;
                isTweenChanging = false;
                transform.position = targetTweenPos;
                transform.rotation = tagetTweenUpdatePosQ;

                lastTweenUpdatePos = transform.position;
                lastTweenUpdateQ = transform.rotation;
                yield break;
            }
            yield return new WaitForSeconds(0);
        }
    }

    /**/
    ///----------------- boss 相机 圈内旋转(戴锦雄版本 圈内绕圆旋转实现) 基于向量 start-------------------
    float maxCameraDistanceOffsetFromPlayer = 3.0f;
    float minCameraDistanceOffsetFromPlayer = 8.0f;
    //float bossInnerDistance = 6.0f;
    float maxCameraHightOffsetFromPlayer = 2.0f;
    float minCameraHightOffsetFromPlayer = 5.0f;

    float distanceBetweenBossAndPlayer;
    Vector3 cameraOutterDistanceDirection;
    float distancePercent;
    Vector3 newTargetToPoint;
    Quaternion targetLookAtRotation;
    float horizontalDistanceBetweenBossAndPlayer;
    Vector3 centerPoint;
    Vector3 lookAtPoint;

    float tweenPostionSpeed;
    float tweenRotateSpeed;

    Vector3 innerCameraDiretion;
    float innerCameraDistance;
    Vector3 lastEnterPoint;
    Vector3 newDirection;

    /// <summary>
    /// 是否触发进入内圈
    /// </summary>
    bool innerTrigger = false;
    Vector3 enterCameraPoint;
    Vector3 enterBossPoint;
    Vector3 enterPlayerPoint;

    Vector3 circumferenceCenterPoint;
    Vector3 circumferenceCenterPointDirection;
    float circumferenceRadius;
    float cameraHightOffset;

    public void BossCameraHandler2()
    {
        tweenPostionSpeed = 8;
        tweenRotateSpeed = 10;

        distanceBetweenBossAndPlayer = Vector3.Distance(followTarget.position, boss.position);
        horizontalDistanceBetweenBossAndPlayer = Vector2.Distance(new Vector2(followTarget.position.x, followTarget.position.z), new Vector2(boss.position.x, boss.position.z));

        distancePercent = (horizontalDistanceBetweenBossAndPlayer - bossInnerDistance) / maxCameraDistanceOffsetFromPlayer;
        distancePercent = distancePercent < 0 ? 0 : distancePercent;
        distancePercent = distancePercent > 1 ? 1 : distancePercent;

        if (horizontalDistanceBetweenBossAndPlayer > bossInnerDistance)
        {//外圈
            cameraOutterDistanceDirection = ((new Vector3(followTarget.position.x, boss.position.y, followTarget.position.z)) - (new Vector3(boss.position.x, boss.position.y, boss.position.z))).normalized;

            newTargetToPoint = boss.position +
                               cameraOutterDistanceDirection * (horizontalDistanceBetweenBossAndPlayer + minCameraDistanceOffsetFromPlayer + maxCameraDistanceOffsetFromPlayer * distancePercent) +
                               Vector3.up * (minCameraHightOffsetFromPlayer + maxCameraHightOffsetFromPlayer * distancePercent);
            innerTrigger = false;
        }
        else
        {//内圈
            if (!innerTrigger)
            {//进入内圈瞬间
                cameraHightOffset = transform.position.y - boss.position.y;
                enterBossPoint = boss.position;
                enterPlayerPoint = followTarget.position;
                enterCameraPoint = new Vector3(transform.position.x, boss.position.y, transform.position.z);
                circumferenceRadius = Vector3.Distance(enterCameraPoint, enterBossPoint) * 0.5f*0.5f;
                circumferenceCenterPointDirection = (enterCameraPoint - enterBossPoint).normalized;
                innerTrigger = true;
            }

            Vector3 outterPoint = enterBossPoint + (enterBossPoint - enterCameraPoint).normalized * bossInnerDistance;
            Vector3 enterDynamicPoint = enterBossPoint + (enterCameraPoint - enterBossPoint).normalized * bossInnerDistance;
            Vector3 enterDynamicToOutterDirection = enterDynamicPoint - outterPoint;
            Vector3 playerToOutterDirection = followTarget.position - outterPoint;

            float projectPercent = (Vector3.Project(playerToOutterDirection, enterDynamicToOutterDirection).magnitude / (bossInnerDistance * 2));
            projectPercent = projectPercent > 0.99f ? 1 : projectPercent;
            if (projectPercent.ToString() == "NaN")
            {
                projectPercent = 0;
            }
            
            circumferenceCenterPoint = boss.position + 
                                       circumferenceCenterPointDirection * (Vector3.Distance(enterCameraPoint, enterBossPoint) * 0.5f + circumferenceRadius * projectPercent) + 
                                       Vector3.up* cameraHightOffset;

            newTargetToPoint = circumferenceCenterPoint + 
                               ((new Vector3(followTarget.position.x, boss.position.y, followTarget.position.z)) - boss.position).normalized* circumferenceRadius;
        }

        centerPoint = getCenterCompute(followTarget.position.x, followTarget.position.y, followTarget.position.z, boss.position.x, boss.position.y, boss.position.z);
        lookAtPoint = centerPoint + (boss.position - centerPoint).normalized * ((1 - distancePercent) * Vector3.Distance(centerPoint, boss.position));
        targetLookAtRotation = Quaternion.LookRotation(lookAtPoint - transform.position, Vector3.up);

        if (!isFocusOnBoss)
        {
            if (horizontalDistanceBetweenBossAndPlayer > bossInnerDistance)
            {
                float offsetSpeed = Vector3.Distance(transform.position, newTargetToPoint) / 2.0f * 1.0f;
                transform.position = Vector3.Lerp(transform.position, newTargetToPoint, Time.deltaTime * tweenPostionSpeed * offsetSpeed * 0.09f);
            }
            else
            {
                transform.position = Vector3.Lerp(transform.position, newTargetToPoint, Time.deltaTime * tweenPostionSpeed);
            }
            transform.rotation = Quaternion.Lerp(transform.rotation, targetLookAtRotation, Time.deltaTime * tweenRotateSpeed);
        }

        stopFocusBossTweenToRotation = targetLookAtRotation;
        stopFocusBossTweenToPosition = newTargetToPoint;

    }
    ///----------------- boss 相机 圈内旋转(戴锦雄版本 圈内绕圆旋转实现) 基于向量 end-------------------

    // 触摸位置
    private Vector2 m_TouchPos = new Vector2();
    private Vector2 m_MousePos = new Vector2();
    private bool m_bMoved = false;
    // 移动忽略距离
    private const float MOVE_INOREGE_DIS = 5f;

    private bool m_bDrag = false;
    private Vector2 m_vStartPos = Vector2.zero;
    private Vector3 m_vStartAngle = Vector3.zero;
    private Vector3 m_vStartCamPos = Vector3.zero;
    private Vector3 m_beginOffset;

    void Update()
    {
        if (!isTest || followTarget == null) return;
        //begin
        if (Input.GetMouseButtonDown(0))
        {
            m_TouchPos.x = Input.mousePosition.x;
            m_TouchPos.y = Input.mousePosition.y;

            m_MousePos.x = Input.mousePosition.x;
            m_MousePos.y = Input.mousePosition.y;

            m_bDrag = true;
            m_vStartPos.x = m_TouchPos.x;
            m_vStartPos.y = m_TouchPos.y;
            m_vStartCamPos = transform.position;
            Quaternion rot = transform.rotation;
            m_vStartAngle.x = rot.eulerAngles.x;
            m_vStartAngle.y = rot.eulerAngles.y;
            m_vStartAngle.z = rot.eulerAngles.z;
            Vector3 pos = boss == null ? followTarget.position : boss.position;
            pos.y += 0.35f; // 将目标上移
            m_beginOffset = m_vStartCamPos - pos;
        }
        //move
        if (Input.GetMouseButton(0))
        {
            if (m_bDrag)
            {

                if (Input.mousePosition.x != m_MousePos.x || Input.mousePosition.y != m_MousePos.y)
                {
                    if (m_bMoved)
                    {
                        MoveCamera();
                    }

                    if (GetDistance(ref m_TouchPos, ref m_MousePos) > MOVE_INOREGE_DIS && !m_bMoved) // 忽略微小移动
                    {
                        MoveCamera();
                        m_bMoved = true;
                    }
                }

                m_MousePos.x = Input.mousePosition.x;
                m_MousePos.y = Input.mousePosition.y;
            }
        }
        //end
        if (Input.GetMouseButtonUp(0))
        {
            m_bDrag = false;
        }
        //Scroll
        if (Input.GetAxis("Mouse ScrollWheel") != 0)
        {
            Vector3 pos = boss == null ? followTarget.position : boss.position;
            Vector3 camPos = transform.position;
            Vector3 dir = camPos - pos;  //得到相机的朝向方向
            Vector3 newPos = camPos + (float)Input.GetAxis("Mouse ScrollWheel") * dir;
            transform.position = newPos;
            transform.LookAt(pos, Vector3.up);
        }
    }

    /// <summary>
    /// 移动摄像机
    /// </summary>
    void MoveCamera()
    {
        float xpos = m_MousePos.x;
        xpos = xpos - m_vStartPos.x;
        float ypos = m_MousePos.y;
        ypos = ypos - m_vStartPos.y;
        Vector3 pos = boss == null ? followTarget.position : boss.position;
        Vector3 dir = m_beginOffset;
        //dir.Normalize();
        Quaternion rot = new Quaternion();
        rot.eulerAngles = new Vector3(-ypos * 0.1f, xpos * 0.1f, 0);
        dir = rot * dir;
        m_Offset = dir;
        dir = pos + dir;
        transform.position = dir;
        transform.LookAt(pos, Vector3.up);
    }

    /// <summary>
    /// 获取2个2维向量间的距离
    /// </summary>
    /// <param name="bQuick">是否返回距离平方</param>
    /// <returns></returns>
    public float GetDistance(ref Vector2 pos1, ref Vector2 pos2, bool bQuick = true)
    {
        float x = pos2.x - pos1.x;
        float y = pos2.y - pos1.y;
        if (bQuick)
        {
            return x * x + y * y;
        }
        return Mathf.Sqrt(x * x + y * y);
    }

    /// <summary>
    /// 计算两点间的中心点
    /// </summary>
    /// <returns></returns>
    public static Vector3 getCenterCompute(float x1, float y1, float z1, float x2, float y2, float z2)
    {
        float ctX = x1 + (x2 - x1) *0.5f;
        float ctY = y1 + (y2 - y1) *0.5f;
        float ctZ = z1 + (z2 - z1) *0.5f;
        return new Vector3(ctX, ctY, ctZ);
    }


    ///// <summary>
    ///// 设定当前摄像机的颜色度
    ///// </summary>
    ///// <param name="duration">Duration.</param>
    //public static void SetColorCorrectionCurvesSaturation(int duration)
    //{
    //    if (_colorCor)
    //        _colorCor.saturation = duration;
    //}
}