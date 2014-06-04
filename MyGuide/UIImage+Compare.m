//
//  UIImage+UIImage_Compare.m
//  MyGuide
//
//  Created by afilipowicz on 28.05.2014.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "UIImage+Compare.h"

@implementation UIImage (Compare)

- (BOOL)isEqualToImage:(UIImage *)image
{
    NSData *data1 = UIImagePNGRepresentation(self);
    NSData *data2 = UIImagePNGRepresentation(image);
    
    return [data1 isEqual:data2];
}

@end
