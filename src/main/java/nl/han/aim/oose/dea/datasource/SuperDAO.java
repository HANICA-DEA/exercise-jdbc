package nl.han.aim.oose.dea.datasource;

import java.util.List;

public class SuperDAO<Model> {

    public Model create(Model m) {
        // TODO: create entity
        return m;
    }

    public Model findAll(List<Model> l) {
        // TODO: implement.
        return null;
    }

    public Model findById(Model m) {
        // TODO: create entity
        return m;
    }

    public Model update(Model m) {
        // TODO: create entity
        return m;
    }

    /** Warning: Maak niet alle mogelijke methoden, denk ook aan 'Interface Segregation' pattern.
     * Waarschijnlijk heeft maar een deel van DAO klasses ook echt 'delete' functionaliteit.
     * Een eenmaal aan items gekoppelde category wil je waarschijnlijk niet verwijderen.
     * @param m
     * @return
     */
    public Model delete(Model m) {

    }

}
