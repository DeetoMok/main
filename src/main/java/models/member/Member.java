package models.member;

import java.util.Objects;
import java.util.UUID;

public class Member implements IMember {
    private String name;
    private String phone;
    private String email;
    private int indexNumber;
    private String role;
    private String memberID;

    /**
     * Class representing a member in a project team.
     * @param name The name of the member.
     * @param phone The phone number of the member.
     * @param email The email address of the member.
     * @param indexNumber The index number assigned to the member, unique to the project.
     */
    public Member(String name, String phone, String email, int indexNumber, String role) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.indexNumber = indexNumber;
        this.role = role;
        this.memberID = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Member)) {
            return false;
        } else {
            Member other = (Member) obj;
            return this.name.equals(other.name);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.memberID);
    }


    @Override
    public String getDetails() {
        return this.indexNumber + ". " + this.name + " (Phone: " + this.phone + " | Email: "
            + this.email + " | Role: " + this.role + ")";
    }

    @Override
    public void setIndexNumber(int indexNumber) {
        this.indexNumber = indexNumber;
    }

    @Override
    public int getIndexNumber() {
        return this.indexNumber;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getPhone() {
        return this.phone;
    }

    @Override
    public String getEmail() {
        return this.email;
    }

    @Override
    public String getRole() {
        return this.role;
    }

    @Override
    public String setRole(String input) {
        return this.role = input;
    }

    @Override
    public void updateDetails(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public String getMemberID() {
        return this.memberID;
    }
}
