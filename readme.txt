PROJECT TITLE: COMP - Project 1
GROUP: 2F

NAME1: André Moutinho, NR1: up201707291, GRADE1: 18, CONTRIBUTION1: 29%
NAME2: Sérgio Dias, NR2: up201704889, GRADE2: 18, CONTRIBUTION2: 29%
NAME3: Cláudia Mamede, NR3: up201604832, GRADE3: 18, CONTRIBUTION3: 29%
NAME4: Luís Marques, NR4: 201104354, GRADE4: -, CONTRIBUTION4: -
...

GLOBAL Grade of the project: 17

SUMMARY: 

This project was developed within the scope of Compilers course of the Integrated Master in Informatics and
Computer Engineering of the Faculty of Engineering of the University of Porto. 
It consists of compiler for Java--, a subset of the Java programming language.
The compiler validates the grammar syntax, semantic and generates low level code (with or without optimizations, as specified below).

EXECUTE:

From the root of the project, use 'gradle build' to compile the program. 
After that, use 'java -jar comp2020-2f.jar <path-to-jmm-file> [-Ierror] [-r=<N>] [-o]
The generated .j file will be located at jvm folder. 

DEALING WITH SYNTACTIC ERRORS: 

Syntatic errors are being handled inside the while condition as per requested in the first checkpoint. 
The tool only exits after it encounters 10 errors. Anyway, if at least one error is detected after parsing the code, the program throws an exception.

SEMANTIC ANALYSIS: 

In the file README.md there is a checklist with the semantic rules that our tool implements.
Moreover, there are some rules not mentioned in the checklist:
 - It is possible to use continue and break inside a while loop (proper verification is executed);
 - When methods are being called in a chain, there is a verification to check if the return of the method is of the class type in order to be able to call another method;
 - The flag "-Ierror" controls whether variables that might not have been initialized should appear as errors or warnings. This is included in the other checklist, but we thought we should point it out.

INTERMEDIATE REPRESENTATIONS (IRs): 

Although we didn't implement an IR for code generation, we implemented one for the -r optimization. This IR is represented as a HashMap that has "MethodForms" (name and argument types of a method) associated with a "MethodLines". The latter is a class that stores all the code lines contained in the respective method. The code lines are represented by the class "CodeLine" which has information about its predecessors, antecessors, uses, defs, live-in and live-out.

CODE GENERATION: 

While going through the nodes (starting from the root), the "CodeGenerator.java" ensures that the appropriate JVM instructions are generated for each one of nodes. This class mainly uses the information provided by the SymbolTable and the SimpleNode class for variable type, name and values checks.
It uses low cost instructions such as "iinc" and the following instructions depending on the integer value when loading a variable: 'iconst_m1', 'iconst_', 'bipush', 'sipush' and 'ldc'.
To ensure the compiler uses only the necessary stack space, for each operation the cost is calculated and the result (positive or negative) is added to the stack never dropping bellow zero.
Every time the stack reaches a maximum size, that value is the one used as the maximum stack size.
The calculation of the locals takes in consideration if the method is static or not. 
The only static method in this language is the main method and so the first variable will be the argument 'String[] args', followed by the local variables.
For methods non static the first variable is the objectref 'this' followed by the arguments and the local variables.
In addition to what was done to complete the checklist, the generated code also allows single if instructions (without else blocks), uses while templates by default and has implemented a short circuit evaluation for the logical operation AND.
When all instructions are generated, the .j file is created (with same name as the class name of the .jmm) and saved in the "jvm" folder.

OVERVIEW: 

As for the main features, most of the code necessary to build the symbol table and implement the semantic verification is located in the class "SemanticAnalyzer" and the code generation is located in the class "CodeGenerator". As for the parser and tree generation, all of the code can be encountered at the javacc folder. 
The code implemented optimizations is mostly distributed among the files "OptimizationR.java", "OptimizationO.java" and the "CodeGenerator.java".
We implement the graph coloring algorithm studied in class to handle the -r optimization.
Our project does not have third-party tools nor packages. 

TASK DISTRIBUTION:

Checkpoint I:

Every member worked on the parser equally.

Checkpoint II:

André Moutinho: Symbol table construction and Semantic Analysis
Sérgio Dias: Symbol table construction and Semantic Analysis
Cláudia Mamede: Symbol table construction and Code Generation
Luís Marques: Code Generation support and arithmetic expressions

Checkpoint III:

André Moutinho: Code generation for boolean operations and check if variables might not have been initialized (also implemented option to treat it as error or warning)
Sérgio Dias: Code generation for boolean operations and if conditions (if, if-else and if-elseif blocks)
Cláudia Mamede: Code generation for array accesses and while loops.
Luís Marques: Stack and locals limits calculations and code generation support

After Checkpoint III:

André Moutinho: Optimization -r
Sérgio Dias: Optimization -r
Cláudia Mamede: Short circuit evaluation for AND operations and optimization -o for while loops
Luís Marques: Improvements on stack limits calculations and optimization -o for variables

PROS:
 - Global lookahead of 1 for the parser; 
 - Semantic analysis is very complete (checklist is 100%), being error-proof to all the provided tests (plus some more of our own);
 - Added break and continue expressions to the original grammar, thus improving the versatility of the language;
 - Added alone if statements, thus avoiding the need to create an empty else block in some cases;
 - Appropriate error treatment, with an exception containing information about the row/column where the error was spotted;
 - "-Ierror" flag for treatment as warning or error;
 - Optimized instruction selection to load a constant and use of iinc when possible;
 - Register Allocation (-r optimization);
 - Uses while templates and remove unnecessary "goto" instructions (-o optimization);
 - Short circuit evaluation for logical operations;
 - Handles jasmin keywords.

CONS: 
 - There's room for improvement when it comes to code organization, although refactoring was frequently carried out;
 - Jasmin keywords handling could be more effective and extensive.









