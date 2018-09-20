//
//  ViewController.m
//  demo
//
//  Created by Tommy on 2018/7/18.
//  Copyright © 2018年 p0sixB1ackcat. All rights reserved.
//

#import "ViewController.h"

#import <aa_GameSDK/AAAGameManager.h>


#define  kWidth  [UIScreen mainScreen].bounds.size.width
#define  kHeight [UIScreen mainScreen].bounds.size.height
//竖屏比例

#define  width_scale_Vertical  [UIScreen mainScreen].bounds.size.width/375.0f
//判断iPhonex
#define  height_scale_Vertical  (([UIScreen mainScreen].bounds.size.height == 812.0f) ? (([UIScreen mainScreen].bounds.size.height - 145)/667.0f):([UIScreen mainScreen].bounds.size.height/667.0f))

//横屏放缩比例
//判断iPhonex
#define  width_scale_Landscape  (([UIScreen mainScreen].bounds.size.width == 812.0f) ? (([UIScreen mainScreen].bounds.size.width - 145)/667.0f) : ([UIScreen mainScreen].bounds.size.width/667.0f))
#define  height_scale_Landscape  [UIScreen mainScreen].bounds.size.height/375.0f


@interface ViewController ()
@property(nonatomic,strong)UIButton *btn1;
@property(nonatomic,strong)UIButton *btn2;
@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.view.backgroundColor = [UIColor grayColor];
    
    UIButton *btn = [[UIButton alloc]initWithFrame:CGRectMake(100, 100, kWidth - 200 , 50)];
    [btn  setTitle:@"登录" forState:UIControlStateNormal];
    btn.backgroundColor = [UIColor cyanColor];
    [btn addTarget:self action:@selector(loginAction:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:btn];
    self.btn1 = btn;
    
    UIButton *btn2 = [[UIButton alloc]initWithFrame:CGRectMake(100, 200, kWidth - 200 , 50 )];
    [btn2 setTitle:@"支付" forState:UIControlStateNormal];
    [btn2 setBackgroundColor:[UIColor cyanColor]];
    [btn2 addTarget:self action:@selector(payAction:) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:btn2];
    self.btn2 = btn2;
}


-(void)loginAction:(UIButton *)sender{
    
    
    [[AAAGameManager shareManager] txy_loginViewShowAndBlock:^(NSString *uid, NSString *token) {
        
        NSLog(@"---%@--%@",uid,token);
        
    }];
    
}


-(void)payAction:(UIButton *)sender{
    
     [[AAAGameManager shareManager]txy_payWithGoodsName:@"商品名称" Price:@"商品价格" game_trade_no:@"订单号(前端生成用于验证订单)" producetID:@"内购id(appstore后台的id)"];
    
}


-(void)quitGame{
    
    [[AAAGameManager shareManager] txy_quitUserAccount:^(NSString *message) {
        
        NSLog(@"退出账号");
        
    }];
}

-(void)viewWillLayoutSubviews{
    [super viewDidLayoutSubviews];
    if ([UIApplication sharedApplication].statusBarOrientation == UIInterfaceOrientationPortrait || [UIApplication sharedApplication].statusBarOrientation == UIInterfaceOrientationPortraitUpsideDown) {
        
        self.btn1.frame = CGRectMake(100 * width_scale_Vertical, 100 *height_scale_Vertical, kWidth - 200 * width_scale_Vertical , 50 * height_scale_Vertical);
        self.btn2.frame = CGRectMake(100 * width_scale_Vertical, 200 * height_scale_Vertical, kWidth - 200 * width_scale_Vertical , 50*height_scale_Vertical);
        self.view.frame = [UIScreen mainScreen].bounds;
        
     
        
        
    }else{
        
        self.btn1.frame = CGRectMake(100 * width_scale_Landscape, 100 * height_scale_Landscape, kWidth - 200 * width_scale_Landscape , 50 * height_scale_Landscape);
        self.btn2.frame = CGRectMake(100 * width_scale_Landscape, 200 *height_scale_Landscape, kWidth - 200 *width_scale_Landscape , 50 * height_scale_Landscape );
        self.view.frame = [UIScreen mainScreen].bounds;
        
    
        
    }
    
    
}


//产生随机字符串
- (NSString *)generateTradeNO
{
    static int kNumber = 15;
    
    NSString *sourceStr = @"0123456789ABCDEFGHIJKLMNOPQRST";
    NSMutableString *resultStr = [[NSMutableString alloc] init];
    srand(time(0));
    for (int i = 0; i < kNumber; i++)
    {
        unsigned index = rand() % [sourceStr length];
        NSString *oneStr = [sourceStr substringWithRange:NSMakeRange(index, 1)];
        [resultStr appendString:oneStr];
    }
    return resultStr;
}


@end
