package irc.cpe.cozy.Model;

/**
 * Created by Angèle on 04/01/2016.
 */

public class ListElement {

        String name = null;
        boolean selected = false;

        public ListElement(String name, boolean selected) {
            super();
            this.name = name;
            this.selected = selected;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }

        public boolean isSelected() {
            return selected;
        }
        public void setSelected(boolean selected) {
            this.selected = selected;
        }

}

