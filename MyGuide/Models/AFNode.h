//
//  AFNode.h
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>

@interface AFNode : NSObject

@property (nonatomic, readonly) double latitude;
@property (nonatomic, readonly) double longitude;

- (id) initWithLatitude: (double)latitude andLongitude:(double)longitude;
- (NSInteger) distanceFromLocation: (CLLocation *)location;

@end
