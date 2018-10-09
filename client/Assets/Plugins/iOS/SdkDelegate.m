#import "SdkDelegate.h"

//@@@@@ 导入平台sdk头文件 #import<sdk/ccxccc.h>


//*****************************************************************************

@implementation SdkDelegate
#if defined(__cplusplus)
extern "C"{
#endif
    extern void UnitySendMessage(const char *, const char *, const char *);
    extern NSString* ToNSString (const char* string);
    NSString* ToNSString (const char* string)
    {
        return [NSString stringWithUTF8String:(string ? string : "")];
    }
#if defined(__cplusplus)
}
#endif
//发送给u3d消息接口
+ (void)sendU3dMessage:(NSString *)messageName param:(NSDictionary *)dict
{
    NSString *param = @"";
    if ( nil != dict ) {
        for (NSString *key in dict)
        {
            if ([param length] == 0)
            {
                param = [param stringByAppendingFormat:@"%@=%@", key, [dict valueForKey:key]];
            }
            else
            {
                param = [param stringByAppendingFormat:@"&%@=%@", key, [dict valueForKey:key]];
            }
        }
    }
    UnitySendMessage("GameManager", [messageName UTF8String], [param UTF8String]);
}

//初始化注册回调
- (void)registeredMethForCallBack{
    
    //@@@@@ 注册回调 NSDictionary *dict=@{@"xxx":x,@"xxx":x};  [SdkDelegate sendU3dMessage:@"IOSCallLoginCallBack" param:dict];
    
}

@end

//****************************************************************************
#if defined(__cplusplus)
extern "C"{
#endif
    static SdkDelegate *sdkDelegateIns;
    //********************************供u3d调用的c函数********************************
    
    // sdk 平台初始
    void _PlatformInit()
    {
        NSLog(@"平台初始");
        if(sdkDelegateIns==NULL){
            sdkDelegateIns = [[SdkDelegate alloc]init];
        }
        [sdkDelegateIns registeredMethForCallBack];//注册回调
        
        //@@@@@@@ 平台初始
        
    }
    
    //登录初始
    void _Login()
    {
        NSLog(@"登录初始");
        //@@@@@@@ 登录初始 NSDictionary *dict=@{@"xxx":x,@"xxx":x};  [SdkDelegate sendU3dMessage:@"IOSCallLoginCallBack" param:dict];
    }
    
    //用户中心初始
    void _UserCenter()
    {
        NSLog(@"用户中心");
        //@@@@@@@ 用户中心
    }
    // 注销初始
    void _LogoutAccount()
    {
        NSLog(@"退出账号");
        //@@@@@@@ 退出账号
    }
    
    // 开始购买商品 _Pay(svrId, rId, rName, cpOrderId, pdId, pdName, pdDesc, total, desc);
    void _Pay(const char* svrId, const char* rId, const char* rName, const char* cpOrderId,
              const char* pdId, const char* pdName, const char* pdDesc, int total, const char* desc)
    {
        NSLog(@"充值信息：%@，%@，%@，%@，%@，%@，%@，%@，%@",
              ToNSString(svrId),ToNSString(rId),ToNSString(rName),
              ToNSString(cpOrderId),ToNSString(pdId),ToNSString(pdName),
              ToNSString(pdDesc),[NSString stringWithFormat:@"%d", total],ToNSString(desc));
        
        //@@@@@@@@@@@ 充值信息
        
    }
    
    //上传信息 服务器ID svrId, 角色id rId, 服务器名 svrName, 角色名 rName,等级 lv, 上传类型 state 0创建角色 1登录游戏 2角色升级
    //注：老的项目要去掉 state _UploadRoleInfo(const char* svrId, const char* rId, const char* svrName, const char* rName, int lv)
    void _UploadRoleInfo(const char* svrId, const char* rId, const char* svrName, const char* rName, int lv, int state)
    {
        //@@@@@@@@@@@ 上传角色信息
    }
    
#if defined(__cplusplus)
}
#endif