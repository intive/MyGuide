//
//  Restaurant.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/7/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "Restaurant.h"

@implementation Restaurant

- (instancetype)init
{
    self = [super init];
    if (self) {
        _dishes = [@[] mutableCopy];
    }
    return self;
}

- (void) addNewDish: (Dish *) dish {
    [_dishes addObject: dish];
}

@end