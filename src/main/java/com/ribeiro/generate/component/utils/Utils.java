package com.ribeiro.generate.component.utils;

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
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.ribeiro.generate.component.controller.CheckPoint;
import com.ribeiro.generate.component.exception.ComponentException;
import com.ribeiro.generate.component.xml.XmlHelper;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Utils {
    
    private static Utils instance;
    private static final String PATH_ROOT_XML = "";
    private static final String PATH_ROOT_PROP = "./config/";
    private static final CheckPoint status = CheckPoint.READING_PROPERTIES;
    private Utils(){}

    public static Utils getInstance(){
        
        if(instance == null){
            instance = new Utils();
        }

        return instance;
    }

    public Properties loadConfig(String fileName){
        
        try{

            Properties prop = new Properties();
            FileInputStream in = new FileInputStream(new File(PATH_ROOT_PROP+fileName));
            prop.load(in);
            return prop;

        }catch(Exception ex){

            ex.addSuppressed(new ComponentException("\nError at class " + this.getClass().getName(),status));
            ex.printStackTrace();

        }
        
        return null;
    }

    // Imprime o Status atual do CheckPoint
    public void checkStatus(Object[] obj) {
        Boolean result = (Boolean) obj[0];
        CheckPoint status = (CheckPoint) obj[1];

        System.out.println(status.name() + " : " + result);
    }

    /**
     * @important This function read only from internal folder xml
     */
    public Document readXML(String fileName) throws Exception{
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputStream in = XmlHelper.getInstance().getClass().getResourceAsStream(PATH_ROOT_XML+fileName+".xml");
        Document doc = db.parse(in);
        
        return doc;
    }

    /**
     * @important This function read only from internal folder xml
     */
    public Document readXML(File file) throws Exception{
        
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        FileInputStream fis = new FileInputStream(file);
        Document doc = db.parse(fis);

        return doc;
    }

    public Boolean saveXML(File file, Node node) throws Exception {
        
        try{
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult outputTarget = new StreamResult(file);
            DOMSource xmlSource = new DOMSource(node);
            
            transformer.transform(xmlSource, outputTarget);
            
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return true;
    }

}
