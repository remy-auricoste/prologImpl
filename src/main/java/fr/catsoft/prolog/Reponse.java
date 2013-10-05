package fr.catsoft.prolog;

import fr.catsoft.prolog.spec.base.AReponse;
import fr.catsoft.prolog.spec.interf.ITerme;

import java.util.*;

/**
 * Commentaire
 *
 * @version $Revision$ $Date$
 */
public class Reponse extends AReponse {

    private Map<ITerme, Map<String, Object>> args;

    public Reponse() {
        super();
        args = new HashMap<ITerme, Map<String, Object>>();
    }

    public Map<ITerme, Map<String, Object>> getArgs() {
        return args;
    }

    public void ajouterSolution(ITerme solution, Map<String, Object> args) {
        getFaits().add(solution);
        getArgs().put(solution, args);
    }
}
