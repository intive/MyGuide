#import "NSBubbleData.h"
#import <QuartzCore/QuartzCore.h>

@implementation NSBubbleData

const UIEdgeInsets textInsetsMine    = {5, 10, 11, 17};
const UIEdgeInsets textInsetsSomeone = {5, 15, 11, 10};

#pragma mark - Text bubble

- (id) initWithText:(NSString *)text date:(NSString *)date type:(NSBubbleType)type
{
    UIFont *font = [UIFont fontWithName: @"HelveticaNeue-Light" size: [UIFont systemFontSize]];
    CGRect rect = [(text ? text : @"") boundingRectWithSize: CGSizeMake(220, MAXFLOAT)
                                                    options: NSStringDrawingUsesLineFragmentOrigin | NSLineBreakByWordWrapping
                                                 attributes: @{ NSFontAttributeName: [UIFont systemFontOfSize:[UIFont systemFontSize]] }
                                                    context: nil];
    
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(0, 0, rect.size.width, rect.size.height)];
    label.numberOfLines = 0;
    label.lineBreakMode = NSLineBreakByWordWrapping;
    label.text = (text ? text : @"");
    label.font = font;
    label.backgroundColor = [UIColor clearColor];
    label.textColor = [UIColor whiteColor];
    
    UIEdgeInsets insets = (type == BubbleLeft ? textInsetsMine : textInsetsSomeone);
    return [self initWithView:label date:date type:type insets:insets];
}

#pragma mark - Image bubble

const UIEdgeInsets imageInsetsMine    = {11, 13, 16, 22};
const UIEdgeInsets imageInsetsSomeone = {11, 18, 16, 14};

- (id) initWithImage:(NSString *)imageName andDate:(NSString *)date andType:(NSBubbleType)type
{
    UIImage *image = [UIImage imageNamed: imageName];
    CGSize size = image.size;
    if (size.width > 220)
    {
        size.height /= (size.width / 220);
        size.width = 220;
    }
    
    UIImageView *imageView = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, size.width, size.height)];
    imageView.image = image;
    imageView.layer.cornerRadius  = 5.0;
    imageView.layer.masksToBounds = YES;
    
    UIEdgeInsets insets = (type == BubbleLeft ? imageInsetsMine : imageInsetsSomeone);
    return [self initWithView:imageView date:date type:type insets:insets];       
}

#pragma mark - Custom view bubble

- (id)initWithView:(UIView *)view date:(NSString *)date type:(NSBubbleType)type insets:(UIEdgeInsets)insets
{
    self = [super init];
    if (self)
    {
        _view = view;
        _date = date;
        _type = type;
        _insets = insets;
    }
    return self;
}

@end