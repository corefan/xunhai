//
//  ExtendDemo.m
//  IOSDemo
//
//  Created by 雪鲤鱼 on 15/7/16.
//  Copyright (c) 2015年 yijie. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ExtendDemo.h"

@implementation ExtendDemo

-(void) onResponse:(NSString*) tag : (NSString*) value {
    
    NSLog(@"sfwarning  extend onResponse:%@,%@", tag, value);
}
-(NSString*) extend : (NSString*) data : (int32_t) count {
    NSString* r =[YiJieOnlineHelper extend : data : count];
    NSLog(@"demo extend r = %@",r);
    return r;
}
-(void) setExtendCallback : (id)extendCallback {
    NSLog(@"demo setExtendCallback");
    [YiJieOnlineHelper setExtendCallback : extendCallback];
}
-(NSString*) extendTest : (NSString*) data : (int32_t) count {
 [self setExtendCallback : self];
 return [self extend : data : count];
}
@end