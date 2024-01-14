/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.function;

import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.logging.IStringable;
import pasa.cbentley.core.src4.structs.decisiontree.BooleanTreeResolver;

/**
 * Boolean tree
 * <br>
 * <br>
 * Used by {@link Acceptor};
 * <br>
 * <br>
 * 
 * @author Charles Bentley
 *
 */
public class BooleanTree implements IStringable {

   /**
    * Node that recieves an Acceptor
    * @author Charles Bentley
    *
    */
   public class AccNode {

      int     addy;

      /**
       * index of node in the flattened expression.
       */
      int     index;

      boolean isLeaf = true;

      int     left;

      /**
       * result for boolean expression
       */
      boolean result;

      int     right;

      int     type   = NODE;

      AccNode(int type, int index, int addy) {
         this(type, -1, -1, index, addy);
      }

      AccNode(int type, int left, int right, int index, int addy) {
         this.type = type;
         if (this.type == NODE)
            isLeaf = false;
         this.left = left;
         this.right = right;
         this.index = index;
         this.addy = addy;
      }

      AccNode(int type, int left, int right, int index, int[] addies) {
         this(type, left, right, index, addies.length - 1);
      }

      AccNode(int type, int index, int[] addies) {
         this(type, -1, -1, index, addies.length - 1);
      }

      public AccNode left() {
         return treenodes[left];
      }

      public AccNode right() {
         return treenodes[right];
      }

   }

   public static final int LEAF   = 2;

   public static final int LMASK  = 1;

   public static final int NODE   = 1;

   public static final int OP_AND = 0;

   public static final int OP_OR  = 1;

   public static final int RMASK  = 2;

   public static boolean booleanAction(boolean b1, int op, boolean b2) {
      switch (op) {
         case OP_AND:
            return b1 && b2;
         case OP_OR:
            return b1 || b2;
         default:
            return false;
      }

   }

   public static AccNode[] increaseCapacity(AccNode[] ar, int add) {
      if (ar == null) {
         return new AccNode[add];
      }
      AccNode[] oldData = ar;
      ar = new AccNode[oldData.length + add];
      System.arraycopy(oldData, 0, ar, 0, oldData.length);
      return ar;
   }

   public Acceptor[]   acceptors;

   /**
    * This node is one of the Nodes in _operators
    */
   public int          root = -1;

   public AccNode[]    treenodes;

   private BOCtx boc;

   public BooleanTree(BOCtx boc, int f) {
      this.boc = boc;
      treenodes = new AccNode[0];
      add(f, 0);
   }

   /**
    * @return
    */
   public boolean accept() {
      return true;
   }

   /**
    * add to the right, operator becomes the new root
    * 
    * @param f
    * @param operator
    */
   public void add(int f, int operator) {
      if (root == -1) {
         treenodes = new AccNode[1];
         treenodes[0] = new AccNode(LEAF, f, 0);
         root = 0;
      } else {
         treenodes = increaseCapacity(treenodes, 2);
         AccNode newright = new AccNode(LEAF, f, treenodes.length - 2);
         treenodes[newright.addy] = newright;
         AccNode newRoot = new AccNode(NODE, root, newright.addy, operator, treenodes.length - 1);
         treenodes[newRoot.addy] = newRoot;
         root = newRoot.addy;
      }
   }

   /**
    * Add AND ( LEFT OR RIGHT)
    * AND TYPE = 3 OR TYPE = 5
    * @param left
    * @param opbetween
    * @param right
    * @param roperator if tree is empty, this operator is ignored
    */
   public void add(int left, int opbetween, int right, int roperator) {
      if (root == -1) {
         add(left, 0);
         add(right, opbetween);
      } else {
         addFull(left, opbetween, right, roperator);
      }
   }

   private void addFull(int left, int opbetween, int right, int roperator) {
      treenodes = increaseCapacity(treenodes, 4);
      int fl = treenodes.length;
      AccNode newright = new AccNode(LEAF, right, fl - 4);
      treenodes[newright.addy] = newright;
      AccNode newleft = new AccNode(LEAF, left, fl - 3);
      treenodes[newleft.addy] = newleft;
      AccNode newOp = new AccNode(NODE, newleft.addy, newright.addy, opbetween, fl - 2);
      treenodes[newOp.addy] = newOp;
      //new root with old root as the right node
      AccNode newRoot = new AccNode(NODE, root, newOp.addy, roperator, fl - 1);
      treenodes[newRoot.addy] = newRoot;
      root = newRoot.addy;
   }

   private void algo() {
      int len = (treenodes.length - 1) / 2;
      int count = 0;
      AccNode node = null;
      while (count != len) {
         for (int i = 0; i < treenodes.length; i++) {
            node = treenodes[i];
            // op is NODE , then left and right CANNOT be null
            if (node.type == NODE && !node.isLeaf && node.left().isLeaf && node.right().isLeaf) {
               node.isLeaf = true;
               resolve(node);
               count++;
            }
         }
      }
   }

   public void clear() {
      root = -1;
      treenodes = new AccNode[0];
   }

   /**
    * Evaluate through this boolean expression and those given Filter indexes.
    * .
    * Filter produce boolean result of matching.
    * @param mf
    * @return
    */
   public boolean evaluate() {
      if (root == -1)
         return true;
      prepare();
      if (treenodes.length == 1) {
         resolve(treenodes[root]);
         return treenodes[root].result;
      }
      algo();
      //when the algo finishes, the result is at the root
      return treenodes[root].result;
   }

   private void prepare() {
      for (int i = 0; i < treenodes.length; i++) {
         treenodes[i].result = false;
         //make sure a leaf is a leaf and parents are unresolved
         if (treenodes[i].right == -1) {
            treenodes[i].isLeaf = true;
         } else {
            treenodes[i].isLeaf = false;
         }
      }
   }

   /**
    * At current Node (Boolean expression), ask the {@link BooleanTreeResolver} to compute result.
    * @param mf
    * @param op
    */
   private void resolve(AccNode op) {
      if (op.left().type == NODE) {
         if (op.right().type == NODE) {
            // we have an operator result here
            op.result = resolve(op.left().result, op.index, op.right().result);
         } else {
            // right is not a node
            op.result = resolve(op.left().result, op.index, op.right().index);
         }
      } else {
         if (op.right().type == NODE) {
            op.result = resolve(op.left().index, op.index, op.right().result);
         } else {
            // left and right are a LEAVES
            op.result = resolve(op.left().index, op.index, op.right().index);
         }
      }
   }

   public boolean resolve(boolean left, int op, boolean right) {
      return booleanAction(left, op, right);
   }

   public boolean resolve(boolean left, int op, int right) {
      if (left && op == OP_OR)
         return true;
      if (!left && op == OP_AND)
         return false;
      boolean bright = acceptors[right].accept();
      return booleanAction(left, op, bright);

   }

   /**
    * 
    * @param left to be decided based on resolver.
    * @param op
    * @param right known boolean expression
    * @return
    */
   public boolean resolve(int left, int op, boolean right) {
      if (right && op == OP_OR)
         return true;
      if (!right && op == OP_AND)
         return false;
      //ask to resolve the filter at index left
      boolean bleft = acceptors[left].accept();
      return booleanAction(bleft, op, right);
   }

   public boolean resolve(int left, int op, int right) {
      boolean b1 = acceptors[left].accept();
      if (b1 && op == OP_OR)
         return true;
      if (!b1 && op == OP_AND)
         return false;
      boolean b2 = acceptors[right].accept();
      return booleanAction(b1, op, b2);
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx sb) {
      sb.root(this, "BooleanTree");
      sb.append("root=" + root + " \n");
      for (int i = 0; i < treenodes.length; i++) {
         sb.append(treenodes[i].addy + ",");
         sb.append(treenodes[i].index + ",");
         sb.append(treenodes[i].left + ",");
         sb.append(treenodes[i].right + ",");
         sb.append(treenodes[i].type + "\n");
      }
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "BooleanTree");
   }

   //#enddebug

}
