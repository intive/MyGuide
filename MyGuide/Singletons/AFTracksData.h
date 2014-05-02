//
//  AFTracksData.h
//  MyGuide
//
//  Created by afilipowicz on 02.05.2014.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AFTracksData : NSObject

@property (nonatomic) NSArray *tracks;

+ (id)sharedParsedData;

@end
