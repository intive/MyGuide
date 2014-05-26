//
//  Restaurant.h
//  MyGuide
//
//  Created by Kamil Lelonek on 4/7/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Dish.h"
#import "Translatable.h"

@interface Restaurant : Translatable

@property (nonatomic) NSString *openHours;
@property (nonatomic) NSMutableArray *dishes;
@property (nonatomic) double latitude;
@property (nonatomic) double longitude;

- (id) initWithLatitude: (NSString *)latitude andLongitude: (NSString *)longitude;
- (void) addNewDish: (Dish *) dish;

@end