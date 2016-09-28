//
//  SampleService.m
//  QordobaTest
//
//  Created by Ayman Mahfouz on 9/8/16.
//  Copyright © 2016 Ayman Mahfouz. All rights reserved.
//

#import "SampleService.h"
#import "Qortoba.h"

@implementation SampleService

-(id)init {
    return self;
}

-(void)action1 {
    NSLog(@"action 1 invoked.");
}

-(void)action2:(NSArray*)params {
    
    NSLog(@"action 2 invoked with params (%@ , %@).", [params objectAtIndex:0], [params objectAtIndex:1]);
}

@end
