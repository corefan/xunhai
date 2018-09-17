//
//  YJSDKHelper.h
//  YJSDKHelper.h
//
//  Created by 雪鲤鱼 on 15/6/30.
//  Copyright (c) 2015年 YiJie. All rights reserved.


#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
@interface YJSDKHelperUser : NSObject
@property (retain)  NSString* channnelUserId;
@property (retain)  NSString* userName;
@property (retain)  NSString* token;
@end

/*支付信息*/
@interface YJSDKHelperPayInfo:NSObject
@property (retain)  NSString* orderId;
@property (retain)  NSString* productName;
@property   int32_t productPrice;
@property   int32_t productCount;
@property (retain)NSString* callbackURL;    // 回调地址
@property (retain)NSString* callbackInfo;   // 透传参数
@property NSString* productID;
@property(nonatomic) int orderType; // 交易类型  0 普通消费 1 充值货币  2 充值钱包
+(instancetype) instance;
@end

@protocol YJSDKHelperInitDelegate <NSObject>
@required
-(void) onResponse:(NSString*) tag : (NSString*) value;
@end

/*登录回调*/
@protocol YJSDKHelperLoginDelegate <NSObject>
@required
-(void) onYJLogout : (NSString *) remain;
-(void) onYJLoginSuccess : (YJSDKHelperUser*) user ;
-(void) onYJLoginFailed : (NSString*) reason;
@end

/*支付结果回调
 *result：“success”：成功 “failed”：失败
 * msg：失败原因
 */
@protocol YJSDKHelperPayResultDelegate <NSObject>
@required
-(void) onPayResponse : (NSString*) tag: (NSString*) value;
@end

/*防沉迷回调
 *resp：json字符串：code：0:成功 其它：失败码
                  msg：失败原因
  */
@protocol YJSDKHelperAntiDelegate <NSObject>
@required
-(void) onAntiResponse : (NSString*) resp;
@end

/*礼包激活码回调
 *resp：json字符串：result：0:成功 其它：失败码
 msg：失败原因
   */
@protocol YJSDKHelperGiftcodeDelegate <NSObject>
@required
-(void) onGiftcodeResponse : (NSString*) result;
@end

@interface YJSDKHelper : NSObject
+(YJSDKHelper*) Instance;
/*第三方跳转接口*/
- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation;
-(BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url;

/*初始化接口*/
+(void) initSDK :(id)initListener;
/*登录接口
 * @param payinfo     登录回调
 */
+(void) login : (id)loginListener;
/*登出接口*/
+(void) logout;
/*充值接口
 * @param payinfo     支付信息
 * @param payResultListener
 */
+(void) charge : (YJSDKHelperPayInfo*)payinfo: (id) payResultListener;
/**支付接口
 * @param payinfo     定额支付信息
 * @param payResultListener
 */
+(void) pay : (YJSDKHelperPayInfo*)payinfo : (id) payResultListener;

/**游戏开始接口
 * @param roleData     角色信息：json字符串
 * roleId: 角色id
 * roleName:角色名称
 * roleLevel:角色等级
 * zoneId:区服id
 * zoneName:区服名称
*/
+(void) gamestart : (id) roleData ;

/**创建角色接口
 * @param roleData     角色信息
 * roleId: 角色id
 * roleName:角色名称
 * roleLevel:角色等级
 * zoneId:区服id
 * zoneName:区服名称
 */
+(void) createrole : (id) roleData ;

/**角色升级接口
 * @param roleData     角色信息
 * roleId: 角色id
 * roleName:角色名称
 * roleLevel:角色等级
 * zoneId:区服id
 * zoneName:区服名称
 */

+(void) levelup : (id) roleData ;
/*实名注册接口*/
+(void) realnamereg;
/*防沉迷查询接口
 * @param antiaddictionDelegate    防沉迷查询回调
 */
+(void) antiaddictionQuery:(id) antiaddictionDelegate ;
/**激活礼包码接口
 * @param code     礼包码
 * @param giftcodeDelegate     激活回调
 */
+(void) checkgiftcode:(NSString*)code:(id) giftcodeDelegate ;



@end
