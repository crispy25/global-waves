package searchbar;

import contracts.ISearchable;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class SearchBar {

    private ArrayList<ISearchable> searchResult;
    private ISearchable selected;
}
