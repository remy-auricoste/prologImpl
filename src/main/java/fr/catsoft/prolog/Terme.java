package fr.catsoft.prolog;

import fr.catsoft.commons.common.exception.CatchException;
import fr.catsoft.commons.common.modele.WriteOnceMap;
import fr.catsoft.prolog.spec.base.ATerme;
import fr.catsoft.prolog.spec.interf.ITerme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Commentaire
 *
 * @version $Revision$ $Date$
 */
public class Terme extends ATerme implements Cloneable, ITerme {

    public Terme(String nom) {
        super(nom);
    }

    public Terme(String nom, List<ITerme> arguments) {
        super(nom, arguments);
    }

    public Map<String, Object> unifier(ITerme terme) {
        try {
            WriteOnceMap<String, Object> retour = new WriteOnceMap<String, Object>();
            if (!terme.getNom().equals(getNom())) {
                if (isParametrableNom()) {
                    if (getArite() != 0) {
                        retour.putWithException(getNom(), terme.getNom());
                        setNom(terme.getNom());
                    } else {
                        retour.putWithException(getNom(), terme);
                        setNom(terme.getNom());
                        setArguments(terme.getArguments());
                        return retour;
                    }
                } else if (terme.isParametrableNom()) {
                    return ((Terme) terme).unifier(this);
                } else {
                    throw new UnificationException();
                }
            }
            int cpt = 0;
            for (ITerme arg : getArguments()) {
                retour.putAllWithException(((Terme) arg).unifier(terme.getArguments().get(cpt)));
                cpt++;
            }
            return retour;
        } catch (CatchException e) {
            throw new UnificationException();
        }
    }

    public void setMap(Map<String, Object> map) {
        if (isParametrableNom()) {
            Object valeur = map.get(getNom());
            if (valeur != null) {
                if (valeur instanceof String) {
                    setNom((String) valeur);
                } else {
                    ITerme terme = (ITerme) valeur;
                    copier(terme);
                }
            }
        }
        for (ITerme terme : getArguments()) {
            ((Terme) terme).setMap(map);
        }
    }

    @Override
    public Terme clone() {
        Terme retour = (Terme) super.clone();
        List<ITerme> args = new ArrayList<ITerme>();
        for (ITerme terme : getArguments()) {
            args.add(terme.clone());
        }
        retour.setArguments(args);
        return retour;
    }

    public void copier(ITerme terme) {
        setNom(terme.getNom());
        setArguments(terme.getArguments());
    }
}
