
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
//参数
///商品id
extern NSString * const USkZsekgme;
extern NSString * const rtNicglCrf;///商品名字
extern NSString * const hhzsYIlOBm;///商品描述
extern NSString * const bilvnlceZT;///扩展参数，可选
extern NSString * const sOTFBCkoiT;///商品金额
extern NSString * const BechjhkBvy; ///单号



///角色所在的serverid
extern NSString * const YzdykEoMfg;
///服务器名
extern NSString * const JrfhsJhkbM;
///角色id
extern NSString * const TsJluslLzl;
///角色名
extern NSString * const rXKXrFUIHy;
///角色等级
extern NSString * const HhmnYzrlJX;
///公会名
extern NSString * const rJHmcGlVve;
///角色vip等级
extern NSString * const SMbCMJbRyV;
///角色游戏余
extern NSString * const CDjCcFrILH;
///数据类型，1为进入游戏，2为创建角色，3为角色升级，4为退出
extern NSString * const omILmBFYVY;


//浮点可选参数
extern NSString * const MctYlJdOBv;//浮点初始化的中心y轴位置
extern NSString * const SsIvokfVNl;//浮点初始化在屏幕左边还是右边

///创建角色的时间 时间戳
extern NSString * const NizjGKLYOs;
///角色等级变化时间 时间戳
extern NSString * const XehORFKYti;
///货类型名称
extern NSString * const oRDrsLzvOD;

typedef void (^GameMainThreadCallBack)(NSDictionary *responseDic);

@interface  LJkCbmoIVV: NSObject
///
+ (void)GFNEuZGCcl:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation;

///登出
+ (void)sbjomJDLgo;

///显示登录界面
+ (void)tzbRgsFmjX:(GameMainThreadCallBack)receiverBlock;

///登出回调接口
+(void)SCfhghCFXe:(GameMainThreadCallBack)receiverBlock;

///向服务器发送信息
+(void)OyLsUhLdvs:(NSDictionary*)info failedBlock:(void(^)())failedBlock;

///发送信息成功回调
+(void)IsdDolKtXt:(GameMainThreadCallBack)receiverBlock;

///设置角色信息
+(void)VlNbnjCSBT:(NSDictionary*)roleInfo;

///设置自动登录
+(void)SOKRYyuLyB:(BOOL)isAutoLogin;

+ (void)GyXGyzzXmS:(NSDictionary *)floatViewInfo;

//如果在某些场景有必要隐藏浮点，可以调用这个方法。
+ (void)yDBMnsoVum;

@end
