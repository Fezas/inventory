/*
 * Copyright (c) 2023. Stepantsov P.V.
 */

package mo.inventory.util;

import javafx.scene.control.*;
import javafx.util.Callback;

public class ContextMenuListCell<T> extends TreeView<T> {

    public static <T> Callback<TreeView<T>,TreeCell<T>> forListView(ContextMenu contextMenu) {
        return forListView(contextMenu, null);
    }

    public static <T> Callback<TreeView<T>, TreeCell<T>> forListView(final ContextMenu contextMenu, final Callback<TreeView<T>, TreeCell<T>> cellFactory) {
        return new Callback<TreeView<T>,TreeCell<T>>() {
            @Override public TreeCell<T> call(TreeView<T> treeView) {
                TreeCell<T> cell = cellFactory == null ? new DefaultListCell<T>() : cellFactory.call(treeView);
                cell.setContextMenu(contextMenu);
                return cell;
            }
        };
    }

    public ContextMenuListCell(ContextMenu contextMenu) {
        setContextMenu(contextMenu);
    }
}
