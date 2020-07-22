package com.hnjz.core.model.tree;

import com.hnjz.core.model.tree.Tree;

import java.util.List;
import java.util.Vector;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:40
 */
public class TreeNode {
    private Tree tree;
    private com.hnjz.core.model.tree.TreeNode parent;
    private List<com.hnjz.core.model.tree.TreeNode> children = new Vector();
    private List<com.hnjz.core.model.tree.TreeNode> childrenGroup = new Vector();
    private String nodeId;
    private String parentId;
    private int orderNo;

    public TreeNode() {
    }

    public String getNodeId() {
        return this.nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getParentId() {
        return this.parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Tree getTree() {
        return this.tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public void setParent(com.hnjz.core.model.tree.TreeNode parent) {
        this.parent = parent;
    }

    public com.hnjz.core.model.tree.TreeNode getParent() {
        return this.parent;
    }

    public List<com.hnjz.core.model.tree.TreeNode> getChildren() {
        return this.children;
    }

    public boolean isLeaf() {
        if (this.children == null) {
            return true;
        } else {
            return this.children.size() == 0;
        }
    }

    public void addChild(com.hnjz.core.model.tree.TreeNode node) {
        this.children.add(node);
    }

    public void addChildGroup(com.hnjz.core.model.tree.TreeNode node) {
        if (!this.childrenGroup.isEmpty() && !this.childrenGroup.contains(node)) {
            this.childrenGroup.add(node);
            if (this.parent != null && !this.parent.getAllChildren().contains(node)) {
                this.parent.addChildGroup(node);
            }
        }

    }

    public List<com.hnjz.core.model.tree.TreeNode> getAllChildren() {
        if (this.childrenGroup.isEmpty()) {
            synchronized(this.tree) {
                for(int i = 0; i < this.children.size(); ++i) {
                    com.hnjz.core.model.tree.TreeNode node = (com.hnjz.core.model.tree.TreeNode)this.children.get(i);
                    this.childrenGroup.add(node);
                    this.childrenGroup.addAll(node.getAllChildren());
                }
            }
        }

        return this.childrenGroup;
    }

    public void removeDisendantChild(com.hnjz.core.model.tree.TreeNode tn) {
        if (!this.childrenGroup.isEmpty()) {
            synchronized(this.tree) {
                this.childrenGroup.remove(tn);
                if (this.parent != null) {
                    this.parent.removeDisendantChild(tn);
                }
            }
        }

    }

    public List<com.hnjz.core.model.tree.TreeNode> getParents() {
        List results = new Vector();

        for(com.hnjz.core.model.tree.TreeNode parent = this.getParent(); parent != null; parent = parent.getParent()) {
            results.add(parent);
        }

        return results;
    }

    public boolean isMyParent(String nodeId) {
        com.hnjz.core.model.tree.TreeNode target = this.tree.getTreeNode(nodeId);
        com.hnjz.core.model.tree.TreeNode parent = this.getParent();
        if (parent == null) {
            return target == null;
        } else {
            return parent.equals(target);
        }
    }

    public boolean isMyAncestor(String nodeId) {
        com.hnjz.core.model.tree.TreeNode target = this.tree.getTreeNode(nodeId);
        return target == null ? true : target.getAllChildren().contains(this);
    }

    public boolean isMyBrother(String nodeId) {
        com.hnjz.core.model.tree.TreeNode target = this.tree.getTreeNode(nodeId);
        if (target == null) {
            return false;
        } else {
            com.hnjz.core.model.tree.TreeNode p1 = this.getParent();
            com.hnjz.core.model.tree.TreeNode p2 = target.getParent();
            return this.equals(p1, p2);
        }
    }

    private boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        } else {
            return object1 != null && object2 != null ? object1.equals(object2) : false;
        }
    }

    public int getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }
}
