
#import <UIKit/UIKit.h>
#import "MYGame_Confuse.h"
//------------------------------define begin---------------------------------------------
//初始化
typedef enum {
    HAXINITSuccess                 = 2000,    //初始化成功
    HAXINITServerError             = 6001,    //服务器正在维护
    HAXINITNetError                = 7,       //网络异常
    HAXINITTimeOut                = 8,       //time out
    
    HAXINITOtherError              = 0,       //其他错误
    
} HAX_INIT_ERROR;

//login
typedef enum
{
    HAXLOGINFail                 = 0,
    HAXLOGINSuccess              = 1,
} HAX_Login_Result;

typedef enum {
    HAXTreatedOrderSuccess         = 1,        //成功
    HAXTreatedOrderFail            = 2,        //失败
    HAXTreatedOrdering             = 3,        //正在进行中
    HAXTreatedOrderCancel          = 4,        //取消
    HAXTreatedOrderCloseMenu       = 5,        //关闭界面
    HAXTreatedOrderNoModeList      = 6,        //服务端信息列表为空
    HAXTreatedOrderNetError        = 7,        //网络异常
    HAXHtmlError = 8,
    
    HAXTreatedOrderInvalidProduct  = 11,        //Apple，苹果后台没有对应的产品ID或产品ID无效
    HAXTreatedOrderProductIdMiss  = 12,        //Apple，苹果后台没有对应的产品ID或产品ID无效
    HAXTreatedOrderReceiptFail     = 13,        //Apple，凭证验证失败或者没有通知到game服务器
    HAXTreatedOrderUnKnow          = 14,        //结果未知
    HAXTreatedOrderOtherError      = 0,        //其他错误
    
} HAX_TreatedOrder_ERROR;

typedef void(^CompletionBlock)(NSDictionary *resultDic);
@class OrderInfo;
@interface HAXApi : NSObject
{
}

+ (HAXApi *) HaxInstance;
+ (NSString *) HAXVersion;
-(void) SetApiWithInfo:(NSString *)gameid
                    gai:(NSString *)gameappid
                  name:(NSString *)gamename
                    pi:(NSString *)promoteid
                    pa:(NSString *)promoteaccount
            completion:(CompletionBlock)completion;
-(void) AddLoginView:(CompletionBlock)completion;
-(void) Logout;
-(void) SetLogoutCallback:(CompletionBlock)exitCallback;

-(void) GoPurView:(OrderInfo *)orderInfo completionBlock:(CompletionBlock)completionBlock;
@end

@interface OrderInfo : NSObject
@property (nonatomic, assign) int goodsPrice;
@property (nonatomic, copy) NSString *goodsName;
@property (nonatomic, copy) NSString *goodsDesc;
@property (nonatomic, copy) NSString *productId;
@property (nonatomic, copy) NSString *extendInfo;
@property (nonatomic, copy) NSString *player_server;
@property (nonatomic, copy) NSString *player_role;
@property (nonatomic, copy) NSString *cp_trade_no;
@end
