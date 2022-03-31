package org.func.spring.boot.utils;

import org.func.spring.boot.container.FuncMethod;
import org.func.spring.boot.exception.FuncMethodNotFoundException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

import static org.func.spring.boot.utils.FuncString.format;

/**
 * @author yiur
 */
public final class ExceptionUtil {

  private ExceptionUtil() {
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
