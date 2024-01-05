package artist;

import constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class Event {

    private String name;
    private String description;
    private String date;

    @Override
    public String toString() {
        return name + " - " + date + ":\n\t" + description;
    }

    /**
     * Check if the given date is valid
     * @param date date to be checked
     * @return true if date is valid, false otherwise
     */
    public static boolean isDateValid(final String date) {
        if (!date.matches(Constants.DATE_FORMAT)) {
            return false;
        }

        String[] dateInfo = date.split(Constants.HYPHEN);

        int day = Integer.parseInt(dateInfo[0]);
        int month = Integer.parseInt(dateInfo[1]);
        int year = Integer.parseInt(dateInfo[2]);

        if (day > Constants.REGULAR_MONTH_DAYS) {
            return false;
        }
        if (month == Constants.FEBRUARY_MONTH && day > Constants.FEBRUARY_MONTH_DAYS) {
            return false;
        }
        if (month > Constants.MONTHS) {
            return false;
        }
        if (year < Constants.MINIMUM_YEAR || year > Constants.CURRENT_YEAR) {
            return false;
        }

        return true;
    }
}
