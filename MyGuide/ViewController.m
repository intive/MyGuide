//
//  ViewController.m
//  MyGuide
//
//  Created by Rafał Korżyński on 27/1/14.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "ViewController.h"
#import "SettingsParser.h"

@interface ViewController ()

@end

@implementation ViewController

- (void) loadSettings: (id) object {
    SettingsParser *parser = [[SettingsParser alloc] init];
    [parser loadSettings];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self performSelectorInBackground:@selector(loadSettings:) withObject:nil];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
