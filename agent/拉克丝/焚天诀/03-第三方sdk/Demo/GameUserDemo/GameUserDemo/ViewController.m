//
//  ViewController.m
//  GameUserDemo
//
//  Created by Apple on 2017/5/16.
//  Copyright © 2017年 YaYa. All rights reserved.
//

#import "ViewController.h"
#import <GameUser_framwork/YvGameUserLoginViewController.h>
#import <GameUser_framwork/YvGameUserAPIManage.h>

@interface ViewController ()<YvGameUserAPIManageDelegate>

@end

@implementation ViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    // Do any additional setup after loading the view, typically from a nib.
    [YvGameUserAPIManage shareInstance].delegate = self;
    
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)LoginAction:(id)sender {
    
    [[YvGameUserAPIManage shareInstance] YvGameUserLogin];
    
}

- (IBAction)payAction:(id)sender
{
    [[YvGameUserAPIManage shareInstance] YvGameUserQueryFuKuanResult:^(BOOL isFuKuan) {
        if (isFuKuan)
        {
            YvOrderInfoModel *model = [[YvOrderInfoModel alloc] init];
            model.gameName = @"大闹天空：悟空";
            model.goodsName = @"60钻石";
            model.goodsId = [self randomString:32];
            model.fuAmount = 1;
            model.notifyUrl = [YvGameUserAPIManage shareInstance].isTest ? @"http://123.59.128.31:8088/cp/notify" : @"http://gamepay.aiwaya.cn:8089/cp/notify";
            
            [[YvGameUserAPIManage shareInstance] YvGameUserFuKuanWithOrderInfo:model];
        }
        else
        {
            //          苹果支付
            YvOrderInfoModel *model = [[YvOrderInfoModel alloc] init];
            model.goodsName = @"60钻石";
            model.goodsId = [self randomString:32];
            model.fuAmount = 600;
            model.notifyUrl = [YvGameUserAPIManage shareInstance].isTest ? @"http://123.59.128.31:8088/cp/notify" : @"http://gamepay.aiwaya.cn:8089/cp/notify";
            model.productId = @"appBuyItem_mlzd_1";
            
            [[YvGameUserAPIManage shareInstance] YvGameUserFuKuanWithOrderInfo:model];
        }
    }];

}

#pragma mark - YvGameUserAPIManageDelegate
-(void)YvGameUserLoginResp:(YvGameUserAPIManage*)apiManage result:(UInt32)result msg:(NSString*)msg token:(NSString*)token
{
    NSLog(@"token");
}

#pragma mark - private
- (NSString*)randomString:(NSInteger)randomLeng
{
    NSInteger leng = randomLeng;
    
    NSTimeInterval timeStamp =  [[NSDate date] timeIntervalSince1970];
    NSInteger second = timeStamp;
    
    //时间戳精确到秒
    NSString *secondString = [NSString stringWithFormat:@"%ld",(long)second];
    
    //随机长度必须要大于时间戳长度
    if (randomLeng > secondString.length)
    {
        leng = randomLeng - secondString.length;
    }
    else
    {
        secondString = @"";
    }
    char data[leng];
    
    for (NSInteger x = 0; x<leng; x++)
    {
        NSInteger v = arc4random() % 3;
        switch (v) {
            case 0:
            {
                data[x] = (char)('A' + (arc4random_uniform(26)));
            }
                break;
            case 2:
            {
                data[x] = (char)('a' + (arc4random_uniform(26)));
            }
                break;
                
            default:{
                data[x] = (char)('0' + (arc4random_uniform(9)));
            }
                break;
        }
    }
    
    return [secondString  stringByAppendingString:[[NSString alloc] initWithBytes:data length:leng encoding:NSASCIIStringEncoding]];
}




@end
