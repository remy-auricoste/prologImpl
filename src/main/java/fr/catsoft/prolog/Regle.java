package fr.catsoft.prolog;

import fr.catsoft.prolog.spec.base.ARegle;
import fr.catsoft.prolog.spec.interf.IRegle;
import fr.catsoft.prolog.spec.interf.ITerme;

import java.util.ArrayList;
import java.util.List;

/**
 * Commentaire
 *
 * @version $Revision$ $Date$
 */
public class Regle extends ARegle {

    public Regle(List<ITerme> conditions, ITerme resutat) {
        super(conditions, resutat);
    }

    @Override
    public Regle clone() {
        List<ITerme> listeClone = new ArrayList<ITerme>();
        for (ITerme terme : getConditions()) {
            listeClone.add(terme.clone());
        }
        return new Regle(listeClone, getResutat().clone());
    }
}
