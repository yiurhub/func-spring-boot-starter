package org.func.spring.boot.exception;

/**
 * @author Yiur
 */
public class FuncMethodNotFoundException extends FuncException {
    
    private static final long serialVersionUID = -7657349271970657758L;

    public FuncMethodNotFoundException() {
    }

    public FuncMethodNotFoundException(String message) {
        super(message);
    }

}
