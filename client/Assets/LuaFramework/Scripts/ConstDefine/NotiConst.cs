using UnityEngine;
using System.Collections;

public class NotiConst
{
    /// <summary>
    /// Controller层消息通知
    /// </summary>
    public const string START_UP = "StartUp";                       //启动框架
    //public const string DISPATCH_MESSAGE = "DispatchMessage";       //派发信息

    /// <summary>
    /// View层消息通知
    /// </summary>
    //public const string UPDATE_EXTRACT = "UpdateExtract";           //更新解包
    //public const string UPDATE_DOWNLOAD = "UpdateDownload";         //更新下载
    //public const string UPDATE_PROGRESS = "UpdateProgress";         //更新进度


    // 加载显示进度UI （数据不是真实的）
    // 当前正在下载的"文件名" (随机进度值，如果还是当前文件名，再加上一个随机值作为进度)
    public const string LOADER_PROGRESS = "LOADER_PROGRESS";
    // 完成"文件名" 
    public const string LOADER_COMPLETED = "LOADER_COMPLETED";
    // 全部加载完成
    public const string LOADER_ALL_COMPLETED = "LOADER_ALL_COMPLETED";
}
