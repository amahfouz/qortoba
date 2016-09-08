//
//  Qortoba.h
//  QordobaTest
//
//  Created by Ayman Mahfouz on 9/2/16.
//  Copyright © 2016 Ayman Mahfouz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface Qortoba : NSObject {
    
}

// returns a singleton instance

+(id)sharedInstance;

// attempts to handle the specified URL as an encoded invocation

-(BOOL)handleInvocation:(UIWebView*)webView
                 forUrl:(NSURL*)url;

// registers a service object with the specified name

-(void)registerService:(NSObject*)serviceObj
              withName:(NSString*)serviceName;

// deregisters the service with the specified name

-(void)deregisterService:(NSString*)serviceName;

@end
