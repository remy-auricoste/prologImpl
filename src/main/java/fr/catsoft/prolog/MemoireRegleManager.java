package fr.catsoft.prolog;

import fr.catsoft.prolog.spec.interf.IRegle;
import fr.catsoft.prolog.spec.interf.IRegleManager;
import fr.catsoft.prolog.spec.interf.ITerme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Commentaire
 *
 * @version $Revision$ $Date$
 */
public class MemoireRegleManager implements IRegleManager {

    private static final String CLEF_FONCTION_GENERIQUE = "";

    private Map<String, List<IRegle>> regles;

    public MemoireRegleManager() {
        regles = new HashMap<String, List<IRegle>>();
        regles.put(CLEF_FONCTION_GENERIQUE, new ArrayList<IRegle>());
    }

    @Override
    public void ajouterFait(ITerme fait) {
        ajouterRegle(new Regle(null, fait));
    }

    @Override
    public void ajouterRegle(IRegle regle) {
        String clef = regle.getResutat().isParametrableNom() ? CLEF_FONCTION_GENERIQUE : regle.getResutat().getNom();
        List<IRegle> listeRegles = regles.get(clef);
        if (listeRegles == null) {
            listeRegles = new ArrayList<IRegle>();
            regles.put(clef, listeRegles);
        }
        listeRegles.add(regle);
    }

    public List<IRegle> getReglesMatch(ITerme terme) {
        List<IRegle> listeRegles = new ArrayList<IRegle>();
        List<IRegle> listeSimples = regles.get(terme.getNom());
        if (listeSimples != null) {
            listeRegles.addAll(regles.get(terme.getNom()));
        }
        listeRegles.addAll(regles.get(CLEF_FONCTION_GENERIQUE));
        return listeRegles;
    }
}
