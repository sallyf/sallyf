package com.sallyf.sallyf.ContainerInstantiator;

import com.sallyf.sallyf.Container.ServiceRepresentationInterface;

import java.util.ArrayList;

public class DependencyNode
{
    private DependencyNode parent;

    private ServiceRepresentationInterface serviceRepresentation;

    private ArrayList<DependencyNode> children;

    public DependencyNode(ServiceRepresentationInterface serviceRepresentation)
    {
        this.serviceRepresentation = serviceRepresentation;
        this.children = new ArrayList<>();
    }

    public DependencyNode getParent()
    {
        return parent;
    }

    public DependencyNode setParent(DependencyNode parent)
    {
        this.parent = parent;

        return this;
    }

    public DependencyNode addChild(DependencyNode node)
    {
        children.add(node);

        node.setParent(this);

        return node;
    }

    public ArrayList<DependencyNode> getChildren()
    {
        return children;
    }

    public ServiceRepresentationInterface getServiceRepresentation()
    {
        return serviceRepresentation;
    }

    public void setServiceRepresentation(ServiceRepresentationInterface serviceDefinition)
    {
        this.serviceRepresentation = serviceDefinition;
    }

    public boolean isInTree()
    {
        DependencyNode current = this.getParent();

        while (current != null) {
            if (current.getServiceRepresentation().getAlias() == this.getServiceRepresentation().getAlias()) {
                return true;
            }

            current = current.getParent();
        }

        return false;
    }
}
