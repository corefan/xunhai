//
//  InAppPurchaseHelper.h
//  Unity-iPhone
//
//  Created by Ekko on 11/3/17.
//
//


#ifndef InAppPurchaseHelper_h
#define InAppPurchaseHelper_h

#import <StoreKit/StoreKit.h>

//沙盒测试环境验证
#define SANDBOX @"https://sandbox.itunes.apple.com/verifyReceipt"
//正式环境验证
#define AppStore @"https://buy.itunes.apple.com/verifyReceipt"


@interface IAPHelper : NSObject <SKProductsRequestDelegate, SKPaymentTransactionObserver>
{
    NSMutableArray *mProductIdsToRestore;

    NSString *mCallBackObjectName;
    NSString *mGameServerAddress;
}

@property (nonatomic, retain) NSString *mPayInfo;

// 单例
+ (IAPHelper*) Instance;

// 初始化 设置回调的对象
- (void)initIAPHelper:(NSString*)callBackName gameServerAddress:(NSString*)gameServerAddress;

// 开始购买
- (void)startBuyProduct:(NSString*)productId username:(NSString*)username;

// 是否允许内购
- (BOOL) canMakePay;

// 通过product ID 获取详细信息
- (void) getProductInfoById:(NSString*)productID;

// 支付成功
- (void) completeTransaction:(SKPaymentTransaction*)transaction;

// 支付失败
- (void) failedTransaction:(SKPaymentTransaction*)transaction;

// 对于已购商品，处理恢复购买的逻辑
- (void) restoreTransaction:(SKPaymentTransaction*)transaction;

// 二次验证订单
- (void) verifyPurchaseWithPaymentTransaction:(SKPaymentTransaction*)transaction restore:(BOOL)restore;


// 发送收据到游戏服务器
- (void) sendReceiptToGameServer:(NSData*)receiptData transaction:(SKPaymentTransaction*)transaction;

-(void) restoreTransactions:(NSMutableArray*)prodcutIdentifier username:(NSString*)username;


- (void)paymentQueueRestoreCompletedTransactionsFinished:(SKPaymentQueue *)queue;

- (void)paymentQueue:(SKPaymentQueue *)queue restoreCompletedTransactionsFailedWithError:(NSError *)error;

@end


#endif /* InAppPurchaseHelper_h */
