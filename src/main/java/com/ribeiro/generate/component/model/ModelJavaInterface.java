package com.ribeiro.generate.component.model;
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

import java.util.List;

import lombok.Getter;

public class ModelJavaInterface {
    
    @Getter
    private String packageName;
    
    @Getter
    private String nameInterface;
    
    private StringBuilder methodsList;

    public ModelJavaInterface(String packageName, String nameInterface, List<String> methodsList){
        // Package Line
        this.packageName = "package " + packageName + ";\n\n";
        // Interface Line
        this.nameInterface = "public interface " + nameInterface.substring(0, 1).toUpperCase() + nameInterface.substring(1) + " {\n";
        
        // Methods Line
        this.methodsList = new StringBuilder();
        methodsList.forEach((e) -> {
            String name = e.substring(0, 1).toUpperCase() + e.substring(1);
            this.methodsList.append("   String " + "get"+name+"();\n\n");
        });

        // Final File
        this.methodsList.append("}");
    }


    public String getMethodsList(){
        return methodsList.toString();
    }

}
