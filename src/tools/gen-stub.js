//
// Generates a stub code for invoking Qortoba from 
// a given JavaScript class.
//

var fs = require('fs');
var assert = require('assert');
var esprima = require('esprima');
var escodegen = require('escodegen');

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
  console.log(JSON.stringify(esprima.parse(data), null, 4));

  // verify the syntax of the program
  verifyProgram(esprima.parse(data));
});

//
// Functions to generate the stub.
//
// Top down traversal of the program
// tree verifying syntax
//

function verifyProgram(astRoot) {
    assert(astRoot);
    assert(astRoot.type == "Program", "Invalid program file.");
    verifyBody(astRoot.body);

    console.log(escodegen.generate(astRoot));
}

function verifyBody(body) {
    assert(body);
    assert(body.length > 1, "Must declare constructor and at least one function.");
    
    var ctr = body[0];
    verifyConstructor(ctr);

    var id = ctr.id;
    assert(id.type == "Identifier", "Expected identifier.");

    // record the name of the class 

    global.theClassName = id.name;

    // the rest of the body should be additions of functions to prototype 
    body.slice(1).forEach(function(value) {
        verifyPrototypeDecl(value);    
    });
}

function verifyConstructor(ctr) {
    assert(ctr);
    assert(ctr.type == "FunctionDeclaration", "Invalid constructor function.")
}

function verifyPrototypeDecl(decl) {
    assert(decl, "No prototype decl found.");
    assert(decl.type == "ExpressionStatement", "Invalid expression.");
    verifyExpression(decl.expression);
}

function verifyExpression(expr) {
    
    assert(expr.operator == "=", "Invalid operator.");
    assert(expr.type, "AssignmentExpression");
    
    var funcName = verifyLeftSide(expr.left);
    instrumentRightSide(expr.right, funcName);
}

function verifyLeftSide(left) {
    assert(left.type == "MemberExpression");
    verifyClassPrototypeProperty(left.object);
    assert(left.property);
    assert(left.property.type == "Identifier", "Expected identifier.");
    
    var funcName = left.property.name;
    assert(funcName);

    return funcName;
}

function verifyClassPrototypeProperty(protoProp) {
    assert(protoProp.type == "MemberExpression");
    var obj = protoProp.object;

    assert(obj);
    assert(obj.type == "Identifier", "Expected identifier.");
    assert(obj.name == global.theClassName, "Incorrect classname: " + obj.name);
    assert(protoProp.property);
    assert(protoProp.property.type == "Identifier", "Expected 'prototype'.");
    assert(protoProp.property.name == "prototype", "Expected 'prototype'.");
}

function instrumentRightSide(right, funcName) {
    assert(right.type == "FunctionExpression", "Expected function expressiont.");
    assert(right.params, "Expected params.");

    // construct the new function body

    var newCode = "QortobaService.exec(\"" + theClassName + "\", \"" + funcName + "\", ";
    var params = right.params;

    if (params) {
        newCode = newCode + "[";
        params.forEach(function(val, index, arr) {
            newCode = newCode + val.name;
            if (index < arr.length - 1)
                newCode = newCode + ", ";
        });
        newCode = newCode + "]";
    }
    else {
        newCode = newCode + "null";
    }
    newCode = newCode + ");";

    // insert the new code

    right.body.body.push(esprima.parse(newCode));
}

