//
//  SettingsParserTest.m
//  MyGuide
//
//  Created by squixy on 22.02.2014.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import <XCTest/XCTest.h>
#import "SettingsParser.h"
#import "Settings.h"

@interface SettingsParserTest : XCTestCase {
    Settings        *settings;
    SettingsParser  *parser;
}
@end

@implementation SettingsParserTest

- (void)setUp
{
    [super setUp];
    settings = [Settings sharedSettingsData];
    parser   = [[SettingsParser alloc] init];
}

- (void)testShouldLoadSettings
{
    XCTAssertEqual(settings.innerRadius, (NSInteger)1);
    XCTAssertEqual(settings.externalRadius, (NSInteger)2);
    XCTAssertEqualObjects(settings.languageFallback, @"pl");
}
@end