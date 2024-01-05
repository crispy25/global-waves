package artist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class Merch {

    private String name;
    private String description;
    private int price;

    @Override
    public String toString() {
        return name + " - " + price + ":\n\t" + description;
    }
}
