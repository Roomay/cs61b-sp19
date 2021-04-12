import static org.junit.Assert.*;

import org.junit.Test;

public class FilkTest {
    @Test
    public void testFilk(){
        int j=0;
        for (int i = 0; i < 500; i++, j++) {
            assertTrue(Flik.isSameNumber(i,j));
            assertTrue("Exception here", Flik.isSameNumber(i,j));
        }
    }
}
