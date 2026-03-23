package seedu.address.model.demerit;

import static java.util.Objects.requireNonNull;

/**
 * Represents one demerit rule from the DPS catalogue.
 */
public class DemeritRule {

    private final int index;
    private final String title;
    private final int firstOffencePoints;
    private final int secondOffencePoints;
    private final int thirdAndSubsequentPoints;

    /**
     * Creates a demerit rule with indexed point tiers.
     */
    public DemeritRule(int index, String title, int firstOffencePoints,
                       int secondOffencePoints, int thirdAndSubsequentPoints) {
        requireNonNull(title);
        this.index = index;
        this.title = title.trim();
        this.firstOffencePoints = firstOffencePoints;
        this.secondOffencePoints = secondOffencePoints;
        this.thirdAndSubsequentPoints = thirdAndSubsequentPoints;
    }

    public int getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public int getFirstOffencePoints() {
        return firstOffencePoints;
    }

    public int getSecondOffencePoints() {
        return secondOffencePoints;
    }

    public int getThirdAndSubsequentPoints() {
        return thirdAndSubsequentPoints;
    }

    /**
     * Returns the applicable point value for the given occurrence count.
     * First offence uses the first tier, second offence uses the second tier,
     * and third or later offences use the third-and-subsequent tier.
     */
    public int getPointsForOccurrence(int occurrenceCount) {
        if (occurrenceCount <= 1) {
            return firstOffencePoints;
        }
        if (occurrenceCount == 2) {
            return secondOffencePoints;
        }
        return thirdAndSubsequentPoints;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s (1st: %d, 2nd: %d, 3rd+: %d)",
                index, title, firstOffencePoints, secondOffencePoints, thirdAndSubsequentPoints);
    }
}