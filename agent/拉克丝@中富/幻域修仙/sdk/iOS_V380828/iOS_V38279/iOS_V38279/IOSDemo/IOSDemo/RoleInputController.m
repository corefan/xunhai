//
//  ChargeInputController.m
//  IOSDemo
//
//  Created by 雪鲤鱼 on 15/7/17.
//  Copyright (c) 2015年 yijie. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "RoleInputController.h"
#import <YijieAdvPlatform/YJSDKHelper.h>
@interface RoleInputController ()

@end

@implementation RoleInputController
UITextField* roleid;
UITextField* rolename;
UITextField* rolelevel;
UITextField* zoneid;
UITextField* zonename;
UIButton *createbutton;
UIButton *gamestartbutton;
UIButton *levelbutton;
int type = 0;


- (IBAction)roleidTextField:(UITextField *)sender {
    
    roleid = sender;
}
- (IBAction)rolenameTextField:(UITextField *)sender {
    rolename = sender;
    
}
- (IBAction)roleleveltxfield:(id)sender {
    rolelevel = sender;
    
}
- (IBAction)zoneidtxfield:(id)sender {
    zoneid = sender;
    
}

- (IBAction)zonenametxfield:(id)sender {
    zonename = sender;
    
}


- (IBAction) cancelBtnClick:(id)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}


- (IBAction) sureBtnClick:(id)sender {
    if(roleid.text.length == 0){
        [self showarltview];
        return;
    }else if(rolelevel.text.length == 0){
        [self showarltview];
        return;
    }else if(zonename.text.length == 0){
        [self showarltview];
        return;
    }else if(rolename.text.length == 0){
        [self showarltview];
        return;
    }else if(zoneid.text.length == 0){
        [self showarltview];
        return;
    }
    
    
    NSMutableDictionary *info = [[NSMutableDictionary alloc] init];
    [info setObject:roleid.text forKey:@"roleId"];
    [info setObject:rolelevel.text forKey:@"roleLevel"];
    [info setObject:zonename.text forKey:@"zoneName"];
    [info setObject:rolename.text forKey:@"roleName"];
    [info setObject:zoneid.text forKey:@"zoneId"];
    NSLog(@"type:%d",type);
    NSError* error1 = nil;
    NSData* jsonData1 = [NSJSONSerialization dataWithJSONObject:info options:NSJSONWritingPrettyPrinted error:&error1];
    if(error1 == nil){
        NSString* jsonString = [[NSString alloc] initWithData:jsonData1 encoding:NSUTF8StringEncoding];
        if(type==0){
            [YJSDKHelper createrole:jsonString];
        }else if(type==1){
             [YJSDKHelper gamestart:jsonString];
        }else if(type==2){
            [YJSDKHelper levelup:jsonString];
        }
        
    }
    
}

- (IBAction)createrole:(id)sender {
    type = 0;
        createbutton = (UIButton *)sender;
    [createbutton setBackgroundColor:[UIColor grayColor]];
        [levelbutton setBackgroundColor:[UIColor whiteColor]];

        [gamestartbutton setBackgroundColor:[UIColor whiteColor]];
    
    
}
- (IBAction)gamestart:(id)sender {
    type = 1;
        gamestartbutton = (UIButton *)sender;
    [gamestartbutton setBackgroundColor:[UIColor grayColor]];
   
    //if(levelbutton!=nil){
        [levelbutton setBackgroundColor:[UIColor whiteColor]];
    //}else if (createbutton!=nil){
        [createbutton setBackgroundColor:[UIColor whiteColor]];
    
    
}
- (IBAction)levelup:(id)sender {
    type = 2;
        levelbutton = (UIButton *)sender;
    [levelbutton setBackgroundColor:[UIColor grayColor]];
    
        [gamestartbutton setBackgroundColor:[UIColor whiteColor]];
    
        [createbutton setBackgroundColor:[UIColor whiteColor]];
    
    
}



- (void)touchesBegan:(NSSet*)touches withEvent:(UIEvent *)event{
    
    [self.view endEditing:YES];
    
}

-(void)showarltview{
    UIAlertView * alert = [[UIAlertView alloc]initWithTitle:@"提示" message:@"参数不能为空" delegate:self cancelButtonTitle:nil otherButtonTitles:@"确定", nil];
    [alert show];
}


@end
