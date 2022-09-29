package com.company;

/**
 * Owner class is for all people that have
 * name and that are owner of something.
 */
public class Owner {
    private String name;

    /**
     * Creates an owner with given name.
     * @param name  Name of this owner.
     */
    public Owner(String name) {
        setName(name);
    }

    /**
     * @return Name of this owner.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this owner.
     * @param name New name of this owner.
     */
    public void setName(String name) {
        this.name = name;
    }
}
