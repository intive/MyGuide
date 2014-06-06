//
//  AccessViewController.m
//  MyGuide
//
//  Created by Kamil Lelonek on 6/6/14.
//  Copyright (c) 2014 - Open Source (Apache 2.0 license). All rights reserved.
//

#import "AccessViewController.h"
#import "InformationData.h"
#import "Settings.h"

@interface AccessViewController ()

@property (nonatomic) InformationData *data;

@end

@implementation AccessViewController

- (id)initWithCoder: (NSCoder *)coder
{
    self = [super initWithCoder:coder];
    if (self) {
        _data = [InformationData sharedParsedData];
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.labelAddress.text = self.data.address;
    self.textViewPark.text = [self.data.parkingInformation getName];
    [self prepareTramsAndBusses];
    [self prepareMapView];
}

#pragma mark - Bussess and Trams

- (void)prepareTramsAndBusses
{
    [self renderScrollView:self.scrollViewBuses withCollection:[self.data.buses componentsSeparatedByString:@", "]];
    [self renderScrollView:self.scrollViewTrams withCollection:[self.data.trams componentsSeparatedByString:@", "]];
}

- (void)renderScrollView: (UIScrollView *)scrollView withCollection: (NSArray *)collection
{
    int x = 0;
    for (int i = 0; i < collection.count; i++) {
        UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(x, 0, 40, 30)];

        label.text          = collection[i];
        label.textAlignment = NSTextAlignmentCenter;
        label.font          = [UIFont fontWithName:@"HelveticaNeue-Light" size:16];
        
        label.layer.cornerRadius = 5.0;
        label.layer.borderColor  = [UIColor blackColor].CGColor;
        label.layer.borderWidth  = 1.0;
        
        [scrollView addSubview:label];
        x += label.frame.size.width + 10;
    }
    
    scrollView.contentSize = CGSizeMake(x, scrollView.frame.size.height);
}

#pragma mark - MapView

- (void)prepareMapView
{
    self.mapView.mapType  = MKMapTypeHybrid;
    self.mapView.delegate = self;
    Settings *settings = [Settings sharedSettingsData];
    CLLocationCoordinate2D coordinates = CLLocationCoordinate2DMake(settings.zooCenter.latitude, settings.zooCenter.longitude);
    [self addZooPin: coordinates];
    [self zoomOnZoo: coordinates];
}

- (void)addZooPin: (CLLocationCoordinate2D)coordinates
{
    [self.mapView removeAnnotations: self.mapView.annotations];
    MKPointAnnotation *annotationPoint  = [MKPointAnnotation new];
    annotationPoint.title               = @"ZOO Wrocław";
    annotationPoint.coordinate          = coordinates;
    [self.mapView addAnnotation: annotationPoint];
}

- (void)zoomOnZoo: (CLLocationCoordinate2D)coordinates
{
    MKCoordinateSpan span = MKCoordinateSpanMake(180 / pow(2, 14) * self.mapView.frame.size.height / 256, 0);
    [self.mapView setRegion: MKCoordinateRegionMake(coordinates, span) animated: YES];
}

- (MKAnnotationView *)mapView:(MKMapView *)mapview viewForAnnotation:(id <MKAnnotation>)annotation
{
    if ([annotation isKindOfClass:[MKUserLocation class]]) return nil;
    
    static NSString *AnnotationIdentifier = @"ZOO";
    MKAnnotationView *annotationView = [self.mapView dequeueReusableAnnotationViewWithIdentifier:AnnotationIdentifier];
    
    if(annotationView) return annotationView;
    else
    {
        MKAnnotationView *annotationView = [[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:AnnotationIdentifier];
        annotationView.image = [UIImage imageNamed:@"pinOrange"];
        return annotationView;
    }
    return nil;
}

- (void)viewDidAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    self.parentViewController.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"mapType"] style:UIBarButtonItemStylePlain target:self action:@selector(changeMapType)];
}

- (void)changeMapType
{
    self.mapView.mapType = self.mapView.mapType == MKMapTypeStandard ? MKMapTypeHybrid : MKMapTypeStandard;
}

- (void) viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    self.parentViewController.navigationItem.rightBarButtonItem = nil;
}

@end
