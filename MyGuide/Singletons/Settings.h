//
//  Settings.h
//  MyGuide
//
//  Created by squixy on 20.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <MapKit/MapKit.h>

@interface Settings : NSObject
@property (nonatomic, readonly) NSInteger innerRadius;
@property (nonatomic, readonly) NSInteger externalRadius;
@property (nonatomic, readonly) NSString *languageFallback;
@property (nonatomic, readonly) BOOL showAnimalsOnMap;
@property (nonatomic, readonly) BOOL showUserPosition;
@property (nonatomic, readonly) BOOL showPathsOnMap;
@property (nonatomic, readonly) BOOL showJunctionsOnMap;
@property (nonatomic, readonly) NSInteger centerRadius;
@property (nonatomic, readonly) double cameraMaxAltitude;
@property (nonatomic, readonly) double cameraMinAltitude;
@property (nonatomic, readonly, getter = calculateZooCenter) CLLocationCoordinate2D zooCenter;
@property (nonatomic, readonly, getter = calculateMapCenter) CLLocationCoordinate2D mapCenter;
@property (nonatomic, readonly, getter = calculateMaxSpan) MKCoordinateSpan maxSpan;
@property (nonatomic, readonly, getter = calculateMinSpan) MKCoordinateSpan minSpan;
@property (nonatomic, readonly, getter = calculateMapBounds) MKCoordinateRegion mapBounds;
@property (atomic, readonly) double maxUserDistance;

+ (id)   sharedSettingsData;
- (void) injectDataWithName: (NSString*) name andValue: (NSString*) value;
- (CLLocationCoordinate2D) calculateMapCenter;
- (CLLocationCoordinate2D) calculateZooCenter;
- (MKCoordinateRegion) calculateMapBounds;
- (MKCoordinateSpan) calculateMaxSpan;
- (MKCoordinateSpan) calculateMinSpan;

@end