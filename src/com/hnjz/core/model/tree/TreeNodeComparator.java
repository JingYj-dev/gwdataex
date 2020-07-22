package com.hnjz.core.model.tree;

import com.hnjz.core.model.tree.TreeNode;

import java.util.Comparator;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:40
 */
public class TreeNodeComparator implements Comparator<TreeNode> {
    TreeNodeComparator() {
    }

    public int compare(TreeNode obj1, TreeNode obj2) {
        return obj1.getOrderNo() - obj2.getOrderNo();
    }
}
