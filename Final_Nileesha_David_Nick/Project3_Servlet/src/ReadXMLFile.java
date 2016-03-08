

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jason.JSONException;
import org.jason.JSONObject;
import org.jason.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
//import org.json.*;



public class ReadXMLFile {

    static String json;
  public static void main(String[] args) {
    try {
        
    	StreamResult xml= mergeFiles("C:\\Users\\Sajani\\Desktop\\merge1.xml","C:\\Users\\Sajani\\Desktop\\merge2.xml");
       
    	String json=getJSON("C:\\Users\\Sajani\\Desktop\\merge1.xml","C:\\Users\\Sajani\\Desktop\\merge2.xml");
    	FileWriter output= new FileWriter ("JSON.xml");
    	System.out.println(json);
    	output.write(json);
    	output.close();
    } catch (Exception e) {
    	e.printStackTrace();
        System.out.println(e.getMessage());
    }
  }
  
    public static StreamResult mergeFiles(String in1, String in2) throws Exception{
        File file1 = new File(in1);
        File file2 = new File(in2);
        DocumentBuilderFactory docfactory=DocumentBuilderFactory.newInstance();
        docfactory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder dBuilder = docfactory.newDocumentBuilder();
        Document doc1 = dBuilder.parse(file1);//import first CCD and parse
        doc1.getDocumentElement().normalize();
        Document doc2 = dBuilder.parse(file2);//import second CCD and parse
        doc2.getDocumentElement().normalize();
        NodeList list1=doc1.getLastChild().getChildNodes();//returns a NodeList of ClinicalDocument children (realmCode, title, author, etc.)
        int index=0, inc=0;
        Node temp1=list1.item(index),temp2=doc2.getLastChild().getChildNodes().item(index), temp3;
        while(!temp2.getNodeName().equals("component")){//Cycle through children of second file until we reach <component>
            if(!areEqual(temp1,temp2)&&!temp1.getNodeName().equals("effectiveTime")&&!temp1.getNodeName().equals("id")){//if they are equivalent let it remain. only use file1's <effectiveTime> and <id>
                if(temp1.getNodeName().equals("title"))
                    temp1.setTextContent(temp1.getTextContent()+" / "+temp2.getTextContent());//change the file's title to a combination of both files' titles
                else{
                    inc++; //adjust file1's index, now that we're adding a node into the middle of it
                    temp3=doc1.importNode(temp2, true);
                    doc1.getLastChild().insertBefore(temp3, temp1); //insert node from file2 to the current index of file1
                }
            }
            index++; 
            temp1=list1.item(index+inc); //shift to next sibling in file1
            temp2=temp2.getNextSibling(); //shift to next sibling in file2
        }
        temp1=temp1.getFirstChild(); //go down a level into the <component> field
        temp2=temp2.getFirstChild();
        while(!temp1.getNodeName().equals("structuredBody")){ //make sure the current index is the <structuredBody> field
            temp1=temp1.getNextSibling();
            temp2=temp2.getNextSibling();
        }
        Node body1=temp1, body2=temp2;
        Node section1, section2, area1, area2;
        for(int i=1;i<body1.getChildNodes().getLength();i+=2){//execute following loop for every <component> within the body
            section1=body1.getChildNodes().item(i).getChildNodes().item(1);//go down a level, returning the <section> field
            section2=body2.getChildNodes().item(i).getChildNodes().item(1);//go down a level, returning the <section> field
            temp1=section1.getFirstChild();
            temp2=section2.getFirstChild();
            while(!temp1.getNodeName().equals("text")){//set temp nodes to their respective file's <text> field
                temp1=temp1.getNextSibling();
                temp2=temp2.getNextSibling();
            }
            area1=temp1;
            area2=temp2;
            temp1=temp1.getChildNodes().item(1);//go down a level, hopefully returning a table of data
            temp2=temp2.getChildNodes().item(1);//go down a level, hopefully returning a table of data
            if(temp2.getNodeName().equals("table")){//make sure this file contains something within the text field 
                temp3=doc1.importNode(temp2, true);
                area1.insertBefore(temp3, temp1.getNextSibling());//copy file2's table of data to file1
            }
            area2=area2.getNextSibling().getNextSibling();//onto the <entry> fields
            while(area2!=null){//while there are more <entry>s to copy
                temp3=doc1.importNode(area2, true);
                section1.insertBefore(temp3, section1.getLastChild());//copy <entry> from file2 to file1
                area2=area2.getNextSibling().getNextSibling();//next <entry>
            }
        }
        //////////CREATE <author> AND <dataEnterer> NODES
        temp1=list1.item(0);
        while(!temp1.getNodeName().equals("custodian"))
            temp1=temp1.getNextSibling();
        {
        Node author=doc1.createElement("author");
        Element time=doc1.createElement("time");
        Node assignedAuthor=doc1.createElement("assignedAuthor");
        Node id=doc1.createElement("id");
        Node addr=doc1.createElement("addr");
        Node telecom=doc1.createElement("telecom");
        Node assignedPerson=doc1.createElement("assignedPerson");
        Node name=doc1.createElement("name");
        time.setAttribute("value","20151216");
        name.setTextContent("Nick, Nileesha, David");
        author.appendChild(time);
        author.appendChild(assignedAuthor);
        assignedAuthor.appendChild(id);
        assignedAuthor.appendChild(addr);
        assignedAuthor.appendChild(telecom);
        assignedAuthor.appendChild(assignedPerson);
        assignedPerson.appendChild(name);
        doc1.getLastChild().insertBefore(author, temp1);
        }{
        Node dataEnterer=doc1.createElement("dataEnterer");
        Node assignedEntity=doc1.createElement("assignedEntity");
        Node id2=doc1.createElement("id");
        Node addr2=doc1.createElement("addr");
        Node telecom2=doc1.createElement("telecom");
        Node assignedPerson2=doc1.createElement("assignedPerson");
        Node name2=doc1.createElement("name");
        name2.setTextContent("Nick, Nileesha, David");
        dataEnterer.appendChild(assignedEntity);
        assignedEntity.appendChild(id2);
        assignedEntity.appendChild(addr2);
        assignedEntity.appendChild(telecom2);
        assignedEntity.appendChild(assignedPerson2);
        assignedPerson2.appendChild(name2);
        doc1.getLastChild().insertBefore(dataEnterer, temp1);  
        }
        ///////////
        checkIDs(doc1);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc1);
        //File out=new File("C:\\Users\\Sajani\\Documents\\CS370Workspace\\Project3_Servlet\\output.xml");
        File out=new File("output.xml");

        StringWriter writer = new StringWriter();
        StreamResult result1 = new StreamResult(writer);
        StreamResult result2 = new StreamResult(out);
        transformer.transform(source, result1);//create new XML from the merged result
        transformer.transform(source, result2);
        json=writer.toString();
        return result2;
    }

    public static String getJSON(String in1, String in2) throws Exception {
        //mergeFiles(in1, in2);
        try {
            JSONObject xmlJSONObj = XML.toJSONObject(json);
            String jsonPrettyPrintString = xmlJSONObj.toString(4);
            System.out.print(jsonPrettyPrintString);
            return jsonPrettyPrintString;
        } catch (JSONException je) {
            System.out.println(je.toString());
            return null;
        }
    }

  private static void checkIDs(Document doc) {//This traverses the "td" IDs and ensures that there are no duplicates by adding a char to the end of each
     NodeList check=doc.getElementsByTagName("td");
     char x='a';
     String s;
     Element temp;
     for(int i=0;i<check.getLength();i++){
         if(check.item(i).hasAttributes()){
             temp=doc.createElement("td");
             s=check.item(i).getAttributes().item(0).getNodeValue()+x;
             temp.setAttribute("ID",s);
             x++;
             check.item(i).getParentNode().insertBefore(temp,check.item(i));
             check.item(i).getParentNode().removeChild(check.item(i+1));
         }
     }
     check=doc.getElementsByTagName("name");
  }
  
  public static boolean areEqual(Node a, Node b){//Compares two nodes, making sure they contain the same exact information
      boolean test=true;
      if(!a.getNodeName().equals(b.getNodeName())||a.getChildNodes().getLength()!=b.getChildNodes().getLength()||!a.getTextContent().equals(b.getTextContent()))
          return false;
      if(a.hasAttributes()&&b.hasAttributes()){
          for(int i=0;i<a.getAttributes().getLength();i++){
              if(!a.getAttributes().item(i).toString().equals(b.getAttributes().item(i).toString())) {
                  return false;
              }
          }
      }
      for(int i=0;i<a.getChildNodes().getLength()&&test==true;i++)
          test = areEqual(a.getChildNodes().item(i),b.getChildNodes().item(i));
      return test;
  }

}