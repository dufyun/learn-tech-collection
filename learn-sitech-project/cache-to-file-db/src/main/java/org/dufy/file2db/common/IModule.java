package org.dufy.file2db.common;

/**
 * Each back-end module initializer or controller should implements this interface. 
 * The {@link ModuleInitializer} will scan all implementation classes and start them.
 * 
 * @author Charlie
 *
 */
public interface IModule {

	/**
	 * Start up the module
	 */
	public void startup();
	
	/**
	 * Shutdown the module and release resources
	 */
	public void shutdown();
}
