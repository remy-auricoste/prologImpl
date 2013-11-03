package fr.catsoft.prolog;

import fr.catsoft.prolog.spec.base.APrologHelper;
import fr.catsoft.prolog.spec.interf.IRegle;
import fr.catsoft.prolog.spec.interf.ITerme;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: remy
 * Date: 03/11/13
 * Time: 22:29
 * To change this template use File | Settings | File Templates.
 */
public class PrologHelper extends APrologHelper {

    @Override
    public IRegle creerRegle(ITerme resultat, ITerme... resultats) {
        return new Regle(Arrays.asList(resultats), resultat);
    }

    @Override
    protected ITerme newTerme(String nom, List<ITerme> args) {
        return new Terme(nom, args);
    }
}
