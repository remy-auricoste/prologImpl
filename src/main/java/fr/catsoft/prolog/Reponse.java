package fr.catsoft.prolog;

import fr.catsoft.commons.common.exception.CatchException;
import fr.catsoft.commons.common.modele.WriteOnceMap;
import fr.catsoft.prolog.spec.base.AReponse;
import fr.catsoft.prolog.spec.interf.ITerme;

import java.util.*;

/**
 * Commentaire
 *
 * @version $Revision$ $Date$
 */
public class Reponse extends AReponse {

    private Set<ITerme> faits;
    private Map<ITerme, Map<String, Object>> args;
    private WriteOnceMap<ITerme, Reponse> agregations;

    public Reponse() {
        super();
        faits = new HashSet<>();
        args = new HashMap<>();
        agregations = new WriteOnceMap<>();
    }

    private Set<ITerme> getFaits() {
        return faits;
    }

    @Override
    public Iterable<ITerme> getFaitsSimples() {
        return new Iterable<ITerme>() {
            @Override
            public Iterator<ITerme> iterator() {
                return faits.iterator();
            }
        };
    }

    public Map<ITerme, Map<String, Object>> getArgs() {
        return args;
    }

    public void ajouterSolution(ITerme solution, Map<String, Object> args) {
        faits.add(solution);
        this.args.put(solution, args);
    }

    public void agregerReponse(ITerme terme, Reponse reponse) {
        if (!reponse.isVrai()) {
            throw new AgregationException("Impossible d'agreger une reponse fausse");
        }
        try {
            agregations.putWithException(terme, reponse);
        } catch (CatchException e) {
            throw new AgregationException("Le terme " + terme + " a déjà été agrégé", e);
        }
    }

    @Override
    public Iterable<List<ITerme>> getFaitsMultiples() {
        return new Iterable<List<ITerme>>() {
            @Override
            public Iterator<List<ITerme>> iterator() {
                return new Iterator<List<ITerme>>() {

                    private final Iterator<ITerme> localIterator = getFaits().iterator();
                    private Iterator<List<ITerme>> sousIterator;
                    private ITerme localCurrent;

                    private void initSousIterator() {
                        if (localCurrent == null) {
                            return;
                        }
                        Reponse sousReponse = agregations.get(localCurrent);
                        if (sousReponse != null) {
                            sousIterator = sousReponse.getFaitsMultiples().iterator();
                        }
                    }

                    private void instanceSousIterator() {
                        if (sousIterator == null) {
                            initSousIterator();
                        }
                    }

                    @Override
                    public boolean hasNext() {
                        if (localIterator.hasNext()) {
                            return true;
                        }
                        instanceSousIterator();
                        if (sousIterator == null) {
                            return false;
                        }
                        return sousIterator.hasNext();
                    }

                    @Override
                    public List<ITerme> next() {
                        if (localCurrent == null) {
                            localCurrent = localIterator.next();
                        }
                        instanceSousIterator();
                        if (sousIterator == null) {
                            List<ITerme> retour = Arrays.asList(localCurrent);
                            localCurrent = null;
                            return retour;
                        }
                        if (sousIterator.hasNext()) {
                            List<ITerme> retour = new ArrayList<>();
                            retour.add(localCurrent);
                            retour.addAll(sousIterator.next());
                            return retour;
                        } else {
                            initSousIterator();
                            localCurrent = null;
                            return next();
                        }
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}
