//
//  ViewController.m
//  MyGuide
//
//  Created by Rafał Korżyński on 27/1/14.
//  Copyright (c) 2014 BLStream - Rafał Korżyński. All rights reserved.
//

#import "ViewController.h"
#import "AFParsedData.h"
#import "AFXMLParser.h"


@interface ViewController ()

@end

@implementation ViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    // **************************************************************
    // **************************************************************
    //
    // HERE CAN GO ANY NSLog BASED TEST IMPLEMENTATION ON PARSER ETC.
    //
    // **************************************************************
    // **************************************************************
    
    AFXMLParser *parser = [[AFXMLParser alloc] init];
    [parser parse];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

@end
