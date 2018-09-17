//
//  YiJieOnlineHelper.h
//  YiJieOnlineHelper
//
//  Created by 雪鲤鱼 on 15/6/30.
//  Copyright (c) 2015年 YiJie. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface YiJieOnlineUser : NSObject
@property (retain)  NSString* id;
@property (retain)  NSString* channelId;
@property (retain)  NSString* channelUserId;
@property (retain)  NSString* userName;
@property (retain)  NSString* productCode;
@property (retain)  NSString* token;
@end

@protocol YiJieOnlineInitDelegate <NSObject>
-(void) onResponse:(NSString*) tag : (NSString*) value;
@end

@protocol YiJieOnlinePayResultDelegate <NSObject>
@required
-(void) onFailed : (NSString*) msg;
-(void) onSuccess : (NSString*) msg;
-(void) onOderNo : (NSString*) msg;
@end

@protocol YiJieOnlineLoginDelegate <NSObject>
@required
-(void) onLogout : (NSString *) remain;
-(void) onLoginSuccess : (YiJieOnlineUser*) user : (NSString *) remain;
-(void) onLoginFailed : (NSString*) reason : (NSString *) remain;
@end

@protocol YiJieOnlineExitDelegate <NSObject>
@required
-(void) onNoExiterProvide;
-(void) onSDKExit : (BOOL) isExit;
@end
@protocol YiJieOnlineExtendDelegate <NSObject>
-(void) onExtendResponse:(NSString*) tag : (NSString*) value;
@end

@interface YiJieOnlineHelper : NSObject
+(void) initSDKWithListener : (id)initListener;
+(void) setLoginListener : (id)loginListener;
+(void) exit : (id) exitListener;
+(void) login : (NSString*) remain;
+(void) logout : (NSString*) remain;
+(void) charge : (NSString*) itemName : (int32_t) unitPrice : (int32_t) count : (NSString*) callBackInfo : (NSString*) callBackUrl : (id) payResultListener;
/**支付接口
 * @param unitPrice     定额支付总金额，单位人民币分
 * @param unitName      虚拟货币名称
 * @param count         购买虚拟货币数量
 * @param callBackInfo  自定义字符串参数。与支付结果一同发送给游戏服务器。
 * @param callBackUrl   该支付结果通知给游戏服务器时的通知URL地址。
 * @param payResultListener
 */
+(void) pay : (int32_t) unitPrice : (NSString*) unitName : (int32_t) count : (NSString*) callBackInfo : (NSString*) callBackUrl : (id) payResultListener;
/*设置角色使用json格式*/
+(void) setRoleData : (NSString*) roleData;
+(void) payExtend : (int32_t) unitPrice : (NSString *) unitName : (NSString*) itemCode : (NSString *) remain : (int32_t) count : (NSString*) callBackInfo : (NSString*) callBackUrl : (id) payResultListener;
+(void) setData : (NSString*) key : (NSString*) value;
+(NSString*) extend : (NSString*) data : (int32_t) count ;
+(void) setExtendCallback : (id)extendCallback;
+(BOOL) isMusicEnable;
@end

@interface YJAppDelegae : NSObject<UIApplicationDelegate>
@property id<UIApplicationDelegate> sdkDelegate;
+(YJAppDelegae*) Instance;
@end

/*YIJIE ROLE DATA KEYS*/
const extern NSString* kYJRoleDataRoleId;
const extern NSString* kYJRoleDataRoleName;
const extern NSString* kYJRoleDataRoleLevel;
const extern NSString* kYJRoleDataZoneId;
const extern NSString* kYJRoleDataZoneName;
