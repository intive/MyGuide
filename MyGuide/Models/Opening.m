//
//  Opening.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/26/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "Opening.h"

NSString * const keyWeekdays = @"keyWeekdays";
NSString * const keyWeekends = @"keyWeekends";

@implementation Opening

- (id) init
{
    self = [super init];
    if(self) {
        _hours = [NSMutableDictionary dictionaryWithDictionary: @{ keyWeekdays: @"", keyWeekends: @"" }];
    }
    return self;
}

- (void) setWeekdaysHours: (NSString *) weekdaysHours
{
    self.hours[keyWeekdays] = weekdaysHours;
}

- (void) setWeekendsHours: (NSString *) weekendsHours
{
    self.hours[keyWeekends] = weekendsHours;
}

@end
