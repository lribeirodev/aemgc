package com.ribeiro.generate.component.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ribeiro.generate.component.exception.ComponentException;
import com.ribeiro.generate.component.model.Model;
import com.ribeiro.generate.component.model.ModelFactory;
import com.ribeiro.generate.component.model.ModelJavaImpl;
import com.ribeiro.generate.component.model.ModelJavaInterface;
import com.ribeiro.generate.component.utils.Utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class WriteFile {

    private static final String FILE_GROUP = "group.cfg";
    private static final String FILE_CONFIG = "config.cfg";
    private static Properties propConfig = null;
    private static Properties propGroup = null;

    public static WriteFile instance;

    private WriteFile() {
    }

    public static WriteFile getInstance() {
        
        if(instance == null){
            instance = new WriteFile();
            propConfig = Utils.getInstance().loadConfig(FILE_CONFIG);
            propGroup = Utils.getInstance().loadConfig(FILE_GROUP);
        }

        return instance;
    }

    /**
     * @param componentName must be the component name
     * @param status        the options must be between 300 and 399
     * @return Should Return Object [Boolean, CheckPoint] for Utils.CheckStatus
     * @exception Throw a new exception if the status value is a invalid option
     */
    public Object[] writeFile(String componentName, String componentOption, CheckPoint status, String properties) {

        // create a null file to receive an assignment from a option below
        File file = null;
        // store result
        Boolean result = null;

        try {

            switch (status.getStatus()) {

                // WRITE_CONTENT_XML_ROOT(300)
                case 300:
                    result = writeContentXmlRoot(file, componentName, componentOption, status);
                    break;
                // WRITE_CONTENT_DIALOG_AUTHORING(301)
                case 301:
                    result = writeContentDialogAuthoring(file,componentName, componentOption, status);
                    break;
                // WRITE_PROPERTIES_DIALOG_AUTHORING(302)
                case 302:
                    result = writePropertiesDialogAuthoring(file,componentName,componentOption,status, properties);
                // WRITE_FILE_TO_JAVA(303)
                case 303:
                    result = writeFileToJava(file,componentName,componentOption,status, properties);
            }

        } catch (FileNotFoundException fn) {
            
            new Exception("Invalid option try again").printStackTrace();

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

    private Boolean writeContentXmlRoot(File file, String componentName, String componentOption, CheckPoint status)
            throws Exception {

        file = new File("./" + propConfig.get("PATH_COMPONENTS") + "/" + componentName + "/content.xml");
        Document doc = Utils.getInstance().readXML(componentOption);
        NodeList nodeList = doc.getElementsByTagName("jcr:root");
        Node xml = nodeList.item(0);

        System.out.println("Chose a Available Group: ");
        propGroup.forEach((k, v) -> {
            System.out.println(k + " : " + v);
        });
        System.out.print("Type a number: ");

        Map<String, String> map = new HashMap<>();
        map.put("jcr:title", componentName);
        try (Scanner in = new Scanner(System.in)) {
            map.put(
                    "componentGroup",
                    propGroup.getProperty(
                            in.nextLine(),
                            "Group not found"));
        }

        map.forEach((k, v) -> {
            xml.getAttributes()
                    .getNamedItem(k)
                    .setNodeValue(v);
        });

        return Utils.getInstance().saveXML(file, xml);
    }

    private Boolean writeContentDialogAuthoring(File file, String componentName, String componentOption, CheckPoint status) throws Exception{

        file = new File("./" + propConfig.getProperty("PATH_COMPONENTS") + "/" + componentName + "/_cq_dialog/content.xml");
        Document doc = Utils.getInstance().readXML("_cq_dialog_"+componentOption);
        NodeList nodeList = doc.getElementsByTagName("jcr:root");
        Node xml = nodeList.item(0);

        xml.getAttributes()
                .getNamedItem("jcr:title")
                .setNodeValue(componentName);

        return Utils.getInstance().saveXML(file, xml);
    }

    private Boolean writePropertiesDialogAuthoring(File file, String componentName, String componentOption, CheckPoint status, String properties) throws Exception{

        List<Node> nodeList = mapNodeToList(properties);
        
        file = new File("./"+ propConfig.getProperty("PATH_COMPONENTS") + "/" + componentName +"/_cq_dialog/content.xml");
        
        Document doc = Utils.getInstance().readXML(file);
        Node nextNode = doc.getChildNodes().item(0);

        int cont = 0;
        while(cont < 10){
            nextNode = nextNode.getChildNodes().item(1);
            cont++;
        }

        for(Node importedNode : nodeList){
            Node newChild = doc.importNode(importedNode, true);
            Node renamed = doc.renameNode(newChild, "", newChild.getAttributes().getNamedItem("name").getNodeValue().substring(2));
            nextNode.appendChild(renamed);
        }

        Utils.getInstance().saveXML(file, doc);

        return true;
    }

    private Boolean writeFileToJava(File file, String componentName, String componentOption, CheckPoint status, String properties) throws Exception{
        
        // Recupera os Valores do Node e os Adiciona em um Array de String
        List<Node> nodeList = mapNodeToList(properties);
        List<String> methodsList = new ArrayList<>();

        nodeList.forEach((node) -> {
            methodsList.add(node.getAttributes().getNamedItem("name").getNodeValue().substring(2));
        });

        // Fluxo Criação e Salvamento do Arquivo Interface
        ModelJavaInterface modelInterface = new ModelJavaInterface(propConfig.getProperty("PACKAGE_BASE"), componentName, methodsList);
        
        File outInterface = new File("./" + propConfig.getProperty("PATH_JAVA") + "/" + (componentName.substring(0,1).toUpperCase() + componentName.substring(1)) + ".java");
        
        FileOutputStream outInter = new FileOutputStream(outInterface);
        String[] fluxoInterface = {
            modelInterface.getPackageName(),
            modelInterface.getNameInterface(),
            modelInterface.getMethodsList()
        };

        for(String outFluxoInterface : fluxoInterface){
            outInter.write(outFluxoInterface.getBytes());
        }

        outInter.flush();
        outInter.close();

        // Fluxo Criação e Salvamento do Arquivo de Implementação
        File outModel = new File("./" + propConfig.getProperty("PATH_JAVA") + "/impl/" + (componentName.substring(0,1).toUpperCase() + componentName.substring(1)) + "Impl.java");

        ModelJavaImpl modelImpl = new ModelJavaImpl( propConfig.getProperty("PACKAGE_BASE"), componentName.substring(0, 1).toUpperCase() + componentName.substring(1), methodsList);
        String[] fluxoImpl = {
            modelImpl.getPackageName(),
            modelImpl.getImportedClasses(),
            modelImpl.getNameModel(),
            modelImpl.getAttributesImpl()
        };

        FileOutputStream outImpl = new FileOutputStream(outModel);
        for(String outFluxoImpl : fluxoImpl){
            outImpl.write(outFluxoImpl.getBytes());
        }

        outImpl.flush();
        outImpl.close();
        
        return true;
    }

    // Metodos Auxiliares dos Metodos Principais
    private List<Node> mapNodeToList(String properties) throws Exception {
        
        // FIM DO FLUXO DE MAPEAR O MODEL
        List<Node> nodeList = new ArrayList<>(); 
        
        JsonElement jsonElement = JsonParser.parseString(properties);
        JsonArray jsonArr = jsonElement.getAsJsonArray();
        
        jsonArr.forEach((e) -> {
            
            JsonObject jsonObj = e.getAsJsonObject();

            jsonObj.getAsJsonObject().keySet().forEach((key) -> {
             
                try { 
                    // Recebe sempre um novo documento
                    Document doc = Utils.getInstance().readXML("fields");
                    Node itemDialog = doc.getElementsByTagName(key).item(0);
                
                    // Mapeia o Objeto com a Key
                    Model modelObj = ModelFactory.getInstance().build(key);
                    Method[] modelObjMethods = modelObj.getClass().getMethods();
                    for(Method m : modelObjMethods){
                        
                        if(m.getName().contains("set")){
                            m.invoke(modelObj, jsonObj.get(key).getAsString());
                        }
    
                    }
    
                    // Recupera os Valores e Seta eles no Atributo Correspondente do Node
                    for(Method m : modelObjMethods){
                        String nameMethod = m.getName().substring(3).toLowerCase();
                        
                        if(m.getName().contains("get") && m.getName() != "getClass"){
                            String nodeValue = String.valueOf(m.invoke(modelObj));

                            for(int i = 0; i < itemDialog.getAttributes().getLength(); i++){
    
                                String attrName = itemDialog.getAttributes().item(i).getNodeName().toLowerCase();
    
                                if(attrName.equals(nameMethod)){
                                    itemDialog.getAttributes().item(i).setNodeValue(nodeValue);
                                }
                            }
                            
                        }
    
                    }
                    
                    nodeList.add(itemDialog);
                    
                }catch (Exception ex){
                    ex.printStackTrace();
                }
    
            });
            
        });
        // FIM DO FLUXO DE MAPEAMENTO O MODEL
        
        return nodeList;
    }
}
