package fr.catsoft.prolog.test;

import fr.catsoft.prolog.Prolog;
import fr.catsoft.prolog.PrologHelper;
import fr.catsoft.prolog.Terme;
import fr.catsoft.prolog.UnificationException;
import fr.catsoft.prolog.spec.interf.IPrologHelper;
import fr.catsoft.prolog.spec.interf.ITerme;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;

/**
 * Commentaire
 *
 * @version $Revision$ $Date$
 */
public class TestTerme extends TestCase {

    private IPrologHelper helper = new PrologHelper();

    private Terme getTerme(String chaine) {
        return (Terme) helper.creerTerme(chaine);
    }

    @Test
    public void testUnifier() {
        Terme terme1 = getTerme("parent(jean,charles)");
        Terme terme2 = getTerme("parent(jean,X)");
        Map<String, Object> map = terme1.unifier(terme2);
        assertTrue(terme1.equals(terme2));
        assertTrue(map.size() == 1);
        assertTrue(map.get("X").equals(getTerme("charles")));

        terme1 = getTerme("parent(jean,charles)");
        terme2 = getTerme("Y(Z,X)");
        map = terme1.unifier(terme2);
        assertTrue(terme1.equals(terme2));
        assertTrue(map.size() == 3);
        assertTrue(map.get("X").equals(getTerme("charles")));
        assertTrue(map.get("Z").equals(getTerme("jean")));
        assertTrue(map.get("Y").equals("parent"));

        terme1 = getTerme("fonc(a,a)");
        terme2 = getTerme("F(A,A)");
        map = terme1.unifier(terme2);
        assertTrue(terme1.equals(terme2));
        assertTrue(map.size() == 2);
        assertTrue(map.get("F").equals("fonc"));
        assertTrue(map.get("A").equals(getTerme("a")));

        terme1 = getTerme("parent(jean,charles)");
        terme2 = getTerme("Y(X,X)");
        testUnificationException(terme1, terme2);

        terme1 = getTerme("parent(jean,charles)");
        terme2 = getTerme("pere(X,Y)");
        testUnificationException(terme1, terme2);

        terme1 = getTerme("parent(jean,charles)");
        terme2 = getTerme("parent(X,charle)");
        testUnificationException(terme1, terme2);

        terme1 = getTerme("parent(jean,charles)");
        terme2 = getTerme("parent(jea,X)");
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
        ITerme terme = getTerme("a(b(c,d),e(f,g))");
        ITerme terme1 = getTerme("b(c,d)");
        ITerme terme2 = getTerme("e(f,g)");
        ITerme attendu = new Terme("a", Arrays.asList(terme1, terme2));
        assertTrue(attendu.equals(terme));
    }

    @Test
    public void testToString() {
        String chaine = "homme(jean)";
        assertEquals(chaine, helper.creerTerme(chaine).toString());
        chaine = "homme(noir(jean,bleu(jeanne),rouge))";
        assertEquals(chaine, helper.creerTerme(chaine).toString());
    }
}
