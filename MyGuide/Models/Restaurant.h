//
//  Restaurant.h
//  MyGuide
//
//  Created by Kamil Lelonek on 4/7/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Dish.h"

@interface Restaurant : NSObject

@property (nonatomic, readonly) NSString *name;
@property (nonatomic, readonly) NSString *openHours;
@property (nonatomic, readonly) NSMutableArray *dishes;

- (void) addNewDish: (Dish *) dish;

@end