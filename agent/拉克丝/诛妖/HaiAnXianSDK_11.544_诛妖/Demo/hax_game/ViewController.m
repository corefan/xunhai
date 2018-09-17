//
//  ViewController.m
//  ttdb_game
//
//  Created by zhu on 16/6/23.
//  Copyright © 2016年 张高杰. All rights reserved.
//

#import "ViewController.h"
#import <HGameUnit/HAXApi.h>

@interface ViewController ()


@end

@implementation ViewController

-(void)viewWillAppear:(BOOL)animated{
    
    [[self navigationController] setNavigationBarHidden:YES
                                               animated:YES];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    //self.view.backgroundColor = [UIColor colorWithRed:0.6 green:0.6 blue:0.8 alpha:1];
    float screenWidth = [[UIScreen mainScreen] bounds].size.width;
    float screenHeight = [[UIScreen mainScreen] bounds].size.height;
    
    UIImage *image = [UIImage imageNamed:@"LaunchImage.png"];
    UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, screenWidth, screenHeight)];
    [imageView setImage:image];
    [self.view addSubview:imageView];
    
    float buttonWidth = 100;
    float buttonHeight = 40;
    float fromPosY = screenHeight / 2 - 100;
    float fromPosX = screenWidth / 2 - buttonWidth / 2;
    float offsetY = 60;
    UIColor *color1 = [UIColor colorWithRed:0.0f green:0.5f blue:1.0f alpha:1.0f];
    UIColor *color2 = [UIColor colorWithRed:0.2f green:0.5f blue:1.0f alpha:0.8f];
    
    UIButton *btnLogin = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [btnLogin setFrame:CGRectMake(fromPosX, fromPosY, buttonWidth, buttonHeight)];
    btnLogin.backgroundColor = [UIColor colorWithRed:180.0/255 green:200.0/255 blue:242.0/255 alpha:1];
    [btnLogin setTitle:@"点击登录" forState:UIControlStateNormal];
    [btnLogin.layer setMasksToBounds:YES];
    [btnLogin.layer setCornerRadius:6];
    btnLogin.titleLabel.font = [UIFont systemFontOfSize: 18];
    [btnLogin setTitleColor:color1 forState:UIControlStateNormal];
    [btnLogin setTitleColor:color2 forState:UIControlStateHighlighted];
    [btnLogin addTarget:self action:@selector(loginAction) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:btnLogin];
    
    UIButton *btnPay = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [btnPay setFrame:CGRectMake(fromPosX, fromPosY + offsetY, buttonWidth, buttonHeight)];
    btnPay.backgroundColor = [UIColor colorWithRed:190.0/255 green:200.0/255 blue:242.0/255 alpha:1];
    [btnPay setTitle:@"商品" forState:UIControlStateNormal];
    [btnPay.layer setMasksToBounds:YES];
    [btnPay.layer setCornerRadius:6];
    btnPay.titleLabel.font = [UIFont systemFontOfSize: 18];
    [btnPay setTitleColor:color1 forState:UIControlStateNormal];
    [btnPay setTitleColor:color2 forState:UIControlStateHighlighted];
    [btnPay addTarget:self action:@selector(payAction) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:btnPay];
    
    UIButton *btnQuickLogin = [UIButton buttonWithType:UIButtonTypeRoundedRect];
    [btnQuickLogin setFrame:CGRectMake(fromPosX, fromPosY + offsetY * 2, buttonWidth, buttonHeight)];
    btnQuickLogin.backgroundColor = [UIColor colorWithRed:200.0/255 green:200.0/255 blue:242.0/255 alpha:1];
    [btnQuickLogin setTitle:@"退出登陆" forState:UIControlStateNormal];
    [btnQuickLogin.layer setMasksToBounds:YES];
    [btnQuickLogin.layer setCornerRadius:6];
    btnQuickLogin.titleLabel.font = [UIFont systemFontOfSize: 18];
    [btnQuickLogin setTitleColor:color1 forState:UIControlStateNormal];
    [btnQuickLogin setTitleColor:color2 forState:UIControlStateHighlighted];
    [btnQuickLogin addTarget:self action:@selector(LogoutAction) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:btnQuickLogin];
    
}

-(void)loginAction
{
    NSLog(@"loginAction");
    [[HAXApi HaxInstance] AddLoginView:^(NSDictionary *resultDic)
    {
        NSLog(@"[showLogin] resultDic = %@", resultDic);
        NSNumber *loginresult = [resultDic objectForKey:@"loginresult"];
        if([loginresult intValue] == HAXLOGINSuccess)
        {
            NSLog(@"登录成功");
            //todo
        }
    }];
}

-(void)payAction
{    
    int price = 64800;  //648元
    OrderInfo *orderInfo = [[OrderInfo alloc] init];
    orderInfo.goodsName = @"金币";
    orderInfo.goodsPrice = price;                       //单位为分
    orderInfo.goodsDesc = @"有了金币就可以买买买了";    //商品描述
    orderInfo.extendInfo = @"1234567890";           //此字段会透传到游戏服务器，可拼接订单信息和其它信息等
    orderInfo.productId = @"com.test.product12";     //虚拟商品在APP Store中的ID
    //-------注意：此处需要传入的是区服的名称，而不是区服编号-------------------------
    orderInfo.player_server = @"素月流天";            //玩家所在区服名称（跟游戏内显示的区服保持一致）
    orderInfo.player_role = @"小明";                 // 玩家角色名称
    orderInfo.cp_trade_no = @"201705101028";        //CP订单号
    
    [[HAXApi HaxInstance] GoPurView:orderInfo completionBlock:^(NSDictionary *resultDic)
    {
        NSLog(@"[product] resultDic = %@", resultDic);
        NSNumber *payresult = [resultDic objectForKey:@"payresult"];
        int state = [payresult intValue];
        switch (state)
        {
            case HAXTreatedOrderSuccess:
            {
                UIAlertView * alert = [[UIAlertView alloc] initWithTitle:@"提示"
                                                                 message:@"购买成功"
                                                                delegate:self
                                                       cancelButtonTitle:nil
                                                       otherButtonTitles:@"OK", nil];
                [alert show];
                
                
                break;
            }
            case HAXTreatedOrderFail:
            {
                UIAlertView * alert = [[UIAlertView alloc] initWithTitle:@"提示"
                                                                 message:@"支付失败"
                                                                delegate:self
                                                       cancelButtonTitle:nil
                                                       otherButtonTitles:@"OK", nil];
                [alert show];
                break;
            }
            case HAXTreatedOrderCancel:
            case HAXTreatedOrderCloseMenu:
            {
                UIAlertView * alert = [[UIAlertView alloc] initWithTitle:@"提示"
                                                                 message:@"支付取消"
                                                                delegate:self
                                                       cancelButtonTitle:nil
                                                       otherButtonTitles:@"OK", nil];
                [alert show];
                break;
            }
            case HAXTreatedOrdering:
                break;
            default:
            {
                UIAlertView * alert = [[UIAlertView alloc] initWithTitle:@"提示"
                                                                 message:[NSString stringWithFormat:@"支付失败:%d", state]
                                                                delegate:self
                                                       cancelButtonTitle:nil
                                                       otherButtonTitles:@"OK", nil];
                [alert show];
                break;
            }
        }
    }];

    
    
}

/**
 *  退出登录
 */
- (void)LogoutAction
{
    [[HAXApi HaxInstance] Logout];
    /* SetLogoutCallback设置的回调会被调用,请在该回调中退到游戏的登录页 */
}

- (UIColor *)colorWithHexWithLong:(long)hexColor
{
    float red = ((float)((hexColor & 0xFF0000) >> 16))/255.0;
    float green = ((float)((hexColor & 0xFF00) >> 8))/255.0;
    float blue = ((float)(hexColor & 0xFF))/255.0;
    
    return [UIColor colorWithRed:red green:green blue:blue alpha:1];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
