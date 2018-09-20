#define ZHONGFU
#define XJCA

// #define YUNYOU
// #define DTXMZ
// #define DTXX
// #define XMCQ

//#define DONGHAI

#ifdef DONGHAI
#import "FbFunView.h"
#import <DHSDK/DHSDK.h>
#import "NSObject+MD5.h"
#endif

#ifdef YUNYOU
#import <aa_GameSDK/AAAGameManager.h>
#endif

#ifdef ZHONGFU
#ifdef XJCA
#import <SFUnion_WB/SFUnion.h>
#endif
#endif


#import "SdkDelegate.h"

#if defined(__cplusplus)
extern "C"{
#endif
    extern void UnitySendMessage(const char *, const char *, const char *);
    extern NSString* ToNSString (const char* string);
#if defined(__cplusplus)
}
#endif
 
//*****************************************************************************
 
@implementation SdkDelegate
//**********************
//message tools
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
//**********************
//SDK fun
- (void)registeredMethForCallBack{
#ifdef DONGHAI
    //登陆成功 -回调
    [SDHSDK setLoginCallBack:^(DHUser *user, DHLSS lSS) {
        NSString *userId    = user.userId;
        NSString *userName  = user.username;
        NSString *accessToken = user.accessToken;
        NSLog(@"userId      -- %@", userId);
        NSLog(@"userName    -- %@", userName);
        NSLog(@"accessToken -- %@", accessToken);

        [self testMeth:accessToken userId:userId userName:userName];
    }];
    //注销账号 - 回调
    [SDHSDK setLogoutCallBack:^{
        //浮动按钮中有个 切换账号，
        //通过这个方法 初始化游戏,切换到登陆界面等操作
        [SdkDelegate sendU3dMessage:@"IOSCallLogoutCallBack" param:nil];
    }];
    //IAP支付  - 回调
    [SDHSDK setDhInfoCallBack:^(DHPInfoType pType) {
        if(pType == DHZCreateOrderFail)
        {
            NSDictionary *dict1=@{@"pType":@1};
            [SdkDelegate sendU3dMessage:@"IOSCallPayResultCallBack" param:dict1]; //创建订单失败
        }else if(pType == DHZDoesNotExistProduct){
            NSDictionary *dict2=@{@"pType":@2};
            [SdkDelegate sendU3dMessage:@"IOSCallPayResultCallBack" param:dict2]; //商品信息不存在
        }else if(pType == DHZUnknowFail){
            NSDictionary *dict3=@{@"pType":@3};
            [SdkDelegate sendU3dMessage:@"IOSCallPayResultCallBack" param:dict3]; //未知错误
        }else if(pType == DHZVerifyReceiptSucceed){
            NSDictionary *dict4=@{@"pType":@4};
            [SdkDelegate sendU3dMessage:@"IOSCallPayResultCallBack" param:dict4]; //验证成功
        }else if(pType == DHZVerifyReceiptFail){
            NSDictionary *dict5=@{@"pType":@5};
            [SdkDelegate sendU3dMessage:@"IOSCallPayResultCallBack" param:dict5]; //验证失败
        }else if(pType == DHZURLFail){
            NSDictionary *dict6=@{@"pType":@6};
            [SdkDelegate sendU3dMessage:@"IOSCallPayResultCallBack" param:dict6]; //未能连接苹果商店
        }else{
            NSDictionary *dict=@{@"pType":@1};
            [SdkDelegate sendU3dMessage:@"IOSCallPayResultCallBack" param:dict];
        }
    }];
    //API支付页面关闭 - 回调
    [SDHSDK setDhColseBack:^{
        [SdkDelegate sendU3dMessage:@"IOSCallPayClosedCallBack" param:nil];
    }];
#endif
    
    
}

#ifdef DONGHAI

    + (NSString *)dictionaryToString:(NSDictionary *)parameters
    {
        NSString *postString = [NSString new];
        
        NSString *strCom = [NSString new];
        
        NSArray *keyArray = [parameters allKeys];
        NSString *strName = nil;
        NSString *strValue = nil;
        for (int i = 0; i < keyArray.count; i ++) {
            
            strName = keyArray[i];
            strValue = parameters[keyArray[i]];
            strValue = (NSString *)CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes(NULL,
                                                                                             (CFStringRef)strValue,
                                                                                             NULL,
                                                                                             (CFStringRef)@"!*'();:@&=+$,/?%#[]",
                                                                                             kCFStringEncodingUTF8));
            if (i == keyArray.count - 1) {
                strCom = [NSString stringWithFormat:@"%@=%@",strName,strValue];
            } else {
                strCom = [NSString stringWithFormat:@"%@=%@&",strName,strValue];
            }
            
            postString = [postString stringByAppendingString:strCom];
        }
        
        return postString;
    }

    - (void)testMeth:(NSString *)accessToken userId:(NSString *)userId userName:(NSString *)userName {
        
        NSString *gameId = @"86";
        NSString *subGameId = @"149";
        NSString *apiKey = @"b04781b991d8352c50aa5f399fbfabe2";
        
        NSString *url = [NSString stringWithFormat:@"https://api.sdk.dhios.cn/open/verifyAccessToken"];
        NSString *sign = [NSString stringWithFormat:@"%@=%@%@=%@%@=%@%@",@"accessToken",accessToken,@"gameId",gameId,@"subGameId",subGameId,apiKey];
        NSLog(@"sign:%@",sign);
        NSString *md5Str = [NSString md5:sign];
        NSDictionary *dic = @{@"accessToken":accessToken,
                              @"gameId":gameId,
                              @"subGameId":subGameId,
                              @"sign": md5Str
                              };
        NSString *parametersString =  [SdkDelegate dictionaryToString:dic];
        NSLog(@"parametersString = %@ ",parametersString);
        
        NSURL *requestUrl = [NSURL URLWithString:[url stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding]];
        NSMutableURLRequest *request = [NSMutableURLRequest requestWithURL:requestUrl];
        
        [request setHTTPMethod:@"POST"];
        if (parametersString) {
            [request setHTTPBody:[parametersString dataUsingEncoding:NSUTF8StringEncoding]];
        }
        
        NSOperationQueue *queue = [NSOperationQueue mainQueue];
        [NSURLConnection sendAsynchronousRequest:request queue:queue completionHandler:^(NSURLResponse * _Nullable response, NSData * _Nullable data,NSError * _Nullable connectionError) {
            if (connectionError == nil) {
                
                NSDictionary *responseObject = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:nil];
                NSLog(@"请求结果%@,",responseObject);
                
                NSDictionary *dict=@{@"gid":@86,
                                     @"subid":@149,
                                     @"code":[responseObject objectForKey:@"code"],
                                     @"userId":userId,
                                     @"userName":userName};
                
                [SdkDelegate sendU3dMessage:@"IOSCallLoginCallBack" param:dict];
            }
            else
            {
                
            }
        }];
    }
#endif

@end

//****************************************************************************

#if defined(__cplusplus)
extern "C"{
#endif
    //字符串转化的工具函数
    NSString* ToNSString (const char* string)
    {
        return [NSString stringWithUTF8String:(string ? string : "")];
    }
    static SdkDelegate *sdkDelegateIns;
 //********************************供u3d调用的c函数********************************
    
    // sdk 平台初始
    void _PlatformInit()
    {
        if(sdkDelegateIns==NULL){
            sdkDelegateIns = [[SdkDelegate alloc]init];
        }
        NSLog(@"平台初始");
        [sdkDelegateIns registeredMethForCallBack];
#ifdef DONGHAI
        [SDHSDK initWithGameId:86
                subGameId:149
                apiKey:@"b04781b991d8352c50aa5f399fbfabe2"
                success:^{
                   NSLog(@"初始化成功 ==> 加载游戏程序");
                }
                failure:^(int errcode, NSString *errorMessage) {
                   NSLog(@"初始化失败");
        }];
#endif
        
#ifdef YUNYOU
#ifdef DTXMZ
        [[AAAGameManager shareManager] txy_initGameKey:@"NTEyMDUxODM3"];
#endif
#ifdef DTXX
        [[AAAGameManager shareManager] txy_initGameKey:@"MzkzNDQ3MzAw"];
#endif
#ifdef XMCQ
        [[AAAGameManager shareManager] txy_initGameKey:@"MjA3NzAwNTky"];
#endif
#endif
        
    }

    //登录初始
    void _Login()
    {
        NSLog(@"登录初始");
#ifdef DONGHAI
        [SDHSDK login];
#endif
        
#ifdef YUNYOU
        [[AAAGameManager shareManager] txy_loginViewShowAndBlock:^(NSString *uid, NSString *token) {
            NSLog(@"---%@--%@",uid,token);
            NSDictionary *dict=@{@"userName":uid,@"token":token};
            [SdkDelegate sendU3dMessage:@"IOSCallLoginCallBack" param:dict];
        }];
#endif
        
        
    }
    
    //用户中心初始
    void _UserCenter()
    {
#ifdef DONGHAI
        [SDHSDK userCenter];
#endif
        
    }
    // 注销初始
    void _LogoutAccount()
    {
#ifdef DONGHAI
        [SDHSDK logoutAccount];
#endif
      
#ifdef YUNYOU
    [[AAAGameManager shareManager] txy_quitUserAccount:^(NSString *message) {
        NSLog(@"退出账号");
        [SdkDelegate sendU3dMessage:@"IOSCallLogoutCallBack" param:nil];
    }];
#endif 

       
    }

    // 开始购买商品 _Pay(svrId, rId, rName, cpOrderId, pdId, pdName, pdDesc, total, desc);
    void _Pay(const char* svrId, const char* rId, const char* rName, const char* cpOrderId,
                const char* pdId, const char* pdName, const char* pdDesc, int total, const char* desc)
    {
#ifdef DONGHAI
        DHOrder *order = [DHOrder new];
        order.serverId =ToNSString(svrId);
        order.roleId = ToNSString(rId);
        order.roleName =ToNSString(rName);
        order.productName =ToNSString(pdName);
        order.customInfo =ToNSString(desc);
        order.cpOrderId = ToNSString(cpOrderId);//cp创建订单号
        order.productDescription = ToNSString(pdDesc);
        order.productId = ToNSString(pdId);
        order.totalFee = total; //分制
        [SDHSDK createOrder:order];
#endif
        
#ifdef YUNYOU
        [[AAAGameManager shareManager]txy_payWithGoodsName:ToNSString(pdName) 
                                            Price:[NSString stringWithFormat:@"%d", total]
                                            game_trade_no:ToNSString(cpOrderId)
                                            producetID:ToNSString(pdId)];
#endif
        
        
    }
    
    //在相应的位置-自行调用上传角色信息 _UploadRoleInfo(svrId, rId, svrName, rName, lv);
    void _UploadRoleInfo(const char* svrId, const char* rId, const char* svrName, const char* rName, int lv)
    {
#ifdef DONGHAI
        NSDate *date = [NSDate date];
        DHRole *role = [DHRole new];
        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        [formatter setDateStyle:NSDateFormatterMediumStyle];
        [formatter setTimeStyle:NSDateFormatterShortStyle];
        [formatter setDateFormat:@"YYYY-MM-dd hh:mm:ss"];
        NSString *dateTime = [formatter stringFromDate:date];
        [role setLoginTime:dateTime];
        [role setServerId:ToNSString(svrId)];
        [role setServerName:ToNSString(rId)];
        [role setRoleId:ToNSString(svrName)];
        [role setRoleName:ToNSString(rName)];
        [role setRoleLevel:lv];
        [SDHSDK reportRole:role];
#endif
  
    }
    
#if defined(__cplusplus)
}
#endif
