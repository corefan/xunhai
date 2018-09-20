//
//  ViewController.m
//  IOSDemo
//
//  Created by 雪鲤鱼 on 15/6/29.
//  Copyright (c) 2015年 yijie. All rights reserved.
//

#import "ViewController.h"
#import <AdSupport/AdSupport.h>

@interface ViewController ()

@end

@implementation ViewController
YJSDKHelperUser *mUser;

/*登录验证地址请替换成客户自己的服务器地址 */
//正服
//static const NSString* loginCheckUrl = @"http://testomsdk.xiaobalei.com:5555/cp/user/paylog/checkLoginP";
//测服
static const NSString* loginCheckUrl = @"http://testomsdk.xiaobalei.com:5555/cp/user/paylog/checkLogin";

//自建服务器 测服
//static const NSString* loginCheckUrl = @ "http://testomsdk.xiaobalei.com:5555/cp/user/paylog/checkLoginZijian";
//自建服务器 正服
//static const NSString* loginCheckUrl = @ "http://testomsdk.xiaobalei.com:5555/cp/user/paylog/checkLoginZijianP";

- (void)viewDidLoad {
    [super viewDidLoad];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
 
}

- (IBAction)loginClick:(id)sender {
 
    [YJSDKHelper login: self];
}

-(void) onYJLoginSuccess : (YJSDKHelperUser*) user{
    mUser = user;
    NSLog(@" login success =======");
 
    
}

-(void) onYJLoginFailed : (NSString*) reason   {
    NSLog(@"sfwarning  onLoginFailed:%@", reason);
}


-(void) onOderNo : (NSString*) msg {
    NSLog(@"sfwarning  pay onOderNo:%@", msg);
}

-(void) onYJLogout : (NSString *) remain {
     NSLog(@"sfwarning  logout onLogout:%@", remain);
    mUser = nil;
     [self dismissViewControllerAnimated:YES completion:nil];
}


-(void) onLoginCheck : (YJSDKHelperUser*) user {
    NSLog(@"sfwarning  onLoginCheck");
    
    //登录验证所需的参数获取方法参考
    NSString* uin = [user channnelUserId];
    NSString* token = [user token];
    NSDictionary *dict = [[NSBundle mainBundle] infoDictionary];
    NSString *app = [dict objectForKey:@"appid"];
    NSString *appkey = [dict objectForKey:@"appKey"];
    NSString* param = [NSString stringWithFormat:@"请将app=%@&token=%@&uin=%@发送给游戏服务器进行登录验证（具体流程请参考文档）",app, token,uin];
    [self showarltview:param];
    NSError * error = nil;
 
    if (error == nil)
    {
 
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            
            UIViewController* payController = [[self storyboard] instantiateViewControllerWithIdentifier: @"SFPayViewController"];
            [self presentViewController:payController animated:(NO) completion:nil];
  
         });
 
    }
 
}

- (IBAction)logincheckbutton:(id)sender {
    if(mUser==nil||mUser==NULL){
        [self showarltview:@"请先登录"];
    }else{
        [self onLoginCheck : mUser];
    }
    
}


-(void)showarltview:(NSString*)msg{
    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"登录验证结果" message:msg delegate:self cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
    [alert show];
}




@end
