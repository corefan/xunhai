
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
//参数
///商品id
extern NSString * const JRNBtislrh;
extern NSString * const UoRiroNREK;///商品名字
extern NSString * const SzlJbHHfsd;///商品描述
extern NSString * const uVFukeZurL;///扩展参数，可选
extern NSString * const DGYYCNKuHl;///商品金额
extern NSString * const GLjmmELsJm; ///单号



///角色所在的serverid
extern NSString * const yuUCtoGImy;
///服务器名
extern NSString * const GeBKnjhIbu;
///角色id
extern NSString * const jKuijNluYv;
///角色名
extern NSString * const JgmNhkFGls;
///角色等级
extern NSString * const cMSrXLsnTJ;
///公会名
extern NSString * const VfvzGnJsnR;
///角色vip等级
extern NSString * const vGJrsoRHym;
///角色游戏余
extern NSString * const RZEtVzKlzz;
///数据类型，1为进入游戏，2为创建角色，3为角色升级，4为退出
extern NSString * const GEDlgmIhym;


//浮点可选参数
extern NSString * const DRKMHsojbM;//浮点初始化的中心y轴位置
extern NSString * const FYSRtUFNUZ;//浮点初始化在屏幕左边还是右边

///创建角色的时间 时间戳
extern NSString * const ftfVbsSuNF;
///角色等级变化时间 时间戳
extern NSString * const CjYMXBIHcF;
///货类型名称
extern NSString * const rkehZrjdZd;

typedef void (^GameMainThreadCallBack)(NSDictionary *responseDic);

@interface  tlgSBuIEzm: NSObject
///
+ (void)RhTtYbJTFK:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation;

///登出
+ (void)ozsJbiYLHt;

///显示登录界面
+ (void)ZKGuUHBDJE:(GameMainThreadCallBack)receiverBlock;

///登出回调接口
+(void)IcdCOSgojz:(GameMainThreadCallBack)receiverBlock;

///向服务器发送信息
+(void)RIjejdRTcH:(NSDictionary*)info failedBlock:(void(^)())failedBlock;

///发送信息成功回调
+(void)NDZFZcOnMB:(GameMainThreadCallBack)receiverBlock;

///设置角色信息
+(void)UUzSsuImih:(NSDictionary*)roleInfo;

///设置自动登录
+(void)TZSCGYImll:(BOOL)isAutoLogin;

+ (void)FSogBDmNjo:(NSDictionary *)floatViewInfo;

//如果在某些场景有必要隐藏浮点，可以调用这个方法。
+ (void)cDXFosOCCd;

@end
