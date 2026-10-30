package library.services;

import library.domain.Book;
import library.domain.Member;
import library.exceptions.LibraryException;
import library.repository.MemberRepository;

import java.util.List;
import java.util.Map;

public class MemberServices {
    private final MemberRepository memberRepository;

    private List<Member> withBorrowedBooks(List<Member> members) {
        Map<Long, List<Book>> borrowed = memberRepository.findBorrowedBooksByMember();
        for (Member m : members) {
            for (Book b : borrowed.getOrDefault(m.getId(), List.of())) {
                m.addBorrowedBooks(b);
            }
        }
        return members;
    }

    public MemberServices(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member addMember(String name, String email) {
        return memberRepository.insert(new Member(name, email));
    }

    public List<Member> allMembers() {
        return withBorrowedBooks(memberRepository.findAll());
    }

    public List<Member> searchMember(String text) {
        return withBorrowedBooks(memberRepository.search(text));
    }

    public Member findMemberById(long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new LibraryException("Member not found!"));
        for (Book b : memberRepository.findBorrowedBooks(member.getId())) {
            member.addBorrowedBooks(b);
        }
        return member;
    }


    public Member editMember(long id, String name, String email) {
        Member.validateParams(name, email);
        return memberRepository.update(id, new Member(id, name, email));
    }

    public void deleteMember(long id) {
        memberRepository.delete(id);
    }

}