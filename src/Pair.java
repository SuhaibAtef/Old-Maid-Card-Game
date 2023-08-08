public record Pair<L, R>(L left, R right) {

    public Pair {
        assert left != null;
        assert right != null;

    }

    @Override
    public int hashCode() {
        return left.hashCode() ^ right.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair pair)) return false;
        return this.left.equals(pair.left()) &&
                this.right.equals(pair.right()) || this.right.equals(pair.left()) &&
                this.left.equals(pair.right());
    }

}