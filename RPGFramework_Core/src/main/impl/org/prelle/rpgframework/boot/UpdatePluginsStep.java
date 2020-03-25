/**
 * 
 */
package org.prelle.rpgframework.boot;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prelle.rpgframework.PluginRegistryImpl;

import de.rpgframework.ConfigOption;
import de.rpgframework.ExitCodes;
import de.rpgframework.PluginDescriptor;
import de.rpgframework.PluginRegistry;
import de.rpgframework.PluginState;
import de.rpgframework.RPGFrameworkConstants;
import de.rpgframework.RPGFrameworkInitCallback;
import de.rpgframework.RPGFrameworkLoader;
import de.rpgframework.UpdateResult;
import de.rpgframework.boot.BootStep;
import de.rpgframework.boot.StandardBootSteps;

/**
 * Contact all download servers and build a list of usable plugins -
 * each only in its most recent possible version.
 * 
 * @author prelle
 *
 */
public class UpdatePluginsStep implements BootStep {
	
	private final static Logger logger = LogManager.getLogger("rpgframework");
	
	private Path installDir;
	private Path pluginDir;
	private PluginRegistryImpl registry;
	
	private transient int count;
	private transient int max;

	//-------------------------------------------------------------------
	public UpdatePluginsStep(PluginRegistryImpl registry) {
		this.registry = registry;
		installDir = getInstallationDirectory();
		logger.info("Installation directory: "+installDir);
		
		pluginDir  = installDir.resolve("plugins");
	}
	
	//-------------------------------------------------------------------
	private Path getInstallationDirectory() {
		if (System.getProperty(RPGFrameworkConstants.PROPERTY_INSTALLATION_DIRECTORY)==null) {
			logger.fatal("System Property '"+RPGFrameworkConstants.PROPERTY_INSTALLATION_DIRECTORY+"' not set by main application");
			System.exit(ExitCodes.ERROR_INIT_PHASE);
		}
		
		return Paths.get(System.getProperty(RPGFrameworkConstants.PROPERTY_INSTALLATION_DIRECTORY));
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getID()
	 */
	@Override
	public String getID() {
		return StandardBootSteps.UPDATE_PLUGINS.name();
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getWeight()
	 */
	@Override
	public int getWeight() {
		return 30;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#shallBeDisplayedToUser()
	 */
	@Override
	public boolean shallBeDisplayedToUser() {
		return false;
//		return RPGFrameworkLoader.getInstance().getPluginRegistry().getNumberOfPluginsToLoad()<1;
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#getConfiguration()
	 */
	@Override
	public List<ConfigOption<?>> getConfiguration() {
		return new ArrayList<ConfigOption<?>>();
	}

	//-------------------------------------------------------------------
	/**
	 * @see de.rpgframework.boot.BootStep#execute(de.rpgframework.RPGFrameworkInitCallback)
	 */
	@Override
	public boolean execute(RPGFrameworkInitCallback callback) {
		if (callback!=null) {
			callback.progressChanged(0);
			callback.message("Download plugins");
		}
		
		List<PluginDescriptor> local  = registry.getKnownPlugins();
		// Remote plugins should only be the newest left, since step
		// KeepNewestRemotePlugin has been running
		List<PluginDescriptor> remote = registry.getKnownRemotePlugins(); 

		List<PluginDescriptor> toUpdate = detectUpdates(local, remote);
		downloadPlugins(toUpdate, callback);
		
		if (callback!=null) {
			callback.progressChanged(1.0);
		}
		return true;
	}
	
	//-------------------------------------------------------------------
	private synchronized void increase(RPGFrameworkInitCallback callback) {
		count++;
		float perc = ((float)count) / ((float)max);
		if (callback!=null)
			callback.progressChanged(perc);
	}
	
	//-------------------------------------------------------------------
	private static void deleteMappedFilesIfExists(Path path) throws IOException {
		int max = 20;
		IOException ee = null;
	    while (max>0) {
	    	max--;
	        try {
	            Files.deleteIfExists(path);
	            return;
	        } catch (AccessDeniedException e) {
	        	ee = e;
	            System.gc();
	        }
	        try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				return;
			}
	    }
	    path.toFile().deleteOnExit();
	    throw ee;
	}
	
	//-------------------------------------------------------------------
	private void downloadPlugins(List<PluginDescriptor> toDownload, RPGFrameworkInitCallback callback) {
		logger.debug("downloadPlugins: "+toDownload.size());
		max = toDownload.size();
		count = 0;
		
		ThreadGroup tgDownloads = new ThreadGroup("DownloadUpdates");
		List<Thread> threads = new ArrayList<Thread>();
		for (PluginDescriptor desc : toDownload) {
			Thread thread = new Thread(tgDownloads, () -> {
				try {
					HttpURLConnection con = (HttpURLConnection) desc.location.openConnection();
					if (con.getResponseCode()!=200) {
						desc.result = UpdateResult.FAILED;
						logger.error("Update failed for "+desc.name+" - downloading returned code "+con.getResponseCode());
						return;
					}
					Path downloadFile = pluginDir.resolve(desc.filename+".update");
					Path destFile = pluginDir.resolve(desc.filename);
					downloadFile = destFile;
					// Delete eventually existing file
//					Files.deleteIfExists(downloadFile);
					
					logger.debug("  Download "+desc.location+" to "+downloadFile);
					try {
						Files.copy(con.getInputStream(), downloadFile, StandardCopyOption.REPLACE_EXISTING);
						// Verify downloaded file
						if (downloadFile.toFile().length()!=desc.fileSize) {
							logger.warn("Download okay, but filesize does not match");
							registry.addUpdateError("Failed updaing "+downloadFile+" - verification failed");
							desc.result = UpdateResult.VERIFICATION_FAILED;
							return;
						}
					} catch (IOException e) {
						// Failed updating
						logger.error("Failed updating '"+desc.getName()+"': "+e);
						registry.addUpdateError("Plugins to be updated on next start: "+desc.name);
						desc.result = UpdateResult.FAILED;
						registry.addPluginToDeleteOnExit(desc);
						return;
					} finally {
						con.getInputStream().close();
					}
//					// Delete previous
//					try {
//						logger.info("  delete old "+destFile);
//						deleteMappedFilesIfExists(destFile);
//					} catch (Exception e) {
////						// Try to rename
////						Path oldFile = pluginDir.resolve(desc.filename+".old");
////						try {
////							logger.debug("  try move "+destFile+"  to "+oldFile);
////							Files.move(destFile, oldFile, StandardCopyOption.REPLACE_EXISTING);
////							oldFile.toFile().deleteOnExit();
////						} catch (Exception e1) {
//							logger.error("Update failed for "+desc.name+" - since deleting old "+destFile+" failed",e);
//							registry.addUpdateError("Failed updating "+desc.name+" - cannot delete "+destFile);
//							desc.result = UpdateResult.FAILED;
//							return;
////						}
//					}
//					if (desc.localToUpdate!=null) {
//						if (desc.localToUpdate.localFile!=null) {
//							try {
//								Files.delete(desc.localToUpdate.localFile);
//							} catch (Exception e) {
//								logger.error("Delete failed for "+desc.localToUpdate.localFile,e);
//							}
//						}
//					}
					// Mark downloaded as new regular
//					Files.move(downloadFile, destFile);
					logger.info("  Successfully updated  "+desc.filename+" = "+desc.name);
					desc.localFile = destFile;
					desc.result = UpdateResult.UPDATED;
					registry.updateLocalPlugin(desc);
				} catch (IOException e) {
					desc.result = UpdateResult.FAILED;
					logger.error("Update failed for "+desc.name+" - IOException",e);
					// Mark plugin to be deleted on exit
					registry.addPluginToDeleteOnExit(desc);
					registry.addUpdateError("Failed updating "+desc.name+" - "+e);
				}
				increase(callback);
			}, "Update-"+desc.name);
			threads.add(thread);
		}
		
		// Start all updates
		threads.forEach( thread -> thread.start());
		// Wait for  all downloads to finish
		threads.forEach( thread -> {
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	//-------------------------------------------------------------------
	private List<PluginDescriptor> findByUUID(UUID uuid, List<PluginDescriptor> list) {
		List<PluginDescriptor> ret = new ArrayList<PluginDescriptor>();
		for (PluginDescriptor desc : list) {
			if (desc.uuid!=null && desc.uuid.equals(uuid))
				ret.add(desc);
		}
		return ret;
	}

	//-------------------------------------------------------------------
	private List<PluginDescriptor> findByName(String name, List<PluginDescriptor> list) {
		List<PluginDescriptor> ret = new ArrayList<PluginDescriptor>();
		for (PluginDescriptor desc : list) {
			if (desc.name!=null && desc.name.equals(name))
				ret.add(desc);
		}
		return ret;
	}

	//-------------------------------------------------------------------
	private static boolean isNewer(PluginDescriptor check, PluginDescriptor reference) {
		int cmp = check.getVersion().compareTo(reference.getVersion());
		if (cmp>0)
			return true;
		if (cmp<0)
			return false;
		// Identical versions - compare file dates
		if (check.timestamp.isAfter(reference.timestamp))
			return true;
		return false;
	}

	//-------------------------------------------------------------------
	private PluginDescriptor findNewest(List<PluginDescriptor> allMatchingRemote) {
		PluginDescriptor newest = null;
		for (PluginDescriptor tmp : allMatchingRemote) {
			if (newest==null || isNewer(tmp, newest))
				newest = tmp;
		}
		return newest;
	}

	//-------------------------------------------------------------------
	/**
	 * For each installed plugin, try to find a matching remote available one
	 */
	private List<PluginDescriptor> detectUpdates(List<PluginDescriptor> localList, List<PluginDescriptor> remoteList) {
		List<PluginDescriptor> ret = new ArrayList<PluginDescriptor>();
		for (PluginDescriptor local : localList) {
			List<PluginDescriptor> allMatchingRemote = null;
			// If possible, find remote plugins by UUID
			if (local.uuid!=null)
				allMatchingRemote = findByUUID(local.uuid, remoteList);
			// If not found by UUID, try package name
			if (allMatchingRemote==null || allMatchingRemote.isEmpty())
				allMatchingRemote = findByName(local.name, remoteList);
			// From all possible remote plugins, find the newest
			PluginDescriptor newest = findNewest(allMatchingRemote);
			// Is the newest remote - if it exists - newer than the local one?
			// If so, mark it as update
			if (newest!=null && isNewer(newest, local)) {
				logger.debug("Update "+local+" with "+newest);
				newest.localToUpdate = local;
				try {
					Files.deleteIfExists(local.localFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ret.add(newest);
			} else {
				logger.debug("No update found for "+local);
			}
		}
		
		// Now find those plugins, the user requested to update, but that
		// are not available yet
		PluginRegistry registry = RPGFrameworkLoader.getInstance().getPluginRegistry();
		for (PluginDescriptor remote : remoteList) {
			List<PluginDescriptor> allMatchingLocal = null;
			// If possible, find remote plugins by UUID
			if (remote.uuid!=null)
				allMatchingLocal = findByUUID(remote.uuid, localList);
			// If not found
			if ( (allMatchingLocal==null || allMatchingLocal.isEmpty())  && (remote.getState()==PluginState.STABLE || remote.getState()==PluginState.BETA ||registry.getPluginLoading(remote.uuid))) {
				logger.info("Missing "+remote);
				ret.add(remote);
			}
		}
		
		
		return ret;
	}

}
