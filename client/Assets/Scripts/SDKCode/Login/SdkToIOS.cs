using UnityEngine;
using System.Runtime.InteropServices;
public class SdkToIOS : MonoSingleton<SdkToIOS>
{
    //平台接入开关
    public static bool isOpenPlatform = false;

    //导入定义到.m文件中的C函数
    [DllImport("__Internal")]
    private static extern void _PlatformInit();
    [DllImport("__Internal")]
    private static extern void _Login();
    [DllImport("__Internal")]
    private static extern void _UserCenter();
    [DllImport("__Internal")]
    private static extern void _LogoutAccount();
    [DllImport("__Internal")]
    private static extern void  _Pay(string svrId, string rId, string rName, string cpOrderId,
                                        string pdId, string pdName, string pdDesc, int total, string desc);
    [DllImport("__Internal")]
    private static extern void  _UploadRoleInfo(string svrId, string rId, string svrName, string rName, int lv);

    #region me call ios
    //定义接口函数供游戏逻辑调用
    public static void InitSDK()
    {
#if (UNITY_IPHONE || UNITY_IOS) && !UNITY_EDITOR
        isOpenPlatform = true;
#endif
        if (isOpenPlatform)
        {
            _PlatformInit();
        }
    }
    public void OpenLogin()
    {
       if (isOpenPlatform)
       {
           _Login();
       }
    }
    public void OpenUserCenter()
    {
        _UserCenter();
    }
    public void OpenLogoutAccount()
    {
        _LogoutAccount();
    }
    //svr:server, r:role, pd:product
    public void OpenPay(string svrId, string rId, string rName, string cpOrderId,
                        string pdId, string pdName="", string pdDesc="", int total=0, string desc=""){
        _Pay(svrId, rId, rName, cpOrderId, pdId, pdName, pdDesc, total, desc);
    }
    public void UploadRoleInfo(string svrId, string rId, string svrName="", string rName="", int lv=0){
        _UploadRoleInfo(svrId, rId, svrName, rName, lv);
    }
    #endregion

    void Start()
    {
        InitSDK();
    }

    #region ios call me
    public void IOSCallLoginCallBack(string param)
    {
        Debug.Log(">>>>>>>>>>>>IOSCallLoginCallBack "+param);
        GlobalDispatcher.GetInstance().DispatchEvent("IOSCallLoginCallBack", param);
    }
    public void IOSCallLogoutCallBack(string param)
    {
        Debug.Log(">>>>>>>>>>>>IOSCallLogoutCallBack "+param);
        GlobalDispatcher.GetInstance().DispatchEvent("IOSCallLogoutCallBack", param);
    }
    public void IOSCallPayResultCallBack(string param)
    {
        Debug.Log(">>>>>>>>>>>>IOSCallPayResultCallBack "+param);
        GlobalDispatcher.GetInstance().DispatchEvent("IOSCallPayResultCallBack", param);
    }
    public void IOSCallPayClosedCallBack(string param)
    {
        Debug.Log(">>>>>>>>>>>>IOSCallPayClosedCallBack "+param);
        GlobalDispatcher.GetInstance().DispatchEvent("IOSCallPayClosedCallBack", param);
    }
    #endregion
}

