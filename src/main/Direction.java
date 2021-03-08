package main;

public enum Direction {
    Left,
    Right,
    Center;

    @Override
    public String toString() {
        switch(this) {
            case Left: return "L";
            case Right: return "R";
            case Center: return "C";
            default: throw new IllegalArgumentException();
        }
    }
}