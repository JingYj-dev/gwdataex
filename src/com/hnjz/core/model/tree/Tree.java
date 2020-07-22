package com.hnjz.core.model.tree;

import com.hnjz.core.model.tree.TreeNode;
import com.hnjz.core.model.tree.TreeNodeComparator;
import com.hnjz.base.memcached.MemCachedFactory;
import com.hnjz.util.Md5Util;
import com.hnjz.util.StringHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * @author JingYj
 * @version 1.0
 * @date 2020/6/15 18:41
 */
public abstract class Tree {
    private String initTime = "";
    private static Log log = LogFactory.getLog(com.hnjz.core.model.tree.Tree.class);
    private Map treeNodeMaps = new Hashtable();
    private com.hnjz.core.model.tree.TreeNode root;

    public Tree() {
    }

    protected void reload(List nodes) {
        log.info(" tree will start reload all data ");
        synchronized(this) {
            this.treeNodeMaps.clear();
            this.root = null;
            List treeNodes = new Vector(nodes.size());
            int i = 0;

            while(true) {
                com.hnjz.core.model.tree.TreeNode node;
                if (i >= nodes.size()) {
                    Collections.sort(treeNodes, new com.hnjz.core.model.tree.TreeNodeComparator());

                    for(i = 0; i < treeNodes.size(); ++i) {
                        node = (com.hnjz.core.model.tree.TreeNode)treeNodes.get(i);
                        this.initTreeNode(node);
                    }
                    break;
                }

                node = this.transform(nodes.get(i));
                treeNodes.add(node);
                node.setTree(this);
                this.treeNodeMaps.put(node.getNodeId(), node);
                ++i;
            }
        }

        if (this.root == null) {
            log.error(" the root node is not be defined ");
        }

    }

    protected boolean isRootNode(com.hnjz.core.model.tree.TreeNode node) {
        return StringHelper.isEmpty(node.getParentId());
    }

    public com.hnjz.core.model.tree.TreeNode getRootNode() {
        return this.root;
    }

    public com.hnjz.core.model.tree.TreeNode getTreeNode(String nodeId) {
        return (com.hnjz.core.model.tree.TreeNode)this.treeNodeMaps.get(nodeId);
    }

    private void initTreeNode(com.hnjz.core.model.tree.TreeNode node) {
        String parentId = node.getParentId();
        if (this.isRootNode(node)) {
            if (this.root == null) {
                this.root = node;
            } else {
                log.error(" find more then one root node. ignore. ");
            }
        } else {
            com.hnjz.core.model.tree.TreeNode parent = this.getTreeNode(parentId);
            if (parent != null) {
                parent.addChild(node);
                parent.addChildGroup(node);
                node.setParent(parent);
            } else {
                log.warn(" node [id= " + node.getNodeId() + " ]: missing parent node,[" + this.getClass() + "]");
            }
        }

    }

    public void addTreeNode(com.hnjz.core.model.tree.TreeNode node) {
        synchronized(this) {
            node.setTree(this);
            this.treeNodeMaps.put(node.getNodeId(), node);
            this.initTreeNode(node);
        }
    }

    public void deleteTreeNode(String nodeId) {
        synchronized(this) {
            com.hnjz.core.model.tree.TreeNode node = this.getTreeNode(nodeId);
            if (node == null) {
                throw new IllegalArgumentException(nodeId + " cannot be found. ");
            } else {
                if (node.getParent() == null) {
                    this.root = null;
                    this.treeNodeMaps.clear();
                    log.warn(" the root node has been removed. ");
                } else {
                    List children = node.getAllChildren();

                    for(int i = 0; i < children.size(); ++i) {
                        com.hnjz.core.model.tree.TreeNode n = (com.hnjz.core.model.tree.TreeNode)children.get(i);
                        this.treeNodeMaps.remove(n.getNodeId());
                        node.removeDisendantChild(n);
                    }

                    node.getParent().getChildren().remove(node);
                    node.getParent().removeDisendantChild(node);
                    this.treeNodeMaps.remove(nodeId);
                }

            }
        }
    }

    private String getMd5Key() {
        return Md5Util.MD5Encode("CSS_WORKREC:MemCached tree:" + this.getTreeClass().getName());
    }

    public void reloadTreeCache() {
        String newTime = (String) MemCachedFactory.get(this.getMd5Key());
        if (newTime == null) {
            this.initTime = String.valueOf(System.currentTimeMillis());
            MemCachedFactory.set(this.getMd5Key(), this.initTime);
            this.reloadTree();
        } else if (!this.initTime.equals(newTime)) {
            this.initTime = newTime;
            this.reloadTree();
        }

    }

    public void resetMem() {
        this.initTime = String.valueOf(System.currentTimeMillis());
        MemCachedFactory.set(this.getMd5Key(), this.initTime);
        this.reloadTree();
    }

    public List getListById(List<com.hnjz.core.model.tree.TreeNode> treeNodeList, boolean intFlag) {
        List l = new ArrayList();
        Iterator var5 = treeNodeList.iterator();

        while(var5.hasNext()) {
            com.hnjz.core.model.tree.TreeNode tn = (com.hnjz.core.model.tree.TreeNode)var5.next();
            l.add(new Integer(tn.getNodeId()));
        }

        return l;
    }

    public List getListById(List<com.hnjz.core.model.tree.TreeNode> treeNodeList) {
        List l = new ArrayList();
        Iterator var4 = treeNodeList.iterator();

        while(var4.hasNext()) {
            com.hnjz.core.model.tree.TreeNode tn = (com.hnjz.core.model.tree.TreeNode)var4.next();
            l.add(tn.getNodeId());
        }

        return l;
    }

    public List getList(List<com.hnjz.core.model.tree.TreeNode> treeNodeList) {
        List l = new ArrayList();
        Iterator var4 = treeNodeList.iterator();

        while(var4.hasNext()) {
            com.hnjz.core.model.tree.TreeNode tn = (com.hnjz.core.model.tree.TreeNode)var4.next();
            l.add(this.getBindData(tn));
        }

        return l;
    }

    protected abstract com.hnjz.core.model.tree.TreeNode transform(Object var1);

    protected abstract void reloadTree();

    protected abstract Class getTreeClass();

    protected abstract Object getBindData(TreeNode var1);
}
