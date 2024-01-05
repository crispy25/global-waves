package contracts;

import commandinput.Filter;

public interface IFilterable {
    /**
     * Check if the given filters are found in the object properties
     * @param filters filters to be checked
     * @return true if the object has all the filters, false otherwise
     */
    boolean filter(Filter filters);
}
