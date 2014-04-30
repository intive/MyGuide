//
//  Ticket.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/26/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "Ticket.h"

@implementation Ticket

- (NSString *)description {
    return [NSString stringWithFormat: @"%@ %@", [self getName], self.price];
}

@end
