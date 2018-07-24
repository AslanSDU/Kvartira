package kvartira.kz.kvartira.Model;

/**
 * Created by Aslan on 13.12.2015.
 */
public class MenuListItem {

    private String title;
    private int icon;
    private boolean toggle;

    public MenuListItem(String title, int icon, boolean toggle) {
        this.title = title;
        this.icon = icon;
        this.toggle = toggle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getToggle() {
        return toggle;
    }

    public void setToggle(boolean toggle) {
        this.toggle = toggle;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
