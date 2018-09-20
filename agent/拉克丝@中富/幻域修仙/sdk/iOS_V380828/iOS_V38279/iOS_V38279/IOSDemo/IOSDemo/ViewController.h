//
//  ViewController.h
//  IOSDemo
//
//  Created by 雪鲤鱼 on 15/6/29.
//  Copyright (c) 2015年 yijie. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <YijieAdvPlatform/YJSDKHelper.h>

@interface ViewController : UIViewController<YJSDKHelperLoginDelegate>

- (IBAction)initClick:(id)sender;
- (IBAction)loginClick:(id)sender;
-(void) extend;
@end

