//
//  GraphDrawer.h
//  MyGuide
//
//  Created by Kamil Lelonek on 5/23/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>
#import "AFNode.h"

@interface GraphDrawer : NSObject

+ (id) sharedInstance;
- (MKPolyline *) findShortestPathBetweenLocation: (CLLocation *)sourceLocation andLocation: (CLLocation *)destinationLocation;

@end
