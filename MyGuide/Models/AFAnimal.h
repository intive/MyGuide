//
//  AFAnimal.h
//  MyGuide
//
//  Created by afilipowicz on 18.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "AFNode.h"

@interface AFAnimal : NSObject

@property (nonatomic, readonly) AFNode   *coordinates;
@property (nonatomic, readonly) NSString *name;

- (id) init;
- (id) initWithPosition: (AFNode   *) coordinates
                andName: (NSString *) name;

- (void) setCoordinates: (AFNode *) coordinates;
- (void) setName:      (NSString *) name;

@end
