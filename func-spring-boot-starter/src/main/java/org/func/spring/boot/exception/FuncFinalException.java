package org.func.spring.boot.exception;

import org.func.spring.boot.method.FuncMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

import static org.func.spring.boot.utils.StringUtil.format;

/**
 * @author yiur
 */
public final class FuncFinalException {

  private FuncFinalException() {
  }

  public static Throwable unwrapThrowable(Throwable wrapped) {
    Throwable unwrapped = wrapped;
    while (true) {
      if (unwrapped instanceof InvocationTargetException) {
        unwrapped = ((InvocationTargetException) unwrapped).getTargetException();
      } else if (unwrapped instanceof UndeclaredThrowableException) {
        unwrapped = ((UndeclaredThrowableException) unwrapped).getUndeclaredThrowable();
      } else {
        return unwrapped;
      }
    }
  }

  public static void notNullFuncMethod(FuncMethod funcMethod, Method method) throws FuncMethodNotFoundException {
    if (funcMethod == null) {
      throw new FuncMethodNotFoundException(format("The method: ? executed by the current bean does not have an anonymous function configured", method.getName()));
    }
  }

}
