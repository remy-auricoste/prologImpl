package fr.catsoft.prolog.test;

import fr.catsoft.prolog.Machine;
import fr.catsoft.prolog.Terme;
import fr.catsoft.prolog.UnificationException;
import fr.catsoft.prolog.spec.interf.ITerme;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Commentaire
 *
 * @version $Revision$ $Date$
 */
public class TestTerme extends TestCase {

    @Test
    public void testUnifier() {
        Machine machine = new Machine();
        Terme terme1 = machine.creerTerme("parent(jean,charles)");
        Terme terme2 = machine.creerTerme("parent(jean,X)");
        Map<String, Object> map = terme1.unifier(terme2);
        assertTrue(terme1.equals(terme2));
        assertTrue(map.size() == 1);
        assertTrue(map.get("X").equals(machine.creerTerme("charles")));

        terme1 = machine.creerTerme("parent(jean,charles)");
        terme2 = machine.creerTerme("Y(Z,X)");
        map = terme1.unifier(terme2);
        assertTrue(terme1.equals(terme2));
        assertTrue(map.size() == 3);
        assertTrue(map.get("X").equals(machine.creerTerme("charles")));
        assertTrue(map.get("Z").equals(machine.creerTerme("jean")));
        assertTrue(map.get("Y").equals("parent"));

        terme1 = machine.creerTerme("fonc(a,a)");
        terme2 = machine.creerTerme("F(A,A)");
        map = terme1.unifier(terme2);
        assertTrue(terme1.equals(terme2));
        assertTrue(map.size() == 2);
        assertTrue(map.get("F").equals("fonc"));
        assertTrue(map.get("A").equals(machine.creerTerme("a")));

        terme1 = machine.creerTerme("parent(jean,charles)");
        terme2 = machine.creerTerme("Y(X,X)");
        testUnificationException(terme1, terme2);

        terme1 = machine.creerTerme("parent(jean,charles)");
        terme2 = machine.creerTerme("pere(X,Y)");
        testUnificationException(terme1, terme2);

        terme1 = machine.creerTerme("parent(jean,charles)");
        terme2 = machine.creerTerme("parent(X,charle)");
        testUnificationException(terme1, terme2);

        terme1 = machine.creerTerme("parent(jean,charles)");
        terme2 = machine.creerTerme("parent(jea,X)");
        testUnificationException(terme1, terme2);
    }

    private void testUnificationException(Terme terme1, Terme terme2) {
        try {
            terme1.unifier(terme2);
            fail();
        } catch (UnificationException e) {
        }
    }

    @Test
    public void testRecursivite() {
        Machine machine = new Machine();
        Terme terme = machine.creerTerme("a(b(c,d),e(f,g))");
        ITerme terme1 = machine.creerTerme("b(c,d)");
        ITerme terme2 = machine.creerTerme("e(f,g)");
        Terme attendu = new Terme("a", Arrays.asList(terme1, terme2));
        assertTrue(attendu.equals(terme));
    }
}
