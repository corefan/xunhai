using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

public class EffectPool
{
    private static Node effectPoolObject;
    private Dictionary<string, EffectPoolItem> _map = new Dictionary<string, EffectPoolItem>();
    private Dictionary<string, EffectPoolItem> _downDestorymap = new Dictionary<string, EffectPoolItem>();
    //不销毁特效名称;
    public Dictionary<string, bool> downDestoryEffectsList = new Dictionary<string, bool>();

    public EffectPool()
    {
        effectPoolObject = new Node();
        effectPoolObject.GetTransForm().name = "EffectPool";
    }

    public void OnDestroy()
    {
        _downDestorymap.Clear();
        foreach (KeyValuePair<string, EffectPoolItem> kvp in _map)
        {
            kvp.Value.clear();
        }
        _map.Clear();
        downDestoryEffectsList.Clear();
        effectPoolObject = null;
    }
    /// <summary>
    /// 添加预设
    /// </summary>
    /// <param name="name"></param>
    /// <param name="prefab"></param>
    public void AddPrefab(string name)
    {
        if (!_map.ContainsKey(name))
        {
            _map[name] = new EffectPoolItem();
        }
    }

    public void RemovePrefab(string name)
    {
        if (_map.ContainsKey(name))
        {
            _map[name].clear();
            _map.Remove(name);//要把item也移除了
        }
    }

    public bool HasItem(string effectName)
    {
        return _map.ContainsKey(effectName);
    }
    public int ItemCount(string effectName)
    {
        return _map[effectName].freeList.Count;
    }

    /// <summary>
    /// 获取对象
    /// </summary>
    /// <param name="name"></param>
    /// <returns></returns>
    public EffectRenderObj GetObject(string name)
    {
        EffectRenderObj go = null;
        if (_map.ContainsKey(name))
        {
            go = _map[name].getObject();
        }
        return go;
    }

    public void FreeObject(EffectRenderObj obj)
    {
        if (obj == null)
        {
            return;
        }
        string name = obj.effctName;
        AddPrefab(name);

        _map[name].freeObject(obj);

    }

    public bool IsNoDestroyEffect(string strEffectName)
    {
        return downDestoryEffectsList.ContainsKey(strEffectName);
    }

    public void Clear(bool bCleanCache)
    {
        foreach (KeyValuePair<string, EffectPoolItem> kvp in _map)
        {
            if (!bCleanCache)
            {
                if (!downDestoryEffectsList.ContainsKey(kvp.Key))
                {
                    kvp.Value.clear();

                    // 通知管理器 特效删除
                    //                        EffectManager.Instance().RemoveEffect(kvp.Key);
                }
                else
                {
                    if (!_downDestorymap.ContainsKey(kvp.Key))
                    {
                        _downDestorymap[kvp.Key] = kvp.Value;
                    }
                }
            }
            else
            {
                kvp.Value.clear();
                //                    EffectManager.Instance().RemoveEffect(kvp.Key,true);
                downDestoryEffectsList.Clear();
                //                  downDestoryEffectsCount.Clear();
            }
        }
        _map.Clear();
        foreach (KeyValuePair<string, EffectPoolItem> kvp in _downDestorymap)
        {
            _map[kvp.Key] = kvp.Value;
        }
        _downDestorymap.Clear();
    }

    class EffectPoolItem
    {
        public List<EffectRenderObj> freeList = new List<EffectRenderObj>();

        public EffectPoolItem()
        {

        }
        /// <summary>
        /// 获取对象
        /// </summary>
        /// <returns></returns>
        public EffectRenderObj getObject()
        {
            EffectRenderObj go = null;
            if (freeList.Count > 0)
            {
                int index = freeList.Count - 1;
                go = freeList[index];
                freeList.RemoveAt(index);
                if (go == null)
                {
                    return go;
                }
                else
                {
                    go.GetNode().Detach();
                    return go;
                }
            }
            else
            {
                return go;
            }
        }


        /// <summary>
        /// 释放对象
        /// </summary>
        /// <param name="gameObject"></param>
        public void freeObject(EffectRenderObj obj)
        {
            if (obj != null)
            {
                if (!freeList.Contains(obj))
                {
                    freeList.Add(obj);
                }
                if (effectPoolObject != null)
                {
                    obj.GetNode().Attach(effectPoolObject);
                }
            }
        }
        public void clear()
        {
            for (int i = 0; i < freeList.Count; i++)
            {
                if (freeList[i] != null)
                {
                    EffectRenderObjManager.Instance().RemoveRenderobj(freeList[i], false);
                    freeList[i].Release();
                }
            }
            freeList.Clear();
        }


    }
}
