
#region 说明
// 功  能： 层级管理器
// 描  述： 层级管理器
// 时  间： 2016-06-27 18:49:22
// 作  者： zwx
// E-mail： zhuang_wx@qq.com
// 项目名： SKGame
#endregion


using UnityEngine;
using System.Collections;
using FairyGUI;
using System;

/// <summary>
/// 层级标签
/// </summary>
public enum LayerTag
{
    UI=0,
    MSG,
    LOADER,
    TOP,
}

/// <summary>
/// [单例]层级管理器(所有UI层级都是放在 UIPanel 下的)
/// </summary>
public class LayersMgr:MonoSingleton<LayersMgr> {
    /// <summary>
    /// UI默认适配器大小
    /// </summary>
    public const int WIDTH = 1334;
    /// <summary>
    /// UI默认适配器大小
    /// </summary>
    public const int HEIGHT = 750;

    /// <summary>
    /// UIPanel UI容器
    /// </summary>
    public GameObject uiPanelGo;
    /// <summary>
    /// UI Content Scaler UI设配尺寸： designResolutionX、Y
    /// </summary>
    public UIContentScaler scaler;

    /// <summary>
    /// 内容缩放比
    /// </summary>
    public float contentScaleX = 1f;
    public float contentScaleY = 1f;

    /// <summary>
    /// FUI根部容器组件
    /// </summary>
    GComponent _root;

    /// <summary>
    /// 基本UI layer
    /// </summary>
    private GComponent uiLayer;  
    /// <summary>
    /// 消息、广播、滚屏-飘字UI层
    /// </summary>
    private GComponent msgLayer;
    /// <summary>
    /// 加载主游戏资源进度UI（注意采集条请放在UI layer中）
    /// </summary>
    private GComponent loaderLayer;
    /// <summary>
    /// 最顶层（这个预留在用于高级弹窗处理在高级显示的 -- 提示，目前弹出界面是用 GRoot.inst.ShowPopup(gcomp)）
    /// </summary>
    private GComponent topLayer;

    /// <summary>
    /// 初始化
    /// </summary>
    protected override void OnCreate()
    {
        if (uiPanelGo == null)
        {
            uiPanelGo = new GameObject("UIPanel");
            uiPanelGo.layer = LayerMask.NameToLayer(StageCamera.LayerName);
            DontDestroyOnLoad(uiPanelGo);
            //GRoot.inst.SetContentScaleFactor(WIDTH, HEIGHT);

            scaler = uiPanelGo.AddComponent<UIContentScaler>();
            scaler.designResolutionX = WIDTH;
            scaler.designResolutionY = HEIGHT;
            scaler.scaleMode = UIContentScaler.ScaleMode.ScaleWithScreenSize; // ConstantPixelSize;// 

            //在 ScaleMode.ScaleWithScreenSize 时使用--------------------------------------------------------
            int dx = scaler.designResolutionX;
            int dy = scaler.designResolutionY;
            if (Screen.width > Screen.height && dx < dy || Screen.width < Screen.height && dx > dy)
            {
                int tmp = dx;
                dx = dy;
                dy = tmp;
            }
            contentScaleX = (float)Screen.width / dx;
            contentScaleY = (float)Screen.height / dy;
            //---------------------------------------------------------------------------------------------

            UIPanel panel = uiPanelGo.AddComponent<UIPanel>();
            panel.fitScreen = FitScreen.FitSize;
        }
    }

    /// <summary>
    /// 初始化所有层级组件容器
    /// </summary>
    void InitLayers(GComponent root)
    {
        if (_root == null) { 
            root.AddChild(GetUILayer());
            root.AddChild(GetMSGLayer());
            root.AddChild(GetLoaderLayer());
            root.AddChild(GetTopLayer());
        }
        _root = root;
    }
    /// 重启时更新所有UI
    public void ResetAllLayer(){
        RemoveAllLayers();
        if(_root != null){
            _root.AddChild(GetUILayer());
            _root.AddChild(GetMSGLayer());
            _root.AddChild(GetLoaderLayer());
            _root.AddChild(GetTopLayer());
        }
    }

    /// <summary>
    /// FUI构建(注：每次UIPanel会毁掉之前的UI内容)
    /// </summary>
    public bool CreateFUI(string packageName, string componentName)
    {
        if(_root != null)
        {
            Debug.LogWarning("UI引擎层级已经初始化了，再次初始化无效！");
            return false;
        }
        UIPanel panel = uiPanelGo.GetComponent<UIPanel>();
        panel.packageName = packageName;
        panel.componentName = componentName;
        panel.CreateUI();
        InitLayers(panel.ui);
        return true;
    }
    //批量设置一个root下面所有自物体的layer
    public void SetTransformChildLayer(Transform root,string layerName)
    {
        foreach (Transform child in root)
        {
            child.gameObject.layer = LayerMask.NameToLayer(layerName);
        }
    }
    #region get layer
    /// <summary>
    /// 获得指定层级
    /// </summary>
    public GComponent GetLayer(LayerTag tag=LayerTag.UI)
    {
        if (tag == LayerTag.UI)
        {
            return GetUILayer();
        }
        else if (tag == LayerTag.MSG)
        {
            return GetMSGLayer();
        }
        else if (tag == LayerTag.LOADER)
        {
            return GetLoaderLayer();
        }
        else
        {
            return GetTopLayer();
        }
    }

    /// <summary>
    /// 获得UI层
    /// </summary>
    public GComponent GetUILayer()
    {
        if (uiLayer == null)
        {
            uiLayer = CreateLayer();
        }
        return uiLayer;
    }

    /// <summary>
    /// 消息、广播、滚屏-飘字UI层
    /// </summary>
    public GComponent GetMSGLayer()
    {
        if (msgLayer == null)
        {
            msgLayer = CreateLayer();
        }
        return msgLayer;
    }

    /// <summary>
    /// 加载主游戏资源进度UI（注意采集条请放在UI layer中）
    /// </summary>
    public GComponent GetLoaderLayer()
    {
        if (loaderLayer == null)
        {
            loaderLayer = CreateLayer();
        }
        return loaderLayer;
    }

    /// <summary>
    /// 最顶层（这个预留在用于高级弹窗处理在高级显示的）
    /// </summary>
    public GComponent GetTopLayer()
    {
        if (topLayer == null)
        {
            topLayer = CreateLayer();
        }
        return topLayer;
    }

    private GComponent CreateLayer()
    {
        GComponent layer = new GComponent();
        layer.opaque = false;
        layer.fairyBatching = true;
        if (contentScaleX != 1 || contentScaleY != 1)
            layer.SetScale(contentScaleX, contentScaleY);
        return layer;
    }

    #endregion

    #region uicomponent add to layer
    /// <summary>
    /// 将指定的UI 添加到相应的显示层级容器
    /// </summary>
    public GComponent AddToLayer(GComponent ui, LayerTag tag = LayerTag.UI)
    {
        if (ui != null)
        {
            GetLayer(tag).AddChild(ui);
        }
        return ui;
    }

    /// <summary>
    /// 将指定的UI 添加到相应的显示层级容器
    /// </summary>
    public GComponent AddToLayer(string pkgName, string resName, LayerTag tag = LayerTag.UI)
    {
         GComponent ui = UIPackage.CreateObject(pkgName, resName).asCom;
        return AddToLayer(ui, tag);
    }

    /// <summary>
    /// 将指定的UI 添加到相应的显示层级容器
    /// </summary>
    public GComponent AddToLayer(string url, LayerTag tag = LayerTag.UI)
    {
        GComponent ui = UIPackage.CreateObjectFromURL(url).asCom;
        return AddToLayer(ui, tag);
    }

    /// <summary>
    /// 添加到UI层
    /// </summary>
    public GComponent AddToUILayer(GComponent ui)
    {
        return AddToLayer(ui, LayerTag.UI);
    }

    #endregion

    #region clear or dispose
    public void Dispose()
    {
        RemoveAllLayers();
        if (_root != null)
        {
            _root.Dispose();
        }
        _root = null;
    }

    public void RemoveAllLayers()
    {
        RemoveLayer(uiLayer);
        uiLayer = null;
        RemoveLayer(msgLayer);
        msgLayer = null;
        RemoveLayer(loaderLayer);
        loaderLayer = null;
        RemoveLayer(topLayer);
        topLayer = null;
    }

    public void RemoveLayer(GComponent layer)
    {
        if (layer != null)
        {
            if (layer.parent != null)
                layer.RemoveFromParent();
            layer.Dispose();
        }
    }
    #endregion

}

