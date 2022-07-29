
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;

public class Main {
	public static int noErrors = 0;

	public static void main(String args[]) throws ParseException {
		int n = -1;
		boolean optimizationO = false;
		boolean treatNotInitedVarAsError = false;
		for (int i = 1; i < args.length; i++) {
			if (args[i].equals("-Ierror"))
				treatNotInitedVarAsError = true;
			else if(args[i].startsWith("-r")) {
				try {
					n = Integer.parseInt(args[i].substring(3));
				} catch (NumberFormatException e) {
					throw new ParseException("-r optimization must be called with an integer! Format: -r=<n>", 0);
				}
			} else if (args[i].equals("-o")) {
				optimizationO = true;
			} else {
				throw new ParseException("Wrong argument type!", 0);
			}
		}

		System.out.println("\t\t [ OPTIMIZATION -O: " + optimizationO + " ] \n");

		Parser myParser = null;
		try {
			myParser = new Parser(new java.io.FileInputStream(args[0]));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		SimpleNode root = null;
		try {
			root = myParser.Main(); // returns reference to root node
		} catch (Exception e) {
			System.out.println("ERROS: " + noErrors);
			if (noErrors > 0) {
				noErrors = 0;
			}
			throw new RuntimeException(e.toString().substring(16));
		}
		if (noErrors > 0) {
			noErrors = 0;
			throw new RuntimeException();
		}
		fixTree(root, 0);
		root.dump(""); // prints the tree on the screen
		System.out.println("\n==========================================\n");

		SemanticAnalyzer analyzer;
		boolean printSymbolTable = false;

		/* If true, use of not initialized variables will result in an error, otherwise just a warning.
		 * Making it false will cause some tests to fail, since they are excepting for an exception to be thrown.
		 */

		try {
			analyzer = new SemanticAnalyzer(root, treatNotInitedVarAsError);
			if (printSymbolTable) {
				analyzer.symbolTable.print();
				System.out.println("\n==========================================\n");
			}
		} catch (ParseException e) {
			System.err.println(e.toString());
			throw new RuntimeException();
		}
		if (n != -1)
			new OptimizationR(root, analyzer.symbolTable, n);
		new CodeGenerator(root, analyzer.symbolTable, optimizationO);
	}

	public static void fixTree(SimpleNode node, int childNo) {
		if (node.toString().equals("Parenthesis")) {
			SimpleNode firstChild = (SimpleNode) node.jjtGetChild(0);
			if (node.jjtGetNumChildren() == 2) {
				SimpleNode secondChild = (SimpleNode) node.jjtGetChild(1);
				SimpleNode nodeToAppendTo = firstChild.searchSuccessors();
				switch (nodeToAppendTo.toString()) {
					case "EndMethod":
						nodeToAppendTo = (SimpleNode) nodeToAppendTo.jjtGetParent();
					default:
						secondChild.jjtSetParent(nodeToAppendTo);
						nodeToAppendTo.jjtAddChild(secondChild, nodeToAppendTo.jjtGetNumChildren());
						break;
				}
				node.removeLastChild();
			}
			SimpleNode parent = (SimpleNode) node.jjtGetParent();
			parent.setChild(firstChild, childNo);
			firstChild.jjtSetParent(parent);
		}

		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			SimpleNode n = (SimpleNode) node.jjtGetChild(i);
			if (n != null) {
				fixTree(n, i);
			}
		}
	}
}

