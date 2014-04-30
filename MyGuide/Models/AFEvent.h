//
//  AFEvent.h
//  MyGuide
//
//  Created by afilipowicz on 29.04.2014.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AFEvent : NSObject

@property (nonatomic) NSString *namePL;
@property (nonatomic) NSString *nameEN;
@property (nonatomic) NSString *time;
@property (nonatomic) NSString *timeWeekends;
@property (nonatomic) NSString *timeChristmas;
@property (nonatomic) NSString *image;

@end
