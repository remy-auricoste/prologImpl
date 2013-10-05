package fr.catsoft.prolog.test;

import fr.catsoft.prolog.Machine;
import fr.catsoft.prolog.spec.interf.IProlog;
import fr.catsoft.prolog.spec.interf.IReponse;
import fr.catsoft.prolog.spec.test.ATestMachine;
import org.junit.Test;

/**
 * Commentaire
 *
 * @version $Revision$ $Date$
 */
public class TestMachine extends ATestMachine {

    @Override
    protected IProlog getImpl() {
        return new Machine();
    }
}
