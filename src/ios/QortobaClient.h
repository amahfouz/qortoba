//
//  QortobaClient.h
//  QordobaTest
//
//  Created by Ayman Mahfouz on 9/8/16.
//  Copyright © 2016 Ayman Mahfouz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>


@interface QortobaClient : NSObject

-(id)initWithWebView:(UIWebView*)webView;

/*!
  @brief Invokes an Angular service method by name.
 
  @param  serviceName Name of the Angular service as it appears in JS.
  @param  actionName Name of the Angular service method.
  @param  withParams Arguments for Angular service method.
 
  @code
      NSArray* paramsArr = nil;
      [client invokeAction:@"SomeService" byName:@"MethodName" withParams:paramsArr];
  @endcode
 
 @remark Target service must be reachable from root Angular scope.
 */
-(void)invokeAction:(NSString*)serviceName
             byName:(NSString*)actionName
         withParams:(NSArray*)params;

@end
