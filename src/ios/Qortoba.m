//
//  Qortoba.m
//  QordobaTest
//
//  Created by Ayman Mahfouz on 9/2/16.
//  Copyright © 2016 Ayman Mahfouz. All rights reserved.
//

#import "Qortoba.h"

@implementation Qortoba {
    
    // maps service name to service object
    NSMutableDictionary* services;
}

/////////////////////////////////////////////////////////////

+(id)sharedInstance {
    static Qortoba* theSharedInstance = nil;
    
    // init the singleton instance only once
    
    @synchronized(self) {
        if (theSharedInstance == nil) {
            theSharedInstance = [[self alloc] init];
            theSharedInstance->services = [NSMutableDictionary dictionary];
        }
    }
    return theSharedInstance;
}


/////////////////////////////////////////////////////////////

-(void)registerService:(NSObject*)serviceObj
              withName:(NSString*)serviceName {
    
    // check if a service with the same name exists
    
    NSObject* existing = [services objectForKey:serviceName];
    
    if (existing)
        [NSException raise:@"Service already registered."
                    format:@"Service with name '%@' already registered.", serviceName];
    
    
    [services setValue:serviceObj forKey:serviceName];
}

/////////////////////////////////////////////////////////////

-(void)deregisterService:(NSString*)serviceName {
    
    [services removeObjectForKey:serviceName];
}

/////////////////////////////////////////////////////////////

-(BOOL)handleInvocation:(UIWebView*)webView
                 forUrl:(NSURL*)url {
    
    // handle only URLs whose protocol is "qortoba"
    
    if (![url.scheme isEqualToString:@"qortoba"]) {
        return YES;
    }
    
    // extract service, action, and params
    
    NSString* hostStr = [url host];
    NSArray* serviceAndAction = [hostStr componentsSeparatedByString:@"."];
    
    if ([serviceAndAction count] != 2) {
        NSLog(@"Host string (%@) not valid.", hostStr);
        return NO;
    }
    
    NSString* serviceName = [serviceAndAction objectAtIndex:0];
    NSString* actionName = [serviceAndAction objectAtIndex:1];
    
    NSObject* service = [services objectForKey:serviceName];
    
    if (! service)
        [NSException raise:@"Error invoking service."
                    format:@"Service not found for name (%@).", serviceName];
    
    SEL selector = NSSelectorFromString(actionName);
    
    if (! selector)
        [NSException raise:@"Error invoking action."
                    format:@"Action not found for name (%@).", actionName];
    
    
    // find the selector and create an invocation
    
    NSMethodSignature* signature  = [service methodSignatureForSelector:selector];
    NSInvocation* invocation = [NSInvocation invocationWithMethodSignature:signature];
    
    // associate invocation with an instance
    
    [invocation setTarget:service];
    
    // read and decode parameters array string
    
    NSString* queryStr = [url query];
    NSData* queryData = [queryStr dataUsingEncoding:NSUTF8StringEncoding];
    
    NSError *jsonParseError = nil;
    id jsonObject = [NSJSONSerialization JSONObjectWithData:queryData
                                                    options:kNilOptions
                                                      error:&jsonParseError];
    
    if (! [jsonObject isKindOfClass:[NSArray class]])
        [NSException raise:@"Error invoking service."
                    format:@"Query string not valid JSON array (%@).", queryStr];
    
    if (jsonParseError) {
        NSLog(@"Error parsing service parameters string (%@).", queryStr);
        return NO;
    }
    
    // set the params on the invocation object
    
    NSArray* paramsArr = jsonObject;
    NSString* param;
    NSInteger paramIndex = 0;
    for (param in paramsArr) {
        // The +2 is because 0 and 1 are 'self' and '_cmd'
        id paramObj = [paramsArr objectAtIndex:(paramIndex + 2)];
        [invocation setArgument:&paramObj
                        atIndex:paramIndex];
    }
    
    // finally, invoke
    
    [invocation invoke];
    
    return YES;
}

@end
