package com.whenling.castle.repo.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import com.google.common.base.Objects;

public class TreeHelper {

	public static <T extends Hierarchical<T>> Tree<T> toTree(T current, List<T> nodes) {
		Collections.sort(nodes, SortNoComparator.COMPARATOR);
		List<Node<T>> directSubordinates = findDirectSubordinates(current, nodes);
		if (current != null) {
			Node<T> root = toNode(current, directSubordinates);
			directSubordinates = new ArrayList<>();
			directSubordinates.add(root);
		}

		Tree<T> tree = new TreeImpl<>(directSubordinates);
		return tree;
	}

	public static <T extends Hierarchical<T>> void visitNodes(List<? extends Node<T>> nodes, Consumer<Node<T>> consumer) {
		if (nodes != null) {
			nodes.forEach((node) -> {
				consumer.accept(node);
				visitNodes(node.getChildren(), consumer);
			});
		}
	}

	public static <T extends Hierarchical<T>> List<Node<T>> findDirectSubordinates(T root, List<T> allChildren) {
		List<Node<T>> nodes = new ArrayList<>();
		for (T entity : allChildren) {
			if (Objects.equal(entity.getParent(), root)) {
				nodes.add(toNode(entity, findDirectSubordinates(entity, allChildren)));
			}
		}
		return nodes;
	}

	public static <T extends Hierarchical<T>> Node<T> toNode(T entity, List<Node<T>> children) {
		Node<T> node = new Node<>();
		node.setData(entity);
		node.setChildren(children);
		return node;
	}

}
