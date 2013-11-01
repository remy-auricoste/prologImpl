package fr.catsoft.prolog.test;

import fr.catsoft.commons.common.exception.ApplicationException;
import fr.catsoft.commons.common.logger.Logger;
import fr.catsoft.prolog.AgregationException;
import fr.catsoft.prolog.Prolog;
import fr.catsoft.prolog.Reponse;
import fr.catsoft.prolog.spec.interf.ITerme;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: remy
 * Date: 01/11/13
 * Time: 14:18
 * To change this template use File | Settings | File Templates.
 */
public class TestReponse extends TestCase {

    private int getSize(Iterable<?> iterable) {
        int cpt = 0;
        for (Object objet : iterable) {
            cpt++;
        }
        return cpt;
    }

    private <T> T get(Iterable<T> iterable, int index) {
        int cpt = 0;
        for (T objet : iterable) {
            if (cpt == index) {
                return objet;
            }
            cpt++;
        }
        throw new IndexOutOfBoundsException();
    }

    private ITerme getTerme(String chaine) {
        return new Prolog().creerTerme(chaine);
    }

    @Test
    public void testIsVrai() {
        assertTrue(!new Reponse().isVrai());
    }


    @Test
    public void testAgregation() {
        Reponse reponse1 = new Reponse();
        Reponse reponse2 = new Reponse();
        Reponse reponse3 = new Reponse();

        ITerme termeTest = getTerme("test");
        reponse1.ajouterSolution(termeTest, null);
        try {
            reponse1.agregerReponse(termeTest, reponse2);
            fail();
        } catch (AgregationException e) {
        }
        reponse2.ajouterSolution(getTerme("a(1)"), null);
        reponse3.ajouterSolution(getTerme("b(1)"), null);
        reponse1.agregerReponse(termeTest, reponse2);
        try {
            reponse1.agregerReponse(termeTest, reponse3);
            fail();
        } catch (AgregationException e) {
        }
    }

    @Test
    public void testFaitsMultiples() {
        ITerme termeA1 = getTerme("a(1)");
        ITerme termeA2 = getTerme("a(2)");
        ITerme termeB1 = getTerme("b(1)");
        ITerme termeB2 = getTerme("b(2)");
        ITerme termeC1 = getTerme("c(1)");
        ITerme termeC2 = getTerme("c(2)");

        Reponse reponse1 = new Reponse();
        reponse1.ajouterSolution(termeA1, null);
        reponse1.ajouterSolution(termeA2, null);

        Reponse reponse2 = new Reponse();
        reponse2.ajouterSolution(termeB1, null);
        reponse2.ajouterSolution(termeB2, null);

        Reponse reponse3 = new Reponse();
        reponse3.ajouterSolution(termeC1, null);
        reponse3.ajouterSolution(termeC2, null);

        reponse1.agregerReponse(termeA1, reponse2);
        reponse1.agregerReponse(termeA2, reponse2);
        reponse2.agregerReponse(termeB1, reponse3);
        reponse2.agregerReponse(termeB2, reponse3);

        for (List<ITerme> termes : reponse1.getFaitsMultiples()) {
            // Logger.instance().info(termes + "");
        }
        assertEquals(8, getSize(reponse1.getFaitsMultiples()));
        assertTrue(get(reponse1.getFaitsMultiples(), 0).equals(Arrays.asList(termeA1, termeB1, termeC1)));
        assertTrue(get(reponse1.getFaitsMultiples(), 4).equals(Arrays.asList(termeA2, termeB1, termeC1)));
        assertTrue(get(reponse1.getFaitsMultiples(), 7).equals(Arrays.asList(termeA2, termeB2, termeC2)));

    }
}
