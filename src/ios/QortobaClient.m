//
//  QortobaClient.m
//  QordobaTest
//
//  Created by Ayman Mahfouz on 9/8/16.
//  Copyright © 2016 Ayman Mahfouz. All rights reserved.
//

#import "QortobaClient.h"

/*!
    @brief Template for JavaScript to be filled and passed to the web view
 */
static NSString *const INVOCATION_TEMPLATE
    = @"qortoba.callback(1.0, \"wl-service-name\", \"wl-method-name\", \"wl-params-array\");void(0);";

@implementation QortobaClient {
    UIWebView* theWebView;
}

-(id)initWithWebView:(UIWebView*)webView {
    if (self = [super init]) {
        theWebView = webView;
    }
    return self;
}

-(void)invokeAction:(NSString*)serviceName
             byName:(NSString*)actionName
         withParams:(NSArray*)params {
    
    NSMutableString* js = [NSMutableString stringWithString:INVOCATION_TEMPLATE];
    
    // insert service name
    
    [js replaceOccurrencesOfString:@"wl-service-name"
                        withString:serviceName
                           options:NSLiteralSearch
                             range:NSMakeRange(0, [js length])];

    // insert method name
    
    [js replaceOccurrencesOfString:@"wl-method-name"
                        withString:actionName
                           options:NSLiteralSearch
                             range:NSMakeRange(0, [js length])];
    
    // serialize params, if any
    
    NSString* paramsAsStr;
    
    if (params) {
    
        NSError* jsError;
        NSData* jsonData = [NSJSONSerialization dataWithJSONObject:params
                                                           options:kNilOptions
                                                             error:&jsError];

        if (jsError)
            [NSException raise:@"Failed to invoke service."
                        format:@"Unable to serialize parameters: (%@).", [jsError localizedDescription]];
        
        paramsAsStr = [[NSString alloc] initWithData:jsonData
                                            encoding:NSUTF8StringEncoding];

    }
    else
        paramsAsStr = @"[]";
    
    // insert params
    
    [js replaceOccurrencesOfString:@"wl-params-array"
                        withString:paramsAsStr
                           options:NSLiteralSearch
                             range:NSMakeRange(0, [js length])];
    
    // finally, run the javascript
    
    NSLog(@"Executing JS: %@", js);
    
    [theWebView stringByEvaluatingJavaScriptFromString:js];
}

@end