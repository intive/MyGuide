//
//  Gastronomy.h
//  MyGuide
//
//  Created by Kamil Lelonek on 4/7/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GastronomyData : NSObject

@property (nonatomic) NSArray *restaurants;

+ (id) sharedParsedData;

@end