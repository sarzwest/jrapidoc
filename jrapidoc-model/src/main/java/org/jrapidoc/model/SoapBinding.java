package org.jrapidoc.model;

/**
 * Created by papa on 11.4.15.
 */
public class SoapBinding {

    private String style;
    private String use;
    private String parameterStyle;

    private SoapBinding(String style, String use, String parameterStyle) {
        this.style = style;
        this.use = use;
        this.parameterStyle = parameterStyle;
    }

    public static class SoapBindingBuilder{

        private String style = "DOCUMENT";
        private String use = "LITERAL";
        private String parameterStyle = "WRAPPED";

        public SoapBindingBuilder style(String style) {
            this.style = style;
            return this;
        }

        public SoapBindingBuilder use(String use) {
            this.use = use;
            return this;
        }

        public SoapBindingBuilder parameterStyle(String parameterStyle) {
            this.parameterStyle = parameterStyle;
            return this;
        }

        public SoapBinding build(){
            return new SoapBinding(style, use, parameterStyle);
        }

    }
}
