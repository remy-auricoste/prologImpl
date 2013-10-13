package fr.catsoft.prolog.test;

import fr.catsoft.prolog.Prolog;
import fr.catsoft.prolog.spec.interf.IProlog;
import fr.catsoft.prolog.spec.test.ATestMachine;

/**
 * Commentaire
 *
 * @version $Revision$ $Date$
 */
public class TestMachine extends ATestMachine {

    @Override
    protected IProlog getImpl() {
        return new Prolog();
    }
}
