//
//  ViewController.m
//  ZJGSDKDemo
//
//  Created by Dai on 2018/6/21.
//  Copyright © 2018年 Dai. All rights reserved.
//

#import "ViewController.h"
#import <ZJGSDK/ZJGSDK.h>

@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    
    self.view.backgroundColor = [UIColor lightGrayColor];
    
    //设置悬浮按钮菜单为显示,默认不显示
    [ZJGSDK hasFloatMenuButton:YES];
    
    //为注销添加观察者
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(userLogoff:) name:kZJGAccountLogoutNotify object:nil];
    
    UIButton *loginButton = [[UIButton alloc] init];
    [loginButton setBackgroundColor:[UIColor purpleColor]];
    [loginButton setFrame:CGRectMake(20, 50, self.view.frame.size.width - 20*2, 40)];
    [loginButton setTitle:@"登录" forState:UIControlStateNormal];
    [self.view addSubview:loginButton];
    [loginButton addTarget:self action:@selector(login:) forControlEvents:UIControlEventTouchUpInside];
    
    UIButton *logoffButton = [[UIButton alloc] init];
    [logoffButton setBackgroundColor:[UIColor purpleColor]];
    [logoffButton setFrame:CGRectMake(20, 100, self.view.frame.size.width - 20*2, 40)];
    [logoffButton setTitle:@"注销" forState:UIControlStateNormal];
    [self.view addSubview:logoffButton];
    [logoffButton addTarget:self action:@selector(logoff:) forControlEvents:UIControlEventTouchUpInside];
    
    UIButton *payButton = [[UIButton alloc] init];
    [payButton setBackgroundColor:[UIColor purpleColor]];
    [payButton setFrame:CGRectMake(20, 150, self.view.frame.size.width - 20*2, 40)];
    [payButton setTitle:@"支付" forState:UIControlStateNormal];
    [self.view addSubview:payButton];
    [payButton addTarget:self action:@selector(pay:) forControlEvents:UIControlEventTouchUpInside];
    
    UIButton *showMenuButton = [[UIButton alloc] init];
    [showMenuButton setBackgroundColor:[UIColor purpleColor]];
    [showMenuButton setFrame:CGRectMake(20, 200, self.view.frame.size.width - 20*2, 40)];
    [showMenuButton setTitle:@"账号菜单" forState:UIControlStateNormal];
    [self.view addSubview:showMenuButton];
    [showMenuButton addTarget:self action:@selector(showMenu:) forControlEvents:UIControlEventTouchUpInside];
}

- (void)login:(id)sender {
    //拉起登录
    [ZJGSDK login:^(NSString *userID, NSString *account) {
        //登录成功后上报用户
        [ZJGSDK reportUserID:@"test_user"];
    }];
}

- (void)userLogoff:(NSNotification *)notify {
    NSLog(@"用户注销登录 %@ %@", notify.userInfo[@"userid"], notify.userInfo[@"username"]);
}

- (void)logoff:(id)sender {
    [ZJGSDK logout];
}

- (void)pay:(id)sender {
    //购买商品
    [ZJGSDK tradeGoodsID:@"com.qyn.werewolfkilled06" goodsName:@"购买60元宝" price:60*100.0 extend:@"这是附加内容" callbackUrl:@"http://xxx.callback.com" completionHandler:^(NSError *error) {
        //购买结果
        if (error == nil) {
            NSLog(@"购买成功");
        }else{
            NSLog(@"购买失败 %@", [error localizedDescription]);
        }
    }];
}

- (void)showMenu:(id)sender {
    [ZJGSDK showAccountMenu];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


@end
