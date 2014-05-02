//
//  AFTrack.m
//  MyGuide
//
//  Created by afilipowicz on 02.05.2014.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "AFTrack.h"

@interface AFTrack ()

@property (nonatomic) NSMutableDictionary *description;
@property (nonatomic) NSMutableDictionary *name;
@property (nonatomic) NSString  *currentLocale;

@end

@implementation AFTrack

- (id)init
{
    self = [super init];
    if(self) {
        _description = [NSMutableDictionary dictionaryWithDictionary:
                     @{
                       @"en" : @"",
                       @"pl" : @""
                       }
                     ];
        _name = [NSMutableDictionary dictionaryWithDictionary:
                @{
                  @"en" : @"",
                  @"pl" : @""
                  }
                ];
        _currentLocale = [[NSLocale preferredLanguages] objectAtIndex: 0];
        _image         = @"";
        _animalsArray  = @[];
        _progressText  = @"0/55";
        _progressBase  = 0;
        _progressRatio = 0.0f;
    }
    return self;
}

- (void)setDescription:(NSString *)description withLanguage:(NSString *)language
{
    self.description[language] = description;
}

- (NSString *)getDescriptionForLanguage:(NSString *)language
{
    return self.description[language];
}

- (NSString *)getDescription
{
    return [self.currentLocale isEqualToString: @"pl"] ? self.description[@"pl"] : self.description[@"en"];
}

- (void)setName:(NSString *)name withLanguage:(NSString *)language
{
    self.name[language] = name;
}

- (NSString *)getNameForLanguage:(NSString *)language
{
    return self.name[language];
}

- (NSString *)getName
{
    return [self.currentLocale isEqualToString: @"pl"] ? self.name[@"pl"] : self.name[@"en"];
}

- (id)initWithCoder:(NSCoder *)aDecoder
{
    self = [super init];
    if(self){
        self = [self init];
        
        self.name          = [aDecoder decodeObjectForKey:@"name"];
        self.description   = [aDecoder decodeObjectForKey:@"description"];
        self.currentLocale = [aDecoder decodeObjectForKey:@"currentLocale"];
        self.image         = [aDecoder decodeObjectForKey:@"image"];
        self.animalsArray  = [aDecoder decodeObjectForKey:@"animalsArray"];
        self.progressBase  = [aDecoder decodeIntegerForKey:@"progressBase"];
        self.progressRatio = [aDecoder decodeFloatForKey:@"progressRatio"];
        self.progressText  = [aDecoder decodeObjectForKey:@"progressText"];
    }
    return self;
}
- (void)encodeWithCoder:(NSCoder *)aCoder{
    [aCoder encodeObject:self.name          forKey:@"name"];
    [aCoder encodeObject:self.description   forKey:@"description"];
    [aCoder encodeObject:self.currentLocale forKey:@"currentLocale"];
    [aCoder encodeObject:self.image         forKey:@"image"];
    [aCoder encodeObject:self.animalsArray  forKey:@"animalsArray"];
    [aCoder encodeObject:self.progressText  forKey:@"progressText"];
    [aCoder encodeInteger:self.progressBase forKey:@"progressBase"];
    [aCoder encodeFloat:self.progressRatio  forKey:@"progressRatio"];
}

- (void)setAnimalsArray:(NSArray *)animalsArray
{
    _animalsArray = animalsArray;
    _progressText = [NSString stringWithFormat:@"%ld/%ld", (long)self.progressBase, (unsigned long)self.animalsArray.count];
    if(_progressRatio == 0.0f && _animalsArray.count != 0) {
        _progressRatio = (float)(_progressBase / _animalsArray.count);
    }
}

@end