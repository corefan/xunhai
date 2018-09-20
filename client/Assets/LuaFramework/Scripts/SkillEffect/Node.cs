using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class Node
{
    protected UnityEngine.GameObject m_Node = new UnityEngine.GameObject();
    protected UnityEngine.Transform m_NodeTransform = null;

    /// <summary>
    ///  子节点列表
    /// </summary>
    private List<Node> m_Child = new List<Node>();
    public Node()
    {
        if (m_Node != null)
        {
            m_Node.name = "RenderObj";
            m_NodeTransform = m_Node.transform;
        }
    }
    /// <summary>
    /// 获取节点标识
    /// </summary>
    /// <returns></returns>
    public int tag { get; set; }

    /// <summary>
    /// 父节点
    /// </summary>
    public Node parent { get; set; }

    /// <summary>
    /// 附加到父节点
    /// </summary>
    /// <param name="parantNode"></param>
    public void Attach(Node parantNode, int nTag = 0)
    {
        parantNode.AddChild(this, nTag);

        parent = parantNode;
    }

    /// <summary>
    /// 从父节点脱离解除父子关系
    /// </summary>
    public void Detach()
    {
        if (m_Node == null)
        {
            return;
        }

        parent.RemoveChild(this);
        parent = null;
    }
    /// <summary>
    /// 添加子节点
    /// </summary>
    /// <param name="node">子节点</param>
    /// <param name="strTag">节点标识</param>
    public void AddChild(Node node, int nTag = 0)
    {
        node.tag = nTag;

        if (m_Child.Contains(node))
        {
            //Utility.Logger.LogError("Node.AddChild: 对象已经存在子节点列表中，不能重复添加！");
            return;
        }
        if (m_NodeTransform == null || node == null || node.GetTransForm() == null) return;
        node.GetTransForm().parent = m_NodeTransform;

        /// 添加到子节点列表
        m_Child.Add(node);
    }

    /// <summary>
    /// 删除节点
    /// </summary>
    /// <param name="node"></param>
    public void RemoveChild(Node node)
    {
        node.GetTransForm().parent = null;

        m_Child.Remove(node);
    }
    /// <summary>
    ///  各种变换函数
    /// </summary>
    /// <param name="pos"></param>
    public virtual void SetLocalPosition(Vector3 pos)
    {
        if (m_NodeTransform != null)
            m_NodeTransform.localPosition = pos;
    }
    public Vector3 GetLocalPosition()
    {
        return m_NodeTransform.localPosition;
    }

    public virtual void SetLocalRotate(Quaternion rotate)
    {
        if (m_NodeTransform != null)
        {
            m_NodeTransform.localRotation = rotate;
        }
    }
    public Quaternion GetLocalRotate()
    {
        return m_NodeTransform.localRotation;
    }

    public virtual void SetWorldPosition(Vector3 pos)
    {
        if (m_NodeTransform != null)
            m_NodeTransform.position = pos;
    }
    public Vector3 GetWorldPosition()
    {
        return m_NodeTransform.position;
    }
    public virtual void SetWorldRotate(Quaternion rotate)
    {
        if (m_NodeTransform != null)
            m_NodeTransform.rotation = rotate;
    }
    public Quaternion GetWorldRotate()
    {
        return m_NodeTransform.rotation;
    }

    public virtual void SetScale(Vector3 scale)
    {
        if (m_NodeTransform != null)
            m_NodeTransform.localScale = scale;
    }
    public Vector3 GetScale()
    {
        return m_NodeTransform.localScale;
    }
    /// <summary>
    /// 获取变换对象 高级功能使用 一般情况下使用上面的各种变换方法
    /// </summary>
    /// <returns></returns>
    public Transform GetTransForm()
    {
        if (m_Node == null)
        {
            return null;
        }

        if (m_NodeTransform == null)
        {
            m_NodeTransform = m_Node.transform;
        }
        return m_NodeTransform;
    }
}
