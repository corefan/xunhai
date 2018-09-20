//
//  ExtendDemo.h
//  IOSDemo
//
//  Created by 雪鲤鱼 on 15/7/16.
//  Copyright (c) 2015年 yijie. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <OnlineAHelper/YiJieOnlineHelper.h>

@interface ExtendDemo :NSObject<YiJieOnlineExtendDelegate>
-(NSString*) extendTest : (NSString*) data : (int32_t) count ;
@end