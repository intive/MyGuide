//
//  InformationData.h
//  MyGuide
//
//  Created by Kamil Lelonek on 4/26/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Translatable.h"

@interface InformationData : NSObject

#pragma mark - Openings
@property (nonatomic) NSArray      *openings;
@property (nonatomic) Translatable *openingInformation;

#pragma mark - Tickets
@property (nonatomic) NSArray      *tickets;
@property (nonatomic) Translatable *ticketsInformation;

# pragma mark - Contact
@property (nonatomic) NSString *website;
@property (nonatomic) NSString *telephone;
@property (nonatomic) NSString *address;
@property (nonatomic) NSArray  *emails;

# pragma mark - Access
@property (nonatomic) NSString     *trams;
@property (nonatomic) Translatable *parkingInformation;

+ (id) sharedParsedData;

@end
