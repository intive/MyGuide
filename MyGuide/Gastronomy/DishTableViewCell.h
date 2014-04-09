//
//  DishTableViewCell.h
//  MyGuide
//
//  Created by Kamil Lelonek on 4/7/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import <UIKit/UIKit.h>

@interface DishTableViewCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UIImageView *imageLogo;
@property (weak, nonatomic) IBOutlet UILabel     *labelName;
@property (weak, nonatomic) IBOutlet UILabel     *labelPrice;

@end