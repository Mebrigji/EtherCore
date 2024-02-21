package nets.tools.permissions;


import java.util.*;

public class PermissionTreeNode {

    public static PermissionTreeNode TEST = new PermissionTreeNode();

    private Map<String, PermissionTreeNode> children;

    public PermissionTreeNode() {
        this.children = new HashMap<>();
    }


    public void addPermission(String permission) {
        List<String> parts = new ArrayList<>(List.of(permission.split("\\.")));
        PermissionTreeNode node = this;

        for (String part : parts) {
            node = node.children.computeIfAbsent(part, p -> new PermissionTreeNode());
        }
    }

    public List<String> suggestCompletions(String input) {
        List<String> completions = new ArrayList<>();
        PermissionTreeNode node = this;

        // Find the node for the given prefix
        List<String> parts = new ArrayList<>(List.of(input.split("\\.")));
        for (String part : parts) {
            node = node.children.get(part);
            if (node == null) {
                return completions; // No node for the given prefix
            }
        }

        // Collect completions for the found node
        //collectCompletions(node, prefix, completions);

        return completions;
    }

    //private void collectCompletions(PermissionTreeNode node, String currentPath, List<String> completions) {
    //    if (node.nodeName.isEmpty()) {
    //        // If the node is the root, add empty string as a completion
    //        completions.add(currentPath);
    //    } else {
    //        // Add the current node as a completion
    //        completions.add(nodeName + "." + currentPath);
    //    }
//
    //    // Recursively collect completions for child nodes
    //    for (Map.Entry<String, PermissionTreeNode> entry : node.children.entrySet()) {
    //        PermissionTreeNode childNode = entry.getValue();
    //        String childPath = currentPath.isEmpty() ? entry.getKey() : currentPath + "." + entry.getKey();
//
    //        collectCompletions(childNode, childPath, completions);
    //    }
    //}
}