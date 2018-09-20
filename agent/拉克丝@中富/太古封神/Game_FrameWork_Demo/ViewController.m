//
//  ViewController.m
//  Game_FrameWork_Demo
//
//  Created by huosdknew on 2017/11/4.
//  Copyright © 2017年 程强. All rights reserved.
//

#import "ViewController.h"
#import <GameFramework/GameFramework.h>

@interface ViewController ()
{
    UIButton *loginBtn;
    UIButton *payBtn;
    UIButton *setRoleInfoBtn;
    UISegmentedControl *segmentControl;
    UILabel *useridLabel;
    UILabel *sessionLabel;
    UIView *roleView;
    ///服务器id
    UITextField *serverid;
    ///服务器名
    UITextField *servername;
    ///角色名
    UITextField *reloName;
    ///角色id
    UITextField *reloId;
    
    UIButton *hiddenBtn;
    ///完成按钮
    UIButton *cofimBtn;
    ///取消
    UIButton *cancleBtn;
    ///1注册角色信息 2充值
    NSInteger index;
    
    UITextField *amountTF;
    
}
@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    CGFloat itemToLeft = 10.0;
    CGFloat lastItemOriginX = 10.0;
    CGFloat itemHeight = 37.0;
    self.view.backgroundColor = [UIColor whiteColor];
    loginBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [loginBtn.titleLabel setFont:[UIFont fontWithName:@"Arial" size:23.0]];
    loginBtn.backgroundColor = [UIColor orangeColor];
    loginBtn.frame = CGRectMake(itemToLeft, lastItemOriginX, self.view.frame.size.width - itemToLeft * 2, itemHeight);
    [loginBtn setTitle:@"登录" forState:UIControlStateNormal];
    [loginBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [loginBtn setTitleColor:[UIColor grayColor] forState:UIControlStateHighlighted];
    loginBtn.layer.cornerRadius = 7.0;
    [self.view addSubview:loginBtn];
    [loginBtn addTarget:self action:@selector(loginBtnPressed) forControlEvents:UIControlEventTouchUpInside];
    
    lastItemOriginX += 45.0;
    
    payBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [payBtn.titleLabel setFont:[UIFont fontWithName:@"Arial" size:23.0]];
    payBtn.backgroundColor = [UIColor orangeColor];
    payBtn.frame = CGRectMake(itemToLeft, lastItemOriginX, self.view.frame.size.width - itemToLeft * 2, itemHeight);
    [payBtn setTitle:@"支付" forState:UIControlStateNormal];
    [payBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [payBtn setTitleColor:[UIColor grayColor] forState:UIControlStateHighlighted];
    payBtn.layer.cornerRadius = 7.0;
    [self.view addSubview:payBtn];
    [payBtn addTarget:self action:@selector(payBtnPressed) forControlEvents:UIControlEventTouchUpInside];
    lastItemOriginX += 45.0;
    setRoleInfoBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [setRoleInfoBtn.titleLabel setFont:[UIFont fontWithName:@"Arial" size:23.0]];
    setRoleInfoBtn.backgroundColor = [UIColor orangeColor];
    setRoleInfoBtn.frame = CGRectMake(itemToLeft, lastItemOriginX, self.view.frame.size.width - itemToLeft * 2, itemHeight);
    [setRoleInfoBtn setTitle:@"发送角色信息" forState:UIControlStateNormal];
    [setRoleInfoBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [setRoleInfoBtn setTitleColor:[UIColor grayColor] forState:UIControlStateHighlighted];
    setRoleInfoBtn.layer.cornerRadius = 7.0;
    [self.view addSubview:setRoleInfoBtn];
    [setRoleInfoBtn addTarget:self action:@selector(setreloIngoBtnClick) forControlEvents:UIControlEventTouchUpInside];
    
    lastItemOriginX += 45.0;
    amountTF = [[UITextField alloc] init];
    amountTF.frame = CGRectMake(itemToLeft, lastItemOriginX, self.view.frame.size.width - itemToLeft * 2, itemHeight);
    //    _amount.delegate = self;
    amountTF.text = @"0.01";
    [self.view addSubview:amountTF];
    [Game_Api Game_addLogoutCallBlock:^(NSDictionary *responseDic) {
        [self loginBtnPressed];
    }];
    
    [Game_Api Game_sendInfoSuccessedCallBlock:^(NSDictionary *responseDic) {
        NSString *orderid = responseDic[@"orderid"];//单号
        NSString *cporderid = responseDic[@"cpi"]; //cp单号
        
        NSLog(@"成功支付***********");
    }];
    roleView = [[UIView alloc]init];
    
    roleView.backgroundColor = [UIColor whiteColor];
    
    roleView.frame = self.view.bounds;
    
    [self.view addSubview:roleView];
    
    [self setroleViewSubviews];
    
    hiddenBtn = [[UIButton alloc]init];
    
    [hiddenBtn setBackgroundColor:[UIColor clearColor]];
    
    [self.view addSubview:hiddenBtn];
    
    [hiddenBtn addTarget: self action:@selector(registerKeyBoardClick) forControlEvents:UIControlEventTouchUpInside];
    
    hiddenBtn.frame = self.view.bounds;
    
    roleView.hidden = YES;
    
    hiddenBtn.hidden = YES;
    
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(keyboardWillShow)
                                                 name:UIKeyboardWillShowNotification
                                               object:nil];
    
    
}
-(void)registerKeyBoardClick
{
    
    NSLog(@"辞去键盘");
    
    //    [self setEditing:NO];
    
    hiddenBtn.hidden = YES;
    
    [serverid resignFirstResponder];
    
    [servername resignFirstResponder];
    
    [reloName resignFirstResponder];
    
    [reloId resignFirstResponder];
    
}

-(void)keyboardWillShow
{
    
    hiddenBtn.hidden = NO;
    
}


-(void)setroleViewSubviews
{
    
    CGFloat itemToLeft = 10.0;
    CGFloat lastItemOriginX = 10.0;
    CGFloat itemHeight = 37.0;
    
    CGFloat width = self.view.frame.size.width - itemToLeft * 2;
    
    serverid = [[UITextField alloc]init];
    
    serverid.placeholder = @"服务器id";
    
    [roleView addSubview:serverid];
    
    serverid.frame = CGRectMake(itemToLeft, lastItemOriginX, width - itemToLeft * 2, itemHeight);


    
    
    lastItemOriginX += 45.0;
    
    servername = [[UITextField alloc]init];
    
    servername.placeholder = @"服务器名";
    
    [roleView addSubview:servername];
    
    servername.frame = CGRectMake(itemToLeft, lastItemOriginX, width - itemToLeft * 2, itemHeight);
    
    
    lastItemOriginX += 45.0;
    
    reloId = [[UITextField alloc]init];
    
    reloId.placeholder = @"角色id";
    
    [roleView addSubview:reloId];
    
    reloId.frame = CGRectMake(itemToLeft, lastItemOriginX, width - itemToLeft * 2, itemHeight);
    
    
    
    lastItemOriginX += 45.0;
    
    reloName = [[UITextField alloc]init];
    
    reloName.placeholder = @"角色名称";
    
    [roleView addSubview:reloName];
    
    reloName.frame = CGRectMake(itemToLeft, lastItemOriginX, width - itemToLeft * 2, itemHeight);
    
    lastItemOriginX += 45.0;
    
    cofimBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [cofimBtn.titleLabel setFont:[UIFont fontWithName:@"Arial" size:23.0]];
    cofimBtn.backgroundColor = [UIColor orangeColor];
    cofimBtn.frame = CGRectMake(itemToLeft, lastItemOriginX, width - itemToLeft * 2, itemHeight);
    [cofimBtn setTitle:@"完成" forState:UIControlStateNormal];
    [cofimBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [cofimBtn setTitleColor:[UIColor grayColor] forState:UIControlStateHighlighted];
    cofimBtn.layer.cornerRadius = 7.0;
    [roleView addSubview:cofimBtn];
    [cofimBtn addTarget:self action:@selector(cofimCLickWithButtonWithButton:) forControlEvents:UIControlEventTouchUpInside];
    
    lastItemOriginX += 45.0;
    
    cancleBtn = [UIButton buttonWithType:UIButtonTypeCustom];
    [cancleBtn.titleLabel setFont:[UIFont fontWithName:@"Arial" size:23.0]];
    cancleBtn.backgroundColor = [UIColor orangeColor];
    cancleBtn.frame = CGRectMake(itemToLeft, lastItemOriginX, width - itemToLeft * 2, itemHeight);
    [cancleBtn setTitle:@"取消" forState:UIControlStateNormal];
    [cancleBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [cancleBtn setTitleColor:[UIColor grayColor] forState:UIControlStateHighlighted];
    cancleBtn.layer.cornerRadius = 7.0;
    [roleView addSubview:cancleBtn];
    [cancleBtn addTarget:self action:@selector(cofimCLickWithButtonWithButton:) forControlEvents:UIControlEventTouchUpInside];
    
    cofimBtn.tag = 1;
    
    cancleBtn.tag = 2;
}
-(void)cofimCLickWithButtonWithButton:(UIButton *)sender
{
    
    
    [Game_Api Game_Logout];
    if (sender.tag == 1) {
        //1 注册角色
        if (index == 1) {
            
            [Game_Api Game_setRoleInfo:@{element_dataType : @"1",
                                         element_serverID : serverid.text,
                                         element_serverName : servername.text,
                                         element_roleID : reloId.text,
                                         element_roleName : reloName.text,
                                         element_roleLevel : @1,
                                         element_roleVip : @3,
                                         element_roleBalence : @10.2,
                                         element_partyName : @"testPart",
                                         element_rolelevelCtime : @"1479196021",
                                         element_rolelevelMtime : @"1479196736",
                                         }];
            
            //2 充值
        }else{
            
            NSDictionary *orderInfo = @{
                                        element_cpOrderid: @"testorderid",
                                        element_serverID: serverid.text,
                                        element_serverName: servername.text,
                                        element_productID: @"testproductid",
                                        element_productName: @"商品名字"/*product.localizedTitle*/,
                                        element_productdesc: @"goodsdes"/*product.localizedDescription*/,
                                        element_ext: @"testattach",
                                        element_productPrice: amountTF.text,
                                        element_roleID: reloId.text,
                                        element_roleName: reloName.text,
                                        element_currencyName : @"123",
                                        };
            [Game_Api Game_sendOrderInfo:orderInfo failedBlock:^{
                
                NSLog(@"payfailed");
            }];
            
        }
    }
    
    roleView.hidden = YES;
    
    hiddenBtn.hidden = YES;
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)setreloIngoBtnClick
{

    
    index = 1;
    
    roleView.hidden = NO;
    
}
- (void)loginBtnPressed
{
    [Game_Api Game_showLoginWithCallBack:^(NSDictionary *responseDic) {
        //block激活即是SDK登录成功，这个时候游戏通过block获取用户信息
        
        NSString *sdk_userid = responseDic[@"userid"];//sdk的用户id
        NSString *sdk_sessionid = responseDic[@"token"];//sdk的sessionid，用于验证登录是否成功
        
        NSLog(@"userid = %@", sdk_userid);
        NSLog(@"sessionid = %@", sdk_sessionid);
        
        [Game_Api Game_setRoleInfo:@{element_dataType : @"1",
                                     element_serverID : @"1",
                                     element_serverName : @"testserver",
                                     element_roleID : @"123",
                                     element_roleName : @"iOS测试账号",
                                     element_roleLevel : @1,
                                     element_roleVip : @3,
                                     element_roleBalence : @10.2,
                                     element_partyName : @"testparty",
                                     element_rolelevelCtime : @"1479196021",
                                     element_rolelevelMtime : @"1479196736",
                                     }];
        
    }];
}

- (void)payBtnPressed
{
    
    NSDictionary *orderInfo = @{
                                element_cpOrderid: @"testorderid",
                                element_serverID: @"1",
                                element_serverName: @"testserver",
                                element_productID: @"hongjing001",
                                element_productName: @"商品名字"/*product.localizedTitle*/,
                                element_productdesc: @"goodsdes"/*product.localizedDescription*/,
                                element_ext: @"testattach",
                                element_productPrice: amountTF.text,
                                element_roleID: @"123",
                                element_roleName: @"iOS测试账号",
                                element_currencyName : @"123",
                                };
    [Game_Api Game_sendOrderInfo:orderInfo failedBlock:^{
        
        NSLog(@"payfailed***************");
    }];
}

- (NSString *)p_getTimestamp
{
    double secondTime=[[[NSDate alloc]init]timeIntervalSince1970];
    NSString * secondTimeStr=[NSString stringWithFormat:@"%f",secondTime];
    NSRange pointRange=[secondTimeStr rangeOfString:@"."];
    NSString * MSTime=[secondTimeStr substringToIndex:pointRange.location];
    
    return MSTime;
}
- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event
{
    [super touchesBegan:touches withEvent:event];
    [[UIApplication sharedApplication].keyWindow endEditing:YES];
}
@end

