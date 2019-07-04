package pasa.cbentley.byteobjects.functions;

import pasa.cbentley.byteobjects.core.BOAbstractOperator;
import pasa.cbentley.byteobjects.ctx.BOCtx;
import pasa.cbentley.byteobjects.tech.ITechAcceptor;
import pasa.cbentley.byteobjects.tech.ITechOperator;

import pasa.cbentley.core.src4.utils.DateUtils;
import pasa.cbentley.core.src4.utils.IntUtils;

/**
 * Filter Operator
 * 
 * Reduce
 * Map
 * @author Charles Bentley
 *
 */
public class AcceptorOperator extends BOAbstractOperator implements ITechOperator, ITechAcceptor {

   public AcceptorOperator(BOCtx boc) {
      super(boc);
   }

   public boolean booleanAction(boolean b1, int op, boolean b2) {
      switch (op) {
         case OP_LOGICAL_0_AND:
            return b1 && b2;
         case OP_LOGICAL_1_OR:
            return b1 || b2;
         default:
            return false;
      }

   }

   /**
    * Check int dates (in minutes)
    * <br>
    * <br>
    * @param intDateMinutes int date in minutes
    * @param operator
    * @param operand
    * @return
    */
   public boolean checkDateInt(int intDateMinutes, int operator, int operand) {
      int date = 0;
      int wlong = 0;
      int wint = 0;
      switch (operator) {
         case OP_DATE_LAST:
            break;
         case OP_DATE_MONTH:
            break;
         case OP_DATE_DAY:
            break;
         case OP_DATE_WEEK:
            break;
         case OP_DATE_YEAR:
            break;
         default:
            break;
      }
      if (date <= wlong && date >= (wlong - (wint * DateUtils.MILLIS_IN_A_DAY)))
         return true;
      return false;

   }

   public boolean checkMyInt(int x, int operator, int operand) {
      switch (operator) {
         case OP_INT_0_EQUAL:
            return operand == x;
         case OP_NOT_EQUAL:
            return operand != x;
         case OP_INT_1_HIGHER:
            return operand < x;
         case OP_INT_2_LOWER:
            return operand > x;
         case OP_DOES_NOT_EXIST:
            return 0 == x;
      }
      return false;
   }

   public boolean checkMyInts(int[] xs, int operator, int operand) {
      switch (operator) {
         case OP_INT_ARRAY_CONTAINS:
            return IntUtils.contains(xs, operand);
      }
      return false;
   }

   public boolean checkString(String str, int operator, String operand) {
      switch (operator) {
         case OP_DOES_NOT_EXIST:
            return str.equals("");
         case OP_INT_0_EQUAL:
            return str.equals(operand);
         case OP_INT_ARRAY_CONTAINS: {
            if (str.length() != 0) {
               return str.indexOf(operand) != -1;
            } else {
               return false;
            }
         }
         default:
            //UiLink.bip(357, operator + " not a string operator");
      }
      return false;
   }

   public boolean isAccepted(int x, int val, int op, boolean isAcceptor) {
      switch (op) {
         case OP_COMP_0_EQUAL:
            return (x == val) ? isAcceptor : !isAcceptor;
         case OP_COMP_2_BIGGER:
            return (x > val) ? isAcceptor : !isAcceptor;
         case OP_COMP_1_SMALLER:
            return (x < val) ? isAcceptor : !isAcceptor;
         default:
            return isAcceptor;
      }
   }

}
