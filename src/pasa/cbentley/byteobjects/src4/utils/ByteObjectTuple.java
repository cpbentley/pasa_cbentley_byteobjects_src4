package pasa.cbentley.byteobjects.src4.utils;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.ctx.ObjectBoc;
import pasa.cbentley.core.src4.logging.Dctx;

public class ByteObjectTuple extends ObjectBoc {

   private int        id;

   private ByteObject bo;

   private Object     o1;

   private Object     o2;

   private Object     o3;

   public ByteObjectTuple(BOCtx boc) {
      super(boc);
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public ByteObject getBo() {
      return bo;
   }

   public void setBo(ByteObject bo) {
      this.bo = bo;
   }

   public Object getO1() {
      return o1;
   }

   public void setO1(Object o1) {
      this.o1 = o1;
   }

   public Object getO2() {
      return o2;
   }

   public void setO2(Object o2) {
      this.o2 = o2;
   }

   public Object getO3() {
      return o3;
   }

   public void setO3(Object o3) {
      this.o3 = o3;
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, ByteObjectTuple.class, 66);
      toStringPrivate(dc);
      super.toString(dc.sup());

      dc.nlLvl(bo, "ByteObject");

      dc.nlLvlObject("Object1", o1);
      dc.nlLvlObject("Object2", o2);
      dc.nlLvlObject("Object3", o3);

   }

   private void toStringPrivate(Dctx dc) {
      dc.appendVarWithSpace("id", id);
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, ByteObjectTuple.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   //#enddebug

}
