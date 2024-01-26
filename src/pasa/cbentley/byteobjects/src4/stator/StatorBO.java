/*
 * (c) 2018-2020 Charles-Philip Bentley
 * This code is licensed under MIT license (see LICENSE.txt for details)
 */
package pasa.cbentley.byteobjects.src4.stator;

import pasa.cbentley.byteobjects.src4.core.ByteObject;
import pasa.cbentley.byteobjects.src4.ctx.BOCtx;
import pasa.cbentley.byteobjects.src4.utils.ByteObjectTuple;
import pasa.cbentley.core.src4.io.BADataIS;
import pasa.cbentley.core.src4.io.BADataOS;
import pasa.cbentley.core.src4.logging.Dctx;
import pasa.cbentley.core.src4.stator.Stator;
import pasa.cbentley.core.src4.stator.StatorReader;
import pasa.cbentley.core.src4.stator.StatorWriter;
import pasa.cbentley.core.src4.structs.BufferObject;

/**
 * 
 * @author Charles Bentley
 *
 */
public class StatorBO extends Stator implements ITechStatorBO {

   protected final BOCtx boc;

   private BufferObject  bufferTuples;

   public StatorBO(BOCtx boc) {
      super(boc.getUCtx());
      this.boc = boc;
   }

   public StatorWriterBO getWriterBO(int type) {
      return (StatorWriterBO) getWriter(type);
   }

   public StatorReaderBO getReaderBO(int type) {
      return (StatorReaderBO) getReader(type);
   }

   protected StatorReader createReader(int type) {
      return new StatorReaderBO(this, type);
   }

   public StatorWriter createWriter(int type) {
      return new StatorWriterBO(this, type);
   }

   /**
    * Return null if no keyed Readers for key/type
    * <p>
    * 
    * While {@link StatorBO#getStatorWriterKeyedTo(ByteObject, int)} never returns null
    * as it creates one when needed,
    * 
    * </p>
    * @param key
    * @param type
    * @return
    */
   public StatorReaderBO getStatorReaderKeyedToExisting(ByteObject key, int type) {
      if (bufferTuples != null) {
         int len = bufferTuples.getLength();
         for (int i = 0; i < len; i++) {
            ByteObjectTuple bot = (ByteObjectTuple) bufferTuples.get(i);
            if (bot.getId() == type) {
               if (bot.getBo().equalsContent(key)) {
                  return (StatorReaderBO) bot.getO2();
               }
            }
         }
      }
      return null;
   }

   /**
    * Return null if nothing keyed to 
    * @param key
    * @param type
    * @return
    */
   public StatorWriterBO getStatorWriterKeyedToExisting(ByteObject key, int type) {
      if (bufferTuples != null) {
         int len = bufferTuples.getLength();
         for (int i = 0; i < len; i++) {
            ByteObjectTuple bot = (ByteObjectTuple) bufferTuples.get(i);
            if (bot.getId() == type) {
               if (bot.getBo().equalsContent(key)) {
                  return (StatorWriterBO) bot.getO1();
               }
            }
         }
      }
      return null;
   }

   /**
    * A {@link StatorWriterBO} linked to a {@link ByteObject}.
    * 
    * <p>
    *  This allows to save settings for
    * </p>
    * When loading a windows, we want to reuse its coord associated with the screen configuration.
    * If a screen is not there anymore, we do not want the windows so be shown on invisible coordiantes.
    * So the saved coordinates must come from the saved state of windows coordinates associated with current
    * screen configuration.
    * 
    * So {@link StatorBO} can associate a given Writer to not only a type but also a ByteObject
    * @param key
    * @return
    * 
    * <li> {@link ITechStatorBO#TYPE_0_MASTER}
    * <li> {@link ITechStatorBO#TYPE_1_VIEW}
    * <li> {@link ITechStatorBO#TYPE_2_MODEL}
    * <li> {@link ITechStatorBO#TYPE_3_CTX}
    * @param key
    * @param type
    * @return
    */
   public StatorWriterBO getStatorWriterKeyedTo(ByteObject key, int type) {
      if (bufferTuples == null) {
         bufferTuples = new BufferObject(uc, 1);
      } else {
         StatorWriterBO writer = getStatorWriterKeyedToExisting(key, type);
         if (writer != null) {
            return writer;
         }
      }
      ByteObjectTuple bot = new ByteObjectTuple(boc);
      bot.setId(type);
      bot.setBo(key);
      StatorWriterBO writer = (StatorWriterBO) createWriter(type);
      bot.setO1(writer);
      bufferTuples.add(bot);
      return writer;
   }

   protected void serializeAllSub(BADataOS out) {
      out.writeInt(MAGIC_WORD_STATORBO);
      if (bufferTuples != null) {
         int num = bufferTuples.getLength();
         out.writeInt(num);
         for (int index = 0; index < num; index++) {
            ByteObjectTuple bot = (ByteObjectTuple) bufferTuples.get(index);

            //serialize byteobject
            ByteObject bo = bot.getBo();
            bo.serialize(out);

            StatorWriterBO writer = (StatorWriterBO) bot.getO1();
            writer.serializeWhole(out);
         }
      } else {
         out.writeInt(0);
      }
   }

   /**
    * 
    */
   protected boolean switchMagicSub(int magic, BADataIS dis) {
      if (magic == MAGIC_WORD_STATORBO) {
         int readNum = dis.readInt();
         if (readNum != 0) {
            bufferTuples = new BufferObject(uc, readNum);
            for (int i = 0; i < readNum; i++) {
               ByteObjectTuple bot = new ByteObjectTuple(boc);

               ByteObject bo = boc.getByteObjectFactory().serializeReverse(dis);
               bot.setBo(bo);

               int magicToken = dis.readInt();
               if (magicToken != MAGIC_WORD_WRITER) {
                  throw new IllegalArgumentException();
               }
               StatorReader reader = super.readStatorReader(dis);
               int type = reader.getType();
               bot.setId(type);

               bot.setO2(reader);

               bufferTuples.add(bot);
            }
         }
         return true;
      } else {
         //#debug
         toDLog().pAlways("Wrong Magic " + magic, this, Stator.class, "switchMagic", LVL_05_FINE, true);
         return false;
      }
   }

   //#mdebug
   public void toString(Dctx dc) {
      dc.root(this, StatorBO.class, 120);
      toStringPrivate(dc);
      super.toString(dc.sup());

      dc.nlLvl(bufferTuples, "KeysToTuples");
   }

   public void toString1Line(Dctx dc) {
      dc.root1Line(this, StatorBO.class);
      toStringPrivate(dc);
      super.toString1Line(dc.sup1Line());
   }

   private void toStringPrivate(Dctx dc) {

   }

   //#enddebug

}
