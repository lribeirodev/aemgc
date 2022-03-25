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

public class ModelJavaImpl {

    @Getter
    private String packageName;

    private StringBuilder importedClasses;

    private StringBuilder attributesImpl;

    private StringBuilder nameModel;

    private final String[] importsClasses = {
            "import org.apache.sling.api.SlingHttpServletRequest;",
            "import org.apache.sling.models.annotations.DefaultInjectionStrategy;",
            "import org.apache.sling.models.annotations.Model;",
            "import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;\n",
            "import lombok.Getter;\n"
    };

    public ModelJavaImpl(String packageName, String extendInterface, List<String> attributesImpl) {

        // Package Line
        this.packageName = "package " + packageName + ".impl;\n\n";

        // Imports Line
        importedClasses = new StringBuilder();
        importedClasses.append("import " + packageName + "." + extendInterface + ";\n\n");
        for (String im : importsClasses) {
            importedClasses.append(im + "\n");
        }

        // Model e Class Declaration Line
        this.nameModel = new StringBuilder();
        this.nameModel.append("@Model(\n    adaptables = {SlingHttpServletRequest.class},\n");
        this.nameModel.append("    adapters = " + extendInterface + ".class,\n");
        this.nameModel.append("    resourceType = " + extendInterface + "Impl.RESOURCE_TYPE,\n");
        this.nameModel.append("    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)\n");
        this.nameModel.append("public class " + extendInterface + "Impl implements " + extendInterface + " {");

        // Attributes Line
        this.attributesImpl = new StringBuilder();
        this.attributesImpl.append("\n\n    public static final String RESOURCE_TYPE = \"\";");
        attributesImpl.forEach((e) -> {
            this.attributesImpl.append("\n\n   @Getter\n   @ValueMapValue\n   private String " + e + ";");
        });

        // Final File
        this.attributesImpl.append("\n}");
    }

    public String getAttributesImpl() {
        return attributesImpl.toString();
    }

    public String getImportedClasses() {
        return importedClasses.toString();
    }

    public String getNameModel() {
        return nameModel.toString();
    }
}
