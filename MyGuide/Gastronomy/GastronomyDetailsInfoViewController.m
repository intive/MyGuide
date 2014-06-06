//
//  GastronomyDetailsInfoViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 4/6/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "GastronomyDetailsInfoViewController.h"

#define LOREM_IPSUM @"Restauracja Valdi Plus przyciąga niepowtarzalnym klimatem i doskonałym jedzeniem. Nasi kucharze oraz kelnerzy zadbają o Państwa dobry nastrój. Potrawy przyrządzane od podstaw w oparciu o naturalne i świeże produkty zaspokajają od lat podniebienia naszych gości. Doskonałym zakończeniem posiłku jest deser! Szeroki wybór ciast wykonanych według tradycyjnych receptur, deserów lodowych oraz sezonowych zadowoli każdego smakosza słodyczy."

@implementation GastronomyDetailsInfoViewController

-(void) viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self setText: LOREM_IPSUM];
}

- (void)setText: (NSString *)text
{
    NSString *html = [NSString stringWithFormat:
                      @"<div style=\"font-family: 'HelveticaNeue-Light';\" align='justify'>%@<div>", text];
    [self.webView loadHTMLString:html baseURL:nil];
}

@end