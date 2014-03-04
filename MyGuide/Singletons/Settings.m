//
//  Settings.m
//  MyGuide
//
//  Created by squixy on 20.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "Settings.h"

@implementation Settings {
    int    _mapWidth;
    int    _mapHeight;
    double _zooLat;
    double _zooLng;
}

+ (id) sharedSettingsData {
    static Settings *sharedData = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedData = [[self alloc] init];
    });
    return sharedData;
}

- (id) init {
    self = [super init];
    if(self) {
        [self initDefaults];
    }
    return self;
}

- (void) initDefaults {
    _innerRadius      = 1;
    _externalRadius   = 2;
    _languageFallback = @"en";
    _showAnimalsOnMap = YES;
    _showUserPosition = YES;
    _showPathsOnMap = YES;
    _showJunctionsOnMap = YES;
}

- (void) injectDataWithName: (NSString*) name andValue: (NSString*) value {
    if ([name isEqualToString:       @"lang_fallback"]) {
        _languageFallback = [self normalize: value];
    }
    else if ([name isEqualToString:  @"internal_object_radius"]) {
        _innerRadius = [value integerValue];
    }
    else if ([name isEqualToString:  @"external_object_radius"]) {
        _externalRadius = [value integerValue];
    }
    else if ([name isEqualToString:  @"show_animals"]) {
        _showAnimalsOnMap = [value boolValue];
    }
    else if ([name isEqualToString:  @"map_show_user_position"]) {
        _showUserPosition = [value boolValue];
    }
    else if ([name isEqualToString:  @"map_show_paths"]) {
        _showPathsOnMap = [value boolValue];
    }
    else if ([name isEqualToString:  @"map_show_junctions"]) {
        _showJunctionsOnMap = [value boolValue];
    }
    else if ([name isEqualToString:  @"map_zoo_lat"]) {
        _zooLat = [value doubleValue];
    }
    else if ([name isEqualToString:  @"map_zoo_lng"]) {
        _zooLng = [value doubleValue];
    }
    else if ([name isEqualToString:  @"map_max_width"]) {
        _mapWidth = [value doubleValue];
    }
    else if ([name isEqualToString:  @"map_max_height"]) {
        _mapHeight = [value doubleValue];
    }
    else if ([name isEqualToString:  @"map_max_user_distance"]) {
        _maxUserDistance = [value doubleValue];
    }
}

- (NSString*) normalize: (NSString*) aString {
    return [aString stringByTrimmingCharactersInSet: [NSCharacterSet whitespaceAndNewlineCharacterSet]];
}

- (CLLocationCoordinate2D) calculateMapCenter {
    return CLLocationCoordinate2DMake(_zooLat, _zooLng);
}

- (MKCoordinateRegion) calculateMapBounds {
    return MKCoordinateRegionMakeWithDistance(self.mapCenter, _mapWidth, _mapHeight);
}

@end