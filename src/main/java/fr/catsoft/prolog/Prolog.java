package fr.catsoft.prolog;

import fr.catsoft.commons.common.exception.ApplicationException;
import fr.catsoft.prolog.spec.base.AProlog;
import fr.catsoft.prolog.spec.interf.IRegle;
import fr.catsoft.prolog.spec.interf.IRegleManager;
import fr.catsoft.prolog.spec.interf.IReponse;
import fr.catsoft.prolog.spec.interf.ITerme;

import java.util.*;

/**
 * Commentaire
 *
 * @version $Revision$ $Date$
 */
public class Prolog extends AProlog {

    private ContexteQuestion contexteQuestion;

    public Prolog() {
        this(new MemoireRegleManager());
    }

    public Prolog(IRegleManager regleManager) {
        super(regleManager);
    }

    @Override
    public IRegle creerRegle(ITerme resultat, ITerme... resultats) {
        return new Regle(Arrays.asList(resultats), resultat);
    }

    @Override
    protected ITerme newTerme(String nom, List<ITerme> args) {
        return new Terme(nom, args);
    }

    private Reponse questionnerImpl(List<? extends ITerme> termes) {
        // TODO optimiser ordre verification
        Terme terme1 = (Terme) termes.get(0);
        Reponse reponse = questionnerImpl(terme1);
        if (termes.size() == 1) {
            return reponse;
        }
        if (!reponse.isVrai()) {
            return reponse;
        }
        if (!terme1.isParametrable()) {
            reponse.agregerReponse(terme1, questionnerImpl(termes.subList(1, termes.size())));
            return reponse;
        }
        Iterator<ITerme> iterator = reponse.getFaitsSimples().iterator();
        while (iterator.hasNext()) {
            ITerme fait = iterator.next();
            Map<String, Object> args = reponse.getArgs().get(fait);
            if (args == null) {
                throw new ApplicationException("erreur dans la recuperation des args");
            }
            List<Terme> subList = new ArrayList<Terme>();
            for (ITerme terme : termes.subList(1, termes.size())) {
                Terme newTerme = (Terme) terme.clone();
                newTerme.setMap(args);
                subList.add(newTerme);
            }
            Reponse sousReponse = questionnerImpl(subList);
            if (!sousReponse.isVrai()) {
                iterator.remove();
                reponse.getArgs().remove(fait);
            } else {
                reponse.agregerReponse(fait, sousReponse);
            }
        }
        return reponse;
    }

    public IReponse questionner(ITerme terme) {
        return questionner(Arrays.asList(terme));
    }

    @Override
    public IReponse questionner(List<ITerme> termes) {
        contexteQuestion = new ContexteQuestion();
        return questionnerImpl(termes);
    }

    private Reponse questionnerImpl(Terme terme) {
        Reponse reponse = new Reponse();
        if (contexteQuestion.getQuestionsEnCours().contains(terme)) {
            return reponse;
        } else {
            contexteQuestion.getQuestionsEnCours().add(terme.clone());
        }
        List<IRegle> listeRegles = regleManager.getReglesMatch(terme);
        if (listeRegles == null || listeRegles.isEmpty()) {
            return retour(reponse, terme);
        }
        for (IRegle regle : listeRegles) {
            Regle regleClone = (Regle) regle.clone();
            try {
                Terme resultatClone = (Terme) regleClone.getResutat();
                Map<String, Object> map = resultatClone.unifier(terme.clone());
                for (ITerme condition : regleClone.getConditions()) {
                    ((Terme) condition).setMap(map);
                }

                if (!regleClone.getConditions().isEmpty()) {
                    Reponse reponseTmp = questionnerImpl(regleClone.getConditions());
                    if (!reponseTmp.isVrai()) {
                        throw new UnificationException();
                    }
                    // si toutes les conditions sont remplies, on ajoute les conclusions aux resultats
                    for (Map<String, Object> mapParam : reponseTmp.getArgs().values()) {
                        Terme solution = resultatClone.clone();
                        solution.setMap(mapParam);
                        reponse.ajouterSolution(solution, mapParam);
                    }
                } else {
                    // si il n'y a pas de condition, on ajoute simplement la conclusion aux resultats
                    reponse.ajouterSolution(resultatClone, map);
                }
                // si la réponse n'est pas générique, on peut la renvoyer de suite
                if (!terme.isParametrable()) {
                    return retour(reponse, terme);
                }
            } catch (UnificationException e) {
            }
        }
        return retour(reponse, terme);
    }

    private Reponse retour(Reponse reponse, ITerme question) {
        contexteQuestion.getQuestionsEnCours().remove(question);
        return reponse;
    }

    @Override
    public Terme creerTerme(String chaine) {
        return (Terme) super.creerTerme(chaine);
    }
}
