/**
 * 
 */
package org.prelle.rpgframework.character;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prelle.rpgframework.character.SyncEvent.Operation;

/**
 * Defines a local filesystem and one or more remote filesystems and synchronizes them.
 * 
 * @author prelle
 *
 */
@SuppressWarnings("unused")
public class FileSystemSync {
	
	public enum SyncMode {
		UPLOAD_ONLY,
		DOWNLOAD_ONLY,
		UPLOAD_AND_DOWNLOAD
	}
	
	private Logger logger ;
	
	private String name;
	private Origin localRoot;
	private List<Origin> remoteRoots;
	private SyncMode mode;
	private FileSystemSyncListener listener;
	
	private Map<String,SyncEvent> events;

	//-------------------------------------------------------------------
	public static void main(String[] args) {
		FileSystem myFS = FileSystems.getDefault();
		Path local = myFS.getPath("/home/prelle/.local/share/rpgframework/player/myself");
		Path remote1 = myFS.getPath("/tmp/test");
		Path remote2 = myFS.getPath("/tmp/test2");
		
		FileSystemSyncListener listener = new FileSystemSyncListener() {
			public void localDirectoryCreated(Path path) {
				System.out.println("** New directory "+path);
			}
		};
		
		FileSystemSync sync = new FileSystemSync("test", local, SyncMode.UPLOAD_AND_DOWNLOAD, listener);
		sync.addRemoteRoot("test.remote1",remote1);
		sync.addRemoteRoot("test.remote2",remote2);
	}
	

	//-------------------------------------------------------------------
	/**
	 */
	public FileSystemSync(String name, Path local, SyncMode mode, FileSystemSyncListener listener) {
		logger = LogManager.getLogger("rpgframework.fs."+name);
		
		this.name = name;
		this.mode = mode;
		this.localRoot = new Origin(name+".local", local, this, true);
		this.listener  = listener;
		remoteRoots = new ArrayList<Origin>();
		events      = new HashMap<>();
		logger.info("Local files located at "+local);
		
		Timer timer = new Timer("cleaner-"+name, true);
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for (SyncEvent event : new ArrayList<SyncEvent>(events.values())) {
					if (event.isFinished(localRoot, remoteRoots)) {
						logger.debug("Forget event "+event);
						events.remove(event.getKey());
					}
				}
			}
		}, 2000, 5000);
		logger.debug("Started sync thread");
	}

	//-------------------------------------------------------------------
	public SyncEvent getOrCreateEvent(SyncEvent.Operation type, Path relative) {
		String key = SyncEvent.buildKey(type, relative);
		SyncEvent event = events.get(key);
		if (event==null) {
			event = new SyncEvent(type, relative);
			events.put(key, event);
		}
		return event;
	}

	//-------------------------------------------------------------------
	public void addRemoteRoot(String name, Path remoteRoot) {
		synchronized (remoteRoots) {
			Origin remote = new Origin(name, remoteRoot, this, false);
			this.remoteRoots.add(remote);

			logger.info("Added as remote root: "+remoteRoot);

			Thread thread = new Thread(new Runnable(){
				public void run() {
					synchronized (localRoot) {
						try {
							initialSync();
						} catch (Exception e) {
							logger.error("Failed syncing",e);
						}
					}
				}}, "FileSystemSync-"+name);
			thread.setDaemon(false);

			thread.start();
		}
	}

	//-------------------------------------------------------------------
	private boolean needsUpload(Path local, Path remote) {
		if (!Files.exists(local)) { 
			logger.warn("File to check for upload does not exist: "+local);
			return false;
		}
		if (!Files.exists(remote)) { 
			return true;
		}
		
		
		long ageDiff = 0;
		try {
			if (Files.size(local)!=Files.size(remote))
				return true;

			ageDiff = Files.getLastModifiedTime(local).toMillis() - Files.getLastModifiedTime(remote).toMillis();
		} catch (IOException e) {
			logger.error("Could not determine age difference: "+e);
		}
		// Check necessity for an upload
		return ageDiff>10000;
	}

	//-------------------------------------------------------------------
	private boolean needsDownload(Path remote, Path local) {
		if (!Files.exists(remote)) { 
			logger.warn("File to check for download does not exist: "+local);
			return false;
		}
		if (!Files.exists(local)) { 
			return true;
		}
		
		
		long ageDiff = 0;
		try {
			if (Files.size(remote)!=Files.size(local))
				return true;

			ageDiff = Files.getLastModifiedTime(remote).toMillis() - Files.getLastModifiedTime(local).toMillis();
		} catch (IOException e) {
			logger.error("Could not determine age difference: "+e);
		}
		// Check necessity for an upload
		return ageDiff>10000;
	}

	//-------------------------------------------------------------------
	private void initialSync() {
		
		synchronized(remoteRoots) {
			logger.debug("Start initial sync to "+remoteRoots.size()+" remote sites");

			/*
			 * Duplicate local files to all remote sites.
			 * Iterate through files for each remote site. While doing this
			 * ignore events from that site.
			 */
//			if (mode!=SyncMode.DOWNLOAD_ONLY) {
//				logger.debug("Upload local files");
//				for (Origin remote : remoteRoots) {
//					uploadLocalChanges(remote);
//				}
//			}

			if (mode!=SyncMode.UPLOAD_ONLY) {
				logger.debug("Download remote files");
				for (Origin origin : remoteRoots) {
					downloadRemoteChanges(origin);
				}
			}
		}
//		
//		logger.fatal("Stop here");
//		System.exit(0);
	}

	//-------------------------------------------------------------------
	private void upload(Path local, Path remote, Origin origin) {
		logger.info("UPLOAD from "+local+" to "+remote);
		
		// Replacing a file will result in a DELETE and MODIFY event
		Path relative = localRoot.rootDir.relativize(local);
		SyncEvent delEvent = getOrCreateEvent(Operation.DELETE, relative);
		SyncEvent creEvent = getOrCreateEvent(Operation.CREATE, relative);
//		SyncEvent modEvent = getOrCreateEvent(Operation.MODIFY, relative);
		delEvent.addProcessedBy(localRoot); // prevents remote replacement issueing a delete
		creEvent.addProcessedBy(localRoot);
//		modEvent.addProcessedBy(localRoot);
		delEvent.addProcessedBy(origin); 
		creEvent.addProcessedBy(origin);
//		modEvent.addProcessedBy(origin);
		try {
			Files.copy(local, remote, new CopyOption[]{
					StandardCopyOption.REPLACE_EXISTING,
					StandardCopyOption.COPY_ATTRIBUTES
				    });
			Files.setLastModifiedTime(remote, Files.getLastModifiedTime(local));
		} catch (IOException e) {
			logger.error("Upload of "+remote+" failed: "+e,e);
		}
		
	}

	//-------------------------------------------------------------------
	private void download(Origin origin, Path remote, Path local) {
		logger.info("DOWNLD "+remote);
		Path relative = localRoot.rootDir.relativize(local);
		SyncEvent delEvent = getOrCreateEvent(Operation.DELETE, relative);
		SyncEvent creEvent = getOrCreateEvent(Operation.CREATE, relative);
//		SyncEvent modEvent = getOrCreateEvent(Operation.MODIFY, relative);
		delEvent.addProcessedBy(origin);
		delEvent.addProcessedBy(localRoot);
		creEvent.addProcessedBy(origin);
		creEvent.addProcessedBy(localRoot);
//		modEvent.addProcessedBy(origin);
//		modEvent.addProcessedBy(localRoot);
		try {
			Files.copy(remote, local, new CopyOption[]{
					StandardCopyOption.REPLACE_EXISTING,
					StandardCopyOption.COPY_ATTRIBUTES
				    });
			Files.setLastModifiedTime(local, Files.getLastModifiedTime(remote));
		} catch (IOException e) {
			logger.error("Download of "+remote+" failed: "+e);
		}
		
	}

	//-------------------------------------------------------------------
	private void deleteLocally(Path local) {
		logger.info("DELETE LOCAL "+local);
		try {
			Files.walkFileTree(local, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					Files.delete(dir);
					localRoot.unwatchDirectory(dir);
					return FileVisitResult.CONTINUE;
				}

			});
		} catch (IOException e) {
			logger.error("Delete of "+local+" failed: "+e);
		}

	}

	//-------------------------------------------------------------------
	private void deleteRemote(Path remote, final Origin origin) {
		logger.info("DELETE REMOTE "+remote);
		
		// Try to delete parent directory
		try {
			Files.walkFileTree(remote, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					Files.delete(dir);
					origin.unwatchDirectory(dir);
					return FileVisitResult.CONTINUE;
				}

			});
		} catch (Exception e) {
			logger.warn("Failed deleting "+remote+": "+e);
		}
		
	}

//	//-------------------------------------------------------------------
//	private void uploadLocalChanges(final Origin remote) {
//		uploadLocalChanges(remote, localRoot.rootDir);
//	}

	//-------------------------------------------------------------------
	private void uploadLocalChanges(final Origin remote, Path dir) {
		try {
			Files.walkFileTree(dir, new FileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					if (dir.getFileName().toString().equals(".svn"))
						return FileVisitResult.SKIP_SUBTREE;
					if (Files.isHidden(dir))
						return FileVisitResult.SKIP_SUBTREE;
					logger.debug("Relative "+dir+" from "+localRoot.rootDir+" ...");
					Path relative = localRoot.rootDir.relativize(dir);
					logger.debug("Relative "+dir+" from "+localRoot.rootDir+" to: "+relative);
					Path remoteDir = remote.rootDir.resolve(relative);
					logger.info(" remoteDir = "+remoteDir);
					if (!Files.exists(remoteDir)) {
						logger.info("CREATE "+remoteDir);
						// Create sync event
						SyncEvent event = getOrCreateEvent(Operation.CREATE, relative);
						event.addProcessedBy(localRoot);
						event.addProcessedBy(remote);
						
						Files.createDirectory(remoteDir);
						// TODO Watch
//						remote.watchDirectory(remoteDir);
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path local, BasicFileAttributes attrs) throws IOException {
					if (local.getFileName().toString().endsWith("~"))
						return FileVisitResult.CONTINUE;
					if (local.getFileName().toString().endsWith("#"))
						return FileVisitResult.CONTINUE;
					if (Files.isHidden(local))
						return FileVisitResult.CONTINUE;

					Path relative = localRoot.rootDir.relativize(local);
					Path remoteFile = remote.rootDir.resolve(relative);
					if (needsUpload(local, remoteFile)) {
						// Create sync event
						SyncEvent event = getOrCreateEvent(Operation.CREATE, relative);
						event.addProcessedBy(localRoot);
						event.addProcessedBy(remote);
						
						upload(local, remoteFile, remote);
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
					logger.debug("visit failed "+file);
					// TODO Auto-generated method stub
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			logger.error("Failed uploading local changes",e);
		}
	}

	//-------------------------------------------------------------------
	private void downloadRemoteChanges(final Origin origin) {
		try {
			Files.walkFileTree(origin.rootDir, new FileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					if (dir.getFileName().toString().equals(".svn"))
						return FileVisitResult.CONTINUE;
					Path relative = origin.rootDir.relativize(dir);
					Path localDir = localRoot.rootDir.resolve(relative.toString());
					if (!Files.exists(localDir)) {
						logger.debug("create local directory: "+localDir);
						Files.createDirectory(localDir);
//						localRoot.watchDirectory(localDir);
					}
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFile(Path remote, BasicFileAttributes attrs) throws IOException {
					if (remote.getFileName().toString().endsWith("~"))
						return FileVisitResult.CONTINUE;
					if (remote.getFileName().toString().endsWith("#"))
						return FileVisitResult.CONTINUE;
					
					Path relative = origin.rootDir.relativize(remote);
					Path local = localRoot.rootDir.resolve(relative.toString());
					if (needsDownload(remote, local)) {
						logger.info("Would download "+remote+" to local "+local+" from "+origin);
						System.exit(0);
						download(origin, remote, local);
					}

					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
					logger.debug("visit failed "+file);
					// TODO Auto-generated method stub
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			logger.error("Failed uploading local changes",e);
		}
	}
	
	//-------------------------------------------------------------------
	/**
	 * Upload a local change to all remote sites that want to receive
	 * events
	 */
	void eventLocalPathCreated(SyncEvent event, Path local) {
		if (mode==SyncMode.DOWNLOAD_ONLY)
			return;
		
		Path relative = event.getPath();
		if (Files.isDirectory(local))
			localRoot.watchDirectory(local);

		for (Origin origin : remoteRoots) {
			if (event.hasBeenProcessedBy(origin)) {
				logger.debug("Path already created at "+origin);
				continue;
			}
			event.addProcessedBy(origin);
			
			Path remote = origin.rootDir.resolve(relative);
			logger.debug("Create at "+origin+": "+event.getPath());
			try {
				if (Files.isDirectory(local)) {
					Files.createDirectory(remote);
					// Sync files already existing in the directory - in 
					// case other process already wrote data here
					logger.warn("TODO: check for contents already in dir "+local);
					uploadLocalChanges(origin, local);
				} else if (Files.isRegularFile(local))
					Files.createFile(remote);
			} catch (IOException e) {
				logger.warn("Failed creating remote "+remote+": "+e);
			}
			
		}
	}
	
	//-------------------------------------------------------------------
	void eventLocalFileChanged(SyncEvent event, Path local) {
		if (mode==SyncMode.DOWNLOAD_ONLY)
			return;
		
		Path relative = event.getPath();
		for (Origin origin : remoteRoots) {
			if (event.hasBeenProcessedBy(origin)) {
				logger.debug("Path already modified at "+origin);
				continue;
			}
			event.addProcessedBy(origin);
			
			Path remote = origin.rootDir.resolve(relative);
			if (!Files.exists(remote)) {
				logger.warn("Local file changed, but remote file not found: "+remote);
//				continue;
			}
			
			upload(local, remote, origin);
		}
	}
	
	//-------------------------------------------------------------------
	void eventLocalFileDeleted(SyncEvent event, Path local) {
		if (mode==SyncMode.DOWNLOAD_ONLY)
			return;
		
		logger.debug("EVENT local path deleted");
		Path relative = localRoot.rootDir.relativize(local);
		for (Origin origin : remoteRoots) {
			if (event.hasBeenProcessedBy(origin))
				continue;
			event.addProcessedBy(origin);
			
			Path remote = origin.rootDir.resolve(relative);
			if (!Files.exists(remote)) {
				logger.warn("Local file deleted, but remote file not found: "+remote);
				continue;
			}
			
			deleteRemote(remote, origin);
		}
	}
	
	//-------------------------------------------------------------------
	void eventRemotePathCreated(SyncEvent event, Origin origin, Path remote) {
		if (mode==SyncMode.UPLOAD_ONLY)
			return;
		
		Path relative = origin.rootDir.relativize(remote);
		Path local = localRoot.rootDir.resolve(relative);
		if (Files.exists(local)) {
			logger.warn("Received event for remote creation of an path that already exists locally. This should have been blocked.");
			return;
		}
		
		try {
			if (Files.isDirectory(remote)) {
				Files.createDirectory(local);
				localRoot.watchDirectory(local);
			} else if (Files.isRegularFile(remote)) {
				Files.createFile(local);
				download(origin, remote, local);
			}
		} catch (IOException e) {
			logger.error("Failed reacting on remote path creation",e);
		}
	}
	
	//-------------------------------------------------------------------
	void eventRemoteFileChanged(SyncEvent event, Origin origin, Path remote) {
		if (mode==SyncMode.UPLOAD_ONLY)
			return;
		
		Path relative = origin.rootDir.relativize(remote);
		Path local = localRoot.rootDir.resolve(relative);
		if (!Files.exists(local)) {
			logger.warn("Received event for remote change of an path that does not exist locally. This should have been created beforehand");
			return;
		}
		if (!Files.isRegularFile(remote)) {
			logger.warn("Received event for remote change of an path that is not a regular file");
			return;
		}
		
		download(origin, remote, local);
	}
	
	//-------------------------------------------------------------------
	void eventRemoteFileDeleted(SyncEvent event, Origin origin, Path remote) {
		if (mode==SyncMode.UPLOAD_ONLY)
			return;
		
		Path relative = origin.rootDir.relativize(remote);
		logger.warn("TODO: remote delete");
		Path local = localRoot.rootDir.resolve(relative);
		if (!Files.exists(local)) {
			logger.warn("Received event for remote deletion of an path that does not exist locally. This should not happen");
			return;
		}
		
		deleteLocally(local);
	}

}
//-------------------------------------------------------------------
//-------------------------------------------------------------------
class SyncEvent {
	public enum Operation {
		CREATE,
		MODIFY,
		DELETE
	}
	private Path relative;
	private Operation type;
	private List<Origin> processedBy = new ArrayList<>();
	public SyncEvent(Operation op, Path relative) {
		this.type = op;
		this.relative  = relative;
	}
	public String getKey() { return buildKey(type, relative); }
	public Path getPath() { return relative; }
	public Operation getType() { return type; }
	public String toString() { return type+" "+relative+"  "+processedBy; }
	@Override
	public int hashCode() {
		return getKey().hashCode();
	}
	@Override
	public boolean equals(Object o) {
		if (o instanceof SyncEvent) {
			return getKey().equals( ((SyncEvent)o ).getKey() );
		}
		return false;
	}
	public void addProcessedBy(Origin origin) {
		if (!processedBy.contains(origin))
			processedBy.add(origin);
	}
	public boolean hasBeenProcessedBy(Origin origin) {
		return processedBy.contains(origin);
	}
	public boolean isFinished(Origin local, List<Origin> remote) {
		if (!hasBeenProcessedBy(local)) return false;
		for (Origin orig : remote)
			if (!hasBeenProcessedBy(orig)) return false;
		return true;
	}
	public static String buildKey(Operation type, Path relative) {
		return type.name()+"-"+relative.toString();
	}
}

//-------------------------------------------------------------------
//-------------------------------------------------------------------
class Origin {
	
	private Logger logger ;

	String name;
	FileSystemSync callback;
	Path rootDir;
	boolean isLocal;
	WatchService watcher;
	private Map<WatchKey, Path> watchedDirs;
	
	private Thread watchThread;
	
	//-------------------------------------------------------------------
	public Origin(String name, Path root, FileSystemSync sync, boolean isLocal) {
		this.name = name;
		logger = LogManager.getLogger("rpgframework.fs."+name);
		this.rootDir = root;
		this.callback = sync;
		this.isLocal  = isLocal;
		watchedDirs = new HashMap<>();
		try {
			watcher = rootDir.getFileSystem().newWatchService();
			watchThread = new Thread(new Runnable(){
				public void run() {
					watch();
				}}, "FileSystemWatch-"+name);
			watchThread.start();
			
//			watchDirectory(root);
//			watchExistingDirectories();
		} catch (IOException e) {
			logger.fatal("Failed setting up filesystem watcher for "+rootDir,e);
		}
	}
	
	//-------------------------------------------------------------------
	public String toString() {
		return name;
	}

//	//-------------------------------------------------------------------
//	private void watchExistingDirectories() throws IOException {
//		Files.walkFileTree(rootDir, new SimpleFileVisitor<Path>() {
//			@Override
//			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attr) throws IOException {
//				watchDirectory(dir);
//				return FileVisitResult.CONTINUE;
//			}
//
//		});
//		
//	}

	//-------------------------------------------------------------------
	@SuppressWarnings("unused")
	public void watchDirectory(Path dir) {
		logger.debug("watch "+dir);
		if (true)
//		if (rootDir.getFileSystem()!=dir.getFileSystem())
			throw new IllegalArgumentException("Cannot watch files from other filesystems");

		try {
			WatchKey key = dir.register(watcher,
					StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_DELETE,
					StandardWatchEventKinds.ENTRY_MODIFY);
			watchedDirs.put(key, dir);
		} catch (IOException e) {
			logger.error("Failed creating watcher for "+dir+": "+e);
		}
	}

	//-------------------------------------------------------------------
	public void unwatchDirectory(Path dir) {
		if (rootDir.getFileSystem()!=dir.getFileSystem())
			throw new IllegalArgumentException("Cannot unwatch files from other filesystems");

		// Find key
		WatchKey key = null;
		for (Entry<WatchKey, Path> entry : watchedDirs.entrySet()) {
			if (entry.getValue().equals(dir)) {
				key = entry.getKey();
				break;
			}
		}
		if (key==null) {
			logger.warn("Cannot unwatch unknown "+dir);
			throw new IllegalArgumentException("I am not watching "+dir);
		}
		logger.debug("Unwatch "+key+"  ("+dir+")");
		System.err.println("Unwatch "+dir);
		key.cancel();
		watchedDirs.remove(key);
	}

	//-------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	private void watch() {
		WatchKey key = null;
        try {
			while (true) {
				logger.debug("Watch for changes");
			    key = watcher.take();
			    Path dir = watchedDirs.get(key);

			    Kind<?> kind = null;
			    for (WatchEvent<?> watchEvent : key.pollEvents()) {
		            Path path = dir.resolve( ((WatchEvent<Path>) watchEvent).context() );
			    	
			        // Get the type of the event
			        kind = watchEvent.kind();
			        logger.debug("event "+kind+" / "+path);
			        if (StandardWatchEventKinds.OVERFLOW == kind) {
			            continue; // loop
			        } else if (StandardWatchEventKinds.ENTRY_CREATE == kind) {
			            // A new Path was created
						Path relative = rootDir.relativize(path);
				        SyncEvent event = callback.getOrCreateEvent(Operation.CREATE, relative);
				        if (event.hasBeenProcessedBy(this)) {
//				        	logger.debug("Ignore already processed "+event);
				        	continue;
				        }
				        event.addProcessedBy(this);
				        logger.debug(event);
				        
				        if (isLocal)
				        	callback.eventLocalPathCreated(event, path);
				        else
				        	callback.eventRemotePathCreated(event, this, path);
			        } else if (StandardWatchEventKinds.ENTRY_MODIFY == kind) {
						Path relative = rootDir.relativize(path);
				        SyncEvent event = callback.getOrCreateEvent(Operation.MODIFY, relative);
				        if (event.hasBeenProcessedBy(this)) {
//				        	logger.debug("Ignore already processed "+event);
				        	continue;
				        }
				        event.addProcessedBy(this);
				        logger.debug(event);
				        
				        if (isLocal)
				        	callback.eventLocalFileChanged(event, path);
				        else
				        	callback.eventRemoteFileChanged(event, this, path);
			        } else if (StandardWatchEventKinds.ENTRY_DELETE == kind) {
						Path relative = rootDir.relativize(path);
				        SyncEvent event = callback.getOrCreateEvent(Operation.DELETE, relative);
				        if (event.hasBeenProcessedBy(this)) {
//				        	logger.debug("Ignore already processed "+event);
				        	continue;
				        }
				        event.addProcessedBy(this);
				        logger.debug("Path deleted: " +event);
				        
				        if (isLocal)
				        	callback.eventLocalFileDeleted(event, path);
				        else
				        	callback.eventRemoteFileDeleted(event, this, path);
			        }
			    }

			    if (!key.reset()) {
			        break; // loop
			    }
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
