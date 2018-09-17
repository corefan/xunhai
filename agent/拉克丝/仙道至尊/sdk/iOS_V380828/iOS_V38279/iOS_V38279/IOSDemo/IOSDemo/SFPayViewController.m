//
//  PayViewController.m
//  IOSDemo
//
//  Created by 雪鲤鱼 on 15/7/16.
//  Copyright (c) 2015年 yijie. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "SFPayViewController.h"


@interface SFPayViewController ()

@end

@implementation SFPayViewController
NSString* cpPaySybcUrl = @"http://testomsdk.xiaobalei.com:5555/sync/cb/ios/yijiepay/85659E9904556EC0/sync.html";
NSString* cpPayInfo = @"购买金币";
- (IBAction) payBtnClick:(id)sender
{
    YJSDKHelperPayInfo * payinfo = [YJSDKHelperPayInfo instance];
    payinfo.productName = @"100金币" ;
    payinfo.productPrice =600 ;
    payinfo.productCount =1 ;
    payinfo.orderId =  [self getUUIDString];
    payinfo.callbackURL = cpPaySybcUrl;
    payinfo.callbackInfo = @"";
    payinfo.productID = @"100001";
    payinfo.orderType = 0  ;// 交易类型  0 普通消费 1 充值货币  2 充值钱包
    [YJSDKHelper pay:payinfo :self ] ;
    

}


- (IBAction) exitBtnClick:(id)sender
{
 
         exit(0);
}

- (IBAction) logoutBtnClick:(id)sender
{
    [YJSDKHelper logout ];
    
}

-(void) onPayResponse:(NSString *)result :(NSString *)msg {
    if([result isEqualToString: @"success"]){
        
        NSString* str = [NSString stringWithFormat:@"success %@",msg];
        [self showAlert:str];
        
    }else if([result isEqualToString: @"failed"]){
        NSString* str = [NSString stringWithFormat:@"failed %@",msg];
        [self showAlert:str];
    }
    
}


/*游戏开始上报接口*/
- (IBAction)gamestartbutton:(id)sender {
    NSLog(@"游戏开始上报接口");
    NSMutableDictionary *info = [[NSMutableDictionary alloc] init];
    [info setObject:@"123" forKey:@"roleId"];
    [info setObject:@"100" forKey:@"roleLevel"];
    [info setObject:@"一区" forKey:@"zoneName"];
    [info setObject:@"zz" forKey:@"roleName"];
    [info setObject:@"1001" forKey:@"zoneId"];
    
    NSError* error1 = nil;
    NSData* jsonData1 = [NSJSONSerialization dataWithJSONObject:info options:NSJSONWritingPrettyPrinted error:&error1];
    if(error1 == nil){
        NSString* jsonString = [[NSString alloc] initWithData:jsonData1 encoding:NSUTF8StringEncoding];
        
        [YJSDKHelper gamestart:jsonString];
    }
}

/*角色升级上报接口*/
- (IBAction)levelupbutton:(id)sender {
    NSLog(@"角色升级上报接口");
    NSMutableDictionary *info = [[NSMutableDictionary alloc] init];
    [info setObject:@"123" forKey:@"roleId"];
    [info setObject:@"100" forKey:@"roleLevel"];
    [info setObject:@"一区" forKey:@"zoneName"];
    [info setObject:@"zz" forKey:@"roleName"];
    [info setObject:@"1001" forKey:@"zoneId"];
    
    NSError* error1 = nil;
    NSData* jsonData1 = [NSJSONSerialization dataWithJSONObject:info options:NSJSONWritingPrettyPrinted error:&error1];
    if(error1 == nil){
        NSString* jsonString = [[NSString alloc] initWithData:jsonData1 encoding:NSUTF8StringEncoding];
        
        [YJSDKHelper levelup:jsonString];
    }
    
    
}


/*创建角色上报接口*/
- (IBAction)createrolebutton:(id)sender {
    NSLog(@"创建角色上报接口");
    NSMutableDictionary *info = [[NSMutableDictionary alloc] init];
    [info setObject:@"123" forKey:@"roleId"];
    [info setObject:@"100" forKey:@"roleLevel"];
    [info setObject:@"一区" forKey:@"zoneName"];
    [info setObject:@"zz" forKey:@"roleName"];
    [info setObject:@"1001" forKey:@"zoneId"];
    
    NSError* error1 = nil;
    NSData* jsonData1 = [NSJSONSerialization dataWithJSONObject:info options:NSJSONWritingPrettyPrinted error:&error1];
    if(error1 == nil){
        NSString* jsonString = [[NSString alloc] initWithData:jsonData1 encoding:NSUTF8StringEncoding];
        
        [YJSDKHelper createrole:jsonString];
    }
    
}

 


-(void)showAlert:(NSString*)msg{
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:msg message:nil preferredStyle:UIAlertControllerStyleAlert];
    UIViewController *rootVC = self;
    if (rootVC) {
        [rootVC presentViewController:alert animated:YES completion:^{
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                [alert dismissViewControllerAnimated:YES completion:nil];
            });
        }];
    }
}

/*实名注册接口*/
- (IBAction) realnameBtnClick:(id)sender
{
     NSLog(@"realnameBtnClick  " );
      [YJSDKHelper realnamereg];
}

/*防沉迷查询接口
 * @param antiaddictionDelegate    防沉迷查询回调
 */
- (IBAction) antiaddictionBtnClick:(id)sender
{
  
    [YJSDKHelper antiaddictionQuery:self];
 
}

- (IBAction) checkgiftcodeBtnClick:(id)sender
{
    [self showCGAlert];
  
}
/*防沉迷回调
 *resp：json字符串：code：0:成功 其它：失败码
 msg：失败原因
 */
-(void) onAntiResponse : (NSString*) resp{
    
    
        NSDictionary* jsonData = [SFPayViewController dictionaryWithJsonString:resp];
        
        NSString * msg =   [[jsonData valueForKey:@"msg"] objectForKey:@"msg" ] ;;
        
 
        [self showAlert :msg ] ;
 
}



-(void)showCGAlert{
    
       
   
    UIAlertView* alert = [[UIAlertView alloc] initWithTitle:@"激活礼包"
                          
                                                    message:@"please input"
                          
                                                   delegate:self
                          
                                          cancelButtonTitle:@"cancel"
                          
                                          otherButtonTitles:@"OK",nil];
    
    // 基本输入框，显示实际输入的内容
    
    alert.alertViewStyle = UIAlertViewStylePlainTextInput;
    
    //设置输入框的键盘类型
    
    UITextField *tf = [alert textFieldAtIndex:0];
    
    tf.keyboardType = UIKeyboardTypeNumberPad;

    [alert show];
    
    
}

-(void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if (buttonIndex == 1) {
        UITextField *txt = [alertView textFieldAtIndex:0];
        NSString* code = txt.text;
 
       NSLog(@"alertView %@" );
     
        if (code> 0 ){
     
            [YJSDKHelper checkgiftcode:code :self];
        } else {
            NSLog(@"error %@" );
        }
    }
    
    
}
/*礼包激活码回调
 *resp：json字符串：result：0:成功 其它：失败码
 msg：失败原因
 */
-(void) onGiftcodeResponse : (NSString*) result{
    
    NSDictionary* jsonData = [SFPayViewController dictionaryWithJsonString:result];
    
    NSString * msg = [jsonData valueForKey:@"msg"] ;
 
      [self showAlert :result ] ;
    
    
}

+ (NSDictionary *)dictionaryWithJsonString:(NSString *)jsonString {
    
    if (jsonString == nil) {
        return nil;
    }
    NSData *jsonData = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
    NSError *err;
    NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:jsonData
                                                        options:NSJSONReadingMutableContainers
                                                          error:&err];
    if(err) {
        
        NSLog(@"json解析失败：%@",err);
        
        return dic;
    }
    
    return dic;
    
}

- (NSString*) getUUIDString

{
    
    CFUUIDRef uuidObj = CFUUIDCreate(nil);
    
    NSString *uuidString = (__bridge_transfer NSString*)CFUUIDCreateString(nil, uuidObj);
    
    CFRelease(uuidObj);
    NSString*uuid = [[uuidString lowercaseString] stringByReplacingOccurrencesOfString:@"-" withString:@""];
    NSLog(@"uuid =  %@",uuid);
    return uuid;
    
}


- (IBAction) RoleButton:(id)sender
{
    UIViewController* roleController = [[self storyboard] instantiateViewControllerWithIdentifier: @"RoleInputController"];
    [self presentViewController:roleController animated:(NO) completion:nil];
    
}

@end
