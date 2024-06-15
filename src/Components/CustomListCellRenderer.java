package Components;

import javax.swing.*;
import java.awt.*;

public class CustomListCellRenderer extends JLabel implements ListCellRenderer {

    public CustomListCellRenderer() {
        this.setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(value.toString());

        if (!list.isEnabled()) {
            this.setForeground(Color.WHITE);
            this.setBackground(Color.black);
        }

        return this;
    }
}