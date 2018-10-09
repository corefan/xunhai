using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using LuaFramework;

public class EffectRenderObj : Node
{
//     private TaskPriority m_ePriority = TaskPriority.TaskPriority_Normal;
//     private SkillEffect effectRenderAble;
//     private OnCreateRenderObj m_CreateEvent = null;
    //特效控制器...

    public delegate void LoadComCallBack(EffectRenderObj effectRender);
    private LoadComCallBack m_LoadComCallBack = null;
    private EffectController efCtrl;
    //特效描述文件..
    private EffectObjectDescriptor des;
    /// <summary>
    /// 游戏对象ID
    /// </summary>
    private uint m_uID = 0;
    private GameObject _effectObj;

    public GameObject effectObj
    {
        set
        {
            _effectObj = value;
        }
        get
        {
            return _effectObj;
        }
    }

//     // 判定列表
//     private List<IJudge> _overJudge = new List<IJudge>();
//     // LinkerList...
//     private List<ILinker> _LinkerList = new List<ILinker>();
    public EffectRenderObj(uint uID)
    {
        m_uID = uID;
    }

    public void Release()
    {
        if (m_Node == null)
        {
            return;
        }
        if (efCtrl != null)
        {
            efCtrl.Release();
        }
//         m_CreateEvent = null;
//         if (effectRenderAble != null)
//         {
//             effectRenderAble.Destroy();
//         }
        //doLink();
        if (m_Node != null)
        {
            GameObject.DestroyImmediate(m_Node, true);
            m_Node = null;
        }
//         _overJudge = null;
//         _LinkerList = null;
    }
//     public List<IJudge> overJudge
//     {
//         get
//         {
//             return _overJudge;
//         }
//     }
//     public List<ILinker> LinkerList
//     {
//         get
//         {
//             return _LinkerList;
//         }
//     }
    /// <summary>
    /// 获取对象ID
    /// </summary>
    /// <returns></returns>
    public uint GetID()
    {
        return m_uID;
    }
    private bool _setStart = false;
    public void setStart(bool b = false)
    {
        if (b)
        {
            _dead = false;
        }
        _setStart = b;
    }
    /// <summary>
    /// 绑定骨骼
    /// </summary>
    public void bindBone(string boneName, GameObject obj, Vector3 pos)
    {
        GameObject bood = getKid(boneName, obj);
        if (bood != null)
        {
            Transform transform = bood.transform;
            GetNode().GetTransForm().parent = transform;
            GetNode().SetLocalPosition(pos);
            GetNode().SetScale(Vector3.one);
            GetNode().GetTransForm().rotation = transform.rotation;
        }
    }
    /// <summary>
    ///  找骨骼节点..
    /// </summary>
    /// <param name="name"></param>
    /// <param name="parent"></param>
    /// <returns></returns>
    public GameObject getKid(string name, GameObject parent)
    {
        //int len = parent.transform.childCount;
        bool b = false;
        if (!parent.activeSelf)
        {
            b = true;
            SetActive(parent, true);
        }
        Transform[] allTran = parent.GetComponentsInChildren<Transform>();
        if (b)
        {
            SetActive(parent, false);
        }
        for (int i = 0; i < allTran.Length; i++)
        {
            Transform t = allTran[i];
            if (t.name == name)
            {
                return t.gameObject;
            }
        }
        //foreach (Transform t in parent.GetComponentsInChildren<Transform>())
        //{
        //    if (t.name == name)
        //    {
        //        return t.gameObject;
        //    }
        //}
        return null;
    }
    /// <summary>
    ///  设置是否active...
    /// </summary>
    /// <param name="obj"></param>
    /// <param name="show"></param>
    public void SetActive(GameObject obj, bool show)
    {
        if (show ^ obj.activeSelf)
        {
            obj.SetActive(show);
        }
    }
    /// <summary>
    /// 获取节点
    /// </summary>
    /// <returns></returns>
    public Node GetNode()
    {
        return this;
    }

    public EffectObjectDescriptor GetDes()
    {
        return des;
    }

    public void SetNcCurveDurationTime(string name, float time)
    {
        if (des == null || des.ncCurveAnimationList == null) return;
        for (int i = 0; i < des.ncCurveAnimationList.Count; i++)
        {
            if (des.ncCurveAnimationList[i] != null && des.ncCurveAnimationList[i].name == name)
            {
                des.ncCurveAnimationList[i].m_fDurationTime = time;
            }
        }
    }
    public void SetParticleSystemSize(string name, float size)
    {
        if (des == null || des.particleSystemList == null) return;
        for (int i = 0; i < des.particleSystemList.Count; i++)
        {
            if (des.particleSystemList[i] != null && des.particleSystemList[i].name == name)
            {
                des.particleSystemList[i].startSize = size;
            }
        }
    }
    // 对象更新
    public bool Update(float dt)
    {
        if (cheakPlay())
        {
            return true;
        }
        //ck Linker..
        doLink();
        //ck dojudge...
        if (doJudge())
        {
            return true;
        }
        return false;
    }
    private int loop = 1;
    public void setLoop(int loops)
    {
        loop = loops;
    }
    public virtual bool cheakPlay()
    {
        if (isStopedEf)
        {
            return false;
        }
        if (loop > 0 && checkPlayComplete())
        {
            loop--;
            if (des != null)
            {
                des.setDisable();
                resetState();
                des.reStart();
                setStart(true);
            }
        }
        if (_autoDestroy && loop == 0)
        {
            dispose();
            return true;
        }
        return false;
    }
    public void dispose()
    {
        _dead = true;
        //外派事件销毁...
        loop = 1;
        isStopedEf = false;
//         _overJudge.Clear();
//         _LinkerList.Clear();
        EffectRenderObjManager.Instance().RemoveRenderobj(this);
    }
    protected void doLink()
    {
//         if (_LinkerList == null) return;
//         for (int i = 0; i < _LinkerList.Count; i++)
//         {
//             ILinker linker = _LinkerList[i];
//             if (linker.doLink())
//             {
//                 _LinkerList.Remove(linker);
//                 linker.dispose();
//                 i--;
//                 continue;
//             }
//         }
    }
    protected bool doJudge()
    {
        //这里不能 添加 JudgeTime JudgeFrame暂停后 不准...
//         for (int i = 0; i < _overJudge.Count; i++)
//         {
//             IJudge judge = _overJudge[i];
//             if (!judge.judge())
//             {
//                 dispose();
//                 return true;
//             }
//         }
        return false;
    }
    //设置名称
    public void SetName(ref string strName) { if (m_Node != null) { m_Node.name = strName; } }
    // 获取名称
    public string GetName() { if (m_Node != null) { return m_Node.name; } return ""; }
    public string effctName
    {
        get
        {
            return _effctName;
        }
    }
    private string _effctName;
    private string _url;

    private bool isStopedEf = false;
    /// <summary>
    /// 以下为预设...
    /// </summary>
    private float _size = 1;
    private float _speed = 1;
    private float _liftTime = 0;
    private bool _isStop = false;
    private bool _resize = false;
    private bool _compareLife = false;
    private bool _setLife = false;

    private bool _autoDestroy = true;

    /// <summary>
    /// 是否死亡...
    /// </summary>
    private bool _dead = false;
    public bool dead
    {
        get
        {
            return _dead;
        }
    }
    /// <summary>
    /// 渲染对象创建
    /// </summary>
    /// <param name="strObjFileName">对象描述文件名称</param>
    /// <returns>是否创建成功</returns>
    public bool Create(ref string effctName, ref string url, LoadComCallBack loadComCallBack) // obj描述文件
    {
        m_LoadComCallBack = loadComCallBack;
        //m_CreateEvent = createEvent;
        _effctName = effctName;
        _url = url;
        //m_ePriority = ePriority;
        // 创建特效
        CreateEffect(effctName, _url);
        isStopedEf = false;
        _dead = false;

        m_Node.name = effctName + "_" + m_uID.ToString();

        return true;
    }

    public void resetState()
    {
        _size = 1;
        _speed = 1;
        _isStop = false;
        _resize = false;
        _compareLife = false;
        if (efCtrl != null)
        {
            efCtrl.timeCount = 0;
        }
    }
    public bool isStopedEffect()
    {
        return isStopedEf;
    }
    // 暂停当前动作
    public void stop()
    {
        if (efCtrl != null)
        {
            isStopedEf = true;
            efCtrl.stop();
            _isStop = false;
        }
        else
        {
            _isStop = true;
        }
    }
    // 继续当前动作
    public void play()
    {
        if (efCtrl != null)
        {
            isStopedEf = false;
            efCtrl.play();
            _isStop = false;
        }
    }
    public bool checkPlayComplete()
    {
        if (efCtrl != null)
        {
            return efCtrl.checkPlayComplete();
        }
        else
        {
            return false;
        }
    }
    public void setSize(float size)
    {
        if (efCtrl != null)
        {
            efCtrl.setSize(size);
            _size = 1;
        }
        else { _size = size; }
    }
    public void reSize()
    {
        if (efCtrl != null)
        {
            efCtrl.reSize();
            _resize = false;
            _size = 1;
        }
        else { _resize = true; }
    }
    /// <summary>
    /// 时间到自动销毁..
    /// </summary>
    public bool autoDestroy
    {
        get { return _autoDestroy; }
        set { _autoDestroy = value; }
    }
    public void setLife(float lifeTime)
    {
        if (efCtrl != null)
        {
            _setLife = false;
            efCtrl.lifeTime = lifeTime;
            _liftTime = lifeTime;
        }
        else
        {
            _liftTime = lifeTime;
            _setLife = true;
        }
    }
    public void compareLife()
    {
        if (efCtrl != null)
        {
            efCtrl.compareLife();
            _compareLife = false;
        }
        else { _compareLife = true; }
    }
    public void setSpeed(float speed)
    {
        if (efCtrl != null)
        {
            efCtrl.setSpeed(speed);
            _speed = 1;
        }
        else { _speed = speed; }
    }

    //-------------------------------------------------------------------------------------------------
    // 创建特效
    private void CreateEffect(string effctName, string url)
    {
        // LuaHelper.GetResManager().LoadPrefab(url, effctName,effectCallBack);
    }

    private void effectCallBack(Object[] obj)
    {
        if (obj == null || obj[0] == null) return;
        Object prefab = obj[0];
        if (prefab == null) return;
//         GetTransForm().localPosition = Vector3.zero;
//         GetTransForm().localScale = Vector3.one;
        _effectObj = GameObject.Instantiate(prefab) as GameObject;
        _effectObj.name = _effctName;
        _effectObj.transform.parent = GetTransForm();
        _effectObj.transform.localScale = Vector3.one;
        _effectObj.transform.localPosition = Vector3.zero;
        _effectObj.transform.localRotation = Quaternion.Euler(Vector3.zero);
        des = new EffectObjectDescriptor();
        des.Init(this.GetTransForm().gameObject);
        des.RecordState();

        if (_setStart)
        {
            des.reStart();
        }
        else
        {
            des.setDisable();
        }
        efCtrl = new EffectController(this);
        //以下有顺序...不能乱..
        if (_compareLife)
        {
            compareLife();
        }
        if (_size != 1)
        {
            setSize(_size);
        }
        if (_speed != 1)
        {
            setSpeed(_speed);
        }

        if (_resize)
        {
            reSize();
        }

        if (_isStop)
        {
            stop();
        }
        if (_setLife)
        {
            setLife(_liftTime);
        }
        if (m_LoadComCallBack != null)
        {
            m_LoadComCallBack(this);
        }
    }
}
