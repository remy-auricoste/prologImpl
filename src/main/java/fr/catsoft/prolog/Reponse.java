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

    private Set<ITerme> faits;
    private Map<ITerme, Map<String, Object>> args;
    private Reponse sousReponse;

    public Reponse() {
        super();
        faits = new HashSet<>();
        args = new HashMap<>();
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

    public Reponse agregerReponse(Reponse reponse) {
        if (sousReponse != null) {
            throw new AgregationException("Une reponse a deja ete agregee");
        }
        if (!reponse.isVrai()) {
            throw new AgregationException("Impossible d'agreger une reponse fausse");
        }
        sousReponse = reponse;
        return this;
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
                        sousIterator = sousReponse.getFaitsMultiples().iterator();
                    }

                    private void instanceSousIterator() {
                        if (sousIterator == null) {
                            initSousIterator();
                        }
                    }

                    private ITerme getLocalCurrent() {
                        if (localCurrent == null) {
                            localCurrent = localIterator.next();
                        }
                        return localCurrent;
                    }

                    @Override
                    public boolean hasNext() {
                        if (localIterator.hasNext()) {
                            return true;
                        }
                        if (sousReponse == null) {
                            return false;
                        }
                        instanceSousIterator();
                        return sousIterator.hasNext();
                    }

                    @Override
                    public List<ITerme> next() {
                        if (sousReponse == null) {
                            return Arrays.asList(localIterator.next());
                        }
                        instanceSousIterator();
                        if (sousIterator.hasNext()) {
                            List<ITerme> retour = new ArrayList<>();
                            retour.add(getLocalCurrent());
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
