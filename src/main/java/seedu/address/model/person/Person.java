package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.TagType;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;
    private final StudentId studentId;

    // Data fields
    private final RoomNumber roomNumber;
    private final EmergencyContact emergencyContact;
    private final Map<TagType, Tag> tags;
    private final Remark remark;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, StudentId studentId, RoomNumber roomNumber,
                  EmergencyContact emergencyContact, Remark remark, Map<TagType, Tag> tags) {
        requireAllNonNull(name, phone, email, studentId, roomNumber, emergencyContact, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.studentId = studentId;
        this.roomNumber = roomNumber;
        this.emergencyContact = emergencyContact;
        this.remark = remark;
        this.tags = tags;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public StudentId getStudentId() {
        return studentId;
    }

    public RoomNumber getRoomNumber() {
        return roomNumber;
    }

    public EmergencyContact getEmergencyContact() {
        return emergencyContact;
    }

    public Remark getRemark() {
        return remark;
    }

    public Map<TagType, Tag> getTags() {
        return Collections.unmodifiableMap(tags);
    }

    /**
     * @return the gender tag(s) of this person, if they exist
     */
    public Tag getGender() {
        return tags.get(TagType.GENDER);
    }

    /*
     * @return the year tags of this person
     */
    public Tag getYear() {
        return tags.get(TagType.YEAR);
    }

    /*
     * @return the year tags of this person, if they exist.
     */
    public Tag getMajor() {
        return tags.get(TagType.MAJOR);
    }

    /**
     * Asserts that the number of tags of a given type does not exceed the allowed maximum.
     */
    private static void assertTagLimit(Set<Tag> tagSet, TagType type) {
        assert tagSet.size() <= type.getMaxTagsPerType()
                : "Tag count for " + type + " exceeds limit of " + type.getMaxTagsPerType();
    }

    /**
     * Returns true if both persons have the same studentId.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getStudentId().equals(getStudentId());
    }

    /**
     * Returns true if both persons have the same room
     */
    public boolean hasSameRoom(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }
        return otherPerson != null
                && otherPerson.getRoomNumber().equals(getRoomNumber());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && studentId.equals(otherPerson.studentId)
                && roomNumber.equals(otherPerson.roomNumber)
                && emergencyContact.equals(otherPerson.emergencyContact)
                && remark.equals(otherPerson.remark)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, studentId, roomNumber, emergencyContact, remark, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("studentId", studentId)
                .add("roomNumber", roomNumber)
                .add("emergencyContact", emergencyContact)
                .add("remark", remark)
                .add("tags", tags)
                .toString();
    }

}
