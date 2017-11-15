package test.lib.fastutil.ints;

import it.unimi.dsi.fastutil.ints.IntArrayFrontCodedList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertTrue;


@SuppressWarnings({"unchecked"})
public class IntArrayFrontCodedListTest {


    private static java.util.Random r = new java.util.Random(0);

    private static int genKey() {
        return r.nextInt();
    }

    private static boolean contentEquals(java.util.List x, java.util.List y) {
        if (x.size() != y.size()) return false;
        for (int i = 0; i < x.size(); i++)
            if (!java.util.Arrays.equals((int[]) x.get(i), (int[]) y.get(i))) return false;
        return true;
    }

    private static int l[];

    private static int[][] a;

    private static void test(int n) throws IOException, ClassNotFoundException {
        l = new int[n];
        a = new int[n][];
        for (int i = 0; i < n; i++)
            l[i] = (int) (Math.abs(r.nextGaussian()) * 32);
        for (int i = 0; i < n; i++)
            a[i] = new int[l[i]];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < l[i]; j++)
                a[i][j] = genKey();
        IntArrayFrontCodedList m = new IntArrayFrontCodedList(it.unimi.dsi.fastutil.objects.ObjectIterators.wrap(a),
                r.nextInt(4) + 1);
        it.unimi.dsi.fastutil.objects.ObjectArrayList t = new it.unimi.dsi.fastutil.objects.ObjectArrayList(a);
        // System.out.println(m);
        // for( i = 0; i < t.size(); i++ )
        // System.out.println(ARRAY_LIST.wrap((KEY_TYPE[])t.get(i)));
        /* Now we check that m actually holds that data. */
        assertTrue(contentEquals(m, t));
		/* Now we check cloning. */
        assertTrue(contentEquals(m, m.clone()));
		/* Now we play with iterators. */
        {
            ObjectListIterator i;
            java.util.ListIterator j;
            i = m.listIterator();
            j = t.listIterator();
            for (int k = 0; k < 2 * n; k++) {
                assertTrue(i.hasNext() == j.hasNext());
                assertTrue(i.hasPrevious() == j.hasPrevious());
                if (r.nextFloat() < .8 && i.hasNext()) {
                    assertTrue(java.util.Arrays.equals((int[]) i.next(), (int[]) j.next()));
                } else if (r.nextFloat() < .2 && i.hasPrevious()) {
                    assertTrue(java.util.Arrays.equals((int[]) i.previous(), (int[]) j.previous()));
                }
                assertTrue(i.nextIndex() == j.nextIndex());
                assertTrue(i.previousIndex() == j.previousIndex());
            }
        }
        {
            int from = r.nextInt(m.size() + 1);
            ObjectListIterator i;
            java.util.ListIterator j;
            i = m.listIterator(from);
            j = t.listIterator(from);
            for (int k = 0; k < 2 * n; k++) {
                assertTrue(i.hasNext() == j.hasNext());
                assertTrue(i.hasPrevious() == j.hasPrevious());
                if (r.nextFloat() < .8 && i.hasNext()) {
                    assertTrue(java.util.Arrays.equals((int[]) i.next(), (int[]) j.next()));
                    // System.err.println("Done next " + I + " " + J + "  " + badPrevious);
                } else if (r.nextFloat() < .2 && i.hasPrevious()) {
                    assertTrue(java.util.Arrays.equals((int[]) i.previous(), (int[]) j.previous()));
                }
            }
        }
        java.io.File ff = new java.io.File("it.unimi.dsi.fastutil.test");
        java.io.OutputStream os = new java.io.FileOutputStream(ff);
        java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(os);
        oos.writeObject(m);
        oos.close();
        java.io.InputStream is = new java.io.FileInputStream(ff);
        java.io.ObjectInputStream ois = new java.io.ObjectInputStream(is);
        m = (IntArrayFrontCodedList) ois.readObject();
        ois.close();
        ff.delete();
        assertTrue(contentEquals(m, t));
    }

    @Test
    void test1() throws IOException, ClassNotFoundException {
        test(1);
    }

    @Test
    void test10() throws Exception {
        test(10);
    }

    @Test
    void test100() throws IOException, ClassNotFoundException {
        test(100);
    }

    @Test
    void test1000() throws IOException, ClassNotFoundException {
        test(1000);
    }

    @Test
    void test10000() throws IOException, ClassNotFoundException {
        test(10000);
    }
}
