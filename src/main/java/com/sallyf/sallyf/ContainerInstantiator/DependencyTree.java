package com.sallyf.sallyf.ContainerInstantiator;

import com.sallyf.sallyf.Container.ServiceInterface;
import com.sallyf.sallyf.Container.ServiceDefinition;
import com.sallyf.sallyf.Container.ServiceRepresentationInterface;

import java.util.ArrayList;
import java.util.Map;

public class DependencyTree<T extends ServiceInterface>
{
    private ContainerInstantiator containerInstantiator;

    private DependencyNode root;

    private DependencyNode circularReferenceNode = null;

    public DependencyTree(ContainerInstantiator containerInstantiator, ServiceDefinition<T> serviceDefinition)
    {
        this.containerInstantiator = containerInstantiator;

        setRoot(new DependencyNode(serviceDefinition));
    }

    public DependencyNode getRoot()
    {
        return root;
    }

    public DependencyNode setRoot(DependencyNode root)
    {
        this.root = root;

        return root;
    }

    public DependencyNode getCircularReferenceNode()
    {
        return circularReferenceNode;
    }

    public void setCircularReferenceNode(DependencyNode circularReferenceNode)
    {
        this.circularReferenceNode = circularReferenceNode;
    }

    public boolean hasCircularReference()
    {
        return null != circularReferenceNode;
    }

    public String getCircularReferencePath()
    {
        if (null == circularReferenceNode) {
            return "No circular reference";
        }

        ArrayList<String> path = new ArrayList<>();

        DependencyNode current = getCircularReferenceNode();

        while (current != null) {
            ServiceRepresentationInterface serviceRepresentation = current.getServiceRepresentation();
            if (serviceRepresentation instanceof ServiceDefinition) {
                path.add(((ServiceDefinition) serviceRepresentation).getType().getName());
            } else {
                path.add(serviceRepresentation.getAlias().getName());
            }

            current = current.getParent();
        }

        return String.join(" -> ", path);
    }

    public boolean isFullyInstantiated()
    {
        DependencyNode current = this.getRoot();

        if (null == current) {
            return true;
        }

        return recursiveChecker(current, containerInstantiator.getServices());
    }

    public boolean isFullyInstantiable()
    {
        DependencyNode current = this.getRoot();

        if (null == current) {
            return true;
        }

        return recursiveChecker(current, containerInstantiator.getServiceDefinitions());
    }

    private boolean recursiveChecker(DependencyNode current, Map map)
    {
        for (DependencyNode childNode : current.getChildren()) {
            if (!map.containsKey(childNode.getServiceRepresentation().getAlias())) {
                return false;
            }

            if (!recursiveChecker(childNode, map)) {
                return false;
            }
        }

        return true;
    }
}
