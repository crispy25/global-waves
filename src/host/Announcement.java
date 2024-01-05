package host;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class Announcement {

    private String name;
    private String description;

    @Override
    public String toString() {
        return name + ":\n\t" + description + "\n";
    }
}
