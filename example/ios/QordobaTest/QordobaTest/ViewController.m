//
//  ViewController.m
//  QordobaTest
//
//  Created by Ayman Mahfouz on 8/30/16.
//  Copyright © 2016 Ayman Mahfouz. All rights reserved.
//

#import "ViewController.h"
#import "Qortoba.h"
#import "QortobaClient.h"
#import "SampleService.h"

@interface ViewController ()

@end

@implementation ViewController

// handle button click 

- (IBAction)buttonClicked:(id)sender {

    // create a qortoba client to invoke angular services
    
    QortobaClient* client = [[QortobaClient alloc] initWithWebView:self.webView];
    
    // invoke the example angular service action with no params
    
    [client invokeAction:@"ExampleService" byName:@"action1" withParams:nil];
    
    // invoke the example angular service action with two params
    
    NSArray* params = [NSArray arrayWithObjects:@"p1", @"p2", nil];
    
    [client invokeAction:@"ExampleService" byName:@"action2" withParams:params];
    
}

// ViewController methods

- (void)viewDidLoad {
    
    [super viewDidLoad];
    
    // create the sample Qortoba service

    SampleService* service = [[SampleService alloc] init];
    
    // register the sample service with Qortoba
    
    [[Qortoba sharedInstance] registerService:service withName:@"sample"];
    
    // load the included index.html
    
    NSURL* url = [NSURL fileURLWithPath:[[NSBundle mainBundle]
                                         pathForResource:@"index"
                                         ofType:@"html"
                                         inDirectory:@"js"]
                            isDirectory:NO];
    
    NSLog(@"Loading index.html.");
    
    [self.webView loadRequest:[NSURLRequest requestWithURL:url]];
}

// UIWebView delegate methods

- (BOOL)webView:(UIWebView *)webView
    shouldStartLoadWithRequest:(NSURLRequest *)request
                navigationType:(UIWebViewNavigationType)navigationType {

    // This is how you integrate Qortoba with your web view.
    // You implement this method and delegate the call to Qortoba.
    // Qortoba returns true if it does not recognize the URL thereby
    // allowing the web view to proceed with loading.
    
    return [[Qortoba sharedInstance] handleInvocation:webView forUrl:request.URL];
}

@end
