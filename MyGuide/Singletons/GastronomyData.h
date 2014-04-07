//
//  Gastronomy.h
//  MyGuide
//
//  Created by Kamil Lelonek on 4/7/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <Foundation/Foundation.h>

@interface GastronomyData : NSObject

@property (nonatomic, readonly, getter = localizeAnimalsArray) NSArray *restaurantsArray;
@property (nonatomic) NSArray *restaurantsPL;
@property (nonatomic) NSArray *restaurantsEN;

+ (id) sharedParsedData;
- (NSArray *) localizeRestaurantsArray;

@end