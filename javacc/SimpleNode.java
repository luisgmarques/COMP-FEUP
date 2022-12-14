import java.util.ArrayList;
import java.util.List;

/* Generated By:JJTree: Do not edit this line. SimpleNode.java Version 6.0 */
/* JavaCCOptions:MULTI=false,NODE_USES_PARSER=false,VISITOR=false,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
public
class SimpleNode implements Node {

  protected Node parent;
  protected Node[] children;
  protected int id;
  protected int identifier;
  protected static int id_static = 0;
  protected Object value;
  protected Parser parser;
  protected String image;
  protected static ClassDeclaration classDec;
  protected static List<VarDeclaration> varDecList = new ArrayList<VarDeclaration>();
  protected static List<MethodDeclaration> methodDecList = new ArrayList<MethodDeclaration>();
  protected static List<String> importVars = new ArrayList<String>();
  protected Image img;
  protected Token token;

  public CodeLine codeLine;
  public MethodLines methodLines;

  public SimpleNode(int i) {
    id = i;
    identifier = id_static++;
  }

  public SimpleNode(Parser p, int i) {
    this(i);
    parser = p;
  }

  public void jjtOpen() {
  }

  public void jjtClose() {
  }

  public void jjtSetParent(Node n) { parent = n; }
  public Node jjtGetParent() { return parent; }

  public SimpleNode getParent() { return (SimpleNode) parent; }
  public SimpleNode getChild(int i) {
    return (SimpleNode) children[i];
  }

  public void jjtAddChild(Node n, int i) {
    if (children == null) {
      children = new Node[i + 1];
    } else if (i >= children.length) {
      Node c[] = new Node[i + 1];
      System.arraycopy(children, 0, c, 0, children.length);
      children = c;
    }
    children[i] = n;
  }

  public Node jjtGetChild(int i) {
    return children[i];
  }

  public int jjtGetNumChildren() {
    return (children == null) ? 0 : children.length;
  }

  public void jjtSetValue(Object value) { this.value = value; }
  public Object jjtGetValue() { return value; }

  /* You can override these two methods in subclasses of SimpleNode to
     customize the way the node appears when the tree is dumped.  If
     your output uses more than one line you should override
     toString(String), otherwise overriding toString() is probably all
     you need to do. */

  public String toString() {
    return ParserTreeConstants.jjtNodeName[id];
  }
  public String toString(String prefix) { return prefix + toString(); }

  /* Override this method if you want to customize how the node dumps
     out its children. */

  public void dump(String prefix) {
    System.out.print(toString(prefix));
    if (this.img != null)
      System.out.print("\t[ "+this.img.toString()+" ]");
    System.out.println();
    if (children != null) {
      for (int i = 0; i < children.length; ++i) {
        SimpleNode n = (SimpleNode)children[i];
        if (n != null) {
          n.dump(prefix + "  ");
        }
      }
    }
  }

  public Image getImage() {
    return img;
  }

  public Token getToken() {
    return token;
  }

  public void setChild(Node node, int i) {
    children[i] = node;
  }

  public void removeLastChild() {
    Node c[] = new Node[children.length - 1];
    System.arraycopy(children, 0, c, 0, children.length - 1);
    children = c;
  }

  public String firstChildName() {
    if(jjtGetNumChildren() > 0) {
      return jjtGetChild(0).toString();
    } else return "";
  }

  public SimpleNode getLastChild() {
    int numChildren = jjtGetNumChildren();
    if(jjtGetNumChildren() > 0)
      return (SimpleNode) jjtGetChild(numChildren - 1);
    return null;
  }

  public SimpleNode searchSuccessors() {
      if(jjtGetNumChildren() == 0) {
        return this;
      }
      return getLastChild().searchSuccessors();
  }

  public SimpleNode getNextSibling() {
    SimpleNode parent = (SimpleNode) jjtGetParent();
    for(int i = 0; i < parent.jjtGetNumChildren(); i++) {
      if(((SimpleNode) parent.jjtGetChild(i)).equals(this)) {
        if(i < parent.jjtGetNumChildren() - 1)
          return (SimpleNode) parent.jjtGetChild(i+1);
      }
    }
    return null;
  }

  public SimpleNode getNextSiblingNotElse(boolean found) {
    SimpleNode parent = (SimpleNode) jjtGetParent();
    for(int i = 0; i < parent.jjtGetNumChildren(); i++) {
      if(((SimpleNode) parent.jjtGetChild(i)).equals(this) || found) {
        found = true;
        if(i < parent.jjtGetNumChildren() - 1)
          if (!parent.jjtGetChild(i+1).toString().equals("Else"))
            return (SimpleNode) parent.jjtGetChild(i+1);
      }
    }
    return null;
  }

  public boolean isCondExpression() {
    SimpleNode parent = (SimpleNode) jjtGetParent();
    if (parent.toString().equals("Class")) return false;
    if (parent.toString().equals("If") || parent.toString().equals("While")) {
      SimpleNode node = (SimpleNode) parent.jjtGetChild(0);
      if (node.equals(this)) return true;
      else return false;
    }
    return parent.isCondExpression();
  }

  public boolean checkIfPredecessorExists(String predecessorName) {
    SimpleNode parent = (SimpleNode) this.jjtGetParent();
    while(!parent.toString().equals(predecessorName)) {
      if (parent.toString().equals("Class")) return false;
      parent = (SimpleNode) parent.jjtGetParent();
    }
    return true;
  }

  public SimpleNode getPredecessor(String predecessorName) {
    SimpleNode parent = (SimpleNode) this.jjtGetParent();
    while(!parent.toString().equals(predecessorName)) {
      if (parent.toString().equals("Class")) return null;
      parent = (SimpleNode) parent.jjtGetParent();
    }
    return parent;
  }

  public SimpleNode getGrandchild() {
      if(jjtGetNumChildren() > 0) {
          SimpleNode child = (SimpleNode) this.jjtGetChild(0);
          if(child.jjtGetNumChildren() > 0)
              return (SimpleNode) child.jjtGetChild(0);
      }
      return null;
  }

  public int getId() {
    return id;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof SimpleNode)) {
      return false;
    }
    SimpleNode s = (SimpleNode) o;
    if(identifier == s.identifier && jjtGetNumChildren() == s.jjtGetNumChildren() && id == ((SimpleNode) o).getId() && toString().equals(o.toString()))
      return true;
    return false;
  }
}

/* JavaCC - OriginalChecksum=8bb69b442959e844c3d0c42fc85e5d2c (do not edit this line) */
