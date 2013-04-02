/*
   Copyright 2013 Red Hat, Inc. and/or its affiliates.

   This file is part of check-release plugin.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package com.redhat.plugin.checkrelease;

import java.io.InputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import org.apache.maven.plugin.logging.Log;

/**
 * @author Burak Serdar (bserdar@redhat.com)
 */
public class XMLFileComparator implements FileComparator {

    private final Log log;

    public XMLFileComparator(Log log) {
        this.log=log;
    }

    public String compare(InputStream s1,InputStream s2) {
        try {
            if(log!=null)
                log.debug("Start XML comparison");
            DocumentBuilder b=DocumentBuilderFactory.
                newInstance().newDocumentBuilder();
            Document d1=b.parse(s1);
            Document d2=b.parse(s2);
            String s=compare(d1,d2);
            if(log!=null)
                log.debug("End XML comparison:"+s);
            return s;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String compare(Document d1,Document d2) {
        DepthFirstIterator itr1=new DepthFirstIterator(d1);
        DepthFirstIterator itr2=new DepthFirstIterator(d2);
        boolean done=false;
        Node n1=itr1.next();
        Node n2=itr2.next();
        do {
            if(log!=null) {
                if(n1!=null)
                    log.debug("Node1:"+n1.getNodeName()+" "+n1.getNodeValue());
                if(n2!=null)
                    log.debug("Node2:"+n2.getNodeName()+" "+n2.getNodeValue());
            }
            if(n1!=null) {
                if(n2!=null) {
                    if(n1.getNodeType()==Node.COMMENT_NODE)
                        n1=itr1.next();
                    else {
                        if(n2.getNodeType()==Node.COMMENT_NODE)
                            n2=itr2.next();
                        else {
                            if(n1.getNodeType()==Node.TEXT_NODE&&
                               ws(n1.getNodeValue()))
                                n1=itr1.next();
                            else {
                                if(n2.getNodeType()==Node.TEXT_NODE&&
                                   ws(n2.getNodeValue()))
                                    n2=itr2.next();
                                else {
                                    if(n1.getNodeType()==
                                       n2.getNodeType()) {
                                        String s=compare(n1,n2);
                                        if(s!=null)
                                            return s;
                                        n1=itr1.next();
                                        n2=itr2.next();
                                    } else {
                                        return "Different nodes:"+n1+"-"+n2;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if(n1.getNodeType()==Node.COMMENT_NODE)
                        n1=itr1.next();
                    else if(n1.getNodeType()==Node.TEXT_NODE) {
                        if(ws(n1.getNodeValue()))
                            n1=itr1.next();
                        else
                            return "Different text:"+n1.getNodeValue();
                    } else
                        return "Different content:"+n1.toString();
                }
            } else {
                if(n2!=null) {
                    if(log!=null)
                        log.debug("Node1 is null, Node2 node:"+n2);
                    if(n2.getNodeType()==Node.COMMENT_NODE)
                        n2=itr2.next();
                    else if(n2.getNodeType()==Node.TEXT_NODE)
                        if(ws(n2.getNodeValue()))
                            n2=itr2.next();
                        else
                            return "Different text:"+n2.getNodeValue();
                    else
                        return "Different node:"+n2.toString();
                } else
                    done=true;
            }
        } while(!done);
        return null;
    }

    private boolean ws(String s) {
        int n=s.length();
        for(int i=0;i<n;i++)
            if(!Character.isWhitespace(s.charAt(i)))
                return false;
        return true;
    }

    private String compare(Node n1,Node n2) {
        switch(n1.getNodeType()) {
        case Node.ATTRIBUTE_NODE:
        case Node.CDATA_SECTION_NODE:
        case Node.COMMENT_NODE:
        case Node.TEXT_NODE:
        case Node.PROCESSING_INSTRUCTION_NODE:
            return n1.getNodeValue().equals(n2.getNodeValue())?null:
            "Different node value:"+n1.getNodeValue()+"-"+n2.getNodeValue();

        case Node.ELEMENT_NODE:
            Element e1=(Element)n1;
            Element e2=(Element)n2;
            if(!e1.getTagName().equals(e2.getTagName()))
                return "Different element:"+e1.getTagName()+"-"+e2.getTagName();
            break;
        }
        return null;
    }
}
