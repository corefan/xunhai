//
//  ZJGSDK.h
//  ZJGSDK
//
//  Version Beta 0.1.1
//
//  Created by Dai on 2018/6/13.
//  Copyright © 2018年 Dai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

//! Project version number for ZJGSDK.
FOUNDATION_EXPORT double ZJGSDKVersionNumber;

//! Project version string for ZJGSDK.
FOUNDATION_EXPORT const unsigned char ZJGSDKVersionString[];



@interface ZJGSDK : NSObject

#pragma mark Require
/**
 ！使用SDK之前必须注册渠道ID
 请在程序启动时注册
 
 @param channelID 联系ZJGame获取指定渠道ID.
 */
+ (void)registZJGSDK:(NSString *)channelID;

/**
 在用户登入游戏后，需要上报用户游戏内唯一标识(用户ID或角色ID)
 用于登录用户同步 或 支付用户标识
 
 @param userID 用户游戏内唯一标识
 */
+ (void)reportUserID:(NSString *)userID;

//ZJGame 支付系统------------------------------------------
#pragma mark ZJTrade

/**
 获取交易方式类型

 @return 0 for apple
 */
+ (int)tradeType;

/**
 内购交易

 @param goodsID 内购商品ID
 @param goodsName 内购商品名称
 @param price 内购商品价格,以分为单位 例如：648.00元 则传入值64800.00
 @param extend 扩展字段，在服务器回调时原内容返回
 @param url 游戏服务器回调地址
 @param handler 支付结果回调
 */
+ (void)tradeGoodsID:(NSString *)goodsID goodsName:(NSString *)goodsName price:(double)price extend:(NSString *)extend callbackUrl:(NSString *)url completionHandler:(void(^)(NSError *error))handler;


//ZJGame 登录系统-------------------------------------------------------
#pragma mark ZJLogin

//ZJGame 账号登出通知
FOUNDATION_EXTERN NSString *const kZJGAccountLogoutNotify;

/**
 ZJGame账号系统 用户登录
 
 用户登录会拉起ZJGame账号登录页面，账号登录成功后通过callback回调返回用户id与用户账号
 
 @param callback 登录回调 userID, userName
 */
+ (void)login:(void(^)(NSString *userID, NSString *account))callback;

/**
 ZJGame 登出当前账号并重新拉起ZJGame登录页面
 */
+ (void)logout;

/**
 登录状态手动调用此方法，可以拉起ZJGame账号管理页面
 
 */
+ (void)showAccountMenu;

/**
 是否展示账号悬浮按钮
 
 默认不展示
 
 @param flag YES 展示悬浮按钮; NO 隐藏悬浮按钮
 */
+ (void)hasFloatMenuButton:(BOOL)flag;


@end
