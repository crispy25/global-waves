package pagemanager;

import lombok.Getter;
import lombok.Setter;
import usermanager.User;

@Getter
@Setter
public abstract class Page {

    protected String owner;

    /**
     * Accepts user to visit the page
     * @param user user to visit the page
     * @return the content of the page
     */
    public abstract String acceptVisit(User user);
}
