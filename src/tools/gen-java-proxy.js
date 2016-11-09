//
// Generates a Java class that proxies calls to 
// a JavaScript class using Qortoba client
//

// Usage:
//
// node gen-java-proxy.js file-containing-js-class.js
//

var fs = require('fs');
var path = require('path');
var handlebars = require('handlebars');
var JsClassParser = require('./js-class-parser');

// Parse command line

if (process.argv.length < 3) {
    console.log("Specify the name of the JavaScript file to parse on the command line.");
    process.exit(1);
}

// first two args are the path to node and the path to this script

fs.readFile(process.argv[2], 'utf8', function (err,data) {
  if (err) {
    return console.log(err);
  }

  // parse the class
  var declsAsJson = new JsClassParser(data);  
  var className = declsAsJson.className;

  // load the template
  var tplPath = path.join(__dirname, 'tpl', 'java-class.tpl.java');
  var javaClassTpl = fs.readFileSync(tplPath, 'utf8');

  var template = handlebars.compile(javaClassTpl);
  // create model
  var ctx = {
    'class-name' : declsAsJson.className,
    'methods'    : declsAsJson.funcDecls
  };

  // do the replacement
  var javaClass = template(ctx);

  console.log(javaClass);
});

