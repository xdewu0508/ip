public enum TaskType {
    TODO("[T]"),
    DEADLINE("[D]"),
    EVENT("[E]");

    private final String tag;

    TaskType(String tag) {
        this.tag = tag;
    }

    public String tag() {
        return tag;
    }
}
