package mz.org.fgh.mentoring.entity.form;

public enum SampleQuestion {

    NUMBER_OF_COLLECTED_SAMPLES("a61fe6ce8d5b453ab6c2d7ebc3bedcfe"),

    NUMBER_OF_TRANSPORTED_SAMPLES("9cc2cdd9a1bd41c2803725841ee1b15c"),

    NUMBER_OF_REJECTED_SAMPLES("afd9e8aa511a4ea796f1f7126a9eca4f"),

    NUMBER_OF_RECEIVED_SAMPLES("0dfc7d54c2c64c049f23d3ec49855a81");

    private String value;

    private SampleQuestion(final String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
