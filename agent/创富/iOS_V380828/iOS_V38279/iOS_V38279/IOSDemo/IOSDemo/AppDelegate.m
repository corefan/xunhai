//
//  AppDelegate.m
//  IOSDemo
//
//  Created by 雪鲤鱼 on 15/6/29.
//  Copyright (c) 2015年 yijie. All rights reserved.
//

#import "AppDelegate.h"
 #import <YijieAdvPlatform/YJSDKHelper.h>

@interface AppDelegate ()

@end

@implementation AppDelegate




- (void)applicationWillResignActive:(UIApplication *)application {
    // Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
    // Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
}



- (void)applicationDidBecomeActive:(UIApplication *)application {
    // Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
}

- (void)applicationWillTerminate:(UIApplication *)application {
    // Called when the application is about to terminate. Save data if appropriate. See also applicationDidEnterBackground:.
}



- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation NS_AVAILABLE_IOS(4_2){
    
     return [[YJSDKHelper Instance] application:application openURL:url sourceApplication:sourceApplication annotation:annotation];
}



- (void)applicationDidEnterBackground:(UIApplication *)application {
    // Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
    // If your application supports background execution, this method is called instead of applicationWillTerminate: when the user quits.
    NSLog(@"demo application applicationDidEnterBackground！");
    //[[YJSDKHelper Instance] applicationDidEnterBackground:application];
 
 
     
    
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
 
 
    //[[YJSDKHelper Instance] applicationWillEnterForeground:application];
    
}
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    [YJSDKHelper initSDK :self];
    return  YES;
    
    
}

-(void) onResponse:(NSString*) tag : (NSString*) value {
    
    NSLog(@"sfwarning  onResponse:%@,%@", tag, value);
}
- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    
    

}
- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
    
    

    
}
- (NSUInteger)application:(UIApplication *)application supportedInterfaceOrientationsForWindow:(UIWindow *)window {
    
 
    if (UI_USER_INTERFACE_IDIOM() == UIUserInterfaceIdiomPhone)
        
        return UIInterfaceOrientationMaskAll;
    
    else
        
        return UIInterfaceOrientationMaskAllButUpsideDown;
    
}


- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url{
 
    return  [[YJSDKHelper Instance] application:application handleOpenURL:url];
    
}



@end
