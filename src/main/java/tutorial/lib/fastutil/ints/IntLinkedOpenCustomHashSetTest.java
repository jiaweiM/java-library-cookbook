package tutorial.lib.fastutil.ints;


import it.unimi.dsi.fastutil.ints.IntHash;
import it.unimi.dsi.fastutil.ints.IntLinkedOpenCustomHashSet;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IntLinkedOpenCustomHashSetTest
{
    @Test
    public void testGetNullKey()
    {
        final IntLinkedOpenCustomHashSet s = new IntLinkedOpenCustomHashSet(new IntHash.Strategy()
        {
            @Override
            public int hashCode(int o)
            {
                return o % 10;
            }

            @Override
            public boolean equals(int a, int b)
            {
                return (a - b) % 10 == 0;
            }
        });

        s.add(3);
        s.add(10);
        s.add(0);
        assertTrue(s.contains(0));
        assertTrue(s.contains(10));
        assertTrue(s.contains(3));
        assertFalse(s.contains(1));
        IntListIterator i = s.iterator();
        assertEquals(3, i.nextInt());
        assertEquals(10, i.nextInt());
        assertFalse(i.hasNext());

        s.remove(0);
        assertFalse(s.contains(0));
        assertFalse(s.contains(10));
        s.add(10);

        i = s.iterator();
        assertEquals(3, i.nextInt());
        assertEquals(10, i.nextInt());
        assertFalse(i.hasNext());

    }
}
