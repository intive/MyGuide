//
//  LocationManager.h
//  MyGuide
//
//  Created by Kamil Lelonek on 3/22/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <CoreLocation/CoreLocation.h>
#import <Foundation/Foundation.h>
#import "AFTrack.h"

@interface LocationManager : NSObject <CLLocationManagerDelegate>

+ (id) sharedLocationManager;
- (void)requestLocationStatus;
- (void)loadTrackRegionsToMonitor:(AFTrack *)currentTrack;
- (void)clearMonitoredTrack;

@end