// Brad Cardello (bcardell)
// Kara Ekiss (kekiss)
// $Id: visitor.java,v 1.3 2014-05-13 00:45:58-07 - - $

interface visitor <key_t, value_t> {
   public void visit (key_t key, value_t value);
}

