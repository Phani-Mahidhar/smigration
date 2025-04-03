package com.sas.parser;

public class Spacer {
    private int n;

    public Spacer() {
        this(0);
    }

    public Spacer(int n) {
        this.set(n);
    }

    public Spacer set(int n) {
        this.n = n;
        return this;
    }

    public int get() {
        return this.n;
    }

    public Spacer add(int n) {
        return this.set(this.n + n);
    }

    public Spacer subtract(int n) {
        return this.set(this.n - n);
    }

    public String toString() {
        return Spaces.of(this.n);
    }

    public StringBuilder spaces(StringBuilder buf) {
        return Spaces.append(buf, this.n);
    }

    public String padRight(String string) {
        Spaces.padRight(string, this.n);
        int x = this.n - string.length();
        return x <= 0 ? string : Spaces.append(new StringBuilder(string), x).toString();
    }
}

