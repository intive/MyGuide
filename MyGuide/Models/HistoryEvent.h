//
//  HistoryEvent.h
//  MyGuide
//
//  Created by Kamil Lelonek on 4/13/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "Translatable.h"

@interface HistoryEvent : Translatable

@property (nonatomic) NSString *date;
@property (nonatomic) NSString *image;

@end