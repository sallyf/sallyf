package com.sallyf.sallyf.Container;

import java.util.ArrayList;

public class DependencyNode
{
    private DependencyNode parent;

    private ServiceAliasAwareInterface serviceAliasAware;

    private ArrayList<DependencyNode> children;

    public DependencyNode(ServiceAliasAwareInterface serviceAliasAware)
    {
        this.serviceAliasAware = serviceAliasAware;
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

    public ServiceAliasAwareInterface getServiceAliasAware()
    {
        return serviceAliasAware;
    }

    public void setServiceAliasAware(ServiceAliasAwareInterface serviceDefinition)
    {
        this.serviceAliasAware = serviceDefinition;
    }

    public boolean isInTree()
    {
        DependencyNode current = this.getParent();

        while (current != null) {
            if (current.getServiceAliasAware().getAlias() == this.getServiceAliasAware().getAlias()) {
                return true;
            }

            current = current.getParent();
        }

        return false;
    }
}
