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

public class CreateFile {

    private static final String FILE_CONFIG = "config.cfg";

    public static CreateFile instance;

    private CreateFile(){}

    public static CreateFile getInstance(){
        return instance == null ? instance = new CreateFile() : instance;
    }

    /**
     * @param path  must be the component name
     * @param status the options must be between 200 and 299
     * @return Should Return Object [Boolean, CheckPoint] for Utils.CheckStatus
     * @exception Throw a new exception if the status value is a invalid option
     */
    public Object[] createFile(String componentName, CheckPoint status) {

        // create a null file to receive an assignment from a option below
        File file = null;
        // store result
        Boolean result = null;
        try {

            Properties propConfig = Utils.getInstance().loadConfig(FILE_CONFIG);

            final String PATH_FOLDER_COMPONENT = "./" + propConfig.getProperty("PATH_COMPONENTS") + "/" + componentName +"/";
            final String PATH_CONTENT_XML_FILE_ROOT = PATH_FOLDER_COMPONENT + "content.xml";
            final String PATH_FILE_HTML = PATH_FOLDER_COMPONENT+componentName+".html";
            final String PATH_CONTENT_XML_DIALOG_FILE = PATH_FOLDER_COMPONENT+"_cq_dialog/content.xml";

            switch (status.getStatus()) {

                // CREATE_CONTENT_XML_ROOT(200)
                case 200:
                    file = new File(PATH_CONTENT_XML_FILE_ROOT);
                    result = file.createNewFile();
                    break;

                // CREATE_COMPONENT_HTML(201)
                case 201:
                    file = new File(PATH_FILE_HTML);
                    result = file.createNewFile();
                    break;

                // CREATE_CONTENT_XML_DIALOG(202)
                case 202:
                    file = new File(PATH_CONTENT_XML_DIALOG_FILE);
                    result = file.createNewFile();
                    break;
            }

        } catch (Exception ex) {
            
            ex.addSuppressed(new ComponentException("\nError at class " + this.getClass().getName(),status));
            ex.printStackTrace();

        }

        // Should Return a Object Containing the result and the status
        return new Object[] {
                result,
                status
        };

    }
}
