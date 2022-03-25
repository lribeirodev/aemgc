package com.ribeiro.generate.component.controller;
/**
 * The MIT License (MIT)
* Copyright © 2022 Lucas Ribeiro
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
* and associated documentation files (the “Software”), to deal in the Software without restriction, 
* including without limitation the rights to use, copy, modify, merge, publish, distribute, 
* sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is 
* furnished to do so, subject to the following conditions:

* The above copyright notice and this permission notice shall be included in all copies or 
* substantial portions of the Software.

* THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING 
* BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
* NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
* DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, 
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import java.io.File;
import java.util.Properties;

import com.ribeiro.generate.component.exception.ComponentException;
import com.ribeiro.generate.component.utils.Utils;

public class CreateFolder {
    
    private static final String FILE_CONFIG = "config.cfg";
    public static CreateFolder instance;

    private CreateFolder(){}

    public static CreateFolder getInstance(){
        return instance == null ? instance = new CreateFolder() : instance;
    }

    /**
     * @param componentName   must be the component name
     * @param status the options must be between 100 and 199
     * @return Should Return Object [Boolean, CheckPoint] for Utils.CheckStatus
     * @exception Throw a new exception if the status value is a invalid option
     */
    public Object[] createPath(String componentName, CheckPoint status) {

        // create a null file to receive an assignment from a option below
        File file = null;
        // store result
        Boolean result = null;
        
        try {

            Properties propConfig = Utils.getInstance().loadConfig(FILE_CONFIG);
            componentName = "./" + propConfig.get("PATH_COMPONENTS") + "/" + componentName;
            switch (status.getStatus()) {

                // CREATE_COMPONENT_FOLDER(100)
                case 100:
                    file = new File(componentName);
                    result = file.mkdir();
                    break;
                // CREATE_FOLDER_DIALOG_AUTHORING(101)
                case 101:
                    final String folderName = "/_cq_dialog";
                    file = new File(componentName + folderName);
                    result = file.mkdir();
                    break;
            }

        } catch (Exception ex) {
            
            ex.addSuppressed(new ComponentException("\nError at class " + this.getClass().getName(), status));
            ex.printStackTrace();

        }

        // Should Return a Object Containing the result and the status
        return new Object[] {
                result,
                status
        };
    }
}
