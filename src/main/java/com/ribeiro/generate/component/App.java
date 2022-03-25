package com.ribeiro.generate.component;

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

import java.io.InputStream;

import com.ribeiro.generate.component.controller.CheckPoint;
import com.ribeiro.generate.component.controller.CreateFolder;
import com.ribeiro.generate.component.controller.CreateFile;
import com.ribeiro.generate.component.controller.WriteFile;
import com.ribeiro.generate.component.utils.Utils;

class App {

    public static void main(String[] args) {
        run(args);
    }

    private static void run(String[] componentName) {

        // try (Scanner in = new Scanner(System.in)) {

        //     System.out.print("generate component: ");
        //     componentName = in.nextLine().split(" ");

            for (CheckPoint status : CheckPoint.values()) {

                int index = status.getStatus();
                
                Object[] result = null;

                int min = 100;
                int max = 400;

                if (index >= min && index < 200) {
                    
                    result = CreateFolder
                                .getInstance()
                                .createPath(componentName[0], status);

                } else if (index >= 200 && index < 300) {

                    result = CreateFile
                                .getInstance()
                                .createFile(componentName[0], status);
                
                } else if (index >= 300 && index < max){
                        
                        result = WriteFile
                                .getInstance()
                                .writeFile(
                                    componentName[0],
                                    componentName.length > 1 ? componentName[1] : "default", status,
                                    componentName.length > 2 ? componentName[2] : ""
                                    );
                }

                // Print Checkpoint and status
                if (index >= min && index <= max) Utils.getInstance().checkStatus(result);
                
            
            // }
        };
    }    

    public InputStream getPath(String fileName){
        return getClass().getResourceAsStream(fileName);
    }

}