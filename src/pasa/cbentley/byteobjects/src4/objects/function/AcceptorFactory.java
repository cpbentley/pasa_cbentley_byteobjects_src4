/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.objects.function;

import pasa.cbentley.byteobjects.src4.core.BOAbstractFactory;
import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.IBOTypesBOC;
import pasa.cbentley.core.src4.logging.Dctx;

public class AcceptorFactory extends BOAbstractFactory implements IBOFunction, ITechAcceptor {

   public AcceptorFactory(BOCtx boc) {
      super(boc);
   }

   public void addAcceptorToFunDef(ByteObject acceptor, ByteObject def) {
      if (acceptor != null) {
         def.setFlag(FUN_OFFSET_02_FLAG, FUN_FLAG_8_ACCEPTOR, true);
         def.addByteObject(acceptor);
      }
   }

   /**
    * Map {@link ByteObject} definition to a Java Object
    * @param acc
    * @return
    */
   public Acceptor createAcceptor(ByteObject acc) {
      boolean accept = acc.hasFlag(IBOAcceptor.ACC_OFFSET_01_FLAG, IBOAcceptor.ACC_FLAG_1_ACCEPT);
      int op = acc.get1(IBOAcceptor.ACC_OFFSET_02_OPERATOR1);
      int type = acc.get1(IBOAcceptor.ACC_OFFSET_03_TYPE1);
      Acceptor acceptor = new Acceptor(boc, op, accept, type);

      int v = acc.get4(IBOAcceptor.ACC_OFFSET_05_OPERAND4);

      switch (type) {
         case ACC_TYPE_0_INT:
            acceptor.setOperandInt(v);
            break;
         case ACC_TYPE_1_ARRAY:
            ByteObject ar = acc.getSubFirst(IBOTypesBOC.TYPE_007_LIT_ARRAY_INT);
            int[] array = boc.getLitteralIntOperator().getLitteralArray(ar);
            acceptor.setOperandArray(array);
            break;
         case ACC_TYPE_2_BYTEOBJECT:
            //will be post initialized
            ByteObject pointer = acc.getSubFirst(IBOTypesBOC.TYPE_010_POINTER);
            if (pointer == null) {
               throw new NullPointerException();
            }
            acceptor.setOperandInt(v);
            acceptor.setOperandPointer(pointer);
            break;
         case ACC_TYPE_3_STRING:
            ByteObject strOperand = acc.getSubFirst(IBOTypesBOC.TYPE_003_LIT_STRING);
            String str = boc.getLitteralStringOperator().getLitteralString(strOperand);
            acceptor.setStrOperand(str);
            break;
         case ACC_TYPE_7_EXPRESSION:
            //
            ByteObject accLeft = acc.getSubOrder(IBOTypesBOC.TYPE_022_ACCEPTOR, 0);
            ByteObject accRight = acc.getSubOrder(IBOTypesBOC.TYPE_022_ACCEPTOR, 1);
            if (accLeft == null) {
               throw new NullPointerException("Null Left Acceptor");
            }
            if (accRight == null) {
               throw new NullPointerException("Null Right Acceptor");
            }
            acceptor.setExpressions(createAcceptor(accLeft), createAcceptor(accRight));
            break;
         default:
            throw new IllegalArgumentException();
      }
      return acceptor;
   }

   /**
    * Inverse function to serialize an acceptor
    * @param ac
    * @return
    */
   public ByteObject getAcceptor(Acceptor ac) {
      ByteObject p = createAcceptorEmpty();

      return p;
   }

   /**
    * Create sub function type Acceptor.
    * <br>
    * <br>
    * @param acceptor
    * @param accept
    * @param countRejects half rejects,
    * @return
    */
   public ByteObject getAcceptor(int acceptor, boolean accept, boolean countRejects) {
      ByteObject p = createAcceptorEmpty();
      p.setFlag(IBOAcceptor.ACC_OFFSET_01_FLAG, IBOAcceptor.ACC_FLAG_1_ACCEPT, accept);
      p.setValue(IBOAcceptor.ACC_OFFSET_05_OPERAND4, acceptor, 4);
      return p;
   }

   public ByteObject getAcceptor(int value, int condition) {
      return getAcceptorInt(value, condition, true);
   }

   public ByteObject getAcceptor(int[] data, boolean isAcceptor) {
      ByteObject p = createAcceptorEmpty();
      p.setFlag(IBOAcceptor.ACC_OFFSET_01_FLAG, IBOAcceptor.ACC_FLAG_1_ACCEPT, isAcceptor);
      p.setFlag(IBOAcceptor.ACC_OFFSET_01_FLAG, IBOAcceptor.ACC_FLAG_8_OPERAND, true);
      p.setValue(IBOAcceptor.ACC_OFFSET_03_TYPE1, ACC_TYPE_1_ARRAY, 1);
      p.addByteObject(boc.getLitteralIntFactory().getLitteralArray(data));
      return p;
   }

   private ByteObject createAcceptorEmpty() {
      return new ByteObject(boc, IBOTypesBOC.TYPE_022_ACCEPTOR, IBOAcceptor.ACC_BASIC_SIZE);
   }

   public ByteObject getAcceptorEqual(int value) {
      return getAcceptor(value, OP_COMP_0_EQUAL);
   }

   /**
    * Creates a more complex Acceptor
    * @param accLeft
    * @param op
    * @param accRight
    * @return
    */
   public ByteObject getAcceptorExpression(ByteObject accLeft, int op, ByteObject accRight) {
      return getAcceptorExpression(accLeft, op, accRight, true);
   }

   /**
    * 
    * @param accLeft
    * @param op
    * @param accRight
    * @param isAcceptor Sets the {@link IBOAcceptor#ACC_FLAG_1_ACCEPT}
    * @return
    */
   public ByteObject getAcceptorExpression(ByteObject accLeft, int op, ByteObject accRight, boolean isAcceptor) {
      ByteObject p = createAcceptorEmpty();
      p.setFlag(IBOAcceptor.ACC_OFFSET_01_FLAG, IBOAcceptor.ACC_FLAG_1_ACCEPT, isAcceptor);
      p.setValue(IBOAcceptor.ACC_OFFSET_02_OPERATOR1, op, 1);
      p.setValue(IBOAcceptor.ACC_OFFSET_03_TYPE1, ACC_TYPE_7_EXPRESSION, 1);
      p.addByteObject(new ByteObject[] { accLeft, accRight });
      return p;
   }

   public ByteObject getAcceptorInt(int value, int condition, boolean acceptor) {
      ByteObject p = createAcceptorEmpty();
      p.setFlag(IBOAcceptor.ACC_OFFSET_01_FLAG, IBOAcceptor.ACC_FLAG_1_ACCEPT, acceptor);
      p.setFlag(IBOAcceptor.ACC_OFFSET_01_FLAG, IBOAcceptor.ACC_FLAG_8_OPERAND, true);
      p.setValue(IBOAcceptor.ACC_OFFSET_05_OPERAND4, value, 4);
      p.setValue(IBOAcceptor.ACC_OFFSET_02_OPERATOR1, condition, 1);
      p.setValue(IBOAcceptor.ACC_OFFSET_03_TYPE1, ACC_TYPE_0_INT, 1);

      return p;
   }

   /**
    * Specific acceptor for {@link ByteObject} that must mastch pointer type and whose pointed value is
    * checked on integer
    * @param val
    * @param op
    * @param pointer
    * @return
    */
   public ByteObject getAcceptorPointer(int operand, int op, ByteObject pointer, boolean acceptor) {
      ByteObject p = createAcceptorEmpty();
      p.setValue(IBOAcceptor.ACC_OFFSET_03_TYPE1, ACC_TYPE_2_BYTEOBJECT, 1);
      p.setFlag(IBOAcceptor.ACC_OFFSET_01_FLAG, IBOAcceptor.ACC_FLAG_8_OPERAND, true);
      p.setFlag(IBOAcceptor.ACC_OFFSET_01_FLAG, IBOAcceptor.ACC_FLAG_1_ACCEPT, acceptor);

      p.setValue(IBOAcceptor.ACC_OFFSET_05_OPERAND4, operand, 4);
      p.setValue(IBOAcceptor.ACC_OFFSET_02_OPERATOR1, op, 1);
      p.addByteObject(pointer);
      return p;
   }

   /**
    * Operator
    * <li>
    * @param str
    * @param operator
    * @param acceptor
    * @return
    */
   public ByteObject getAcceptorString(String str, int operator, boolean acceptor) {
      int size = IBOAcceptor.ACC_BASIC_SIZE;
      size += str.length() * 2;
      ByteObject p = new ByteObject(boc, IBOTypesBOC.TYPE_022_ACCEPTOR, size);
      p.setFlag(IBOAcceptor.ACC_OFFSET_01_FLAG, IBOAcceptor.ACC_FLAG_1_ACCEPT, acceptor);
      p.setFlag(IBOAcceptor.ACC_OFFSET_01_FLAG, IBOAcceptor.ACC_FLAG_8_OPERAND, true);
      p.setValue(IBOAcceptor.ACC_OFFSET_02_OPERATOR1, operator, 1);
      p.setValue(IBOAcceptor.ACC_OFFSET_03_TYPE1, ACC_TYPE_3_STRING, 1);
      p.setDynOverWriteChars(IBOAcceptor.ACC_OFFSET_05_OPERAND4, str.toCharArray(), 0, str.length());
      return p;
   }

   /**
    * Acceptor returns true when value is not EQUAL, BIGGER or SMALLER
    * Rejector is equal to !
    * @param value
    * @return
    */
   public ByteObject getRejector(int value, int condition) {
      return getAcceptorInt(value, condition, false);
   }

   //#mdebug

   public void toStringAcceptor(Dctx sb, ByteObject bo) {
      sb.append("#Acceptor");
      if (bo.hasFlag(IBOAcceptor.ACC_OFFSET_01_FLAG, IBOAcceptor.ACC_FLAG_1_ACCEPT)) {
         sb.append(" accepts=");
      } else {
         sb.append(" rejects=");
      }
      if (bo.hasFlag(IBOAcceptor.ACC_OFFSET_01_FLAG, IBOAcceptor.ACC_FLAG_3_ARRAY)) {
         int[] vals = bo.getValues(IBOAcceptor.ACC_OFFSET_05_OPERAND4);
         boc.getUC().getIU().toStringIntArray(sb, vals, ",");
      } else {
         int val = bo.getValue(IBOAcceptor.ACC_OFFSET_05_OPERAND4, 4);
         sb.append(boc.getUC().getColorU().toStringColor(val));
      }
      if (bo.hasFlag(IBOAcceptor.ACC_OFFSET_01_FLAG, IBOAcceptor.ACC_FLAG_5_CHANNEL)) {
         sb.append(" 8bitsChannelMode");
      }
      if (bo.hasFlag(IBOAcceptor.ACC_OFFSET_01_FLAG, IBOAcceptor.ACC_FLAG_2_COUNT_REJECTS)) {
         sb.append(" countRejections");
      }
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, "AcceptorFactory");
      toStringPrivate(dc);
      super.toString(dc.sup());
   }

   private void toStringPrivate(Dctx dc) {

   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, "AcceptorFactory");
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }
   //#enddebug

}
