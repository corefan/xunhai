//
//  IAPHelper.mm
//  Unity-iPhone
//
//  Created by Ekko on 11/3/17.
//
//

#include <iostream>
#import <CommonCrypto/CommonCrypto.h>

#import "IAPHelper.h"

#if defined(__cplusplus)
extern "C" {
#endif
    extern void       UnitySendMessage(const char* obj, const char* method, const char* msg);
    extern NSString*  ToNSString (const char* string);
#if defined(__cplusplus)
}
#endif


static IAPHelper* instance = nil;

@implementation IAPHelper

//使用同步创建 保证多线程下也只有一个实例
+ (IAPHelper *)Instance
{
    @synchronized(self)
    {
        if (instance == nil)
        {
            instance = [[IAPHelper alloc] init];
        }
    }
    return instance;
}

- (id)init
{
    self = [super init];
    
    if (self)
    {
        // 监听购买结果
        [[SKPaymentQueue defaultQueue] addTransactionObserver:self];
    }
    return self;
}

- (void) dealloc
{
    [[SKPaymentQueue defaultQueue] removeTransactionObserver:self];
    
    mCallBackObjectName = nil;
}

//初始化 设置回调的对象
- (void)initIAPHelper:(NSString*)callBackName gameServerAddress:(NSString*)gameServerAddress
{
    mCallBackObjectName = callBackName;
    mGameServerAddress = gameServerAddress;

}

//是否有购买权限
- (BOOL)canMakePay
{
    return [SKPaymentQueue canMakePayments];
}

// 开始购买商品
- (void)startBuyProduct:(NSString*)productId payInfo:(NSString*)payInfo
{
     self.mPayInfo = payInfo;
    
    if (self.canMakePay)
    {
        NSLog(@"获取ID--->>getProductInfoById: %@", productId);

        [self getProductInfoById:productId];
    }
    else
    {
        NSLog(@"not permission!不允许内购");
        UnitySendMessage(mCallBackObjectName.UTF8String, "PreBuyProductFailed", "NoPermission");
    }
}

// 下面的ProductId应该是事先在itunesConnect中添加好的，已存在的付费项目。否则查询会失败。
- (void)getProductInfoById:(NSString*)productID
{
    NSLog(@"get productId: %@", productID);
    
    NSArray *product = nil;
    product = [[NSArray alloc] initWithObjects:productID, nil];
    
    NSSet *set = [NSSet setWithArray:product];
    SKProductsRequest *request = [[SKProductsRequest alloc] initWithProductIdentifiers:set];
    
    //设置并启动监听
    request.delegate = self;
    [request start];
}


- (void)productsRequest:(SKProductsRequest *)request didReceiveResponse:(SKProductsResponse *)response
{
    NSArray *myProduct = response.products;
    if (myProduct.count == 0)
    {
        UnitySendMessage(mCallBackObjectName.UTF8String, "PreBuyProductFailed", "ProductNotExist");
        
        return;
    }
    
    UnitySendMessage(mCallBackObjectName.UTF8String, "GotProductsSuccess", "");
    
    SKPayment * payment = [SKPayment paymentWithProduct:myProduct[0]];
    
    [[SKPaymentQueue defaultQueue] addPayment:payment];
}

- (void)paymentQueue:(SKPaymentQueue *)queue updatedTransactions:(NSArray *)transactions
{
    for (SKPaymentTransaction *transaction in transactions)
    {
        switch (transaction.transactionState)
        {
                // Call the appropriate custom method for the transaction state.
            case SKPaymentTransactionStatePurchasing:
                // 购买中
                [self showTransactionAsInProgress:transaction deferred:NO];
                break;
            case SKPaymentTransactionStateDeferred:
                // 购买超时
                [self showTransactionAsInProgress:transaction deferred:YES];
                break;
            case SKPaymentTransactionStateFailed:
                // 购买失败
                [self failedTransaction:transaction];
                break;
            case SKPaymentTransactionStatePurchased:
                // 购买完成
                [self verifyPurchaseWithPaymentTransaction:transaction restore:false];
                break;
            case SKPaymentTransactionStateRestored:
                // 恢复购买
                [self restoreTransaction:transaction];
                break;
     
            default:
                // For debugging
                NSLog(@"Unexpected transaction state %@", @(transaction.transactionState));
                break;
        }
    }
}

- (void) showTransactionAsInProgress:(SKPaymentTransaction*)transaction deferred:(BOOL)deffered
{
    //TODO
    NSLog(@"正在购买，请稍候");
}


- (void) completeTransactionByIdentifier:(NSString*)transactionIdentifier
{
     NSArray<SKPaymentTransaction *> * transactions = [[SKPaymentQueue defaultQueue] transactions];
    
    for (SKPaymentTransaction *transaction in transactions)
    {
        if (nil != transaction.transactionIdentifier)
        {
            BOOL result = [transaction.transactionIdentifier compare:transactionIdentifier];
            
            if (NULL != transaction && !result)
            {
                [self completeTransaction:transaction];
                return;
            }
        }
    }
}

- (void) completeTransaction:(SKPaymentTransaction*)transaction
{
    // Remove the transaction from the payment queue.
    [[SKPaymentQueue defaultQueue] finishTransaction: transaction];
    
    // Your application should implement these two methods.
    NSString * productIdentifier = transaction.payment.productIdentifier;
    
    if([productIdentifier length] > 0)
    {
        NSLog(@"productIdentifier : %@", productIdentifier);
    }
    
    UnitySendMessage(mCallBackObjectName.UTF8String, "BuyProductSuccess", productIdentifier.UTF8String);
}

// 支付失败
- (void) failedTransaction:(SKPaymentTransaction*)transaction
{
    if(transaction.error.code != SKErrorPaymentCancelled)
    {
        NSLog(@"failedTransaction error!");
  
        UnitySendMessage(mCallBackObjectName.UTF8String, "BuyProudctFailed", [transaction.payment.productIdentifier UTF8String]);
    }
    else
    {
        NSLog(@"failedTransaction user cancel!");
        
        UnitySendMessage(mCallBackObjectName.UTF8String, "BuyProudctFailed", [transaction.payment.productIdentifier UTF8String]);
    }
    
    [[SKPaymentQueue defaultQueue] finishTransaction: transaction];
}

- (void) restoreTransaction:(SKPaymentTransaction*)transaction
{
    
}

-(void) restoreTransactions:(NSMutableArray*)prodcutIdentifier username:(NSString*)username
{
    if(nil == mProductIdsToRestore)
    {
        mProductIdsToRestore = [[NSMutableArray alloc] init];;
    }

   for (NSString* identifier in prodcutIdentifier)
   {
       [mProductIdsToRestore addObject:identifier];
   }
    
    SKPaymentQueue *paymentQueue = [SKPaymentQueue defaultQueue];
    
    [paymentQueue addTransactionObserver:self];
    
    //NSString* usernameHash = [self hashedValueForAccountName:username];
    
    [paymentQueue restoreCompletedTransactions];
}


- (void)paymentQueueRestoreCompletedTransactionsFinished:(SKPaymentQueue *)queue
{
    NSArray<SKPaymentTransaction *> *transactions = [queue transactions];
    
    NSMutableArray *tmpProductIds = [mProductIdsToRestore mutableCopy];
    
    for (NSString* product in tmpProductIds)
    {
        bool exist = false;
        
        for (SKPaymentTransaction *transaction in transactions)
        {
            if ([transaction.payment.productIdentifier isEqualToString: product])
            {
                [mProductIdsToRestore removeObject:transaction.payment.productIdentifier];
                
                exist = true;
                
                [self verifyPurchaseWithPaymentTransaction:transaction restore:true];
            }
            else
            {
                 [[SKPaymentQueue defaultQueue] finishTransaction: transaction];
            }
        }
        
        if (!exist)
        {
            UnitySendMessage(mCallBackObjectName.UTF8String, "RestoreProductFailed", [product UTF8String]);
            
            [mProductIdsToRestore removeObject:product];
        }
    }
}

- (void)paymentQueue:(SKPaymentQueue *)queue restoreCompletedTransactionsFailedWithError:(NSError *)error
{
    NSLog(@"restoreCompletedTransactionsFailedWithError");
    
    UnitySendMessage(mCallBackObjectName.UTF8String, "CancelRestoreProducts", "");
}

- (void) verifyPurchaseWithPaymentTransaction:(SKPaymentTransaction*)transaction restore:(BOOL)restore
{
    //从沙盒中获取交易凭证并且拼接成请求体数据
    NSURL *receiptUrl=[[NSBundle mainBundle] appStoreReceiptURL];
    NSData *receiptData=[NSData dataWithContentsOfURL:receiptUrl];
    
    NSString *receiptString=[receiptData base64EncodedStringWithOptions:NSDataBase64EncodingEndLineWithLineFeed];//转化为base64字符串
    
    NSString *bodyString = [NSString stringWithFormat:@"%@#%@#%@", transaction.payment.productIdentifier, transaction.transactionIdentifier, receiptString];
    
    const char* bodyStr = [bodyString UTF8String];
    
    if (restore)
    {
        UnitySendMessage(mCallBackObjectName.UTF8String, "VerifyProductReceiptDataRestore", bodyStr);
    }
    else
    {
        [self sendReceiptToGameServer:receiptData transaction:transaction];
        UnitySendMessage(mCallBackObjectName.UTF8String, "VerifyProductReceiptData", bodyStr);
    }
}


// 发送收据到游戏服务器
- (void) sendReceiptToGameServer:(NSData*)receiptData transaction:(SKPaymentTransaction*)transaction
{
    //创建请求到苹果官方进行购买验证
    NSURL *url=[NSURL URLWithString:mGameServerAddress];
    
    NSMutableURLRequest *requestM=[NSMutableURLRequest requestWithURL:url];
    NSString *receiptString=[receiptData base64EncodedStringWithOptions:NSDataBase64EncodingEndLineWithLineFeed];
    NSString *param=[NSString stringWithFormat:@"receipt=%@&payInfo=%@",receiptString, self.mPayInfo];
    requestM.HTTPBody=[param dataUsingEncoding:NSUTF8StringEncoding];
    requestM.HTTPMethod=@"POST";
    
    //创建连接并发送同步请求
    NSError *error=nil;
    NSData *responseData=[NSURLConnection sendSynchronousRequest:requestM returningResponse:nil error:&error];
    
    if (error)
    {
        NSLog(@"验证购买过程中发生错误，错误信息：%@",error.localizedDescription);
        return;
    }
    
    NSDictionary *dic=[NSJSONSerialization JSONObjectWithData:responseData options:NSJSONReadingAllowFragments error:nil];
    NSLog(@"%@",dic);
    
    if([dic[@"state"] intValue] == 0)
    {
        [self completeTransaction:transaction];
        
        NSLog(@"购买成功！");
        NSDictionary *dicReceipt= dic[@"receipt"];
        NSDictionary *dicInApp=[dicReceipt[@"in_app"] firstObject];
        NSString *productIdentifier= dicInApp[@"product_id"];//读取产品标识
        
        //如果是消耗品则记录购买数量，非消耗品则记录是否购买过
        NSUserDefaults *defaults=[NSUserDefaults standardUserDefaults];
        
        if ([productIdentifier isEqualToString:@"123"])
        {
            int purchasedCount=[defaults integerForKey:productIdentifier];//已购买数量
            [[NSUserDefaults standardUserDefaults] setInteger:(purchasedCount+1) forKey:productIdentifier];
        }
        else
        {
            [defaults setBool:YES forKey:productIdentifier];
        }
        
        //在此处对购买记录进行存储，可以存储到开发商的服务器端
    }
    else
    {
        NSLog(@"购买失败，未通过验证！");
        
        //UnitySendMessage(self.mCallBackObjectName.UTF8String, "BuyProudctFailed", "PayFailed");
    }
}


- (NSString *)hashedValueForAccountName:(NSString*)userAccountName
{
    const int HASH_SIZE = 32;
    unsigned char hashedChars[HASH_SIZE];
    const char *accountName = [userAccountName UTF8String];
    size_t accountNameLen = strlen(accountName);
    
    // Confirm that the length of the user name is small enough
    // to be recast when calling the hash function.
    if (accountNameLen > UINT32_MAX) {
        NSLog(@"Account name too long to hash: %@", userAccountName);
        return nil;
    }
    
    CC_SHA256(accountName, (CC_LONG)accountNameLen, hashedChars);
    
    // Convert the array of bytes into a string showing its hex representation.
    NSMutableString *userAccountHash = [[NSMutableString alloc] init];
    for (int i = 0; i < HASH_SIZE; i++) {
        // Add a dash every four bytes, for readability.
        if (i != 0 && i%4 == 0) {
            [userAccountHash appendString:@"-"];
        }
        [userAccountHash appendFormat:@"%02x", hashedChars[i]];
    }
    
    return userAccountHash;
}


#if defined(__cplusplus)
extern "C" {
#endif
    
    NSString* ToNSString (const char* string)
    {
        return [NSString stringWithUTF8String:(string ? string : "")];
    }
    
    // 设置Unity回调的对象名
    void InitIAPHelperIOS(const char* callBackObjectName, const char* gameServerAddress)
    {
        [[IAPHelper Instance] initIAPHelper:ToNSString(callBackObjectName) gameServerAddress:ToNSString(gameServerAddress)];
    }
    
    // 开始购买商品
    void StartBuyProductIOS(const char* productId, const char* username)
    {
        [[IAPHelper Instance] startBuyProduct:ToNSString(productId) username:ToNSString(username)];
    }
    
    // 购买商品
    void BuyProductIOS(const char* productId)
    {
        [[IAPHelper Instance] getProductInfoById:ToNSString(productId)];
    }
    
    void CompletedSKPaymentTransactionIOS(const char* transactionIdentifier)
    {
        NSLog(@"CompletedSKPaymentTransactionIOS");
        
        [[IAPHelper Instance] completeTransactionByIdentifier:ToNSString(transactionIdentifier)];
    }
    
    void RestoreTransactionsIOS(const char* productsIdentifier, const char* username)
    {
        NSString* mProductIdstr = ToNSString(productsIdentifier);
        
        NSArray* mProductIds = [mProductIdstr componentsSeparatedByString:@"#"];
        
        NSMutableArray* mProductIdsMute = [NSMutableArray arrayWithArray:mProductIds];
        
        [[IAPHelper Instance] restoreTransactions:mProductIdsMute username:ToNSString(username)];
    }
    
    void RestoreTransactionIOS(const char* productIdentifier, const char* username)
    {
        NSString* mProductIdstr = ToNSString(productIdentifier);
        
        NSMutableArray* mProductIdsMute = [[NSMutableArray alloc] init];
        
        [mProductIdsMute addObject:mProductIdstr];
        
        [[IAPHelper Instance] restoreTransactions:mProductIdsMute username:ToNSString(username)];
    }
    
#if defined(__cplusplus)
}
#endif

@end
