package seedu.address.model.util;

import static seedu.address.model.util.TagUtil.getTagSet;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.demerit.DemeritIncident;
import seedu.address.model.demerit.DemeritRule;
import seedu.address.model.demerit.DemeritRuleCatalogue;
import seedu.address.model.person.Email;
import seedu.address.model.person.EmergencyContact;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.person.RoomNumber;
import seedu.address.model.person.StudentId;
import seedu.address.model.tag.TagType;


/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    private static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                    new StudentId("A0485321Y"), new RoomNumber("4-A"),
                    new EmergencyContact("98765432"), new Remark("My best friend"),
                    getTagSet(
                            new Object[] {TagType.YEAR, "2"},
                            new Object[] {TagType.GENDER, "he/him"},
                            new Object[] {TagType.MAJOR, "Computer Science"}),
                    getDemeritIncidentList(
                            new Object[] {31, 1, "Left fan on overnight"},
                            new Object[] {28, 1, "Common area left untidy"})),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                    new StudentId("A1123456Z"), new RoomNumber("15-R"),
                    new EmergencyContact("91234567"), new Remark(""),
                    getTagSet(
                            new Object[] {TagType.YEAR, "1"},
                            new Object[] {TagType.GENDER, "she/her"},
                            new Object[] {TagType.MAJOR, "Business"}),
                    List.of()),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                    new StudentId("A1246354T"), new RoomNumber("3-D"),
                    new EmergencyContact("87654321"), new Remark("Really funny person"),
                    getTagSet(
                            new Object[] {TagType.YEAR, "4"},
                            new Object[] {TagType.GENDER, "they/them"},
                            new Object[] {TagType.MAJOR, "Psychology"}),
                    getDemeritIncidentList(
                            new Object[] {18, 1, "Quiet-hours visitor warning"})),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                    new StudentId("A0148321W"), new RoomNumber("10-C"),
                    new EmergencyContact("12345678"), new Remark(""),
                    getTagSet(
                            new Object[] {TagType.YEAR, "3"},
                            new Object[] {TagType.GENDER, "he/him"},
                            new Object[] {TagType.MAJOR, "Mechanical Engineering"}),
                    getDemeritIncidentList(
                            new Object[] {21, 2, "Repeated excessive noise"},
                            new Object[] {25, 1, "Corridor obstruction"})),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                    new StudentId("A1436528Q"), new RoomNumber("5-B"),
                    new EmergencyContact("56789012"), new Remark(""),
                    getTagSet(
                            new Object[] {TagType.YEAR, "2"},
                            new Object[] {TagType.GENDER, "he/him"},
                            new Object[] {TagType.MAJOR, "Information Security"}),
                    List.of()),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                    new StudentId("A0246835Z"), new RoomNumber("12-D"),
                    new EmergencyContact("23456789"), new Remark("Allergic to shellfish"),
                    getTagSet(
                            new Object[] {TagType.YEAR, "5"},
                            new Object[] {TagType.GENDER, "he/him"},
                            new Object[] {TagType.MAJOR, "Law"}),
                    getDemeritIncidentList(
                            new Object[] {26, 1, "Unauthorized appliance found"},
                            new Object[] {26, 2, "Repeated unauthorized appliance use"}))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() throws DataLoadingException {
        AddressBook sampleAb = new AddressBook();
        try {
            for (Person samplePerson : getSamplePersons()) {
                sampleAb.addPerson(samplePerson);
            }
        } catch (IllegalArgumentException e) {
            throw new DataLoadingException(e);
        }
        return sampleAb;
    }

    /**
     * Returns a demerit incident list containing the given tuples:
     * (ruleIndex, offenceNumber, remark).
     */
    public static List<DemeritIncident> getDemeritIncidentList(Object[]... incidents) {
        List<DemeritIncident> demeritIncidents = new ArrayList<>();
        for (Object[] tuple : incidents) {
            int ruleIndex = Integer.parseInt(tuple[0].toString());
            int offenceNumber = Integer.parseInt(tuple[1].toString());
            String remark = tuple.length > 2 ? tuple[2].toString() : "";

            DemeritRule rule = DemeritRuleCatalogue.findByIndex(ruleIndex)
                    .orElseThrow(() -> new IllegalArgumentException("Unknown demerit rule index: " + ruleIndex));
            int pointsApplied = rule.getPointsForOccurrence(offenceNumber);
            demeritIncidents.add(new DemeritIncident(ruleIndex, rule.getTitle(), offenceNumber, pointsApplied, remark));
        }
        return demeritIncidents;
    }
}
