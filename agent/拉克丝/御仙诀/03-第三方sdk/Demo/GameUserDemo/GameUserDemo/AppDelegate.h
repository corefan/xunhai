//
//  AppDelegate.h
//  GameUserDemo
//
//  Created by Apple on 2017/5/16.
//  Copyright © 2017年 YaYa. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreData/CoreData.h>

@interface AppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong) NSPersistentContainer *persistentContainer;

- (void)saveContext;


@end

