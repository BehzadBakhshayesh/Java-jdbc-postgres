package library.services;

import library.exceptions.LibraryException;
import library.domain.Member;
import java.util.ArrayList;

public class MemberServices {
    private ArrayList<Member> members = new ArrayList<>();

    public void addMember(String name, String email) {
        Member member = new Member(name, email);
        this.members.add(member);
    }

    public ArrayList<Member> allMembers() {
        return new ArrayList<>(this.members);
    }

    public ArrayList<Member> searchMember(String search) {
        ArrayList<Member> searchList = new ArrayList<>();
        String searchText = search.toLowerCase();
        for (int i = 0; i < this.members.size(); i++) {
            if (this.members.get(i).getName().toLowerCase().contains(searchText) || this.members.get(i).getEmail().toLowerCase().contains(searchText)) {
                searchList.add(this.members.get(i));
            }
        }
        return searchList;
    }

    public Member findMemberById(long id) {
        for (Member member : members) {
            if (member.getId() == id) {
                return member;
            }
        }
        return null;
    }

    public void editMember(long id, String name, String email) {
        Member foundMember = this.findMemberById(id);
        if (foundMember == null) {
            throw new LibraryException("Member not found!");
        }
        Member.validateParams(name, email);
        foundMember.setName(name);
        foundMember.setEmail(email);
    }

    public void deleteMember(long id) {
        Member foundMember = this.findMemberById(id);
        if (foundMember == null) {
            throw new LibraryException("Member not found!");
        }
        if (!foundMember.borrowedBooks().isEmpty()) {
            throw new LibraryException("The member has borrowed a book!");
        }
        this.members.remove(foundMember);
    }
    public void  clearMembers (){
        members.clear();
    }
}
