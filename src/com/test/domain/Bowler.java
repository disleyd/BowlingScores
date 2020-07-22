package com.test.domain;

public class Bowler {
    private final String name;

    public Bowler(String name) {
        this.name = name;
    }

    public static Bowler bowler(String name) {
        return new Bowler(name);
    }

    public String name() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bowler bowler = (Bowler) o;

        return !(name != null ? !name.equals(bowler.name) : bowler.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
