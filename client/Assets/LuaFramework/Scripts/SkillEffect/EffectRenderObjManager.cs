using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class EffectRenderObjManager {

    private static EffectRenderObjManager _inst = null;
    /// <summary>
    /// 渲染对象列表 用于查找
    /// </summary>
    private Dictionary<uint, EffectRenderObj> m_EffectRenderObj = new Dictionary<uint, EffectRenderObj>();

    private List<EffectRenderObj> m_EffectRenderObjList = new List<EffectRenderObj>();

    /// <summary>
    /// 特效名与特效属于哪个特效组的关联;
    /// </summary>
    public static Dictionary<string, string> effectPack;
    /// <summary>
    /// 缓存池
    /// </summary>
    public EffectPool effectPool;
    /// <summary>
    /// 渲染对象ID
    /// </summary>
    private uint m_uRenderObjIDSeed = 0;

    public static EffectRenderObjManager Instance()
    {
        if (_inst == null)
        {
            _inst = new EffectRenderObjManager();
        }

        return _inst;
    }
    public EffectRenderObjManager()
    {
        effectPool = new EffectPool();
    }
    /// <summary>
    /// 创建渲染对象
    /// </summary>
    /// <param name="strObjFileName">渲染对象描述文件</param>
    /// <returns></returns>
    public EffectRenderObj CreateRenderObj(ref string effectName, ref string url, bool bCached = false)
    {
        EffectRenderObj effect = null;
        if (bCached)
        {
            if (!effectPool.downDestoryEffectsList.ContainsKey(effectName))
            {
                // 加入缓存列表，特效不清理
                effectPool.downDestoryEffectsList.Add(effectName, false);
            }
        }
        //从池里拿...
        effect = effectPool.GetObject(effectName) as EffectRenderObj;
        if (effect != null)
        {
            m_EffectRenderObj[effect.GetID()] = effect;
            if (!m_EffectRenderObjList.Contains(effect))
            {
                m_EffectRenderObjList.Add(effect);
            }
            reStartEffect(effect);
        }
        else
        {
            effect = new EffectRenderObj(m_uRenderObjIDSeed);
            effect.Create(ref effectName, ref url, loadComCallBack);
            m_EffectRenderObj[m_uRenderObjIDSeed] = effect;
            if (!m_EffectRenderObjList.Contains(effect))
            {
                m_EffectRenderObjList.Add(effect);
            }
            m_uRenderObjIDSeed++;
        }
        return effect;
    }

    private void loadComCallBack(EffectRenderObj effectRender)
    {
        reStartEffect(effectRender);
    }
    private void reStartEffect(EffectRenderObj obj)
    {
        if (obj == null)
        {
            return;
        }

        EffectObjectDescriptor eod = obj.GetDes();
        if (eod != null)
        {
            eod.reStart();
        }
        obj.setStart(true);
    }
    private void resetEffect(EffectRenderObj gameObject)
    {
        if (gameObject == null)
        {
            return;
        }
        EffectObjectDescriptor eod = gameObject.GetDes();
        if (eod != null)
        {
            eod.setDisable();
        }
        gameObject.resetState();
        gameObject.setStart(false);
    }
    public void RemoveRenderobj(EffectRenderObj obj, bool Catche = true)
    {
        if (obj == null)
        {
            return;
        }
        //Debug.Log();
        m_EffectRenderObj.Remove(obj.GetID());
        if (m_EffectRenderObjList.Contains(obj))
        {
            m_EffectRenderObjList.Remove(obj);
        }
        if (Catche)
        {
            resetEffect(obj);
            effectPool.FreeObject(obj);
        }
    }

    public void Update(float dt)
    {
        // 渲染对象更新
        for (int i = 0; i < m_EffectRenderObjList.Count; i++)
        {
            if (m_EffectRenderObjList[i].Update(dt))
            {
                i--;
                continue;
            }
        }
    }
    public void ClearPool(bool bCleanCache)
    {
        effectPool.Clear(bCleanCache);
        m_EffectRenderObj.Clear();
        for (int i = 0; i < m_EffectRenderObjList.Count; i++)
        {
            m_EffectRenderObjList[i].Release();
            m_EffectRenderObjList[i] = null;
        }
        m_EffectRenderObjList.Clear();
    }

    public void Release()
    {
        ClearPool(true);
        _inst = null;
    }
}
