//
//  PayViewController.h
//  IOSDemo
//
//  Created by 雪鲤鱼 on 15/7/16.
//  Copyright (c) 2015年 yijie. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <YijieAdvPlatform/YJSDKHelper.h>
@interface SFPayViewController : UIViewController<YJSDKHelperPayResultDelegate, YJSDKHelperAntiDelegate,UIAlertViewDelegate>
- (IBAction) payBtnClick:(id)sender;
- (IBAction) exitBtnClick:(id)sender;
- (IBAction) logoutBtnClick:(id)sender;
@end