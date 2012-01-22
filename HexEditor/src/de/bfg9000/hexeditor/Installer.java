/*
 * Copyright (c) 2012, Thomas Werner
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 *   disclaimer.
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.bfg9000.hexeditor;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;
import org.openide.windows.WindowManager;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        WindowManager.getDefault().invokeWhenUIReady(new ActionInstaller());
    }
    
    @Override
    public void uninstalled() {
        WindowManager.getDefault().invokeWhenUIReady(new ActionUninstaller());
    }

    /**
     * A Runnable that delegates all known filetypes to an abstract method for further processing.
     */
    private static abstract class FileTypeHandler implements Runnable {
        
        @Override
        public void run() {
            final FileObject loaders = FileUtil.getConfigFile("Loaders");
            final FileObject[] categories = loaders.getChildren();
            for(FileObject category: categories) {
                final FileObject[] fileTypes = category.getChildren();
                for(FileObject fileType: fileTypes)
                    handleFileType(fileType);
            }
        }
        
        protected abstract void handleFileType(FileObject fileType);
        
    }
    
    /**
     * Creates references to the 'Open As Hex' action for all known file types. This is done when all modules are loaded
     * and thus all file types have been registered.
     */
    private static final class ActionInstaller extends FileTypeHandler {

        @Override
        protected void handleFileType(FileObject fileType) {
            try {
                final FileObject actionsFolder = FileUtil.createFolder(fileType, "Actions");
                final FileObject hexAction = actionsFolder.getFileObject("de-bfg9000-hexeditor-OpenAsHexAction.shadow");
                if(null == hexAction) {
                    final FileObject action = actionsFolder.createData("de-bfg9000-hexeditor-OpenAsHexAction.shadow");
                    action.setAttribute("originalFile", "Actions/File/de-bfg9000-hexeditor-OpenAsHexAction.instance");
                    action.setAttribute("position", 175);
                }
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            
        }
        
    }
    
    /**
     * Creates references to the 'Open As Hex' action for all known file types. This is done when all modules are loaded
     * and thus all file types have been registered.
     */
    private static final class ActionUninstaller extends FileTypeHandler {

        @Override
        protected void handleFileType(FileObject fileType) {
            try {
                final FileObject actionsFolder = FileUtil.createFolder(fileType, "Actions");
                final FileObject hexAction = actionsFolder.getFileObject("de-bfg9000-hexeditor-OpenAsHexAction.shadow");
                if(null != hexAction)
                    hexAction.delete();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            
        }
        
    }
    
}
