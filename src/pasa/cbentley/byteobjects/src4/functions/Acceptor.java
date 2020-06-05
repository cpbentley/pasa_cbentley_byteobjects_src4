/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.functions;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.tech.ITechAcceptor;
import pasa.cbentley.byteobjects.src4.tech.ITechPointer;
import pasa.cbentley.core.src4.ctx.UCtx;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.utils.IntUtils;

/**
 * Full Java version {@link Acceptor} works on a condition definition and a pointer
 * 
 * Use to
 * <li> Accept reject int value given in the method
 * <li> Accept/Reject complex object based on pointer
 * <br>
 * <br>
 * Equality acceptor/rejector
 * <br>
 * Function returns true(acceptor) or false(rejector) if input value is equal to at least one value in array.
 * 
 * Subclass may implements any accepting procedure
 * all red channels > 128 
 * <br>
 * Used as a search filter for {@link ITechPointer}.
 * <br>
 * <br>
 * Acceptance
 * <li>Value + operator
 * <li>Flag set/notset
 * <li>ByteObject pointer
 * <li>Array container
 * <br>
 * <br>
 * <b>String acceptor</b>
 * <br>
 * We want only ByteObject whose name start with letter s.
 * 
 * <br>
 * <br>
 * @author Charles Bentley
 *
 */
public class Acceptor implements ITechAcceptor {


   private Acceptor    acceptorLeft;

   private Acceptor    acceptorRight;

   /**
    * Boolean tree to compute the results of combined acceptors.
    * <br>
    * Acceptor1 && Acceptor2
    */
   private BooleanTree booleanTree;

   private int         inputInt;

   /**
    * 
    */
   public boolean      isAcceptor;

   int                 op;

   protected int[]     operandArray;

   protected int       operandInt;

   private String      operandStr;

   private ByteObject  pointer;

   private String      inputStr;

   private ByteObject  inputByteObject;

   private int         type;

   private ByteObject  operandPointer;

   private BOCtx       boc;

   public Acceptor(BOCtx boc, int operator, boolean accept, int type) {
      this.boc = boc;
      this.op = operator;
      this.isAcceptor = accept;
      this.type = type;
   }

   public boolean accept() {
      switch (type) {
         case ACC_TYPE_0_INT:
            return accept(inputInt);
         case ACC_TYPE_1_ARRAY:
            return acceptArray(inputInt);
         case ACC_TYPE_2_BYTEOBJECT:
            //check the condition on ByteObejct
            int value = boc.getPointerOperator().getPointerValueEx(operandPointer, inputByteObject);
            return accept(value);
         case ACC_TYPE_3_STRING:
            return boc.getAcceptorStatic().checkString(inputStr, op, operandStr);
         case ACC_TYPE_7_EXPRESSION:
            return acceptExpression();
         default:
            break;
      }
      return false;
   }

   private boolean acceptExpression() {
      //evaluate the left acceptor in the boolean expression
      boolean isLeft = acceptorLeft.accept();
      switch (op) {
         case OP_LOGICAL_0_AND:
            if (isLeft) {
               boolean isRight = acceptorRight.accept();
               if (isAcceptor) {
                  return isLeft && isRight;
               } else {
                  return !(isLeft && isRight);
               }
            } else {
               return !isAcceptor;
            }
         case OP_LOGICAL_1_OR:
            if (isLeft) {
               return isAcceptor;
            } else {
               return acceptorRight.accept();
            }
         default:
            break;
      }
      return booleanTree.evaluate();
   }

   /**
    * Read {@link ByteObject} with resident pointer and apply acceptor
    * <br>
    * <br>
    * @param bo
    * @return
    */
   public boolean accept(ByteObject bo) {
      setInputByteObject(bo);
      return accept();
   }

   public void setInputByteObject(ByteObject bo) {
      inputByteObject = bo;
      //propagate target all all
      if (acceptorLeft != null) {
         acceptorLeft.setInputByteObject(bo);
      }
      if (acceptorRight != null) {
         acceptorRight.setInputByteObject(bo);
      }
   }

   /**
    * Shorcut
    * @param x
    * @return
    */
   public boolean accept(int x) {
      if (operandArray == null) {
         switch (op) {
            case OP_COMP_0_EQUAL:
               return (x == operandInt) ? isAcceptor : !isAcceptor;
            case OP_COMP_2_BIGGER:
               return (x > operandInt) ? isAcceptor : !isAcceptor;
            case OP_COMP_1_SMALLER:
               return (x < operandInt) ? isAcceptor : !isAcceptor;
            default:
               return isAcceptor;
         }
      } else {
         return acceptArray(x);
      }
   }

   private boolean acceptArray(int x) {
      if (IntUtils.contains(operandArray, x)) {
         return isAcceptor;
      } else {
         return !isAcceptor;
      }
   }

   public int getOperandInt() {
      return operandInt;
   }

   /**
    * Sets the target and field pointer for which operator and operand work on
    * @param bo
    * @param pointer
    */
   public void setByteObject(ByteObject bo, ByteObject pointer) {
      inputByteObject = bo;
      this.pointer = pointer;
   }

   public void setExpression() {

   }

   public void setExpressions(Acceptor left, Acceptor right) {
      acceptorLeft = left;
      acceptorRight = right;
   }

   public void setInputInt(int in) {
      inputInt = in;
   }

   public void setInputString(String stri) {
      inputStr = stri;
   }

   public void setOperandArray(int[] ar) {
      operandArray = ar;
   }

   public void setOperandInt(int v) {
      operandInt = v;
   }

   public void setStrOperand(String strO) {
      operandStr = strO;
   }

   public void setOperandPointer(ByteObject pointer) {
      operandPointer = pointer;
   }

   //#mdebug
   public String toString() {
      return Dctx.toString(this);
   }

   public void toString(Dctx dc) {
      dc.root(this, "Acceptor");
   }

   public UCtx toStringGetUCtx() {
      return boc.getUCtx();
   }

   public String toString1Line() {
      return Dctx.toString1Line(this);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "Acceptor");
   }

   //#enddebug

}
