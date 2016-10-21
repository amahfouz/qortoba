//
// Generates a stub code for invoking Qortoba from 
// a given JavaScript class.
//

var fs = require('fs');
var JsClassParser = require('./js-class-parser');

// Parse command line

if (process.argv.length < 3) {
    console.log("Specify the name of the file to parse on the command line.");
    process.exit(1);
}

// first two args are the path to node and the path to this script

fs.readFile(process.argv[2], 'utf8', function (err,data) {
  if (err) {
    return console.log(err);
  }
  //console.log(JSON.stringify(esprima.parse(data), null, 4));

  // verify the syntax of the program
  var declsAsJson = new JsClassParser(data);  

  console.log(JSON.stringify(declsAsJson));

  var className = declsAsJson.className;
  var prog = "function " + className + "() { }";

  function commaSeparatedParams(paramsArr) {
        if (! paramsArr)
            return null;

        // non-null params
        var result = "";
        paramsArr.forEach(function(param, index, arr) {
            result = result + param;
            if (index < arr.length - 1)
                result = result + ",";
        });    
        return result;
  }

  declsAsJson.funcDecls.forEach(function(decl) {

        var funcName = decl.name;
        var csParams = commaSeparatedParams(decl.params);

        prog = prog + "\n";
        prog = prog + className + ".prototype." + funcName + " = function(";

        // add formal arguments

        if (csParams)
            prog = prog + csParams;
     
        prog = prog + ") {\n";

        // append the Qortoba call

        var newCode = "    QortobaService.exec('" + className + "', '" + funcName + "', ";

        if (csParams) {
            newCode = newCode + "[";
            newCode = newCode + csParams;
            newCode = newCode + "]";
        }
        else {
            newCode = newCode + "null";
        }
        newCode = newCode + ");";

        prog = prog + newCode + '\n}'
      });
  
  console.log(prog);

});

