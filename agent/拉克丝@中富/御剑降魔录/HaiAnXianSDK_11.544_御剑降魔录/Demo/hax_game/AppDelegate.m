
//
//  AppDelegate.m
//  ttdb_game
//
//  Created by zhu on 16/6/23.
//  Copyright © 2016年 张高杰. All rights reserved.
//

#import "AppDelegate.h"
#import "ViewController.h"
#import <HGameUnit/HAXApi.h>
@interface AppDelegate ()


@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:
        (NSDictionary *)launchOptions {
    
    NSLog(@"SDK version:%@",[HAXApi HAXVersion]);
    //初始化SDK,传入游戏信息
    [[HAXApi HaxInstance] SetApiWithInfo:@"50000000"            //gameId
                                     gai:@"3A4C2D409AB4BEF43"   //gameAppId
                                    name:@"王者荣耀"                //gameName
                                      pi:@"50000000"            //promoteId
                                      pa:@"test1a"              //promoteAccount
                              completion:^(NSDictionary *resultDic)
    {
        NSLog(@"[init] resultDic:%@",resultDic);
        NSNumber *statusCode = [resultDic objectForKey:@"statusCode"];
        if ([statusCode intValue] == HAXINITSuccess)
        {
            //初始化成功
            //登录须放在初始化成功后处理
        }
    }];
    
    //(1)当从用户中心页面退出登录或手动调用Logout，此回调会被调用
    [[HAXApi HaxInstance] SetLogoutCallback:^(NSDictionary *resultDic)
    {
        //处理退出登录后的释放逻辑
        NSLog(@"resultDic:%@",resultDic);
    }];
    return YES;
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    // Called as part of the transition from the background to the inactive state; here you can undo many of the changes made on entering the background.
}
- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
}



- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
    // Saves changes in the application's managed object context before the application terminates.
}

@end
