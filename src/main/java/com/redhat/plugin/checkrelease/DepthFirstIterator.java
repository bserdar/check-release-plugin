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

import org.w3c.dom.Node;

/**
 * @author Burak Serdar (bserdar@redhat.com)
 */
public class DepthFirstIterator {
    private Node current;
    private final Node root;
    private boolean done=false;
    
    public DepthFirstIterator(Node root) {
        current=null;
        this.root=root;
    }

    public boolean hasNext() {
        return findNext()!=null;
    }

    public Node next() {
        current=findNext();
        if(current==null)
            done=true;
        return current;
    }
    
    private Node findNext() {
        if(current==null)
            if(!done)
                return root;
            else
                return null;
        else {
            Node n=current.getFirstChild();
            if(n!=null)
                return n;
            else {
                n=current.getNextSibling();
                if(n!=null)
                    return n;
                else {
                    if(current!=root) {
                        Node trc=current;
                        do {
                            trc=trc.getParentNode();
                            n=trc.getNextSibling();
                            if(n!=null)
                                return n;
                        } while(trc!=root);
                    }
                }
            }
        }
        return null;
    }
}

