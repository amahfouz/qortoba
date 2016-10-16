var assert = require('assert');
var esprima = require('esprima');


module.exports = JsClassParser;

//
// Class to parse a JavaScript class.
//
// Top down traversal of the program
// tree verifying syntax and generating
// a JSON representation of the class.
//


function JsClassParser(utfData) {

    // JSON representation of the class.
    var declsAsJson = { 'className': '', 'funcDecls': [] };

    verifyProgram(esprima.parse(utfData));

    return declsAsJson;

    //
    // Nested (private) functions
    //

    function verifyProgram(astRoot) {
        assert(astRoot);
        assert(astRoot.type == "Program", "Invalid program file.");
        verifyBody(astRoot.body);

        var genOptions = { 
            format : { 
                compact : true 
            }, 
            comment : true
        };
    }

    function verifyBody(body) {
        assert(body);
        assert(body.length > 1, "Must declare constructor and at least one function.");
        
        var ctr = body[0];
        assert(ctr);
        assert(ctr.type == "FunctionDeclaration", "Invalid constructor function.")

        var id = ctr.id;
        assert(id.type == "Identifier", "Expected identifier.");

        // record the name of the class 

        declsAsJson.className = id.name;

        // the rest of the body should be additions of functions to prototype 
        body.slice(1).forEach(function(value) {
            verifyPrototypeDecl(value);    
        });
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
        assert(obj.name == declsAsJson.className, "Incorrect classname: " + obj.name);
        assert(protoProp.property);
        assert(protoProp.property.type == "Identifier", "Expected 'prototype'.");
        assert(protoProp.property.name == "prototype", "Expected 'prototype'.");
    }

    // add the function body
    function instrumentRightSide(right, funcName) {
        assert(right.type == "FunctionExpression", "Expected function expressiont.");
        assert(right.params, "Expected params.");

        // create function decl

        var params = right.params;
        var funcDecl = { 'name' : funcName, 'params' : [] };

        declsAsJson.funcDecls.push(funcDecl);

        if (params) {
            params.forEach(function(val) {
                funcDecl.params.push(val.name);
            });
        }
    }
}