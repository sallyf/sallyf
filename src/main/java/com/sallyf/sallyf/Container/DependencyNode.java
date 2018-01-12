package com.sallyf.sallyf.Container;

import java.util.ArrayList;

public class DependencyNode
{
    private DependencyNode parent;

    private Class<? extends ContainerAwareInterface> value;

    private ArrayList<DependencyNode> children;

    public DependencyNode(Class<? extends ContainerAwareInterface> type)
    {
        this.value = type;
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

    public Class<? extends ContainerAwareInterface> getType()
    {
        return value;
    }

    public DependencyNode setType(Class<? extends ContainerAwareInterface> value)
    {
        this.value = value;
        return this;
    }

    public boolean isInTree()
    {
        DependencyNode current = this.getParent();

        while (current != null) {
            if (current.getType() == this.getType()) {
                return true;
            }

            current = current.getParent();
        }

        return false;
    }
}
