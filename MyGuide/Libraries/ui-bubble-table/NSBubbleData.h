#import <Foundation/Foundation.h>

typedef enum _NSBubbleType
{
    BubbleLeft  = 0,
    BubbleRight = 1
} NSBubbleType;

@interface NSBubbleData : NSObject

@property (readonly, nonatomic, strong) NSString *date;
@property (readonly, nonatomic, strong) UIView   *view;
@property (readonly, nonatomic) NSBubbleType type;
@property (readonly, nonatomic) UIEdgeInsets insets;
@property (nonatomic, strong)   UIImage *avatar;

- (id) initWithText:(NSString *)text date:(NSString *)date type:(NSBubbleType)type;
- (id) initWithImage:(NSString *)imageName andDate:(NSString *)date andType:(NSBubbleType)type;
- (id) initWithView:(UIView *)view date:(NSString *)date type:(NSBubbleType)type insets:(UIEdgeInsets)insets;

@end