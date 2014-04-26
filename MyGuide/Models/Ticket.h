//
//  Ticket.h
//  MyGuide
//
//  Created by Kamil Lelonek on 4/26/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Translatable.h"

typedef NS_ENUM(NSInteger, TicketKind) {
    Group,
    Individual
};

@interface Ticket : Translatable

@property (nonatomic) TicketKind ticketKind;
@property (nonatomic) NSNumber   *price;

@end
