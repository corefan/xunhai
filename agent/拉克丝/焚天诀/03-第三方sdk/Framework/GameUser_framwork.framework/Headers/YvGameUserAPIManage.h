//
//  YvGameUserAPIManage.h
//  GameUser_framwork
//
//  Created by Apple on 2017/5/18.
//  Copyright © 2017年 YaYa. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@class YvGameUserAPIManage;
@class YvFuOrderResp;
@class YvFuTypeInfo;

@interface YvOrderInfoModel : NSObject

@property (nonatomic, copy) NSString *gameName;
@property (nonatomic, copy) NSString *goodsId;
@property (nonatomic, copy) NSString *goodsName;
@property (nonatomic, assign) NSInteger fuAmount;
@property (nonatomic, copy) NSString *notifyUrl;
@property (nonatomic, copy) NSString *productId;

@end

//委托
@protocol YvGameUserAPIManageDelegate <NSObject>

@optional

- (void)YvGameUserLoginResp:(YvGameUserAPIManage*)apiManage result:(UInt32)result msg:(NSString*)msg token:(NSString*)token;

@end

@interface YvGameUserAPIManage : NSObject

@property(nonatomic, copy) NSString* appId;
@property(nonatomic, copy) NSString* account;
@property(nonatomic, copy) NSString* channelId;
@property(nonatomic, copy) NSString* urlScheme;
@property(nonatomic, assign) BOOL isTest;
@property(nonatomic, assign) UInt32 yunvaId;
@property(nonatomic, assign) BOOL   isLogin;

@property(nonatomic, weak) id<YvGameUserAPIManageDelegate> delegate;

+ (YvGameUserAPIManage *)shareInstance;

-(void)initWithAppId:(NSString*)appId channelId:(NSString*)channelId urlScheme:(NSString *)urlScheme isTest:(BOOL)isTest;

- (void)YvGameUserLogin;

- (void)YvGameUserQueryFuKuanResult:(void(^)(BOOL isFuKuan))result;

- (void)YvGameUserFuKuanWithOrderInfo:(YvOrderInfoModel *)orderInfo;

@end

