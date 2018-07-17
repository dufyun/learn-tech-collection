package org.dufy.file2db;

import java.util.Observable;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * A signal observer to deal with system signals.
 * You can send a SIGHUP signal to JVM process, then the File2DB procedure
 * will suspend for a while.
 * 
 * <p>Referenced to <I>POSIX Signal Handling In Java</I>
 * 
 * @author ringlord.com
 * @author Charlie
 *
 */
@Component
public class StoreSignalObserver implements Observer {
  
  private static final Logger logger = LoggerFactory.getLogger(StoreSignalObserver.class);

  public void observe() {
    try {
      final SignalHandler sh = new SignalHandler();
      sh.addObserver(this);
      sh.handleSignal("SEGV");
    } catch (Throwable x) {
      
      // SignalHandler failed to instantiate: maybe the classes do not exist,
      // or the API has changed, or something else went wrong; actualy we get
      // here on an InterruptedException from Thread.sleep, too.
      x.printStackTrace();
    }
  }

  /**
   * Implementation of Observer, called by {@link SignalHandler} when a signal is received.
   * @param o Our {@link SignalHandler} object
   * @param arg The {@link sun.misc.Signal} that triggered the call
   **/
  public void update(final Observable o, final Object arg) {
    // use the same method that the Timer employs to trigger a
    // rotation, which ensures that signal and timer don't screw
    // each other up.
    System.out.println("Received signal: " + arg);
    logger.info("Received signal:{}, pause the StoreManager", arg);
    StoreManager.getInstance().setPauseFlag(true);
  }

  /**
   * <p>
   * An implementation of Sun Microsystems' {@link sun.misc.SignalHandler} interface that is also
   * {@link Observable} so that we can notify {@link Observer}S when a signal is raised. The
   * {@link #handle(sun.misc.signal)} method is called by Sun's libraries for every signal received
   * that was registered with a call to Sun's static
   * {@link sun.misc.Signal#handle(sun.misc.Signal,sun.misc.SignalHandle)} method.
   * </p>
   **/
  class SignalHandler extends Observable implements sun.misc.SignalHandler {
    /**
     * Tells the object to handle the given signal.
     * @param signalName The name of the signal, such as "SEGV", "ILL", "FPE", "ABRT", "INT",
     *          "TERM", "HUP", etc. Not all platforms support all signals. Microsoft Windows may not
     *          support HUP, for example, whereas that is a widely use and supported signal under
     *          Unix (and its variants); additionally, the JVM may be using some signals (the use of
     *          -Xrs will reduce or disable them at the cost of losing what the JVM wanted them
     *          for).
     * @exception IllegalArgumentException is thrown when the named signal is not available for some
     *              reason. Watch out: the original cause (missing class or method) may be wrapped
     *              inside the exception!
     **/
    public void handleSignal(final String signalName) throws IllegalArgumentException {
      try {
        sun.misc.Signal.handle(new sun.misc.Signal(signalName), this);
      } catch (IllegalArgumentException x) {
        // Most likely this is a signal that's not supported on this
        // platform or with the JVM as it is currently configured
        throw x;
      } catch (Throwable x) {
        // We may have a serious problem, including missing classes
        // or changed APIs
        throw new IllegalArgumentException("Signal unsupported: " + signalName, x);
      }
    }

    /**
     * Called by Sun Microsystems' signal trapping routines in the JVM.
     * @param signal The {@link sun.misc.Signal} that we received
     **/
    public void handle(final sun.misc.Signal signal) {
      // setChanged ensures that notifyObservers actually calls someone. In
      // simple cases this seems like extra work but in asynchronous designs,
      // setChanged might be called on one thread, and notifyObservers, on
      // another or only when multiple changes may have been completed (to
      // wrap up multiple changes in a single notifcation).
      setChanged();
      notifyObservers(signal);
    }
  }
}
