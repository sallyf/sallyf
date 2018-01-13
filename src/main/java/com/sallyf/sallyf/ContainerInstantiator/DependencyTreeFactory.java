package com.sallyf.sallyf.ContainerInstantiator;

import com.sallyf.sallyf.Container.*;
import com.sallyf.sallyf.Container.Exception.ServiceInstantiationException;

import java.util.ArrayList;
import java.util.HashMap;

public class DependencyTreeFactory
{
    private ContainerInstantiator containerInstantiator;

    private HashMap<Class, DependencyTree<? extends ContainerAwareInterface>> trees = new HashMap<>();

    public DependencyTreeFactory(ContainerInstantiator containerInstantiator)
    {
        this.containerInstantiator = containerInstantiator;
    }

    public <T extends ContainerAwareInterface> DependencyTree<T> generate(ServiceDefinition<T> serviceDefinition)
    {
        if (getTrees().containsKey(serviceDefinition.getAlias())) {
            return (DependencyTree<T>) getTrees().get(serviceDefinition.getAlias());
        }

        DependencyTree<T> tree = new DependencyTree<>(containerInstantiator, serviceDefinition);
        DependencyNode root = tree.getRoot();

        addServiceDefinitionToTree(tree, serviceDefinition, root);

        trees.put(serviceDefinition.getAlias(), tree);

        return tree;
    }

    private void addServiceDefinitionToTree(DependencyTree tree, ServiceDefinition<?> serviceDefinition, DependencyNode currentNode)
    {
        ArrayList<ConstructorDefinition> constructorDefinitions = serviceDefinition.getConstructorDefinitions();

        for (ConstructorDefinition constructorDefinition : constructorDefinitions) {
            for (ReferenceInterface childReference : constructorDefinition.getArgs()) {
                if (childReference instanceof ServiceReference) {
                    ServiceReference<?> childServiceReference = (ServiceReference) childReference;

                    DependencyNode childDependencyNode = currentNode.addChild(new DependencyNode(childServiceReference));

                    if (childDependencyNode.isInTree()) {
                        tree.setCircularReferenceNode(childDependencyNode);
                        return;
                    }

                    ServiceDefinition<?> childServiceDefinition = containerInstantiator.getServiceDefinitions().get(childServiceReference.getAlias());

                    if (null != childServiceDefinition) {
                        addServiceDefinitionToTree(tree, childServiceDefinition, childDependencyNode);
                    }
                }
            }
        }
    }

    public HashMap<Class, DependencyTree<? extends ContainerAwareInterface>> getTrees()
    {
        return trees;
    }
}
