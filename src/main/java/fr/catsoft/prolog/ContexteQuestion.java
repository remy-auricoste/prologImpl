package fr.catsoft.prolog;

import fr.catsoft.prolog.spec.interf.ITerme;

import java.util.HashSet;
import java.util.Set;

/**
 * Commentaire
 *
 * @version $Revision$ $Date$
 */
public class ContexteQuestion {

    private Set<ITerme> questionsEnCours;

    public ContexteQuestion() {
        questionsEnCours = new HashSet<ITerme>();
    }

    public Set<ITerme> getQuestionsEnCours() {
        return questionsEnCours;
    }
}
